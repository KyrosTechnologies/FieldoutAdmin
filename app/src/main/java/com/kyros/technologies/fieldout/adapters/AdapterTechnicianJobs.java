package com.kyros.technologies.fieldout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Rohin on 19-01-2018.
 */

public class AdapterTechnicianJobs extends RecyclerView.Adapter<AdapterTechnicianJobs.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;

    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView tech_my_id,tech_job_status,tech_priority,tech_jobs_customer_name,tech_phone,tech_job_type;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            tech_my_id=itemView.findViewById(R.id.tech_my_id);
            tech_job_status=itemView.findViewById(R.id.tech_job_status);
            tech_priority=itemView.findViewById(R.id.tech_priority);
            tech_jobs_customer_name=itemView.findViewById(R.id.tech_jobs_customer_name);
            tech_phone=itemView.findViewById(R.id.tech_phone);
            tech_job_type=itemView.findViewById(R.id.tech_job_type);

        }
    }
    public AdapterTechnicianJobs(Context mContext, ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public AdapterTechnicianJobs.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_technician_jobs_list,parent,false);

        return new AdapterTechnicianJobs.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(AdapterTechnicianJobs.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        String cusname=commonJobs.getCustomername();
        String sitename=commonJobs.getSitename();
        String equipname=commonJobs.getEquipname();
        String myid=commonJobs.getMyId();
        String status=commonJobs.getStatus();
        String priority=commonJobs.getPriority();
        String phone=commonJobs.getPhone();
        String jobtype=commonJobs.getJobTypeName();

        holder.tech_my_id.setText(myid);
        holder.tech_job_status.setText(status);
        holder.tech_priority.setText(priority);
        holder.tech_jobs_customer_name.setText(cusname+"/"+sitename+"/"+equipname);
        holder.tech_phone.setText(phone);
        holder.tech_job_type.setText(jobtype);

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}