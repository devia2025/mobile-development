package com.official.talaka.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;
import com.official.talaka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Forgot extends AppCompatActivity {
    String IP = "http://192.168.1.100/";
    RequestQueue requestQueue;
    private WebView webView;
    EditText email;
    ImageView btn;
    String rand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot);
        YoYo.with(Techniques.StandUp)
                .duration(3000)
                .playOn(findViewById(R.id.Email_forgot));

        YoYo.with(Techniques.ZoomInDown).duration(3000).playOn(findViewById(R.id.forgot_btn));

        YoYo.with(Techniques.BounceIn).duration(3000).playOn(findViewById(R.id.textView));

        btn = findViewById(R.id.forgot_btn);
        email = findViewById(R.id.Email_forgot);
        requestQueue = Volley.newRequestQueue(this);


        webView = (WebView) findViewById(R.id.Password_resend);
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void Back(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

//    private void verify(){
//        //link
//        String url = IP.toString() + "Extra/Info/Password.php?email=" + email.getText().toString();
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    JSONArray jsonArray = response.getJSONArray("Verified_Status");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject employee = jsonArray.getJSONObject(i);
//
//                        String Status  = employee.getString("Status");
//                        String Password  = employee.getString("Password");
//
//                        if (Status.toString().equals("Success")){
//                            if (!Password.toString().matches("")){
//                                webView.loadUrl(IP.toString() + "resturant/Verification/verify/forgot.php?email=" + email.getText().toString()  + "&password=" + Password.toString());
//                            }else {
//                                final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Email not found.!", Snackbar.LENGTH_INDEFINITE);
//                                //   snackBar.setActionTextColor(Color.WHITE);
//                                snackBar.setActionTextColor(Color.parseColor("#6672FF"));
//                                snackBar.setAction("Got It", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        // Call your action method here
//                                        snackBar.dismiss();
//
//                                    }
//                                });
//                                snackBar.show();
//                            }
//
//                        }
//                        if (Status.toString().equals("Error")){
//                            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Something went wrong.!", Snackbar.LENGTH_INDEFINITE);
//                            //   snackBar.setActionTextColor(Color.WHITE);
//                            snackBar.setActionTextColor(Color.parseColor("#6672FF"));
//                            snackBar.setAction("Got It", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    // Call your action method here
//                                    snackBar.dismiss();
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
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//        requestQueue.add(request);
//
//
//    }
//    private void randoms(){
//        int min = 100000;
//        int max = 100000000;
//
//        //Generate random int value from 50 to 100
//        //   System.out.println("Random value in int from "+min+" to "+max+ ":");
//        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
//        //  System.out.println(random_int);
//        rand = String.valueOf(random_int);
//    }

    public void check(View view) {

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        btn.setEnabled(false);
        //  verify();
        webView.loadUrl(IP.toString() + "Talaka/Users/Verification/verify/forgot.php?email=" + email.getText().toString());
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





        if(html.toString().contains("Authentication succeeded")){

            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "new password has been reset, Check your email", Snackbar.LENGTH_INDEFINITE);
            //   snackBar.setActionTextColor(Color.WHITE);
            snackBar.setActionTextColor(Color.parseColor("#6672FF"));
            snackBar.setAction("Got it", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call your action method here
                    snackBar.dismiss();
                }
            });
            snackBar.show();


        }else {
            btn.setEnabled(false);
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