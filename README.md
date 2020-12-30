# [Diluv-Gradle](https://plugins.gradle.org/plugin/com.diluv.diluvgradle) ![Latest Version](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/com/diluv/diluvgradle/com.diluv.diluvgradle.gradle.plugin/maven-metadata.xml.svg?colorB=007ec6&label=Latest%20Version)
A Gradle plugin for uploading Gradle build artifacts directly to Diluv.

## Features

- Upload build artifacts to Diluv directly.
- Auto-detect game version from environmental context clues.
- Auto-detect supported mod loaders from environmental context clues.
- Lots of configuration options and scripting potential.

## Usage Guide
To use this plugin you must add it to your build script. This can be done using the plugins DSL or added to the classpath directly for legacy scripts.

**Plugin DSL**    
```gradle
plugins {
    id "com.diluv.diluvgradle" version "1.4.0"
}
```

**Legacy**
```gradle
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath group: 'gradle.plugin.com.diluv.diluvgradle', name: 'DiluvGradle', version: '1.2.2'
    }
}
```

The next step is to create a new task for uploading to Diluv. This task allows you to configure the upload and control when and how files are uploaded.

```groovy
import com.diluv.diluvgradle.TaskDiluvUpload

task publishDiluv (type: TaskDiluvUpload){

    // This token is used to authenticate with Diluv. It should be handled with the same
    // sensitivity and security as a password. This means you should never share this token
    // or upload it to a public location such as GitHub. These are commonly stored as secret
    // environmental variables. 
    token = findProperty('diluv_token');
    
    // This tells Diluv what project you are uploading the file to. You can find the ID for
    // any project on the Diluv Project Page. This is not considered secret or sensitive.
    projectId = findProperty('diluv_project');
    
    // Tells DiluvGradle what file to upload. This can be a Java file, a path to a file, or
    // certain tasks which produce files such as any AbstractArchiveTask.
    uploadFile = jar;
}
```

### Available Properties

| Name                             | Type   | Description                                                                                                                      |
|----------------------------------|--------|----------------------------------------------------------------------------------------------------------------------------------|
| apiUrl                           | Field  | The URL for the Diluv REST API to use. This is primarily used to debug with locally hosted instances of the API.                 |
| projectId                        | Field  | The ID of the project to upload your file to. This is a required property to set.                                                |
| token                            | Field  | The authorization token used to verify your identity with the Diluv API. This is a required property to set.                     |
| uploadFile                       | Field  | The file to upload. This can be a file instance, path to a file, or some tasks that produce a file such as "jar".                |
| failSilently                     | Field  | Enabling this option will allow the DiluvGradle plugin to fail without causing the entire build to fail.                         |
| ignoreSemVer                     | Field  | Enabling this option will disable local semantic versioning checks. This will not cause server-side checks to be disabled.       |
| detectLoaders                    | Field  | Disabling this will prevent the auto detection of mod loaders.                                                                   |
| addGameVersion(version)          | Method | Adds a compatible game version to the list of versions supported by the file.                                                    |
| setVersion(version)              | Method | Sets the version of the file itself. By default this will pull from the project.version property.                                |
| setChangelog(changelog)          | Method | Sets the change log for the file. This can be a string or a plaintext file containing the changelog info.                        |
| setReleaseType(type)             | Method | Sets the release type for the project. Accepted values are "alpha", "beta", and "release".                                       |
| addDependency(projectId)         | Method | Marks another Diluv project as a required dependency for this file.                                                              |
| addOptionalDependency(projectId) | Method | Marks another Diluv project as being recommended or having additional functionality with this file.                              |
| addIncompatibility(projectId)    | Method | Marks another Diluv project as being incompatible with this file.                                                                |
| addLoader(loader)                | Method | Marks a mod loader as being compatible with the file. Such as "forge" or "fabric".                                               |
| wasUploadSuccessful()            | Method | Returns true if the file was successfully uploaded. If false is returned the upload failed or the file hasn't been uploaded yet. |
| getUploadInfo()                  | Method | Returns an object containing various API data about the file that was uploaded. If called too early an exception will be raised. |
| getErrorInfo()                   | Method | Returns an object containing the error message from the API. If called too early an exception will be raised.                    |

#### Upload Info

| Property          | Type              | Description                                                                |
|-------------------|-------------------|----------------------------------------------------------------------------|
| status            | String            | The current status of the file. Approved, pending, etc.                    |
| lastStatusChanged | Long              | The time the status last changed.                                          |
| id                | Long              | The ID for the uploaded file.                                              |
| name              | String            | The name of the uploaded file.                                             |
| downloadURL       | String            | A download URL for the uploaded file.                                      |
| size              | Long              | The file size in bytes.                                                    |
| changelog         | String            | The changelog for the file.                                                |
| sha512            | String            | A sha512 hash of the file.                                                 |
| downloadCount     | Long              | The amount of downloads the file has. Almost always 0 for new files.       |
| releaseType       | String            | The release type of the file.                                              |
| classifier        | String            | The classifier of the file.                                                |
| createdAt         | Long              | When the file was created.                                                 |
| gameVersions      | List<GameVersion> | A list of game versions that the file supports.                            |
| gameSlug          | String            | The slug for the game that the project belongs to.                         |
| projectTypeSlug   | String            | The slug for the project type.                                             |
| projectSlug       | String            | The slug of the project.                                                   |
| uploader          | UserInfo          | The user who uploaded the file.                                            |

#### Error Info

| Property | Type   | Description                                                          |
|----------|--------|----------------------------------------------------------------------|
| type     | String | The type of error that occurred, for example an authorization error. |
| error    | String | An API error code string.                                            |
| message  | String | The error message from the API.                                      |

#### GameVersion

| Property | Type   | Description                                 |
|----------|--------|---------------------------------------------|
| version  | String | The version of the game.                    |
| type     | String | The type of release this is.                |
| released | Long   | The timestamp of when the file was created. |

### UserInfo

| Property    | Type   | Description                                   |
|-------------|--------|-----------------------------------------------|
| userId      | Long   | The id of the user.                           |
| username    | String | The name of the user.                         |
| displayName | String | The display name of the user.                 |
| avatarURL   | String | A URL that points to their profile avatar.    |
| createdAt   | Long   | The timestamp for when the user joined Diluv. |

## Development Information
This section contains information useful to those working on the plugin directly or creating their own custom versions of our plugin. If you want to just use DiluvGradle in your build pipeline you will not need to know or understand any of this.

### Local Usage
If you want to use the plugin from your local maven repo make sure you have added the mavenLocal repository to your script. Grabbing the plugin is the same as normal. To publish locally you run `./gradlew clean build publishToMavenLocal`. Local maven files can be found in the `%home%/.m2/` directory.

### Direct File Usage
In some cases you may want to use the JAR file directly in your script rather than pulling from a repository. This is generally not recommended but may be unavoidable in some circumstances. If you do this make sure you add all of our dependencies to your classpath. Using the file directly will not use our pom file and will not pull these dependencies in for you.

```groovy
buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath files { file('../../../../build/libs').listFiles()}
        classpath group: 'org.apache.httpcomponents', name: 'httpmime', version: '4.5.2'
        classpath group: 'org.apache.httpcomponents', name: 'httpclient', version: '4.5.2'
        classpath group: 'com.google.code.gson', name: 'gson', version: '2.6.2'
    }
}
```

### Examples
Many example projects can be found in the [examples]() section of this repo. These projects provide a great environment for debugging the plugin and testing out new changes. 