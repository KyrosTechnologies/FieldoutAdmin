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
import com.example.rohin.fieldoutadmin.activity.AddJobsActivity;
import com.example.rohin.fieldoutadmin.adapters.JobsToScheduleAdapter;
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
 * Created by Rohin on 14-12-2017.
 */

public class JobsToScheduleFragment extends Fragment {

    private View latejobs;
    private RecyclerView jobs_to_schedule_recycler;
    private LinearLayout no_jobsto_linear,add_job_schedule;
    private PreferenceManager store;
    private String domainid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        latejobs=inflater.inflate(R.layout.fragment_to_schedule,container,false);
        jobs_to_schedule_recycler=latejobs.findViewById(R.id.jobs_to_schedule_recycler);
        no_jobsto_linear=latejobs.findViewById(R.id.no_jobsto_linear);
        add_job_schedule=latejobs.findViewById(R.id.add_job_schedule);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        domainid=store.getIdDomain();

        add_job_schedule.setOnClickListener(view-> startActivity(new Intent(getContext(), AddJobsActivity.class)));

        return latejobs;
    }

    private void GetToScheduleJobsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"jobs/getToScheduleJobs/"+domainid;
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
                    JSONArray array=obj.getJSONArray("jobs");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String jobid=first.getString("id");
                        String myId="";
                        try {
                            myId=first.getString("myId");
                        }catch (Exception e){

                        }
                        String priority="";
                        try {
                            priority=first.getString("priority");
                        }catch (Exception e){

                        }
                        String description="";
                        try {
                            description=first.getString("description");
                        }catch (Exception e){

                        }
                        String contactEmail="";
                        try {
                            contactEmail=first.getString("contactEmail");
                        }catch (Exception e){

                        }
                        String contactName="";
                        try {
                            contactName=first.getString("contactName");
                        }catch (Exception e){

                        }
                        JSONObject customerInfo=null;
                        try {
                            customerInfo=first.getJSONObject("customerInfo");
                        }catch (Exception e){

                        }
                        String customername="";
                        try {
                            customername=customerInfo.getString("name");
                        }catch (Exception e){

                        }
                        String contactMobile="";
                        try {
                            contactMobile=customerInfo.getString("contactMobile");
                        }catch (Exception e){

                        }
                        String contactPhone="";
                        try {
                            contactPhone=customerInfo.getString("contactPhone");
                        }catch (Exception e){

                        }
                        JSONObject equipmentInfo=null;
                        try {
                            equipmentInfo=first.getJSONObject("equipmentInfo");
                        }catch (Exception e){

                        }
                        String equipname="";
                        try {
                            equipname=equipmentInfo.getString("name");
                        }catch (Exception e){

                        }
                        JSONObject siteInfo=null;
                        try {
                            siteInfo=first.getJSONObject("siteInfo");
                        }catch (Exception e){

                        }
                        String sitename="";
                        try {
                            sitename=siteInfo.getString("name");
                        }catch (Exception e){

                        }
                        String address="";
                        try {
                            address=siteInfo.getString("address");
                        }catch (Exception e){

                        }
                        JSONArray positions=null;
                        String latlng=null;
                        try {
                            StringBuilder stringBuilder=new StringBuilder();
                            positions=siteInfo.getJSONArray("positions");
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
                        JSONObject jobTypeInfo=null;
                        try {
                            jobTypeInfo=first.getJSONObject("jobTypeInfo");
                        }catch (Exception e){

                        }
                        String job_type_name="";
                        try {
                            job_type_name=jobTypeInfo.getString("job_type_name");
                        }catch (Exception e){

                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setEquipname(equipname);
                        commonJobs.setCustomername(customername);
                        commonJobs.setMyId(myId);
                        commonJobs.setJobid(jobid);
                        commonJobs.setContactname(contactName);
                        commonJobs.setPhone(contactPhone);
                        commonJobs.setMobilenum(contactMobile);
                        commonJobs.setSitename(sitename);
                        commonJobs.setEmail(contactEmail);
                        commonJobs.setJobTypeName(job_type_name);
                        commonJobs.setPriority(priority);
                        commonJobs.setLatlng(latlng);
                        commonJobs.setAddress(address);
                        commonJobs.setDescription(description);
                        commonJobsArrayList.add(commonJobs);

                    }
                    if (commonJobsArrayList.size()!=0){
                        jobs_to_schedule_recycler = latejobs.findViewById(R.id.jobs_to_schedule_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        jobs_to_schedule_recycler.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        jobs_to_schedule_recycler.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        JobsToScheduleAdapter jpbsToScheduleAdapter = new JobsToScheduleAdapter(getContext(), commonJobsArrayList);
                        jobs_to_schedule_recycler.setAdapter(jpbsToScheduleAdapter);
                        jpbsToScheduleAdapter.notifyDataSetChanged();
                        no_jobsto_linear.setVisibility(View.GONE);
                        jobs_to_schedule_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_jobsto_linear.setVisibility(View.VISIBLE);
                        jobs_to_schedule_recycler.setVisibility(View.GONE);
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
        GetToScheduleJobsList();
    }
}
