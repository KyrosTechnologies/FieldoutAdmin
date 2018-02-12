package com.kyros.technologies.fieldout.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.activity.InvoiceCustomerSite;
import com.kyros.technologies.fieldout.adapters.InvoicesAdapter;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.common.EndURL;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rohin on 18-12-2017.
 */

public class InvoicesFragment extends Fragment {

    private View invoice;
    private RecyclerView invoices_recycler;
    private LinearLayout no_invoices_linear,add_invoices;
    private PreferenceManager store;
    private String domainid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        invoice=inflater.inflate(R.layout.fragment_invoices,container,false);
        invoices_recycler=invoice.findViewById(R.id.invoices_recycler);
        no_invoices_linear=invoice.findViewById(R.id.no_invoices_linear);
        add_invoices=invoice.findViewById(R.id.add_invoices);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        domainid=store.getIdDomain();

        add_invoices.setOnClickListener(view-> startActivity(new Intent(getContext(), InvoiceCustomerSite.class)));

        return invoice;
    }

    private void GetInvoicesList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"invoices/getAll";
        Log.d("waggonurl", url);
        //showProgressDialog();

        JSONObject inputjso=new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, inputjso, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                commonJobsArrayList.clear();

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("invoices");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String invoiceid=first.getString("id");
                        String status=first.getString("status");
                        String dateString=first.getString("dateString");
                        String grandtax="";
                        try {
                            grandtax=first.getString("grand_tax");
                        }catch (Exception e){

                        }
                        String grandtotal="";
                        try {
                            grandtotal=first.getString("grand_total");
                        }catch (Exception e){

                        }
                        String grandamount="";
                        try {
                            grandamount=first.getString("grand_amount");
                        }catch (Exception e){

                        }
                        JSONObject customerInfo=null;
                        try {
                            customerInfo = first.getJSONObject("customerInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String cusname="";
                        try {
                            cusname=customerInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setDateString(dateString);
                        commonJobs.setCustomername(cusname);
                        commonJobs.setTax(grandtax);
                        commonJobs.setInvoiceId(invoiceid);
                        commonJobs.setTotalamount(grandtotal);
                        commonJobs.setStatus(status);
                        commonJobs.setAmount(grandamount);
                        commonJobsArrayList.add(commonJobs);

                    }
                    if (commonJobsArrayList.size()!=0){
                        invoices_recycler = invoice.findViewById(R.id.invoices_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        invoices_recycler.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        invoices_recycler.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        InvoicesAdapter invoicesAdapter = new InvoicesAdapter(getContext(), commonJobsArrayList);
                        invoices_recycler.setAdapter(invoicesAdapter);
                        invoicesAdapter.notifyDataSetChanged();
                        no_invoices_linear.setVisibility(View.GONE);
                        invoices_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_invoices_linear.setVisibility(View.VISIBLE);
                        invoices_recycler.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //dismissProgressDialog();

            }
        }) {

            @Override
            public Map<String, String> getHeaders()throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idDomain",store.getIdDomain());
                params.put("Authorization", store.getToken());
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    @Override
    public void onResume() {
        super.onResume();
        GetInvoicesList();
    }
}
