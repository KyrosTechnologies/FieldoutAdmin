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
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.example.rohin.fieldoutadmin.adapters.ScheduleDayAdapter;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.common.EndURL;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rohin on 13-12-2017.
 */

public class ScheduleDayFragment extends Fragment {

    private View day;
    private RecyclerView week_recycler;
    private LinearLayout no_jobs_linear,add_jobs_schedule;
    private PreferenceManager store;
    private String userid=null;
    private String dtformat=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        day=inflater.inflate(R.layout.fragment_week_schedule,container,false);
        week_recycler=day.findViewById(R.id.week_recycler);
        no_jobs_linear=day.findViewById(R.id.no_jobs_linear);
        add_jobs_schedule=day.findViewById(R.id.add_jobs_schedule);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        userid=store.getUserid();

        add_jobs_schedule.setOnClickListener(view-> startActivity(new Intent(getContext(), AddJobsActivity.class)));
        return day;
    }

    private void PutDayCalendarList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"userjobs/getJobsByDate/"+userid;
        Log.d("waggonurl", url);
        //showProgressDialog();

        JSONObject inputjso=new JSONObject();
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        dtformat=dateFormat.format(new Date());
        try{
            inputjso.put("date",dtformat);
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, inputjso, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                commonJobsArrayList.clear();

                try {

                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray array = obj.getJSONArray("userjobs");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject first = array.getJSONObject(i);
                        String status = first.getString("status");
                        String idJob=first.getString("idJob");
                        JSONObject jobinfo=null;
                        try {
                            jobinfo = first.getJSONObject("jobInfo");
                        }catch (Exception e){

                        }
                        String myId="";
                        try {
                            myId=jobinfo.getString("myId");
                        }catch (Exception e){
                        }
                        String priority="";
                        try {
                            priority=jobinfo.getString("priority");

                        }catch (Exception e){

                        }
                        String contactName="";
                        try {
                            contactName=jobinfo.getString("contactName");
                        }catch (Exception e){

                        }
                        String contactFirstName="";
                        try {
                            contactFirstName=jobinfo.getString("contactFirstName");
                        }catch (Exception e){
                        }
                        String contactLastName="";
                        try {
                            contactLastName=first.getString("contactLastName");
                        }catch (Exception e){
                        }
                        String contactMobile="";
                        try {
                            contactMobile=jobinfo.getString("contactMobile");
                        }catch (Exception e){

                        }
                        String contactPhone="";
                        try {
                            contactPhone=jobinfo.getString("contactPhone");
                        }catch (Exception e){

                        }
                        String contactEmail="";
                        try {
                            contactEmail=jobinfo.getString("contactEmail");
                        }catch (Exception e){

                        }
                        String description="";
                        try {
                            description=jobinfo.getString("description");
                        }catch (Exception e){
                        }
                        String schedullingPreferredDate="";
                        try {
                            schedullingPreferredDate=jobinfo.getString("schedullingPreferredDate");
                        }catch (Exception e){
                        }
                        String complementAddress="";
                        try {
                            complementAddress=jobinfo.getString("complementAddress");
                        }catch (Exception e){

                        }
                        JSONObject jobTypeInfo =null;
                        try {
                            jobTypeInfo = jobinfo.getJSONObject("jobTypeInfo");
                        }catch (Exception e){
                        }
                        String jobtypename="";
                        try {
                            jobtypename=jobTypeInfo.getString("job_type_name");
                        }catch (Exception e){
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
                        JSONArray tagInfo=null;
                        try {
                            tagInfo=jobinfo.getJSONArray("tagInfo");
                        }catch (Exception e){
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

                        String scheduledBeginDate = first.getString("scheduledBeginDateString");
                        String scheduledEndDate = first.getString("scheduledEndDateString");

                        CommonJobs commonJobs = new CommonJobs();
                        commonJobs.setJobid(idJob);
                        commonJobs.setEquipname(equipname);
                        commonJobs.setCustomername(cusname);
                        commonJobs.setMyId(myId);
                        commonJobs.setSchedulingdate(schedullingPreferredDate);
                        commonJobs.setComplementAddress(complementAddress);
                        commonJobs.setJobTypeName(jobtypename);
                        commonJobs.setScheduledBeginDate(scheduledBeginDate);
                        commonJobs.setScheduleenddate(scheduledEndDate);
                        commonJobs.setStatus(status);
                        commonJobs.setSitename(sitename);
                        commonJobs.setFirstname(contactFirstName);
                        commonJobs.setLastname(contactLastName);
                        commonJobs.setPriority(priority);
                        commonJobs.setAddress(address);
                        commonJobs.setLatlng(latlng);
                        commonJobs.setContactname(contactName);
                        commonJobs.setMobilenum(contactMobile);
                        commonJobs.setPhone(contactPhone);
                        commonJobs.setEmail(contactEmail);
                        commonJobs.setTaginfo(tagInfo);
                        commonJobs.setDescription(description);
                        commonJobsArrayList.add(commonJobs);

                    }


                    if (commonJobsArrayList.size()!=0){
                        week_recycler = day.findViewById(R.id.week_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        //  week_recycler.setLayoutManager(layoutManager);
                        week_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        week_recycler.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        ScheduleDayAdapter scheduleDayAdapter = new ScheduleDayAdapter(getContext(), commonJobsArrayList);
                        week_recycler.setAdapter(scheduleDayAdapter);
                        scheduleDayAdapter.notifyDataSetChanged();
                        no_jobs_linear.setVisibility(View.GONE);
                        week_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_jobs_linear.setVisibility(View.VISIBLE);
                        week_recycler.setVisibility(View.GONE);
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
        PutDayCalendarList();
    }
}
