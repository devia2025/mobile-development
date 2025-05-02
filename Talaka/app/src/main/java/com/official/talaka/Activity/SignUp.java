package com.official.talaka.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.official.talaka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class SignUp extends AppCompatActivity {
    RequestQueue requestQueue;
    String IP = "http://192.168.1.100/";
    TextView email,password,recovery;
    ImageView btn;
    String token;
    SharedPreferences sharedPreferences;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        YoYo.with(Techniques.BounceIn).duration(3000).playOn(findViewById(R.id.welcome_label));
        YoYo.with(Techniques.BounceInLeft).duration(3000).playOn(findViewById(R.id.email_register));
        YoYo.with(Techniques.BounceInRight).duration(3000).playOn(findViewById(R.id.password_register));
        YoYo.with(Techniques.ZoomInDown).duration(3000).playOn(findViewById(R.id.register_btn));
        YoYo.with(Techniques.BounceIn).duration(3000).playOn(findViewById(R.id.register_bottom));

        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        btn = findViewById(R.id.register_btn);
        recovery = findViewById(R.id.recovery);

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        webView = (WebView) findViewById(R.id.signup_msg);
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

        final ImageView button = (ImageView) findViewById(R.id.register_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(email.getText().toString().matches("") || password.getText().toString().matches("") || !email.getText().toString().contains("@")){

                    final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Complete info.!", Snackbar.LENGTH_INDEFINITE);
                    //   snackBar.setActionTextColor(Color.WHITE);
                    snackBar.setActionTextColor(Color.parseColor("#6672FF"));
                    snackBar.setAction("Got it", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackBar.dismiss();
                        }
                    });
                    snackBar.show();
                }else {
///


                    email.setEnabled(false);
                    password.setEnabled(false);
                    btn.setEnabled(false);
                    recovery.setEnabled(false);
                    Toast.makeText(SignUp.this, "Please Wait..!", Toast.LENGTH_LONG).show();

                    //link
                    String url = IP.toString() + "Talaka/Users/Acc/Register.php?email=" + email.getText().toString() + "&password=" +  password.getText().toString();
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONArray jsonArray = response.getJSONArray("Register_Status");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject employee = jsonArray.getJSONObject(i);

                                    String Status  = employee.getString("Status");

                                    if (Status.toString().equals("Success")){
                                        token  = employee.getString("Token").replace("No-","");
//                                        Toast.makeText(Signup.this, "Account Successfully Registered", Toast.LENGTH_SHORT).show();


                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("Token",token.toString());
                                        editor.putString("Email",email.getText().toString());
                                        editor.putString("Password",password.getText().toString());
                                        editor.apply();

                                        try {
                                            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                        } catch (Exception e) {
                                            // TODO: handle exception
                                        }

                                        webView.loadUrl(IP.toString() + "Talaka/Users/Verification/verify/register.php?email=" + email.getText().toString()  + "&token=" + token.toString());
                                    }else {
                                        if (Status.toString().equals("Error")){

//                                        Toast.makeText(Signup.this, "Something went wrong.!", Toast.LENGTH_SHORT).show();

                                            email.setEnabled(true);
                                            password.setEnabled(true);
                                            btn.setEnabled(true);
                                            recovery.setEnabled(true);

                                            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Something went wrong.!\n- use another email", Snackbar.LENGTH_INDEFINITE);
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
                    //////
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void Signup(View view) {
    }
    public void Recovery(View view) {
        Intent intent = new Intent(this, Forgot.class);
        startActivity(intent);
    }

    public void Back(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
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

            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Account Successfully Registered, Check your email", Snackbar.LENGTH_INDEFINITE);
            //   snackBar.setActionTextColor(Color.WHITE);
            snackBar.setActionTextColor(Color.parseColor("#6672FF"));
            snackBar.setAction("Verification", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent ii = new Intent(SignUp.this, Verification.class);
                    startActivity(ii);
                    // Call your action method here
                    snackBar.dismiss();

                }
            });
            snackBar.show();


        }else {
            email.setEnabled(true);
            password.setEnabled(true);
            btn.setEnabled(true);
            recovery.setEnabled(true);

            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Something went wrong.!", Snackbar.LENGTH_INDEFINITE);
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
}