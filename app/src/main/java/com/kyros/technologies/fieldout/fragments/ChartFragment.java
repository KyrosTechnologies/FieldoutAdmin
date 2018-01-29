package com.kyros.technologies.fieldout.fragments;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.activity.ChartCompletedJobs;
import com.kyros.technologies.fieldout.activity.ChartIncompletedJobs;
import com.kyros.technologies.fieldout.activity.ChartLateJobs;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.common.EndURL;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rohin on 26-01-2018.
 */

public class ChartFragment extends Fragment {

    private View chart;
    PieChart incompletedchart,latejobschart,completedjobschart;
    private TextView incomplete_no_chart,late_no_chart,completed_no_chart;
    ArrayList<Entry> incompletedentries ;
    ArrayList<Entry> lateentries;
    ArrayList<Entry> completedentries;
    ArrayList<String> incompletedEntryLabels ;
    ArrayList<String> lateEntryLabels;
    ArrayList<String> completedEntryLabels;
    private PreferenceManager store;
    ArrayList<CommonJobs> cusDetailsArrayList = new ArrayList<CommonJobs>();
    ArrayList<String> incompletedx = new ArrayList<String>();
    ArrayList<Entry> incompletedy = new ArrayList<Entry>();
    ArrayList<String> latex = new ArrayList<String>();
    ArrayList<Entry> latey = new ArrayList<Entry>();
    ArrayList<String> completedx = new ArrayList<String>();
    ArrayList<Entry> completedy = new ArrayList<Entry>();
    private String userid=null;
    private String[] xValues = {"High", "Low", "Normal"};
    public static  final int[] MY_COLORS = {
            Color.rgb(255,0,0), Color.rgb(0,136,233),Color.rgb(255,165,0)};
    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        chart=inflater.inflate(R.layout.fragment_chart,container,false);
        incompletedchart=chart.findViewById(R.id.incompletedchart);
        latejobschart=chart.findViewById(R.id.latejobschart);
        completedjobschart=chart.findViewById(R.id.completedjobschart);
        late_no_chart=chart.findViewById(R.id.late_no_chart);
        incomplete_no_chart=chart.findViewById(R.id.incomplete_no_chart);
        completed_no_chart=chart.findViewById(R.id.completed_no_chart);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        userid=store.getUserid();

        incompletedentries = new ArrayList<>();
        lateentries=new ArrayList<>();
        completedentries=new ArrayList<>();

        incompletedEntryLabels = new ArrayList<String>();
        lateEntryLabels=new ArrayList<>();
        completedEntryLabels=new ArrayList<>();

        GetInCompletedList();
        GetLateJobsList();
        GetCompletedJobsList();

        incompletedchart.setDescription("");
        latejobschart.setDescription("");
        completedjobschart.setDescription("");

        incompletedchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Intent i=new Intent(getContext(), ChartIncompletedJobs.class);
                i.putExtra("index",h.getXIndex());
                startActivity(i);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        latejobschart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Intent i=new Intent(getContext(), ChartLateJobs.class);
                i.putExtra("index",h.getXIndex());
                startActivity(i);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        completedjobschart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Intent i=new Intent(getContext(), ChartCompletedJobs.class);
                i.putExtra("index",h.getXIndex());
                startActivity(i);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return chart;

    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(getContext());
            pDialog.setTitle("Please Wait");
            pDialog.setMessage("Synchronization in progress...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
        }
        pDialog.show();
    }

    private void dismissProgressDialog() {
        try{
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void GetInCompletedList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"userjobs/getInCompletedJobs/"+userid;
        Log.d("waggonurl", url);
        showProgressDialog();
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.d("List TeamsResponse",response.toString());
                cusDetailsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray highLevelJobs=obj.getJSONArray("highLevelJobs");
                    JSONArray lowLevelJobs=obj.getJSONArray("lowLevelJobs");
                    JSONArray normalLevelJobs=obj.getJSONArray("normalLevelJobs");
                    JSONArray result=obj.getJSONArray("result");
                    if (highLevelJobs.length()==0&&lowLevelJobs.length()==0&&normalLevelJobs.length()==0){
                        incomplete_no_chart.setVisibility(View.VISIBLE);
                        incompletedchart.setVisibility(View.GONE);
                    }else {
                        incomplete_no_chart.setVisibility(View.GONE);
                        incompletedchart.setVisibility(View.VISIBLE);
                    }
                    for (int i=0;i<result.length();i++){
                        JSONObject first=result.getJSONObject(i);
                        int count=first.getInt("count");
                        String name=first.getString("name");
                        int values=first.getInt("value");
                        incompletedx.add(new String(xValues[i]));
                        incompletedy.add(new Entry(count,i));

                    }
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
                    }
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
                    }
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
                    }

                    PieDataSet dataSet = new PieDataSet(incompletedy, "");
                    dataSet.setSliceSpace(3);
                    dataSet.setSelectionShift(5);

                    ArrayList<Integer> colors = new ArrayList<Integer>();

                    for (int c : MY_COLORS)
                        colors.add(c);


                    dataSet.setColors(colors);

                    PieData data = new PieData(incompletedx, dataSet);
                    data.setValueFormatter(new MyValueFormatter());
                    data.setValueTextSize(11f);
                    data.setValueTextColor(Color.WHITE);

                    incompletedchart.setData(data);

                    incompletedchart.highlightValues(null);

                    incompletedchart.invalidate();

                    incompletedchart.animateXY(1400, 1400);

                    Legend l = incompletedchart.getLegend();
                    l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
                    l.setXEntrySpace(7);
                    l.setYEntrySpace(5);



                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();
                Log.e("Error"," "+error.getMessage());
                dismissProgressDialog();
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

    private void GetLateJobsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"userjobs/getGLateJobs/"+userid;
        Log.d("waggonurl", url);
        showProgressDialog();
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.d("List TeamsResponse",response.toString());
                cusDetailsArrayList.clear();
                try {
                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray highLevelJobs=obj.getJSONArray("highLevelJobs");
                    JSONArray lowLevelJobs=obj.getJSONArray("lowLevelJobs");
                    JSONArray normalLevelJobs=obj.getJSONArray("normalLevelJobs");
                    JSONArray result=obj.getJSONArray("result");
                    if (highLevelJobs.length()==0&&lowLevelJobs.length()==0&&normalLevelJobs.length()==0){
                        late_no_chart.setVisibility(View.VISIBLE);
                        latejobschart.setVisibility(View.GONE);
                    }else {
                        late_no_chart.setVisibility(View.GONE);
                        latejobschart.setVisibility(View.VISIBLE);
                    }
                    for (int i=0;i<result.length();i++){
                        JSONObject first=result.getJSONObject(i);
                        int count=first.getInt("count");
                        String name=first.getString("name");
                        int values=first.getInt("value");
                        latex.add(new String(xValues[i]));
                        latey.add(new Entry(count,i));

                    }
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
                    }
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
                    }

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
                    }

                    PieDataSet dataSet = new PieDataSet(latey, "");
                    dataSet.setSliceSpace(3);
                    dataSet.setSelectionShift(5);

                    ArrayList<Integer> colors = new ArrayList<Integer>();

                    for (int c : MY_COLORS)
                        colors.add(c);


                    dataSet.setColors(colors);

                    PieData data = new PieData(latex, dataSet);
                    data.setValueFormatter(new MyValueFormatter());
                    data.setValueTextSize(11f);
                    data.setValueTextColor(Color.WHITE);

                    latejobschart.setData(data);

                    latejobschart.highlightValues(null);

                    latejobschart.invalidate();

                    latejobschart.animateXY(1400, 1400);

                    Legend l = latejobschart.getLegend();
                    l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
                    l.setXEntrySpace(7);
                    l.setYEntrySpace(5);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();
                Log.e("Error"," "+error.getMessage());
                dismissProgressDialog();
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

    private void GetCompletedJobsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"userjobs/getCompletedJobs/"+userid;
        Log.d("waggonurl", url);
        showProgressDialog();
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.d("List TeamsResponse",response.toString());
                cusDetailsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray highLevelJobs=obj.getJSONArray("highLevelJobs");
                    JSONArray lowLevelJobs=obj.getJSONArray("lowLevelJobs");
                    JSONArray normalLevelJobs=obj.getJSONArray("normalLevelJobs");
                    JSONArray result=obj.getJSONArray("result");
                    if (highLevelJobs.length()==0&&lowLevelJobs.length()==0&&normalLevelJobs.length()==0){
                        completed_no_chart.setVisibility(View.VISIBLE);
                        completedjobschart.setVisibility(View.GONE);
                    }else {
                        completed_no_chart.setVisibility(View.GONE);
                        completedjobschart.setVisibility(View.VISIBLE);
                    }
                    for (int i=0;i<result.length();i++){
                        JSONObject first=result.getJSONObject(i);
                        int count=first.getInt("count");
                        String name=first.getString("name");
                        int values=first.getInt("value");
                        completedx.add(new String(xValues[i]));
                        completedy.add(new Entry(count,i));

                    }
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
                    }
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
                    }
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
                    }

                    PieDataSet dataSet = new PieDataSet(completedy, "");
                    dataSet.setSliceSpace(3);
                    dataSet.setSelectionShift(5);

                    ArrayList<Integer> colors = new ArrayList<Integer>();

                    for (int c : MY_COLORS)
                        colors.add(c);


                    dataSet.setColors(colors);

                    PieData data = new PieData(completedx, dataSet);
                    data.setValueFormatter(new MyValueFormatter());
                    data.setValueTextSize(11f);
                    data.setValueTextColor(Color.WHITE);

                    completedjobschart.setData(data);

                    completedjobschart.highlightValues(null);

                    completedjobschart.invalidate();

                    completedjobschart.animateXY(1400, 1400);

                    Legend l = completedjobschart.getLegend();
                    l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
                    l.setXEntrySpace(7);
                    l.setYEntrySpace(5);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();
                Log.e("Error"," "+error.getMessage());
                dismissProgressDialog();
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


    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value) + "";
        }
    }

}