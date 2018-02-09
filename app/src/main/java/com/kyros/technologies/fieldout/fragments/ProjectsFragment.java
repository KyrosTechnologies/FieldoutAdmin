package com.kyros.technologies.fieldout.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
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
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.activity.AddProjects;
import com.kyros.technologies.fieldout.adapters.ProjectsAdapter;
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
 * Created by Rohin on 05-01-2018.
 */

public class ProjectsFragment extends Fragment {

    private View projects;
    private RecyclerView projects_recycler;
    private LinearLayout no_projects_linear,add_projects;
    private PreferenceManager store;
    private String domainid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        projects=inflater.inflate(R.layout.fragment_projects,container,false);
        projects_recycler=projects.findViewById(R.id.projects_recycler);
        no_projects_linear=projects.findViewById(R.id.no_projects_linear);
        add_projects=projects.findViewById(R.id.add_projects);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        domainid=store.getIdDomain();

        add_projects.setOnClickListener(view-> startActivity(new Intent(getContext(), AddProjects.class)));

        return projects;
    }

    private void GetQuotationsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"projects/getAll";
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
                    JSONArray array=obj.getJSONArray("projects");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String projectid=first.getString("id");
                        store.putProjectId(String.valueOf(projectid));
                        String custom_project_no="";
                        try {
                            custom_project_no=first.getString("custom_project_no");
                        }catch (Exception e){
                        }
                        String description="";
                        try {
                            description=first.getString("description");
                        }catch (Exception e){
                        }
                        JSONObject customerInfo=null;
                        try {
                            customerInfo = first.getJSONObject("customerInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String cusname=null;
                        try {
                            cusname=customerInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        JSONObject equipmentInfo=null;
                        try {
                            equipmentInfo=first.getJSONObject("equipmentInfo");
                        }catch (Exception e){
                        }
                        String equipname="";
                        try {
                            equipname=equipmentInfo.getString("name");
                        }catch (Exception e){
                        }
                        JSONObject siteInfo=null;
                        try {
                            siteInfo=first.getJSONObject("siteInfo");
                        }catch (Exception e){
                        }
                        String sitename="";
                        try {
                            sitename=siteInfo.getString("name");
                        }catch (Exception e){
                        }
                        JSONObject projectTypeInfo=null;
                        try {
                            projectTypeInfo=first.getJSONObject("projectTypeInfo");
                        }catch (Exception e){
                        }
                        String type_name="";
                        try {
                            type_name=projectTypeInfo.getString("type_name");
                        }catch (Exception e){
                        }
                        JSONArray tagInfo=null;
                        try {
                            tagInfo=first.getJSONArray("tagInfo");
                        }catch (Exception e){
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setProjectid(projectid);
                        commonJobs.setCustomername(cusname);
                        commonJobs.setProjectno(custom_project_no);
                        commonJobs.setEquipname(equipname);
                        commonJobs.setSitename(sitename);
                        commonJobs.setDescription(description);
                        commonJobs.setTaginfo(tagInfo);
                        commonJobs.setProjecttypename(type_name);
                        commonJobsArrayList.add(commonJobs);

                    }
                    if (commonJobsArrayList.size()!=0){
                        projects_recycler = projects.findViewById(R.id.projects_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        projects_recycler.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        projects_recycler.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        ProjectsAdapter projectsAdapter = new ProjectsAdapter(getContext(), commonJobsArrayList);
                        projects_recycler.setAdapter(projectsAdapter);
                        projectsAdapter.notifyDataSetChanged();
                        no_projects_linear.setVisibility(View.GONE);
                        projects_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_projects_linear.setVisibility(View.VISIBLE);
                        projects_recycler.setVisibility(View.GONE);
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
                params.put("idDomain",store.getIdDomain());
                params.put("Authorization", store.getToken());
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
    public void onResume() {
        super.onResume();
        GetQuotationsList();
    }
}