package com.app.gps.jobfinder.Utilities.Http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.app.gps.jobfinder.Models.ApiResponse;
import com.app.gps.jobfinder.R;


public class HttpAsyncTask extends AsyncTask<String, Void, ApiResponse> {
    private Context context;
    private AsyncTaskCompleteListener<String> callback;
    private String type;



    public HttpAsyncTask(Context context, AsyncTaskCompleteListener<String> cb, String type) {
        this.context = context;
        this.callback = cb;
        this.type = type;
    }
    @Override
    protected void onPostExecute(ApiResponse s) {
        super.onPostExecute(s);
        callback.onTaskComplete(s);
    }

    @Override
    protected ApiResponse doInBackground(String... strings) {

        ApiResponse message = null;
        try {
            if (strings[1].equals("get"))
                message = HttpRequestHandler.SEND_GET(strings[0]);
            else if (strings[1].equals("post"))
                message = HttpRequestHandler.SEND_POST(strings[0], strings[1], strings[2]);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }




}
