package com.diluv.diluvgradle;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.annotation.Nullable;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.plugins.AppliedPlugin;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

import com.diluv.diluvgradle.request.FileProjectRelation;
import com.diluv.diluvgradle.request.RelationType;
import com.diluv.diluvgradle.request.RequestData;
import com.diluv.diluvgradle.responses.ResponseError;
import com.diluv.diluvgradle.responses.ResponseUpload;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A task used to communicate with Diluv for the purpose of uploading build artifacts.
 */
public class TaskDiluvUpload extends DefaultTask {
    
    /**
     * An internal logger instance used to output status and debug information about the plugin
     * and it's usage.
     */
    private final Logger log;
    
    /**
     * A project specific instance of Gson. Used to serialize JSON objects for use with Diluv's
     * REST API.
     */
    private final Gson gson;
    
    /**
     * Represents the file upload data attached to the API call when uploading the file.
     */
    private final RequestData request;
    
    /**
     * The URL used for communicating with Diluv. This should not be changed unless you know
     * what you're doing. It's main use case is for debug, development, or advanced user
     * configurations.
     */
    public String apiURL = "https://api.diluv.com";
    
    /**
     * The ID of the project to upload to.
     */
    public String projectId;
    
    /**
     * The API token used to communicate with Diluv. Make sure you keep this public!
     */
    public String token;
    
    /**
     * The upload artifact file. This can be any object type that is resolvable by
     * {@link #resolveFile(Project, Object, File)}.
     */
    public Object uploadFile;
    
    /**
     * Allows build to continue even if the upload failed.
     */
    public boolean failSilently = false;
    
    /**
     * If enabled the plugin-side semantic version check will be ignored.
     */
    public boolean ignoreSemVer = false;
    
    /**
     * If enabled the plugin will try to define loaders based on other plugins in the project
     * environment.
     */
    public boolean detectLoaders = true;
    
    /**
     * The response from the API when the file was uploaded successfully.
     */
    @Nullable
    @Internal
    private ResponseUpload uploadInfo = null;
    
    /**
     * The response from the API when the file failed to upload.
     */
    @Nullable
    @Internal
    private ResponseError errorInfo = null;
    
    public TaskDiluvUpload() {
        
        this.log = Logging.getLogger("DiluvGradle");
        this.gson = new GsonBuilder().create();
        this.request = new RequestData();
        
        // If the build task is present make sure this task is ran after it. This is required
        // for some environments such as those with parallel tasks enabled.
        this.mustRunAfter(this.getProject().getTasks().getByName("build"));
    }
    
    /**
     * Adds a compatible game version to the file.
     * 
     * @param version The compatible game version.
     */
    public void addGameVersion (String version) {
        
        this.log.debug("Adding game version {}.", version);
        
        if (!this.request.addGameVersion(version)) {
            
            this.log.warn("The game version {} was not be applied.", version);
        }
    }
    
    /**
     * Sets the version of the file being uploaded.
     * 
     * @param version The version of the file being uploaded.
     */
    public void setVersion (String version) {
        
        if (this.request.hasVersion()) {
            
            this.log.debug("Replacing file version {} with {}.", this.request.getVersion(), version);
        }
        
        this.request.setVersion(version);
    }
    
    /**
     * Sets the changelog for the file.
     * 
     * @param changelog The changelog for the file.
     */
    public void setChangelog (String changelog) {
        
        this.request.setChangelog(changelog);
        this.log.debug("Setting changelog to: '{}'", changelog);
    }
    
    /**
     * Attempts to read a changelog from a file using the UTF-8 character set.
     * 
     * @param file The file to read the changelog from.
     */
    public void setChangelog (File file) {
        
        this.setChangelog(file, StandardCharsets.UTF_8);
    }
    
    /**
     * Attempts to read a changelog from a file using the specified character set.
     * 
     * @param file The file to read the changelog from.
     * @param charset The name of the character set to use.
     */
    public void setChangelog (File file, String charset) {
        
        this.setChangelog(file, Charset.forName(charset));
    }
    
    /**
     * Attempt to read a changelog from a file using the given character set.
     * 
     * @param file The file to read the changelog from.
     * @param charset The character set to read the file with.
     */
    public void setChangelog (File file, Charset charset) {
        
        if (file != null && file.exists()) {
            
            try {
                
                // Read the file as bytes rather than lines to preserve line terminators.
                final byte[] encoded = Files.readAllBytes(file.toPath());
                
                this.log.debug("Setting changelog from file {}. Read {} bytes.", file, encoded != null ? encoded.length : 0);
                this.setChangelog(new String(encoded, charset));
            }
            
            catch (final IOException e) {
                
                this.log.error("Failed to set changelog from file.", e);
                throw new GradleException("Failed to set changelog from file.", e);
            }
        }
        
        else {
            
            this.log.error("Could not set changelog to file {}. The file is missing or null.", file);
            throw new GradleException("Could not set changelog from file. The file was missing or null. file=" + file);
        }
    }
    
    /**
     * Sets the release type of the file being uploaded.
     * 
     * @param type The type of release.
     */
    public void setReleaseType (String type) {
        
        this.log.debug("Setting release type to {}.", type);
        this.request.setReleaseType(type);
    }
    
    /**
     * Sets the classifier type of the file being uploaded.
     * 
     * @param classifier The classifier of the file being uploaded.
     */
    public void setClassifier (String classifier) {
        
        this.log.debug("Setting classifier to {}.", classifier);
        this.request.setClassifier(classifier);
    }
    
    /**
     * Adds a required project dependency for the file.
     * 
     * @param project The project that this file requires.
     */
    public void addDependency (long project) {
        
        this.addRelation(project, RelationType.REQUIRED);
    }
    
    /**
     * Adds an optional project dependency for the file.
     * 
     * @param project The project that is optional.
     */
    public void addOptionalDependency (long project) {
        
        this.addRelation(project, RelationType.OPTIONAL);
    }
    
    /**
     * Adds an incompatibility relationship for the file.
     * 
     * @param project The project that the file is not compatible with.
     */
    public void addIncompatibility (long project) {
        
        this.addRelation(project, RelationType.INCOMPATIBLE);
    }
    
    /**
     * Adds a project relationship to the uploaded file. This determines things like
     * dependencies and incompatibilities.
     * 
     * @param project The project to add a relation with.
     * @param type The type of relation to add.
     */
    private void addRelation (long project, RelationType type) {
        
        final FileProjectRelation existingRelation = this.request.addRelation(new FileProjectRelation(project, type));
        this.log.debug("Added {} relation with project {}.", type, project);
        
        if (existingRelation != null) {
            
            this.log.warn("Overwriting relation for {} to {}, was previously set as {}.", project, type, existingRelation);
        }
    }
    
    /**
     * Adds a loader tag for the file.
     * 
     * @param loader The loader to allow.
     */
    public void addLoader (String loader) {
        
        this.log.debug("Adding loader tag {}.", loader);
        
        if (!this.request.addLoader(loader)) {
            
            this.log.warn("The loader tag {} was already applied.", loader);
        }
    }
    
    /**
     * Checks if the upload was successful or not. This is provided as a small helper for use
     * in the build script.
     * 
     * @return Whether or not the file was successfully uploaded.
     */
    public boolean wasUploadSuccessful () {
        
        return this.uploadInfo != null && this.errorInfo == null;
    }
    
    /**
     * Attempts to get the upload info for this task. If the file has not been uploaded yet an
     * exception will be raised.
     * 
     * @return If the file was uploaded successfully the upload info response will be returned.
     *         Otherwise null.
     */
    @Nullable
    public ResponseUpload getUploadInfo () {
        
        if (this.uploadInfo != null) {
            
            return this.uploadInfo;
        }
        
        else if (this.errorInfo == null) {
            
            throw new GradleException("Attempted to access upload info before file was uploaded. The info is not available at this stage!");
        }
        
        return null;
    }
    
    /**
     * Attempts to get the upload error info for this task. Attempting to use this before the
     * file has been uploaded will cause an exception to be raised.
     * 
     * @return If the file was uploaded unsuccessfully the error info will be returned.
     *         Otherwise null.
     */
    @Nullable
    public ResponseError getErrorInfo () {
        
        if (this.errorInfo != null) {
            
            return this.errorInfo;
        }
        
        else if (this.uploadInfo == null) {
            
            throw new GradleException("Attempted to access upload error info before file was uploaded. The info is not available at this stage!");
        }
        
        return null;
    }
    
    @TaskAction
    public void apply () {
        
        try {
            
            // Attempt to automatically resolve the game version if one wasn't specified.
            if (!this.request.hasGameVersion()) {
                
                this.detectGameVersionForge();
                this.detectGameVersionFabric();
            }
            
            // Check the game version again, if it's still not there the upload has failed.
            if (!this.request.hasGameVersion()) {
                
                throw new GradleException("Can not upload to Diluv. No game version specified.");
            }
            
            // Use project version from Gradle if no version was specified.
            if (this.request.getVersion() == null || this.request.getVersion().isEmpty()) {
                
                final String projectBuildVersion = this.getProject().getVersion().toString();
                
                if (projectBuildVersion != null && !projectBuildVersion.isEmpty()) {
                    
                    this.log.debug("File version will fall back to build version of {}.", projectBuildVersion);
                    this.request.setVersion(projectBuildVersion);
                }
                
                else {
                    
                    throw new GradleException("No file version was specified, and the fallback Gradle build version could not be found.");
                }
            }
            
            this.addLoaderForPlugin("net.minecraftforge.gradle", "forge");
            this.addLoaderForPlugin("fabric-loom", "fabric");
            
            // Only semantic versioning is allowed.
            if (!this.ignoreSemVer && !Constants.SEM_VER.matcher(this.request.getVersion()).matches()) {
                
                this.log.error("Project version {} is not semantic versioning compatible. The file can not be uploaded. https://semver.org", this.request.getVersion());
                throw new GradleException("Project version '" + this.request.getVersion() + "' is not semantic versioning compatible. The file can not be uploaded. https://semver.org");
            }
            
            // Set a default changelog if the dev hasn't provided one.
            if (!this.request.hasChangelog()) {
                
                this.request.setChangelog("The project has been updated to " + this.request.getVersion() + ".");
                this.log.warn("No changelog was specified. A default one will be used. This is not recommended.");
            }
            
            final File file = resolveFile(this.getProject(), this.uploadFile, null);
            
            // Ensure the file actually exists before trying to upload it.
            if (file == null || !file.exists()) {
                
                this.log.error("The upload file is missing or null. {}", this.uploadFile);
                throw new GradleException("The upload file is missing or null. " + String.valueOf(this.uploadFile));
            }
            
            try {
                
                final URI endpoint = new URI(this.getUploadEndpoint());
                
                try {
                    
                    this.upload(endpoint, file);
                }
                
                catch (final IOException e) {
                    
                    this.log.error("Failed to upload the file!", e);
                    throw new GradleException("Failed to upload the file!", e);
                }
            }
            
            catch (final URISyntaxException e) {
                
                this.log.error("Invalid endpoint URI!", e);
                throw new GradleException("Invalid endpoint URI!", e);
            }
        }
        
        catch (final Exception e) {
            
            if (this.failSilently) {
                
                this.log.info("Failed to upload to Diluv. Check logs for more info.");
                this.log.error("Diluv upload failed silently.", e);
            }
            
            else {
                
                throw e;
            }
        }
    }
    
    /**
     * Uploads a file using the provided configuration.
     * 
     * @param endpoint The upload endpoint.
     * @param file The file to upload.
     * @throws IOException Whenever something goes wrong wit uploading the file.
     */
    public void upload (URI endpoint, File file) throws IOException {
        
        this.log.debug("Uploading {} to {}.", file.getPath(), this.getUploadEndpoint());
        
        final HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();
        final HttpPost post = new HttpPost(endpoint);
        
        post.addHeader("Authorization", "Bearer " + this.token);
        
        final MultipartEntityBuilder form = MultipartEntityBuilder.create();
        form.addBinaryBody("file", file);
        form.addTextBody("filename", file.getName());
        form.addTextBody("data", this.gson.toJson(this.request), ContentType.APPLICATION_JSON);
        post.setEntity(form.build());
        
        try {
            
            final HttpResponse response = client.execute(post);
            final int status = response.getStatusLine().getStatusCode();
            final String responseBody = EntityUtils.toString(response.getEntity());
            
            this.log.debug("Diluv Response Code: {}", status);
            this.log.debug("Diluv Response Body: {}", responseBody);
            
            if (status == 200) {
                
                this.uploadInfo = this.gson.fromJson(responseBody, ResponseUpload.class);
                this.log.lifecycle("Sucessfully uploaded {} to {} as file id {}.", file.getName(), this.projectId, this.uploadInfo.getId());
            }
            
            else {
                
                this.errorInfo = this.gson.fromJson(responseBody, ResponseError.class);
                this.log.error("Upload failed! Status: {} Reason: {}", status, this.errorInfo.getMessage());
                throw new GradleException("Upload failed! Status: " + status + " Reason: " + this.errorInfo.getMessage());
            }
        }
        
        catch (final IOException e) {
            
            this.log.error("Failure to upload file!", e);
            throw e;
        }
    }
    
    /**
     * Provides the upload API endpoint to use.
     * 
     * @return The upload API endpoint.
     */
    private String getUploadEndpoint () {
        
        return this.apiURL + "/v1/projects/" + this.projectId + "/files";
    }
    
    /**
     * Attempts to resolve a file using an arbitrary object provided by a user defined gradle
     * task.
     * 
     * @param project The project instance. This is used as a last resort to resolve the file
     *        using Gradle's built in handling.
     * @param in The arbitrary input object from the user.
     * @param fallback A fallback file to use. This can be null.
     * @return A file handle for the resolved input. If the input can not be resolved this will
     *         be null or the fallback.
     */
    @Nullable
    private static File resolveFile (Project project, Object in, @Nullable File fallback) {
        
        // If input or project is null shortcut to the fallback.
        if (in == null || project == null) {
            
            return fallback;
        }
        
        // If the file is a Java file handle no additional handling is needed.
        else if (in instanceof File) {
            
            return (File) in;
        }
        
        // Grabs the file from an archive task. Allows build scripts to do things like the jar
        // task directly.
        else if (in instanceof AbstractArchiveTask) {
            
            return ((AbstractArchiveTask) in).getArchivePath();
        }
        
        // Fallback to Gradle's built in file resolution mechanics.
        return project.file(in);
    }
    
    /**
     * Attempts to detect the game version by detecting ForgeGradle data in the build
     * environment.
     */
    private void detectGameVersionForge () {
        
        try {
            
            final ExtraPropertiesExtension extraProps = this.getProject().getExtensions().getExtraProperties();
            
            // ForgeGradle will store the game version here.
            // https://github.com/MinecraftForge/ForgeGradle/blob/9252ffe1fa5c2acf133f35d169ba4ffc84e6a9fd/src/userdev/java/net/minecraftforge/gradle/userdev/MinecraftUserRepo.java#L179
            if (extraProps.has("MC_VERSION")) {
                
                final String forgeGameVersion = extraProps.get("MC_VERSION").toString();
                
                if (forgeGameVersion != null && !forgeGameVersion.isEmpty()) {
                    
                    this.log.debug("Detected fallback game version {} from ForgeGradle.", forgeGameVersion);
                    this.addGameVersion(forgeGameVersion);
                }
            }
        }
        
        catch (final Exception e) {
            
            this.log.debug("Failed to detect ForgeGradle game version.", e);
        }
    }
    
    /**
     * Attempts to detect the game version by detecting LoomGradle data in the build
     * environment.
     */
    private void detectGameVersionFabric () {
        
        // Loom/Fabric Gradle detection.
        try {
            
            // Using reflection because loom isn't always available.
            final Class<?> loomType = Class.forName("net.fabricmc.loom.LoomGradleExtension");
            final Method getProvider = loomType.getMethod("getMinecraftProvider");
            
            final Class<?> minecraftProvider = Class.forName("net.fabricmc.loom.providers.MinecraftProvider");
            final Method getVersion = minecraftProvider.getMethod("getMinecraftVersion");
            
            final Object loomExt = this.getProject().getExtensions().getByType(loomType);
            final Object loomProvider = getProvider.invoke(loomExt);
            final Object loomVersion = getVersion.invoke(loomProvider);
            
            final String loomGameVersion = loomVersion.toString();
            
            if (loomGameVersion != null && !loomGameVersion.isEmpty()) {
                
                this.log.debug("Detected fallback game version {} from Loom.", loomGameVersion);
                this.addGameVersion(loomGameVersion);
            }
        }
        
        catch (final Exception e) {
            
            this.log.debug("Failed to detect Loom game version.", e);
        }
    }
    
    /**
     * Applies a mod loader automatically if a plugin with the specified name has been applied.
     * 
     * @param pluginName The plugin to search for.
     * @param loaderName The mod loader to apply.
     */
    private void addLoaderForPlugin (String pluginName, String loaderName) {
        
        if (this.detectLoaders) {
            
            try {
                
                final AppliedPlugin plugin = this.getProject().getPluginManager().findPlugin(pluginName);
                
                if (plugin != null) {
                    
                    this.addLoader(loaderName);
                    this.log.debug("Applying loader {} because plugin {} was found.", loaderName, pluginName);
                }
                
                else {
                    
                    this.log.debug("Could not automatically apply loader {} because plugin {} has not been applied.", loaderName, pluginName);
                }
            }
            
            catch (final Exception e) {
                
                this.log.debug("Failed to detect plugin {}.", pluginName, e);
            }
        }
    }
}