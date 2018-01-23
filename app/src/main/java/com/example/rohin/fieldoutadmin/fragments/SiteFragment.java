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
import com.example.rohin.fieldoutadmin.activity.AddSite;
import com.example.rohin.fieldoutadmin.adapters.SiteAdapter;
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

public class SiteFragment extends Fragment {

    private View sites;
    private RecyclerView site_recycler;
    private LinearLayout no_site_linear,add_site;
    private PreferenceManager store;
    private String domainid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sites=inflater.inflate(R.layout.fragment_site,container,false);
        site_recycler=sites.findViewById(R.id.site_recycler);
        no_site_linear=sites.findViewById(R.id.no_site_linear);
        add_site=sites.findViewById(R.id.add_site);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        domainid=store.getIdDomain();

        add_site.setOnClickListener(view-> startActivity(new Intent(getContext(), AddSite.class)));

        return sites;
    }

    private void GetSiteList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"sites/getByDomainId/"+domainid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                commonJobsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("sites");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String siteid=first.getString("id");
                        store.putSiteId(String.valueOf(siteid));
                        String sitename="";
                        try {
                            sitename=first.getString("name");
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
                        String contactphone="";
                        try {
                            contactphone=first.getString("contactPhone");
                        }catch (Exception e){
                        }
                        String addressComplement="";
                        try {
                            addressComplement=first.getString("addressComplement");
                        }catch (Exception e){

                        }
                        String address="";
                        try {
                            address=first.getString("address");
                        }catch (Exception e){
                        }
                        String contactEmail="";
                        try {
                            contactEmail=first.getString("contactEmail");
                        }catch (Exception e){

                        }
                        String contactMobile="";
                        try {
                            contactMobile=first.getString("contactMobile");
                        }catch (Exception e){

                        }
                        String myId="";
                        try {
                            myId=first.getString("myId");
                        }catch (Exception e){

                        }
                        String contactFax="";
                        try {
                            contactFax=first.getString("contactFax");
                        }catch (Exception e){

                        }
                        JSONObject customerInfo=null;
                        try {
                            customerInfo=first.getJSONObject("customerInfo");
                        }catch (Exception e){
                        }
                        String cusname="";
                        try {
                            cusname=customerInfo.getString("name");
                        }catch (Exception e){
                        }
                        String custid="";
                        try {
                            custid=customerInfo.getString("id");
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
                        JSONArray customFields=null;
                        try{
                            customFields=first.getJSONArray("CustomFieldValues");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setSiteid(siteid);
                        commonJobs.setCustomername(cusname);
                        commonJobs.setComplementAddress(addressComplement);
                        commonJobs.setCustomerid(custid);
                        commonJobs.setFirstname(contactfirstname);
                        commonJobs.setLastname(contactlastname);
                        commonJobs.setPhone(contactphone);
                        commonJobs.setEmail(contactEmail);
                        commonJobs.setFax(contactFax);
                        commonJobs.setMobilenum(contactMobile);
                        commonJobs.setAddress(address);
                        commonJobs.setMyId(myId);
                        commonJobs.setTaginfo(tagInfo);
                        commonJobs.setSitename(sitename);
                        commonJobs.setLatlng(latlng);
                        if(customFields!=null){
                            commonJobs.setCustomFields(customFields.toString());
                        }
                        commonJobsArrayList.add(commonJobs);

                    }

                    if (commonJobsArrayList.size()!=0){
                        site_recycler=sites.findViewById(R.id.site_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        site_recycler.setLayoutManager(layoutManager);
                        //site_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        site_recycler.setItemAnimator(new DefaultItemAnimator());
                        SiteAdapter siteAdapter=new SiteAdapter(getContext(), commonJobsArrayList);
                        site_recycler.setAdapter(siteAdapter);
                        siteAdapter.notifyDataSetChanged();
                        no_site_linear.setVisibility(View.GONE);
                        site_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_site_linear.setVisibility(View.VISIBLE);
                        site_recycler.setVisibility(View.GONE);
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
        GetSiteList();
    }
}