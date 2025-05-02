package com.official.talaka.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.google.android.material.snackbar.Snackbar;
import com.official.talaka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class User_Activity extends AppCompatActivity {

    String email_Caller,Username_Caller,Type_Caller,my_Username;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    View view_Error;
    EditText language,respect;
    Button btn;
    String mobile,messenger,telegram;
    private View mTarget1;
    String IP = "http://192.168.1.100/";
    String Rate;
    SharedPreferences preferences;
    private AdView mAdView;
    private WebView webView;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        Username_Caller = preferences.getString("UserActivity_username", "");
        Type_Caller = preferences.getString("UserActivity_Type", "");

        my_Username = preferences.getString("Username", "");
        Rate = preferences.getString("Rate", "");

        language = findViewById(R.id.rate_user_language);
        respect = findViewById(R.id.rate_user_repsect);
        btn = findViewById(R.id.rate_btn);

        mAdView = findViewById(R.id.ads_user);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        load_ads_banner();

        webView = (WebView) findViewById(R.id.email);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.setWebChromeClient(new WebChromeClient());
        if (18 < Build.VERSION.SDK_INT ){
            //18 = JellyBean MR2, KITKAT=19
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        webView.setWebViewClient(new MyWebViewClient());
        back = findViewById(R.id.call_back);
    }


    public void onResume() {
        super.onResume();
        requestQueue = Volley.newRequestQueue(this);
        get_User_Info();
    }


    private void load_ads_banner() {

//        for(int i=0;i<3;i++) {

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

// TODO: Add adView to your view hierarchy.
//        }
    }

    private void get_User_Info() {
       //   Toast.makeText(User_Activity.this, Username_Caller.toString(), Toast.LENGTH_SHORT).show();
        //link
        String url = IP.toString() + "talaka/Users/Practice/Check_Email.php?username=" + Username_Caller.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Info");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);


                       // Username_Caller = employee.getString("Username");
                        String lvl = employee.getString("Lvl");

                        String email = employee.getString("Email");
                        email_Caller = employee.getString("Email");

                        String gender = employee.getString("Genders");
                        String rate = employee.getString("Rate");
                        String language = employee.getString("Language");
                        String respect = employee.getString("Respect");
                        mobile = employee.getString("Mobile");
                        messenger = employee.getString("Messenger");
                        telegram = employee.getString("Telegram");

                        //Toast.makeText(User_Activity.this, mobile.toString(), Toast.LENGTH_SHORT).show();
                        TextView username_txt =  (TextView) findViewById(R.id.username_user);
                        username_txt.setText(Username_Caller.toString());

                        TextView lvl_txt =  (TextView) findViewById(R.id.lvl_user);
                        lvl_txt.setText(lvl.toString());

                        TextView email_txt =  (TextView) findViewById(R.id.email_user);
                        email_txt.setText(email.toString());

                        TextView gender_txt =  (TextView) findViewById(R.id.gender_user);
                        gender_txt.setText(gender.toString());

                        TextView Mobile_textview =  (TextView) findViewById(R.id.mobile);
                        TextView messenger_textview =  (TextView) findViewById(R.id.messenger);
                        TextView telegram_textview =  (TextView) findViewById(R.id.telegram);

                        Mobile_textview.setVisibility(View.VISIBLE);
                        messenger_textview.setVisibility(View.VISIBLE);
                        telegram_textview.setVisibility(View.VISIBLE);
                        ///////////////////////////////////////
                        TextView rate_txt =  (TextView) findViewById(R.id.Rate_Str);

                        TextView total_rate_txt =  (TextView) findViewById(R.id.total_rate);
                        total_rate_txt.setText(rate.toString());


                        TextView language_txt =  (TextView) findViewById(R.id.language_rate);
                        TextView respect_txt =  (TextView) findViewById(R.id.respect_rate);

                        ///////////////////////////////////////



                        if (rate.toString().equals("0")){
                            rate_txt.setText("Rate : (0/5)");
                            language_txt.setText(language.toString());
                            respect_txt.setText(respect.toString());
                        }else {
                            DecimalFormat decimalFormat;
                            decimalFormat = new DecimalFormat("#.#");

                            double valueTwo,valueThree,valuedouble;

                            valueTwo = Double.parseDouble(language.toString()) / Double.parseDouble(rate.toString());
                            valueThree = Double.parseDouble(respect.toString()) / Double.parseDouble(rate.toString());

                            valuedouble =valueTwo+valueThree;
                            rate_txt.setText("Rate ( " + decimalFormat.format(valuedouble/2)  + " / 5 )");

                            language_txt.setText(decimalFormat.format(valueTwo));
                            respect_txt.setText(decimalFormat.format(valueThree));
                        }



                        ///////////////////////////////////////
                        ///////////////////////////////////////


                        check_Rated();
                        //  results.append(firstName + ", " + String.valueOf(age) + ", " + mail +"\n\n");
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

    private void check_Rated() {

        //link
        String url = IP.toString() + "Talaka/Users/Update/Check_Rate_User.php?email=" + email_Caller.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);

                        String status = employee.getString("Status");

                        if (status.toString().equals("Succesfully")){


                            if (Type_Caller.toString().equals("Call")){

                                String Call = employee.getString("Recieve_Rate");


                               // Toast.makeText(User_Activity.this, Call.toString(), Toast.LENGTH_SHORT).show();

                                if (!Call.toString().contains(my_Username.toString())){

                                    if (Rate.toString().equals("Yes")){
                                        language.setVisibility(View.VISIBLE);
                                        respect.setVisibility(View.VISIBLE);
                                        btn.setVisibility(View.VISIBLE);
                                    }else {
                                        if (Rate.toString().equals("No")){
                                            //Toast.makeText(User_Activity.this, "Email : "+email_Caller.toString() + "\n" + "User : " + Username_Caller.toString(), Toast.LENGTH_SHORT).show();
                                            webView.loadUrl(IP.toString() + "Talaka/Users/Verification/verify/online.php?email=" + email_Caller.toString()  + "&user=" + Username_Caller.toString());

                                            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Please Wait.!", Snackbar.LENGTH_INDEFINITE);
                                            //   snackBar.setActionTextColor(Color.WHITE);
                                            snackBar.setActionTextColor(Color.parseColor("#6672FF"));
                                            snackBar.setAction("Ok", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    // Call your action method here
                                                    snackBar.dismiss();

                                                }
                                            });
                                            snackBar.show();

                                            back.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                }
                            }else {
                                if (Type_Caller.toString().equals("Online")){

                                    String Recieve = employee.getString("Call_Rate");

                                    // Toast.makeText(User_Activity.this, Call.toString(), Toast.LENGTH_SHORT).show();

                                    if (!Recieve.toString().contains(my_Username.toString())){
                                        if (Rate.toString().equals("Yes")){
                                            language.setVisibility(View.VISIBLE);
                                            respect.setVisibility(View.VISIBLE);
                                            btn.setVisibility(View.VISIBLE);
                                        }else {
                                            if (Rate.toString().equals("No")){
                                                //Toast.makeText(User_Activity.this, "Email : "+email_Caller.toString() + "\n" + "User : " + Username_Caller.toString(), Toast.LENGTH_SHORT).show();
                                                webView.loadUrl(IP.toString() + "Talaka/Users/Verification/verify/caller.php?email=" + email_Caller.toString()  + "&user=" + Username_Caller.toString());

                                                final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Please Wait.!", Snackbar.LENGTH_INDEFINITE);
                                                //   snackBar.setActionTextColor(Color.WHITE);
                                                snackBar.setActionTextColor(Color.parseColor("#6672FF"));
                                                snackBar.setAction("Ok", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        // Call your action method here
                                                        snackBar.dismiss();

                                                    }
                                                });
                                                snackBar.show();

                                                back.setVisibility(View.INVISIBLE);
                                            }
                                        }

                                    }
                                }
                            }


                        }else {
                            ShowError_Dialog();
                        }


                        //  results.append(firstName + ", " + String.valueOf(age) + ", " + mail +"\n\n");
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

    private void Rating() {

        EditText language,respect;
        Button btn;

        language = findViewById(R.id.rate_user_language);
        respect = findViewById(R.id.rate_user_repsect);
        btn = findViewById(R.id.rate_btn);

        if(!language.getText().toString().matches("") && !respect.getText().toString().matches("")){
            language.setEnabled(false);
            respect.setEnabled(false);
            btn.setVisibility(View.GONE);

            //link
            String url = IP.toString() + "Talaka/Users/Update/Rate_Increase.php?email=" + email_Caller.toString() + "&language=" + language.getText().toString() + "&respet=" + respect.getText().toString() + "&type=" + Type_Caller.toString() + "&me=" + my_Username.toString();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);

                            String status = employee.getString("Status");

                            if (status.toString().equals("Succesfully")){


                                language.setVisibility(View.GONE);
                                respect.setVisibility(View.GONE);
                                btn.setVisibility(View.GONE);

                                final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Done, thanks.", Snackbar.LENGTH_INDEFINITE);
                                //   snackBar.setActionTextColor(Color.WHITE);
                                snackBar.setActionTextColor(Color.parseColor("#FFD700"));
                                snackBar.setAction("Refresh", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Call your action method here
                                        snackBar.dismiss();

                                        Intent i = new Intent(User_Activity.this, User_Activity.class);
                                        startActivity(i);
                                        finish();

                                    }
                                });
                                snackBar.show();

                            }else {
                                ShowError_Dialog();
                            }


                            //  results.append(firstName + ", " + String.valueOf(age) + ", " + mail +"\n\n");
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
        }else {
            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Error, input required.", Snackbar.LENGTH_INDEFINITE);
            //   snackBar.setActionTextColor(Color.WHITE);
            snackBar.setActionTextColor(Color.parseColor("#FFD700"));
            snackBar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call your action method here
                    snackBar.dismiss();

                }
            });
            snackBar.show();
        }




    }
    @SuppressLint("ResourceType")
    private void ShowError_Dialog(){
        builder = new AlertDialog.Builder(User_Activity.this, R.style.AlertDialogTheme);
       view_Error = LayoutInflater.from(User_Activity.this).inflate(
                R.xml.error_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(true);
        builder.setView(view_Error);

        alertDialog = builder.create();

        view_Error.findViewById(R.id.erro_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(User_Activity.this, Practice.class);
                startActivity(i);
                finish();

                alertDialog.dismiss();

            }
        });

        if (alertDialog.getWindow()!= null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

///////




        ////////////
        // Spinner_install();
    }

    public void Rating(View view) {
        Rating();
    }

    public void User_Back(View view) {
        Intent ii = new Intent(User_Activity.this, Settings.class);
        startActivity(ii);
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

    public void phone_number(View view) {

        if (mobile.toString().contains("+20")){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobile.toString().substring(2,13)));
            startActivity(intent);
            return;
        }
    }

    public void messenger(View view) {
        //
           if (messenger.toString().equals("null") || messenger.toString().equals("")){

               final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "User not Provided Messenger Link.", Snackbar.LENGTH_INDEFINITE);
               //   snackBar.setActionTextColor(Color.WHITE);
               snackBar.setActionTextColor(Color.parseColor("#FFD700"));
               snackBar.setAction("Ok", new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       // Call your action method here
                       snackBar.dismiss();

                   }
               });
               snackBar.show();

           } else {
               Uri uri = Uri.parse(messenger.toString()); // missing 'http://' will cause crashed
               Intent intent0 = new Intent(Intent.ACTION_VIEW, uri);
               startActivity(intent0);
           }

    }

    public void Telegram(View view) {

        if (telegram.toString().equals("null") || telegram.toString().equals("")){

            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "User not Provided Telegram Link.", Snackbar.LENGTH_INDEFINITE);
            //   snackBar.setActionTextColor(Color.WHITE);
            snackBar.setActionTextColor(Color.parseColor("#FFD700"));
            snackBar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call your action method here
                    snackBar.dismiss();

                }
            });
            snackBar.show();

        } else {
            Uri uri = Uri.parse("https://telegram.me/" + telegram.toString()); // missing 'http://' will cause crashed
            Intent intent0 = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent0);
        }
    }


    public void viewSource() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(
                "javascript:this.document.location.href = 'source://' + encodeURI(document.documentElement.outerHTML);");
    }

    public class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("source://")) {
                try {
                    String html = URLDecoder.decode(url, "UTF-8").substring(9);
                    sourceReceived(html);
                } catch (UnsupportedEncodingException e) {
                    // Log.e("example", "failed to decode source", e);
                }
                webView.getSettings().setJavaScriptEnabled(false);
                return true;
            }
            // For all other links, let the WebView do it's normal thing
            return false;
        }

        public void onPageFinished(WebView view, String url) {
            // do your stuff here
            viewSource();
        }

    }
    private void sourceReceived(String html) {
        //  AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  builder.setMessage(html);
        //  builder.setTitle("View Source");
        //  AlertDialog dialog = builder.create();
        //   dialog.show();

        if(html.toString().contains("Authentication succeeded")){

            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Done, User has been notified by email", Snackbar.LENGTH_INDEFINITE);
            //   snackBar.setActionTextColor(Color.WHITE);
            snackBar.setActionTextColor(Color.parseColor("#6672FF"));
            snackBar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                 //   Intent ii = new Intent(Signup.this, Verification.class);
                  //  startActivity(ii);
                    // Call your action method here
                    snackBar.dismiss();

                }
            });
            snackBar.show();

            back.setVisibility(View.VISIBLE);
        }else {

            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Something went wrong.!", Snackbar.LENGTH_INDEFINITE);
            //   snackBar.setActionTextColor(Color.WHITE);
            snackBar.setActionTextColor(Color.parseColor("#6672FF"));
            snackBar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call your action method here
                    snackBar.dismiss();

                }
            });
            snackBar.show();
            back.setVisibility(View.VISIBLE);
        }
    }
}