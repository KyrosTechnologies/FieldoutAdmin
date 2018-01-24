package com.kyros.technologies.fieldout.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.activity.ActivitiesUpdateDelete;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Rohin on 13-12-2017.
 */

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;


    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView technician_activity,activity_name,activity_from,activity_to,activity_note;
        public LinearLayout activities_linear;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            technician_activity=itemView.findViewById(R.id.technician_activity);
            activities_linear=itemView.findViewById(R.id.activities_linear);
            activity_name=itemView.findViewById(R.id.activity_name);
            activity_from=itemView.findViewById(R.id.activity_from);
            activity_to=itemView.findViewById(R.id.activity_to);
            activity_note=itemView.findViewById(R.id.activity_note);

        }
    }
    public ActivitiesAdapter(Context mContext,ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public ActivitiesAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activities,parent,false);

        return new ActivitiesAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(ActivitiesAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        String nmactivity=commonJobs.getActivitytypename();
        String noteactivity=commonJobs.getNoteactivity();
        String dtstart=commonJobs.getDtstart();
        String dtend=commonJobs.getDtend();
        String username=commonJobs.getTechnicianname();
        String activitiesid=commonJobs.getActivitiesid();

        holder.activities_linear.setOnClickListener(view -> {
            Intent i=new Intent(mContext,ActivitiesUpdateDelete.class);
            store.putTechSpinner(username);
            i.putExtra("activitiesid",activitiesid);
            Log.e("Activities Id"," : "+activitiesid);
            store.putActivityTypeSpinner(nmactivity);
            i.putExtra("dtstart",dtstart);
            i.putExtra("dtend",dtend);
            i.putExtra("noteactivity",noteactivity);
            mContext.startActivity(i);
        });

        holder.technician_activity.setText(username);
        holder.activity_name.setText(nmactivity);
        holder.activity_from.setText(dtstart);
        holder.activity_to.setText(dtend);
        holder.activity_note.setText(noteactivity);

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}