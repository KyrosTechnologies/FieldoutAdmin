package com.example.rohin.fieldoutadmin.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.adapters.AdapterAddInvoices;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.common.EndURL;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rohin on 29-12-2017.
 */

public class AddInvoicesListActivity extends AppCompatActivity {

    private PreferenceManager store;
    private String domainid=null;
    private TextView invoices_create_date,invoices_pay_date,mark_as_sent,mark_as_paid,mark_as_late,mark_as_cancel;
    private RecyclerView invoice_item_recycler;
    private LinearLayout add_invoices_list;
    private String invoiceid=null;
    private String adapinvoice=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    ArrayList<String>descriptionArrayList=new ArrayList<String>();
    ArrayList<String>discountArrayList=new ArrayList<String>();
    ArrayList<String>itemArrayList=new ArrayList<String>();
    ArrayList<String>quantityArrayList=new ArrayList<String>();
    ArrayList<String>taxArrayList=new ArrayList<String>();
    ArrayList<String>totalArrayList=new ArrayList<String>();
    ArrayList<String>unitpriceArrayList=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.invoices_add_recycler);
        invoices_create_date=findViewById(R.id.invoices_create_date);
        invoices_pay_date=findViewById(R.id.invoices_pay_date);
        invoice_item_recycler=findViewById(R.id.invoice_item_recycler);
        add_invoices_list=findViewById(R.id.add_invoices_list);
        mark_as_sent=findViewById(R.id.mark_as_sent);
        mark_as_paid=findViewById(R.id.mark_as_paid);
        mark_as_late=findViewById(R.id.mark_as_late);
        mark_as_cancel=findViewById(R.id.mark_as_cancel);
        store = PreferenceManager.getInstance(getApplicationContext());
        domainid = store.getIdDomain();

        try {
            Bundle bundle = getIntent().getExtras();
            invoiceid=bundle.getString("invoiceid");
            adapinvoice=bundle.getString("invoiceid");
        } catch (Exception e) {
            e.printStackTrace();
        }

        add_invoices_list.setOnClickListener(view -> {
            Intent i=new Intent(AddInvoicesListActivity.this,AddInvoices.class);
            i.putExtra("invoiceid",invoiceid);
            startActivity(i);
        });

        mark_as_sent.setOnClickListener(view -> {
            InvoicesSentApi();
        });
        mark_as_paid.setOnClickListener(view -> {
            InvoicesPaidApi();
        });
        mark_as_late.setOnClickListener(view -> {
            InvoicesLateApi();
        });
        mark_as_cancel.setOnClickListener(view -> {
            InvoicesCancelApi();
        });
    }

    private void GetInvoiceItemsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"invoices/getById/"+adapinvoice;
        store.putInvoiceId(adapinvoice);
        Log.d("waggonurl", url);
        //showProgressDialog();

        JSONObject inputjso=new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, inputjso, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                commonJobsArrayList.clear();
                descriptionArrayList.clear();
                discountArrayList.clear();
                itemArrayList.clear();
                quantityArrayList.clear();
                taxArrayList.clear();
                totalArrayList.clear();
                unitpriceArrayList.clear();
                try {

                    JSONObject obj = new JSONObject(response.toString());
                    JSONObject object = obj.getJSONObject("invoice");

                    String dateString=object.getString("dateString");
                    if (dateString!=null){
                        invoices_create_date.setText(dateString);
                    }

                    JSONArray descriptionarray = object.getJSONArray("description");
                    for (int i = 0; i < descriptionarray.length(); i++) {
                        String description=descriptionarray.getString(i);
                        descriptionArrayList.add(description);
                    }

                    JSONArray discountarray=object.getJSONArray("discount");
                    for (int i=0;i<discountarray.length();i++){
                        String discount=discountarray.getString(i);
                        discountArrayList.add(discount);
                    }

                    JSONArray itemarray=object.getJSONArray("item");
                    for (int i=0;i<itemarray.length();i++){
                        String item=itemarray.getString(i);
                        itemArrayList.add(item);
                    }

                    JSONArray quantityarray=object.getJSONArray("quantity");
                    for (int i=0;i<quantityarray.length();i++){
                        String quantity=quantityarray.getString(i);
                        quantityArrayList.add(quantity);
                    }

                    JSONArray taxarray=object.getJSONArray("tax");
                    for (int i=0;i<taxarray.length();i++){
                        String tax=taxarray.getString(i);
                        taxArrayList.add(tax);
                    }

                    JSONArray totalarray=object.getJSONArray("total");
                    for (int i=0;i<totalarray.length();i++){
                        String total=totalarray.getString(i);
                        totalArrayList.add(total);
                    }

                    JSONArray unitpricearray=object.getJSONArray("unit_price");
                    for (int i=0;i<unitpricearray.length();i++){
                        String unitprice=unitpricearray.getString(i);
                        unitpriceArrayList.add(unitprice);
                    }

                    CommonJobs commonJobs = new CommonJobs();
                    commonJobs.setDescriptionlist(descriptionArrayList);
                    commonJobs.setTaxlist(taxArrayList);
                    commonJobs.setDiscount(discountArrayList);
                    commonJobs.setItem(itemArrayList);
                    commonJobs.setQuantity(quantityArrayList);
                    commonJobs.setTotal(totalArrayList);
                    commonJobs.setUnitprice(unitpriceArrayList);
                    commonJobsArrayList.add(commonJobs);


                        invoice_item_recycler = findViewById(R.id.invoice_item_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                        invoice_item_recycler.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        invoice_item_recycler.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        AdapterAddInvoices adapterAddInvoices = new AdapterAddInvoices(getApplicationContext(),commonJobsArrayList,descriptionArrayList,
                                discountArrayList,itemArrayList,quantityArrayList,taxArrayList,totalArrayList,unitpriceArrayList);
                        invoice_item_recycler.setAdapter(adapterAddInvoices);
                        adapterAddInvoices.notifyDataSetChanged();

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

    private void InvoicesSaveApi(){

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "invoices/save/"+adapinvoice;
        Log.d("waggonurl", url);
        JSONObject inputLogin = new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response", response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        String first = obj.getString("result");

                    }
                    AddInvoicesListActivity.this.finish();
//                    InvoicesFragment h= new InvoicesFragment();
//                    android.support.v4.app.FragmentTransaction k=
//                            getSupportFragmentManager().beginTransaction();
//                    k.replace(R.id.container_fragments,h);
//                    k.commit();

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

    private void InvoicesSentApi(){

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "invoices/sent/"+adapinvoice;
        Log.d("waggonurl", url);
        JSONObject inputLogin = new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response", response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("invoice");

                    }
                    AddInvoicesListActivity.this.finish();

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

    private void InvoicesPaidApi(){

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "invoices/paid/"+adapinvoice;
        Log.d("waggonurl", url);
        JSONObject inputLogin = new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response", response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("invoice");

                    }
                    AddInvoicesListActivity.this.finish();

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

    private void InvoicesLateApi(){

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "invoices/late/"+adapinvoice;
        Log.d("waggonurl", url);
        JSONObject inputLogin = new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response", response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("invoice");

                    }
                    AddInvoicesListActivity.this.finish();

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

    private void InvoicesCancelApi(){

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "invoices/cancelled/"+adapinvoice;
        Log.d("waggonurl", url);
        JSONObject inputLogin = new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response", response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("invoice");

                    }
                    AddInvoicesListActivity.this.finish();

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
    public void onResume() {
        super.onResume();
        GetInvoiceItemsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AddInvoicesListActivity.this.finish();
                return true;
            case R.id.action_save:
                InvoicesSaveApi();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

