package com.official.tmrnty.Extra.retrofit;
import com.official.tmrnty.Extra.model.FoodData;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("Training_List.php")
    Call<List<FoodData>> getAllData();

    // lets make our model class of json data.

}
