package com.official.talaka.Extra.retrofit;

import android.content.SharedPreferences;

import com.official.talaka.Extra.model.FoodData2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface lvl3 {
    SharedPreferences preferences = null;
    @GET("Articles.php?lvl=3")
    Call<List<FoodData2>> getAllData();
}