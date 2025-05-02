package com.official.tmrnty.Extra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;
import com.official.tmrnty.Extra.adapter.RecommendedAdapter;
import com.official.tmrnty.Extra.model.FoodData;
import com.official.tmrnty.Extra.model.Recommended;
import com.official.tmrnty.Extra.retrofit.ApiInterface;
import com.official.tmrnty.Extra.retrofit.RetrofitClient;
import com.official.tmrnty.R;
import com.official.tmrnty.Splash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity {
    SharedPreferences preferences;
    RequestQueue requestQueue;
    String IP = Splash.preferences.getString("IP","");

    ApiInterface apiInterface;
    RecyclerView recommendedRecyclerView;
    RecommendedAdapter recommendedAdapter;
    Boolean start = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar progressBar = findViewById(R.id.progressBar_Dashboard);
        progressBar.setVisibility(View.VISIBLE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        requestQueue = Volley.newRequestQueue(this);

        YoYo.with(Techniques.FadeIn).duration(1000).playOn(findViewById(R.id.dashboard_container));
        YoYo.with(Techniques.BounceInLeft).duration(2000).playOn(findViewById(R.id.Username));
        YoYo.with(Techniques.BounceInRight).duration(2000).playOn(findViewById(R.id.Level));
        YoYo.with(Techniques.FadeInLeft).duration(2000).playOn(findViewById(R.id.Catagories));
        setup_menu();
    }

    private void setup_menu(){
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        retrofit2.Call<List<FoodData>> call = apiInterface.getAllData();
        call.enqueue(new Callback<List<FoodData>>() {
            @Override
            public void onResponse(retrofit2.Call<List<FoodData>> call, Response<List<FoodData>> response) {

                if(response.isSuccessful()){
                    List<FoodData> foodDataList = response.body();
                    getRecommendedData(foodDataList.get(0).getAllmenu1());


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_Dashboard);
                            progressBar.setVisibility(View.GONE);

                            LinearLayoutCompat linearLayoutCompat = (LinearLayoutCompat) findViewById(R.id.popular_article);
                            RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.article_recycler);

                            linearLayoutCompat.setVisibility(View.VISIBLE);
                            recyclerView1.setVisibility(View.VISIBLE);

                            YoYo.with(Techniques.BounceInLeft).duration(2000).playOn(findViewById(R.id.popular_article));
                            YoYo.with(Techniques.BounceInRight).duration(2000).playOn(findViewById(R.id.article_recycler));

                            start = true;
                          //  Checking();
                        }
                    }, 1500);   //5 seconds


                }else{
                    final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Can't reach server.!\nError Data responding", Snackbar.LENGTH_INDEFINITE);
                    //   snackBar.setActionTextColor(Color.WHITE);
                    snackBar.setActionTextColor(Color.parseColor("#6672FF"));
                    snackBar.setAction("Got It", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Call your action method here
                            snackBar.dismiss();

                        }
                    });
                    snackBar.show();
                }


//                getAllMenu1(foodDataList.get(0).getAllmenu());

//                getAllMenu2(foodDataList.get(0).getAllmenu2());
//                getAllMenu3(foodDataList.get(0).getAllmenu3());
//                getAllMenu4(foodDataList.get(0).getAllmenu4());
                // lets run it.
                // we have fetched data from server.
                // now we have to show data in app using recycler view
                // lets make recycler view adapter
                // we have setup and bind popular section
                // in a same way we add recommended and all menu items
                // we add two adapter class for allmenu and recommended items.
                // so lets do it fast.
            }

            @Override
            public void onFailure(Call<List<FoodData>> call, Throwable t) {
                Toast.makeText(Main.this, "Server is not responding.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void  getRecommendedData(List<Recommended> recommendedList){
        recommendedRecyclerView = findViewById(R.id.article_recycler);
        recommendedAdapter = new RecommendedAdapter(this, recommendedList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recommendedRecyclerView.setLayoutManager(layoutManager);
        recommendedRecyclerView.setAdapter(recommendedAdapter);
    }

//    private void Checking(){
//
//        String url = IP.toString() + "Talaka/Users/Acc/Check.php?email=" + preferences.getString("Email", "");
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    JSONArray jsonArray = response.getJSONArray("Info");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject employee = jsonArray.getJSONObject(i);
//                        String Status  = employee.getString("Status");
//                        if (Status.toString().equals("Done")){
//
//                            Claimed_calls = employee.getString("Claim_Call");
//                            Claimed_recieve = employee.getString("Claim_Recieve");
//
//                            ID  = employee.getString("ID");
//                            Name  = employee.getString("Username");
//                            Lvl  = employee.getString("Lvl");
//                            Email = employee.getString("Email");
//                            Activation = employee.getString("Activation");
//                            String Gender = employee.getString("Genders");
//                            String Gender_req = employee.getString("Genders_Required");
//                            String Mobile = employee.getString("Mobile");
//                            Options = employee.getString("Options");
//
//                            String Messenger = employee.getString("Messenger");
//                            String Telegram = employee.getString("Telegram");
//
//                            SharedPreferences.Editor editor = preferences.edit();
//                            editor.putString("ID",ID.toString());
//                            editor.putString("Username",Name.toString());
//                            editor.putString("Lvl",Lvl.toString());
//                            editor.putString("Email",Email.toString());
//                            editor.putString("Claimed_Call",Claimed_calls.toString());
//                            editor.putString("Claimed_Online",Claimed_recieve.toString());
//                            editor.putString("Email",Email.toString());
//                            editor.putString("Gender",Gender.toString());
//                            editor.putString("Genders_Required",Gender_req.toString());
//                            editor.apply();
//
//                            TextView textView1 = findViewById(R.id.Username);
//                            TextView textView2 = findViewById(R.id.Level);
//
//                            textView1.setText("Hello : " + Name.toString());
//                            textView2.setText("Level : " + Lvl.toString() );
//
//
//                            if(!Name.toString().matches("Anonymous")){
//                                Accessibility++;
//                            }
//                            if(!Activation.toString().matches("Contact Admin")){
//                                Accessibility++;
//                            }
//                            if(!Gender.toString().matches("") || !Gender_req.toString().matches("")){
//                                Accessibility++;
//                            }
//
//                            if(!Mobile.toString().matches("null")){
//                                Accessibility++;
//                            }
//
//                            if(!Messenger.toString().matches("null") || !Telegram.toString().matches("null")){
//                                Accessibility++;
//                            }
//
//                            Checking2();
//                        }else {
//
//                            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Can't reach server.!\nPlease Check your internet", Snackbar.LENGTH_INDEFINITE);
//                            //   snackBar.setActionTextColor(Color.WHITE);
//                            snackBar.setActionTextColor(Color.parseColor("#6672FF"));
//                            snackBar.setAction("Got It", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    // Call your action method here
//                                    snackBar.dismiss();
//
//                                }
//                            });
//                            snackBar.show();
//                        }
//                    }
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    JSONArray jsonArray = response.getJSONArray("Claim_Value");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject employee = jsonArray.getJSONObject(i);
//                        daily_call  = employee.getString("Calls");
//                        daily_online  = employee.getString("Recieves");
//                    }
//
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("Daily_Call",daily_call.toString());
//                    editor.putString("Daily_Online",daily_online.toString());
//                    editor.apply();
//
//                    TextView calls = findViewById(R.id.calls);
//                    TextView recieve = findViewById(R.id.online);
//
//
//                    int ca,onl;
//                    ca = Integer.valueOf(daily_call.toString()) - Integer.valueOf(Claimed_calls.toString());
//                    onl = Integer.valueOf(daily_online.toString()) - Integer.valueOf(Claimed_recieve.toString());
//                    if(ca <= 0){
//                        ca = 0;
//                    }
//                    if(onl <= 0){
//                        onl = 0;
//                    }
//                    calls.setText(Html.fromHtml("Call [<font color=\"#FF0000\"><bold>" +  Integer.toString(ca) + "</bold></font>]"));
//                    recieve.setText(Html.fromHtml("Online [<font color=\"#FF0000\"><bold>" +  Integer.toString(onl) + "</bold></font>]"));
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
////
//                try {
//                    JSONArray jsonArray = response.getJSONArray("Queue_Call");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject employee = jsonArray.getJSONObject(i);
//
//                        Queue_Call  = employee.getString("COUNT(1)");
//
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("Queue_Call",Queue_Call.toString());
//                        editor.apply();
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    JSONArray jsonArray = response.getJSONArray("Queue_Online");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject employee = jsonArray.getJSONObject(i);
//
//                        Queue_online  = employee.getString("COUNT(1)");
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("Queue_Online",Queue_Call.toString());
//                        editor.apply();
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        requestQueue.add(request);
//    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void Lists(View view) {

        if(start){
            ConstraintLayout practice_layout = findViewById(R.id.talaka_practice_layout);
            ConstraintLayout assess_layout = findViewById(R.id.talaka_assess_layout);
            TextView practice = findViewById(R.id.practice_label);
            TextView Assess = findViewById(R.id.assess_label);
            practice.setTextColor(Color.parseColor("#FFFFFF"));
            Assess.setTextColor(Color.parseColor("#C6C6C6"));

            if (practice_layout.getVisibility() == View.VISIBLE){
                practice_layout.setVisibility(View.GONE);
                practice.setTextColor(Color.parseColor("#C6C6C6"));
            }else {
                YoYo.with(Techniques.FadeIn).duration(1000).playOn(practice_layout);
                practice_layout.setVisibility(View.VISIBLE);
                assess_layout.setVisibility(View.GONE);
            }
        }else {
            Toast.makeText(Main.this, "Please Wait..", Toast.LENGTH_SHORT).show();
        }


    }

    public void more(View view) {
        if(start){
            ConstraintLayout practice_layout = findViewById(R.id.talaka_practice_layout);
            ConstraintLayout assess_layout = findViewById(R.id.talaka_assess_layout);
            TextView practice = findViewById(R.id.practice_label);
            TextView Assess = findViewById(R.id.assess_label);
            Assess.setTextColor(Color.parseColor("#FFFFFF"));
            practice.setTextColor(Color.parseColor("#C6C6C6"));


            if (assess_layout.getVisibility() == View.VISIBLE){
                assess_layout.setVisibility(View.GONE);
                Assess.setTextColor(Color.parseColor("#C6C6C6"));
            }else {
                YoYo.with(Techniques.FadeIn).duration(1000).playOn(assess_layout);
                assess_layout.setVisibility(View.VISIBLE);
                practice_layout.setVisibility(View.GONE);
            }
        }else {
            Toast.makeText(Main.this, "Please Wait..", Toast.LENGTH_SHORT).show();
        }
    }

    public void me(View view) {
        if(start){
            Intent intent = new Intent(this, Me.class);
            startActivity(intent);
        }
    }

    public void Videos(View view) {
        if(start){
        Intent intent = new Intent(this, Videos.class);
        startActivity(intent);
        }
    }

    public void Traps(View view) {

        SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Train",  "Traps");
                            editor.apply();

        Intent intent = new Intent(this, List_Log.class);
        startActivity(intent);
    }

    public void Shoulders(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Train",  "Shoulders");
        editor.apply();

        Intent intent = new Intent(this, List_Log.class);
        startActivity(intent);
    }

    public void Chest(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Train",  "Chest");
        editor.apply();

        Intent intent = new Intent(this, List_Log.class);
        startActivity(intent);
    }

    public void Biceps(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Train",  "Biceps");
        editor.apply();

        Intent intent = new Intent(this, List_Log.class);
        startActivity(intent);
    }

    public void Forearms(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Train",  "Forearms");
        editor.apply();

        Intent intent = new Intent(this, List_Log.class);
        startActivity(intent);
    }

    public void Abdominals(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Train",  "Abdominals");
        editor.apply();

        Intent intent = new Intent(this, List_Log.class);
        startActivity(intent);
    }

    public void Quads(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Train",  "Quads");
        editor.apply();

        Intent intent = new Intent(this, List_Log.class);
        startActivity(intent);
    }

    public void Calves(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Train",  "Calves");
        editor.apply();

        Intent intent = new Intent(this, List_Log.class);
        startActivity(intent);
    }
}