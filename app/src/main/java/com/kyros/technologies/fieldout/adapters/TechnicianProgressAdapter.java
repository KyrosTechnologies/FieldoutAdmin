package com.kyros.technologies.fieldout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rohin on 02-02-2018.
 */

public class TechnicianProgressAdapter extends RecyclerView.Adapter<TechnicianProgressAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;


    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView tech_name_progress,jobs_count_tech_all,jobs_count_tech_closed;
        public ProgressBar progressBar_tech;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            tech_name_progress=itemView.findViewById(R.id.tech_name_progress);
            jobs_count_tech_all=itemView.findViewById(R.id.jobs_count_tech_all);
            jobs_count_tech_closed=itemView.findViewById(R.id.jobs_count_tech_closed);
            progressBar_tech=itemView.findViewById(R.id.progressBar_tech);

        }
    }
    public TechnicianProgressAdapter(Context mContext,ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public TechnicianProgressAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_technician_progress_bar,parent,false);

        return new TechnicianProgressAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(TechnicianProgressAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        String techname=commonJobs.getTechnicianname();
        JSONArray counts=commonJobs.getCounts();

        holder.tech_name_progress.setText(techname);
        int all=0;
        int closed=0;
        for (int i=0;i<counts.length();i++){
            try {
                JSONObject first=counts.getJSONObject(i);
                String name=first.getString("name");
                int count=first.getInt("count");
                if (i==0){
                    holder.jobs_count_tech_all.setText(String.valueOf(count));
                    all=count;
                }else if (i==1){
                    holder.jobs_count_tech_closed.setText(String.valueOf(count));
                    closed=count;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        int progress=(closed/all)*100;
        holder.progressBar_tech.setProgress(progress);

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}