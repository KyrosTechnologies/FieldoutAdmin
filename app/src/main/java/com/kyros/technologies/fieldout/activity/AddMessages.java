package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
 * Created by Rohin on 19-01-2018.
 */

public class AddMessages extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private PreferenceManager store;
    private String usedid=null;
    private String domainid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    private EditText msg_title_edit,msg_body_edit;
    private LinearLayout msg_priority_linear;
    private TextView msg_priority_text;
    private Spinner msg_tech_spinner;
    private Button save_messages;
    private AlertDialog prioritydialog;
    private String selectedpriority=null;
    private String techniciantext=null;
    private List<String> spinnerlist=new ArrayList<String>();
    private String msgtitle;
    private String msgbody;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_messages);
        msg_title_edit=findViewById(R.id.msg_title_edit);
        msg_body_edit=findViewById(R.id.msg_body_edit);
        msg_priority_linear=findViewById(R.id.msg_priority_linear);
        msg_priority_text=findViewById(R.id.msg_priority_text);
        msg_tech_spinner=findViewById(R.id.msg_tech_spinner);
        save_messages=findViewById(R.id.save_messages);
        store= PreferenceManager.getInstance(getApplicationContext());
        usedid=store.getUserid();
        domainid=store.getIdDomain();
        msg_tech_spinner.setOnItemSelectedListener(this);
        GetTechnicianList();

        msg_priority_linear.setOnClickListener(view -> {
            showPriorityDialog();

        });

        save_messages.setOnClickListener(view -> {
            msgtitle=msg_title_edit.getText().toString();
            if (msgtitle==null && msgtitle.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Message Title!", Toast.LENGTH_SHORT).show();
                return ;
            }
            msgbody=msg_body_edit.getText().toString();
            if (msgbody==null && msgbody.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Message Body!", Toast.LENGTH_SHORT).show();
                return ;
            }
            String technician = null;
            try {
                technician=msg_tech_spinner.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(technician==null){
                Toast.makeText(getApplicationContext(), "Please Select Technician!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(msgtitle!=null &&!msgtitle.isEmpty()&&msgbody!=null &&!msgbody.isEmpty()&& technician!=null){
                AddMessagesApi(msgtitle,msgbody,techniciantext);
            }else{
                Toast.makeText(getApplicationContext(), "Enter All the Required Fields", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void showPriorityDialog(){
        if(prioritydialog==null){
            AlertDialog.Builder builder=new AlertDialog.Builder(AddMessages.this);
            LayoutInflater inflater=getLayoutInflater();
            View view=inflater.inflate(R.layout.priority_spinner_value,null);
            builder.setView(view);
            TextView low_priority=view.findViewById(R.id.low_priority);
            TextView normal_priority=view.findViewById(R.id.normal_priority);
            TextView high_priority=view.findViewById(R.id.high_priority);

            low_priority.setOnClickListener(view1 -> {
                selectedpriority="Low";
                prioritydialog.dismiss();
                if (selectedpriority!=null){
                    msg_priority_text.setText(selectedpriority);
                }
            });

            normal_priority.setOnClickListener(view1 -> {
                selectedpriority="Normal";
                prioritydialog.dismiss();
                if (selectedpriority!=null){
                    msg_priority_text.setText(selectedpriority);
                }
            });

            high_priority.setOnClickListener(view1 -> {
                selectedpriority="High";
                prioritydialog.dismiss();
                if (selectedpriority!=null){
                    msg_priority_text.setText(selectedpriority);
                }
            });
            prioritydialog=builder.create();
            prioritydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            prioritydialog.setCancelable(false);
            prioritydialog.setCanceledOnTouchOutside(false);
        }
        prioritydialog.show();

    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(AddMessages.this);
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

                    ArrayAdapter<String> adapter=new  ArrayAdapter<String>(AddMessages.this,android.R.layout.simple_spinner_item,
                            spinnerlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    msg_tech_spinner.setPrompt("Technician");
                    msg_tech_spinner.setAdapter(adapter);
                    msg_tech_spinner.setAdapter(new SpinnerDetails(adapter,R.layout.technician,AddMessages.this));
                    msg_tech_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                String text = msg_tech_spinner.getSelectedItem().toString();
                                techniciantext=text;
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

    private void AddMessagesApi(String msgtitle, String msgbody,String techniciantext) {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL + "messages/add";
        Log.d("waggonurl", url);
        showProgressDialog();
        JSONObject inputLogin = new JSONObject();

        String technicianid=null;
        for (int i=0;i<commonJobsArrayList.size();i++){
            String techName=commonJobsArrayList.get(i).getTechnicianname();
            if (techniciantext!=null){
                if (techniciantext.equals(techName)) {
                    technicianid=commonJobsArrayList.get(i).getTechnicianid();
                }
            }
        }

        try {
            inputLogin.put("msgTitle",msgtitle);
            inputLogin.put("msgBody", msgbody);
            inputLogin.put("msgPriority",selectedpriority);
            inputLogin.put("idUser",technicianid);
            inputLogin.put("idSender",usedid);

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
                        JSONObject first = obj.getJSONObject("message");
                        String msgid = first.getString("id");
                        store.putMessageId(String.valueOf(msgid));

                    }

                    AddMessages.this.finish();

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AddMessages.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
