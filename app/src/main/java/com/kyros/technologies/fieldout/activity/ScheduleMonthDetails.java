package com.kyros.technologies.fieldout.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.kyros.technologies.fieldout.common.FilePath;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rohin on 27-12-2017.
 */

public class ScheduleMonthDetails extends AppCompatActivity {

    private TextView status_jobs_details,priority_jobs_details,job_details_my_id,job_details_job_type,job_details_address,
            job_details_lat_lng,job_details_name,job_details_mobile,job_details_phone,job_details_email,job_details_desc;
    private Button validate_jobs_details,unschedule_jobs_details;
    private String userid=null;
    private String priority=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    private PreferenceManager store;
    private LinearLayout add_attachments;
    private String jobtype=null;
    private String address=null;
    private String myid=null;
    private String contactname=null;
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
    private String userjobid=null;
    private String date=null;
    private String taginfo=null;
    private String TAG=AddAttachmentActivity.class.getSimpleName();
    private int FILE_PICKER=3;
    private final int READ_EXTERNAL_STORAGE=4;
    private AlertDialog.Builder builder;
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
        userid=store.getUserid();

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
            contactname = bundle.getString("contactname");
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
        if (contactname!=null){
            job_details_name.setText(contactname);
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

        validate_jobs_details.setOnClickListener(view -> {
            if (status.equals("completed")){
                UpdateValidateApi();
            }else {
                Toast.makeText(getApplicationContext(),"Job Need to be Completed to Validate",Toast.LENGTH_SHORT).show();
            }
        });

        unschedule_jobs_details.setOnClickListener(view -> {
            showDeleteDialog();
        });

        add_attachments.setOnClickListener(view -> {
           checkReadPermission();
        });

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
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

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
                return;
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
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == FILE_PICKER){
                if(data != null){
                    Uri selectedFileUri = data.getData();
                    String selectedFilePath = FilePath.getPath(this,selectedFileUri);
                    Log.i(TAG,"Selected File Path:" + selectedFilePath);

                    if(selectedFilePath != null && !selectedFilePath.equals("")){
                        Log.d("File Path : ",TAG+" / / "+selectedFilePath);
                        byte[] fileByte=getBytes(new File(selectedFilePath));
                        try {
                            Log.d("fileByte : ",TAG+" / / "+fileByte.toString());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        showToast("File path is empty!");
                    }
                }
            }
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

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void DeleteActivitiesList() {
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
}