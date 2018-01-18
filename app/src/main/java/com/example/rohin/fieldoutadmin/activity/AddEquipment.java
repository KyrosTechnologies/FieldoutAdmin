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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Rohin on 23-12-2017.
 */

public class AddEquipment extends AppCompatActivity {

    private PreferenceManager store;
    private EditText equipment_name_edit_text,job_number_equipment;
    private AutoCompleteTextView equipment_cus_auto_complete,equipment_site_auto_complete;
    private Button save_equipment;
    private RecyclerView tags_selected_equipment;
    private TextView tags_add_equipment_text;
    private String domainid=null;
    private String cusid=null;
    private String siteid=null;
    private List<String> cusnamestring=new ArrayList<String>();
    ArrayList<CommonJobs> cusDetailsArrayList = new ArrayList<CommonJobs>();
    private List<String> sitenamestring=new ArrayList<String>();
    ArrayList<CommonJobs> siteDetailsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    private String myid=null;
    private String cusname=null;
    private String sitename=null;
    private String equipmentname=null;
    private String queryurl = null;
    private AlertDialog tagsDialog=null;
    private SkilledTradersAdapter adapter;
    private SkilledTradersAdapter selectedAdapter;
    private List<String>tagsList=new ArrayList<>();
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
        setContentView(R.layout.add_equipments);
        store = PreferenceManager.getInstance(getApplicationContext());
        equipment_name_edit_text=findViewById(R.id.equipment_name_edit_text);
        job_number_equipment=findViewById(R.id.job_number_equipment);
        equipment_cus_auto_complete=findViewById(R.id.equipment_cus_auto_complete);
        equipment_site_auto_complete=findViewById(R.id.equipment_site_auto_complete);
        save_equipment=findViewById(R.id.save_equipment);
        tags_add_equipment_text=findViewById(R.id.tags_add_equipment_text);
        tags_selected_equipment=findViewById(R.id.tags_selected_equipment);
        domainid=store.getIdDomain();
        GetCustomerList();
        GetTagsList();

        save_equipment.setOnClickListener(view -> {
            cusname=equipment_cus_auto_complete.getText().toString();
            if (cusname==null && cusname.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Customer Name!", Toast.LENGTH_SHORT).show();
                return ;
            }
            sitename=equipment_site_auto_complete.getText().toString();
            if (sitename==null && sitename.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Site Name!", Toast.LENGTH_SHORT).show();
                return ;
            }
            equipmentname=equipment_name_edit_text.getText().toString();
            if (equipmentname==null && equipmentname.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Equipment Namr!", Toast.LENGTH_SHORT).show();
                return ;
            }
            myid=job_number_equipment.getText().toString();
            if (myid==null && myid.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Custom Job!", Toast.LENGTH_SHORT).show();
                return ;
            }

            if(cusname!=null &&!cusname.isEmpty()&&sitename!=null &&!sitename.isEmpty()&&equipmentname!=null &&!equipmentname.isEmpty()&&myid!=null &&!myid.isEmpty()){
                String cusname=equipment_cus_auto_complete.getText().toString();
                for (int i=0;i<cusDetailsArrayList.size();i++){
                    if (cusname.equals(cusDetailsArrayList.get(i).getCustomername())){
                        cusid=cusDetailsArrayList.get(i).getCustomerid();
                    }
                }
                String sitename=equipment_site_auto_complete.getText().toString();
                for (int i=0;i<siteDetailsArrayList.size();i++){
                    if (sitename.equals(siteDetailsArrayList.get(i).getSitename())){
                        siteid=siteDetailsArrayList.get(i).getSiteid();
                    }
                }
                AddEquipmentApi(equipmentname,myid,cusid,siteid);
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();
            }
        });

        tags_add_equipment_text.setOnClickListener(view -> skilledTradesDialog(new ArrayList<>(),tags_add_equipment_text));


        equipment_cus_auto_complete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String g = s.toString();
                g = g.replace(" ", "");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                queryurl = s.toString();
                for (int i=0;i<cusDetailsArrayList.size();i++){
                    if (queryurl.equals(cusDetailsArrayList.get(i).getCustomername())){
                        cusid=cusDetailsArrayList.get(i).getCustomerid();
                    }
                }
                GetSitesList();
            }

        });

    }

    private void skilledTradesDialog(List<String> skilledName,TextView tView) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AddEquipment.this);
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
                tags_selected_equipment.setVisibility(View.VISIBLE);
                selectedAdapter=new SkilledTradersAdapter(this,tagsList);
                tags_selected_equipment.setItemAnimator(new DefaultItemAnimator());
                tags_selected_equipment.setLayoutManager(new LinearLayoutManager(this));
                tags_selected_equipment.setAdapter(selectedAdapter);
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
                        tags_selected_equipment.setVisibility(View.VISIBLE);
                        tags_selected_equipment = findViewById(R.id.tags_selected_equipment);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                        tags_selected_equipment.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        tags_selected_equipment.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        SkilledTradersAdapter projectsAdapter = new SkilledTradersAdapter(AddEquipment.this,tagsArrayList);
                        tags_selected_equipment.setAdapter(projectsAdapter);
                        projectsAdapter.notifyDataSetChanged();
                    }else {
                        tags_selected_equipment.setVisibility(View.GONE);
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

    private void AddEquipmentApi(String name, String myid,String cusid,String siteid) {

        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "equipments/add";
        Log.d("waggonurl", url);
        JSONObject inputLogin = new JSONObject();

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
            inputLogin.put("name",name);
            inputLogin.put("myId",myid);
            inputLogin.put("idDomain",domainid);
            inputLogin.put("idCustomer",cusid);
            inputLogin.put("idSite",siteid);
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
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("equipment");
                        String equipmentid = first.getString("id");
                        store.putEquipmentId(String.valueOf(equipmentid));
                    }
                    AddEquipment.this.finish();


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

    private void GetCustomerList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"customers/getByDomainId/"+domainid;
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
                        AddEquipment.this.cusid=customerid;
                        commonJobs.setFirstname(contactfirstname);
                        commonJobs.setLastname(contactlastname);
                        commonJobs.setMobilenum(contactmobile);
                        commonJobs.setPhone(contactphone);
                        commonJobs.setAddress(address);
                        commonJobs.setTagname(tagname);
                        commonJobs.setComplementAddress(addressComplement);
                        commonJobs.setEmail(contactEmail);
                        cusDetailsArrayList.add(commonJobs);
                        cusnamestring.add(name);

                    }
                    String[]customer=cusnamestring.stream().toArray(String[]::new);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (AddEquipment.this,android.R.layout.select_dialog_item,customer);
                    equipment_cus_auto_complete= findViewById(R.id.equipment_cus_auto_complete);
                    equipment_cus_auto_complete.setThreshold(1);//will start working from first character
                    equipment_cus_auto_complete.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                    equipment_cus_auto_complete.setTextColor(R.color.text_light);

                    equipment_cus_auto_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String customername=customer[i];
                            bindViews(customername);
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
                return params;
            }


        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void bindViews(String customername) {
        for (int i = 0; i < cusDetailsArrayList.size(); i++) {
            String cname = cusDetailsArrayList.get(i).getCustomername();

        }
    }

    private void GetSitesList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"sites/getByCustomerId/"+cusid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                siteDetailsArrayList.clear();
                sitenamestring.clear();
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
                        AddEquipment.this.siteid=siteid;
                        siteDetailsArrayList.add(commonJobs);
                        sitenamestring.add(sitename);

                    }
                    String[]sites=sitenamestring.stream().toArray(String[]::new);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (AddEquipment.this,android.R.layout.select_dialog_item,sites);
                    equipment_site_auto_complete= findViewById(R.id.equipment_site_auto_complete);
                    equipment_site_auto_complete.setThreshold(1);//will start working from first character
                    equipment_site_auto_complete.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                    equipment_site_auto_complete.setTextColor(R.color.text_light);

                    equipment_site_auto_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String site=sites[i];
                            bindSites(site);
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
                return params;
            }


        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void bindSites(String site) {
        for (int i = 0; i < siteDetailsArrayList.size(); i++) {
            String sname = siteDetailsArrayList.get(i).getSitename();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AddEquipment.this.finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
