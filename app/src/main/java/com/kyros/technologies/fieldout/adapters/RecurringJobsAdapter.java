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
 * Created by Rohin on 31-01-2018.
 */

public class RecurringJobsAdapter extends RecyclerView.Adapter<RecurringJobsAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;


    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView technician_name,recurring_phone,recurring_created_at,recurring_from,recurring_to;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            technician_name=itemView.findViewById(R.id.technician_name);
            recurring_phone=itemView.findViewById(R.id.recurring_phone);
            recurring_created_at=itemView.findViewById(R.id.recurring_created_at);
            recurring_from=itemView.findViewById(R.id.recurring_from);
            recurring_to=itemView.findViewById(R.id.recurring_to);

        }
    }
    public RecurringJobsAdapter(Context mContext,ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public RecurringJobsAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recurring_jobs,parent,false);

        return new RecurringJobsAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(RecurringJobsAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        String phone=commonJobs.getPhone();
        String techname=commonJobs.getTechnicianname();
        String createdat=commonJobs.getCreatedat();
        String startdate=commonJobs.getStartdate();
        String enddate=commonJobs.getEnddate();

        holder.technician_name.setText(techname);
        holder.recurring_phone.setText(phone);
        holder.recurring_created_at.setText(createdat);
        holder.recurring_from.setText(startdate);
        holder.recurring_to.setText(enddate);


    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}