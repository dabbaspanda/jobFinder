package com.app.gps.jobfinder.Models.ProviderModels;

import org.json.JSONArray;

import java.net.URL;

public class SearchGov {

    private String id;
    private String position_title;
    private String organization_name;
    private String rate_interval_code;
    private String minimum;
    private String maximum;
    private URL start_date;
    private JSONArray locations;
    private String url;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition_title() {
        return position_title;
    }

    public void setPosition_title(String position_title) {
        this.position_title = position_title;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getRate_interval_code() {
        return rate_interval_code;
    }

    public void setRate_interval_code(String rate_interval_code) {
        this.rate_interval_code = rate_interval_code;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    public URL getStart_date() {
        return start_date;
    }

    public void setStart_date(URL start_date) {
        this.start_date = start_date;
    }

    public JSONArray getLocations() {
        return locations;
    }

    public void setLocations(JSONArray locations) {
        this.locations = locations;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
