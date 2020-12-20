package com.diluv.diluvgradle.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestData {
    
    @Expose
    @SerializedName("version")
    private String version;
    
    @Expose
    @SerializedName("changelog")
    private String changelog;
    
    @Expose
    @SerializedName("releaseType")
    private String releaseType;
    
    @Expose
    @SerializedName("classifier")
    private String classifier;
    
    @Expose
    @SerializedName("gameVersions")
    private Collection<String> gameVersions = new HashSet<>();
    
    @Expose
    @SerializedName("loaders")
    private Collection<String> loaders = new HashSet<>();
    
    @Expose
    @SerializedName("dependencies")
    private Collection<FileProjectRelation> dependencies = new ArrayList<>();
    
    public void setVersion (String version) {
        
        this.version = version;
    }
    
    public void setChangelog (String changelog) {
        
        this.changelog = changelog;
    }
    
    public void setReleaseType (String releaseType) {
        
        this.releaseType = releaseType;
    }
    
    public void setClassifier (String classifier) {
        
        this.classifier = classifier;
    }
    
    public void setGameVersions (Collection<String> gameVersions) {
        
        this.gameVersions = gameVersions;
    }
    
    public void setLoaders (Collection<String> loaders) {
        
        this.loaders = loaders;
    }
    
    public void setDependencies (List<FileProjectRelation> dependencies) {
        
        this.dependencies = dependencies;
    }
}
