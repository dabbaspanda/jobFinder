package com.app.gps.jobfinder.Utilities.Http;


import com.app.gps.jobfinder.Models.ApiResponse;

public interface AsyncTaskCompleteListener<S> {
    public void onTaskComplete(ApiResponse result);
}
