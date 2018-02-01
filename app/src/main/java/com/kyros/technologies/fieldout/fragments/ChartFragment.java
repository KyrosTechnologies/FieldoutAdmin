package com.kyros.technologies.fieldout.fragments;

import android.annotation.TargetApi;
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
import com.kyros.technologies.fieldout.activity.ChartAllJJobs;
import com.kyros.technologies.fieldout.activity.ChartCompletedJobs;
import com.kyros.technologies.fieldout.activity.ChartIncompletedJobs;
import com.kyros.technologies.fieldout.activity.ChartLateJobs;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.common.EndURL;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rohin on 26-01-2018.
 */

public class ChartFragment extends Fragment {

    private View chart;
    PieChart incompletedchart,latejobschart,completedjobschart,alljobschart;
    private TextView incomplete_no_chart,late_no_chart,completed_no_chart,all_no_chart,day_open,day_all,week_open,week_all,month_open,month_all;
    ArrayList<Entry> incompletedentries ;
    ArrayList<Entry> lateentries;
    ArrayList<Entry> completedentries;
    ArrayList<Entry> allentries;
    ArrayList<String> incompletedEntryLabels ;
    ArrayList<String> lateEntryLabels;
    ArrayList<String> completedEntryLabels;
    ArrayList<String> allEntryLabels;
    private PreferenceManager store;
    ArrayList<CommonJobs> cusDetailsArrayList = new ArrayList<CommonJobs>();
    ArrayList<String> incompletedx = new ArrayList<String>();
    ArrayList<Entry> incompletedy = new ArrayList<Entry>();
    ArrayList<String> allx = new ArrayList<String>();
    ArrayList<Entry> ally = new ArrayList<Entry>();
    ArrayList<String> latex = new ArrayList<String>();
    ArrayList<Entry> latey = new ArrayList<Entry>();
    ArrayList<String> completedx = new ArrayList<String>();
    ArrayList<Entry> completedy = new ArrayList<Entry>();
    private String userid=null;
    private String dtformat=null;
    private String[] xValues = {"High", "Low", "Normal"};
    public static  final int[] MY_COLORS = {
            Color.rgb(51,120,196),Color.rgb(189,189,189),Color.rgb(111,148,24)};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        chart=inflater.inflate(R.layout.fragment_chart,container,false);
        incompletedchart=chart.findViewById(R.id.incompletedchart);
        latejobschart=chart.findViewById(R.id.latejobschart);
        alljobschart=chart.findViewById(R.id.alljobschart);
        completedjobschart=chart.findViewById(R.id.completedjobschart);
        late_no_chart=chart.findViewById(R.id.late_no_chart);
        incomplete_no_chart=chart.findViewById(R.id.incomplete_no_chart);
        completed_no_chart=chart.findViewById(R.id.completed_no_chart);
        all_no_chart=chart.findViewById(R.id.all_no_chart);
        day_open=chart.findViewById(R.id.day_open);
        day_all=chart.findViewById(R.id.day_all);
        week_open=chart.findViewById(R.id.week_open);
        week_all=chart.findViewById(R.id.week_all);
        month_open=chart.findViewById(R.id.month_open);
        month_all=chart.findViewById(R.id.month_all);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        userid=store.getUserid();
        GetGraphList();

        incompletedentries = new ArrayList<>();
        lateentries=new ArrayList<>();
        completedentries=new ArrayList<>();
        allentries=new ArrayList<>();

        incompletedEntryLabels = new ArrayList<String>();
        lateEntryLabels=new ArrayList<>();
        completedEntryLabels=new ArrayList<>();
        allEntryLabels=new ArrayList<>();

        incompletedchart.setDescription("");
        latejobschart.setDescription("");
        completedjobschart.setDescription("");
        alljobschart.setDescription("");

        return chart;

    }

    private void GetGraphList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"graph/getAll/"+userid;
        Log.d("waggonurl", url);
        //showProgressDialog();

        JSONObject inputjso=new JSONObject();
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        dtformat=dateFormat.format(new Date());
        try{
            inputjso.put("dateString",dtformat);
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, inputjso, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                cusDetailsArrayList.clear();

                try {

                    JSONObject obj = new JSONObject(response.toString());
                    JSONObject object = obj.getJSONObject("graphResult");
                    JSONObject firstLayer=object.getJSONObject("firstLayer");
                    JSONObject thisMonth=firstLayer.getJSONObject("thisMonth");
                    JSONArray allJobs=thisMonth.getJSONArray("allJobs");
                    JSONArray openJobs=thisMonth.getJSONArray("openJobs");
                    JSONArray counts=thisMonth.getJSONArray("counts");
                    for (int i=0;i<counts.length();i++){
                        JSONObject first = counts.getJSONObject(i);
                        int count=first.getInt("count");
                        String name=first.getString("name");
                        if (i==0){
                            month_all.setText(String.valueOf(count));
                        }else if (i==1){
                            month_open.setText(String.valueOf(count));
                        }
                    }
                    JSONObject thisWeek=firstLayer.getJSONObject("thisWeek");
                    JSONArray allJobsthisWeek=thisWeek.getJSONArray("allJobs");
                    JSONArray openJobsthisWeek=thisWeek.getJSONArray("openJobs");
                    JSONArray countsthisWeek=thisWeek.getJSONArray("counts");
                    for (int i=0;i<countsthisWeek.length();i++){
                        JSONObject first = countsthisWeek.getJSONObject(i);
                        int count=first.getInt("count");
                        String name=first.getString("name");
                        if (i==0){
                            week_all.setText(String.valueOf(count));
                        }else if (i==1){
                            week_open.setText(String.valueOf(count));
                        }
                    }
                    JSONObject today=firstLayer.getJSONObject("today");
                    JSONArray allJobstoday=today.getJSONArray("allJobs");
                    JSONArray openJobstoday=today.getJSONArray("openJobs");
                    JSONArray countstoday=today.getJSONArray("counts");
                    for (int i=0;i<countstoday.length();i++){
                        JSONObject first = countstoday.getJSONObject(i);
                        int count=first.getInt("count");
                        String name=first.getString("name");
                        if (i==0){
                            day_all.setText(String.valueOf(count));
                        }else if (i==1){
                            day_open.setText(String.valueOf(count));
                        }
                    }
                    JSONObject secondLayer=object.getJSONObject("secondLayer");
                    JSONObject allJobssecondLayer=secondLayer.getJSONObject("allJobs");
                    JSONArray highLevelJobs=allJobssecondLayer.getJSONArray("highLevelJobs");
                    JSONArray lowLevelJobs=allJobssecondLayer.getJSONArray("lowLevelJobs");
                    JSONArray normalLevelJobs=allJobssecondLayer.getJSONArray("normalLevelJobs");
                    JSONArray result=allJobssecondLayer.getJSONArray("counts");
                    if (highLevelJobs.length()==0&&lowLevelJobs.length()==0&&normalLevelJobs.length()==0){
                        all_no_chart.setVisibility(View.VISIBLE);
                        alljobschart.setVisibility(View.GONE);
                    }else {
                        all_no_chart.setVisibility(View.GONE);
                        alljobschart.setVisibility(View.VISIBLE);
                    }
                    for (int i=0;i<result.length();i++) {
                        JSONObject first = result.getJSONObject(i);
                        int count = first.getInt("count");
                        String name = first.getString("name");
                        int values = first.getInt("value");
                        allx.add(new String(xValues[i]));
                        ally.add(new Entry(count, i));
                    }

                    PieDataSet dataSet = new PieDataSet(ally, "");
                    dataSet.setSliceSpace(3);
                    dataSet.setSelectionShift(5);

                    ArrayList<Integer> colors = new ArrayList<Integer>();

                    for (int c : MY_COLORS)
                        colors.add(c);


                    dataSet.setColors(colors);

                    PieData data = new PieData(allx, dataSet);
                    data.setValueFormatter(new MyValueFormatter());
                    data.setValueTextSize(11f);
                    data.setValueTextColor(Color.BLACK);

                    alljobschart.setData(data);

                    alljobschart.highlightValues(null);

                    alljobschart.invalidate();

                    alljobschart.animateXY(1400, 1400);

                    Legend l = alljobschart.getLegend();
                    l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
                    l.setXEntrySpace(7);
                    l.setYEntrySpace(5);

                    JSONObject completedJobssecondLayer=secondLayer.getJSONObject("completedJobs");
                    JSONArray completedhighLevelJobs=completedJobssecondLayer.getJSONArray("highLevelJobs");
                    JSONArray completedlowLevelJobs=completedJobssecondLayer.getJSONArray("lowLevelJobs");
                    JSONArray completednormalLevelJobs=completedJobssecondLayer.getJSONArray("normalLevelJobs");
                    JSONArray completedresult=completedJobssecondLayer.getJSONArray("counts");
                    if (completedhighLevelJobs.length()==0&&completedlowLevelJobs.length()==0&&completednormalLevelJobs.length()==0){
                        completed_no_chart.setVisibility(View.VISIBLE);
                        completedjobschart.setVisibility(View.GONE);
                    }else {
                        completed_no_chart.setVisibility(View.GONE);
                        completedjobschart.setVisibility(View.VISIBLE);
                    }
                    for (int i=0;i<completedresult.length();i++) {
                        JSONObject first = completedresult.getJSONObject(i);
                        int count = first.getInt("count");
                        String name = first.getString("name");
                        int values = first.getInt("value");
                        completedx.add(new String(xValues[i]));
                        completedy.add(new Entry(count, i));
                    }

                    PieDataSet completeddataSet = new PieDataSet(completedy, "");
                    completeddataSet.setSliceSpace(3);
                    completeddataSet.setSelectionShift(5);

                    ArrayList<Integer> completedcolors = new ArrayList<Integer>();

                    for (int c : MY_COLORS)
                        completedcolors.add(c);


                    completeddataSet.setColors(completedcolors);

                    PieData completeddata = new PieData(completedx, completeddataSet);
                    completeddata.setValueFormatter(new MyValueFormatter());
                    completeddata.setValueTextSize(11f);
                    completeddata.setValueTextColor(Color.BLACK);

                    completedjobschart.setData(completeddata);

                    completedjobschart.highlightValues(null);

                    completedjobschart.invalidate();

                    completedjobschart.animateXY(1400, 1400);

                    Legend k = completedjobschart.getLegend();
                    k.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
                    k.setXEntrySpace(7);
                    k.setYEntrySpace(5);

                    JSONObject lateJobssecondLayer=secondLayer.getJSONObject("lateJobs");
                    JSONArray latehighLevelJobs=lateJobssecondLayer.getJSONArray("highLevelJobs");
                    JSONArray latelowLevelJobs=lateJobssecondLayer.getJSONArray("lowLevelJobs");
                    JSONArray latenormalLevelJobs=lateJobssecondLayer.getJSONArray("normalLevelJobs");
                    JSONArray lateresult=lateJobssecondLayer.getJSONArray("counts");
                    if (latehighLevelJobs.length()==0&&latelowLevelJobs.length()==0&&latenormalLevelJobs.length()==0){
                        late_no_chart.setVisibility(View.VISIBLE);
                        latejobschart.setVisibility(View.GONE);
                    }else {
                        late_no_chart.setVisibility(View.GONE);
                        latejobschart.setVisibility(View.VISIBLE);
                    }
                    for (int i=0;i<lateresult.length();i++) {
                        JSONObject first = lateresult.getJSONObject(i);
                        int count = first.getInt("count");
                        String name = first.getString("name");
                        int values = first.getInt("value");
                        latex.add(new String(xValues[i]));
                        latey.add(new Entry(count, i));
                    }

                    PieDataSet latedateset = new PieDataSet(latey, "");
                    latedateset.setSliceSpace(3);
                    latedateset.setSelectionShift(5);

                    ArrayList<Integer> colorslate = new ArrayList<Integer>();

                    for (int c : MY_COLORS)
                        colorslate.add(c);


                    latedateset.setColors(colorslate);

                    PieData late = new PieData(latex, latedateset);
                    late.setValueFormatter(new MyValueFormatter());
                    late.setValueTextSize(11f);
                    late.setValueTextColor(Color.BLACK);

                    latejobschart.setData(late);

                    latejobschart.highlightValues(null);

                    latejobschart.invalidate();

                    latejobschart.animateXY(1400, 1400);

                    Legend j = latejobschart.getLegend();
                    j.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
                    j.setXEntrySpace(7);
                    j.setYEntrySpace(5);

                    JSONObject openJobssecondLayer=secondLayer.getJSONObject("openJobs");
                    JSONArray openhighLevelJobs=openJobssecondLayer.getJSONArray("highLevelJobs");
                    JSONArray openlowLevelJobs=openJobssecondLayer.getJSONArray("lowLevelJobs");
                    JSONArray opennormalLevelJobs=openJobssecondLayer.getJSONArray("normalLevelJobs");
                    JSONArray openresult=openJobssecondLayer.getJSONArray("counts");
                    if (openhighLevelJobs.length()==0&&openlowLevelJobs.length()==0&&opennormalLevelJobs.length()==0){
                        incomplete_no_chart.setVisibility(View.VISIBLE);
                        incompletedchart.setVisibility(View.GONE);
                    }else {
                        incomplete_no_chart.setVisibility(View.GONE);
                        incompletedchart.setVisibility(View.VISIBLE);
                    }
                    for (int i=0;i<openresult.length();i++) {
                        JSONObject first = openresult.getJSONObject(i);
                        int count = first.getInt("count");
                        String name = first.getString("name");
                        int values = first.getInt("value");
                        incompletedx.add(new String(xValues[i]));
                        incompletedy.add(new Entry(count, i));
                    }

                    PieDataSet incompletedateset = new PieDataSet(incompletedy, "");
                    incompletedateset.setSliceSpace(3);
                    incompletedateset.setSelectionShift(5);

                    ArrayList<Integer> colorsincomplete = new ArrayList<Integer>();

                    for (int c : MY_COLORS)
                        colorsincomplete.add(c);


                    incompletedateset.setColors(colorsincomplete);

                    PieData incomplete = new PieData(incompletedx, incompletedateset);
                    incomplete.setValueFormatter(new MyValueFormatter());
                    incomplete.setValueTextSize(11f);
                    incomplete.setValueTextColor(Color.BLACK);

                    incompletedchart.setData(incomplete);

                    incompletedchart.highlightValues(null);

                    incompletedchart.invalidate();

                    incompletedchart.animateXY(1400, 1400);

                    Legend legend = incompletedchart.getLegend();
                    legend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
                    legend.setXEntrySpace(7);
                    legend.setYEntrySpace(5);

                    completedjobschart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                            Intent i=new Intent(getContext(), ChartCompletedJobs.class);
                            i.putExtra("index",h.getXIndex());
                            i.putExtra("completedjobs",completedJobssecondLayer.toString());
                            startActivity(i);
                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });

                    incompletedchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                            Intent i=new Intent(getContext(), ChartIncompletedJobs.class);
                            i.putExtra("index",h.getXIndex());
                            i.putExtra("completedjobs",openJobssecondLayer.toString());
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
                            i.putExtra("completedjobs",lateJobssecondLayer.toString());
                            i.putExtra("index",h.getXIndex());
                            startActivity(i);
                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });

                    alljobschart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                            Intent i=new Intent(getContext(), ChartAllJJobs.class);
                            i.putExtra("index",h.getXIndex());
                            i.putExtra("completedjobs",allJobssecondLayer.toString());
                            startActivity(i);
                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });

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