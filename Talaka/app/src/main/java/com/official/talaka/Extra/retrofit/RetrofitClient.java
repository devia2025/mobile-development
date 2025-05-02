package com.official.talaka.Extra.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.1.100/Talaka/Users/Article/";

    public static Retrofit getRetrofitInstance(){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;

    }

}
