package com.official.talaka.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.material.snackbar.Snackbar;
import com.official.talaka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Login extends AppCompatActivity {

    private Handler handler = new Handler();
    private View mTarget,mTarget2,mTarget3,mTarget4,mTarget5;
    EditText email,password;
    RequestQueue requestQueue;
    String IP = "http://192.168.1.100/";
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        requestQueue = Volley.newRequestQueue(this);

        YoYo.with(Techniques.BounceIn).duration(3000).playOn(findViewById(R.id.welcome_label));
        YoYo.with(Techniques.BounceInLeft).duration(3000).playOn(findViewById(R.id.email_login));
        YoYo.with(Techniques.BounceInRight).duration(3000).playOn(findViewById(R.id.password_login));
        YoYo.with(Techniques.ZoomInDown).duration(3000).playOn(findViewById(R.id.login_btn));
        YoYo.with(Techniques.BounceIn).duration(3000).playOn(findViewById(R.id.login_bottom));

        email = (EditText) findViewById(R.id.email_login);
        password = (EditText) findViewById(R.id.password_login);


        final ImageView button = (ImageView) findViewById(R.id.login_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

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
                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    verify();
                }

            }
        });

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        Starting();


    }

    private void Starting(){
        String name = preferences.getString("Email", "");
        String pass = preferences.getString("Password", "");


        if(!name.equalsIgnoreCase("") || !pass.equalsIgnoreCase(""))
        {
            email.setText(name.toString());
            password.setText(pass.toString());
//            Intent intent = new Intent(this, Home.class);
//            startActivity(intent);
        }

    }
    public void Register(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }


    private void verify(){
        //link
        String url = IP.toString() + "Talaka/Users/Acc/Check.php?email=" + email.getText().toString() + "&password=" +password.getText().toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("Info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);

                        String Status  = employee.getString("Status");


                        if (Status.toString().equals("Done")){

                            String Email  = employee.getString("Email");
                            String Verify = employee.getString("Verify");
                            String Login = employee.getString("Login");
                            if(email.getText().toString().equals(Email.toString()) &&
                                    Login.toString().matches("Yes"))
                            {

                                if(Verify.toString().equals("Yes")){

                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("Login","Yes");
                                    editor.putString("Email",email.getText().toString());
                                    editor.apply();

                                    Intent ii = new Intent(Login.this, Home.class);
                                    startActivity(ii);

                                 //   Toast.makeText(Login.this, "Loged", Toast.LENGTH_LONG).show();

                                }else {
                                    Toast.makeText(Login.this, "Needed Verify, Check your email", Toast.LENGTH_LONG).show();
                                    Intent ii = new Intent(Login.this, Verification.class);
                                    startActivity(ii);
                                }


                            }else {
                                final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Invalid Email/Password.!", Snackbar.LENGTH_INDEFINITE);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}