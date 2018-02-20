package com.kyros.technologies.fieldout.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.common.EndURL;
import com.kyros.technologies.fieldout.common.FilePath;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.ActivityAddAttachmentBinding;
import com.kyros.technologies.fieldout.models.AddAttachments;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.AddAttachmentsViewModel;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rohin on 27-12-2017.
 */

public class ScheduleMonthDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView status_jobs_details,priority_jobs_details,job_details_my_id,job_details_job_type,job_details_address,
            job_details_lat_lng,job_details_name,job_details_mobile,job_details_phone,job_details_email,job_details_desc,
            schedule_start_date,schedule_start_time,schedule_end_date,schedule_end_time,path_text;
    private Button validate_jobs_details,unschedule_jobs_details,reschedule_jobs_details,save_jobs_details;
    private ImageView cancel_attachment;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> technicianArrayList = new ArrayList<CommonJobs>();
    private String domainid=null;
    private Spinner technician_spinner;
    private String userid=null;
    private String priority=null;
    private LinearLayout add_attachments,add_attachments_linear;
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
    private ActivityAddAttachmentBinding binding;
    private PreferenceManager store;
    private CompositeSubscription subscription;
    private String TAG=AddAttachmentActivity.class.getSimpleName();
    private int FILE_PICKER=3;
    private RxPermissions rxPermissions;
    private final int READ_EXTERNAL_STORAGE=4;
    @Inject
    AddAttachmentsViewModel viewModel;
    private byte[] fileByte;
    private String fileName=null;

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
        save_jobs_details=findViewById(R.id.save_jobs_details);
        path_text=findViewById(R.id.path_text);
        cancel_attachment=findViewById(R.id.cancel_attachment);
        add_attachments_linear=findViewById(R.id.add_attachments_linear);
        userjobid=store.getUserid();
        domainid = store.getIdDomain();
        technician_spinner.setOnItemSelectedListener(this);
        GetTechnicianList();
        ((ServiceHandler)getApplication()).getApplicationComponent().injectSchedulemonthDetails(this);
        subscription=new CompositeSubscription();
        rxPermissions = new RxPermissions(this);
        add_attachments.setOnClickListener(view -> checkReadPermission()
        );

        try {

            Bundle bundle = getIntent().getExtras();
            jobid=bundle.getString("jobid");
            userjobid=bundle.getString("userjobid");
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

        cancel_attachment.setOnClickListener(view -> {
            add_attachments_linear.setVisibility(View.GONE);
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

        save_jobs_details.setOnClickListener(view -> {
            initiateAddAttachmentAPIByteStream(fileName,fileByte);
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


        schedule_start_date.setOnClickListener(view -> {
            Calendar mcurrentDate=Calendar.getInstance();
            final int mYear = mcurrentDate.get(Calendar.YEAR);
            final int mMonth=mcurrentDate.get(Calendar.MONTH);
            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(ScheduleMonthDetails.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    int month=selectedmonth+1;
                    String monts=String.format("%02d",month);
                    String currentdate=String.valueOf(selectedyear+"-"+monts+"-"+selectedday+" ");
                    schedule_start_date.setText(currentdate);
                    startdate=currentdate;

                }
            },mYear, mMonth, mDay);
            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            mDatePicker.show();
        });

        schedule_end_date.setOnClickListener(view -> {
            Calendar mcurrentDate=Calendar.getInstance();
            final int mYear = mcurrentDate.get(Calendar.YEAR);
            final int mMonth=mcurrentDate.get(Calendar.MONTH);
            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(ScheduleMonthDetails.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
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
            mDatePicker.show();
        });

    }

    public static boolean CheckDates(String d1,String d2)    {
        SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;
        try {
            if(dfDate.parse(d1).before(dfDate.parse(d2)))
            {
                b = true;
            }
            else if(dfDate.parse(d1).equals(dfDate.parse(d2)))
            {
                b = true;
            }
            else
            {
                b = false;
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
        timePickerDialog=new TimePickerDialog(ScheduleMonthDetails.this, (timePicker, selectedHour, selectedMinute) -> onTimeSet(selectedHour,selectedMinute),hour,minute,false);
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
        endtimePickerDialog=new TimePickerDialog(ScheduleMonthDetails.this, (timePicker, selectedHour, selectedMinute) -> onEndTimeSet(selectedHour,selectedMinute),hour,minute,false);
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
        String url = EndURL.URL+"users/getTechnicians";
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
                        String techfirstName=first.getString("firstName");
                        String techlastName=first.getString("lastName");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setTechnicianid(technicianid);
                        commonJobs.setFirstname(techfirstName);
                        commonJobs.setLastname(techlastName);
                        commonJobsArrayList.add(commonJobs);
                        spinnerlist.add(techfirstName+" "+techlastName);

                    }

                    for (String s:spinnerlist) {

                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(ScheduleMonthDetails.this,android.R.layout.simple_spinner_item,
                            spinnerlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    technician_spinner.setPrompt("Technician");
                    technician_spinner.setAdapter(adapter);
                    technician_spinner.setAdapter(new SpinnerDetails(adapter,R.layout.technician,ScheduleMonthDetails.this));
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
                params.put("idDomain",store.getIdDomain());
                return params;
            }


        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20*10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(ScheduleMonthDetails.this);
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
        for (int i=0;i<commonJobsArrayList.size();i++){
            String techName=commonJobsArrayList.get(i).getFirstname();
            String techlastname=commonJobsArrayList.get(i).getLastname();
            if (techniciantext!=null){
                if (techniciantext.equals(techName+" "+techlastname)) {
                    technicianid=commonJobsArrayList.get(i).getTechnicianid();
                }
            }
        }

        try {
            inputLogin.put("idUser", technicianid);
            inputLogin.put("idJob", jobid);
            inputLogin.put("scheduledBeginDateString",stime);
            inputLogin.put("scheduledEndDateString",etime);
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

                        ScheduleMonthDetails.this.finish();

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
                params.put("idDomain",store.getIdDomain());
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
                    ScheduleMonthDetails.this.finish();

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
                params.put("idDomain",store.getIdDomain());
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void UnScheduleJobsApi() {
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
                ScheduleMonthDetails.this.finish();
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
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }
    private void showDeleteDialog(){
        if(builder==null){
            builder = new AlertDialog.Builder(ScheduleMonthDetails.this);
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        UnScheduleJobsApi();
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
                ScheduleMonthDetails.this.finish();
                return true;
            case R.id.action_edit:
                Intent i=new Intent(ScheduleMonthDetails.this,JobsUpdateDelete.class);
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

    private void checkReadPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showToast("Please allow permission for attachments!");

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE);

            }
        }else{
            getAttachment();
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAttachment();

                } else {

                    showToast("Please enable permission for attachment");
                }
            }

        }
    }
    private void getAttachment() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload"),FILE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FILE_PICKER) {
                if (data != null) {
                    Uri selectedFileUri = data.getData();
                    String selectedFilePath = FilePath.getPath(this, selectedFileUri);
                    Log.i(TAG, "Selected File Path:" + selectedFilePath);

                    if (selectedFilePath != null && !selectedFilePath.equals("")) {
                        Log.d("File Path : ", TAG + " / / " + selectedFilePath);
                        File file = new File(selectedFilePath);
                        fileByte = getBytes(file);
                        fileName = file.getName();
                        if (fileName != null) {
                            add_attachments_linear.setVisibility(View.VISIBLE);
                            path_text.setText(fileName);

                            try {
                                Log.d("fileByte : ", TAG + " / / " + fileByte.toString());
                                Log.d("fileName : ", TAG + " / / " + fileName);
                                Log.d("fileSize : ", TAG + " / / " + file.length());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            showToast("File path is empty!");
                        }
                    }
                }
            }
        }

    }

    private void initiateAddAttachmentAPIByteStream(String fileName, byte[] fileByte) {
        showProgressDialog();
        AddAttachments addAttachments=new AddAttachments();
        addAttachments.setFileData( encodeBase64(fileByte));
        addAttachments.setFileName(fileName);
        addAttachments.setFileType(fileName);
        StringTokenizer stringTokenizer=new StringTokenizer(fileName,".");
        String ext=stringTokenizer.nextToken();
        addAttachments.setFileExtension(ext);
        Log.d("InputAddAttach : ",""+new Gson().toJson(addAttachments));
        subscription.add(viewModel.addResponseBodyObservableByteStream(store.getToken(),addAttachments,store.getIdDomain())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                .subscribe(this::addByteResponse,this::addByteError,this::addByteCompleted));
    }

    private void addByteCompleted() {
        dismissProgressDialog();
    }

    private void addByteError(Throwable throwable) {
        dismissProgressDialog();
        Log.d("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());
    }

    private void addByteResponse(ResponseBody responseBody) {
        dismissProgressDialog();
        if(responseBody != null){
            try {
                Log.d("Response Byte : ",TAG+" / / "+responseBody.string());
                ScheduleMonthDetails.this.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            showToast("response is null !");
        }
    }

    private void addCompleted() {
        dismissProgressDialog();
    }

    private void addError(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());
        dismissProgressDialog();
    }

    private void addResponse(ResponseBody responseBody) {
        dismissProgressDialog();
        if(responseBody != null){
            try {
                String response=responseBody.string();
                Log.d("Response : ",TAG+" / / "+response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            showToast("add response is null!");
        }
    }

    private byte[] getBytes (File file){
        FileInputStream input = null;
        if (file.exists())
            try{
                input = new FileInputStream (file);
                int len = (int) file.length();
                byte[] data = new byte[len];
                int count, total = 0;
                while ((count = input.read (data, total, len - total)) > 0) total += count;
                return data;
            }catch (Exception e){
                e.printStackTrace();
            }
            finally{
                if (input != null)
                    try{
                        input.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
            }
        return null;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissProgressDialog();
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    @NonNull
    private MultipartBody.Part attachmentFilePart(String partName,Uri fileUri,String fileName,File file){
        String mimeType = null;
        if (fileUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(fileUri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        RequestBody requestFile=RequestBody.create(MediaType.parse(mimeType),file);
        return MultipartBody.Part.createFormData(partName,fileName,requestFile);
    }
    @NonNull
    private RequestBody createPartFromString (String value){
        return RequestBody.create(MultipartBody.FORM,value);
    }

    private String encodeBase64(byte[] bytes){
        byte[] encodeValue = Base64.encode(bytes, Base64.DEFAULT);
        return new String(encodeValue);
    }
    private String decodeBase64(byte[] bytes){
        byte[] decodeValue = Base64.decode(bytes, Base64.DEFAULT);
        return new String(decodeValue);

    }

}