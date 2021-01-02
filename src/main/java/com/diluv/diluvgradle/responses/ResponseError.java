package com.diluv.diluvgradle.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A binding for Diluv API error messages. These are created by deserializing the JSON response
 * from the Diluv API when a request was not successful.
 */
public class ResponseError {
    
    /**
     * The type of error that was encountered. Ex. "Bad Request"
     */
    @Expose
    @SerializedName("type")
    private String type;
    
    /**
     * A non-localized string representing the encountered error.
     */
    @Expose
    @SerializedName("error")
    private String error;
    
    /**
     * A string containing the error message localized to English.
     */
    @Expose
    @SerializedName("message")
    private String message;
    
    /**
     * Gets the type of error that was encountered.
     * 
     * @return The type of error encountered.
     */
    public String getType () {
        
        return this.type;
    }
    
    /**
     * Gets a non-localized string representing the encountered error.
     * 
     * @return A non-localized string representing the encountered error.
     */
    public String getError () {
        
        return this.error;
    }
    
    /**
     * Gets the error message.
     * 
     * @return The error message. This is localized to English.
     */
    public String getMessage () {
        
        return this.message;
    }
}