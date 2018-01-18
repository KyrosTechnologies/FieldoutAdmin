package com.example.rohin.fieldoutadmin.fragments;

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
import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.activity.QuotationCustomerSite;
import com.example.rohin.fieldoutadmin.adapters.QuotationsAdapter;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.common.EndURL;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rohin on 18-12-2017.
 */

public class QuotationsFragment extends Fragment {

    private View quotations;
    private RecyclerView quotations_recycler;
    private LinearLayout no_quotations_linear,add_quotation;
    private PreferenceManager store;
    private String domainid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        quotations=inflater.inflate(R.layout.fragment_quotations,container,false);
        quotations_recycler=quotations.findViewById(R.id.quotations_recycler);
        no_quotations_linear=quotations.findViewById(R.id.no_quotations_linear);
        add_quotation=quotations.findViewById(R.id.add_quotation);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        domainid=store.getIdDomain();

        add_quotation.setOnClickListener(view-> startActivity(new Intent(getContext(), QuotationCustomerSite.class)));

        return quotations;
    }

    private void GetQuotationsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"quotations/getByDomainId/"+domainid;
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
                    JSONArray array=obj.getJSONArray("quotations");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String quotationid=first.getString("id");
                        store.putQuotationId(String.valueOf(quotationid));
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
                        String cusname=null;
                        try {
                            cusname=customerInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setDateString(dateString);
                        commonJobs.setCustomername(cusname);
                        commonJobs.setTax(grandtax);
                        commonJobs.setQuotationid(quotationid);
                        commonJobs.setTotalamount(grandtotal);
                        commonJobs.setStatus(status);
                        commonJobs.setAmount(grandamount);
                        commonJobsArrayList.add(commonJobs);

                    }
                    if (commonJobsArrayList.size()!=0){
                        quotations_recycler = quotations.findViewById(R.id.quotations_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        quotations_recycler.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        quotations_recycler.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        QuotationsAdapter quotationsAdapter = new QuotationsAdapter(getContext(), commonJobsArrayList);
                        quotations_recycler.setAdapter(quotationsAdapter);
                        quotationsAdapter.notifyDataSetChanged();
                        no_quotations_linear.setVisibility(View.GONE);
                        quotations_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_quotations_linear.setVisibility(View.VISIBLE);
                        quotations_recycler.setVisibility(View.GONE);
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

                params.put("Authorization", store.getToken());
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    @Override
    public void onResume() {
        super.onResume();
        GetQuotationsList();
    }
}
