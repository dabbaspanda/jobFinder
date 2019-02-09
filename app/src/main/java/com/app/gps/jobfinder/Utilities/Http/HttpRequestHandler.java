package com.app.gps.jobfinder.Utilities.Http;


import android.util.Log;

import com.app.gps.jobfinder.Models.ApiResponse;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpRequestHandler {
    static ApiResponse apiResponse = null;

    public static ApiResponse SEND_POST(String url, String json, String httpType) throws Exception {
        URL obj = new URL(url);
        byte[] post_data = json.getBytes(StandardCharsets.UTF_8);
        int post_data_length = post_data.length;
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(httpType.toUpperCase());
        con.setRequestProperty("Content-type", "application/json");
        con.setRequestProperty("charset", "utf-8");
        con.setRequestProperty("Content-Length", Integer.toString(post_data_length));
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true); //want to take response from server
        DataOutputStream wr = new DataOutputStream(con.getOutputStream()); //write on request body
        wr.writeBytes(json); //here to encode to bytes
        wr.flush(); //end of bytes
        wr.close();
        int responseCode = con.getResponseCode(); //ex:200 ok
        Log.e("responseCode", String.valueOf(responseCode));
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())); //to read response
        String inputLine;
        StringBuffer response = new StringBuffer(); //to save response as block to read

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine); //keep reading response
        }
        in.close(); //close response reader
        //print result
        //ready to read

        return getResponse(response.toString(), String.valueOf(responseCode));
    }

    public static ApiResponse SEND_GET(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        InputStream is = null;
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        String inputLine;
        StringBuffer response = new StringBuffer(); //to save response as block to read
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine); //keep reading response
        }
        return getResponse(response.toString(), String.valueOf(responseCode));
    }

    private static ApiResponse getResponse(String json, String status) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        return new ApiResponse(status, jsonArray);

    }
}