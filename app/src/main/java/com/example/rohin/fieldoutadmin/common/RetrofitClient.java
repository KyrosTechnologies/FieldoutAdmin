package com.example.rohin.fieldoutadmin.common;

import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
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
}
