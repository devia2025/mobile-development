package com.official.talaka.Extra.retrofit;

import com.official.talaka.Extra.model.FoodData;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("Articles.php")
    Call<List<FoodData>> getAllData();

    // lets make our model class of json data.

}
