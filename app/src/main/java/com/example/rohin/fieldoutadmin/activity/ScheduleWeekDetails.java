package com.example.rohin.fieldoutadmin.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.common.EndURL;
import com.example.rohin.fieldoutadmin.common.FilePath;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Rohin on 27-12-2017.
 */

public class ScheduleWeekDetails extends AppCompatActivity {

    private TextView status_jobs_details,priority_jobs_details,job_details_my_id,job_details_job_type,job_details_address,
            job_details_lat_lng,job_details_name,job_details_mobile,job_details_phone,job_details_email,job_details_desc,
            path_text;
    private String priority=null;
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
    private String date=null;
    private String taginfo=null;
    private byte[]addfile;
    private Button save_jobs_details;
    private LinearLayout add_attachments,add_attachments_linear;
    private ImageView cancel_attachment;
    private String PathHolder=null;
    private String TAG=AddAttachmentActivity.class.getSimpleName();
    private int FILE_PICKER=3;
    private final int READ_EXTERNAL_STORAGE=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.jobs_details);
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
        add_attachments_linear=findViewById(R.id.add_attachments_linear);
        path_text=findViewById(R.id.path_text);
        cancel_attachment=findViewById(R.id.cancel_attachment);
        save_jobs_details=findViewById(R.id.save_jobs_details);

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

        cancel_attachment.setOnClickListener(view -> {
            addfile=null;
            add_attachments_linear.setVisibility(View.GONE);
        });

        save_jobs_details.setOnClickListener(view -> {
            AddAttachmentsApi();
        });
        add_attachments.setOnClickListener(view -> {
            checkReadPermission();
        });

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

    private void AddAttachmentsApi() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"attachments/add";

        Log.d("waggonurl", url);
        JSONObject inputLogin=new JSONObject();
        try{
            inputLogin.put("fileName",PathHolder);
            inputLogin.put("fileData",addfile);
            inputLogin.put("job",jobid);

        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("Add File"," "+addfile.toString());
        Log.d("inputJsonuser",inputLogin.toString());
//        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, inputLogin, new Response.Listener<JSONObject>() {
//            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//            @Override
//            public void onResponse(JSONObject response) {
//
//                Log.d("List TeamsResponse",response.toString());
//                try {
//
//                    JSONObject obj=new JSONObject(response.toString());
//                    boolean success=obj.getBoolean("isSuccess");
//
//                        JSONObject first = obj.getJSONObject("userDetails");
//
//                    }
//                    catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (error!=null){
//                    Log.e("Error",""+error.toString());
//                }
//
////                texts.setText(error.toString());
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("Content-Type", "Application/json");
//                return params;
//            }
//
//        };
//        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

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
                ScheduleWeekDetails.this.finish();
                return true;
               case R.id.action_edit:
                    Intent i=new Intent(ScheduleWeekDetails.this,JobsUpdateDelete.class);
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