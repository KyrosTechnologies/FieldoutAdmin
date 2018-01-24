package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.common.EndURL;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rohin on 03-01-2018.
 */

public class AddInvoices extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private PreferenceManager store;
    private EditText item_invoice,desc_invoice,unit_price_invoice,quantity_invoice,discount_invoice;
    private TextView total_invoice;
    private Button save_invoice_add;
    private String domainid=null;
    private Spinner tax_invoice_spinner;
    private String invoiceid=null;
    private String item=null;
    private String description=null;
    private String unitprice="0";
    private String quantity="0";
    private String discount="0";
    private String total=null;
    private String tax=null;
    private List<String> spinnerlist=new ArrayList<String>();
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    private String taxrate="0";
    private double price=0;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.add_invoices_list);
        item_invoice=findViewById(R.id.item_invoice);
        desc_invoice=findViewById(R.id.desc_invoice);
        unit_price_invoice=findViewById(R.id.unit_price_invoice);
        quantity_invoice=findViewById(R.id.quantity_invoice);
        discount_invoice=findViewById(R.id.discount_invoice);
        total_invoice=findViewById(R.id.total_invoice);
        tax_invoice_spinner=findViewById(R.id.tax_invoice_spinner);
        save_invoice_add=findViewById(R.id.save_invoice_add);
        store = PreferenceManager.getInstance(getApplicationContext());
        domainid=store.getIdDomain();
        tax_invoice_spinner.setOnItemSelectedListener(this);
        GetTaxList();

        try {
            Bundle bundle = getIntent().getExtras();
            invoiceid=bundle.getString("invoiceid");
        } catch (Exception e) {
            e.printStackTrace();
        }

        item=item_invoice.getText().toString();
        description=desc_invoice.getText().toString();
        unitprice=unit_price_invoice.getText().toString();
        quantity=quantity_invoice.getText().toString();
        discount=discount_invoice.getText().toString();
        unit_price_invoice.addTextChangedListener(textWatcher);
        quantity_invoice.addTextChangedListener(textWatcher);
        discount_invoice.addTextChangedListener(textWatcher);

        save_invoice_add.setOnClickListener(view -> {
            item=item_invoice.getText().toString();
            if (item==null && item.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Item Name!", Toast.LENGTH_SHORT).show();
                return ;
            }
            description=desc_invoice.getText().toString();
            if (description==null && description.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Description!", Toast.LENGTH_SHORT).show();
                return ;
            }
            unitprice=unit_price_invoice.getText().toString();
            if (unitprice==null && unitprice.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Unit Price!", Toast.LENGTH_SHORT).show();
                return ;
            }
            quantity=quantity_invoice.getText().toString();
            if (quantity==null && quantity.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Quantity!", Toast.LENGTH_SHORT).show();
                return ;
            }
            discount=discount_invoice.getText().toString();
            if (discount==null && discount.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Discount!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String taxes = null;
            try {
                taxes=tax_invoice_spinner.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(taxes==null){
                Toast.makeText(getApplicationContext(), "Please Select Tax Rate!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(item!=null &&!item.isEmpty()&&description!=null &&!description.isEmpty()&& unitprice!=null&&!unitprice.isEmpty()
                    &&!quantity.isEmpty()&&quantity!=null&&discount!=null &&!discount.isEmpty()&&taxes!=null){
                InvoicesAddItemsApi();
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            unitprice=unit_price_invoice.getText().toString();
            quantity=quantity_invoice.getText().toString();
            discount=discount_invoice.getText().toString();
            if (!unitprice.isEmpty()&&!quantity.isEmpty()&&!taxrate.isEmpty()){
                if (discount.equals(" ")||discount.equals("")){
                    discount="0";
                }

                double tamount=Double.parseDouble(unitprice)*Double.parseDouble(quantity);
                price=tamount;

                double taxprice=Double.parseDouble(taxrate)/100;
                double amount1=tamount*taxprice;
                double taxamount=tamount+amount1;
                double discountprice=Double.parseDouble(discount)/100;
                double amount2=tamount*discountprice;
                double totalamount=taxamount-amount2;

                String totaltext=String.valueOf(totalamount);
                if (totaltext!=null){
                    total_invoice.setText(totaltext);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void GetTaxList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"taxes/getByDomainId/"+domainid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("taxes");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String taxid=first.getString("id");
                        store.putTaxId(String.valueOf(taxid));
                        String taxrate=first.getString("rate");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setTaxid(taxid);
                        commonJobs.setTaxrate(taxrate);
                        commonJobsArrayList.add(commonJobs);
                        spinnerlist.add(taxrate);

                    }

                    for (String s:spinnerlist) {

                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(AddInvoices.this,android.R.layout.simple_spinner_item,
                            spinnerlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    tax_invoice_spinner.setPrompt("Tax");
                    tax_invoice_spinner.setAdapter(adapter);
                    tax_invoice_spinner.setAdapter(new SpinnerDetails(adapter,R.layout.tax,AddInvoices.this));
                    tax_invoice_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = tax_invoice_spinner.getSelectedItem().toString();
                                taxrate=text;
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            unitprice=unit_price_invoice.getText().toString();
                            quantity=quantity_invoice.getText().toString();
                            discount=discount_invoice.getText().toString();
                            if (!unitprice.isEmpty()&&!quantity.isEmpty()&&!taxrate.isEmpty()){
                                if (discount.equals(" ")||discount.equals("")){
                                    discount="0";
                                }

                                double tamount=Double.parseDouble(unitprice)*Double.parseDouble(quantity);
                                price=tamount;

                                double taxprice=Double.parseDouble(taxrate)/100;
                                double amount1=tamount*taxprice;
                                double taxamount=tamount+amount1;
                                double discountprice=Double.parseDouble(discount)/100;
                                double amount2=tamount*discountprice;
                                double totalamount=taxamount-amount2;

                                String totaltext=String.valueOf(totalamount);
                                if (totaltext!=null){
                                    total_invoice.setText(totaltext);
                                }
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

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(AddInvoices.this);
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

    private void InvoicesAddItemsApi(){

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "invoices/addItems/"+invoiceid;
        Log.d("waggonurl", url);
        showProgressDialog();
        JSONObject inputLogin = new JSONObject();

        if (discount.equals(" ")||discount.equals("")){
            discount="0";
        }

        try {
            inputLogin.put("amount",price);
            inputLogin.put("tax",Integer.parseInt(taxrate));
            inputLogin.put("item",item);
            inputLogin.put("description",description);
            inputLogin.put("unit_price",Double.parseDouble(unitprice));
            inputLogin.put("quantity",Double.parseDouble(quantity));
            inputLogin.put("discount",Double.parseDouble(discount));

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("inputJsonuser", inputLogin.toString());
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
                        String first = obj.getString("result");

                    }
                    AddInvoices.this.finish();

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
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AddInvoices.this.finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}