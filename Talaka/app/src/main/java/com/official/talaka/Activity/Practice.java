package com.official.talaka.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.official.talaka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Practice extends AppCompatActivity {
    private Handler handler = new Handler();
    private View mTarget1,mTarget2,mTarget3,mTarget4,mTarget5;
    SharedPreferences sharedPreferences;
    String Lvl,Status;
    RequestQueue requestQueue;
    String Username,gender,calls,recieves,call_status,recieve_status,calls_left,recives_left;

    String IP = "http://192.168.1.100/";
    java.util.Date Server_date,Activation_date;  //server Date activation Date
    String activation;
    String dialog_tittle,dialog_message;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    String Date;
    SharedPreferences preferences;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mTarget1 = findViewById(R.id.talaka_Practice);
        mTarget2 = findViewById(R.id.practice_call);
        mTarget3 = findViewById(R.id.practice_recieve);
        mTarget4 = findViewById(R.id.practice_article);
        mTarget5 = findViewById(R.id.practice_back);

        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(mTarget1);// after start,just click mTarget view, rope is not init
        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(mTarget2);// after start,just click mTarget view, rope is not init
        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(mTarget3);// after start,just click mTarget view, rope is not init
        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(mTarget4);// after start,just click mTarget view, rope is not init
        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(mTarget5);// after start,just click mTarget view, rope is not init

        handler.postDelayed(runnable, 5000);

        mAdView = findViewById(R.id.ads_practice);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        load_ads_banner();
    }

    private void load_ads_banner() {

//        for(int i=0;i<3;i++) {

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

// TODO: Add adView to your view hierarchy.
//        }
    }

    @Override
    public void onResume(){
        super.onResume();


      //  Button call,recieve,article;

        //call = findViewById(R.id.practice_call);
      //  recieve = findViewById(R.id.practice_recieve);
      //  article = findViewById(R.id.practice_article);


        requestQueue = Volley.newRequestQueue(this);

        Username = preferences.getString("Username", "");
        Checking();

    }

    private void Checking(){
        //link
        Button call,recieve,article;
        call = findViewById(R.id.practice_call);
        recieve = findViewById(R.id.practice_recieve);
        article = findViewById(R.id.practice_article);

        String url = IP.toString() + "Talaka/Users/Info/Check.php?username=" + Username.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String Version = "2";
                String Server_Status, Version_Status = "";

                try {
                    JSONArray jsonArray1 = response.getJSONArray("Server");
                    JSONArray jsonArray2 = response.getJSONArray("Info");

                    JSONArray jsonArray3 = response.getJSONArray("Claim_Value");
                    JSONArray jsonArray4 = response.getJSONArray("Queue_Call");
                    JSONArray jsonArray5 = response.getJSONArray("Queue_Online");

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject employee = jsonArray1.getJSONObject(i);
                        Date = employee.getString("Date");

                        Server_Status  = employee.getString("Server Status");
                        Version_Status = employee.getString("Version");

                        if (!Version_Status.toString().equals(Version.toString())){
                            dialog_tittle = "Update";
                            dialog_message = "Its Better To Update Now !";
                            // dialog();
                            showWarningDialog();
                        }else {
                            /////////////////
                            if (!Server_Status.toString().equals("1")){
                                dialog_tittle = "Maintenance";
                                dialog_message = "Try Again Later !";
                                //  dialog();
                                showWarningDialog();
                            }
                            /////////////////


                            for (int ii = 0; ii < jsonArray2.length(); ii++) {
                                JSONObject employeee = jsonArray2.getJSONObject(ii);
                                Lvl = employeee.getString("Lvl");
                                activation = employeee.getString("Activation");


                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("Mobile",employeee.getString("Mobile"));
                                editor.apply();

                                try {

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                    Server_date = sdf.parse(Date.toString());

                                    if (activation.toString().equals("Contact Admin")){
                                        Activation_date = sdf.parse("01-01-2000");
                                    }else {
                                        Activation_date = sdf.parse(activation.toString());
                                    }


                                   // Toast.makeText(Practice.this, Username.toString(), Toast.LENGTH_SHORT).show();

                                 //   Toast.makeText(Practice.this, Date.toString(), Toast.LENGTH_SHORT).show();
                                 //   Toast.makeText(Practice.this, activation.toString(), Toast.LENGTH_SHORT).show();


                                    if (Server_date.getTime() > Activation_date.getTime()) {
                                        Status ="Expired";

                                        //Toast.makeText(Home.this, "Expired", Toast.LENGTH_SHORT).show();
                                        //     .putString("Access", "No")
                                    } else {
                                        if (Server_date.getTime() < Activation_date.getTime()) {

                                            //Toast.makeText(Home.this, "Work", Toast.LENGTH_SHORT).show();
                                            //     .putString("Access", "Yes")
                                            Status ="Work";
                                        }}

                                } catch (ParseException e) {
                                //    e.printStackTrace();
                                  //  Toast.makeText(Practice.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                 //   Toast.makeText(Practice.this, "error", Toast.LENGTH_SHORT).show();
                                    //      Toast.makeText(Home.this, "Expired00000", Toast.LENGTH_SHORT).show();
                                    Status ="Expired";
                                }

                            }

                        }
                    }



                    for (int iii = 0; iii < jsonArray3.length(); iii++) {
                        JSONObject employeeee = jsonArray3.getJSONObject(iii);
                        calls = employeeee.getString("Calls");
                        recieves = employeeee.getString("Recieves");

                        for (int i = 0; i < jsonArray5.length(); i++) {
                            JSONObject employee = jsonArray5.getJSONObject(i);

                            String counted2 = employee.getString("COUNT(1)");

                            try{

                                /////////////////////
                                int number2 = Integer.parseInt(recieves);
                                int counter2 = Integer.parseInt(counted2);
                                int result2 = number2 - counter2;


                                if (result2 <= 0){

                                    /////////////// now lets check calls (recive first finish it)

                                    for (int ii = 0; ii < jsonArray4.length(); ii++) {
                                        JSONObject employeee = jsonArray4.getJSONObject(ii);

                                        String counted1 = employeee.getString("COUNT(1)");

                                        try{
                                            /////////////////////
                                            int number1 = Integer.parseInt(calls);
                                            int counter1 = Integer.parseInt(counted1);
                                            int result1 = number1 - counter1;
                                            if (result1 > 0){

                                                call.setVisibility(View.VISIBLE);
                                            }
                                            /////////////////////
                                        }
                                        catch (NumberFormatException ex){
                                            ex.printStackTrace();
                                        }
                                    }

                                    /////////////////////


                                }else {
                                    if (result2 > 0){
                                        recieve.setVisibility(View.VISIBLE);
                                    }
                                }
                                /////////////////////
                            }
                            catch (NumberFormatException ex){
                                ex.printStackTrace();
                            }
                        }

                    }



                    article.setVisibility(View.VISIBLE);
              //////////////////
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


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            YoYo.with(Techniques.BounceInDown).duration(3000).playOn(mTarget1);// after start,just click mTarget view, rope is not init

            handler.postDelayed(runnable, 5000);
        }
    };

    public void Practice_Back(View view) {
        handler.removeCallbacks(runnable);
        Intent i = new Intent(Practice.this, Home.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void Call(View view) {

        if (Status.toString().equals("Work")){
            handler.removeCallbacks(runnable);
            Intent i = new Intent(Practice.this, Call.class);
            startActivity(i);
            finish();
        }else {
            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Non-activated |Contact Admin", Snackbar.LENGTH_INDEFINITE);
            //   snackBar.setActionTextColor(Color.WHITE);
            snackBar.setActionTextColor(Color.parseColor("#FFD700"));
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

    public void Recieve(View view) {

        if (Status.toString().equals("Work")){
            handler.removeCallbacks(runnable);
            Intent i = new Intent(Practice.this, Recieve.class);
            startActivity(i);
            finish();
        }else {
            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Non-activated |Contact Admin", Snackbar.LENGTH_INDEFINITE);
            //   snackBar.setActionTextColor(Color.WHITE);
            snackBar.setActionTextColor(Color.parseColor("#FFD700"));
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

    public void Article(View view) {
        handler.removeCallbacks(runnable);
        Intent i = new Intent(Practice.this, Article.class);
        startActivity(i);
        finish();
    }

    private void showWarningDialog(){
        builder = new AlertDialog.Builder(Practice.this, R.style.AlertDialogTheme);
        @SuppressLint("ResourceType") View view = LayoutInflater.from(Practice.this).inflate(
                R.xml.update,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(false);
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(dialog_tittle.toString());
        ((TextView) view.findViewById(R.id.textMessage)).setText(dialog_message.toString());
        ((TextView) view.findViewById(R.id.buttonAction)).setText("Ok");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.close);

        alertDialog = builder.create();

        view.findViewById(R.id.imageIcon).setVisibility(View.INVISIBLE);

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (dialog_tittle.toString().equals("Update")){
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        finish();
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        finish();
                    }
                }else {
                    finish();
                }


                //  alertDialog.dismiss();
                //   try {
                //      getEncryptedSharedPreferences().edit().clear().commit();
                //  } catch (GeneralSecurityException e) {
                //  } catch (IOException e) {
                //  }
                // finish();

            }
        });

        if (alertDialog.getWindow()!= null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}