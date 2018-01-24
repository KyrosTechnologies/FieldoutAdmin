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
import com.kyros.technologies.fieldout.activity.ResourcesUpdateDelete;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Rohin on 13-12-2017.
 */

public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;

    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView technician_resources,resource_name,resource_from,resource_to,resource_returned;
        public LinearLayout resources_linear;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            technician_resources=itemView.findViewById(R.id.technician_resources);
            resources_linear=itemView.findViewById(R.id.resources_linear);
            resource_name=itemView.findViewById(R.id.resource_name);
            resource_from=itemView.findViewById(R.id.resource_from);
            resource_to=itemView.findViewById(R.id.resource_to);
            resource_returned=itemView.findViewById(R.id.resource_returned);

        }
    }
    public ResourcesAdapter(Context mContext,ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public ResourcesAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_resources,parent,false);

        return new ResourcesAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(ResourcesAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        String dtstart=commonJobs.getDtstart();
        String dtend=commonJobs.getDtend();
        String username=commonJobs.getResourceusername();
        String resourcename=commonJobs.getResourcename();
        //String dtreturned=commonJobs.getNoteactivity();
        String noteactivity=commonJobs.getNoteactivity();
        String resourceid=commonJobs.getResourceid();
        String idToolsAndResurces=commonJobs.getIdtoolsresources();

        holder.resources_linear.setOnClickListener(view -> {
            Intent i=new Intent(mContext, ResourcesUpdateDelete.class);
            store.putTechSpinner(username);
            store.putResourceNameSpinner(resourcename);
            Log.e("Delete Id Adapter"," "+resourceid);
            i.putExtra("resourcesid",resourceid);
            i.putExtra("dtstart",dtstart);
            i.putExtra("dtend",dtend);
            i.putExtra("noteactivity",noteactivity);
            mContext.startActivity(i);
        });

        holder.technician_resources.setText(username);
        holder.resource_name.setText(resourcename);
        holder.resource_from.setText(dtstart);
        holder.resource_to.setText(dtend);
        holder.resource_returned.setText(noteactivity);

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}