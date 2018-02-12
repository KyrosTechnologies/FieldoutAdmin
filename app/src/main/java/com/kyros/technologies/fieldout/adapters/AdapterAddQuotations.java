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
import com.kyros.technologies.fieldout.activity.QuotationItemsUpdateDelete;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Rohin on 04-01-2018.
 */

public class AdapterAddQuotations extends RecyclerView.Adapter<AdapterAddQuotations.MyViewHolderEleven>{

    private Context mContext;
    private PreferenceManager store;
    private ArrayList<CommonJobs> commonJobsArrayList;

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
    public AdapterAddQuotations(Context mContext, ArrayList<CommonJobs>duration){
        this.mContext=mContext;
        this.commonJobsArrayList =duration;

    }
    @Override
    public AdapterAddQuotations.MyViewHolderEleven onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add_invoices,parent,false);

        return new AdapterAddQuotations.MyViewHolderEleven(view);
    }

    @Override
    public void onBindViewHolder(AdapterAddQuotations.MyViewHolderEleven holder, int position) {

        CommonJobs commonJobs=commonJobsArrayList.get(position);
        String desc=commonJobs.getDescription();
        String items=commonJobs.getItemname();
        String itemid=commonJobs.getItemid();
        String unitprice=commonJobs.getUnitproice();
        String quantity=commonJobs.getQuantity();
        String discount=commonJobs.getDiscount();
        String total=commonJobs.getTotal();
        String tax=commonJobs.getTax();

        holder.add_invoices_linear.setOnClickListener(view -> {
            Intent i=new Intent(mContext, QuotationItemsUpdateDelete.class);
            i.putExtra("itemid",itemid);
            i.putExtra("amount",total);
            i.putExtra("tax",tax);
            i.putExtra("items",items);
            i.putExtra("description",desc);
            i.putExtra("unitprice",unitprice);
            i.putExtra("quantity",quantity);
            i.putExtra("discount",discount);
            mContext.startActivity(i);
        });

        holder.desc_invoice.setText(desc);
        holder.discount_invoice.setText(discount);
        holder.item_invoice.setText(items);
        holder.unit_price_invoice.setText(unitprice);
        holder.quantity_invoice.setText(quantity);
        holder.tax_invoice.setText(tax);
        holder.total_invoice.setText(total);

    }

    @Override
    public int getItemCount() {
        return commonJobsArrayList.size();
    }

}