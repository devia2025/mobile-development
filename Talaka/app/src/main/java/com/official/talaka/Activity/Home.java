package com.official.talaka.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;
import com.official.talaka.Extra.adapter.RecommendedAdapter;
import com.official.talaka.Extra.model.FoodData;
import com.official.talaka.Extra.model.Recommended;
import com.official.talaka.Extra.retrofit.ApiInterface;
import com.official.talaka.Extra.retrofit.RetrofitClient;
import com.official.talaka.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {
    SharedPreferences preferences;
    RequestQueue requestQueue;
    ApiInterface apiInterface;
    RecyclerView recommendedRecyclerView;
    RecommendedAdapter recommendedAdapter;
    String IP = "http://192.168.1.100/";
    Boolean start = false;
    Boolean Access = false;
    Boolean Renew = false;
    String Activation, Server_date;
    String ID, Name, Lvl, Email,Options;
    String Claimed_calls, Claimed_recieve, Queue_Call, Queue_online,daily_call, daily_online;
    int Accessibility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        requestQueue = Volley.newRequestQueue(this);

        YoYo.with(Techniques.FadeIn).duration(1000).playOn(findViewById(R.id.dashboard_container));
        YoYo.with(Techniques.BounceInLeft).duration(2000).playOn(findViewById(R.id.Username));
        YoYo.with(Techniques.BounceInRight).duration(2000).playOn(findViewById(R.id.Level));
        YoYo.with(Techniques.FadeInRight).duration(2000).playOn(findViewById(R.id.imageView4));
        YoYo.with(Techniques.FadeInLeft).duration(2000).playOn(findViewById(R.id.Catagories));

        setup_menu();
    }
    public void Practice(View view) {
        if(start){
            if (Options.toString().contains("1")){
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
                final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "you have no access.!", Snackbar.LENGTH_INDEFINITE);
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

        }
//        else {
//            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Can't reach server.!\nPlease Check your internet", Snackbar.LENGTH_INDEFINITE);
//            //   snackBar.setActionTextColor(Color.WHITE);
//            snackBar.setActionTextColor(Color.parseColor("#6672FF"));
//            snackBar.setAction("Got It", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Call your action method here
//                    snackBar.dismiss();
//
//                }
//            });
//            snackBar.show();
//        }

    }
    public void Assess(View view) {
        if(start){

            if (Options.toString().contains("2")){
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
                final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "you have no access.!", Snackbar.LENGTH_INDEFINITE);
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

        }else {
            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Can't reach server.!\nPlease Check your internet", Snackbar.LENGTH_INDEFINITE);
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

                            Checking();
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
                Toast.makeText(Home.this, "Server is not responding.", Toast.LENGTH_SHORT).show();
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
    private void Checking(){

        String url = IP.toString() + "Talaka/Users/Acc/Check.php?email=" + preferences.getString("Email", "");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("Info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        String Status  = employee.getString("Status");
                        if (Status.toString().equals("Done")){

                            Claimed_calls = employee.getString("Claim_Call");
                            Claimed_recieve = employee.getString("Claim_Recieve");

                             ID  = employee.getString("ID");
                             Name  = employee.getString("Username");
                             Lvl  = employee.getString("Lvl");
                             Email = employee.getString("Email");
                             Activation = employee.getString("Activation");
                             String Gender = employee.getString("Genders");
                             String Gender_req = employee.getString("Genders_Required");
                             String Mobile = employee.getString("Mobile");
                             Options = employee.getString("Options");

                             String Messenger = employee.getString("Messenger");
                             String Telegram = employee.getString("Telegram");

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("ID",ID.toString());
                            editor.putString("Username",Name.toString());
                            editor.putString("Lvl",Lvl.toString());
                            editor.putString("Email",Email.toString());
                            editor.putString("Claimed_Call",Claimed_calls.toString());
                            editor.putString("Claimed_Online",Claimed_recieve.toString());
                            editor.putString("Email",Email.toString());
                            editor.putString("Gender",Gender.toString());
                            editor.putString("Genders_Required",Gender_req.toString());
                            editor.apply();

                            TextView textView1 = findViewById(R.id.Username);
                            TextView textView2 = findViewById(R.id.Level);

                            textView1.setText("Hello : " + Name.toString());
                            textView2.setText("Level : " + Lvl.toString() );


                            if(!Name.toString().matches("Anonymous")){
                                Accessibility++;
                            }
                            if(!Activation.toString().matches("Contact Admin")){
                                Accessibility++;
                            }
                            if(!Gender.toString().matches("") || !Gender_req.toString().matches("")){
                                Accessibility++;
                            }

                            if(!Mobile.toString().matches("null")){
                                Accessibility++;
                            }

                            if(!Messenger.toString().matches("null") || !Telegram.toString().matches("null")){
                                Accessibility++;
                            }

                           Checking2();
                        }else {

                            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Can't reach server.!\nPlease Check your internet", Snackbar.LENGTH_INDEFINITE);
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
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray jsonArray = response.getJSONArray("Claim_Value");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        daily_call  = employee.getString("Calls");
                        daily_online  = employee.getString("Recieves");
                    }

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("Daily_Call",daily_call.toString());
                    editor.putString("Daily_Online",daily_online.toString());
                    editor.apply();

                    TextView calls = findViewById(R.id.calls);
                    TextView recieve = findViewById(R.id.online);


                    int ca,onl;
                    ca = Integer.valueOf(daily_call.toString()) - Integer.valueOf(Claimed_calls.toString());
                    onl = Integer.valueOf(daily_online.toString()) - Integer.valueOf(Claimed_recieve.toString());
                    if(ca <= 0){
                        ca = 0;
                    }
                    if(onl <= 0){
                        onl = 0;
                    }
                    calls.setText(Html.fromHtml("Call [<font color=\"#FF0000\"><bold>" +  Integer.toString(ca) + "</bold></font>]"));
                    recieve.setText(Html.fromHtml("Online [<font color=\"#FF0000\"><bold>" +  Integer.toString(onl) + "</bold></font>]"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
                try {
                    JSONArray jsonArray = response.getJSONArray("Queue_Call");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);

                        Queue_Call  = employee.getString("COUNT(1)");

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("Queue_Call",Queue_Call.toString());
                        editor.apply();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray jsonArray = response.getJSONArray("Queue_Online");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);

                        Queue_online  = employee.getString("COUNT(1)");
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("Queue_Online",Queue_Call.toString());
                        editor.apply();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }
    private void Checking2(){

        String url = IP.toString() + "Talaka/Users/Acc/Check.php?email=" + preferences.getString("Email", "");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("Server");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        Server_date  = employee.getString("Date");
                        if (Server_date.toString().contains("-")){
                            ///////////////
                            if (Activation.toString().matches("Contact Admin")){
                                Access = false;
                            }else {

                                java.util.Date Server_date_, my_date_;

                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                    Server_date_ = sdf.parse(Server_date.toString());
                                    my_date_ = sdf.parse(Activation.toString());
                                    if (Server_date_.getTime() <= my_date_.getTime()) {
                                        Access = true;
                                    }else {
                                        Renew = true;
                                        Access = false;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            ///////////////
                        }
                        start = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                try {
//                    JSONArray jsonArray = response.getJSONArray("Claim_Value");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject employee = jsonArray.getJSONObject(i);
//
//                        call  = employee.getString("Calls");
//                        online  = employee.getString("Recieves");
//
//                        TextView calls = findViewById(R.id.calls);
//                        TextView recieve = findViewById(R.id.online);
//
//                        calls.setText(Html.fromHtml("Call [<font color=\"#FF0000\"><bold>" +  call.toString() + "</bold></font>]"));
//                        recieve.setText(Html.fromHtml("Online [<font color=\"#FF0000\"><bold>" +  online.toString() + "</bold></font>]"));
//
//
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//    public void views_all(View view) {
//        Intent intent = new Intent(this, Articles.class);
//        startActivity(intent);
//    }
    public void articles(View view) {
        Intent intent = new Intent(this, Articles.class);
        startActivity(intent);
    }
    public void call(View view) {
        int checker = Integer.valueOf(daily_call.toString()) - Integer.valueOf(Claimed_calls.toString());
        if (checker > 0){
            if (!Access){
                if(Renew){
                    final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Upgrade.!\nPlease Contact Admin", Snackbar.LENGTH_INDEFINITE);
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
                }else {
                    final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Can't Access.!\nPlease Contact Admin", Snackbar.LENGTH_INDEFINITE);
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

            }else {
                if(Accessibility != 5){
                    final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Complete your info? ->Settings.\n[Username/Gender/Mobile/Social]", Snackbar.LENGTH_INDEFINITE);
                    //   snackBar.setActionTextColor(Color.WHITE);
                    snackBar.setActionTextColor(Color.parseColor("#6672FF"));
                    snackBar.setAction("Check", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Call your action method here
                            snackBar.dismiss();
                            Intent intent = new Intent(Home.this, com.official.talaka.Activity.Settings.class);
                            startActivity(intent);
                        }
                    });
                    snackBar.show();
                }else {
                    if(Integer.valueOf(daily_online.toString()) - Integer.valueOf(Claimed_recieve.toString()) > 0){
                        final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Complete your Online task first, before Call.", Snackbar.LENGTH_INDEFINITE);
                        //   snackBar.setActionTextColor(Color.WHITE);
                        snackBar.setActionTextColor(Color.parseColor("#6672FF"));
                        snackBar.setAction("Check", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Call your action method here
                                snackBar.dismiss();
                                Intent intent = new Intent(Home.this, com.official.talaka.Activity.Recieve.class);
                                startActivity(intent);
                            }
                        });
                        snackBar.show();
                    }else {
                        Intent intent = new Intent(this, com.official.talaka.Activity.Call.class);
                        startActivity(intent);
                    }

                }

            }
        }else {
            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Daily Call Limit?.!", Snackbar.LENGTH_INDEFINITE);
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
    }
    public void Online(View view) {
        int checker = Integer.valueOf(daily_online.toString()) - Integer.valueOf(Claimed_recieve.toString());
        if (checker > 0){
            if (!Access){
                if(Renew){
                    final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Upgrade.!\nPlease Contact Admin", Snackbar.LENGTH_INDEFINITE);
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
                }else {
                    final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Can't Access.!\nPlease Contact Admin", Snackbar.LENGTH_INDEFINITE);
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

            }else {
                if(Accessibility != 5){
                    final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Complete your info? ->Settings.\n[Username/Gender/Mobile/Social]", Snackbar.LENGTH_INDEFINITE);
                    //   snackBar.setActionTextColor(Color.WHITE);
                    snackBar.setActionTextColor(Color.parseColor("#6672FF"));
                    snackBar.setAction("Check", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Call your action method here
                            snackBar.dismiss();
                            Intent intent = new Intent(Home.this, com.official.talaka.Activity.Settings.class);
                            startActivity(intent);
                        }
                    });
                    snackBar.show();
                }else {
                    Intent intent = new Intent(this, com.official.talaka.Activity.Recieve.class);
                    startActivity(intent);
                }

            }
        }else {
            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Daily Online Limit?.!", Snackbar.LENGTH_INDEFINITE);
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
    }
    public void Settings(View view) {
        if (Access){
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
        }

    }

    public void questions_assess(View view) {
        if (!Access){
            if(Renew){
                final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Upgrade.!\nPlease Contact Admin", Snackbar.LENGTH_INDEFINITE);
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
            }else {
                final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Can't Access.!\nPlease Contact Admin", Snackbar.LENGTH_INDEFINITE);
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

        }else {
            if(Accessibility != 5){
                final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Complete your info? ->Settings.\n[Username/Gender/Mobile/Social]", Snackbar.LENGTH_INDEFINITE);
                //   snackBar.setActionTextColor(Color.WHITE);
                snackBar.setActionTextColor(Color.parseColor("#6672FF"));
                snackBar.setAction("Check", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Call your action method here
                        snackBar.dismiss();
                        Intent intent = new Intent(Home.this, com.official.talaka.Activity.Settings.class);
                        startActivity(intent);
                    }
                });
                snackBar.show();
            }else {

                    Intent intent = new Intent(this, com.official.talaka.Activity.Assess.class);
                    startActivity(intent);
            }

        }
    }
}