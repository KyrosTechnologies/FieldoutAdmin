package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.MessagesAdapter;
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
 * Created by Rohin on 19-01-2018.
 */

public class MessageList extends AppCompatActivity {

    private PreferenceManager store;
    private String usedid=null;
    ArrayList<CommonJobs> commonJobsArrayList = new ArrayList<CommonJobs>();
    private RecyclerView messages_recycler;
    private LinearLayout no_messages_linear,add_messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_messages_list);
        messages_recycler=findViewById(R.id.messages_recycler);
        no_messages_linear=findViewById(R.id.no_messages_linear);
        add_messages=findViewById(R.id.add_messages);
        store= PreferenceManager.getInstance(getApplicationContext());
        usedid=store.getUserid();

        add_messages.setOnClickListener(view -> {
            Intent i=new Intent(MessageList.this,AddMessages.class);
            startActivity(i);
        });

    }

    private void GetMessagesList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"messages/getByUserId/"+usedid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                commonJobsArrayList.clear();
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONArray array=obj.getJSONArray("messages");
                    for (int i=0;i<array.length();i++){
                        JSONObject first=array.getJSONObject(i);
                        String messageid=first.getString("id");
                        store.putMessageId(String.valueOf(messageid));
                        String msgBody=first.getString("msgBody");
                        String msgTitle=first.getString("msgTitle");
                        int isDeleted=first.getInt("isDeleted");
                        int isRead=first.getInt("isRead");
                        int isSent=first.getInt("isSent");

                        CommonJobs commonJobs=new CommonJobs();
                        commonJobs.setMessageid(messageid);
                        commonJobs.setMessagebody(msgBody);
                        commonJobs.setMessagetitle(msgTitle);
                        commonJobs.setIssent(isSent);
                        commonJobs.setIsread(isRead);
                        commonJobs.setIsdelete(isDeleted);
                        commonJobsArrayList.add(commonJobs);

                    }

                    if (commonJobsArrayList.size()!=0){
                        messages_recycler=findViewById(R.id.messages_recycler);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                        messages_recycler.setLayoutManager(layoutManager);
                        //activity_recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                        messages_recycler.setItemAnimator(new DefaultItemAnimator());
                        MessagesAdapter messagesAdapter=new MessagesAdapter(MessageList.this,commonJobsArrayList);
                        messages_recycler.setAdapter(messagesAdapter);
                        messagesAdapter.notifyDataSetChanged();
                        no_messages_linear.setVisibility(View.GONE);
                        messages_recycler.setVisibility(View.VISIBLE);
                    }else {
                        no_messages_linear.setVisibility(View.VISIBLE);
                        messages_recycler.setVisibility(View.GONE);
                    }

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
                params.put("idDomain",store.getIdDomain());
                return params;
            }


        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    @Override
    public void onResume() {
        super.onResume();
        GetMessagesList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                MessageList.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
