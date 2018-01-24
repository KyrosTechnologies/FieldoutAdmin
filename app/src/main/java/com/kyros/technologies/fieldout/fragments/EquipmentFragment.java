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
import com.kyros.technologies.fieldout.activity.AddEquipment;
import com.kyros.technologies.fieldout.adapters.EquipmentAdapter;
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

public class EquipmentFragment extends Fragment {

    private View equipment;
    private RecyclerView equipment_recycler;
    private LinearLayout no_equipment_linear,add_equipment;
    private PreferenceManager store;
    private String domainid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        equipment=inflater.inflate(R.layout.fragment_equipment,container,false);
        equipment_recycler=equipment.findViewById(R.id.equipment_recycler);
        no_equipment_linear=equipment.findViewById(R.id.no_equipment_linear);
        add_equipment=equipment.findViewById(R.id.add_equipment);
        store= PreferenceManager.getInstance(getActivity().getApplicationContext());
        domainid=store.getIdDomain();

        add_equipment.setOnClickListener(view-> startActivity(new Intent(getContext(), AddEquipment.class)));

        return equipment;
    }

    private void GetEquipmentList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"equipments/getByDomainId/"+domainid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List TeamsResponse",response.toString());
                commonJobsArrayList.clear();
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
                        }
                        String myId="";
                        try {
                            myId=first.getString("myId");
                        }catch (Exception e){

                        }
                        JSONObject customerInfo=null;
                        try {
                            customerInfo=first.getJSONObject("customerInfo");
                        }catch (Exception e){
                        }
                        String cusname="";
                        try {
                            cusname=customerInfo.getString("name");
                        }catch (Exception e){
                        }
                        String cusid="";
                        try {
                            cusid=customerInfo.getString("id");
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
                        String siteid="";
                        try {
                            siteid=siteInfo.getString("id");
                        }catch (Exception e){

                        }
                        JSONArray positions=null;
                        String latlng=null;
                        try {
                            StringBuilder stringBuilder=new StringBuilder();
                            positions=first.getJSONArray("positions");
                            for (int j=0;j<positions.length();j++){
                                Double pos=positions.getDouble(j);

                                if (j==positions.length()){
                                    stringBuilder.append(pos);
                                }else {
                                    stringBuilder.append(pos+"  ");
                                }
                            }
                            latlng=stringBuilder.toString();
                        }catch (Exception e){

                        }

                        JSONArray tagInfo=null;
                        try {
                            tagInfo=first.getJSONArray("tagInfo");
                        }catch (Exception e){
                        }
                        JSONArray customFields=null;
                        try{
                            customFields=first.getJSONArray("CustomFieldValues");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setCustomername(cusname);
                        commonJobs.setCustomerid(cusid);
                        commonJobs.setMyId(myId);
                        commonJobs.setTaginfo(tagInfo);
                        commonJobs.setSitename(sitename);
                        commonJobs.setSiteid(siteid);
                        commonJobs.setEquipname(equipname);
                        commonJobs.setEquipid(equipmentid);
                        commonJobs.setLatlng(latlng);
                        if(customFields!=null){
                            commonJobs.setCustomFields(customFields.toString());
                        }
                        commonJobsArrayList.add(commonJobs);

                    }

                    if (commonJobsArrayList.size()!=0){
                        equipment_recycler=equipment.findViewById(R.id.equipment_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                        equipment_recycler.setLayoutManager(layoutManager);
                        //equipment_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        equipment_recycler.setItemAnimator(new DefaultItemAnimator());
                        EquipmentAdapter equipmentAdapter=new EquipmentAdapter(getContext(), commonJobsArrayList);
                        equipment_recycler.setAdapter(equipmentAdapter);
                        equipmentAdapter.notifyDataSetChanged();
                        no_equipment_linear.setVisibility(View.GONE);
                        equipment_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_equipment_linear.setVisibility(View.VISIBLE);
                        equipment_recycler.setVisibility(View.GONE);
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
        GetEquipmentList();
    }
}