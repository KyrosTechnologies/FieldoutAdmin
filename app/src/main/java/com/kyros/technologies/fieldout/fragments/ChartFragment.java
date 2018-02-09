package com.kyros.technologies.fieldout.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.activity.ChartAllJJobs;
import com.kyros.technologies.fieldout.activity.ChartCompletedJobs;
import com.kyros.technologies.fieldout.activity.ChartIncompletedJobs;
import com.kyros.technologies.fieldout.activity.ChartLateJobs;
import com.kyros.technologies.fieldout.activity.CustomerJobsList;
import com.kyros.technologies.fieldout.activity.SiteJobsList;
import com.kyros.technologies.fieldout.activity.StatusJobsList;
import com.kyros.technologies.fieldout.adapters.TechnicianDayAdapter;
import com.kyros.technologies.fieldout.adapters.TechnicianProgressAdapter;
import com.kyros.technologies.fieldout.adapters.TechnicianWeekAdapter;
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
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.BubbleChartView;

/**
 * Created by Rohin on 26-01-2018.
 */

public class ChartFragment extends Fragment implements OnChartValueSelectedListener{

    private View chart;
    PieChart incompletedchart,latejobschart,completedjobschart,alljobschart;
    private BarChart bar_by_status;
    private TextView incomplete_no_chart,late_no_chart,completed_no_chart,all_no_chart,day_open,day_all,week_open,
            week_all,month_open,month_all,status_one_chart,today_text,this_week_text,this_month_text,status_one_site,
            status_jobs,no_jobs_found;
    private RecyclerView this_month_tech_recycler,this_week_tech_recycler,today_tech_recycler;
    ArrayList<Entry> incompletedentries ;
    ArrayList<Entry> lateentries;
    List<BubbleEntry> bubbleEntries=new ArrayList<>();
    ArrayList<String> bubblex=new ArrayList<String>();
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
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs>thismonthArrayList=new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> thisWeekArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> thisWeekClosedArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> todayArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> todayClosedArrayList = new ArrayList<CommonJobs>();
    private String[] xValues = {"High", "Low", "Normal"};
    public static  final int[] MY_COLORS = {
            Color.rgb(51,120,196),Color.rgb(189,189,189),Color.rgb(111,148,24)};

    private JSONArray techallJobs;
    private JSONArray techclosedjobs;
    private JSONArray techallJobsWeek;
    private JSONArray techclosedjobsWeek;
    private JSONArray techtodayallJobs;
    private JSONArray techclosedjobsToday;
    private JSONArray customer;
    private JSONArray site;

    private BubbleChartView bubbleChartView,sitejobs;
    private BubbleChartData bubbleChartData,siteChartData;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private ValueShape shape = ValueShape.CIRCLE;
    List<BubbleValue> values = new ArrayList<BubbleValue>();
    List<BubbleValue> sitevalues = new ArrayList<BubbleValue>();
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;

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
        bubbleChartView=chart.findViewById(R.id.jobs_by_status);
        sitejobs=chart.findViewById(R.id.jobs_by_site);
        all_no_chart=chart.findViewById(R.id.all_no_chart);
        bar_by_status=chart.findViewById(R.id.bar_by_status);
        day_open=chart.findViewById(R.id.day_open);
        day_all=chart.findViewById(R.id.day_all);
        week_open=chart.findViewById(R.id.week_open);
        week_all=chart.findViewById(R.id.week_all);
        month_open=chart.findViewById(R.id.month_open);
        month_all=chart.findViewById(R.id.month_all);
        status_one_chart=chart.findViewById(R.id.status_one_chart);
        this_month_tech_recycler=chart.findViewById(R.id.this_month_tech_recycler);
        today_text=chart.findViewById(R.id.today_text);
        this_week_text=chart.findViewById(R.id.this_week_text);
        this_month_text=chart.findViewById(R.id.this_month_text);
        this_week_tech_recycler=chart.findViewById(R.id.this_week_tech_recycler);
        today_tech_recycler=chart.findViewById(R.id.today_tech_recycler);
        status_one_site=chart.findViewById(R.id.status_one_site);
        status_jobs=chart.findViewById(R.id.status_jobs);
        no_jobs_found=chart.findViewById(R.id.no_jobs_found);
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
        bar_by_status.setDescription("");

        bar_by_status.setMaxVisibleValueCount(8);

        bar_by_status.setPinchZoom(false);

        bar_by_status.setDrawBarShadow(false);
        bar_by_status.setDrawGridBackground(false);

        XAxis xAxis = bar_by_status.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setDrawGridLines(false);

        bar_by_status.getAxisLeft().setDrawGridLines(false);

//        mChart.animateY(2500);

        bar_by_status.getLegend().setEnabled(false);


        today_text.setOnClickListener(view -> {
            if (techtodayallJobs.length()==0&&techclosedjobsToday.length()==0){
                no_jobs_found.setVisibility(View.VISIBLE);
                today_tech_recycler.setVisibility(View.GONE);
            }
            today_tech_recycler.setVisibility(View.VISIBLE);
            this_month_tech_recycler.setVisibility(View.GONE);
            this_week_tech_recycler.setVisibility(View.GONE);
        });

        this_month_text.setOnClickListener(view -> {
            if (techallJobs.length()==0&&techclosedjobs.length()==0){
                no_jobs_found.setVisibility(View.VISIBLE);
                this_month_tech_recycler.setVisibility(View.GONE);
            }
            today_tech_recycler.setVisibility(View.GONE);
            this_month_tech_recycler.setVisibility(View.VISIBLE);
            this_week_tech_recycler.setVisibility(View.GONE);
        });

        this_week_text.setOnClickListener(view -> {
            if (techallJobsWeek.length()==0&&techclosedjobsWeek.length()==0){
                no_jobs_found.setVisibility(View.VISIBLE);
                this_week_tech_recycler.setVisibility(View.GONE);
            }
            today_tech_recycler.setVisibility(View.GONE);
            this_month_tech_recycler.setVisibility(View.GONE);
            this_week_tech_recycler.setVisibility(View.VISIBLE);
        });

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
                commonJobsArrayList.clear();
                thismonthArrayList.clear();
                thisWeekArrayList.clear();
                thisWeekClosedArrayList.clear();
                todayArrayList.clear();
                todayClosedArrayList.clear();
                values.clear();
                sitevalues.clear();

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

                    JSONObject technicianAndJobsLayer=object.getJSONObject("technicianAndJobsLayer");
                    JSONArray techthisMonth=technicianAndJobsLayer.getJSONArray("thisMonth");
                    for (int i=0;i<techthisMonth.length();i++){
                        JSONObject first = techthisMonth.getJSONObject(i);
                        String username=first.getString("username");
                        JSONArray techcounts=first.getJSONArray("counts");
                        techallJobs=first.getJSONArray("allJobs");
                        techclosedjobs=first.getJSONArray("closedJobs");
                        for (int a=0;a<techallJobs.length();a++){
                            JSONObject second = techallJobs.getJSONObject(a);
                            String normallevelid=second.getString("id");
                           // String normallevelidJob=second.getString("idJob");
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
                            commonJobs.setCounts(techcounts);
                            commonJobs.setTechnicianname(username);
                            commonJobsArrayList.add(commonJobs);
                        }

                        try {
                            for (int a=0;a<techclosedjobs.length();a++){
                                JSONObject second = techclosedjobs.getJSONObject(a);
                                String normallevelid=second.getString("id");
                                // String normallevelidJob=second.getString("idJob");
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
                                commonJobs.setTechmonthClosedJobs(techclosedjobs);
                                commonJobs.setCounts(techcounts);
                                commonJobs.setTechnicianname(username);
                                thismonthArrayList.add(commonJobs);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    if (commonJobsArrayList.size()!=0){
                        this_month_tech_recycler=chart.findViewById(R.id.this_month_tech_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        this_month_tech_recycler.setLayoutManager(layoutManager);
                        this_month_tech_recycler.setItemAnimator(new DefaultItemAnimator());
                        TechnicianProgressAdapter technicianProgressAdapter=new TechnicianProgressAdapter(getContext(), commonJobsArrayList);
                        this_month_tech_recycler.setAdapter(technicianProgressAdapter);
                        technicianProgressAdapter.notifyDataSetChanged();
                    }

                    if (thismonthArrayList.size()!=0){
                        this_month_tech_recycler=chart.findViewById(R.id.this_month_tech_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        this_month_tech_recycler.setLayoutManager(layoutManager);
                        this_month_tech_recycler.setItemAnimator(new DefaultItemAnimator());
                        TechnicianProgressAdapter technicianClosedJobs=new TechnicianProgressAdapter(getContext(), thismonthArrayList);
                        this_month_tech_recycler.setAdapter(technicianClosedJobs);
                        technicianClosedJobs.notifyDataSetChanged();
                    }

                    JSONArray techthisWeek=technicianAndJobsLayer.getJSONArray("thisWeek");
                    for (int i=0;i<techthisWeek.length();i++){
                        JSONObject first = techthisWeek.getJSONObject(i);
                        String username=first.getString("username");
                        JSONArray techcounts=first.getJSONArray("counts");
                        techallJobsWeek=first.getJSONArray("allJobs");
                        techclosedjobsWeek=first.getJSONArray("closedJobs");
                        for (int a=0;a<techallJobsWeek.length();a++){
                            JSONObject second = techallJobsWeek.getJSONObject(a);
                            String normallevelid=second.getString("id");
                            // String normallevelidJob=second.getString("idJob");
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
                            commonJobs.setWeekAllJobs(techallJobsWeek);
                            commonJobs.setCounts(techcounts);
                            commonJobs.setTechnicianname(username);
                            thisWeekArrayList.add(commonJobs);
                        }

                        try {
                            for (int a=0;a<techclosedjobsWeek.length();a++){
                                JSONObject second = techclosedjobsWeek.getJSONObject(a);
                                String normallevelid=second.getString("id");
                                // String normallevelidJob=second.getString("idJob");
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
                                commonJobs.setWeekClosedJobs(techclosedjobsWeek);
                                commonJobs.setCounts(techcounts);
                                commonJobs.setTechnicianname(username);
                                thisWeekClosedArrayList.add(commonJobs);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    if (thisWeekArrayList.size()!=0){
                        this_week_tech_recycler=chart.findViewById(R.id.this_week_tech_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        this_week_tech_recycler.setLayoutManager(layoutManager);
                        //activity_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        this_week_tech_recycler.setItemAnimator(new DefaultItemAnimator());
                        TechnicianWeekAdapter technicianweeekAllAdapter=new TechnicianWeekAdapter(getContext(), thisWeekArrayList);
                        this_week_tech_recycler.setAdapter(technicianweeekAllAdapter);
                        technicianweeekAllAdapter.notifyDataSetChanged();
                    }

                    if (thisWeekClosedArrayList.size()!=0){
                        this_week_tech_recycler=chart.findViewById(R.id.this_week_tech_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        this_week_tech_recycler.setLayoutManager(layoutManager);
                        //activity_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        this_week_tech_recycler.setItemAnimator(new DefaultItemAnimator());
                        TechnicianWeekAdapter technicianweeekClosedAdapter=new TechnicianWeekAdapter(getContext(), thisWeekClosedArrayList);
                        this_week_tech_recycler.setAdapter(technicianweeekClosedAdapter);
                        technicianweeekClosedAdapter.notifyDataSetChanged();
                    }

                    JSONArray techtoday=technicianAndJobsLayer.getJSONArray("today");
                    for (int i=0;i<techtoday.length();i++){
                        JSONObject first = techtoday.getJSONObject(i);
                        String username=first.getString("username");
                        JSONArray techcounts=first.getJSONArray("counts");
                        techtodayallJobs=first.getJSONArray("allJobs");
                        techclosedjobsToday=first.getJSONArray("closedJobs");
                        for (int a=0;a<techtodayallJobs.length();a++){
                            JSONObject second = techtodayallJobs.getJSONObject(a);
                            String normallevelid=second.getString("id");
                            // String normallevelidJob=second.getString("idJob");
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
                            commonJobs.setDayAllJobs(techtodayallJobs);
                            commonJobs.setCounts(techcounts);
                            commonJobs.setTechnicianname(username);
                            commonJobs.setCounts(techcounts);
                            commonJobs.setTechnicianname(username);
                            todayArrayList.add(commonJobs);
                        }

                        try {
                            for (int a=0;a<techclosedjobsToday.length();a++){
                                JSONObject second = techclosedjobsToday.getJSONObject(a);
                                String normallevelid=second.getString("id");
                                // String normallevelidJob=second.getString("idJob");
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
                                commonJobs.setDayClosedJobs(techclosedjobsToday);
                                commonJobs.setCounts(techcounts);
                                commonJobs.setTechnicianname(username);
                                todayClosedArrayList.add(commonJobs);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    if (todayArrayList.size()!=0){
                        today_tech_recycler=chart.findViewById(R.id.today_tech_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        today_tech_recycler.setLayoutManager(layoutManager);
                        today_tech_recycler.setItemAnimator(new DefaultItemAnimator());
                        TechnicianDayAdapter techniciantodayallAdapter=new TechnicianDayAdapter(getContext(), todayArrayList);
                        today_tech_recycler.setAdapter(techniciantodayallAdapter);
                        techniciantodayallAdapter.notifyDataSetChanged();
                    }

                    if (todayClosedArrayList.size()!=0){
                        today_tech_recycler=chart.findViewById(R.id.today_tech_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        today_tech_recycler.setLayoutManager(layoutManager);
                        today_tech_recycler.setItemAnimator(new DefaultItemAnimator());
                        TechnicianDayAdapter techniciantodayclosedAdapter=new TechnicianDayAdapter(getContext(), todayClosedArrayList);
                        today_tech_recycler.setAdapter(techniciantodayclosedAdapter);
                        techniciantodayclosedAdapter.notifyDataSetChanged();
                    }

                    JSONObject thirdLayer=object.getJSONObject("thirdLayer");
                    customer=thirdLayer.getJSONArray("customer");
                    for (int i=0;i<customer.length();i++){
                        JSONObject last = customer.getJSONObject(i);
                        String customerName=last.getString("customerName");
                        if (customerName==null){
                            status_one_chart.setVisibility(View.VISIBLE);
                            bubbleChartView.setVisibility(View.GONE);
                        }
                        String jobsCount=last.getString("jobsCount");
                        BubbleValue value = new BubbleValue(i, 0, Integer.parseInt(jobsCount));
                        value.setColor(ChartUtils.pickColor());
                        value.setShape(shape);
                        values.add(value);
                    }

                    bubbleChartData = new BubbleChartData(values);
                    bubbleChartData.setHasLabels(hasLabels);
                    bubbleChartData.setHasLabelsOnlyForSelected(hasLabelForSelected);
                    bubbleChartData.setMinBubbleRadius(10);

                    if (hasAxes) {
                        Axis axisX = new Axis();
                        Axis axisY = new Axis().setHasLines(true);
                        if (hasAxesNames) {
                            axisX.setName("");
                            axisY.setName("");
                        }
                        bubbleChartData.setAxisXBottom(axisX);
                        bubbleChartData.setAxisYLeft(axisY);
                    } else {
                        bubbleChartData.setAxisXBottom(null);
                        bubbleChartData.setAxisYLeft(null);
                    }

                    bubbleChartView.setBubbleChartData(bubbleChartData);

                    bubbleChartView.setOnValueTouchListener(new ValueTouchListener());

                    site=thirdLayer.getJSONArray("site");
                    for (int i=0;i<site.length();i++){
                        JSONObject last = site.getJSONObject(i);
                        String siteName=last.getString("siteName");
                        if (siteName==null){
                            status_one_site.setVisibility(View.VISIBLE);
                            sitejobs.setVisibility(View.GONE);
                        }
                        String jobsCount=last.getString("jobsCount");
                        BubbleValue value = new BubbleValue(i, 0, Integer.parseInt(jobsCount));
                        value.setColor(ChartUtils.pickColor());
                        value.setShape(shape);
                        sitevalues.add(value);
                    }

                    siteChartData = new BubbleChartData(sitevalues);
                    siteChartData.setHasLabels(hasLabels);
                    siteChartData.setHasLabelsOnlyForSelected(hasLabelForSelected);
                    siteChartData.setMinBubbleRadius(1);

                    if (hasAxes) {
                        Axis axisX = new Axis();
                        Axis axisY = new Axis().setHasLines(true);
                        if (hasAxesNames) {
                            axisX.setName("");
                            axisY.setName("");
                        }
                        siteChartData.setAxisXBottom(axisX);
                        siteChartData.setAxisYLeft(axisY);
                    } else {
                        siteChartData.setAxisXBottom(null);
                        siteChartData.setAxisYLeft(null);
                    }

                    sitejobs.setBubbleChartData(siteChartData);

                    sitejobs.setOnValueTouchListener(new ValueTouchListenerSite());

                    JSONObject status=thirdLayer.getJSONObject("status");
                    JSONArray createdJobs=status.getJSONArray("createdJobs");
                    JSONArray canceledJobs=status.getJSONArray("canceledJobs");
                    JSONArray completedJobs=status.getJSONArray("completedJobs");
                    JSONArray pausedJobs=status.getJSONArray("pausedJobs");
                    JSONArray scheduledJobs=status.getJSONArray("scheduledJobs");
                    JSONArray startedJobs=status.getJSONArray("startedJobs");
                    JSONArray synchronizedJobs=status.getJSONArray("synchronizedJobs");
                    JSONArray validatedJobs=status.getJSONArray("validatedJobs");
                    JSONArray jobscounts=status.getJSONArray("counts");
                    ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                    ArrayList<String> xVals = new ArrayList<String>();
                    for (int i = 0; i < (jobscounts.length()-1); i++) {
                        JSONObject statuss = jobscounts.getJSONObject(i);
                        int count=statuss.getInt("count");
                        String name=statuss.getString("name");
                        yVals1.add(new BarEntry(count , i));
                        xVals.add(name+"");
                    }

                    BarDataSet set1 = new BarDataSet(yVals1, "");
                    set1.setColors(ColorTemplate.COLORFUL_COLORS);
                    set1.setDrawValues(false);

                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                    dataSets.add(set1);

                    BarData barData = new BarData(xVals, dataSets);

                    bar_by_status.setData(barData);
                    bar_by_status.invalidate();

                    bar_by_status.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                            Intent i=new Intent(getContext(), StatusJobsList.class);
                        if (h.getXIndex()==0){
                            i.putExtra("statusindex",createdJobs.toString());
                        }else if (h.getXIndex()==1){
                            i.putExtra("statusindex",scheduledJobs.toString());
                        }else if (h.getXIndex()==2){
                            i.putExtra("statusindex",synchronizedJobs.toString());
                        }else if (h.getXIndex()==3){
                            i.putExtra("statusindex",startedJobs.toString());
                        }else if (h.getXIndex()==4){
                            i.putExtra("statusindex",pausedJobs.toString());
                        }else if (h.getXIndex()==5){
                            i.putExtra("statusindex",completedJobs.toString());
                        }else if (h.getXIndex()==6){
                            i.putExtra("statusindex",validatedJobs.toString());
                        }else if (h.getXIndex()==7){
                            i.putExtra("statusindex",canceledJobs.toString());
                        }
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
                params.put("idDomain",store.getIdDomain());
                params.put("Authorization", store.getToken());
                return params;
            }

        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20*10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private class ValueTouchListener implements BubbleChartOnValueSelectListener {

        @Override
        public void onValueSelected(int bubbleIndex, BubbleValue value) {
            Intent i=new Intent(getContext(), CustomerJobsList.class);
            i.putExtra("customerjobs",customer.toString());
            startActivity(i);
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }

    private class ValueTouchListenerSite implements BubbleChartOnValueSelectListener {

        @Override
        public void onValueSelected(int bubbleIndex, BubbleValue value) {
            Intent i=new Intent(getContext(), SiteJobsList.class);
            i.putExtra("sitejobs",site.toString());
            startActivity(i);
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

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