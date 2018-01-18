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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.activity.AddCustomer;
import com.example.rohin.fieldoutadmin.adapters.CustomerAdapter;
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
 * Created by Rohin on 13-12-2017.
 */

public class CustomerFragment extends Fragment {

    private View customers;
    private RecyclerView customers_recycler;
    private LinearLayout no_customer_linear,add_customer;
    private PreferenceManager store;
    private String domainid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        customers=inflater.inflate(R.layout.fragment_customers,container,false);
        customers_recycler=customers.findViewById(R.id.customers_recycler);
        no_customer_linear=customers.findViewById(R.id.no_customer_linear);
        add_customer=customers.findViewById(R.id.add_customer);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        domainid=store.getIdDomain();

        add_customer.setOnClickListener(view-> startActivity(new Intent(getContext(), AddCustomer.class)));

        return customers;
    }

    private void GetCustomerList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"customers/getByDomainId/"+domainid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                commonJobsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("customers");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String customerid=first.getString("id");
                        store.putCustomerId(String.valueOf(customerid));
                        String name="";
                        try {
                            name=first.getString("name");
                        }catch (Exception e){
                        }
                        String myId="";
                        try {
                            myId=first.getString("myId");
                        }catch (Exception e){

                        }
                        String addressComplement="";
                        try {
                            addressComplement=first.getString("addressComplement");
                        }catch (Exception e){

                        }
                        String contactfirstname="";
                        try {
                            contactfirstname=first.getString("contactFirstName");
                        }catch (Exception e){
                        }
                        String contactlastname="";
                        try {
                            contactlastname=first.getString("contactLastName");
                        }catch (Exception e){
                        }
                        String contactmobile="";
                        try {
                            contactmobile=first.getString("contactMobile");
                        }catch (Exception e){
                        }
                        String contactphone="";
                        try {
                            contactphone=first.getString("contactPhone");
                        }catch (Exception e){
                        }
                        String address="";
                        try {
                            address=first.getString("address");
                        }catch (Exception e){
                        }
                        String contactFax="";
                        try {
                            contactFax=first.getString("contactFax");
                        }catch (Exception e){

                        }
                        String contactEmail="";
                        try {
                            contactEmail=first.getString("contactEmail");
                        }catch (Exception e){

                        }
                        JSONArray positions=null;
                        String latlng=null;
                        try {
                            StringBuilder stringBuilder=new StringBuilder();
                            positions=first.getJSONArray("positions");
                            for (int j=0;j<positions.length();j++){
                                Double pos=positions.getDouble(j);

                                if (j==positions.length()){
                                    stringBuilder.append(pos);
                                }else {
                                    stringBuilder.append(pos+"  ");
                                }
                            }
                            latlng=stringBuilder.toString();
                                }catch (Exception e){

                        }

                        JSONArray tagInfo=null;
                        try {
                            tagInfo=first.getJSONArray("tagInfo");
                        }catch (Exception e){
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setCustomername(name);
                        commonJobs.setMyId(myId);
                        commonJobs.setAddress(address);
                        commonJobs.setComplementAddress(addressComplement);
                        commonJobs.setCustomerid(customerid);
                        commonJobs.setFirstname(contactfirstname);
                        commonJobs.setLastname(contactlastname);
                        commonJobs.setMobilenum(contactmobile);
                        commonJobs.setPhone(contactphone);
                        commonJobs.setFax(contactFax);
                        commonJobs.setEmail(contactEmail);
                        commonJobs.setTaginfo(tagInfo);
                        commonJobs.setLatlng(latlng);
                        commonJobsArrayList.add(commonJobs);

                    }


                    if (commonJobsArrayList.size()!=0){
                        customers_recycler=customers.findViewById(R.id.customers_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        customers_recycler.setLayoutManager(layoutManager);
                        //customers_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        customers_recycler.setItemAnimator(new DefaultItemAnimator());
                        CustomerAdapter customerAdapter=new CustomerAdapter(getContext(), commonJobsArrayList);
                        customers_recycler.setAdapter(customerAdapter);
                        customerAdapter.notifyDataSetChanged();
                        no_customer_linear.setVisibility(View.GONE);
                        customers_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_customer_linear.setVisibility(View.VISIBLE);
                        customers_recycler.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();
                Log.e("Error"," "+error.getMessage());

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
        GetCustomerList();
    }
}