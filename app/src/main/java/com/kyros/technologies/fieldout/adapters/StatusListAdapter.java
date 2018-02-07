package com.kyros.technologies.fieldout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Rohin on 07-02-2018.
 */

public class StatusListAdapter extends RecyclerView.Adapter<StatusListAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;

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
    public StatusListAdapter(Context mContext, ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public StatusListAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_frag_month_jobs,parent,false);

        return new StatusListAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(StatusListAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        final String sitename=commonJobs.getSitename();
        String cusname=commonJobs.getCustomername();
        String equipname=commonJobs.getEquipname();
        String myid=commonJobs.getMyId();
        String status=commonJobs.getStatus();
        String address=commonJobs.getGlobalAddress();
        String jobtype=commonJobs.getJobTypeName();
        String compaddress=commonJobs.getComplementAddress();
        String startdate=commonJobs.getScheduledBeginDate();
        String enddate=commonJobs.getScheduleenddate();
        String priority=commonJobs.getPriority();
        String contactname=commonJobs.getContactname();
        String firstname=commonJobs.getFirstname();
        String lastname=commonJobs.getLastname();
        String mobile=commonJobs.getMobilenum();
        String userjobid=commonJobs.getUserJobId();
        String phone=commonJobs.getPhone();
        String email=commonJobs.getEmail();
        String desc=commonJobs.getDescription();
        String latlng=commonJobs.getLatlng();
        String jobid=commonJobs.getJobid();
        String date=commonJobs.getSchedulingdate();
        JSONArray tags=commonJobs.getTaginfo();

        holder.job_my_id.setText(myid);
        holder.job_status.setText(status);
        holder.jobs_customer_name.setText(cusname+" / "+sitename+" / "+equipname);
        holder.job_tech.setText(jobtype);
        holder.job_from.setText(startdate);
        holder.job_to.setText(enddate);

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}