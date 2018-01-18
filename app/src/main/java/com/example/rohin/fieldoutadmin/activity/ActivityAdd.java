package com.example.rohin.fieldoutadmin.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.common.EndURL;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rohin on 14-12-2017.
 */

public class ActivityAdd extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private PreferenceManager store;
    private List<String> spinnerlist=new ArrayList<String>();
    private List<String> activitylist=new ArrayList<String>();
    private Spinner users_activity_spinner,activity_type_spinner;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> activityJobsArrayList = new ArrayList<CommonJobs>();
    private String techid=null;
    private String domainid=null;
    private Button save_activity;
    private EditText activity_note_edit;
    private TimePickerDialog timePickerDialog,endtimePickerDialog;
    private TextView activity_start_time,activity_end_time,activity_start_date,activity_end_date;
    private String monthString;
    private String techniciantext=null;
    private String activitytext=null;
    private String startdate=null;
    private String enddate=null;
    private String starttime=null;
    private String endtime=null;
    private String noteactivity=null;
    private String date_start;
    private String date_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_activities);
        store= PreferenceManager.getInstance(getApplicationContext());
        domainid=store.getIdDomain();
        users_activity_spinner=findViewById(R.id.users_activity_spinner);
        activity_type_spinner=findViewById(R.id.activity_type_spinner);
        save_activity=findViewById(R.id.save_activity);
        activity_start_time=findViewById(R.id.activity_start_time);
        activity_end_date=findViewById(R.id.activity_end_date);
        activity_start_date=findViewById(R.id.activity_start_date);
        activity_end_time=findViewById(R.id.activity_end_time);
        activity_note_edit=findViewById(R.id.activity_note_edit);
        users_activity_spinner.setOnItemSelectedListener(this);
        GetTechnicianList();
        activity_type_spinner.setOnItemSelectedListener(this);
        GetActivityTypeList();

        save_activity.setOnClickListener(view -> {
            date_start=activity_start_date.getText() .toString();
            if (date_start==null && date_start.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select Start Date!", Toast.LENGTH_SHORT).show();
                return ;
            }
            date_end=activity_end_date.getText() .toString();
            if (date_end==null && date_end.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select End Date!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String activityspinner = null;
            try {
                activityspinner=users_activity_spinner.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(activityspinner==null){
                Toast.makeText(getApplicationContext(), "Please Select Activity Type!", Toast.LENGTH_SHORT).show();
                return;
            }

            String techspinner = null;
            try {
                techspinner=activity_type_spinner.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(techspinner==null){
                Toast.makeText(getApplicationContext(), "Please Select Technician!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(date_start!=null &&!date_start.isEmpty()&&date_end!=null &&!date_end.isEmpty()&& activityspinner!=null&&techspinner!=null){
                noteactivity=activity_note_edit.getText().toString();
                AddActivitiesApi(techniciantext,activitytext,startdate,enddate,noteactivity);
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();

            }

        });

        activity_start_time.setOnClickListener(view -> {
            starttimePicker();
        });

        activity_end_time.setOnClickListener(view -> {
            endtimePicker();
        });

        activity_start_date.setOnClickListener(view -> {
            Calendar mcurrentDate=Calendar.getInstance();
            final int mYear = mcurrentDate.get(Calendar.YEAR);
            final int mMonth=mcurrentDate.get(Calendar.MONTH);
            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(ActivityAdd.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    int month=selectedmonth+1;

                    String currentdate=String.valueOf(selectedyear+"-"+month+"-"+selectedday+" ");
                    activity_start_date.setText(currentdate);
                    startdate=currentdate;


                }
            },mYear, mMonth, mDay);
//                mDatePicker.setTitle("Select date");
            mDatePicker.show();
        });

        activity_end_date.setOnClickListener(view -> {
            Calendar mcurrentDate=Calendar.getInstance();
            final int mYear = mcurrentDate.get(Calendar.YEAR);
            final int mMonth=mcurrentDate.get(Calendar.MONTH);
            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(ActivityAdd.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    int month=selectedmonth+1;


                    String currentdate=String.valueOf(selectedyear+"-"+month+"-"+selectedday+" ");
                    activity_end_date.setText(currentdate);
                    enddate=currentdate;


                }
            },mYear, mMonth, mDay);
//                mDatePicker.setTitle("Select date");
            mDatePicker.show();
        });

    }

    private void starttimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog=new TimePickerDialog(ActivityAdd.this, (timePicker, selectedHour, selectedMinute) -> onTimeSet(selectedHour,selectedMinute),hour,minute,false);
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
        activity_start_time.setText(timeValue);
        starttime=timeValue;
    }

    private void endtimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        endtimePickerDialog=new TimePickerDialog(ActivityAdd.this, (timePicker, selectedHour, selectedMinute) -> onEndTimeSet(selectedHour,selectedMinute),hour,minute,false);
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
        activity_end_time.setText(timeValue);
        endtime=timeValue;
    }

    private void AddActivitiesApi(String techniciantext, String activitytext, String startdate, String enddate, String noteactivity) {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "activities/add";
        Log.d("waggonurl", url);
        JSONObject inputLogin = new JSONObject();
        String stime=startdate+starttime;
        String etime=enddate+endtime;
        String technicianid=null;
        for (int i=0;i<commonJobsArrayList.size();i++){
            String techName=commonJobsArrayList.get(i).getTechnicianname();
            if (techniciantext!=null){
                if (techniciantext.equals(techName)) {
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
            inputLogin.put("dtStart",stime);
            inputLogin.put("dtEnd",etime);
            inputLogin.put("idUser",technicianid);
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
                    JSONObject first=obj.getJSONObject("activity");
                    String dtEnd=first.getString("dtEnd");
                    String dtStart=first.getString("dtStart");
                    String activityid=first.getString("id");
                    store.putActivityTypeId(String.valueOf(activityid));
                    String technicianid=first.getString("idUser");
                    String nmActivity=first.getString("nmActivity");
                    String noteActivity=first.getString("noteActivity");

                    ActivityAdd.this.finish();

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

                    }

                    for (String s:spinnerlist) {

                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(ActivityAdd.this,android.R.layout.simple_spinner_item,
                            spinnerlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    users_activity_spinner.setPrompt("Technician");
                    users_activity_spinner.setAdapter(adapter);
                    users_activity_spinner.setAdapter(new SpinnerDetails(adapter,R.layout.technician,ActivityAdd.this));
                    users_activity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = users_activity_spinner.getSelectedItem().toString();
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

    private void GetActivityTypeList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"activity_types/getByDomainId/"+domainid;
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
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(ActivityAdd.this,android.R.layout.simple_spinner_item,
                            activitylist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    activity_type_spinner.setPrompt("Activity Type");
                    activity_type_spinner.setAdapter(adapter);
                    activity_type_spinner.setAdapter(new SpinnerDetails(adapter,R.layout.nothing_selected_nature,ActivityAdd.this));
                    activity_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = activity_type_spinner.getSelectedItem().toString();
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
                return params;
            }


        };
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                ActivityAdd.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
