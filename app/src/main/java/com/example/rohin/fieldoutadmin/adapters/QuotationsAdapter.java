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
import com.example.rohin.fieldoutadmin.activity.AddQuotationsListActivity;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Rohin on 18-12-2017.
 */

public class QuotationsAdapter extends RecyclerView.Adapter<QuotationsAdapter.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;

    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView invoice_date,invoices_status,invoices_customer_name,invoices_amount,invoices_tax,invoices_total;
        public LinearLayout invoices_linear;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            invoice_date=itemView.findViewById(R.id.invoice_date);
            invoices_linear=itemView.findViewById(R.id.invoices_linear);
            invoices_status=itemView.findViewById(R.id.invoices_status);
            invoices_customer_name=itemView.findViewById(R.id.invoices_customer_name);
            invoices_amount=itemView.findViewById(R.id.invoices_amount);
            invoices_tax=itemView.findViewById(R.id.invoices_tax);
            invoices_total=itemView.findViewById(R.id.invoices_total);

        }
    }
    public QuotationsAdapter(Context mContext, ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;


    }
    @Override
    public QuotationsAdapter.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_invoices,parent,false);

        return new QuotationsAdapter.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(QuotationsAdapter.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        String cusname=commonJobs.getCustomername();
        String total=commonJobs.getTotalamount();
        String date=commonJobs.getDateString();
        String status=commonJobs.getStatus();
        String tax=commonJobs.getTax();
        String amount=commonJobs.getAmount();
        String quotationid=commonJobs.getQuotationid();

        holder.invoices_linear.setOnClickListener(view -> {
            Intent i=new Intent(mContext, AddQuotationsListActivity.class);
            i.putExtra("quotationid",quotationid);
            mContext.startActivity(i);
        });

        holder.invoice_date.setText(date);
        holder.invoices_status.setText(status);
        holder.invoices_customer_name.setText(cusname);
        holder.invoices_amount.setText(amount);
        holder.invoices_tax.setText(tax);
        holder.invoices_total.setText(total);

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}