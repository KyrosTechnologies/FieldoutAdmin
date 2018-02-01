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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.activity.AddRecurringJobs;
import com.kyros.technologies.fieldout.adapters.RecurringJobsAdapter;
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
 * Created by Rohin on 31-01-2018.
 */

public class FragmentRecurringJobs extends Fragment {

    private View recurring;
    private RecyclerView recurring_recycler;
    private LinearLayout no_recurring_linear,add_recurring_jobs;
    private PreferenceManager store;
    private String domainid = null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recurring = inflater.inflate(R.layout.fragment_recurring_jobs, container, false);
        recurring_recycler = recurring.findViewById(R.id.recurring_recycler);
        no_recurring_linear = recurring.findViewById(R.id.no_recurring_linear);
        add_recurring_jobs=recurring.findViewById(R.id.add_recurring_jobs);
        store = PreferenceManager.getInstance(getActivity().getApplicationContext());
        domainid = store.getIdDomain();

        add_recurring_jobs.setOnClickListener(view-> startActivity(new Intent(getContext(), AddRecurringJobs.class)));

        return recurring;
    }

    private void GetrecurringJobsList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "recurring_jobs/getByDomainId/"+domainid;
        Log.d("waggonurl", url);
        //showProgressDialog();

        JSONObject inputjso = new JSONObject();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, inputjso, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse", response.toString());
                commonJobsArrayList.clear();

                try {

                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray array = obj.getJSONArray("recurring_jobs");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject first = array.getJSONObject(i);
                        String endDate=first.getString("endDate");
                        String startDate=first.getString("startDate");
                        JSONObject userInfo=null;
                        try {
                            userInfo=first.getJSONObject("userInfo");
                        }catch (Exception e){

                        }
                        String created_at=userInfo.getString("created_at");
                        String phone=userInfo.getString("phone");
                        String username=userInfo.getString("username");


                        CommonJobs commonJobs = new CommonJobs();
                        commonJobs.setStartdate(startDate);
                        commonJobs.setEnddate(endDate);
                        commonJobs.setCreatedat(created_at);
                        commonJobs.setPhone(phone);
                        commonJobs.setTechnicianname(username);
                        commonJobsArrayList.add(commonJobs);

                    }
                    if (commonJobsArrayList.size() != 0) {
                        recurring_recycler = recurring.findViewById(R.id.recurring_recycler);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        recurring_recycler.setLayoutManager(layoutManager);
                        //jobs_month_recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        recurring_recycler.setItemAnimator(new DefaultItemAnimator());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        RecurringJobsAdapter recurringJobsAdapter = new RecurringJobsAdapter(getContext(), commonJobsArrayList);
                        recurring_recycler.setAdapter(recurringJobsAdapter);
                        recurringJobsAdapter.notifyDataSetChanged();
                        no_recurring_linear.setVisibility(View.GONE);
                        recurring_recycler.setVisibility(View.VISIBLE);
                    } else {
                        no_recurring_linear.setVisibility(View.VISIBLE);
                        recurring_recycler.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
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
            public Map<String, String> getHeaders() throws AuthFailureError {
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
        GetrecurringJobsList();
    }

}