package com.app.gps.jobfinder.App;

public class Constants {
    public static String GITHUB_PROVIDER = "https://jobs.github.com/";
    public static String SEARCH_GOV_PROVIDER = "https://jobs.search.gov/";


    public static String searchGitHub(String page, String key){
        return GITHUB_PROVIDER + "/positions.json?page="+page+"&search="+ key;
    }

    public static String searchGOV(String key){
        return SEARCH_GOV_PROVIDER + "jobs/search.json?query="+key;
    }

}
