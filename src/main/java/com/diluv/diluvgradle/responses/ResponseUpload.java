package com.diluv.diluvgradle.responses;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUpload {
    
    @Expose
    @SerializedName("status")
    private String status;
    
    @Expose
    @SerializedName("lastStatusChanged")
    private Long lastStatusChanged;
    
    @Expose
    @SerializedName("id")
    private Long id;
    
    @Expose
    @SerializedName("name")
    private String name;
    
    @Expose
    @SerializedName("downloadURL")
    private String downloadURL;
    
    @Expose
    @SerializedName("size")
    private Long size;
    
    @Expose
    @SerializedName("changelog")
    private String changelog;
    
    @Expose
    @SerializedName("sha512")
    private String sha512;
    
    @Expose
    @SerializedName("downloads")
    private Long downloadCount;
    
    @Expose
    @SerializedName("releaseType")
    private String releaseType;
    
    @Expose
    @SerializedName("classifier")
    private String classifier;
    
    @Expose
    @SerializedName("createdAt")
    private Long createdAt;
    
    @Expose
    @SerializedName("dependencies")
    private List<Long> dependencies;
    
    @Expose
    @SerializedName("gameVersions")
    private List<GameVersion> gameVersions;
    
    @Expose
    @SerializedName("gameSlug")
    private String gameSlug;
    
    @Expose
    @SerializedName("projectTypeSlug")
    private String projectTypeSlug;
    
    @Expose
    @SerializedName("projectSlug")
    private String projectSlug;
    
    @Expose
    @SerializedName("user")
    private UserInfo uploader;
    
    public String getStatus () {
        
        return this.status;
    }
    
    public Long getLastStatusChanged () {
        
        return this.lastStatusChanged;
    }
    
    public Long getId () {
        
        return this.id;
    }
    
    public String getName () {
        
        return this.name;
    }
    
    public String getDownloadURL () {
        
        return this.downloadURL;
    }
    
    public Long getSize () {
        
        return this.size;
    }
    
    public String getChangelog () {
        
        return this.changelog;
    }
    
    public String getSha512 () {
        
        return this.sha512;
    }
    
    public Long getDownloadCount () {
        
        return this.downloadCount;
    }
    
    public String getReleaseType () {
        
        return this.releaseType;
    }
    
    public String getClassifier () {
        
        return this.classifier;
    }
    
    public Long getCreatedAt () {
        
        return this.createdAt;
    }
    
    public List<Long> getDependencies () {
        
        return this.dependencies;
    }
    
    public List<GameVersion> getGameVersions () {
        
        return this.gameVersions;
    }
    
    public String getGameSlug () {
        
        return this.gameSlug;
    }
    
    public String getProjectTypeSlug () {
        
        return this.projectTypeSlug;
    }
    
    public String getProjectSlug () {
        
        return this.projectSlug;
    }
    
    public UserInfo getUploader () {
        
        return this.uploader;
    }
    
    public static class UserInfo {
        
        @Expose
        @SerializedName("userId")
        private Long userId;
        
        @Expose
        @SerializedName("username")
        private String username;
        
        @Expose
        @SerializedName("displayName")
        private Long displayName;
        
        @Expose
        @SerializedName("avatarURL")
        private String avatarURL;
        
        @Expose
        @SerializedName("createdAt")
        private Long createdAt;
        
        public Long getUserId () {
            
            return this.userId;
        }
        
        public String getUsername () {
            
            return this.username;
        }
        
        public Long getDisplayName () {
            
            return this.displayName;
        }
        
        public String getAvatarURL () {
            
            return this.avatarURL;
        }
        
        public Long getCreatedAt () {
            
            return this.createdAt;
        }
    }
    
    public static class GameVersion {
        
        @Expose
        @SerializedName("version")
        private String version;
        
        @Expose
        @SerializedName("type")
        private String type;
        
        @Expose
        @SerializedName("released")
        private long released;
        
        public String getVersion () {
            
            return this.version;
        }
        
        public String getType () {
            
            return this.type;
        }
        
        public Long getReleaseDate () {
            
            return this.released;
        }
    }
}