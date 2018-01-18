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
import com.example.rohin.fieldoutadmin.activity.ProjectsUpdateDelete;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Rohin on 05-01-2018.
 */

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;
    private String username=null;


    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView projects_number,projects_customer_name,project_type;
        public LinearLayout projects_linear;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            projects_number=itemView.findViewById(R.id.projects_number);
            projects_linear=itemView.findViewById(R.id.projects_linear);
            projects_customer_name=itemView.findViewById(R.id.projects_customer_name);
            project_type=itemView.findViewById(R.id.project_type);

        }
    }
    public ProjectsAdapter(Context mContext, ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public ProjectsAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_projects,parent,false);

        return new ProjectsAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(ProjectsAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        final String sitename=commonJobs.getSitename();
        String cusname=commonJobs.getCustomername();
        String equipname=commonJobs.getEquipname();
        String projectno=commonJobs.getProjectno();
        String projecttype=commonJobs.getProjecttypename();
        String desc=commonJobs.getDescription();
        String projectid=commonJobs.getProjectid();
        JSONArray tags=commonJobs.getTaginfo();
        holder.projects_linear.setOnClickListener(view -> {
            Intent i=new Intent(mContext, ProjectsUpdateDelete.class);
            i.putExtra("cusname",cusname);
            i.putExtra("sitename",sitename);
            i.putExtra("equipname",equipname);
            i.putExtra("projectno",projectno);
            i.putExtra("description",desc);
            i.putExtra("projectid",projectid);
            i.putExtra("tags",tags.toString());
            mContext.startActivity(i);
        });


        holder.projects_number.setText(projectno);
        holder.project_type.setText(projecttype);
        holder.projects_customer_name.setText(cusname+" / "+sitename+" / "+equipname);

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}