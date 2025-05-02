package com.official.talaka.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.official.talaka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Verification extends AppCompatActivity {
    String token,email;
    SharedPreferences sharedPreferences;
    String IP = "http://192.168.1.100/";
    RequestQueue requestQueue;
    ImageView btn;
    EditText code;
    private WebView webView;
    LinearLayout resend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        requestQueue = Volley.newRequestQueue(this);

        resend = (LinearLayout) findViewById(R.id.resend_code);
        resend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                resend.setEnabled(false);
                btn.setEnabled(false);
                code.setEnabled(false);

                Toast.makeText(Verification.this, "Please Wait..!", Toast.LENGTH_SHORT).show();

                webView.loadUrl(IP.toString() + "Talaka/Users/Verification/verify/code.php?email=" + email.toString()  + "&token=" + code.getText().toString());

            }
        });

        token = sharedPreferences.getString("Token", "");
        email = sharedPreferences.getString("Email", "");
        btn = findViewById(R.id.verify_btn);
        code = findViewById(R.id.editTextTextPersonName);


        webView = (WebView) findViewById(R.id.code_resend);
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
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
    private void verify(){
        //link

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }


        String url = IP.toString() + "Talaka/Users/Info/Verify.php?email=" + email.toString() + "&token=" +  code.getText().toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("Verified_Status");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);

                        String Status  = employee.getString("Status");


                        if (Status.toString().equals("Success")){

                            Toast.makeText(Verification.this, "Thanks.!", Toast.LENGTH_LONG).show();
                            Intent ii = new Intent(Verification.this, Login.class);
                            startActivity(ii);

                        }
                        if (Status.toString().equals("Error")){


                            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Something went wrong.!", Snackbar.LENGTH_INDEFINITE);
                            //   snackBar.setActionTextColor(Color.WHITE);
                            snackBar.setActionTextColor(Color.parseColor("#6672FF"));
                            snackBar.setAction("Got It", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackBar.dismiss();
                                }
                            });
                            snackBar.show();
                            return;
                        }

                        if (Status.toString().equals("Wrong")){


                            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Wrong Code.!", Snackbar.LENGTH_INDEFINITE);
                            //   snackBar.setActionTextColor(Color.WHITE);
                            snackBar.setActionTextColor(Color.parseColor("#6672FF"));
                            snackBar.setAction("Got it", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackBar.dismiss();
                                }
                            });
                            snackBar.show();
                            return;
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
    }
    public void viewSource() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(
                "javascript:this.document.location.href = 'source://' + encodeURI(document.documentElement.outerHTML);");
    }

    public void verify(View view) {
        verify();
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

            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Check your email : \n" + email.toString(), Snackbar.LENGTH_INDEFINITE);
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
            resend.setEnabled(false);
            btn.setEnabled(false);
            code.setEnabled(false);

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