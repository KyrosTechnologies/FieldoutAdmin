package com.kyros.technologies.fieldout.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kyros.technologies.fieldout.R;

/**
 * Created by Rohin on 26-12-2017.
 */

public class SiteDetails extends AppCompatActivity {

    private TextView job_number_details,main_address_details,lat_lng_details,name_details,mobile_details,phone_details;
    private String siteid=null;
    private String sitname=null;
    private String siteaddress=null;
    private String sitecompaddress=null;
    private String sitemyid=null;
    private String sitefirstname=null;
    private String sitelastname=null;
    private String sitemobile=null;
    private String sitephone=null;
    private String sitefax=null;
    private String siteemail=null;
    private String cusid=null;
    private String cusname=null;
    private String latlng=null;
    private String taginfo=null;
    private String customFields=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.customer_details);
        job_number_details = findViewById(R.id.job_number_details);
        main_address_details = findViewById(R.id.main_address_details);
        lat_lng_details = findViewById(R.id.lat_lng_details);
        name_details = findViewById(R.id.name_details);
        mobile_details = findViewById(R.id.mobile_details);
        phone_details = findViewById(R.id.phone_details);

        try {

            Bundle bundle = getIntent().getExtras();
            siteid=bundle.getString("siteid");
            cusid=bundle.getString("customerid");
            sitname=bundle.getString("sitename");
            cusname=bundle.getString("customername");
            siteaddress=bundle.getString("address");
            sitecompaddress=bundle.getString("compaddress");
            sitemyid=bundle.getString("myid");
            sitefirstname=bundle.getString("firstname");
            sitelastname=bundle.getString("lastname");
            sitemobile=bundle.getString("mobile");
            sitephone=bundle.getString("phone");
            sitefax=bundle.getString("fax");
            siteemail=bundle.getString("email");
            latlng=bundle.getString("latlng");
            taginfo=bundle.getString("tags");
            customFields=bundle.getString("customFields");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sitemyid != null) {
            job_number_details.setText(sitemyid);
        }
        if (siteaddress != null) {
            main_address_details.setText(siteaddress);
        }
        if (sitefirstname==null){
            sitefirstname="";
        }
        if (sitelastname==null){
            sitelastname="";
        }
        if (siteemail==null){
            siteemail="";
        }
        name_details.setText(sitefirstname+" "+sitelastname+" ("+siteemail+")");

        if (sitemobile != null) {
            mobile_details.setText(sitemobile);
        }
        if (sitephone != null) {
            phone_details.setText(sitephone);
        }
        if (latlng!=null){
            lat_lng_details.setText(latlng);
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
                SiteDetails.this.finish();
                return true;
            case R.id.action_edit:
                Intent i=new Intent(SiteDetails.this,SitesUpdateDelete.class);
                i.putExtra("siteid",siteid);
                i.putExtra("customerid",cusid);
                i.putExtra("customername",cusname);
                i.putExtra("sitename",sitname);
                i.putExtra("address",siteaddress);
                i.putExtra("compaddress",sitecompaddress);
                i.putExtra("myid",sitemyid);
                i.putExtra("firstname",sitefirstname);
                i.putExtra("lastname",sitelastname);
                i.putExtra("phone",sitephone);
                i.putExtra("mobile",sitemobile);
                i.putExtra("fax",sitefax);
                i.putExtra("email",siteemail);
                i.putExtra("tags",taginfo.toString());
                i.putExtra("customFields",customFields);
                startActivity(i);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}