package com.diluv.diluvgradle.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A POJO that defines a relationship between a file and a project hosted on Diluv. These are
 * intended to be serialized to JSON and submitted to the Diluv REST API as part of the file
 * upload request data body.
 */
public final class FileProjectRelation {
    
    /**
     * The ID of the project that the relation is for.
     */
    @Expose
    @SerializedName("projectId")
    private final Long projectId;
    
    /**
     * The type of relation being defined.
     */
    @Expose
    @SerializedName("type")
    private final RelationType type;
    
    /**
     * Creates a new project relationship.
     * 
     * @param projectId The ID of the project to create a relation with.
     * @param type The type of relation being created.
     */
    public FileProjectRelation(Long projectId, RelationType type) {
        
        this.projectId = projectId;
        this.type = type;
    }
    
    public Long getProjectId () {
        
        return this.projectId;
    }
    
    public RelationType getRelationType () {
        
        return this.type;
    }
}