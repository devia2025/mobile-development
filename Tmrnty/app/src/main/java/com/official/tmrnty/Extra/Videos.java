package com.official.tmrnty.Extra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.official.tmrnty.Extra.model.FoodData2;
import com.official.tmrnty.Extra.model.Recommended;
import com.official.tmrnty.Extra.retrofit.ApiInterface;
import com.official.tmrnty.Extra.retrofit.ApiInterface2;
import com.official.tmrnty.Extra.retrofit.RetrofitClient;
import com.official.tmrnty.R;
import com.official.tmrnty.Extra.adapter.RecommendedAdapter2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Videos extends AppCompatActivity {
    RecyclerView recommendedRecyclerView;
    RecommendedAdapter2 recommendedAdapter;
    SharedPreferences preferences;
    ApiInterface2  apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.articles_container));

        setup_menu();
    }

    public void Back(View view) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void setup_menu(){
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface2.class);
        retrofit2.Call<List<FoodData2>> call = apiInterface.getAllData();
        call.enqueue(new Callback<List<FoodData2>>() {
            @Override
            public void onResponse(retrofit2.Call<List<FoodData2>> call, Response<List<FoodData2>> response) {
                if(response.isSuccessful()){
                    List<FoodData2> foodDataList = response.body();
                    getRecommendedData1(foodDataList.get(0).getAllmenu1());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_articles);
                            progressBar.setVisibility(View.GONE);

                            NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nested);
                            nestedScrollView.setVisibility(View.VISIBLE);

                            YoYo.with(Techniques.BounceInLeft).duration(2000).playOn(findViewById(R.id.nested));
                            YoYo.with(Techniques.BounceInRight).duration(2000).playOn(findViewById(R.id.back));


                            TextView textView = findViewById(R.id.total_articles);
                            textView.setText( "Total Videos : " + Integer.toString(recommendedRecyclerView.getAdapter().getItemCount()));
                        }
                    }, 1500);   //5 seconds
                }else{
                    Toast.makeText(Videos.this, "Error Data responding.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<FoodData2>> call, Throwable t) {
                Toast.makeText(Videos.this, "Server is not responding.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void  getRecommendedData1(List<Recommended> recommendedList){
        recommendedRecyclerView = findViewById(R.id.menu_1);
        recommendedAdapter = new RecommendedAdapter2(this, recommendedList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recommendedRecyclerView.setLayoutManager(layoutManager);
        recommendedRecyclerView.setAdapter(recommendedAdapter);
    }
}