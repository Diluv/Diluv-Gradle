package com.diluv.diluvgradle.responses;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class defines a POJO that represents the API response for files that have been
 * successfully uploaded to Diluv.
 */
public class ResponseUpload {
    
    /**
     * The upload status for the file. Barring rare exceptions this will always be PENDING for
     * newly uploaded files.
     */
    @Expose
    @SerializedName("status")
    private String status;
    
    /**
     * The time stamp of when the file status was last changed.
     */
    @Expose
    @SerializedName("lastStatusChanged")
    private Long lastStatusChanged;
    
    /**
     * An ID assigned to the file by Diluv.
     */
    @Expose
    @SerializedName("id")
    private Long id;
    
    /**
     * The name of the file that was uploaded.
     */
    @Expose
    @SerializedName("name")
    private String name;
    
    /**
     * A URL that can be used to download the file from the CDN.
     */
    @Expose
    @SerializedName("downloadURL")
    private String downloadURL;
    
    /**
     * The size of the uploaded file in bytes.
     */
    @Expose
    @SerializedName("size")
    private Long size;
    
    /**
     * The changelog of the uploaded file.
     */
    @Expose
    @SerializedName("changelog")
    private String changelog;
    
    /**
     * The SHA512 hash of the file uploaded to Diluv.
     */
    @Expose
    @SerializedName("sha512")
    private String sha512;
    
    /**
     * The download count for the file. Barring rare exceptions this will always be 0 for newly
     * uploaded files.
     */
    @Expose
    @SerializedName("downloads")
    private Long downloadCount;
    
    /**
     * The release type for the file.
     */
    @Expose
    @SerializedName("releaseType")
    private String releaseType;
    
    /**
     * The type of file that was uploaded.
     */
    @Expose
    @SerializedName("classifier")
    private String classifier;
    
    /**
     * The date the file entry was created.
     */
    @Expose
    @SerializedName("createdAt")
    private Long createdAt;
    
    /**
     * A list of game versions that the file supports.
     */
    @Expose
    @SerializedName("gameVersions")
    private List<GameVersion> gameVersions;
    
    /**
     * The slug of the game this file belongs to.
     */
    @Expose
    @SerializedName("gameSlug")
    private String gameSlug;
    
    /**
     * The slug of the project type this belongs to.
     */
    @Expose
    @SerializedName("projectTypeSlug")
    private String projectTypeSlug;
    
    /**
     * The slug of the project this belongs to.
     */
    @Expose
    @SerializedName("projectSlug")
    private String projectSlug;
    
    /**
     * The user who uploaded the file.
     */
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
        private String displayName;
        
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
        
        public String getDisplayName () {
            
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