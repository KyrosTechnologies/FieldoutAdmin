package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.AdapterAddInvoices;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.common.EndURL;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.models.PdfTotalList;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rohin on 29-12-2017.
 */

public class AddInvoicesListActivity extends AppCompatActivity {

    private PreferenceManager store;
    private String domainid=null;
    private TextView invoices_create_date,invoices_pay_date,mark_as_sent,mark_as_paid,mark_as_late,mark_as_cancel,
            add_invoices_list,text_view_export;
    private RecyclerView invoice_item_recycler;
    private String invoiceid=null;
    private String adapinvoice=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    private ProgressDialog pDialog;
    private List<String[]> data = new ArrayList<>();
    private static final int MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE = 1;
    private AlertDialog showChooseDialog;
    private List<PdfTotalList>pdfTotalListList=new ArrayList<>();
    private String TAG=AddInvoicesListActivity.class.getSimpleName();
    private String status=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.invoices_add_recycler);
        data.add(new String[]{"ITEM", "DESCRIPTION","UNIT PRICE","QUANTITY","DISCOUNT","TOTAL","TAX"});
        invoices_create_date=findViewById(R.id.invoices_create_date);
        invoices_pay_date=findViewById(R.id.invoices_pay_date);
        invoice_item_recycler=findViewById(R.id.invoice_item_recycler);
        add_invoices_list=findViewById(R.id.add_invoices_list);
        text_view_export=findViewById(R.id.text_view_export);
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
            status=bundle.getString("status");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (status.equals("draft")){
            mark_as_sent.setEnabled(true);
            mark_as_sent.setClickable(true);
            mark_as_paid.setEnabled(false);
            mark_as_paid.setClickable(false);
            mark_as_late.setEnabled(false);
            mark_as_late.setClickable(false);
            mark_as_cancel.setEnabled(false);
            mark_as_cancel.setClickable(false);
            Toast.makeText(getApplicationContext(),"Invoice need to be Sent",Toast.LENGTH_SHORT).show();
        }else if (status.equals("sent")){
            mark_as_sent.setEnabled(false);
            mark_as_sent.setClickable(false);
            mark_as_paid.setEnabled(true);
            mark_as_paid.setClickable(true);
            mark_as_late.setEnabled(true);
            mark_as_late.setClickable(true);
            mark_as_cancel.setEnabled(true);
            mark_as_cancel.setClickable(true);
            Toast.makeText(getApplicationContext(),"Already Sent Invoice Cannot Sent again",Toast.LENGTH_SHORT).show();
        }else if (status.equals("paid")){
            mark_as_sent.setEnabled(false);
            mark_as_sent.setClickable(false);
            mark_as_late.setEnabled(false);
            mark_as_late.setClickable(false);
            mark_as_cancel.setEnabled(false);
            mark_as_cancel.setClickable(false);
            Toast.makeText(getApplicationContext(),"Paid Invoice cannot resent or Cancel",Toast.LENGTH_SHORT).show();
        }else if (status.equals("late")){
            mark_as_sent.setEnabled(false);
            mark_as_sent.setClickable(false);
            mark_as_paid.setEnabled(false);
            mark_as_paid.setClickable(false);
            mark_as_cancel.setEnabled(false);
            mark_as_cancel.setClickable(false);
            Toast.makeText(getApplicationContext(),"Late invoices cannot Cancel",Toast.LENGTH_SHORT).show();
        }else if (status.equals("canceled")){
            mark_as_sent.setEnabled(false);
            mark_as_sent.setClickable(false);
            mark_as_paid.setEnabled(false);
            mark_as_paid.setClickable(false);
            mark_as_late.setEnabled(false);
            mark_as_late.setClickable(false);
            Toast.makeText(getApplicationContext(),"Canceled invoice cannot resent or Paid",Toast.LENGTH_SHORT).show();
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
        text_view_export.setOnClickListener(view -> {
            if(pdfTotalListList.size() != 0){
                checkPermission();
            }else{
                showToast("List is empty!");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissChooseDialog();
    }
    private void showDialogChoose(File textFile) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Do you want to open ?");
        builder.setMessage("File stored under : "+textFile.toString());
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            Log.d("Path : ",textFile.toString());
            Uri path= Uri.parse(textFile.toString());
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(path, "resource_type_value/folder");
            startActivity(Intent.createChooser(intent, "Open folder"));
        });
        builder.setNegativeButton("Cancel",((dialogInterface, i) -> dialogInterface.cancel()));
        showChooseDialog=builder.create();
        showChooseDialog.show();

    }
    private void dismissChooseDialog(){
        if(showChooseDialog != null && showChooseDialog.isShowing()){
            showChooseDialog.dismiss();
        }
    }
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)&&ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Toast.makeText(getApplicationContext(),"Please allow read,write permission for storage",Toast.LENGTH_SHORT).show();


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }else{
            try {
                fileWriteCSV(store.getIdDomain()+"_csv_");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_WRITE_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        fileWriteCSV(store.getIdDomain()+"_csv_");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {


                    Toast.makeText(getApplicationContext(),"Please allow read,write permission for storage",Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }

    private  void fileWriteCSV(String title)throws Exception{
        File newFolder = new File(Environment.getExternalStorageDirectory(), "FieldOut");
        if (!newFolder.exists()) {
            newFolder.mkdir();
        }
        File textFile=new File(newFolder,File.separator+title+".csv");
        CSVWriter csvWriter=new CSVWriter(new FileWriter(textFile));
        csvWriter.writeAll(data);
        csvWriter.close();
        showToast("File write successfully");
        showDialogChoose(textFile);
    }



    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
                try {

                    JSONObject obj = new JSONObject(response.toString());
                    JSONObject object = obj.getJSONObject("invoice");

                    String dateString=object.getString("dateString");
                    if (dateString!=null){
                        invoices_create_date.setText(dateString);
                    }
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


//                    if((descriptionArrayList.size() == taxArrayList.size()) && (taxArrayList.size() == discountArrayList.size()) && (discountArrayList.size() == itemArrayList.size()) && (itemArrayList.size() == quantityArrayList.size()) && (quantityArrayList.size() == totalArrayList.size()) && (totalArrayList.size() == unitpriceArrayList.size()) && (unitpriceArrayList.size() == descriptionArrayList.size())){
//                        for(int i=0; i<descriptionArrayList.size(); i++){
//                            PdfTotalList pdfTotalList=new PdfTotalList();
//                            pdfTotalList.setItem(itemArrayList.get(i));
//                            pdfTotalList.setDescription(descriptionArrayList.get(i));
//                            pdfTotalList.setUnitPrice(Integer.parseInt(unitpriceArrayList.get(i)));
//                            pdfTotalList.setQuantity(Integer.parseInt(quantityArrayList.get(i)));
//                            pdfTotalList.setDiscount(Integer.parseInt(discountArrayList.get(i)));
//                            pdfTotalList.setTotal(Integer.parseInt(totalArrayList.get(i)));
//                            pdfTotalList.setTax(Integer.parseInt(taxArrayList.get(i)));
//                            data.add(new String[]{itemArrayList.get(i),descriptionArrayList.get(i),String.valueOf(unitpriceArrayList.get(i)),String.valueOf(quantityArrayList.get(i)),String.valueOf(discountArrayList.get(i)),String.valueOf(totalArrayList.get(i)),String.valueOf(taxArrayList.get(i))});
//                            pdfTotalListList.add(pdfTotalList);
//                        }
//                        Log.d("Pdf Total List : ",TAG+" / / "+new Gson().toJson(pdfTotalListList));
//                    }else{
//                        showToast("list or not same!");
//                    }


                        invoice_item_recycler = findViewById(R.id.invoice_item_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                        invoice_item_recycler.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        invoice_item_recycler.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        AdapterAddInvoices adapterAddInvoices = new AdapterAddInvoices(getApplicationContext(),commonJobsArrayList);
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
                params.put("idDomain",store.getIdDomain());
                params.put("Authorization", store.getToken());
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(AddInvoicesListActivity.this);
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
    private void InvoicesSentApi(){

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "invoices/sent/"+adapinvoice;
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

    private void InvoicesPaidApi(){

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "invoices/paid/"+adapinvoice;
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

    private void InvoicesLateApi(){

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "invoices/late/"+adapinvoice;
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

    private void InvoicesCancelApi(){

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "invoices/cancelled/"+adapinvoice;
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

    @Override
    public void onResume() {
        super.onResume();
        GetInvoiceItemsList();
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
                AddInvoicesListActivity.this.finish();
                return true;
            case R.id.action_edit:
                Intent i=new Intent(AddInvoicesListActivity.this,InvoicesUpdateDelete.class);
                i.putExtra("invoiceid",adapinvoice);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

