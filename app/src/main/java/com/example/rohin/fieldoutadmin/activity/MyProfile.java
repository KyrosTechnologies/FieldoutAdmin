package com.example.rohin.fieldoutadmin.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;

/**
 * Created by Rohin on 18-01-2018.
 */

public class MyProfile extends AppCompatActivity{

    private TextView first_name_profile,last_name_profile,mobile_profile,email_profile,user_name_profile,language_profile;
    private String firstname=null;
    private String lastname=null;
    private String mobile=null;
    private String email=null;
    private String username=null;
    private String language=null;
    private PreferenceManager store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_my_profile);
        first_name_profile=findViewById(R.id.first_name_profile);
        last_name_profile=findViewById(R.id.last_name_profile);
        mobile_profile=findViewById(R.id.mobile_profile);
        email_profile=findViewById(R.id.email_profile);
        user_name_profile=findViewById(R.id.user_name_profile);
        language_profile=findViewById(R.id.language_profile);
        store= PreferenceManager.getInstance(getApplicationContext());
        firstname=store.getFirstName();
        lastname=store.getLastName();
        mobile=store.getPhone();
        email=store.getEmail();
        username=store.getUsername();
        language=store.getLanguage();

        if (firstname!=null){
            first_name_profile.setText(firstname);
        }
        if (lastname!=null){
            last_name_profile.setText(lastname);
        }
        if (mobile!=null){
            mobile_profile.setText(mobile);
        }
        if (email!=null){
            email_profile.setText(email);
        }
        if (username!=null){
            user_name_profile.setText(username);
        }
        if (language!=null){
            language_profile.setText(language);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                MyProfile.this.finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
