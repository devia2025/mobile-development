package com.official.tmrnty.Extra.retrofit;

import android.content.SharedPreferences;

import com.official.tmrnty.Extra.model.FoodData2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface2 {
    SharedPreferences preferences = null;
    @GET("Training_List.php")
    Call<List<FoodData2>> getAllData();
}