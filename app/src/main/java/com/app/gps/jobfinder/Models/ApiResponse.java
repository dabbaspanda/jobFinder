package com.app.gps.jobfinder.Models;

import org.json.JSONArray;


public class ApiResponse {
    private String status;
    private String message;
    private JSONArray object;

    public ApiResponse(String status, JSONArray object) {
        this.status = status;
        this.object = object;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONArray getObject() {
        return object;
    }

    public void setObject(JSONArray object) {
        this.object = object;
    }
}
