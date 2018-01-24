package com.kyros.technologies.fieldout.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.activity.EquipmentUpdateDelete;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rohin on 13-12-2017.
 */

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;


    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView technician_equipment,customer_equipment,site_equipment,equipment_tags;
        public LinearLayout equipment_linear;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            technician_equipment=itemView.findViewById(R.id.technician_equipment);
            equipment_linear=itemView.findViewById(R.id.equipment_linear);
            customer_equipment=itemView.findViewById(R.id.customer_equipment);
            site_equipment=itemView.findViewById(R.id.site_equipment);
            equipment_tags=itemView.findViewById(R.id.equipment_tags);

        }
    }
    public EquipmentAdapter(Context mContext,ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public EquipmentAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_equipment,parent,false);

        return new EquipmentAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(EquipmentAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        String tagname=commonJobs.getTagname();
        String equipname=commonJobs.getEquipname();
        String sitename=commonJobs.getSitename();
        String customername=commonJobs.getCustomername();
        String myid=commonJobs.getMyId();
        String siteid=commonJobs.getSiteid();
        String cusid=commonJobs.getCustomerid();
        String equipid=commonJobs.getEquipid();
        JSONArray tags=commonJobs.getTaginfo();
        String customFields=commonJobs.getCustomFields();

        holder.equipment_linear.setOnClickListener(view -> {
            Intent i=new Intent(mContext, EquipmentUpdateDelete.class);
            i.putExtra("equipid",equipid);
            i.putExtra("cusid",cusid);
            i.putExtra("siteid",siteid);
            i.putExtra("myid",myid);
            i.putExtra("customername",customername);
            i.putExtra("sitename",sitename);
            i.putExtra("equipmentname",equipname);
            i.putExtra("tags",tags.toString());
            i.putExtra("customFields",customFields);
            mContext.startActivity(i);
        });

        holder.technician_equipment.setText(equipname);
        holder.customer_equipment.setText(customername);
        holder.site_equipment.setText(sitename);
        String tag=null;
        for (int i=0;i<tags.length();i++){
            JSONObject first= null;
            try {
                first = tags.getJSONObject(i);
                String tagid=first.getString("id");
                tag=first.getString("name");

            } catch (JSONException e) {
            }

        }
        if (tag!=null){
            holder.equipment_tags.setText(tag);
        }

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}