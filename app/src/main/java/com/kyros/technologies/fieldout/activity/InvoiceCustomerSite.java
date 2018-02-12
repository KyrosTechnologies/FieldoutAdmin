package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Spinner;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rohin on 29-12-2017.
 */

public class InvoiceCustomerSite extends AppCompatActivity {

    private PreferenceManager store;
    private Button save_invoice_cus_site;
    private Spinner incoices_customer_spinner,incoices_site_spinner;
    private String domainid=null;
    private String cusid=null;
    private String siteid=null;
    private String invoiceid=null;
    private List<String> spinnerlist=new ArrayList<String>();
    private List<String> sitelist=new ArrayList<String>();
    ArrayList<CommonJobs> cusDetailsArrayList = new ArrayList<CommonJobs>();
    private List<String> sitenamestring=new ArrayList<String>();
    ArrayList<CommonJobs> siteDetailsArrayList = new ArrayList<CommonJobs>();
    ArrayList<String > customerIdArrayList = new ArrayList<String>();
    private String customertext=null;
    private String sitetext=null;
    private String dtformat=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.invoices_add_cus_site);
        store = PreferenceManager.getInstance(getApplicationContext());
        save_invoice_cus_site=findViewById(R.id.save_invoice_cus_site);
        incoices_customer_spinner=findViewById(R.id.incoices_customer_spinner);
        incoices_site_spinner=findViewById(R.id.incoices_site_spinner);
        domainid=store.getIdDomain();
        GetCustomerList();

        save_invoice_cus_site.setOnClickListener(view -> {
            String customerspinner = null;
            String sitespinner = null;
            try {
                customerspinner=incoices_customer_spinner.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(customerspinner==null){
                Toast.makeText(getApplicationContext(), "Please Select Customer Name!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                sitespinner=incoices_site_spinner.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(sitespinner==null){
                Toast.makeText(getApplicationContext(), "Please Select Site Name!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(customerspinner!=null&&sitespinner!=null){
                InvoicesAddApi();
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();

            }
            });

    }

    private void GetCustomerList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"customers/getAll";
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                cusDetailsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("customers");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String customerid=first.getString("id");
                        store.putCustomerId(String.valueOf(customerid));
                        String name="";
                        try {
                            name=first.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String contactEmail="";
                        try {
                            contactEmail=first.getString("contactEmail");
                        }catch (Exception e){

                        }
                        String contactfirstname="";
                        try {
                            contactfirstname=first.getString("contactFirstName");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String contactlastname="";
                        try {
                            contactlastname=first.getString("contactLastName");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String contactmobile="";
                        try {
                            contactmobile=first.getString("contactMobile");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String contactphone="";
                        try {
                            contactphone=first.getString("contactPhone");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String address="";
                        try {
                            address=first.getString("address");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String addressComplement="";
                        try {
                            addressComplement=first.getString("addressComplement");
                        }catch (Exception e){

                        }
                        JSONObject tagInfo =null;
                        try {
                            tagInfo = first.getJSONObject("tagInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        String tagid=null;
                        try {
                            tagid=tagInfo.getString("id");
                            store.putTagId(String.valueOf(tagid));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String tagname="";
                        try {
                            tagname=tagInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setCustomername(name);
                        commonJobs.setCustomerid(customerid);
                        InvoiceCustomerSite.this.cusid=customerid;
                        customerIdArrayList.add(customerid);
                        commonJobs.setFirstname(contactfirstname);
                        commonJobs.setLastname(contactlastname);
                        commonJobs.setMobilenum(contactmobile);
                        commonJobs.setPhone(contactphone);
                        commonJobs.setAddress(address);
                        commonJobs.setTagname(tagname);
                        commonJobs.setComplementAddress(addressComplement);
                        commonJobs.setEmail(contactEmail);
                        cusDetailsArrayList.add(commonJobs);
                        spinnerlist.add(name);

                    }
                    for (String s:spinnerlist) {
                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(InvoiceCustomerSite.this,android.R.layout.simple_spinner_item,
                            spinnerlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    incoices_customer_spinner.setPrompt("Customer");
                    incoices_customer_spinner.setAdapter(adapter);
                    incoices_customer_spinner.setAdapter(new SpinnerDetails(adapter,R.layout.customer,InvoiceCustomerSite.this));
                    incoices_customer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = incoices_customer_spinner.getSelectedItem().toString();
                                customertext=text;
                                int j=i-1;
                                GetSitesList(customerIdArrayList.get(j));
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
                Log.e("Error"," "+error.getMessage());

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

    private void GetSitesList(String cusid) {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"sites/getByCustomerId/"+cusid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                siteDetailsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("sites");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String siteid=first.getString("id");
                        store.putSiteId(String.valueOf(siteid));
                        String sitename="";
                        try {
                            sitename=first.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setSitename(sitename);
                        commonJobs.setSiteid(siteid);
                        InvoiceCustomerSite.this.siteid=siteid;
                        siteDetailsArrayList.add(commonJobs);
                        sitelist.add(sitename);

                    }
                    for (String s:sitelist) {

                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(InvoiceCustomerSite.this,android.R.layout.simple_spinner_item,
                            sitelist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    incoices_site_spinner.setPrompt("Site");
                    incoices_site_spinner.setAdapter(adapter);
                    incoices_site_spinner.setAdapter(new SpinnerDetails(adapter,R.layout.site,InvoiceCustomerSite.this));
                    incoices_site_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = incoices_site_spinner.getSelectedItem().toString();
                                sitetext=text;
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
                Log.e("Error"," "+error.getMessage());

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

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(InvoiceCustomerSite.this);
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

    private void InvoicesAddApi() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"invoices/add";
        store= PreferenceManager.getInstance(getApplicationContext());
        Log.d("waggonurl", url);
        showProgressDialog();
        JSONObject inputLogin=new JSONObject();
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        dtformat=dateFormat.format(new Date());
        try{
            inputLogin.put("idCustomer",cusid);
            inputLogin.put("idSite",siteid);
            inputLogin.put("status","draft");
            inputLogin.put("dateString",dtformat);

        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("inputJsonuser",inputLogin.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.d("List TeamsResponse",response.toString());
                  try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("invoice");
                        String invoiceid = first.getString("id");
                        store.putInvoiceId(String.valueOf(invoiceid));

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setInvoiceId(invoiceid);
                        commonJobsArrayList.add(commonJobs);
                        Intent i=new Intent(InvoiceCustomerSite.this,AddInvoicesListActivity.class);
                        i.putExtra("invoiceid",invoiceid);
                        startActivity(i);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error!=null){
                    Log.e("Error",""+error.toString());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                InvoiceCustomerSite.this.finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
