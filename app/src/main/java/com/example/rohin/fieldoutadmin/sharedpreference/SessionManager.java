package com.example.rohin.fieldoutadmin.sharedpreference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.rohin.fieldoutadmin.activity.MainActivity;

import java.util.HashMap;

/**
 * Created by Thirunavukkarasu on 28-07-2016.
 */
public class SessionManager {
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context _context;
    private final int PRIVATE_MODE=0;
    private static final String PREF_NAME="SessionDetails";
    private static final String IS_LOGIN="IsLoggedIn";
    private static final String IS_FIRST_TIME_LAUNCH="Is First Time Launch";
    private static final String KEY_NUMBER="number";
    private static final String KEY_PASSWORD="password";
    public SessionManager(Context context){
        this._context=context;
        pref=_context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();

    }
    public void createLoginSession(String number, String password){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_NUMBER,number);
        editor.putString(KEY_PASSWORD,password);
        editor.commit();

    }
    public void checkLogin(){

        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            _context.startActivity(i);

        }

    }
    public  boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public HashMap<String,String> getUserDetails(){
        HashMap<String,String> user= new HashMap<String, String>();
        user.put(KEY_NUMBER,pref.getString(KEY_NUMBER,null));
        user.put(KEY_PASSWORD,pref.getString(KEY_PASSWORD,null));

        return user;
    }
    public void logoutUser(){
        editor.remove(KEY_NUMBER);
        editor.remove(KEY_PASSWORD);
        editor.putBoolean(IS_LOGIN,false);
        editor.commit();
        Intent i= new Intent(_context,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);


    }
    public void setIsFirstTimeLaunch(boolean isFirstTime){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH,isFirstTime);
        editor.commit();
    }
    public boolean  isFirstTimeLaunch(){
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }

}


