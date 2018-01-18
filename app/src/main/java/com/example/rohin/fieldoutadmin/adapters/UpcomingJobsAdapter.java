package com.example.rohin.fieldoutadmin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Rohin on 14-12-2017.
 */

public class UpcomingJobsAdapter extends RecyclerView.Adapter<UpcomingJobsAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;
    private String username=null;


    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView job_my_id,job_status,jobs_customer_name,job_tech,job_from,job_to;
        public LinearLayout month_jobs_linear;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            job_my_id=itemView.findViewById(R.id.job_my_id);
            month_jobs_linear=itemView.findViewById(R.id.month_jobs_linear);
            job_status=itemView.findViewById(R.id.job_status);
            jobs_customer_name=itemView.findViewById(R.id.jobs_customer_name);
            job_tech=itemView.findViewById(R.id.job_tech);
            job_from=itemView.findViewById(R.id.job_from);
            job_to=itemView.findViewById(R.id.job_to);

        }
    }
    public UpcomingJobsAdapter(Context mContext, ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public UpcomingJobsAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_frag_month_jobs,parent,false);

        return new UpcomingJobsAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(UpcomingJobsAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        final String sitename=commonJobs.getSitename();
        String cusname=commonJobs.getCustomername();
        String equipname=commonJobs.getEquipname();
        String myid=commonJobs.getMyId();
        String status=commonJobs.getStatus();
        String address=commonJobs.getGlobalAddress();
        String jobtype=commonJobs.getJobTypeName();
        username=store.getUsername();
        String startdate=commonJobs.getScheduledBeginDate();
        String enddate=commonJobs.getScheduleenddate();

        holder.job_my_id.setText(myid);
        holder.job_status.setText(status);
        holder.jobs_customer_name.setText(cusname+" / "+sitename+" / "+equipname);
        holder.job_tech.setText(username);
        holder.job_from.setText(startdate);
        holder.job_to.setText(enddate);

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}