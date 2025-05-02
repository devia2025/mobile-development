package com.official.talaka.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.official.talaka.More.ForegroundService;
import com.official.talaka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Recieve extends AppCompatActivity {
    RequestQueue requestQueue;
    String IP = "http://192.168.1.100/";
    SharedPreferences preferences;
    private AdView mAdView;
    String Username,Lvl,Gender,online_queue;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    View view_Error;
    String Error_Tiitle_Str, Error_description_Str;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recieve);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mAdView = findViewById(R.id.ads_recieve);
        button =  findViewById(R.id.search_btn);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        load_ads_banner(); //Toast.makeText(Recieve.this, online_queue.toString(), Toast.LENGTH_SHORT).show();
    }
    private void load_ads_banner() {
//        for(int i=0;i<3;i++) {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
// TODO: Add adView to your view hierarchy.
//        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void onResume() {
        super.onResume();
        requestQueue = Volley.newRequestQueue(this);
        online_queue = preferences.getString("Queue_Online", "");
        TextView textView = findViewById(R.id.Status);
        if(online_queue.toString().matches("0")){
            Button button = findViewById(R.id.search_btn);
            button.setVisibility(View.VISIBLE);
            textView.setText("Ready");
        }else {
            textView.setText("Online Limit reached.\ntry again tomorrow.!");
        }
    }
    public void Online_Back(View view) {
        Intent i = new Intent(Recieve.this, Home.class);
        startActivity(i);
        finish();
    }
    public void Search(View view) {
        button.setVisibility(View.INVISIBLE);
        Recieve();
    }
    private void Recieve(){
        Username = preferences.getString("Username", "");
        Lvl = preferences.getString("Lvl", "");
        Gender = preferences.getString("Genders_Required", "");
        //link
        String url = IP.toString() + "Talaka/Users/Practice/Online.php?username=" + Username.toString() + "&lvl=" + Lvl.toString() + "&gender=" + Gender.toString() ;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

//                    JSONArray jsonArray = response.getJSONArray("Info");

                    JSONArray jsonArray1 = response.getJSONArray("Queue_My_Status"); // {"Queue_My_Status":[{"Status":"Pending"}]}
                    JSONArray jsonArray2 = response.getJSONArray("Put_Me_Queue");  ///// {"Put_Me_Queue":[{"Status":"Succesfully"}]}
                    JSONArray jsonArray3 = response.getJSONArray("Get_Online_User"); /// {"Get_Online_User":[{"ID":"2","Lvl":"2","Username":"Gazr","Type":"Online","Gender_Required":"Male","Status":"Pending"}]}

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject employee = jsonArray1.getJSONObject(i);
                        String status = employee.getString("Status");
                        if (status.toString().equals("Pending")){
                            Error_Tiitle_Str = ("Queue Alert");
                            Error_description_Str = ("Failed, You Already In Queue");
                            ShowError_Dialog3();
                            return;
                        }else {
                            //////////////////

                            for (int ii = 0; ii < jsonArray2.length(); ii++) {
                                JSONObject employeee = jsonArray2.getJSONObject(ii);
                                String statuss = employeee.getString("Status");
                                if (statuss.toString().equals("Succesfully")){
                                    Error_Tiitle_Str = ("Queue Alert");
                                    Error_description_Str = ("No Caller, Successfully Created Queue For You\n\nyou will get email if another user calling you");
                                    ShowError_Dialog2();

                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("Worker","Online");
                                    editor.apply();

//                                    try {
//                                        Intent serviceIntent1 = new Intent(Call.this, ForegroundService.class);
//                                        serviceIntent1.putExtra("inputExtra", "");
//                                        ContextCompat.startForegroundService(Call.this, serviceIntent1);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }

                                    return;
                                }else {
                                    /////////////////

                                    for (int iii = 0; iii < jsonArray3.length(); iii++) {
                                        JSONObject employeeee = jsonArray3.getJSONObject(iii);

                                        String statusss = employeeee.getString("Status");
                                        String caller_username = employeeee.getString("Username");

                                        if (statusss.toString().equals("Pending")){


                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("UserActivity_username",caller_username.toString());
                                            editor.putString("UserActivity_Type","Online");
                                            editor.putString("Rate","No");
                                            editor.putString("Worker","Online");
                                            editor.apply();

                                            Intent iiii = new Intent(Recieve.this, User_Activity.class);
                                            startActivity(iiii);
                                            finish();


                                        }else {
                                            /////////////////

                                            /////////////////
                                        }
                                    }

                                    /////////////////
                                }
                            }

                            /////////////////
                        }

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    @SuppressLint("ResourceType")
    private void ShowError_Dialog2(){
        builder = new AlertDialog.Builder(Recieve.this, R.style.AlertDialogTheme);
        view_Error = LayoutInflater.from(Recieve.this).inflate(
                R.xml.error_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(false);
        builder.setView(view_Error);
        alertDialog = builder.create();
        TextView textView1 = view_Error.findViewById(R.id.Error_Tiitle);
        TextView textView2 = view_Error.findViewById(R.id.Error_description);
        textView1.setText(Error_Tiitle_Str.toString());
        textView2.setText(Error_description_Str.toString());
        view_Error.findViewById(R.id.erro_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent i = new Intent(Recieve.this, Home.class);
                startActivity(i);
                finish();
            }
        });
        if (alertDialog.getWindow()!= null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void ShowError_Dialog3(){
        builder = new AlertDialog.Builder(Recieve.this, R.style.AlertDialogTheme);
        @SuppressLint("ResourceType") View view = LayoutInflater.from(Recieve.this).inflate(
                R.xml.sup_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(false);
        builder.setView(view);

        TextView textView1 = view.findViewById(R.id.textTitle);
        TextView textView2 = view.findViewById(R.id.textMessage);

        textView1.setText(Error_Tiitle_Str.toString());
        textView2.setText(Error_description_Str.toString());

        ((TextView) view.findViewById(R.id.buttonNo)).setText("keep Waiting");
        ((TextView) view.findViewById(R.id.buttonYes)).setText("Cancel Queue");

        alertDialog = builder.create();

        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                remove_me();
            }
        });
        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                button.setVisibility(View.VISIBLE);
            }
        });
        if (alertDialog.getWindow()!= null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void remove_me(){
        //link
       // String url = IP.toString() + "Talaka/Users/Practice/Delete_Queue.php?user=" + Username.toString() + "&lvl=" + Lvl.toString() + "&type=Online";
        String url = IP.toString() + "Talaka/Users/Practice/Delete_Queue.php?user=" + Username.toString()  + "&type=Online";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray1 = response.getJSONArray("result");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject employee = jsonArray1.getJSONObject(i);
                        String status = employee.getString("Status");
                        if (status.toString().equals("Succesfully")){
                            try {
//                                Intent serviceIntent = new Intent(Recieve.this, ForegroundService.class);
//                                stopService(serviceIntent);
//                                back();
                                Intent ii = new Intent(Recieve.this, Home.class);
                                startActivity(ii);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }


}