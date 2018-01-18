package com.example.rohin.fieldoutadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.activity.ActivityJobsSchedule;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Rohin on 14-12-2017.
 */

public class JobsToScheduleAdapter extends RecyclerView.Adapter<JobsToScheduleAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;

    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView to_my_id,to_customer_name,to_priority,to_phone,to_mobile,to_job_type;
        public LinearLayout to_jobs_linear;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            to_my_id=itemView.findViewById(R.id.to_my_id);
            to_jobs_linear=itemView.findViewById(R.id.to_jobs_linear);
            to_customer_name=itemView.findViewById(R.id.to_customer_name);
            to_priority=itemView.findViewById(R.id.to_priority);
            to_phone=itemView.findViewById(R.id.to_phone);
            to_mobile=itemView.findViewById(R.id.to_mobile);
            to_job_type=itemView.findViewById(R.id.to_job_type);

        }
    }
    public JobsToScheduleAdapter(Context mContext, ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public JobsToScheduleAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_jobs_to_schedule,parent,false);

        return new JobsToScheduleAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(JobsToScheduleAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        String cusname=commonJobs.getCustomername();
        String myid=commonJobs.getMyId();
        String priority=commonJobs.getPriority();
        String phone=commonJobs.getPhone();
        String mobile=commonJobs.getMobilenum();
        String jobtype=commonJobs.getJobTypeName();
        String address=commonJobs.getAddress();
        String latlng=commonJobs.getLatlng();
        String contactname=commonJobs.getContactname();
        String email=commonJobs.getEmail();
        String desc=commonJobs.getDescription();
        String jobid=commonJobs.getJobid();

        holder.to_jobs_linear.setOnClickListener(view -> {
            Intent i=new Intent(mContext, ActivityJobsSchedule.class);
            i.putExtra("jobtypename",jobtype);
            i.putExtra("jobid",jobid);
            mContext.startActivity(i);
        });

        holder.to_my_id.setText(myid);
        holder.to_customer_name.setText(cusname);
        holder.to_priority.setText(priority);
        holder.to_phone.setText(phone);
        holder.to_mobile.setText(mobile);
        holder.to_job_type.setText(jobtype);

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}