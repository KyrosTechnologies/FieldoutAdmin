package com.example.rohin.fieldoutadmin.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.rohin.fieldoutadmin.dependencyinjection.ApplicationComponent;
import com.example.rohin.fieldoutadmin.dependencyinjection.DaggerApplicationComponent;

/**
 * Created by Thirunavukkarasu on 28-07-2016.
 */
public class ServiceHandler extends Application {
    private ApplicationComponent applicationComponent;
    private SharedPreferences msharepreference;

    private static final String TAG = ServiceHandler.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static ServiceHandler mInstance;
    //below line from cumulations service handler for retrofit
    //private ApiService mApiservice;


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        applicationComponent= DaggerApplicationComponent.create();
        //below code is cumulations

    }
    public ApplicationComponent getApplicationComponent(){
        return this.applicationComponent;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       MultiDex.install(this);
    }

    public static synchronized ServiceHandler getInstance() {
        return mInstance;
    }
    public static Context getContext(){
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }



    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    //below method is cumulations

}