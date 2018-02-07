package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.SkilledTradersAdapter;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.common.EndURL;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rohin on 18-12-2017.
 */

public class AddJobsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextView scheduling_date,priority_text,scheduling_time;
    private Spinner job_type_spinner,user_spinner,team_spinner,window_spinner,site_auto_complete,equipment_auto_complete;
    private EditText jobs_first_name,jobs_last_name,phone_jobs_edit,mobile_jobs_edit,
            email_jobs_edit,job_number_edit,description_edit,additional_address_auto_complete;
    private PreferenceManager store;
    private Button save_button,save_schedule;
    private TextView tags_add_jobs_text;
    private RecyclerView tags_selected_jobs;
    ArrayList<CommonJobs> jobTypeArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> technicianArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> schedulingArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> teamsArrayList = new ArrayList<CommonJobs>();
    private List<String> spinnerlist =new ArrayList<String>();
    private List<String> technicianlist=new ArrayList<String>();
    private List<String> teamslist=new ArrayList<String>();
    private List<String> schedulinglist=new ArrayList<String>();
    private List<String> cusnamestring=new ArrayList<String>();
    private List<String> sitenamestring=new ArrayList<String>();
    private List<String> equipnamestring=new ArrayList<String>();
    ArrayList<CommonJobs> cusDetailsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> siteDetailsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> equipDetailsArrayList = new ArrayList<CommonJobs>();
    ArrayList<String>tagsArrayList=new ArrayList<String>();
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    private String domainid=null;
    private String techniciantext=null;
    private String jobtypetext=null;
    private String schedulingtext=null;
    private String teamstext=null;
    private LinearLayout priority_linear;
    private AlertDialog prioritydialog;
    private String selectedpriority=null;
    private TimePickerDialog timePickerDialog;
    private String starttime=null;
    private AutoCompleteTextView customer_auto_complete,address_auto_complete;
    private String priority=null;
    private String address=null;
    private String firstname=null;
    private String lastname=null;
    private String phone=null;
    private String mobile=null;
    private String email=null;
    private String userid=null;
    private String siteid=null;
    private String equipmentid=null;
    private String customerid=null;
    private String myid=null;
    private String description=null;
    private String globalAddress=null;
    private String complementaddress=null;
    private double latitude=0;
    private double longitude=0;
    private String customer;
    private AlertDialog tagsDialog=null;
    private SkilledTradersAdapter adapter;
    private SkilledTradersAdapter selectedAdapter;
    private List<String>tagsList=new ArrayList<>();
    private JSONArray tagarray=null;
    private int saveclick;
    public String job_type_name;
    private String customertext=null;
    private String sitetext=null;
    private String equiptext=null;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_jobs);
        store= PreferenceManager.getInstance(getApplicationContext());
        scheduling_date=findViewById(R.id.scheduling_date);
        job_type_spinner=findViewById(R.id.job_type_spinner);
        priority_linear=findViewById(R.id.priority_linear);
        priority_text=findViewById(R.id.priority_text);
        window_spinner=findViewById(R.id.window_spinner);
        scheduling_time=findViewById(R.id.scheduling_time);
        user_spinner=findViewById(R.id.user_spinner);
        team_spinner=findViewById(R.id.team_spinner);
        customer_auto_complete=findViewById(R.id.customer_auto_complete);
        site_auto_complete=findViewById(R.id.site_auto_complete);
        equipment_auto_complete=findViewById(R.id.equipment_auto_complete);
        jobs_first_name=findViewById(R.id.jobs_first_name);
        jobs_last_name=findViewById(R.id.jobs_last_name);
        phone_jobs_edit=findViewById(R.id.phone_jobs_edit);
        mobile_jobs_edit=findViewById(R.id.mobile_jobs_edit);
        email_jobs_edit=findViewById(R.id.email_jobs_edit);
        address_auto_complete=findViewById(R.id.address_auto_complete);
        additional_address_auto_complete=findViewById(R.id.additional_address_auto_complete);
        save_button=findViewById(R.id.save_button);
        save_schedule=findViewById(R.id.save_schedule);
        description_edit=findViewById(R.id.description_edit);
        job_number_edit=findViewById(R.id.job_number_edit);
        tags_add_jobs_text=findViewById(R.id.tags_add_jobs_text);
        tags_selected_jobs=findViewById(R.id.tags_selected_jobs);
        domainid=store.getIdDomain();
        userid=store.getUserid();
        GetSpinnersList();

        scheduling_time.setOnClickListener(view -> {
            starttimePicker();
        });

        //tags_add_jobs_text.setOnClickListener(view -> skilledTradesDialog(new ArrayList<>(),tags_add_jobs_text));

        save_button.setOnClickListener(view -> {
            saveclick=1;
            customer=customer_auto_complete.getText() .toString();
            if (customer==null && customer.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Customer Name!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String sitename = null;
            try {
                sitename=site_auto_complete.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(sitename==null){
                Toast.makeText(getApplicationContext(), "Please Select Site Name!", Toast.LENGTH_SHORT).show();
                return;
            }
            globalAddress=address_auto_complete.getText() .toString();
            if (globalAddress==null && globalAddress.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Address!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String equipmentname = null;
            try {
                equipmentname=equipment_auto_complete.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(equipmentname==null){
                Toast.makeText(getApplicationContext(), "Please Select Equipment Name!", Toast.LENGTH_SHORT).show();
                return;
            }
            myid=job_number_edit.getText().toString();
            if (myid==null && myid.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Custom Job!", Toast.LENGTH_SHORT).show();
                return ;
            }
            description=description_edit.getText().toString();
            if (description==null && description.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Custom Job!", Toast.LENGTH_SHORT).show();
                return ;
            }
            priority=priority_text.getText().toString();
            if (priority==null && priority.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select Priority!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String jobtype = null;
            try {
                jobtype=job_type_spinner.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(jobtype==null){
                Toast.makeText(getApplicationContext(), "Please Select Job Type!", Toast.LENGTH_SHORT).show();
                return;
            }
            complementaddress=additional_address_auto_complete.getText().toString();
            firstname=jobs_first_name.getText().toString();
            lastname=jobs_last_name.getText().toString();
            phone=phone_jobs_edit.getText().toString();
            mobile=mobile_jobs_edit.getText().toString();
            email=email_jobs_edit.getText().toString();
            if(customer!=null &&!customer.isEmpty()&&sitename!=null&& jobtype!=null&&equipmentname!=null
                    &&globalAddress!=null &&!globalAddress.isEmpty()&&myid!=null &&!myid.isEmpty()&&description!=null &&!description.isEmpty()
                    &&priority!=null &&!priority.isEmpty()){
                GetLatLngList(globalAddress);
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();

            }

        });

        save_schedule.setOnClickListener(view -> {
            saveclick=2;
            customer=customer_auto_complete.getText() .toString();
            if (customer==null && customer.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Customer Name!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String sitename = null;
            try {
                sitename=site_auto_complete.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(sitename==null){
                Toast.makeText(getApplicationContext(), "Please Select Site Name!", Toast.LENGTH_SHORT).show();
                return;
            }
            globalAddress=address_auto_complete.getText() .toString();
            if (globalAddress==null && globalAddress.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Address!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String equipmentname = null;
            try {
                equipmentname=equipment_auto_complete.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(equipmentname==null){
                Toast.makeText(getApplicationContext(), "Please Select Equipment Name!", Toast.LENGTH_SHORT).show();
                return;
            }
            myid=job_number_edit.getText().toString();
            if (myid==null && myid.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Custom Job!", Toast.LENGTH_SHORT).show();
                return ;
            }
            description=description_edit.getText().toString();
            if (description==null && description.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Custom Job!", Toast.LENGTH_SHORT).show();
                return ;
            }
            priority=priority_text.getText().toString();
            if (priority==null && priority.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select Priority!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String jobtype = null;
            try {
                jobtype=job_type_spinner.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(jobtype==null){
                Toast.makeText(getApplicationContext(), "Please Select Job Type!", Toast.LENGTH_SHORT).show();
                return;
            }
            complementaddress=additional_address_auto_complete.getText().toString();
            firstname=jobs_first_name.getText().toString();
            lastname=jobs_last_name.getText().toString();
            phone=phone_jobs_edit.getText().toString();
            mobile=mobile_jobs_edit.getText().toString();
            email=email_jobs_edit.getText().toString();
            if(customer!=null &&!customer.isEmpty()&&sitename!=null&& jobtype!=null&&equipmentname!=null
                    &&globalAddress!=null &&!globalAddress.isEmpty()&&myid!=null &&!myid.isEmpty()&&description!=null &&!description.isEmpty()
                    &&priority!=null &&!priority.isEmpty()){
                GetLatLngList(globalAddress);
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();

            }
        });

        scheduling_date.setOnClickListener(view -> {
            Calendar mcurrentDate=Calendar.getInstance();
            final int mYear = mcurrentDate.get(Calendar.YEAR);
            final int mMonth=mcurrentDate.get(Calendar.MONTH);
            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(AddJobsActivity.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    int month=selectedmonth+1;
                    String monts=String.format("%02d",month);
                    String currentdate=String.valueOf(selectedyear+"-"+monts+"-"+selectedday+" ");
                    scheduling_date.setText(currentdate);

                }
            },mYear, mMonth, mDay);
            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                mDatePicker.setTitle("Select date");
            mDatePicker.show();
        });

        priority_linear.setOnClickListener(view -> {
            showPriorityDialog();

        });

    }

    private void skilledTradesDialog(List<String> skilledName,TextView tView) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AddJobsActivity.this);
        LayoutInflater layoutInflate=getLayoutInflater();
        View view=layoutInflate.inflate(R.layout.adapter_teams_list,null);
        EditText editText=view.findViewById(R.id.et_skilled);
        editText.setHint("Enter tags");
        Button button=view.findViewById(R.id.button_add_skill);
        RecyclerView recyclerView=view.findViewById(R.id.skilled_traders_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new SkilledTradersAdapter(this,skilledName);
        recyclerView.setAdapter(adapter);
        builder.setView(view);
        button.setOnClickListener(views->{
            String edName=editText.getText().toString();
            if(edName!=null && !edName.isEmpty()){
                adapter.setSkilledTradersName(edName);
                editText.setText("");
            }
        });
        builder.setPositiveButton("OK",(dialog,id)->{
            tagsList=adapter.getSkilledTradersList();
            if(tagsList.size()!=0){
                tags_selected_jobs.setVisibility(View.VISIBLE);
                selectedAdapter=new SkilledTradersAdapter(this,tagsList);
                tags_selected_jobs.setItemAnimator(new DefaultItemAnimator());
                tags_selected_jobs.setLayoutManager(new LinearLayoutManager(this));
                tags_selected_jobs.setAdapter(selectedAdapter);
                selectedAdapter.notifyDataSetChanged();
                for (int i=0;i<tagsList.size();i++){
                    AddTagsApi(tagsList.get(i));
                }
            }

        });
        builder.setNegativeButton("Cancel",(dialog,i)->{
            dismissTagsDialog();
        });
        tagsDialog=builder.create();
        tagsDialog.show();

    }
    private  void dismissTagsDialog(){
        if(tagsDialog!=null && tagsDialog.isShowing()){
            tagsDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissTagsDialog();

    }

    private void showPriorityDialog(){
        if(prioritydialog==null){
            AlertDialog.Builder builder=new AlertDialog.Builder(AddJobsActivity.this);
            LayoutInflater inflater=getLayoutInflater();
            View view=inflater.inflate(R.layout.priority_spinner_value,null);
            builder.setView(view);
            TextView low_priority=view.findViewById(R.id.low_priority);
            TextView normal_priority=view.findViewById(R.id.normal_priority);
            TextView high_priority=view.findViewById(R.id.high_priority);

            low_priority.setOnClickListener(view1 -> {
                selectedpriority="Low";
                prioritydialog.dismiss();
                if (selectedpriority!=null){
                    priority_text.setText(selectedpriority);
                }
            });

            normal_priority.setOnClickListener(view1 -> {
                selectedpriority="Normal";
                prioritydialog.dismiss();
                if (selectedpriority!=null){
                    priority_text.setText(selectedpriority);
                }
            });

            high_priority.setOnClickListener(view1 -> {
                selectedpriority="High";
                prioritydialog.dismiss();
                if (selectedpriority!=null){
                    priority_text.setText(selectedpriority);
                }
            });
            prioritydialog=builder.create();
            prioritydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            prioritydialog.setCancelable(false);
            prioritydialog.setCanceledOnTouchOutside(false);
        }
        prioritydialog.show();

    }

    private void starttimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog=new TimePickerDialog(AddJobsActivity.this, (timePicker, selectedHour, selectedMinute) -> onTimeSet(selectedHour,selectedMinute),hour,minute,false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
    private void onTimeSet(int hourOfDay, int minute) {
        String am_pm = "";
        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";
        String hours = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
        String finalTimeHour=String.format("%02d",Integer.parseInt(hours));
        String finalTimeMinute=String.format("%02d",datetime.get(Calendar.MINUTE));
        String timeValue=finalTimeHour+":"+finalTimeMinute+" "+am_pm;
        scheduling_time.setText(timeValue);
        starttime=timeValue;
    }
    private void GetLatLngList(String address) {
        String tag_json_obj = "json_obj_req";
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+address+"&key=AIzaSyD917w2chNF_jp_9W5f7s-yZ-jTqcYY3Lg";
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("results");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        JSONObject geometry=first.getJSONObject("geometry");
                        JSONObject location=geometry.getJSONObject("location");
                        double lat=location.getDouble("lat");
                        AddJobsActivity.this.latitude=lat;
                        double lng=location.getDouble("lng");
                        AddJobsActivity.this.longitude=lng;

                    }
                    AddJobsApi(myid,description,globalAddress,complementaddress,firstname,lastname,mobile,phone,email,jobtypetext,teamstext,schedulingtext,techniciantext,latitude,longitude);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();

            }
        }) {

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void GetSpinnersList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"consolidatedResult/addJob/"+domainid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                cusDetailsArrayList.clear();
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
                            e.printStackTrace();
                        }
                        String contactEmail="";
                        try {
                            contactEmail=first.getString("contactEmail");
                        }catch (Exception e){

                        }
                        String contactfirstname="";
                        try {
                            contactfirstname=first.getString("contactFirstName");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String contactlastname="";
                        try {
                            contactlastname=first.getString("contactLastName");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String contactmobile="";
                        try {
                            contactmobile=first.getString("contactMobile");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String contactphone="";
                        try {
                            contactphone=first.getString("contactPhone");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String address="";
                        try {
                            address=first.getString("address");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String addressComplement="";
                        try {
                            addressComplement=first.getString("addressComplement");
                        }catch (Exception e){

                        }
                        JSONObject tagInfo =null;
                        try {
                            tagInfo = first.getJSONObject("tagInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        String tagid=null;
                        try {
                            tagid=tagInfo.getString("id");
                            store.putTagId(String.valueOf(tagid));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String tagname="";
                        try {
                            tagname=tagInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setCustomername(name);
                        commonJobs.setCustomerid(customerid);
                        AddJobsActivity.this.customerid=customerid;
                        commonJobs.setFirstname(contactfirstname);
                        commonJobs.setLastname(contactlastname);
                        commonJobs.setMobilenum(contactmobile);
                        commonJobs.setPhone(contactphone);
                        commonJobs.setAddress(address);
                        commonJobs.setTagname(tagname);
                        commonJobs.setComplementAddress(addressComplement);
                        commonJobs.setEmail(contactEmail);
                        cusDetailsArrayList.add(commonJobs);
                        cusnamestring.add(name);

                    }
                    String[]customer=cusnamestring.stream().toArray(String[]::new);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (AddJobsActivity.this,android.R.layout.select_dialog_item,customer);
                    customer_auto_complete= findViewById(R.id.customer_auto_complete);
                    customer_auto_complete.setThreshold(1);//will start working from first character
                    customer_auto_complete.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                    customer_auto_complete.setTextColor(R.color.text_light);

                    customer_auto_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String customername=customer[i];
                            bindViews(customername);
                            customertext=customername;
                            GetSiteList(customertext);
                        }
                    });

                    JSONArray jobTypes=obj.getJSONArray("jobTypes");
                    for (int i=0;i<jobTypes.length();i++){
                        JSONObject first=jobTypes.getJSONObject(i);
                        String jobtypeid=first.getString("id");
                        store.putIdJobType(String.valueOf(jobtypeid));
                        String typename=first.getString("job_type_name");
                        job_type_name=typename;

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setIdjobtype(jobtypeid);
                        commonJobs.setJobTypeName(job_type_name);
                        jobTypeArrayList.add(commonJobs);
                        spinnerlist.add(job_type_name);

                    }

                    for (String s:spinnerlist) {

                    }
                    ArrayAdapter<String> adapters=new  ArrayAdapter<String>(AddJobsActivity.this,android.R.layout.simple_spinner_item,
                            spinnerlist);
                    adapters.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    job_type_spinner.setPrompt("Job Type");
                    job_type_spinner.setAdapter(adapters);
                    job_type_spinner.setAdapter(new SpinnerDetails(adapters,R.layout.job_type_spinner,AddJobsActivity.this));
                    job_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = job_type_spinner.getSelectedItem().toString();
                                jobtypetext=text;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    JSONArray technicians=obj.getJSONArray("technicians");
                    for (int i=0;i<technicians.length();i++){
                        JSONObject first=technicians.getJSONObject(i);
                        String technicianid=first.getString("id");
                        store.putTechnicianId(String.valueOf(technicianid));
                        String techusername=first.getString("username");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setTechnicianid(technicianid);
                        commonJobs.setTechnicianname(techusername);
                        technicianArrayList.add(commonJobs);
                        technicianlist.add(techusername);

                    }

                    for (String s:technicianlist) {

                    }
                    ArrayAdapter<String> stringArrayAdapter=new  ArrayAdapter<String>(AddJobsActivity.this,android.R.layout.simple_spinner_item,
                            technicianlist);
                    stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    user_spinner.setPrompt("Technician");
                    user_spinner.setAdapter(stringArrayAdapter);
                    user_spinner.setAdapter(new SpinnerDetails(stringArrayAdapter,R.layout.technician,AddJobsActivity.this));
                    user_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = user_spinner.getSelectedItem().toString();
                                techniciantext=text;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    JSONArray schedulingWindows=obj.getJSONArray("schedullingWindows");
                    for (int i=0;i<schedulingWindows.length();i++){
                        JSONObject first=schedulingWindows.getJSONObject(i);
                        String schedulingid=first.getString("id");
                        store.putSchedulingId(String.valueOf(schedulingid));
                        String schedulingname=first.getString("name");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setSchedulingid(schedulingid);
                        commonJobs.setSchedulingname(schedulingname);
                        schedulingArrayList.add(commonJobs);
                        schedulinglist.add(schedulingname);

                    }

                    for (String s:schedulinglist) {

                    }
                    ArrayAdapter<String> adapter1=new  ArrayAdapter<String>(AddJobsActivity.this,android.R.layout.simple_spinner_item,
                            schedulinglist);
                    adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    window_spinner.setPrompt("Scheduling");
                    window_spinner.setAdapter(adapter1);
                    window_spinner.setAdapter(new SpinnerDetails(adapter1,R.layout.window_spinner_value,AddJobsActivity.this));
                    window_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = window_spinner.getSelectedItem().toString();
                                schedulingtext=text;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    JSONArray teams=obj.getJSONArray("teams");
                    for (int i=0;i<teams.length();i++){
                        JSONObject first=teams.getJSONObject(i);
                        String teamid=first.getString("id");
                        store.putTeamId(String.valueOf(teamid));
                        String teamusername=first.getString("name");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setTeamid(teamid);
                        commonJobs.setTeamname(teamusername);
                        teamsArrayList.add(commonJobs);
                        teamslist.add(teamusername);

                    }

                    for (String s:teamslist) {

                    }
                    ArrayAdapter<String> teamsadapter=new  ArrayAdapter<String>(AddJobsActivity.this,android.R.layout.simple_spinner_item,
                            teamslist);
                    teamsadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    team_spinner.setPrompt("Technician");
                    team_spinner.setAdapter(teamsadapter);
                    team_spinner.setAdapter(new SpinnerDetails(teamsadapter,R.layout.teams_spinner,AddJobsActivity.this));
                    team_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = team_spinner.getSelectedItem().toString();
                                teamstext=text;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    JSONArray tags=obj.getJSONArray("tags");
                    tagarray=new JSONArray(tags.toString());
                    for (int i=0;i<tags.length();i++){
                        JSONObject first=tags.getJSONObject(i);
                        String tagid=first.getString("id");
                        String tagname=first.getString("name");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setTagid(tagid);
                        commonJobs.setTagname(tagname);
                        commonJobsArrayList.add(commonJobs);
                        tagsArrayList.add(tagname);

                    }

                    if (tagsArrayList.size()!=0){
                        tags_selected_jobs.setVisibility(View.VISIBLE);
                        tags_selected_jobs = findViewById(R.id.tags_selected_jobs);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                        tags_selected_jobs.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        tags_selected_jobs.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        SkilledTradersAdapter projectsAdapter = new SkilledTradersAdapter(AddJobsActivity.this,tagsArrayList);
                        tags_selected_jobs.setAdapter(projectsAdapter);
                        projectsAdapter.notifyDataSetChanged();
                    }else {
                        tags_selected_jobs.setVisibility(View.GONE);
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

    private void GetSiteList(String customername) {
        String customid="";
        for (int i=0;i<cusDetailsArrayList.size();i++){
            if (customername.equals(cusDetailsArrayList.get(i).getCustomername())){
                customid=cusDetailsArrayList.get(i).getCustomerid();
            }
        }
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"sites/getByCustomerId/"+customid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                siteDetailsArrayList.clear();
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
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setSitename(sitename);
                        commonJobs.setSiteid(siteid);
                        AddJobsActivity.this.siteid=siteid;
                        siteDetailsArrayList.add(commonJobs);
                        sitenamestring.add(sitename);

                    }

                    for (String s:sitenamestring) {

                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(AddJobsActivity.this,android.R.layout.simple_spinner_item,
                            sitenamestring);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    site_auto_complete.setPrompt("Site");
                    site_auto_complete.setAdapter(adapter);
                    site_auto_complete.setAdapter(new SpinnerDetails(adapter,R.layout.site_spinner,AddJobsActivity.this));
                    site_auto_complete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = site_auto_complete.getSelectedItem().toString();
                                sitetext=text;
                                GetEquipmentList(sitetext);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

//                    String[]site=sitenamestring.stream().toArray(String[]::new);
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                            (AddJobsActivity.this,android.R.layout.select_dialog_item,site);
//                    site_auto_complete= findViewById(R.id.site_auto_complete);
//                    site_auto_complete.setThreshold(1);//will start working from first character
//                    site_auto_complete.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
//                    site_auto_complete.setTextColor(R.color.text_light);
//
//                    site_auto_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            String sitename=site[i];
//                            bindSites(sitename);
//                            GetEquipmentList(sitename);
//                        }
//                    });

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

    private void GetEquipmentList(String sitename) {

        String siteid="";
        for (int i=0;i<siteDetailsArrayList.size();i++){
            if (sitename.equals(siteDetailsArrayList.get(i).getSitename())){
                siteid=siteDetailsArrayList.get(i).getSiteid();
            }
        }
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"equipments/getBySiteId/"+siteid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                equipDetailsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("equipments");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String equipmentid=first.getString("id");
                        store.putEquipmentId(String.valueOf(equipmentid));
                        String equipname="";
                        try {
                            equipname=first.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setEquipid(equipmentid);
                        AddJobsActivity.this.equipmentid=equipmentid;
                        commonJobs.setEquipname(equipname);
                        equipDetailsArrayList.add(commonJobs);
                        equipnamestring.add(equipname);

                    }

                    for (String s:equipnamestring) {

                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(AddJobsActivity.this,android.R.layout.simple_spinner_item,
                            equipnamestring);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    equipment_auto_complete.setPrompt("Equipment");
                    equipment_auto_complete.setAdapter(adapter);
                    equipment_auto_complete.setAdapter(new SpinnerDetails(adapter,R.layout.equipment_spinner,AddJobsActivity.this));
                    equipment_auto_complete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = equipment_auto_complete.getSelectedItem().toString();
                                equiptext=text;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

//                    String[]equipment=equipnamestring.stream().toArray(String[]::new);
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                            (AddJobsActivity.this,android.R.layout.select_dialog_item,equipment);
//                    equipment_auto_complete= findViewById(R.id.equipment_auto_complete);
//                    equipment_auto_complete.setThreshold(1);//will start working from first character
//                    equipment_auto_complete.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
//                    equipment_auto_complete.setTextColor(R.color.text_light);
//
//                    equipment_auto_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            String equipname=equipment[i];
//                            bindEquipments(equipname);
//                        }
//                    });

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

    private void AddTagsApi(String name) {

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "tags/add";
        Log.d("waggonurl", url);
        JSONObject inputLogin = new JSONObject();

        try {
            inputLogin.put("name",name);
            inputLogin.put("idDomain",domainid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("inputJsonuser", inputLogin.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response", response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("tag");
                        String tagid=first.getString("id");
                        store.putTagId(String.valueOf(tagid));

                        tagsArrayList.add(tagid);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.e("Error", "" + error.toString());
                }
//                texts.setText(error.toString());
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

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(AddJobsActivity.this);
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

    private void AddJobsApi(String myid, String description, String globalAddress, String complementaddress, String firstname,
                            String lastname, String mobile, String phone, String email, String jobtypetext, String teamstext,
                            String schedulingtext, String techniciantext, double latitude, double longitude) {

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "jobs/add";
        Log.d("waggonurl", url);
        showProgressDialog();
        JSONObject inputLogin = new JSONObject();

        String customid=null;
        for (int i=0;i<cusDetailsArrayList.size();i++){
            String customname=cusDetailsArrayList.get(i).getCustomername();
            if (customertext!=null){
                if (customertext.equals(customname)) {
                    customid=cusDetailsArrayList.get(i).getCustomerid();
                }
            }
        }
        String sitid=null;
        for (int i=0;i<siteDetailsArrayList.size();i++){
            String sitename=siteDetailsArrayList.get(i).getSitename();
            if (sitetext!=null){
                if (sitetext.equals(sitename)) {
                    sitid=siteDetailsArrayList.get(i).getSiteid();
                }
            }
        }
        String equipid=null;
        for (int i=0;i<equipDetailsArrayList.size();i++){
            String equipname=equipDetailsArrayList.get(i).getEquipname();
            if (equiptext!=null){
                if (equiptext.equals(equipname)) {
                    equipid=equipDetailsArrayList.get(i).getEquipid();
                }
            }
        }

        String technicianid=null;
        for (int i=0;i<technicianArrayList.size();i++){
            String techName=technicianArrayList.get(i).getTechnicianname();
            if (techniciantext!=null){
                if (techniciantext.equals(techName)) {
                    technicianid=technicianArrayList.get(i).getTechnicianid();
                }
            }
        }
        String jobtypeid=null;
        for (int i=0;i<jobTypeArrayList.size();i++){
            String typename=jobTypeArrayList.get(i).getJobTypeName();

            if (jobtypetext!=null){
                if (jobtypetext.equals(typename)) {
                    jobtypeid=jobTypeArrayList.get(i).getIdjobtype();
                }
            }

        }

        String teamid=null;
        for (int i=0;i<teamsArrayList.size();i++){
            String teamname=teamsArrayList.get(i).getTeamname();
            if (teamstext!=null){
                if (teamstext.equals(teamname)) {
                    teamid=teamsArrayList.get(i).getTeamid();
                }
            }

        }

        String schedulingid=null;
        for (int i=0;i<schedulingArrayList.size();i++){
            String schedulingname=schedulingArrayList.get(i).getSchedulingname();
            if (schedulingtext!=null){
                if (schedulingtext.equals(schedulingname)) {
                    schedulingid=schedulingArrayList.get(i).getSchedulingid();
                }
            }

        }
            JSONObject position=new JSONObject();
        try {
            position.put("lat",latitude);
            position.put("lng",longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String>tagListId=new ArrayList<String>();
        for (int i=0;i<tagarray.length();i++){
            JSONObject first= null;
            try {
                first = tagarray.getJSONObject(i);
                String tagid=first.getString("id");
                String tagname=first.getString("name");
                if (tagsArrayList.get(i).equals(tagname)){
                    tagListId.add(tagid);
                }
            } catch (JSONException e) {
            }

        }

        JSONArray jsonArray=new JSONArray();
        for (int i=0;i<tagListId.size();i++){
            jsonArray.put(tagListId.get(i));
        }

        try {
            inputLogin.put("myId", myid);
            inputLogin.put("idJobType", jobtypeid);
            inputLogin.put("idCustomer",customid);
            inputLogin.put("idSite",sitid);
            inputLogin.put("idEquipment",equipid);
            inputLogin.put("idCreator",userid);
            inputLogin.put("idDomain",domainid);
            inputLogin.put("description",description);
            inputLogin.put("priority",selectedpriority);
            inputLogin.put("globalAddress",globalAddress);
            inputLogin.put("complementAddress",complementaddress);
            inputLogin.put("contactFirstName",firstname);
            inputLogin.put("contactLastName",lastname);
            inputLogin.put("contactMobile",mobile);
            inputLogin.put("contactPhone",phone);
            inputLogin.put("contactEmail",email);
            inputLogin.put("idSchedulingWindow",schedulingid);
            inputLogin.put("schedullingPreferredDate",scheduling_date.getText().toString());
            inputLogin.put("schedullingPreferredTeam",teamid);
            inputLogin.put("schedullingpreferredUser",technicianid);
            inputLogin.put("positions",position);
            inputLogin.put("tags",jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("inputJsonuser", inputLogin.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.d("List Response", response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("job");
                        String jobid=first.getString("id");
                        store.putJobId(String.valueOf(jobid));
                        String myId=first.getString("myId");
                        store.putmyId(String.valueOf(myId));

                        if (saveclick==1){
                            AddJobsActivity.this.finish();
                        }else {
                            Intent i=new Intent(AddJobsActivity.this,ActivityJobsSchedule.class);
                            i.putExtra("jobtypename",job_type_name);
                            i.putExtra("jobid",jobid);
                            startActivity(i);
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.e("Error", "" + error.toString());
                }
                dismissProgressDialog();
//                texts.setText(error.toString());
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

    private void bindViews(String customername) {
        for (int i=0;i<cusDetailsArrayList.size();i++){
            String cname=cusDetailsArrayList.get(i).getCustomername();
            if (cname.equals(customername)){
                    address=cusDetailsArrayList.get(i).getAddress();
                    complementaddress=cusDetailsArrayList.get(i).getComplementAddress();
                    firstname=cusDetailsArrayList.get(i).getFirstname();
                    lastname=cusDetailsArrayList.get(i).getLastname();
                    phone=cusDetailsArrayList.get(i).getPhone();
                    mobile=cusDetailsArrayList.get(i).getMobilenum();
                    email=cusDetailsArrayList.get(i).getEmail();
            }
        }
        if (address!=null){
            address_auto_complete.setText(address);
        }
        if (complementaddress!=null){
            additional_address_auto_complete.setText(complementaddress);
        }
        if (firstname!=null){
            jobs_first_name.setText(firstname);
        }
        if (lastname!=null){
            jobs_last_name.setText(lastname);
        }
        if (phone!=null){
            phone_jobs_edit.setText(phone);
        }
        if (mobile!=null){
            mobile_jobs_edit.setText(mobile);
        }
        if (email!=null){
            email_jobs_edit.setText(email);
        }
    }

//    private void bindSites(String sitename) {
//        for (int i=0;i<siteDetailsArrayList.size();i++){
//            String sname=siteDetailsArrayList.get(i).getSitename();
////            if (sname.equals(sitename)){
////                sitename=siteDetailsArrayList.get(i).getSitename();
////            }
//        }
//    }
//
//    private void bindEquipments(String equipname) {
//        for (int i=0;i<equipDetailsArrayList.size();i++){
//            String ename=equipDetailsArrayList.get(i).getEquipname();
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AddJobsActivity.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
