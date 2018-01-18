package com.example.rohin.fieldoutadmin.fragments;

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
import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.activity.ActivityAdd;
import com.example.rohin.fieldoutadmin.adapters.ActivitiesAdapter;
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
 * Created by Rohin on 13-12-2017.
 */

public class ActivitiesFragment extends Fragment {

    private View activities;
    private RecyclerView activity_recycler;
    private LinearLayout no_activity_linear,add_activities;
    private PreferenceManager store;
    private String userid=null;
    private String dtformat=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activities=inflater.inflate(R.layout.fragment_activities,container,false);
        activity_recycler=activities.findViewById(R.id.activity_recycler);
        no_activity_linear=activities.findViewById(R.id.no_activity_linear);
        add_activities=activities.findViewById(R.id.add_activities);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        userid=store.getUserid();

        add_activities.setOnClickListener(view-> startActivity(new Intent(getContext(), ActivityAdd.class)));

        return activities;
    }

    private void GetActivitiesList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"activities/getByUserId/"+userid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                commonJobsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("Activity");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String dtend=first.getString("dtEnd");
                        String acticitiesid=first.getString("id");
                        String dtstart=first.getString("dtStart");
                        String iduser=first.getString("idUser");
                        String nmactivity=first.getString("nmActivity");
                        String noteactivity=first.getString("noteActivity");
                        JSONObject AvtivityTypeInfo=null;
                        try {
                            AvtivityTypeInfo=first.getJSONObject("AvtivityTypeInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String activitytypename="";
                        try {
                            activitytypename=AvtivityTypeInfo.getString("name");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        JSONObject userInfo=null;
                        try {
                            userInfo=first.getJSONObject("userInfo");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String username="";
                        try {
                            username=userInfo.getString("username");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setActivitiesid(acticitiesid);
                        commonJobs.setDtend(dtend);
                        commonJobs.setDtstart(dtstart);
                        commonJobs.setNoteactivity(noteactivity);
                        commonJobs.setActivitytypename(activitytypename);
                        commonJobs.setNmactivity(nmactivity);
                        commonJobs.setTechnicianname(username);
                        commonJobsArrayList.add(commonJobs);

                    }


                    if (commonJobsArrayList.size()!=0){
                        activity_recycler=activities.findViewById(R.id.activity_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        activity_recycler.setLayoutManager(layoutManager);
                        //activity_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        activity_recycler.setItemAnimator(new DefaultItemAnimator());
                        ActivitiesAdapter adapter1=new ActivitiesAdapter(getContext(), commonJobsArrayList);
                        activity_recycler.setAdapter(adapter1);
                        adapter1.notifyDataSetChanged();
                        no_activity_linear.setVisibility(View.GONE);
                        activity_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_activity_linear.setVisibility(View.VISIBLE);
                        activity_recycler.setVisibility(View.GONE);
                    }

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

    @Override
    public void onResume() {
        super.onResume();
        GetActivitiesList();
    }
}
