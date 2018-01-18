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
import com.example.rohin.fieldoutadmin.activity.InvoicesUpdateDelete;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Rohin on 02-01-2018.
 */

public class AdapterAddInvoices extends RecyclerView.Adapter<AdapterAddInvoices.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;
    ArrayList<String>descriptionArrayList;
    ArrayList<String>discountArrayList;
    ArrayList<String>itemArrayList;
    ArrayList<String>quantityArrayList;
    ArrayList<String>taxArrayList;
    ArrayList<String>totalArrayList;
    ArrayList<String>unitpriceArrayList;

    public class MyViewHolderEleven extends RecyclerView.ViewHolder{
        public TextView desc_invoice,unit_price_invoice,quantity_invoice,discount_invoice,total_invoice,tax_invoice,item_invoice;
        public LinearLayout add_invoices_linear;

        public MyViewHolderEleven(View itemView) {
            super(itemView);
            store= PreferenceManager.getInstance(mContext.getApplicationContext());
            desc_invoice=itemView.findViewById(R.id.desc_invoice);
            add_invoices_linear=itemView.findViewById(R.id.add_invoices_linear);
            unit_price_invoice=itemView.findViewById(R.id.unit_price_invoice);
            quantity_invoice=itemView.findViewById(R.id.quantity_invoice);
            discount_invoice=itemView.findViewById(R.id.discount_invoice);
            total_invoice=itemView.findViewById(R.id.total_invoice);
            tax_invoice=itemView.findViewById(R.id.tax_invoice);
            item_invoice=itemView.findViewById(R.id.item_invoice);

        }
    }
    public AdapterAddInvoices(Context mContext, ArrayList<CommonJobs>duration, ArrayList<String>descriptionArrayList,ArrayList<String>discountArrayList,
                              ArrayList<String>itemArrayList,ArrayList<String>quantityArrayList,ArrayList<String>taxArrayList,
                              ArrayList<String>totalArrayList,ArrayList<String>unitpriceArrayList){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;
        this.descriptionArrayList=descriptionArrayList;
        this.discountArrayList=discountArrayList;
        this.itemArrayList=itemArrayList;
        this.quantityArrayList=quantityArrayList;
        this.taxArrayList=taxArrayList;
        this.totalArrayList=totalArrayList;
        this.unitpriceArrayList=unitpriceArrayList;


    }
    @Override
    public AdapterAddInvoices.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add_invoices,parent,false);

        return new AdapterAddInvoices.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(AdapterAddInvoices.MyViewHolderEleven holder, int position) {

        String descrip=descriptionArrayList.get(position);
        if (descrip!=null){
            holder.desc_invoice.setText(descrip);
        }
        String pricein=unitpriceArrayList.get(position);
        if (pricein!=null){
            holder.unit_price_invoice.setText(pricein);
        }
        String quantityin=quantityArrayList.get(position);
        if (quantityin!=null){
            holder.quantity_invoice.setText(quantityin);
        }
        String discountin=discountArrayList.get(position);
        if (discountin!=null){
            holder.discount_invoice.setText(discountin);
        }
        String totalin=totalArrayList.get(position);
        if (totalin!=null){
            holder.total_invoice.setText(totalin);
        }
        String taxin=taxArrayList.get(position);
        if (taxin!=null){
            holder.tax_invoice.setText(taxin);
        }
        String itemsin=itemArrayList.get(position);
        if (itemsin!=null){
            holder.item_invoice.setText(itemsin);
        }

        holder.add_invoices_linear.setOnClickListener(view -> {
            Intent i=new Intent(mContext, InvoicesUpdateDelete.class);
            i.putExtra("position",position);
            i.putExtra("amount",totalin);
            i.putExtra("tax",taxin);
            i.putExtra("items",itemsin);
            i.putExtra("description",descrip);
            i.putExtra("unitprice",pricein);
            i.putExtra("quantity",quantityin);
            i.putExtra("discount",discountin);
            mContext.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return descriptionArrayList.size();
    }

}