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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.activity.ResourcesAddActivity;
import com.kyros.technologies.fieldout.adapters.ResourcesAdapter;
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
 * Created by Rohin on 13-12-2017.
 */

public class ResourcesFragment extends Fragment {

    private View resources;
    private RecyclerView resource_recycler;
    private LinearLayout no_resource_linear,add_resources;
    private PreferenceManager store;
    private String domainid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resources=inflater.inflate(R.layout.fragment_resources,container,false);
        resource_recycler=resources.findViewById(R.id.resource_recycler);
        no_resource_linear=resources.findViewById(R.id.no_resource_linear);
        add_resources=resources.findViewById(R.id.add_resources);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        domainid=store.getIdDomain();

        add_resources.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), ResourcesAddActivity.class);
            startActivity(i);
        });

        return resources;
    }

    private void GetResourcesList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"resources/getByDomainId/"+domainid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                commonJobsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("resources");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String dtend=first.getString("dtEnd");
                        String dtstart=first.getString("dtStart");
                        String idToolsAndResurces=first.getString("idToolsAndResurces");
                        String id=first.getString("id");
                        store.putResourceId(String.valueOf(id));
                        String dtreturned=null;
//                        try {
//                            dtreturned=first.getString("dtReturned");
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
                        String noteactivity="";
                        try {
                            noteactivity=first.getString("note");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        JSONObject userInfo=null;
                        try {
                            userInfo = first.getJSONObject("userInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        String username="";
                        try {
                            username=userInfo.getString("username");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String createdat="";
                        try {
                            createdat=userInfo.getString("created_at");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        JSONObject toolsAndResourcesInfo=null;
                        try {
                            toolsAndResourcesInfo=first.getJSONObject("toolsAndResourcesInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String resource_name="";
                        try {
                            resource_name=toolsAndResourcesInfo.getString("resource_name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setDtend(dtend);
                        commonJobs.setDtstart(dtstart);
                        commonJobs.setResourceid(id);
                        commonJobs.setIdtoolsresources(idToolsAndResurces);
                        commonJobs.setNoteactivity(noteactivity);
                        //commonJobs.setDtreturned(dtreturned);
                        commonJobs.setResourcename(resource_name);
                        commonJobs.setResourceusername(username);
                        commonJobs.setCreatedat(createdat);
                        commonJobsArrayList.add(commonJobs);

                    }

                    if (commonJobsArrayList.size()!=0){
                        resource_recycler=resources.findViewById(R.id.resource_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        resource_recycler.setLayoutManager(layoutManager);
                        //resource_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        resource_recycler.setItemAnimator(new DefaultItemAnimator());
                        ResourcesAdapter resourcesAdapter=new ResourcesAdapter(getContext(), commonJobsArrayList);
                        resource_recycler.setAdapter(resourcesAdapter);
                        resourcesAdapter.notifyDataSetChanged();
                        no_resource_linear.setVisibility(View.GONE);
                        resource_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_resource_linear.setVisibility(View.VISIBLE);
                        resource_recycler.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume() {
        super.onResume();
        GetResourcesList();
    }
}
