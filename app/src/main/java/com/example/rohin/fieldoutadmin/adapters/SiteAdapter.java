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
import com.example.rohin.fieldoutadmin.activity.SiteDetails;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rohin on 13-12-2017.
 */

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;


    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView technician_site,customer_site_name,contact_site_name,site_phone,site_tags;
        public LinearLayout site_linear;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            technician_site=itemView.findViewById(R.id.technician_site);
            site_linear=itemView.findViewById(R.id.site_linear);
            customer_site_name=itemView.findViewById(R.id.customer_site_name);
            contact_site_name=itemView.findViewById(R.id.contact_site_name);
            site_phone=itemView.findViewById(R.id.site_phone);
            site_tags=itemView.findViewById(R.id.site_tags);

        }
    }
    public SiteAdapter(Context mContext,ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public SiteAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sites,parent,false);

        return new SiteAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(SiteAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        String tagname=commonJobs.getTagname();
        String siteid=commonJobs.getSiteid();
        String mobile=commonJobs.getMobilenum();
        String phone=commonJobs.getPhone();
        String sitename=commonJobs.getSitename();
        String address=commonJobs.getAddress();
        String compaddress=commonJobs.getComplementAddress();
        String myid=commonJobs.getMyId();
        String cusid=commonJobs.getCustomerid();
        String fax=commonJobs.getFax();
        String email=commonJobs.getEmail();
        String customername=commonJobs.getCustomername();
        String firstname=commonJobs.getFirstname();
        String lastname=commonJobs.getLastname();
        String latlng=commonJobs.getLatlng();
        JSONArray tags=commonJobs.getTaginfo();

        holder.site_linear.setOnClickListener(view -> {
            Intent i=new Intent(mContext, SiteDetails.class);
            i.putExtra("siteid",siteid);
            i.putExtra("customerid",cusid);
            i.putExtra("customername",customername);
            i.putExtra("sitename",sitename);
            i.putExtra("address",address);
            i.putExtra("compaddress",compaddress);
            i.putExtra("myid",myid);
            i.putExtra("firstname",firstname);
            i.putExtra("lastname",lastname);
            i.putExtra("phone",phone);
            i.putExtra("mobile",mobile);
            i.putExtra("fax",fax);
            i.putExtra("email",email);
            i.putExtra("latlng",latlng);
            i.putExtra("tags",tags.toString());
            mContext.startActivity(i);
        });

        holder.technician_site.setText(sitename);
        holder.customer_site_name.setText(customername);
        holder.contact_site_name.setText(firstname+" "+lastname);
        holder.site_phone.setText(phone);
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
            holder.site_tags.setText(tag);
        }

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}