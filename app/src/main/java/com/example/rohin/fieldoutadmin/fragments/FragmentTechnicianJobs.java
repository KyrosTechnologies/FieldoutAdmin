package com.example.rohin.fieldoutadmin.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.activity.SpinnerDetails;
import com.example.rohin.fieldoutadmin.adapters.AdapterTechnicianJobs;
import com.example.rohin.fieldoutadmin.common.CommonJobs;
import com.example.rohin.fieldoutadmin.common.EndURL;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rohin on 19-01-2018.
 */

public class FragmentTechnicianJobs extends Fragment implements AdapterView.OnItemSelectedListener{

    private View techjobs;
    private Spinner tech_spinner;
    private RecyclerView technician_jobs_recycler;
    private LinearLayout no_tech_jobs_linear;
    private PreferenceManager store;
    private String domainid=null;
    private String techniciantext=null;
    private List<String> spinnerlist=new ArrayList<String>();
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    ArrayList<CommonJobs>commonJobsList=new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        techjobs=inflater.inflate(R.layout.fragment_technician_jobs_list,container,false);
        technician_jobs_recycler=techjobs.findViewById(R.id.technician_jobs_recycler);
        no_tech_jobs_linear=techjobs.findViewById(R.id.no_tech_jobs_linear);
        tech_spinner=techjobs.findViewById(R.id.tech_spinner);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        domainid=store.getIdDomain();
        tech_spinner.setOnItemSelectedListener(this);
        GetTechnicianList();

        return techjobs;
    }

    private void GetTechnicianList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"users/getTechnicians/"+domainid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("technicians");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String technicianid=first.getString("id");
                        String techusername=first.getString("username");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setTechnicianid(technicianid);
                        commonJobs.setTechnicianname(techusername);
                        commonJobsArrayList.add(commonJobs);
                        spinnerlist.add(techusername);

                    }

                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,
                            spinnerlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    tech_spinner.setPrompt("Technician");
                    tech_spinner.setAdapter(adapter);
                    tech_spinner.setAdapter(new SpinnerDetails(adapter,R.layout.technician,getContext()));
                    tech_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = tech_spinner.getSelectedItem().toString();
                                techniciantext=text;
                                GetReportsTechJobsApi(text);
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
                Toast.makeText(getActivity().getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();

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

    private void GetReportsTechJobsApi(String techniciantext) {
        String technicianid=null;
        for (int i=0;i<commonJobsArrayList.size();i++){
            String techName=commonJobsArrayList.get(i).getTechnicianname();
            if (techniciantext!=null){
                if (techniciantext.equals(techName)) {
                    technicianid=commonJobsArrayList.get(i).getTechnicianid();
                }
            }
        }
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"job_reports/getByTechnicianId/"+technicianid;
        Log.d("waggonurl", url);
        //showProgressDialog();

        JSONObject inputjso=new JSONObject();
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, inputjso, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse", response.toString());
                try {

                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray array = obj.getJSONArray("Reports");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject first = array.getJSONObject(i);
                        String status = first.getString("status");
                        String idJob=first.getString("idJob");
                        JSONObject jobinfo=null;
                        try {
                            jobinfo = first.getJSONObject("jobInfo");
                        }catch (Exception e){

                        }
                        String myId="";
                        try {
                            myId=jobinfo.getString("myId");
                        }catch (Exception e){
                        }
                        String priority="";
                        try {
                            priority=jobinfo.getString("priority");

                        }catch (Exception e){

                        }
                        String contactPhone="";
                        try {
                            contactPhone=jobinfo.getString("contactPhone");
                        }catch (Exception e){
                        }
                        JSONObject jobTypeInfo =null;
                        try {
                            jobTypeInfo = jobinfo.getJSONObject("jobTypeInfo");
                        }catch (Exception e){
                        }
                        String jobtypename="";
                        try {
                            jobtypename=jobTypeInfo.getString("job_type_name");
                        }catch (Exception e){
                        }
                        JSONObject customerInfo =null;
                        try {
                            customerInfo = jobinfo.getJSONObject("customerInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String cusname="";
                        try {
                            cusname=customerInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        JSONObject equipmentInfo =null;
                        try {
                            equipmentInfo = jobinfo.getJSONObject("equipmentInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String equipname="";
                        try {
                            equipname=equipmentInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        JSONObject siteInfo =null;
                        try {
                            siteInfo = jobinfo.getJSONObject("siteInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String sitename="";
                        try {
                            sitename=siteInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs = new CommonJobs();
                        commonJobs.setJobid(idJob);
                        commonJobs.setEquipname(equipname);
                        commonJobs.setCustomername(cusname);
                        commonJobs.setMyId(myId);
                        commonJobs.setJobTypeName(jobtypename);
                        commonJobs.setStatus(status);
                        commonJobs.setSitename(sitename);
                        commonJobs.setPhone(contactPhone);
                        commonJobs.setPriority(priority);
                        commonJobsList.add(commonJobs);

                    }
                    if (commonJobsList.size()!=0){
                        technician_jobs_recycler = techjobs.findViewById(R.id.technician_jobs_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        technician_jobs_recycler.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        technician_jobs_recycler.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        AdapterTechnicianJobs adapterTechnicianJobs = new AdapterTechnicianJobs(getContext(), commonJobsList);
                        technician_jobs_recycler.setAdapter(adapterTechnicianJobs);
                        adapterTechnicianJobs.notifyDataSetChanged();
                        no_tech_jobs_linear.setVisibility(View.GONE);
                        technician_jobs_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_tech_jobs_linear.setVisibility(View.VISIBLE);
                        technician_jobs_recycler.setVisibility(View.GONE);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
