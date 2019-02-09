package com.app.gps.jobfinder.Models;

import com.app.gps.jobfinder.Models.ProviderModels.GitHub;
import com.app.gps.jobfinder.Models.ProviderModels.SearchGov;


public class Job {
    private GitHub gitHub;
    private SearchGov searchGov;


    public GitHub getGitHub() {
        return gitHub;
    }

    public void setGitHub(GitHub gitHub) {
        this.gitHub = gitHub;
    }

    public SearchGov getSearchGov() {
        return searchGov;
    }

    public void setSearchGov(SearchGov searchGov) {
        this.searchGov = searchGov;
    }
}
