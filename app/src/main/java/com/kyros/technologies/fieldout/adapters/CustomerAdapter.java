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
import com.kyros.technologies.fieldout.activity.CustomerDetails;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rohin on 13-12-2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;


    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView technician_customer,contact_name,customer_phone,customer_tags;
        public LinearLayout customer_linear;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            technician_customer=itemView.findViewById(R.id.technician_customer);
            customer_linear=itemView.findViewById(R.id.customer_linear);
            contact_name=itemView.findViewById(R.id.contact_name);
            customer_phone=itemView.findViewById(R.id.customer_phone);
            customer_tags=itemView.findViewById(R.id.customer_tags);

        }
    }
    public CustomerAdapter(Context mContext,ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public CustomerAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_customer,parent,false);

        return new CustomerAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(CustomerAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        String tagname=commonJobs.getTagname();
        String customerid=commonJobs.getCustomerid();
        String mobile=commonJobs.getMobilenum();
        String address=commonJobs.getAddress();
        String compaddress=commonJobs.getComplementAddress();
        String myid=commonJobs.getMyId();
        String phone=commonJobs.getPhone();
        String customername=commonJobs.getCustomername();
        String firstname=commonJobs.getFirstname();
        String lastname=commonJobs.getLastname();
        String fax=commonJobs.getFax();
        String email=commonJobs.getEmail();
        String latlng=commonJobs.getLatlng();
        JSONArray tags=commonJobs.getTaginfo();
        String customFields=commonJobs.getCustomFields();

        holder.customer_linear.setOnClickListener(view -> {
            Intent i=new Intent(mContext, CustomerDetails.class);
            i.putExtra("customerid",customerid);
            i.putExtra("customername",customername);
            i.putExtra("address",address);
            i.putExtra("compaddress",compaddress);
            i.putExtra("myid",myid);
            i.putExtra("firstname",firstname);
            i.putExtra("lastname",lastname);
            i.putExtra("mobile",mobile);
            i.putExtra("phone",phone);
            i.putExtra("fax",fax);
            i.putExtra("email",email);
            i.putExtra("latlng",latlng);
            i.putExtra("tags",tags.toString());
            i.putExtra("customFields",customFields);
            mContext.startActivity(i);
        });

        holder.technician_customer.setText(customername);
        holder.contact_name.setText(firstname+" "+lastname);
        holder.customer_phone.setText(mobile+" / "+phone);
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
            holder.customer_tags.setText(tag);
        }

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}