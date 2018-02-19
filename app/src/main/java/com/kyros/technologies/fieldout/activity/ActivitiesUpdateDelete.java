package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
 * Created by Rohin on 15-12-2017.
 */

public class ActivitiesUpdateDelete extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private PreferenceManager store;
    private String domainid=null;
    private List<String> spinnerlist=new ArrayList<String>();
    private List<String> activitylist=new ArrayList<String>();
    private Spinner users_activity_spinner_update,activity_type_spinner_update;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> activityJobsArrayList = new ArrayList<CommonJobs>();
    private String techid=null;
    private Button save_activity_update;
    private EditText activity_note_update;
    private TimePickerDialog timePickerDialog,endtimePickerDialog;
    private TextView activity_start_time_update,activity_end_time_update,activity_start_date_update,activity_end_date_update;
    private String monthString;
    private String techniciantext=null;
    private String activitytext=null;
    private String startdate=null;
    private String enddate=null;
    private String starttime=null;
    private String endtime=null;
    private String noteactivity=null;
    private String activitiesid=null;
    private String dtstart=null;
    private String dtend=null;
    private String notes=null;
    private AlertDialog.Builder builder;
    private String date_start;
    private String date_end;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activities_update_delete);
        store= PreferenceManager.getInstance(getApplicationContext());
        domainid=store.getIdDomain();
        users_activity_spinner_update=findViewById(R.id.users_activity_spinner_update);
        activity_type_spinner_update=findViewById(R.id.activity_type_spinner_update);
        save_activity_update=findViewById(R.id.save_activity_update);
        activity_start_date_update=findViewById(R.id.activity_start_date_update);
        activity_end_date_update=findViewById(R.id.activity_end_date_update);
        activity_start_time_update=findViewById(R.id.activity_start_time_update);
        activity_end_time_update=findViewById(R.id.activity_end_time_update);
        activity_note_update=findViewById(R.id.activity_note_update);
        users_activity_spinner_update.setOnItemSelectedListener(this);
        GetTechnicianList();
        activity_type_spinner_update.setOnItemSelectedListener(this);
        GetActivityList();


        try {

            Bundle bundle=getIntent().getExtras();
            activitiesid=bundle.getString("activitiesid");
            dtstart=bundle.getString("dtstart");
            dtend=bundle.getString("dtend");
            notes=bundle.getString("noteactivity");

        }catch (Exception e){
            e.printStackTrace();
        }
        if (notes!=null){
            activity_note_update.setText(notes);
        }
        if (dtstart!=null){
            String time1=dtstart.substring(11);
            activity_start_time_update.setText(time1);
            String date1=dtstart.substring(0,dtstart.length()-9);
            activity_start_date_update.setText(date1);
        }
        if (dtend!=null){
            String time2=dtend.substring(11);
            activity_end_time_update.setText(time2);
            String date2=dtend.substring(0,dtend.length()-9);
            activity_end_date_update.setText(date2);

        }


        save_activity_update.setOnClickListener(view -> {
            date_start=activity_start_date_update.getText() .toString();

            if (date_start==null && date_start.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select Start Date!", Toast.LENGTH_SHORT).show();
                return ;
            }
            date_end=activity_end_date_update.getText() .toString();

            if (date_end==null && date_end.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select End Date!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String activityspinner = null;
            try {
                activityspinner=users_activity_spinner_update.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(activityspinner==null){
                Toast.makeText(getApplicationContext(), "Please Select Activity Type!", Toast.LENGTH_SHORT).show();
                return;
            }

            String techspinner = null;
            try {
                techspinner=activity_type_spinner_update.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(techspinner==null){
                Toast.makeText(getApplicationContext(), "Please Select Technician!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(date_start!=null &&!date_start.isEmpty()&&date_end!=null &&!date_end.isEmpty()&& activityspinner!=null&&techspinner!=null){
                noteactivity=activity_note_update.getText().toString();
                String stime=date_start+activity_start_time_update.getText().toString();
                String etime=date_end+activity_end_time_update.getText().toString();
                UpdateActivitiesApi(techniciantext,activitytext,stime,etime,noteactivity);
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();

            }

        });

        activity_start_time_update.setOnClickListener(view -> {
            starttimePicker();
        });

        activity_end_time_update.setOnClickListener(view -> {
            endtimePicker();
        });

        activity_start_date_update.setOnClickListener(view -> {
            Calendar mcurrentDate=Calendar.getInstance();
            final int mYear = mcurrentDate.get(Calendar.YEAR);
            final int mMonth=mcurrentDate.get(Calendar.MONTH);
            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(ActivitiesUpdateDelete.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    int month=selectedmonth+1;
                    String days=String.format("%02d",selectedday);
                    String monts=String.format("%02d",month);
                    String currentdate=String.valueOf(selectedyear+"-"+monts+"-"+days+" ");
                    activity_start_date_update.setText(currentdate);
                    startdate=currentdate;

                }
            },mYear, mMonth, mDay);
            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                mDatePicker.setTitle("Select date");
            mDatePicker.show();
        });

        activity_end_date_update.setOnClickListener(view -> {
            Calendar mcurrentDate=Calendar.getInstance();
            final int mYear = mcurrentDate.get(Calendar.YEAR);
            final int mMonth=mcurrentDate.get(Calendar.MONTH);
            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(ActivitiesUpdateDelete.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    int month=selectedmonth+1;
                    String days=String.format("%02d",selectedday);
                    String monts=String.format("%02d",month);
                    String currentdate=String.valueOf(selectedyear+"-"+monts+"-"+days+" ");
                    boolean enddates=CheckDates(activity_start_date_update.getText().toString(),currentdate);
                    if (enddates){
                        activity_end_date_update.setText(currentdate);
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
        timePickerDialog=new TimePickerDialog(ActivitiesUpdateDelete.this, (timePicker, selectedHour, selectedMinute) -> onTimeSet(selectedHour,selectedMinute),hour,minute,false);
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
        activity_start_time_update.setText(timeValue);
        starttime=timeValue;
    }

    private void endtimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        endtimePickerDialog=new TimePickerDialog(ActivitiesUpdateDelete.this, (timePicker, selectedHour, selectedMinute) -> onEndTimeSet(selectedHour,selectedMinute),hour,minute,false);
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
        activity_end_time_update.setText(timeValue);
        endtime=timeValue;
    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(ActivitiesUpdateDelete.this);
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

    private void UpdateActivitiesApi(String techniciantext, String activitytext, String startdate, String enddate, String noteactivity) {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "activities/update/"+activitiesid;
        Log.d("waggonurl", url);
        showProgressDialog();
        JSONObject inputLogin = new JSONObject();
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
        String activityid=null;
        for (int i=0;i<activityJobsArrayList.size();i++){
            String activityName=activityJobsArrayList.get(i).getActivityname();
            if (activitytext!=null){
                if (activitytext.equals(activityName)) {
                    activityid=activityJobsArrayList.get(i).getActivitytypeid();
                }
            }

        }
        try {
            inputLogin.put("nmActivity", activityid);
            inputLogin.put("noteActivity", noteactivity);
            inputLogin.put("dtStart",startdate);
            inputLogin.put("dtEnd",enddate);
            inputLogin.put("idSender",store.getUserid());
            inputLogin.put("idUser",technicianid);

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
                        JSONObject first = obj.getJSONObject("result");
                        String dtEnd = first.getString("dtEnd");
                        String dtStart = first.getString("dtStart");
                        String activityid = first.getString("id");
                        store.putActivityTypeId(String.valueOf(activityid));
                        String technicianid = first.getString("idUser");
                        String nmActivity = first.getString("nmActivity");
                        String noteActivity = first.getString("noteActivity");
                    }

                    ActivitiesUpdateDelete.this.finish();

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
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20*10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

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
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(ActivitiesUpdateDelete.this,android.R.layout.simple_spinner_item,
                            spinnerlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    users_activity_spinner_update.setPrompt("Technician");
                    users_activity_spinner_update.setAdapter(adapter);
                    users_activity_spinner_update.setAdapter(new SpinnerDetails(adapter,R.layout.technician,ActivitiesUpdateDelete.this));
                    users_activity_spinner_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = users_activity_spinner_update.getSelectedItem().toString();
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

    private void GetActivityList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"activity_types/getAll";
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("activity_types");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String activityid=first.getString("id");
                        store.putActivityTypeId(String.valueOf(activityid));
                        String activityusername=first.getString("name");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setActivitytypeid(activityid);
                        commonJobs.setActivityname(activityusername);
                        activityJobsArrayList.add(commonJobs);
                        activitylist.add(activityusername);

                    }

                    for (String s:activitylist) {


                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(ActivitiesUpdateDelete.this,android.R.layout.simple_spinner_item,
                            activitylist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    activity_type_spinner_update.setPrompt("Activity Type");
                    activity_type_spinner_update.setAdapter(adapter);
                    activity_type_spinner_update.setAdapter(new SpinnerDetails(adapter,R.layout.nothing_selected_nature,ActivitiesUpdateDelete.this));
                    activity_type_spinner_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = activity_type_spinner_update.getSelectedItem().toString();
                                activitytext=text;

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//        int ids=view.getId();
//        Log.e("Ids"," "+ids);
//        int s1=R.id.users_activity_spinner;
//        int s2=R.id.activity_type_spinner;
//        Log.e("SValue"," "+s1+" / "+s2);
//        switch (ids){
//            case R.id.users_activity_spinner:
//
//
//                break;
//            case R.id.activity_type_spinner:
//
//                break;
//
//        }


    }

    private void DeleteActivitiesList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"activities/delete/"+activitiesid;
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
                ActivitiesUpdateDelete.this.finish();
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


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showDeleteDialog(){
        if(builder==null){
            builder = new AlertDialog.Builder(ActivitiesUpdateDelete.this);
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        DeleteActivitiesList();
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
        getMenuInflater().inflate(R.menu.actionbar_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                ActivitiesUpdateDelete.this.finish();
                return true;
            case R.id.action_delete:
                showDeleteDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
