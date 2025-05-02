package com.official.talaka.Activity;

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
import com.official.talaka.Extra.adapter.RecommendedAdapter2;
import com.official.talaka.Extra.model.FoodData2;
import com.official.talaka.Extra.model.Recommended;
import com.official.talaka.Extra.retrofit.lvl1;
import com.official.talaka.Extra.retrofit.RetrofitClient;
import com.official.talaka.Extra.retrofit.lvl2;
import com.official.talaka.Extra.retrofit.lvl3;
import com.official.talaka.Extra.retrofit.lvl4;
import com.official.talaka.Extra.retrofit.lvl5;
import com.official.talaka.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Articles extends AppCompatActivity {
    lvl1 lvl1;
    lvl2 lvl2;
    lvl3 lvl3;
    lvl4 lvl4;
    lvl5 lvl5;

    RecyclerView recommendedRecyclerView;
    RecommendedAdapter2 recommendedAdapter;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.articles_container));

        switch(preferences.getString("Lvl", "")) {
            case "1":
                setup_menu1();
                break;
            case "2":
                setup_menu2();
                break;
            case "3":
                setup_menu3();
                break;
            case "4":
                setup_menu4();
                break;
            case "5":
                setup_menu5();
                break;
            default:
                Toast.makeText(Articles.this, "Error Data responding.", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void Back(View view) {
        Intent intent = new Intent(this, Home.class);
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

    private void setup_menu1(){
        lvl1 = RetrofitClient.getRetrofitInstance().create(lvl1.class);
        retrofit2.Call<List<FoodData2>> call = lvl1.getAllData();
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
                            textView.setText( "Total Articles : " + Integer.toString(recommendedRecyclerView.getAdapter().getItemCount()));
                        }
                    }, 1500);   //5 seconds
                }else{
                    Toast.makeText(Articles.this, "Error Data responding.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<FoodData2>> call, Throwable t) {
                Toast.makeText(Articles.this, "Server is not responding.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setup_menu2(){
        lvl2 = RetrofitClient.getRetrofitInstance().create(lvl2.class);
        retrofit2.Call<List<FoodData2>> call = lvl2.getAllData();
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
                            textView.setText( "Total Article : " + Integer.toString(recommendedRecyclerView.getAdapter().getItemCount()));
                        }
                    }, 1500);   //5 seconds
                }else{
                    Toast.makeText(Articles.this, "Error Data responding.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<FoodData2>> call, Throwable t) {
                Toast.makeText(Articles.this, "Server is not responding.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setup_menu3(){
        lvl3 = RetrofitClient.getRetrofitInstance().create(lvl3.class);
        retrofit2.Call<List<FoodData2>> call = lvl3.getAllData();
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
                            textView.setText( "Total Article : " + Integer.toString(recommendedRecyclerView.getAdapter().getItemCount()));
                        }
                    }, 1500);   //5 seconds
                }else{
                    Toast.makeText(Articles.this, "Error Data responding.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<FoodData2>> call, Throwable t) {
                Toast.makeText(Articles.this, "Server is not responding.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setup_menu4(){
        lvl4 = RetrofitClient.getRetrofitInstance().create(lvl4.class);
        retrofit2.Call<List<FoodData2>> call = lvl4.getAllData();
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
                            textView.setText( "Total Article : " + Integer.toString(recommendedRecyclerView.getAdapter().getItemCount()));
                        }
                    }, 1500);   //5 seconds
                }else{
                    Toast.makeText(Articles.this, "Error Data responding.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<FoodData2>> call, Throwable t) {
                Toast.makeText(Articles.this, "Server is not responding.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setup_menu5(){
        lvl5 = RetrofitClient.getRetrofitInstance().create(lvl5.class);
        retrofit2.Call<List<FoodData2>> call = lvl5.getAllData();
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
                            textView.setText( "Total Article : " + Integer.toString(recommendedRecyclerView.getAdapter().getItemCount()));
                        }
                    }, 1500);   //5 seconds
                }else{
                    Toast.makeText(Articles.this, "Error Data responding.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<FoodData2>> call, Throwable t) {
                Toast.makeText(Articles.this, "Server is not responding.", Toast.LENGTH_SHORT).show();
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