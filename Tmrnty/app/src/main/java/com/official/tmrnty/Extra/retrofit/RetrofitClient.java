package com.official.tmrnty.Extra.retrofit;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.official.tmrnty.Splash;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    SharedPreferences preferences;

    private static Retrofit retrofit;
    private static final String BASE_URL = getName().toString() +"Tmrnty/Training/";

    public static Retrofit getRetrofitInstance(){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;

    }
    public static String getName() {
        return Splash.preferences.getString("IP","");
    }
}
