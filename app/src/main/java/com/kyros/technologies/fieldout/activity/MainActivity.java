package com.kyros.technologies.fieldout.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.EndURL;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.sharedpreference.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private EditText user_name,pass_word,edit_text_domain;
    private TextView sign_up,forgot_password;
    private PreferenceManager store;
    private String username;
    private String password;
    private SessionManager session;
    private ProgressDialog pDialog;
    private AlertDialog forget_dialog;
    private ProgressDialog progressDialog=null;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        login=findViewById(R.id.login);
        user_name=findViewById(R.id.user_name);
        pass_word=findViewById(R.id.pass_word);
        edit_text_domain=findViewById(R.id.edit_text_domain);
        sign_up=findViewById(R.id.sign_up);
        forgot_password=findViewById(R.id.forgot_password);
        store= PreferenceManager.getInstance(getApplicationContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    username=user_name.getText().toString();
                    password=pass_word.getText().toString();
                    if (username==null && username.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter valid Username!", Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    if(password==null && password.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter valid Password!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(username!=null &&!username.isEmpty()&& password!=null && !password.isEmpty() && edit_text_domain.getText().toString() != null && !edit_text_domain.getText().toString().isEmpty()){
                        LoginApi(username,password,edit_text_domain.getText().toString());
                    }else{
                        Toast.makeText(getApplicationContext(), "Username or Password is incorrect!", Toast.LENGTH_SHORT).show();

                    }

            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openpopup();
            }
        });

    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(MainActivity.this);
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

    private void LoginApi(final String username, String password,String domainName) {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"users/login";
        store= PreferenceManager.getInstance(getApplicationContext());

        Log.d("waggonurl", url);
        showProgressDialog();
        JSONObject inputLogin=new JSONObject();
        try{
            inputLogin.put("password",password);
            inputLogin.put("email",username);
            inputLogin.put("domain",domainName);
            inputLogin.put("idDevice",store.getFCMToken());

        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("inputJsonuser",inputLogin.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();

                Log.d("List TeamsResponse",response.toString());
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    boolean success=obj.getBoolean("isSuccess");
                    if (success) {
                        JSONObject first = obj.getJSONObject("userDetails");
                            String id=first.getString("id");
                            store.putUserid(String.valueOf(id));
                            userid=id;
                            String createdat = first.getString("createdAt");
                            store.putCreatedat(String.valueOf(createdat));
                            String hash = first.getString("hash");
                            store.putHash(String.valueOf(hash));
                            String idDomain=null;
                            try {
                                idDomain=first.getString("idDomain");
                                store.putIdDomain(String.valueOf(idDomain));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            String password = first.getString("password");
                            store.putPassword(String.valueOf(password));
                            String token = first.getString("token");
                            store.putToken(String.valueOf(token));
//                            String username = first.getString("username");
//                            store.putUsername(String.valueOf(username));
                            String profile=first.getString("profile");
                            store.putProfile(String.valueOf(profile));
                            String teams=null;
                            try {
                                teams=first.getString("teams");
                                store.putTeamId(String.valueOf(teams));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            String phone=first.getString("phone");
                            store.putPhone(String.valueOf(phone));
                            String CustomFieldValues =null;
                            try {
                                CustomFieldValues = first.getString("CustomFieldValues");
                                store.putCustomFieldValues(String.valueOf(CustomFieldValues));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            String skilledTrades =null;
                            try {
                                skilledTrades = first.getString("skilledTrades");
                                store.putSkilledTrades(String.valueOf(skilledTrades));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            String firstName = first.getString("firstName");
                            store.putFirstName(String.valueOf(firstName));
                            String lastName = first.getString("lastName");
                            store.putLastName(String.valueOf(lastName));
                            String language =null;
                            try {
                                language = first.getString("language");
                                store.putLanguage(String.valueOf(language));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            String email = first.getString("email");
                            store.putEmail(String.valueOf(email));
                            boolean value= store.putEmail(String.valueOf(email));
                            session.createLoginSession(username,password);
                        }
                        if (success){
                            GetDomainIdList();
                        }else {
                            Toast.makeText(getApplicationContext(), "Username or Password does not match", Toast.LENGTH_SHORT).show();
                        }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error!=null){
                    Log.e("Error",""+error.toString());
                }
                    dismissProgressDialog();

//                texts.setText(error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Content-Type", "Application/json");
                return params;
            }

        };
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }

    private void GetDomainIdList() {
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"domain/getDomainByUserId/"+userid;
        Log.d("waggonurl", url);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("List Response",response.toString());
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    JSONObject first=obj.getJSONObject("domain");
                        String domainidid=first.getString("id");
                        store.putIdDomain(String.valueOf(domainidid));
                        String company=first.getString("company");
                        store.putCompanyName(String.valueOf(company));

                    Intent i = new Intent(MainActivity.this, LandingActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

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

    private void openpopup() {
        if(forget_dialog==null){
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater=getLayoutInflater();
            View view=inflater.inflate(R.layout.forgot_password,null);
            builder.setView(view);
            final EditText email_forget_password=(EditText)view.findViewById(R.id.email_forget_password);
            final EditText edit_text_domain_forget=(EditText)view.findViewById(R.id.edit_text_domain_forget);
            TextView back_forget=(TextView)view.findViewById(R.id.back_forget);
            TextView reset_forget=(TextView)view.findViewById(R.id.reset_forget);
            back_forget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closepopup();
                }
            });
            reset_forget.setOnClickListener(v -> {
                String email=email_forget_password.getText().toString();
                String domain=edit_text_domain_forget.getText().toString();
                if(!email.isEmpty()&&email!=null && domain != null && !domain.isEmpty()){
                    ForgotPasswordApi(email,domain);

                }else{
                }
            });
            forget_dialog=builder.create();
            forget_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            forget_dialog.setCancelable(false);
            forget_dialog.setCanceledOnTouchOutside(false);

        }
        forget_dialog.show();

    }

    private void closepopup(){
        if(forget_dialog!=null&& forget_dialog.isShowing()){
            forget_dialog.dismiss();
        }
    }

    private void showDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading Please wait..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);

        }
        progressDialog.show();
    }
    private void dismissDialog(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    private void ForgotPasswordApi(final String mails,String domain){
        String tag_json_obj = "json_obj_req";
        String url = EndURL.URL+"users/forgotPassword";
        Log.d("waggonurl", url);
        closepopup();
        showDialog();
        JSONObject inputLogin=new JSONObject();
        try{
            inputLogin.put("email",mails);
            inputLogin.put("domain",domain);


        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("inputJsonuser",inputLogin.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, inputLogin, new Response.Listener<JSONObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.d("List TeamsResponse",response.toString());
                try {

                    JSONObject obj=new JSONObject(response.toString());
                    String user=obj.getString("user");

                        Toast.makeText(getApplicationContext(),user,Toast.LENGTH_SHORT).show();


                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog();
                Toast.makeText(getApplicationContext(),"Not Working",Toast.LENGTH_SHORT).show();
//                texts.setText(error.toString());
            }
        }) {

        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20*1000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ServiceHandler.getInstance().addToRequestQueue(objectRequest, tag_json_obj);

    }
}
