package com.diluv.diluvgradle.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestData {
    
    @Expose
    @SerializedName("version")
    private String version;
    
    @Expose
    @SerializedName("changelog")
    private String changelog = "";
    
    @Expose
    @SerializedName("releaseType")
    private String releaseType = "alpha";
    
    @Expose
    @SerializedName("classifier")
    private String classifier = "binary";
    
    @Expose
    @SerializedName("gameVersions")
    private final Collection<String> gameVersions = new HashSet<>();
    
    @Expose
    @SerializedName("loaders")
    private final Collection<String> loaders = new HashSet<>();
    
    @Expose
    @SerializedName("dependencies")
    private final List<FileProjectRelation> dependencies = new ArrayList<>();
    
    public void setVersion (String version) {
        
        this.version = version;
    }
    
    public String getVersion () {
        
        return this.version;
    }
    
    public boolean hasVersion () {
        
        return this.version != null && !this.version.isEmpty();
    }
    
    public void setChangelog (String changelog) {
        
        this.changelog = changelog;
    }
    
    public boolean hasChangelog () {
        
        return this.changelog != null && !this.changelog.isEmpty();
    }
    
    public void setReleaseType (String releaseType) {
        
        this.releaseType = releaseType;
    }
    
    public void setClassifier (String classifier) {
        
        this.classifier = classifier;
    }
    
    public boolean addGameVersion (String gameVersion) {
        
        return this.gameVersions.add(gameVersion);
    }
    
    public boolean hasGameVersion () {
        
        return !this.gameVersions.isEmpty();
    }
    
    public boolean addLoader (String loader) {
        
        return this.loaders.add(loader);
    }
    
    public boolean hasGameLoader () {
        
        return !this.loaders.isEmpty();
    }
    
    @Nullable
    public FileProjectRelation addRelation (FileProjectRelation relation) {
        
        final ListIterator<FileProjectRelation> iter = this.dependencies.listIterator();
        
        while (iter.hasNext()) {
            
            final FileProjectRelation existingRelation = iter.next();
            
            if (existingRelation.getProjectId().equals(relation.getProjectId())) {
                
                iter.remove();
                iter.add(relation);
                return existingRelation;
            }
        }
        
        return null;
    }
}
