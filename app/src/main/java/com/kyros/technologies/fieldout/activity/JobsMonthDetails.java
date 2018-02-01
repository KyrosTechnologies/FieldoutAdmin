package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.common.EndURL;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rohin on 27-12-2017.
 */

public class JobsMonthDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextView status_jobs_details,priority_jobs_details,job_details_my_id,job_details_job_type,job_details_address,
            job_details_lat_lng,job_details_name,job_details_mobile,job_details_phone,job_details_email,job_details_desc,
            schedule_start_date,schedule_start_time,schedule_end_date,schedule_end_time;
    private Button validate_jobs_details,unschedule_jobs_details,reschedule_jobs_details;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> technicianArrayList = new ArrayList<CommonJobs>();
    private String domainid=null;
    private Spinner technician_spinner;
    private String userid=null;
    private String priority=null;
    private PreferenceManager store;
    private LinearLayout add_attachments;
    private String jobtype=null;
    private String address=null;
    private String myid=null;
    private String mobile=null;
    private String phone=null;
    private String desc=null;
    private String email=null;
    private String latlng=null;
    private String status=null;
    private String jobid=null;
    private String cusname=null;
    private String sitename=null;
    private String equipname=null;
    private String compaddress=null;
    private String firstname=null;
    private String lastname=null;
    private String date=null;
    private String taginfo=null;
    private String userjobid=null;
    private AlertDialog.Builder builder;
    private String startdate=null;
    private String enddate=null;
    private String starttime=null;
    private String endtime=null;
    private TimePickerDialog timePickerDialog,endtimePickerDialog;
    private String techniciantext=null;
    private List<String> spinnerlist=new ArrayList<String>();
    private String date_start;
    private String date_end;
    private String time_start;
    private String time_end;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.jobs_details);
        store= PreferenceManager.getInstance(getApplicationContext());
        status_jobs_details=findViewById(R.id.status_jobs_details);
        priority_jobs_details = findViewById(R.id.priority_jobs_details);
        job_details_my_id = findViewById(R.id.job_details_my_id);
        job_details_job_type = findViewById(R.id.job_details_job_type);
        job_details_address = findViewById(R.id.job_details_address);
        job_details_lat_lng = findViewById(R.id.job_details_lat_lng);
        job_details_name = findViewById(R.id.job_details_name);
        job_details_mobile=findViewById(R.id.job_details_mobile);
        job_details_phone=findViewById(R.id.job_details_phone);
        job_details_email=findViewById(R.id.job_details_email);
        job_details_desc=findViewById(R.id.job_details_desc);
        add_attachments=findViewById(R.id.add_attachments);
        validate_jobs_details=findViewById(R.id.validate_jobs_details);
        unschedule_jobs_details=findViewById(R.id.unschedule_jobs_details);
        schedule_start_date=findViewById(R.id.schedule_start_date);
        schedule_start_time=findViewById(R.id.schedule_start_time);
        schedule_end_date=findViewById(R.id.schedule_end_date);
        schedule_end_time=findViewById(R.id.schedule_end_time);
        technician_spinner=findViewById(R.id.technician_spinner);
        reschedule_jobs_details=findViewById(R.id.reschedule_jobs_details);
        userjobid=store.getUserid();
        domainid = store.getIdDomain();
        technician_spinner.setOnItemSelectedListener(this);
        GetTechnicianList();

        try {

            Bundle bundle = getIntent().getExtras();
            jobid=bundle.getString("jobid");
            cusname=bundle.getString("cusname");
            sitename=bundle.getString("sitename");
            equipname=bundle.getString("equipname");
            compaddress=bundle.getString("compaddress");
            firstname=bundle.getString("firstname");
            lastname=bundle.getString("lastname");
            date=bundle.getString("date");
            status=bundle.getString("status");
            priority = bundle.getString("priority");
            jobtype = bundle.getString("jobtypename");
            address = bundle.getString("address");
            myid = bundle.getString("myid");
            mobile = bundle.getString("mobile");
            phone = bundle.getString("phone");
            desc = bundle.getString("desc");
            email = bundle.getString("email");
            latlng=bundle.getString("latlng");
            taginfo=bundle.getString("tags");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (status!=null){
            status_jobs_details.setText(status);
        }
        if (priority != null) {
            priority_jobs_details.setText(priority);
        }
        if (myid != null) {
            job_details_my_id.setText(myid);
        }
        if (jobtype != null) {
            job_details_job_type.setText(jobtype);
        }
        if (address != null) {
            job_details_address.setText(address);
        }
        if (latlng!=null){
            job_details_lat_lng.setText(latlng);
        }
        if (firstname!=null&&lastname!=null){
            job_details_name.setText(firstname+" "+lastname);
        }
        if (mobile!=null){
            job_details_mobile.setText(mobile);
        }
        if (phone!=null){
            job_details_phone.setText(phone);
        }
        if (email!=null){
            job_details_email.setText(email);
        }
        if (desc!=null){
            job_details_desc.setText(desc);
        }

        schedule_start_time.setOnClickListener(view -> {
            starttimePicker();
        });

        schedule_end_time.setOnClickListener(view -> {
            endtimePicker();
        });

        validate_jobs_details.setOnClickListener(view -> {
            if (status.equals("completed")){

                UpdateValidateApi();
            }else {
                Toast.makeText(getApplicationContext(),"Job Need to be Completed to Validate",Toast.LENGTH_SHORT).show();
            }
        });
        unschedule_jobs_details.setOnClickListener(view -> {
            if (status.equals("scheduled")&&status.equals("canceled")){

                showDeleteDialog();
            }else {
                Toast.makeText(getApplicationContext(),"Started Jobs cannot be UnScheduled",Toast.LENGTH_SHORT).show();
            }

        });

        reschedule_jobs_details.setOnClickListener(view -> {
            date_start=schedule_start_date.getText() .toString();
            if (date_start==null && date_start.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select Start Date!", Toast.LENGTH_SHORT).show();
                return ;
            }
            date_end=schedule_end_date.getText() .toString();
            if (date_end==null && date_end.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select End Date!", Toast.LENGTH_SHORT).show();
                return ;
            }
            time_start=schedule_start_time.getText() .toString();
            if (time_start==null && time_start.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select Start Time!", Toast.LENGTH_SHORT).show();
                return ;
            }
            time_end=schedule_end_time.getText() .toString();
            if (time_end==null && time_end.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select End Time!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String techspinner = null;
            try {
                techspinner=technician_spinner.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(techspinner==null){
                Toast.makeText(getApplicationContext(), "Please Select Technician!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(date_start!=null &&!date_start.isEmpty()&&date_end!=null &&!date_end.isEmpty()&&time_start!=null &&!time_start.isEmpty()&&
                    time_end!=null &&!time_end.isEmpty()&&techspinner!=null){
                if (status.equals("scheduled")){
                    ReScheduleJobsApi();
                }else {
                    Toast.makeText(getApplicationContext(),"Started Jobs cannot be ReScheduled",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();

            }

        });

        add_attachments.setOnClickListener(view -> {

        });

        schedule_start_date.setOnClickListener(view -> {
            Calendar mcurrentDate=Calendar.getInstance();
            final int mYear = mcurrentDate.get(Calendar.YEAR);
            final int mMonth=mcurrentDate.get(Calendar.MONTH);
            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(JobsMonthDetails.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    int month=selectedmonth+1;
                    String monts=String.format("%02d",month);
                    String currentdate=String.valueOf(selectedyear+"-"+monts+"-"+selectedday+" ");
                    schedule_start_date.setText(currentdate);
                    startdate=currentdate;


                }
            },mYear, mMonth, mDay);
            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                mDatePicker.setTitle("Select date");
            mDatePicker.show();
        });

        schedule_end_date.setOnClickListener(view -> {
            Calendar mcurrentDate=Calendar.getInstance();
            final int mYear = mcurrentDate.get(Calendar.YEAR);
            final int mMonth=mcurrentDate.get(Calendar.MONTH);
            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(JobsMonthDetails.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    int month=selectedmonth+1;
                    String monts2=String.format("%02d",month);
                    String currentdate=String.valueOf(selectedyear+"-"+monts2+"-"+selectedday+" ");
                    boolean enddates=CheckDates(schedule_start_date.getText().toString(),currentdate);
                    if (enddates){
                        schedule_end_date.setText(currentdate);
                        enddate=currentdate;
                    }else {
                        Toast.makeText(getApplicationContext(),"End Date should be greater than Start Date",Toast.LENGTH_SHORT).show();
                    }

                }
            },mYear, mMonth, mDay);
            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

//                mDatePicker.setTitle("Select date");
            mDatePicker.show();
        });

    }

    public static boolean CheckDates(String d1,String d2)    {
        SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;
        try {
            if(dfDate.parse(d1).before(dfDate.parse(d2)))
            {
                b = true;//If start date is before end date
            }
            else if(dfDate.parse(d1).equals(dfDate.parse(d2)))
            {
                b = true;//If two dates are equal
            }
            else
            {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    private void starttimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog=new TimePickerDialog(JobsMonthDetails.this, (timePicker, selectedHour, selectedMinute) -> onTimeSet(selectedHour,selectedMinute),hour,minute,false);
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
        schedule_start_time.setText(timeValue);
        starttime=timeValue;
    }

    private void endtimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        endtimePickerDialog=new TimePickerDialog(JobsMonthDetails.this, (timePicker, selectedHour, selectedMinute) -> onEndTimeSet(selectedHour,selectedMinute),hour,minute,false);
        endtimePickerDialog.setTitle("Select Time");
        endtimePickerDialog.show();
    }
    private void onEndTimeSet(int hourOfDay, int minute) {
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
        schedule_end_time.setText(timeValue);
        endtime=timeValue;
    }

    private void GetTechnicianList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"users/getTechnicians/"+domainid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("technicians");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String technicianid=first.getString("id");
                        store.putTechnicianId(String.valueOf(technicianid));
                        String techusername=first.getString("username");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setTechnicianid(technicianid);
                        commonJobs.setTechnicianname(techusername);
                        commonJobsArrayList.add(commonJobs);
                        spinnerlist.add(techusername);
                        technicianArrayList.add(commonJobs);

                    }

                    for (String s:spinnerlist) {

                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(JobsMonthDetails.this,android.R.layout.simple_spinner_item,
                            spinnerlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    technician_spinner.setPrompt("Technician");
                    technician_spinner.setAdapter(adapter);
                    technician_spinner.setAdapter(new SpinnerDetails(adapter,R.layout.technician,JobsMonthDetails.this));
                    technician_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = technician_spinner.getSelectedItem().toString();
                                techniciantext=text;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

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
            pDialog = new ProgressDialog(JobsMonthDetails.this);
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

    private void ReScheduleJobsApi() {

        String tag_json_obj = "json_obj_req";
        Log.d("URL:" ,tag_json_obj);
        String url = EndURL.URL + "userjobs/update/"+userjobid;
        Log.d("waggonurl", url);
        showProgressDialog();
        JSONObject inputLogin = new JSONObject();

        String stime=startdate+starttime;
        String etime=enddate+endtime;
        String technicianid=null;
        for (int i=0;i<technicianArrayList.size();i++){
            String techName=technicianArrayList.get(i).getTechnicianname();
            if (techniciantext!=null){
                if (techniciantext.equals(techName)) {
                    technicianid=technicianArrayList.get(i).getTechnicianid();
                }
            }
        }

        try {
            inputLogin.put("idUser", technicianid);
            inputLogin.put("idJob", jobid);
            inputLogin.put("scheduledBeginDateString",stime);
            inputLogin.put("scheduledEndDateString",etime);
            inputLogin.put("idDomain",domainid);
            inputLogin.put("status","created");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("inputJsonuser", inputLogin.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.d("List Response", response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        String first = obj.getString("result");

                        JobsMonthDetails.this.finish();

                    }else {
                        String first = obj.getString("result");
                        Toast.makeText(getApplicationContext(),""+first,Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

//
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

    private void UpdateValidateApi() {

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "userjobs/updateValidated/"+userid+"/"+userjobid;
        Log.d("waggonurl", url);
        showProgressDialog();
        JSONObject inputLogin = new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.d("List Response", response.toString());

                try {
                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("userjob");
                        String userjobid = first.getString("id");
                    }
                    JobsMonthDetails.this.finish();

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

    private void UnScheduleJobsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"userjobs/unschedule/"+userjobid;
        Log.d("waggonurl", url);
        //showProgressDialog();

        JSONObject inputjso=new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.DELETE, url, inputjso, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                commonJobsArrayList.clear();

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean array=obj.getBoolean("result");
                    String message=obj.getString("message");
                    if (message!=null){
                        Toast.makeText(getApplicationContext()," "+message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                JobsMonthDetails.this.finish();
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
    private void showDeleteDialog(){
        if(builder==null){
            builder = new AlertDialog.Builder(JobsMonthDetails.this);
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        UnScheduleJobsList();
                        Log.d("Dialog Yes ","dialog initialization");
                        dialog.cancel();
                    });

            builder.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());

        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                JobsMonthDetails.this.finish();
                return true;
            case R.id.action_edit:
                Intent i=new Intent(JobsMonthDetails.this,JobsUpdateDelete.class);
                i.putExtra("jobid",jobid);
                i.putExtra("cusname",cusname);
                i.putExtra("sitename",sitename);
                i.putExtra("equipname",equipname);
                i.putExtra("address",address);
                i.putExtra("compaddress",compaddress);
                i.putExtra("firstname",firstname);
                i.putExtra("lastname",lastname);
                i.putExtra("mobile",mobile);
                i.putExtra("phone",phone);
                i.putExtra("email",email);
                i.putExtra("myid",myid);
                i.putExtra("desc",desc);
                i.putExtra("date",date);
                i.putExtra("tags",taginfo.toString());
                startActivity(i);
                break;

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