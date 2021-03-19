package com.diluv.diluvgradle.request;

import com.google.gson.annotations.SerializedName;

/**
 * All valid options for the types of relations a file can have with other projects.
 */
public enum RelationType {
    
    /**
     * The file requires an instance of the project to work.
     */
    @SerializedName("required")
    REQUIRED,
    
    /**
     * The file has additional functionality when the project is present.
     */
    @SerializedName("optional")
    OPTIONAL,
    
    /**
     * The file is not compatible with the project and will not work when both are used.
     */
    @SerializedName("incompatible")
    INCOMPATIBLE;
}