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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rohin on 15-12-2017.
 */

public class ResourcesUpdateDelete extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private PreferenceManager store;
    private String domainid=null;
    private List<String> spinnerlist=new ArrayList<String>();
    private List<String> activitylist=new ArrayList<String>();
    private Spinner resource_activity_spinner_update,resource_type_spinner_update;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> activityJobsArrayList = new ArrayList<CommonJobs>();
    private String techid=null;
    private Button save_resources_update;
    private EditText resource_note_edit_update;
    private TimePickerDialog timePickerDialog,endtimePickerDialog;
    private TextView resource_start_time_update,resource_end_time_update,resource_start_date_update,resource_end_date_update;
    private String monthString;
    private String techniciantext=null;
    private String activitytext=null;
    private String startdate=null;
    private String enddate=null;
    private String starttime=null;
    private String endtime=null;
    private String noteactivity=null;
    private String resourcesid=null;
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
        setContentView(R.layout.resources_update_delete);
        store= PreferenceManager.getInstance(getApplicationContext());
        domainid=store.getIdDomain();
        resource_activity_spinner_update=findViewById(R.id.resource_activity_spinner_update);
        resource_type_spinner_update=findViewById(R.id.resource_type_spinner_update);
        save_resources_update=findViewById(R.id.save_resources_update);
        resource_start_date_update=findViewById(R.id.resource_start_date_update);
        resource_end_date_update=findViewById(R.id.resource_end_date_update);
        resource_start_time_update=findViewById(R.id.resource_start_time_update);
        resource_end_time_update=findViewById(R.id.resource_end_time_update);
        resource_note_edit_update=findViewById(R.id.resource_note_edit_update);
        resource_activity_spinner_update.setOnItemSelectedListener(this);
        GetTechnicianList();
        resource_type_spinner_update.setOnItemSelectedListener(this);
        GetResourcesList();

        try {

            Bundle bundle=getIntent().getExtras();
            resourcesid=bundle.getString("resourcesid");
            dtstart=bundle.getString("dtstart");
            dtend=bundle.getString("dtend");
            notes=bundle.getString("noteactivity");

        }catch (Exception e){
            e.printStackTrace();
        }
        if (notes!=null){
            resource_note_edit_update.setText(notes);
        }
        if (dtstart!=null){
            String time1=dtstart.substring(11);
            resource_start_time_update.setText(time1);
            String date1=dtstart.substring(0,dtstart.length()-9);
            resource_start_date_update.setText(date1);
        }
        if (dtend!=null){
            String time2=dtend.substring(11);
            resource_end_time_update.setText(time2);
            String date2=dtend.substring(0,dtend.length()-9);
            resource_end_date_update.setText(date2);

        }


        save_resources_update.setOnClickListener(view -> {
            date_start=resource_start_date_update.getText() .toString();
            if (date_start==null && date_start.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select Start Date!", Toast.LENGTH_SHORT).show();
                return ;
            }
            date_end=resource_end_date_update.getText() .toString();
            if (date_end==null && date_end.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Select End Date!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String activityspinner = null;
            try {
                activityspinner=resource_activity_spinner_update.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(activityspinner==null){
                Toast.makeText(getApplicationContext(), "Please Select Activity Type!", Toast.LENGTH_SHORT).show();
                return;
            }

            String techspinner = null;
            try {
                techspinner=resource_type_spinner_update.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(techspinner==null){
                Toast.makeText(getApplicationContext(), "Please Select Technician!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(date_start!=null &&!date_start.isEmpty()&&date_end!=null &&!date_end.isEmpty()&& activityspinner!=null&&techspinner!=null){
                noteactivity=resource_note_edit_update.getText().toString();
                UpdateResourcesApi(techniciantext,activitytext,startdate,enddate,noteactivity);
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();

            }

        });

        resource_start_time_update.setOnClickListener(view -> {
            starttimePicker();
        });

        resource_end_time_update.setOnClickListener(view -> {
            endtimePicker();
        });

        resource_start_date_update.setOnClickListener(view -> {
            Calendar mcurrentDate=Calendar.getInstance();
            final int mYear = mcurrentDate.get(Calendar.YEAR);
            final int mMonth=mcurrentDate.get(Calendar.MONTH);
            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(ResourcesUpdateDelete.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    int month=selectedmonth+1;

                    String currentdate=String.valueOf(selectedyear+"-"+month+"-"+selectedday+" ");
                    resource_start_date_update.setText(currentdate);
                    startdate=currentdate;


                }
            },mYear, mMonth, mDay);
//                mDatePicker.setTitle("Select date");
            mDatePicker.show();
        });

        resource_end_date_update.setOnClickListener(view -> {
            Calendar mcurrentDate=Calendar.getInstance();
            final int mYear = mcurrentDate.get(Calendar.YEAR);
            final int mMonth=mcurrentDate.get(Calendar.MONTH);
            final int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(ResourcesUpdateDelete.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    int month=selectedmonth+1;


                    String currentdate=String.valueOf(selectedyear+"-"+month+"-"+selectedday+" ");
                    resource_end_date_update.setText(currentdate);
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
        timePickerDialog=new TimePickerDialog(ResourcesUpdateDelete.this, (timePicker, selectedHour, selectedMinute) -> onTimeSet(selectedHour,selectedMinute),hour,minute,false);
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
        resource_start_time_update.setText(timeValue);
        starttime=timeValue;
    }

    private void endtimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        endtimePickerDialog=new TimePickerDialog(ResourcesUpdateDelete.this, (timePicker, selectedHour, selectedMinute) -> onEndTimeSet(selectedHour,selectedMinute),hour,minute,false);
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
        resource_end_time_update.setText(timeValue);
        endtime=timeValue;
    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(ResourcesUpdateDelete.this);
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

    private void UpdateResourcesApi(String techniciantext, String activitytext, String startdate, String enddate, String noteactivity) {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "resources/update/"+resourcesid;
        Log.d("waggonurl", url);
        showProgressDialog();
        JSONObject inputLogin = new JSONObject();
        String stime=startdate+starttime;
        String etime=enddate+endtime;
        String idToolsAndResurces=null;
        for (int i=0;i<commonJobsArrayList.size();i++){
            String techName=commonJobsArrayList.get(i).getTechnicianname();
            if (techniciantext!=null){
                if (techniciantext.equals(techName)) {
                    idToolsAndResurces=commonJobsArrayList.get(i).getIdtoolsresources();
                }
            }
        }
        String activityid=null;
        for (int i=0;i<activityJobsArrayList.size();i++){
            String activityName=activityJobsArrayList.get(i).getResourcename();
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
            inputLogin.put("idUser",idToolsAndResurces);

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

                    ResourcesUpdateDelete.this.finish();

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
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(ResourcesUpdateDelete.this,android.R.layout.simple_spinner_item,
                            spinnerlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    resource_activity_spinner_update.setPrompt("Technician");
                    resource_activity_spinner_update.setAdapter(adapter);
                    resource_activity_spinner_update.setAdapter(new ResourcesSelectedSpinner(adapter,R.layout.nothing_selected_list,ResourcesUpdateDelete.this));
                    resource_activity_spinner_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = resource_activity_spinner_update.getSelectedItem().toString();
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

    private void GetResourcesList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"tools_and_resources/getByDomainId/"+domainid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("resources");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String toolsresourceid=first.getString("id");
                        store.putIdToolsResources(String.valueOf(toolsresourceid));
                        String resourceusername=first.getString("resource_name");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setIdtoolsresources(toolsresourceid);
                        commonJobs.setResourcename(resourceusername);
                        activityJobsArrayList.add(commonJobs);
                        activitylist.add(resourceusername);



                    }

                    for (String s:activitylist) {


                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(ResourcesUpdateDelete.this,android.R.layout.simple_spinner_item,
                            activitylist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    resource_type_spinner_update.setPrompt("Tool / Resource");
                    resource_type_spinner_update.setAdapter(adapter);
                    resource_type_spinner_update.setAdapter(new ResourcesSelectedSpinner(adapter,R.layout.tools_resources,ResourcesUpdateDelete.this));
                    resource_type_spinner_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = resource_type_spinner_update.getSelectedItem().toString();
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

    }

    private void DeleteResourcesList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"resources/delete/"+resourcesid;
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
                ResourcesUpdateDelete.this.finish();
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


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showDeleteDialog(){
        if(builder==null){
            builder = new AlertDialog.Builder(ResourcesUpdateDelete.this);
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        Log.e("Delete Id"," "+resourcesid);
                        DeleteResourcesList();
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
                ResourcesUpdateDelete.this.finish();
                return true;
            case R.id.action_delete:
                showDeleteDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
