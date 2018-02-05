package com.kyros.technologies.fieldout.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.CompletedJobsAdapter;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rohin on 05-02-2018.
 */

public class TechnicianMonthAll extends AppCompatActivity {

    private PreferenceManager store;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    private RecyclerView jobs_month_recycler;
    private String techalljobs=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.chart_jobs_list);
        jobs_month_recycler = findViewById(R.id.jobs_month_recycler);
        store = PreferenceManager.getInstance(getApplicationContext());

        try {
            Bundle bundle = getIntent().getExtras();
            techalljobs=bundle.getString("techalljobs");
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            JSONArray techallJobs=new JSONArray(techalljobs);
            for (int a=0;a<techallJobs.length();a++){
                JSONObject second = techallJobs.getJSONObject(a);
                String normallevelid=second.getString("id");
                String normallevelidJob=second.getString("idJob");
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
                    for (int b=0;b<positions.length();b++){
                        Double pos=positions.getDouble(b);

                        if (b==positions.length()){
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
                commonJobs.setTechmonthallJobs(techallJobs);
                commonJobsArrayList.add(commonJobs);
            }

        if (commonJobsArrayList.size()!=0){
            jobs_month_recycler=findViewById(R.id.jobs_month_recycler);
            LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
            jobs_month_recycler.setLayoutManager(layoutManager);
            //activity_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            jobs_month_recycler.setItemAnimator(new DefaultItemAnimator());
            CompletedJobsAdapter completedJobsAdapter = new CompletedJobsAdapter(TechnicianMonthAll.this, commonJobsArrayList);
            jobs_month_recycler.setAdapter(completedJobsAdapter);
            completedJobsAdapter.notifyDataSetChanged();
        }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                TechnicianMonthAll.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
