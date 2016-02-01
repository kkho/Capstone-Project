package com.awesome.kkho.socialme.model;

import java.io.Serializable;

/**
 * Created by kkho on 24.01.2016.
 */
public class ErrorResponse implements Serializable {
    private String status;
    private String error;
    private String description;
    private String location;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
