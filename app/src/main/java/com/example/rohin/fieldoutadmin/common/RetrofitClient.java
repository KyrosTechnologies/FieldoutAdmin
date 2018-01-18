package com.example.rohin.fieldoutadmin.common;

import android.util.Log;

import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;

import org.androidannotations.annotations.App;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kyros on 12-12-2017.
 */
@Module
public class RetrofitClient {
    @Provides
    @Singleton
    public POJOInterface pojoInterface(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(EndURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(POJOInterface.class);
    }
    private Cache provideCache(){
        Cache cache=null;
        try{
            cache=new Cache(new File(ServiceHandler.getContext().getCacheDir(),"http-cache"),
                   10 * 1024 * 1024 );//10 MB cache
        }catch (Exception e){
            e.printStackTrace();
            Log.e("OKHTTP","Could not create cache!");
        }
        return cache;
    }
    private Interceptor provideCacheInterceptor(){
        return chain -> {
           Response response=chain.proceed(chain.request());
           //re-write response header to force use of cache
            CacheControl cacheControl=new CacheControl.Builder()
                    .maxAge(2, TimeUnit.MINUTES)
                    .build();
            return response.newBuilder()
                    .header("Cache-Control",cacheControl.toString())
                    .build();
        };
    }
    private Interceptor provideOfflineCacheInterceptor(){
        return chain -> {
            Request request=chain.request();
            //enable below line for offline Cache
            //if(!Network.isOnline()){
                CacheControl cacheControl=new CacheControl.Builder()
                        .maxStale(7,TimeUnit.DAYS)
                        .build();
                request=request.newBuilder()
                        .cacheControl(cacheControl)
                        .removeHeader("Pragma")
                        .build();
          //  }

            return chain.proceed(request);
        };

    }

}
