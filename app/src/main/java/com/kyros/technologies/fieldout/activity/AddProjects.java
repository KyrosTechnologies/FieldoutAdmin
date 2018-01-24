package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.SkilledTradersAdapter;
import com.kyros.technologies.fieldout.common.CommonJobs;
import com.kyros.technologies.fieldout.common.EndURL;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rohin on 05-01-2018.
 */

public class AddProjects extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private PreferenceManager store;
    private List<String> activitylist=new ArrayList<String>();
    private Spinner project_type_spinner,site_auto_complete,equipment_auto_complete;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> activityJobsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> cusDetailsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> siteDetailsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs> equipDetailsArrayList = new ArrayList<CommonJobs>();
    ArrayList<String>tagsArrayList=new ArrayList<String>();
    private List<String> cusnamestring=new ArrayList<String>();
    private List<String> sitenamestring=new ArrayList<String>();
    private List<String> equipnamestring=new ArrayList<String>();
    private String equipname=null;
    private String techid=null;
    private String domainid=null;
    private Button save_projects;
    private AutoCompleteTextView customer_auto_complete;
    private EditText project_number_edit,description_edit;
    private TextView tags_add_projects_text;
    private RecyclerView tags_selected_jobs;
    private String projecttypetext=null;
    private String siteid=null;
    private String equipmentid=null;
    private String customerid=null;
    private AlertDialog tagsDialog=null;
    private SkilledTradersAdapter adapter;
    private SkilledTradersAdapter selectedAdapter;
    private List<String>tagsList=new ArrayList<>();
    private String projectnumber=null;
    private String description=null;
    private String customer;
    private String site;
    private String equipment;
    private  JSONArray tagarray=null;
    private String customertext=null;
    private String sitetext=null;
    private String equiptext=null;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_projects);
        store= PreferenceManager.getInstance(getApplicationContext());
        domainid=store.getIdDomain();
        project_type_spinner=findViewById(R.id.project_type_spinner);
        save_projects=findViewById(R.id.save_projects);
        customer_auto_complete=findViewById(R.id.customer_auto_complete);
        site_auto_complete=findViewById(R.id.site_auto_complete);
        equipment_auto_complete=findViewById(R.id.equipment_auto_complete);
        tags_add_projects_text=findViewById(R.id.tags_add_projects_text);
        tags_selected_jobs=findViewById(R.id.tags_selected_jobs);
        project_number_edit=findViewById(R.id.project_number_edit);
        description_edit=findViewById(R.id.description_edit);
        project_type_spinner.setOnItemSelectedListener(this);
        GetProjectTypeList();
        GetCustomerList();
        GetTagsList();

        tags_add_projects_text.setOnClickListener(view -> skilledTradesDialog(new ArrayList<>(),tags_add_projects_text));

        save_projects.setOnClickListener(view -> {
            customer=customer_auto_complete.getText() .toString();
            if (customer==null && customer.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Customer Name!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String sitename = null;
            try {
                sitename=site_auto_complete.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(sitename==null){
                Toast.makeText(getApplicationContext(), "Please Select Site Name!", Toast.LENGTH_SHORT).show();
                return;
            }
            String equipmentname = null;
            try {
                equipmentname=equipment_auto_complete.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(equipmentname==null){
                Toast.makeText(getApplicationContext(), "Please Select Equipment Name!", Toast.LENGTH_SHORT).show();
                return;
            }
            projectnumber=project_number_edit.getText().toString();
            if (projectnumber==null && projectnumber.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Project Number!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String projecttypespinner = null;
            try {
                projecttypespinner=project_type_spinner.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(projecttypespinner==null){
                Toast.makeText(getApplicationContext(), "Please Select Technician!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(customer!=null &&!customer.isEmpty()&&sitename!=null&&equipmentname!=null
                    &&projectnumber!=null &&!projectnumber.isEmpty()&&projecttypespinner!=null){
                description=description_edit.getText().toString();
                AddProjectsApi(projecttypetext,projectnumber,description);
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();

            }

        });

    }

    private void skilledTradesDialog(List<String> skilledName,TextView tView) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AddProjects.this);
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
                tags_selected_jobs.setVisibility(View.VISIBLE);
                selectedAdapter=new SkilledTradersAdapter(this,tagsList);
                tags_selected_jobs.setItemAnimator(new DefaultItemAnimator());
                tags_selected_jobs.setLayoutManager(new LinearLayoutManager(this));
                tags_selected_jobs.setAdapter(selectedAdapter);
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

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(AddProjects.this);
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

    private void AddProjectsApi(String projecttypenametext, String projectno, String desc) {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "projects/add";
        Log.d("waggonurl", url);
        showProgressDialog();
        JSONObject inputLogin = new JSONObject();

        String projecttypeid=null;
        for (int i=0;i<activityJobsArrayList.size();i++){
            String projectTypeName=activityJobsArrayList.get(i).getProjecttypename();
            if (projecttypenametext!=null){
                if (projecttypenametext.equals(projectTypeName)) {
                    projecttypeid=activityJobsArrayList.get(i).getProjecttypeid();
                }
            }

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
            inputLogin.put("idCustomer", customerid);
            inputLogin.put("idSite",siteid);
            inputLogin.put("idDomain",domainid);
            inputLogin.put("idEquipment",equipmentid);
            inputLogin.put("tags",jsonArray);
            inputLogin.put("custom_project_no",projectno);
            inputLogin.put("description",desc);
            inputLogin.put("idProjectType", projecttypeid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("inputJsonuser", inputLogin.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.d("List Response", response.toString());

                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("project");
                        String projectid=first.getString("id");
                        store.putProjectId(String.valueOf(projectid));

                        AddProjects.this.finish();
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

    private void GetProjectTypeList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"project_types/getByDomainId/"+domainid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("project_types");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String projecttypeid=first.getString("id");
                        store.putProjectTypeId(String.valueOf(projecttypeid));
                        String projecttypename=first.getString("type_name");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setProjecttypeid(projecttypeid);
                        commonJobs.setProjecttypename(projecttypename);
                        activityJobsArrayList.add(commonJobs);
                        activitylist.add(projecttypename);

                    }

                    for (String s:activitylist) {


                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(AddProjects.this,android.R.layout.simple_spinner_item,
                            activitylist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    project_type_spinner.setPrompt("Project Type");
                    project_type_spinner.setAdapter(adapter);
                    project_type_spinner.setAdapter(new SpinnerDetails(adapter,R.layout.project_type_spinner,AddProjects.this));
                    project_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = project_type_spinner.getSelectedItem().toString();
                                projecttypetext=text;

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
                        AddProjects.this.customerid=customerid;
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
                            (AddProjects.this,android.R.layout.select_dialog_item,customer);
                    customer_auto_complete= findViewById(R.id.customer_auto_complete);
                    customer_auto_complete.setThreshold(1);//will start working from first character
                    customer_auto_complete.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                    customer_auto_complete.setTextColor(R.color.text_light);

                    customer_auto_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String customername=customer[i];
                            bindViews(customername);
                            customertext=customername;
                            GetSiteList(customertext);
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

    private void GetSiteList(String customername) {
        String customid="";
        for (int i=0;i<cusDetailsArrayList.size();i++){
            if (customername.equals(cusDetailsArrayList.get(i).getCustomername())){
                customid=cusDetailsArrayList.get(i).getCustomerid();
            }
        }
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"sites/getByCustomerId/"+customid;
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
                        AddProjects.this.siteid=siteid;
                        siteDetailsArrayList.add(commonJobs);
                        sitenamestring.add(sitename);

                    }

                    for (String s:sitenamestring) {

                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(AddProjects.this,android.R.layout.simple_spinner_item,
                            sitenamestring);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    site_auto_complete.setPrompt("Site");
                    site_auto_complete.setAdapter(adapter);
                    site_auto_complete.setAdapter(new SpinnerDetails(adapter,R.layout.site_spinner,AddProjects.this));
                    site_auto_complete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = site_auto_complete.getSelectedItem().toString();
                                sitetext=text;
                                GetEquipmentList(sitetext);
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
                return params;
            }


        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void GetEquipmentList(String sitename) {

        String siteid="";
        for (int i=0;i<siteDetailsArrayList.size();i++){
            if (sitename.equals(siteDetailsArrayList.get(i).getSitename())){
                siteid=siteDetailsArrayList.get(i).getSiteid();
            }
        }
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"equipments/getBySiteId/"+siteid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                equipDetailsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("equipments");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String equipmentid=first.getString("id");
                        store.putEquipmentId(String.valueOf(equipmentid));
                        String equipname="";
                        try {
                            equipname=first.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setEquipid(equipmentid);
                        AddProjects.this.equipmentid=equipmentid;
                        commonJobs.setEquipname(equipname);
                        equipDetailsArrayList.add(commonJobs);
                        equipnamestring.add(equipname);

                    }

                    for (String s:equipnamestring) {

                    }
                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(AddProjects.this,android.R.layout.simple_spinner_item,
                            equipnamestring);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    equipment_auto_complete.setPrompt("Equipment");
                    equipment_auto_complete.setAdapter(adapter);
                    equipment_auto_complete.setAdapter(new SpinnerDetails(adapter,R.layout.equipment_spinner,AddProjects.this));
                    equipment_auto_complete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = equipment_auto_complete.getSelectedItem().toString();
                                equiptext=text;
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
                return params;
            }


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
                        tags_selected_jobs.setVisibility(View.VISIBLE);
                        tags_selected_jobs = findViewById(R.id.tags_selected_jobs);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                        tags_selected_jobs.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        tags_selected_jobs.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        SkilledTradersAdapter projectsAdapter = new SkilledTradersAdapter(AddProjects.this,tagsArrayList);
                        tags_selected_jobs.setAdapter(projectsAdapter);
                        projectsAdapter.notifyDataSetChanged();
                    }else {
                        tags_selected_jobs.setVisibility(View.GONE);
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

    private void bindViews(String customername) {
        for (int i=0;i<cusDetailsArrayList.size();i++){
            String cname=cusDetailsArrayList.get(i).getCustomername();
//            if (cname.equals(customername)){
//
//            }
        }
    }

    private void bindSites(String sitename) {
        for (int i=0;i<siteDetailsArrayList.size();i++){
            String sname=siteDetailsArrayList.get(i).getSitename();
//            if (sname.equals(sitename)){
//                sitename=siteDetailsArrayList.get(i).getSitename();
//            }
        }
    }

    private void bindEquipments(String equipname) {
        for (int i=0;i<equipDetailsArrayList.size();i++){
            String ename=equipDetailsArrayList.get(i).getEquipname();
        }
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AddProjects.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
