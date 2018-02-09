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

public class CustomerDetails extends AppCompatActivity {

    private TextView job_number_details,main_address_details,lat_lng_details,name_details,mobile_details,phone_details;
    private String customerid=null;
    private String customername=null;
    private String cusaddress=null;
    private String compaddress=null;
    private String cusmyid=null;
    private String cusfirstname=null;
    private String cuslastname=null;
    private String cusmobile=null;
    private String cusphone=null;
    private String cusfax=null;
    private String cusemail=null;
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
            customerid = bundle.getString("customerid");
            customername = bundle.getString("customername");
            cusaddress = bundle.getString("address");
            compaddress = bundle.getString("compaddress");
            cusmyid = bundle.getString("myid");
            cusfirstname = bundle.getString("firstname");
            cuslastname = bundle.getString("lastname");
            cusmobile = bundle.getString("mobile");
            cusphone = bundle.getString("phone");
            cusfax = bundle.getString("fax");
            cusemail = bundle.getString("email");
            latlng=bundle.getString("latlng");
            taginfo=bundle.getString("tags");
            customFields=bundle.getString("customFields");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cusmyid != null) {
            job_number_details.setText(cusmyid);
        }
        if (cusaddress != null) {
            main_address_details.setText(cusaddress);
        }
        if (cusfirstname==null){
            cusfirstname="";
        }
        if (cuslastname==null){
            cuslastname="";
        }
        if (cusemail==null){
            cusemail="";
        }
            name_details.setText(cusfirstname+" "+cuslastname+" ("+cusemail+")");

        if (cusmobile != null) {
            mobile_details.setText(cusmobile);
        }
        if (cusphone != null) {
            phone_details.setText(cusphone);
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
                CustomerDetails.this.finish();
                return true;
            case R.id.action_edit:
                Intent i=new Intent(CustomerDetails.this,CustomerUpdateDelete.class);
                i.putExtra("customerid",customerid);
                i.putExtra("customername",customername);
                i.putExtra("address",cusaddress);
                i.putExtra("compaddress",compaddress);
                i.putExtra("myid",cusmyid);
                i.putExtra("firstname",cusfirstname);
                i.putExtra("lastname",cuslastname);
                i.putExtra("mobile",cusmobile);
                i.putExtra("phone",cusphone);
                i.putExtra("fax",cusfax);
                i.putExtra("email",cusemail);
                try {
                    i.putExtra("tags",taginfo.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                i.putExtra("customFields",customFields);
                startActivity(i);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}