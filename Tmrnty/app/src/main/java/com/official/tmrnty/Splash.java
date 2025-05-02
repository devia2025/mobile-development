package com.official.tmrnty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.official.tmrnty.Extra.Main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Splash extends AppCompatActivity {
    String IP =""; //"http://192.168.1.100/";

    int progress=1;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    RequestQueue requestQueue;
    String dialog_tittle,dialog_message;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    private AdView mAdView;
    private AdManagerInterstitialAd mAdManagerInterstitialAd;
    AdManagerAdRequest adRequest;
    String Ads,Server_Status, Version_Status,Version;

   // SharedPreferences preferences;

    public static SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        preferences = getSharedPreferences( getPackageName() + "IP", MODE_PRIVATE);

        progressBar = findViewById(R.id.progressBar_Welcome);
        mAdView = findViewById(R.id.ads_splash);
      //  preferences = PreferenceManager.getDefaultSharedPreferences(this);

        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(findViewById(R.id.welcome_img));// after start,just click mTarget view, rope is not init
        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(findViewById(R.id.app_welcome));// after start,just click mTarget view, rope is not init
        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(findViewById(R.id.progressBar_Welcome));// after start,just click mTarget view, rope is not init


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void load_ads_banner() {

        for(int i=0;i<10;i++) {

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
// TODO: Add adView to your view hierarchy.
        }
    }

    private void load_ads_interial(){
        AdManagerInterstitialAd.load(this,"ca-app-pub-7549315536694796/1374858860", adRequest,
                new AdManagerInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                        mAdManagerInterstitialAd = interstitialAd;
                        if (mAdManagerInterstitialAd != null) {
                            mAdManagerInterstitialAd.show(Splash.this);
                            listner_interial();
                            return;}
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        //  Log.i(TAG, loadAdError.getMessage());
                        mAdManagerInterstitialAd = null;
//                            Toast.makeText(Splash.this, "No Ads, Thanks!", Toast.LENGTH_SHORT).show();
                        Intent ii = new Intent(Splash.this, Main.class);
                        startActivity(ii);
                        finish();
                        return;
                    }
                });
    }

    private void listner_interial(){
        mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
//                    Toast.makeText(Splash.this, "Thanks,!", Toast.LENGTH_SHORT).show();
                Intent ii = new Intent(Splash.this, Main.class);
                startActivity(ii);
                finish();
                return;
            }
            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {

                ////////////////////

                    // email.setText(name.toString());
                    //  password.setText(pass.toString());

                    Intent intent = new Intent(Splash.this, Main.class);
                    startActivity(intent);
                    finish();
                    return;

                //////////////////

//                    Toast.makeText(Splash.this, "The ad failed to show,!", Toast.LENGTH_SHORT).show();
//                    Intent ii = new Intent(Splash.this, Login.class);
//                    startActivity(ii);
//                    finish();

            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (progress == 100) {
                handler.removeCallbacks(runnable);

                if (!Version_Status.toString().matches(Version.toString())){
                    progressBar.setVisibility(View.GONE);
                    dialog_tittle = "Update";
                    dialog_message = "Its Better To Update Now !";
                    // dialog();
                    showWarningDialog();
                }else {
                    /////////////////

                    if (Server_Status.toString().equals("1")){

                        if(Ads.toString().matches("0")){


                            ////////////////////
                                Intent intent = new Intent(Splash.this, Main.class);
                                startActivity(intent);
                                finish();

                            //////////////////


                        }

                    }else {

                        progressBar.setVisibility(View.GONE);
                        dialog_tittle = "Maintenance";
                        dialog_message = "Try Again Later !";
                        //  dialog();
                        showWarningDialog();
                    }
                    /////////////////
                }
            }else {
                if (progress == 5) {
                    if(Ads.toString().matches("1")){
                        load_ads_banner();}
                }else {
                    if (progress == 80) {
                        if (Ads.toString().matches("1")){
                            load_ads_interial();}}}
                progress+=1;
                progressBar.setProgress(progress);
                handler.postDelayed(runnable, 120);
            }

        }
    };

    private void Checking0(){
        //link
        String url = "https://raw.githubusercontent.com/DevIA3kl/Tmrnty/main/Status.txt";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Login");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        IP  = employee.getString("IP");
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("IP",IP.toString());
                        editor.apply();

                        Checking();
                    }
                } catch (JSONException e) {
                   // e.printStackTrace();
                    Checking0();
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

    private void Checking(){
        //link
        String url =  IP.toString() + "Tmrnty/Server/Check.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        Server_Status  = employee.getString("Server_Status");
                        Version_Status = employee.getString("Version");
                        Ads = employee.getString("Ads");
                        handler.postDelayed(runnable, 35);
                    }
                } catch (JSONException e) {
                   // e.printStackTrace();
                    Checking();
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

    @Override
    public void onResume() {
        super.onResume();
        Version = "8";
        adRequest = new AdManagerAdRequest.Builder().build();
        requestQueue = Volley.newRequestQueue(this);
        handler.postDelayed(runnable2, 3000);
        //
    }
    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            Checking0();
        }
    };

    private void showWarningDialog(){
        builder = new AlertDialog.Builder(Splash.this, R.style.AlertDialogTheme);
        @SuppressLint("ResourceType") View view = LayoutInflater.from(Splash.this).inflate(
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
            }
        });

        if (alertDialog.getWindow()!= null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

}