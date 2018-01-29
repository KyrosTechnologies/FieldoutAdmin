package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.ChartLateJobsAdapter;
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
 * Created by Rohin on 29-01-2018.
 */

public class ChartLateJobs extends AppCompatActivity {

    private PreferenceManager store;
    private String userid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    private int index=0;
    private RecyclerView jobs_month_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.chart_jobs_list);
        jobs_month_recycler=findViewById(R.id.jobs_month_recycler);
        store = PreferenceManager.getInstance(getApplicationContext());
        userid=store.getUserid();

        try {
            Bundle bundle = getIntent().getExtras();
            index = bundle.getInt("index");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void GetLateJobsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"userjobs/getGLateJobs/"+userid;
        Log.d("waggonurl", url);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                commonJobsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray highLevelJobs=obj.getJSONArray("highLevelJobs");
                    JSONArray lowLevelJobs=obj.getJSONArray("lowLevelJobs");
                    JSONArray normalLevelJobs=obj.getJSONArray("normalLevelJobs");
                    JSONArray result=obj.getJSONArray("result");

                    if (index==0){

                        for (int i=0;i<highLevelJobs.length();i++){
                            JSONObject first=highLevelJobs.getJSONObject(i);
                            String highlevelid=first.getString("id");
                            String highlevelidJob=first.getString("idJob");
                            String startdate=first.getString("scheduledBeginDateString");
                            String enddate=first.getString("scheduledEndDateString");
                            String status=first.getString("status");
                            JSONObject jobInfo=null;
                            try {
                                jobInfo=first.getJSONObject("jobInfo");
                            }catch (Exception e){
                            }
                            String myId="";
                            try {
                                myId=jobInfo.getString("myId");
                            }catch (Exception e){
                            }
                            String priority="";
                            try {
                                priority=jobInfo.getString("priority");

                            }catch (Exception e){

                            }
                            String contactName="";
                            try {
                                contactName=jobInfo.getString("contactName");
                            }catch (Exception e){

                            }
                            String contactFirstName="";
                            try {
                                contactFirstName=jobInfo.getString("contactFirstName");
                            }catch (Exception e){
                            }
                            String contactLastName="";
                            try {
                                contactLastName=jobInfo.getString("contactLastName");
                            }catch (Exception e){
                            }
                            String contactMobile="";
                            try {
                                contactMobile=jobInfo.getString("contactMobile");
                            }catch (Exception e){

                            }
                            String contactPhone="";
                            try {
                                contactPhone=jobInfo.getString("contactPhone");
                            }catch (Exception e){

                            }
                            String contactEmail="";
                            try {
                                contactEmail=jobInfo.getString("contactEmail");
                            }catch (Exception e){

                            }
                            String description="";
                            try {
                                description=jobInfo.getString("description");
                            }catch (Exception e){
                            }
                            String schedullingPreferredDate="";
                            try {
                                schedullingPreferredDate=jobInfo.getString("schedullingPreferredDate");
                            }catch (Exception e){
                            }
                            String complementAddress="";
                            try {
                                complementAddress=jobInfo.getString("complementAddress");
                            }catch (Exception e){

                            }
                            JSONObject jobTypeInfo =null;
                            try {
                                jobTypeInfo = jobInfo.getJSONObject("jobTypeInfo");
                            }catch (Exception e){
                            }
                            String jobtypename="";
                            try {
                                jobtypename=jobTypeInfo.getString("job_type_name");
                            }catch (Exception e){
                            }
                            JSONObject customerInfo =null;
                            try {
                                customerInfo = jobInfo.getJSONObject("customerInfo");
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
                                tagInfo=jobInfo.getJSONArray("tagInfo");
                            }catch (Exception e){
                            }
                            JSONObject equipmentInfo =null;
                            try {
                                equipmentInfo = jobInfo.getJSONObject("equipmentInfo");
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
                                siteInfo = jobInfo.getJSONObject("siteInfo");
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

                            CommonJobs commonJobs = new CommonJobs();
                            commonJobs.setHighlevelid(highlevelid);
                            commonJobs.setHighlevelidjob(highlevelidJob);
                            commonJobs.setEquipname(equipname);
                            commonJobs.setCustomername(cusname);
                            commonJobs.setMyId(myId);
                            commonJobs.setSchedulingdate(schedullingPreferredDate);
                            commonJobs.setComplementAddress(complementAddress);
                            commonJobs.setJobTypeName(jobtypename);
                            commonJobs.setScheduledBeginDate(startdate);
                            commonJobs.setScheduleenddate(enddate);
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

                    }else if (index==1){

                        for (int i=0;i<lowLevelJobs.length();i++){
                            JSONObject second=lowLevelJobs.getJSONObject(i);
                            String lowlevelid=second.getString("id");
                            String lowlevelidJob=second.getString("idJob");
                            String startdate=second.getString("scheduledBeginDateString");
                            String enddate=second.getString("scheduledEndDateString");
                            String status=second.getString("status");
                            JSONObject jobInfo=null;
                            try {
                                jobInfo=second.getJSONObject("jobInfo");
                            }catch (Exception e){
                            }
                            String myId="";
                            try {
                                myId=jobInfo.getString("myId");
                            }catch (Exception e){
                            }
                            String priority="";
                            try {
                                priority=jobInfo.getString("priority");

                            }catch (Exception e){

                            }
                            String contactName="";
                            try {
                                contactName=jobInfo.getString("contactName");
                            }catch (Exception e){

                            }
                            String contactFirstName="";
                            try {
                                contactFirstName=jobInfo.getString("contactFirstName");
                            }catch (Exception e){
                            }
                            String contactLastName="";
                            try {
                                contactLastName=jobInfo.getString("contactLastName");
                            }catch (Exception e){
                            }
                            String contactMobile="";
                            try {
                                contactMobile=jobInfo.getString("contactMobile");
                            }catch (Exception e){

                            }
                            String contactPhone="";
                            try {
                                contactPhone=jobInfo.getString("contactPhone");
                            }catch (Exception e){

                            }
                            String contactEmail="";
                            try {
                                contactEmail=jobInfo.getString("contactEmail");
                            }catch (Exception e){

                            }
                            String description="";
                            try {
                                description=jobInfo.getString("description");
                            }catch (Exception e){
                            }
                            String schedullingPreferredDate="";
                            try {
                                schedullingPreferredDate=jobInfo.getString("schedullingPreferredDate");
                            }catch (Exception e){
                            }
                            String complementAddress="";
                            try {
                                complementAddress=jobInfo.getString("complementAddress");
                            }catch (Exception e){

                            }
                            JSONObject jobTypeInfo =null;
                            try {
                                jobTypeInfo = jobInfo.getJSONObject("jobTypeInfo");
                            }catch (Exception e){
                            }
                            String jobtypename="";
                            try {
                                jobtypename=jobTypeInfo.getString("job_type_name");
                            }catch (Exception e){
                            }
                            JSONObject customerInfo =null;
                            try {
                                customerInfo = jobInfo.getJSONObject("customerInfo");
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
                                tagInfo=jobInfo.getJSONArray("tagInfo");
                            }catch (Exception e){
                            }
                            JSONObject equipmentInfo =null;
                            try {
                                equipmentInfo = jobInfo.getJSONObject("equipmentInfo");
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
                                siteInfo = jobInfo.getJSONObject("siteInfo");
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

                            CommonJobs commonJobs = new CommonJobs();
                            commonJobs.setLowlevelid(lowlevelid);
                            commonJobs.setLowlevelidjob(lowlevelidJob);
                            commonJobs.setEquipname(equipname);
                            commonJobs.setCustomername(cusname);
                            commonJobs.setMyId(myId);
                            commonJobs.setSchedulingdate(schedullingPreferredDate);
                            commonJobs.setComplementAddress(complementAddress);
                            commonJobs.setJobTypeName(jobtypename);
                            commonJobs.setScheduledBeginDate(startdate);
                            commonJobs.setScheduleenddate(enddate);
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

                    }else if (index==2){

                        for (int i=0;i<normalLevelJobs.length();i++){
                            JSONObject third=normalLevelJobs.getJSONObject(i);
                            String normallevelid=third.getString("id");
                            String normallevelidJob=third.getString("idJob");
                            String startdate=third.getString("scheduledBeginDateString");
                            String enddate=third.getString("scheduledEndDateString");
                            String status=third.getString("status");
                            JSONObject jobInfo=null;
                            try {
                                jobInfo=third.getJSONObject("jobInfo");
                            }catch (Exception e){
                            }
                            String myId="";
                            try {
                                myId=jobInfo.getString("myId");
                            }catch (Exception e){
                            }
                            String priority="";
                            try {
                                priority=jobInfo.getString("priority");

                            }catch (Exception e){

                            }
                            String contactName="";
                            try {
                                contactName=jobInfo.getString("contactName");
                            }catch (Exception e){

                            }
                            String contactFirstName="";
                            try {
                                contactFirstName=jobInfo.getString("contactFirstName");
                            }catch (Exception e){
                            }
                            String contactLastName="";
                            try {
                                contactLastName=jobInfo.getString("contactLastName");
                            }catch (Exception e){
                            }
                            String contactMobile="";
                            try {
                                contactMobile=jobInfo.getString("contactMobile");
                            }catch (Exception e){

                            }
                            String contactPhone="";
                            try {
                                contactPhone=jobInfo.getString("contactPhone");
                            }catch (Exception e){

                            }
                            String contactEmail="";
                            try {
                                contactEmail=jobInfo.getString("contactEmail");
                            }catch (Exception e){

                            }
                            String description="";
                            try {
                                description=jobInfo.getString("description");
                            }catch (Exception e){
                            }
                            String schedullingPreferredDate="";
                            try {
                                schedullingPreferredDate=jobInfo.getString("schedullingPreferredDate");
                            }catch (Exception e){
                            }
                            String complementAddress="";
                            try {
                                complementAddress=jobInfo.getString("complementAddress");
                            }catch (Exception e){

                            }
                            JSONObject jobTypeInfo =null;
                            try {
                                jobTypeInfo = jobInfo.getJSONObject("jobTypeInfo");
                            }catch (Exception e){
                            }
                            String jobtypename="";
                            try {
                                jobtypename=jobTypeInfo.getString("job_type_name");
                            }catch (Exception e){
                            }
                            JSONObject customerInfo =null;
                            try {
                                customerInfo = jobInfo.getJSONObject("customerInfo");
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
                                tagInfo=jobInfo.getJSONArray("tagInfo");
                            }catch (Exception e){
                            }
                            JSONObject equipmentInfo =null;
                            try {
                                equipmentInfo = jobInfo.getJSONObject("equipmentInfo");
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
                                siteInfo = jobInfo.getJSONObject("siteInfo");
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

                            CommonJobs commonJobs = new CommonJobs();
                            commonJobs.setNormallevelid(normallevelid);
                            commonJobs.setNormallevelidjob(normallevelidJob);
                            commonJobs.setEquipname(equipname);
                            commonJobs.setCustomername(cusname);
                            commonJobs.setMyId(myId);
                            commonJobs.setSchedulingdate(schedullingPreferredDate);
                            commonJobs.setComplementAddress(complementAddress);
                            commonJobs.setJobTypeName(jobtypename);
                            commonJobs.setScheduledBeginDate(startdate);
                            commonJobs.setScheduleenddate(enddate);
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

                    }

                    if (index==0){
                        if(commonJobsArrayList.size()!=0){
                            jobs_month_recycler = findViewById(R.id.jobs_month_recycler);
                            LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            jobs_month_recycler.setLayoutManager(layoutManager);
                            jobs_month_recycler.setItemAnimator(new DefaultItemAnimator());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            ChartLateJobsAdapter chartLateJobsAdapter = new ChartLateJobsAdapter(ChartLateJobs.this, commonJobsArrayList);
                            jobs_month_recycler.setAdapter(chartLateJobsAdapter);
                            chartLateJobsAdapter.notifyDataSetChanged();
                        }
                    }else if (index==1){
                        if(commonJobsArrayList.size()!=0){
                            jobs_month_recycler = findViewById(R.id.jobs_month_recycler);
                            LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            jobs_month_recycler.setLayoutManager(layoutManager);
                            jobs_month_recycler.setItemAnimator(new DefaultItemAnimator());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            ChartLateJobsAdapter chartLateJobsAdapter = new ChartLateJobsAdapter(ChartLateJobs.this, commonJobsArrayList);
                            jobs_month_recycler.setAdapter(chartLateJobsAdapter);
                            chartLateJobsAdapter.notifyDataSetChanged();
                        }
                    }else if (index==2){
                        if(commonJobsArrayList.size()!=0){
                            jobs_month_recycler = findViewById(R.id.jobs_month_recycler);
                            LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                            jobs_month_recycler.setLayoutManager(layoutManager);
                            jobs_month_recycler.setItemAnimator(new DefaultItemAnimator());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            ChartLateJobsAdapter chartLateJobsAdapter = new ChartLateJobsAdapter(ChartLateJobs.this, commonJobsArrayList);
                            jobs_month_recycler.setAdapter(chartLateJobsAdapter);
                            chartLateJobsAdapter.notifyDataSetChanged();
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();
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
        GetLateJobsList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                ChartLateJobs.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}