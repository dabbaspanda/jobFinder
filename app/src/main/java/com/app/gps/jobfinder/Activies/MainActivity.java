package com.app.gps.jobfinder.Activies;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.gps.jobfinder.App.Constants;
import com.app.gps.jobfinder.Models.ApiResponse;
import com.app.gps.jobfinder.Models.Job;
import com.app.gps.jobfinder.Models.ProviderModels.GitHub;
import com.app.gps.jobfinder.Models.ProviderModels.SearchGov;
import com.app.gps.jobfinder.R;
import com.app.gps.jobfinder.Utilities.Adapters.PaginationAdapter;
import com.app.gps.jobfinder.Utilities.Adapters.RecyclerTouchListener;
import com.app.gps.jobfinder.Utilities.Http.AsyncTaskCompleteListener;
import com.app.gps.jobfinder.Utilities.Http.HttpAsyncTask;
import com.app.gps.jobfinder.Utilities.PaginationScrollListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PlaceSelectionListener {

    private static final String LOG_TAG = "PlaceSelectionListener";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int REQUEST_SELECT_PLACE = 1000;
    public static RecyclerView jobsList;
    public static ProgressBar progressBar;
    PaginationAdapter adapter;
    private ArrayList<GitHub> gitHubArrayList = new ArrayList<>();
    private ArrayList<SearchGov> searchGovArrayList = new ArrayList<>();
    private ArrayList<Job> jobs = new ArrayList<>();
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    LinearLayoutManager linearLayoutManager;
    private String TAG = "MAIN_ACTIVITY";
    private String finalSearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jobsList =  findViewById(R.id.JOBS_RECYLER_VIEW);
        progressBar = findViewById(R.id.MAIN_PROGRESS);
        adapter = new PaginationAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        jobsList.setLayoutManager(linearLayoutManager);

        jobsList.setItemAnimator(new DefaultItemAnimator());

        jobsList.setAdapter(adapter);
        jobsList.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        //here we handle our URL
        jobsList.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, jobsList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(jobs.get(position).getGitHub()!= null){
                    if (!jobs.get(position).getGitHub().getUrl().isEmpty()) {
                        Intent intent = new Intent(MainActivity.this, UrlActivity.class);
                        intent.putExtra("url", gitHubArrayList.get(position).getUrl());
                        startActivity(intent);
                    }
                }else if (jobs.get(position).getSearchGov()!=null) {
                    if (!jobs.get(position).getSearchGov().getUrl().isEmpty()) {
                        Intent intent = new Intent(MainActivity.this, UrlActivity.class);
                        intent.putExtra("url", gitHubArrayList.get(position).getUrl());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchTxt) {
                Log.e("asdasd", "CHANGED" + searchTxt);
                if(searchTxt.equals(finalSearchText)){
                    gitHubArrayList.clear();
                    adapter.clear();
                }else{
                    finalSearchText = searchTxt;
                }
                loadFirstPage(searchTxt);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(LOG_TAG, "Place Selected: " + place.getName());
//        locationTextView.setText(getString(R.string.formatted_place_data, place
//                .getName(), place.getAddress(), place.getPhoneNumber(), place
//                .getWebsiteUri(), place.getRating(), place.getId()));
//        if (!TextUtils.isEmpty(place.getAttributions())){
//            attributionsTextView.setText(Html.fromHtml(place.getAttributions().toString()));
//        }
    }

    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadFirstPage(final String searchTxt) {
        HttpAsyncTask getGitHubDAta  =  new HttpAsyncTask(MainActivity.this, new AsyncTaskCompleteListener<String>() {
            @Override
            public void onTaskComplete(ApiResponse result) {
                try{
                    if(result.getObject().length() != 0){
                        gitHubArrayList =  new Gson().fromJson(String.valueOf(result.getObject()), new TypeToken<ArrayList<GitHub>>() {
                        }.getType());
                        for (int counter=0; counter< gitHubArrayList.size(); counter++){
                            Job job = new Job();
                            job.setGitHub(gitHubArrayList.get(counter));
                            jobs.add(job);
                        }
                    }
                }
                catch (Exception ex){

                }
                HttpAsyncTask getSearchGovData = new HttpAsyncTask(MainActivity.this, new AsyncTaskCompleteListener<String>() {
                    @Override
                    public void onTaskComplete(ApiResponse result) {
                        try{
                            if(result.getObject().length() != 0){
                                Type typeOfList = new TypeToken<ArrayList<SearchGov>>(){}.getType();

                                searchGovArrayList =  new Gson().fromJson(String.valueOf(result.getObject()),typeOfList);
                            }
                            for (int counter=0; counter< searchGovArrayList.size(); counter++){
                                Job job = new Job();
                                job.setSearchGov(searchGovArrayList.get(counter));
                                jobs.add(job);
                            }
                            adapter.addAll(jobs);
                        }
                        catch (Exception ex){

                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }, "get");
                getSearchGovData.execute(Constants.searchGOV(searchTxt), "get");


                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }
        }, "get");
        getGitHubDAta.execute(Constants.searchGitHub("1", searchTxt),"get");
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
        HttpAsyncTask getDAta  =  new HttpAsyncTask(this, new AsyncTaskCompleteListener<String>() {
            @Override
            public void onTaskComplete(ApiResponse result) {
                try{
                    adapter.removeLoadingFooter();
                    isLoading = false;
                    gitHubArrayList =  new Gson().fromJson(String.valueOf(result.getObject()), new TypeToken<ArrayList<GitHub>>() {
                    }.getType());
                    for (int counter=0; counter< gitHubArrayList.size(); counter++) {
                        Job job = new Job();
                        job.setGitHub(gitHubArrayList.get(counter));
                        jobs.add(job);
                    }
                    adapter.addAll(jobs);
                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                }
                catch (Exception ex){

                }
            }
        }, "get");
        getDAta.execute(Constants.searchGitHub(String.valueOf(currentPage), finalSearchText),"get");
    }


}
