package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.AdapterAddQuotations;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.common.EndURL;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rohin on 04-01-2018.
 */

public class AddQuotationsListActivity extends AppCompatActivity {

    private PreferenceManager store;
    private String domainid=null;
    private TextView mark_as_sent,mark_as_delete,mark_as_accept;
    private RecyclerView quotation_item_recycler;
    private TextView add_quotations_list;
    private String quotationid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
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
        setContentView(R.layout.quotations_add_recycler);
        quotation_item_recycler=findViewById(R.id.quotation_item_recycler);
        add_quotations_list=findViewById(R.id.add_quotations_list);
        mark_as_sent=findViewById(R.id.mark_as_sent);
        mark_as_delete=findViewById(R.id.mark_as_delete);
        mark_as_accept=findViewById(R.id.mark_as_accept);
        store = PreferenceManager.getInstance(getApplicationContext());
        domainid = store.getIdDomain();

        try {
            Bundle bundle = getIntent().getExtras();
            quotationid=bundle.getString("quotationid");
        } catch (Exception e) {
            e.printStackTrace();
        }

        add_quotations_list.setOnClickListener(view -> {
            Intent i=new Intent(AddQuotationsListActivity.this,AddQuotations.class);
            i.putExtra("quotationid",quotationid);
            startActivity(i);
        });

        mark_as_sent.setOnClickListener(view -> {
            QuotationsSentApi();
        });

        mark_as_accept.setOnClickListener(view -> {
            QuotationsAcceptApi();
        });

        mark_as_delete.setOnClickListener(view -> {
            showDeleteDialog();
        });

    }

    private void GetQuotationItemsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"quotations/getById/"+quotationid;
        store.putQuotationId(quotationid);
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

                    JSONObject obj = new JSONObject(response.toString());
                    JSONObject object = obj.getJSONObject("quotation");

                    String dateString=object.getString("dateString");
                    JSONArray items=object.getJSONArray("items");
                    for (int i=0;i<items.length();i++){
                        JSONObject itemarray = items.getJSONObject(i);
                        String id=itemarray.getString("id");
                        String amount=itemarray.getString("amount");
                        String description=itemarray.getString("description");
                        String discount=itemarray.getString("discount");
                        String item=itemarray.getString("item");
                        String quantity=itemarray.getString("quantity");
                        String tax=itemarray.getString("tax");
                        String total=itemarray.getString("total");
                        String unit_price=itemarray.getString("unit_price");

                        CommonJobs commonJobs = new CommonJobs();
                        commonJobs.setItemid(id);
                        commonJobs.setDescription(description);
                        commonJobs.setTax(tax);
                        commonJobs.setDiscount(discount);
                        commonJobs.setItemname(item);
                        commonJobs.setQuantity(quantity);
                        commonJobs.setTotal(total);
                        commonJobs.setUnitproice(unit_price);
                        commonJobsArrayList.add(commonJobs);

                    }


                    quotation_item_recycler = findViewById(R.id.quotation_item_recycler);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                    quotation_item_recycler.setLayoutManager(layoutManager);
                    //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                    quotation_item_recycler.setItemAnimator(new DefaultItemAnimator());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    AdapterAddQuotations adapterAddInvoices = new AdapterAddQuotations(getApplicationContext(),commonJobsArrayList);
                    quotation_item_recycler.setAdapter(adapterAddInvoices);
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
                params.put("idDomain",store.getIdDomain());
                params.put("Authorization", store.getToken());
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(AddQuotationsListActivity.this);
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

    private void QuotationsSentApi(){

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "quotations/sent/"+quotationid;
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
                        JSONObject first = obj.getJSONObject("quotation");

                    }
                    AddQuotationsListActivity.this.finish();

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
                params.put("idDomain",store.getIdDomain());
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void QuotationsAcceptApi(){

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "quotations/accepted/"+quotationid;
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
                        JSONObject first = obj.getJSONObject("quotation");

                    }
                    AddQuotationsListActivity.this.finish();

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
                params.put("idDomain",store.getIdDomain());
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void QuotationsDeleteApi() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"quotations/delete/"+quotationid;
        Log.d("waggonurl", url);
        //showProgressDialog();

        JSONObject inputjso=new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.DELETE, url, inputjso, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    String array=obj.getString("result");

                }catch (Exception e){
                    e.printStackTrace();
                }
                AddQuotationsListActivity.this.finish();
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
                params.put("idDomain",store.getIdDomain());
                params.put("Authorization", store.getToken());
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void showDeleteDialog(){
        if(builder==null){
            builder = new AlertDialog.Builder(AddQuotationsListActivity.this);
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        QuotationsDeleteApi();
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
    public void onResume() {
        super.onResume();
        GetQuotationItemsList();
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
                AddQuotationsListActivity.this.finish();
                return true;
            case R.id.action_edit:
                Intent i=new Intent(AddQuotationsListActivity.this,QuotationsUpdateDelete.class);
                i.putExtra("quotationid",quotationid);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

