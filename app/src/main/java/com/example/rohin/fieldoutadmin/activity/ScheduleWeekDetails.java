package com.example.rohin.fieldoutadmin.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.common.EndURL;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Rohin on 27-12-2017.
 */

public class ScheduleWeekDetails extends AppCompatActivity {

    private TextView status_jobs_details,priority_jobs_details,job_details_my_id,job_details_job_type,job_details_address,
            job_details_lat_lng,job_details_name,job_details_mobile,job_details_phone,job_details_email,job_details_desc,
            path_text;
    private ImageView cancel_attachment;
    private Button save_jobs_details;
    private String priority=null;
    private LinearLayout add_attachments,add_attachments_linear;
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
    private byte[]addfile;
    private String PathHolder=null;
    private static final int READ_REQUEST_CODE = 42;

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
            jobid=bundle.getString("jobid");

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

        add_attachments.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, 7);
        });

        cancel_attachment.setOnClickListener(view -> {
            addfile=null;
            add_attachments_linear.setVisibility(View.GONE);
        });

        save_jobs_details.setOnClickListener(view -> {
            AddAttachmentsApi();
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case 7:
                if(resultCode==RESULT_OK){
                    PathHolder = data.getData().getPath();
                    if (PathHolder!=null){
                        add_attachments_linear.setVisibility(View.VISIBLE);
                        path_text.setText(PathHolder);
                    }
                    File file=new File(PathHolder);
                    try {
                       addfile=read(file);
                       Log.d("File"," "+addfile.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public byte[] read(File file) throws IOException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        }finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode,
//                                 Intent resultData) {
//
//        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
//        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
//        // response to some other intent, and the code below shouldn't run at all.
//
//        if (requestCode == READ_REQUEST_CODE && resultCode == ScheduleWeekDetails.RESULT_OK) {
//            // The document selected by the user won't be returned in the intent.
//            // Instead, a URI to that document will be contained in the return intent
//            // provided to this method as a parameter.
//            // Pull that URI using resultData.getData().
//            Uri uri = null;
//            if (resultData != null) {
//                uri = resultData.getData();
//                Log.i("File", "Uri: " + uri.toString());
//                try {
//                    readTextFromUri(uri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private void readTextFromUri(Uri uri) throws IOException {
//        InputStream inputStream = getContentResolver().openInputStream(uri);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(
//                inputStream));
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//        int nRead;
//        byte[] data = new byte[16384];
//
//        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
//            buffer.write(data, 0, nRead);
//        }
//
//        buffer.flush();
//
//        byte[]file=buffer.toByteArray();
//        Log.e("File"," "+file.toString());
////        StringBuilder stringBuilder = new StringBuilder();
////        String line;
////        while ((line = reader.readLine()) != null) {
////            stringBuilder.append(line);
////        }
////        return stringBuilder.toString();
//    }

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
//            case R.id.action_edit:
//                Intent i=new Intent(ToScheduleDetails.this,CustomerUpdateDelete.class);
//                i.putExtra("customerid",customerid);
//                i.putExtra("customername",customername);
//                i.putExtra("address",cusaddress);
//                i.putExtra("compaddress",compaddress);
//                i.putExtra("myid",cusmyid);
//                i.putExtra("firstname",cusfirstname);
//                i.putExtra("lastname",cuslastname);
//                i.putExtra("mobile",cusmobile);
//                i.putExtra("phone",cusphone);
//                i.putExtra("fax",cusfax);
//                i.putExtra("email",cusemail);
//                startActivity(i);
//                break;

        }

        return super.onOptionsItemSelected(item);
    }
}