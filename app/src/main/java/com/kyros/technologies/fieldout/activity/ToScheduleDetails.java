package com.kyros.technologies.fieldout.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kyros.technologies.fieldout.R;

/**
 * Created by Rohin on 27-12-2017.
 */

public class ToScheduleDetails extends AppCompatActivity {

    private TextView priority_jobs_details,job_details_my_id,job_details_job_type,job_details_address,job_details_lat_lng,
            job_details_name,job_details_mobile,job_details_phone,job_details_email,job_details_desc;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.jobs_details);
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

        try {

            Bundle bundle = getIntent().getExtras();
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

        } catch (Exception e) {
            e.printStackTrace();
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
                ToScheduleDetails.this.finish();
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