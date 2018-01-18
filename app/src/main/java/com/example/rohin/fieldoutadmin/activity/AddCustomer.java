package com.example.rohin.fieldoutadmin.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.example.rohin.fieldoutadmin.adapters.SkilledTradersAdapter;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.common.EndURL;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rohin on 21-12-2017.
 */

public class AddCustomer extends AppCompatActivity {

    private PreferenceManager store;
    private Spinner spinner_time_zone_customer;
    private List<String> timeZoneList=new ArrayList<>();
    private String selectedTimeZone=null;
    private EditText customer_add_edit_text,additional_address_customer,job_number_customer,customer_first_name,customer_last_name,
            phone_customer,mobile_customer,fax_customer,email_customer;
    private TextView tags_add_customer_text;
    private AutoCompleteTextView address_auto_complete_customer;
    private RecyclerView tags_selected_customer;
    private Button save_customer;
    private double latitude=0;
    private double longitude=0;
    private String domainid=null;
    private String myid=null;
    private String cusname=null;
    private String address=null;
    private String addressComplement=null;
    private String contactFirstName=null;
    private String contactLastName=null;
    private String contactPhone=null;
    private String contactMobile=null;
    private String contactEmail=null;
    private String contactFax=null;
    private AlertDialog tagsDialog=null;
    private SkilledTradersAdapter adapter;
    private SkilledTradersAdapter selectedAdapter;
    private List<String>tagsList=new ArrayList<>();
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    ArrayList<String>tagsArrayList=new ArrayList<String>();
    private  JSONArray tagarray=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_customers);
        store= PreferenceManager.getInstance(getApplicationContext());
        spinner_time_zone_customer=findViewById(R.id.spinner_time_zone_customer);
        customer_add_edit_text=findViewById(R.id.customer_add_edit_text);
        additional_address_customer=findViewById(R.id.additional_address_customer);
        address_auto_complete_customer=findViewById(R.id.address_auto_complete_customer);
        job_number_customer=findViewById(R.id.job_number_customer);
        customer_first_name=findViewById(R.id.customer_first_name);
        customer_last_name=findViewById(R.id.customer_last_name);
        phone_customer=findViewById(R.id.phone_customer);
        mobile_customer=findViewById(R.id.mobile_customer);
        fax_customer=findViewById(R.id.fax_customer);
        email_customer=findViewById(R.id.email_customer);
        save_customer=findViewById(R.id.save_customer);
        tags_selected_customer=findViewById(R.id.tags_selected_customer);
        tags_add_customer_text=findViewById(R.id.tags_add_customer_text);
        domainid=store.getIdDomain();
        GetTagsList();

        String[] ids= TimeZone.getAvailableIDs();
        for(String id:ids){
            timeZoneList.add(displayTimeZone(TimeZone.getTimeZone(id)));
        }
        ArrayAdapter<String> adapterTimeZone=new  ArrayAdapter<>(AddCustomer.this,android.R.layout.simple_spinner_item,
                timeZoneList);
        adapterTimeZone.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_time_zone_customer.setAdapter(adapterTimeZone);
        spinner_time_zone_customer.setPrompt("Time Zone");

        spinner_time_zone_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    String text = spinner_time_zone_customer.getSelectedItem().toString();
                    selectedTimeZone=text;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tags_add_customer_text.setOnClickListener(view -> skilledTradesDialog(new ArrayList<>(),tags_add_customer_text));

        save_customer.setOnClickListener(view -> {
            cusname=customer_add_edit_text.getText().toString();
            if (cusname==null && cusname.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Customer Name!", Toast.LENGTH_SHORT).show();
                return ;
            }
            address=address_auto_complete_customer.getText().toString();
            if (address==null && address.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Address!", Toast.LENGTH_SHORT).show();
                return ;
            }
            myid=job_number_customer.getText().toString();
            if (myid==null && myid.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Custom Job!", Toast.LENGTH_SHORT).show();
                return ;
            }

            if(cusname!=null &&!cusname.isEmpty()&&address!=null &&!address.isEmpty()&&myid!=null &&!myid.isEmpty()){
                addressComplement=additional_address_customer.getText().toString();
                contactFirstName=customer_first_name.getText().toString();
                contactLastName=customer_last_name.getText().toString();
                contactPhone=phone_customer.getText().toString();
                contactMobile=mobile_customer.getText().toString();
                contactFax=fax_customer.getText().toString();
                contactEmail=email_customer.getText().toString();
                GetLatLngList(address);
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void skilledTradesDialog(List<String> skilledName,TextView tView) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AddCustomer.this);
        LayoutInflater layoutInflate=getLayoutInflater();
        View view=layoutInflate.inflate(R.layout.adapter_teams_list,null);
        EditText editText=view.findViewById(R.id.et_skilled);
        editText.setHint("Enter tags");
        Button button=view.findViewById(R.id.button_add_skill);
        RecyclerView recyclerView=view.findViewById(R.id.skilled_traders_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new SkilledTradersAdapter(this,skilledName);
        recyclerView.setAdapter(adapter);
        builder.setView(view);
        button.setOnClickListener(views->{
            String edName=editText.getText().toString();
            if(edName!=null && !edName.isEmpty()){
                adapter.setSkilledTradersName(edName);
                editText.setText("");
            }
        });
        builder.setPositiveButton("OK",(dialog,id)->{
            tagsList=adapter.getSkilledTradersList();
            if(tagsList.size()!=0){
                tags_selected_customer.setVisibility(View.VISIBLE);
                selectedAdapter=new SkilledTradersAdapter(this,tagsList);
                tags_selected_customer.setItemAnimator(new DefaultItemAnimator());
                tags_selected_customer.setLayoutManager(new LinearLayoutManager(this));
                tags_selected_customer.setAdapter(selectedAdapter);
                selectedAdapter.notifyDataSetChanged();
                for (int i=0;i<tagsList.size();i++){
                    AddTagsApi(tagsList.get(i));
                }
            }

        });
        builder.setNegativeButton("Cancel",(dialog,i)->{
            dismissTagsDialog();
        });
        tagsDialog=builder.create();
        tagsDialog.show();

    }
    private  void dismissTagsDialog(){
        if(tagsDialog!=null && tagsDialog.isShowing()){
            tagsDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissTagsDialog();

    }

    private  String displayTimeZone(TimeZone timeZone){
        long hours= TimeUnit.MILLISECONDS.toHours(timeZone.getRawOffset());
        long minutes=TimeUnit.MILLISECONDS.toMinutes(timeZone.getRawOffset()-TimeUnit.HOURS.toMinutes(hours));
        minutes=Math.abs(minutes);
        String result="";
        if (hours > 0) {
            result = String.format("(GMT+%d:%02d) %s", hours, minutes, timeZone.getID());
        } else {
            result = String.format("(GMT%d:%02d) %s", hours, minutes, timeZone.getID());
        }

        return result;

    }

    private void GetLatLngList(String address) {
        String tag_json_obj = "json_obj_req";
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+address+"&key=AIzaSyD917w2chNF_jp_9W5f7s-yZ-jTqcYY3Lg";
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("results");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        JSONObject geometry=first.getJSONObject("geometry");
                        JSONObject location=geometry.getJSONObject("location");
                        double lat=location.getDouble("lat");
                        AddCustomer.this.latitude=lat;
                        double lng=location.getDouble("lng");
                        AddCustomer.this.longitude=lng;

                    }

                    AddCustomerApi(cusname,myid,address,addressComplement,contactFirstName,contactLastName,contactPhone,contactMobile,contactEmail,contactFax,latitude,longitude);

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

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void GetTagsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"tags/getByDomainId/"+domainid;
        Log.d("waggonurl", url);
        //showProgressDialog();

        JSONObject inputjso=new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, inputjso, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                commonJobsArrayList.clear();

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("tags");
                    tagarray=new JSONArray(array.toString());
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String tagid=first.getString("id");
                        String tagname=first.getString("name");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setTagid(tagid);
                        commonJobs.setTagname(tagname);
                        commonJobsArrayList.add(commonJobs);
                        tagsArrayList.add(tagname);

                    }

                    if (tagsArrayList.size()!=0){
                        tags_selected_customer.setVisibility(View.VISIBLE);
                        tags_selected_customer = findViewById(R.id.tags_selected_customer);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                        tags_selected_customer.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        tags_selected_customer.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        SkilledTradersAdapter projectsAdapter = new SkilledTradersAdapter(AddCustomer.this,tagsArrayList);
                        tags_selected_customer.setAdapter(projectsAdapter);
                        projectsAdapter.notifyDataSetChanged();
                    }else {
                        tags_selected_customer.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

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

    private void AddCustomerApi(String myid, String name, String globalAddress, String complementaddress, String firstname,
                            String lastname,String phone, String mobile,  String email, String fax, double latitude, double longitude) {

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "customers/send";
        Log.d("waggonurl", url);
        JSONObject inputLogin = new JSONObject();

        JSONObject position=new JSONObject();
        try {
            position.put("lat",latitude);
            position.put("lng",longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String>tagListId=new ArrayList<String>();
        for (int i=0;i<tagarray.length();i++){
            JSONObject first= null;
            try {
                first = tagarray.getJSONObject(i);
                String tagid=first.getString("id");
                String tagname=first.getString("name");
                if (tagsArrayList.get(i).equals(tagname)){
                    tagListId.add(tagid);
                }
            } catch (JSONException e) {
            }

        }

        JSONArray jsonArray=new JSONArray();
        for (int i=0;i<tagListId.size();i++){
            jsonArray.put(tagListId.get(i));
        }

        try {
          inputLogin.put("name",myid);
          inputLogin.put("myId",name);
          inputLogin.put("address",globalAddress);
          inputLogin.put("idDomain",domainid);
          inputLogin.put("addressComplement",complementaddress);
          inputLogin.put("contactFirstName",firstname);
          inputLogin.put("contactLastName",lastname);
          inputLogin.put("contactPhone",phone);
          inputLogin.put("contactMobile",mobile);
          inputLogin.put("contactEmail",email);
          inputLogin.put("contactFax",fax);
          inputLogin.put("positions",position);
          inputLogin.put("tags",jsonArray);

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
                        JSONObject first = obj.getJSONObject("customer");
                        String customerid=first.getString("id");
                        store.putCustomerId(String.valueOf(customerid));

                        AddCustomer.this.finish();


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

    private void AddTagsApi(String name) {

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "tags/add";
        Log.d("waggonurl", url);
        JSONObject inputLogin = new JSONObject();

        try {
            inputLogin.put("name",name);
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
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("tag");
                        String tagid=first.getString("id");
                        store.putTagId(String.valueOf(tagid));

                        tagsArrayList.add(tagid);

                    }


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AddCustomer.this.finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}