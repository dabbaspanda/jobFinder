package com.app.gps.jobfinder.Utilities.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.gps.jobfinder.Models.Job;
import com.app.gps.jobfinder.Models.ProviderModels.GitHub;
import com.app.gps.jobfinder.Models.ProviderModels.SearchGov;
import com.app.gps.jobfinder.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Job> jobResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        jobResults = new ArrayList<>();
    }

    public List<Job> getJobs() {
        return jobResults;
    }

    public void setJobs(List<Job> jobResults) {
        this.jobResults = jobResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.job_list_2, parent, false);
        viewHolder = new JobsHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Job result = jobResults.get(position);
        GitHub gitHubResult = jobResults.get(position).getGitHub();
        SearchGov searchGovResult = jobResults.get(position).getSearchGov();
        switch (getItemViewType(position)) {
            case ITEM:
                final JobsHolder jobsHolder = (JobsHolder) holder;
                if(gitHubResult !=null){
                    jobsHolder.jobTitle.setText(gitHubResult.getTitle());
                    jobsHolder.company.setText(gitHubResult.getCompany());
                    jobsHolder.location.setText(gitHubResult.getLocation());
                    loadGlide(jobsHolder, gitHubResult.getCompany_logo());
                }
                if(searchGovResult != null){
                    jobsHolder.jobTitle.setText(searchGovResult.getPosition_title());
                    jobsHolder.company.setText(searchGovResult.getOrganization_name());
                    try {
                        jobsHolder.location.setText(searchGovResult.getLocations().getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    private void loadGlide(final JobsHolder jobsHolder, String image) {
        Glide
                .with(context)
                .load(image)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        jobsHolder.mProgress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        // image ready, hide progress now
                        jobsHolder.mProgress.setVisibility(View.GONE);
                        return false;   // return false if you want Glide to handle everything else.
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade()
                .into(jobsHolder.companyLogo);
    }

    @Override
    public int getItemCount() {
        return jobResults == null ? 0 : jobResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == jobResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }



    public void add(Job r) {
        jobResults.add(r);
        notifyItemInserted(jobResults.size() - 1);
    }

    public void addAll(List<Job> moveResults) {
        for (Job result : moveResults) {
            add(result);
        }
    }

    public void remove(Job r) {
        int position = jobResults.indexOf(r);
        if (position > -1) {
            jobResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Job());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = jobResults.size() - 1;
        Job result = getItem(position);

        if (result != null) {
            jobResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Job getItem(int position) {
        return jobResults.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class JobsHolder extends RecyclerView.ViewHolder {
        private TextView jobTitle, company, location, date;
        private ImageView companyLogo;
        private ProgressBar mProgress;

        public JobsHolder(View itemView) {
            super(itemView);

            jobTitle = (TextView) itemView.findViewById(R.id.JOB_TITLE_TXT_VIEW);
            company = (TextView) itemView.findViewById(R.id.COMAPNY_NAME_TXT_VIEW);
            location = (TextView) itemView.findViewById(R.id.LOCATION_TXT_VIEW);
            date =  (TextView) itemView.findViewById(R.id.DATE_TXT_VIEW);
            companyLogo = (ImageView) itemView.findViewById(R.id.COMPANY_LOGO);
            mProgress = (ProgressBar) itemView.findViewById(R.id.JOB_PROGRESS);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
