package com.task.taskshopping.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by KATHIR on 07-06-2020
 */
public class APIclient {
    public static final String USER_URL = "https://www.mocky.io";
    public static final String BASE_URL = "https://www.mocky.io/v2/5def7b172f000063008e0aa2";
    private static Retrofit retrofit = null;

    static Retrofit getclient(){


        retrofit=new Retrofit.Builder()
                .baseUrl(USER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;

    }
    public static Apiinterface getapi(){
        return getclient().create(Apiinterface.class);
    }
}
