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
import com.example.rohin.fieldoutadmin.adapters.UpcomingJobsAdapter;
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

public class UpcomingJobsFragment extends Fragment {

    private View upcomingjobs;
    private RecyclerView jobs_month_recycler;
    private LinearLayout no_monthjobs_linear,add_jobs_day;
    private PreferenceManager store;
    private String domainid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        upcomingjobs=inflater.inflate(R.layout.fragment_month_jobs,container,false);
        jobs_month_recycler=upcomingjobs.findViewById(R.id.jobs_month_recycler);
        no_monthjobs_linear=upcomingjobs.findViewById(R.id.no_monthjobs_linear);
        add_jobs_day=upcomingjobs.findViewById(R.id.add_jobs_day);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        domainid=store.getIdDomain();

        add_jobs_day.setOnClickListener(view-> startActivity(new Intent(getContext(), AddJobsActivity.class)));

        return upcomingjobs;
    }

    private void GetUpcomingJobsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"userjobs/getAUpcomingJobs/"+domainid;
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
                    JSONArray array=obj.getJSONArray("userjobs");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String status=first.getString("status");
                        JSONObject jobinfo =null;
                        try {
                            jobinfo = first.getJSONObject("jobInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String myId="";
                        try {
                            myId=jobinfo.getString("myId");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String globalAddress="";
                        try {
                            globalAddress=jobinfo.getString("globalAddress");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        JSONObject jobTypeInfo =null;
                        try {
                            jobTypeInfo = jobinfo.getJSONObject("jobTypeInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String jobtypename="";
                        try {
                            jobtypename=jobTypeInfo.getString("job_type_name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        JSONObject customerInfo =null;
                        try {
                            customerInfo = jobinfo.getJSONObject("customerInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String cusname="";
                        try {
                            cusname=customerInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        JSONObject equipmentInfo =null;
                        try {
                            equipmentInfo = jobinfo.getJSONObject("equipmentInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String equipname="";
                        try {
                            equipname=equipmentInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        JSONObject siteInfo =null;
                        try {
                            siteInfo = jobinfo.getJSONObject("siteInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String sitename="";
                        try {
                            sitename=siteInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String scheduledBeginDate=first.getString("scheduledBeginDateString");
                        String scheduledEndDate=first.getString("scheduledEndDateString");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setEquipname(equipname);
                        commonJobs.setCustomername(cusname);
                        commonJobs.setMyId(myId);
                        commonJobs.setScheduledBeginDate(scheduledBeginDate);
                        commonJobs.setScheduleenddate(scheduledEndDate);
                        commonJobs.setStatus(status);
                        commonJobs.setSitename(sitename);
                        commonJobsArrayList.add(commonJobs);

                    }
                    if (commonJobsArrayList.size()!=0){
                        jobs_month_recycler = upcomingjobs.findViewById(R.id.jobs_month_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        jobs_month_recycler.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        jobs_month_recycler.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        UpcomingJobsAdapter upcomingJobsAdapter = new UpcomingJobsAdapter(getContext(), commonJobsArrayList);
                        jobs_month_recycler.setAdapter(upcomingJobsAdapter);
                        upcomingJobsAdapter.notifyDataSetChanged();
                        no_monthjobs_linear.setVisibility(View.GONE);
                        jobs_month_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_monthjobs_linear.setVisibility(View.VISIBLE);
                        jobs_month_recycler.setVisibility(View.GONE);
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
        GetUpcomingJobsList();
    }
}
