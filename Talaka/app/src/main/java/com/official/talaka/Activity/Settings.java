package com.official.talaka.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.official.talaka.R;
import com.official.talaka.Splash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Settings extends AppCompatActivity {
    private View mTarget;
    private Handler handler = new Handler();
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    String Email,Username;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    String IP = "http://192.168.1.100/";
    View view_Setting_Gender,view_Setting_Messenger,view_Setting_Telegram,view_Setting_Username,view_Setting_Mobile;
    SharedPreferences preferences;
    String mobile;
    private AdView mAdView;
    TextView flags;
    String mob_1,mob_2;
    int country_numbers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mTarget = findViewById(R.id.imgUser);
        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(mTarget);// after start,just click mTarget view, rope is not init
        handler.postDelayed(runnable, 5000);

        requestQueue = Volley.newRequestQueue(this);

        get_User_Info();

        mAdView = findViewById(R.id.ads_settings);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        load_ads_banner();

        Username = preferences.getString("Username", "");
        Email = preferences.getString("Email", "");
    }
    private void load_ads_banner() {

//        for(int i=0;i<3;i++) {

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

// TODO: Add adView to your view hierarchy.
//        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            YoYo.with(Techniques.BounceInDown).duration(3000).playOn(mTarget);// after start,just click mTarget view, rope is not init
            handler.postDelayed(runnable, 5000);
        }
    };
    private void get_User_Info() {

        Email = preferences.getString("Email", "");

        //link
        String url = IP.toString() + "Talaka/Users/Acc/Check.php?email=" + Email.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Info");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);

                        int id = employee.getInt("ID");
                        String username = employee.getString("Username");
                        String lvl = employee.getString("Lvl");
                        String activation = employee.getString("Activation");
                        String email = employee.getString("Email");
                        String rate = employee.getString("Rate");
                        String language = employee.getString("Language");
                        String respect = employee.getString("Respect");
                        String claim_call = employee.getString("Claim_Call");
                        String claim_recieve = employee.getString("Claim_Recieve");
                        String genders_required = employee.getString("Genders_Required");
                        mobile = employee.getString("Mobile");
                        String messenger = employee.getString("Messenger");
                        String telegram = employee.getString("Telegram");
                        String call_history = employee.getString("Call_History");
                        String recieve_history = employee.getString("Recieve_History");

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("Mobile",mobile.toString());
                        editor.apply();

                        TextView id_txt =  (TextView) findViewById(R.id.id_Settings);
                        id_txt.setText(String.valueOf(id));

                        TextView email_txt =  (TextView) findViewById(R.id.email_Settings);
                        email_txt.setText(email.toString());

                        TextView username_txt =  (TextView) findViewById(R.id.username_Settings);
                        username_txt.setText(username.toString());

                        TextView lvl_txt =  (TextView) findViewById(R.id.lvl_Settings);
                        lvl_txt.setText(lvl.toString());

                        TextView activation_txt =  (TextView) findViewById(R.id.activation_Settings);
                        activation_txt.setText(activation.toString());

                        TextView totalcalls_txt =  (TextView) findViewById(R.id.totalcalls_Settings);
                        totalcalls_txt.setText(claim_call.toString());

                        TextView totalrecieves_txt =  (TextView) findViewById(R.id.totalrecieves_Settings);
                        totalrecieves_txt.setText(claim_recieve.toString());

                        TextView genders_txt =  (TextView) findViewById(R.id.genders_Settings);
                        genders_txt.setText(genders_required.toString());

                        TextView mobile_txt =  (TextView) findViewById(R.id.mobile_Settings);

                        if(mobile.toString().matches("null")){
                            mobile_txt.setText(mobile.toString());
                        }else {
                            mobile_txt.setText("+" + mobile.toString());
                        }

                        TextView messenger_txt =  (TextView) findViewById(R.id.messenger_Settings);
                        messenger_txt.setText(messenger.toString());

                        TextView telegram_txt =  (TextView) findViewById(R.id.telegram_Settings);
                        telegram_txt.setText(telegram.toString());

                        ///////////////
                        TextView total_rate =  (TextView) findViewById(R.id.totalrate_Settings);

                        TextView rate_txt =  (TextView) findViewById(R.id.rate_Settings);
                        TextView language_txt =  (TextView) findViewById(R.id.language_Settings);
                        TextView respect_txt =  (TextView) findViewById(R.id.respect_Settings);



                        total_rate.setText( rate.toString());

                        if (rate.toString().equals("0")){
                            rate_txt.setText("My Rate : (0/5)");
                            language_txt.setText("(0/5)");
                            respect_txt.setText("(0/5)");
                        }else {
                            DecimalFormat decimalFormat;
                            decimalFormat = new DecimalFormat("#.#");

                            double valueTwo,valueThree,valuedouble;

                            valueTwo = Double.parseDouble(language.toString()) / Double.parseDouble(rate.toString());
                            valueThree = Double.parseDouble(respect.toString()) / Double.parseDouble(rate.toString());

                            valuedouble =valueTwo+valueThree;
                            rate_txt.setText("My Rate : (" + decimalFormat.format(valuedouble/2)  + "/5)");

                            language_txt.setText("(" + decimalFormat.format(valueTwo) +"/5)");
                            respect_txt.setText("(" + decimalFormat.format(valueThree) +"/5)");
                        }



                        ///////////////////////////////////////



                        //rate_txt.setText("Rate : (" + rate.toString() + "/5)");
                      //  language_txt.setText(language.toString());
                       // respect_txt.setText(respect.toString());


                        ///////////////



                        TextView call_history_txt =  (TextView) findViewById(R.id.Call_History_Settings);
                        TextView recieve_history_txt =  (TextView) findViewById(R.id.Recieve_History_Settings);


                        if (call_history.toString().matches("")){
                            call_history_txt.setText("Empty");
                        }else {
                            if (!call_history.toString().matches("")){
                                call_history_txt.setText("Calls History Found");
                            }
                        }

                        if (recieve_history.toString().matches("")){
                            recieve_history_txt.setText("Empty");
                        }else {
                            if (!recieve_history.toString().matches("")){
                                recieve_history_txt.setText("Recieve History Found");
                            }
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
    @SuppressLint("ResourceType")
    private void showWarningDialog(){
        builder = new AlertDialog.Builder(Settings.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(Settings.this).inflate(
                R.xml.sup_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(false);
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Logout");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Are you sure.!");
        ((TextView) view.findViewById(R.id.buttonNo)).setText("No");
        ((TextView) view.findViewById(R.id.buttonYes)).setText("I'm Sure");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.close);

        alertDialog = builder.create();

        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //settings.edit().remove("KeyName").commit(); specific key
                preferences.edit().clear().commit();


                alertDialog.dismiss();
                Intent ii = new Intent(Settings.this, Splash.class);
                startActivity(ii);
                finish();

            }
        });

        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                // Toast.makeText(Home.this, "No", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.imageIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                // Toast.makeText(Home.this, "No", Toast.LENGTH_SHORT).show();
            }
        });

        if (alertDialog.getWindow()!= null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }
    @SuppressLint("ResourceType")
    private void showGenderDialog(){
        builder = new AlertDialog.Builder(Settings.this, R.style.AlertDialogTheme);
        view_Setting_Gender = LayoutInflater.from(Settings.this).inflate(
                R.xml.genders_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(true);
        builder.setView(view_Setting_Gender);

        alertDialog = builder.create();

        view_Setting_Gender.findViewById(R.id.genders_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_Gender1();
                alertDialog.dismiss();

            }
        });

        if (alertDialog.getWindow()!= null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

///////



        ////////////
        Spinner_install1();
        Spinner_install2();
    }
    @SuppressLint("ResourceType")
    private void Spinner_install1(){
        final Spinner spinner = (Spinner) view_Setting_Gender.findViewById(R.id.spin_settings);
        // Initializing a String Array
        String[] plants = new String[]{
                "Select gender operation",
                "Both Genders",
                "Male",
                "Female"
        };

        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.xml.item_spinner,plantsList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.xml.item_spinner);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }
    @SuppressLint("ResourceType")
    private void Spinner_install2(){
        final Spinner spinner = (Spinner) view_Setting_Gender.findViewById(R.id.spin_settings2);
        // Initializing a String Array
        String[] plants = new String[]{
                "Select your gender",
                "Male",
                "Female"
        };

        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.xml.item_spinner,plantsList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.xml.item_spinner);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }
    @SuppressLint("ResourceType")
    private void showMessengerDialog(){
        builder = new AlertDialog.Builder(Settings.this, R.style.AlertDialogTheme);
        view_Setting_Messenger = LayoutInflater.from(Settings.this).inflate(
                R.xml.messenger_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(true);
        builder.setView(view_Setting_Messenger);

        alertDialog = builder.create();

        view_Setting_Messenger.findViewById(R.id.Messenger_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText editText = (EditText) view_Setting_Messenger.findViewById(R.id.edit_messenger_settings);

                if (editText.getText().toString().contains("https://m.me/")){
                    change_Messenger();
                }else {

                    Toast.makeText
                            (getApplicationContext(), "invalid request must contains \nhttps://m.me/your_username", Toast.LENGTH_LONG)
                            .show();

                }

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
    @SuppressLint("ResourceType")
    private void showTelegramDialog(){
        builder = new AlertDialog.Builder(Settings.this, R.style.AlertDialogTheme);
        view_Setting_Telegram = LayoutInflater.from(Settings.this).inflate(
                R.xml.telegram_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(true);
        builder.setView(view_Setting_Telegram);

        alertDialog = builder.create();

        view_Setting_Telegram.findViewById(R.id.Telegram_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText editText = (EditText) view_Setting_Telegram.findViewById(R.id.edit_telegram_settings);

                if (editText.getText().toString().contains("@")){
                    change_Telegram();
                }else {

                    Toast.makeText
                            (getApplicationContext(), "invalid request must contains \n@YourUsername", Toast.LENGTH_LONG)
                            .show();

                }

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
    @SuppressLint("ResourceType")
    private void showUsernameDialog(){
        builder = new AlertDialog.Builder(Settings.this, R.style.AlertDialogTheme);
        view_Setting_Username = LayoutInflater.from(Settings.this).inflate(
                R.xml.username_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(true);
        builder.setView(view_Setting_Username);

        alertDialog = builder.create();
        final EditText editText = (EditText) view_Setting_Username.findViewById(R.id.edit_telegram_settings);
        TextView genders_txt =  (TextView) findViewById(R.id.username_Settings);
        editText.setText(genders_txt.getText().toString());
        view_Setting_Username.findViewById(R.id.Telegram_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!editText.getText().toString().matches("")){
                    change_Username();
                }else {

                    Toast.makeText
                                    (getApplicationContext(), "invalid request", Toast.LENGTH_LONG)
                            .show();

                }

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
    @SuppressLint("ResourceType")
    private void showMobileDialog(){
        builder = new AlertDialog.Builder(Settings.this, R.style.AlertDialogTheme);
        view_Setting_Mobile = LayoutInflater.from(Settings.this).inflate(
                R.xml.mobile_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(true);
        builder.setView(view_Setting_Mobile);

        alertDialog = builder.create();
        final EditText editText = (EditText) view_Setting_Mobile.findViewById(R.id.edit_telegram_settings);
        TextView genders_txt =  (TextView) findViewById(R.id.mobile_Settings);
        editText.setText(mobile.toString().replace("+",""));

        view_Setting_Mobile.findViewById(R.id.Telegram_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!editText.getText().toString().matches("") && !editText.getText().toString().matches("null")){
                    mob_2 = "";
                    mob_2 = editText.getText().toString().substring(1);
                    change_mobile();
                }else {

                    Toast.makeText
                                    (getApplicationContext(), "invalid request", Toast.LENGTH_LONG)
                            .show();

                }

                alertDialog.dismiss();

            }
        });

        if (alertDialog.getWindow()!= null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

///////


        flags = view_Setting_Mobile.findViewById(R.id.textView_selectedCountry);
        mob_1 = "";
        mob_1 = getStringBetweenTwoCharacters(flags.getText().toString().toString(),"+","");
        country_numbers = 11;

        flags.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


                switch(s.toString()) {
                    case "+20":
                        country_numbers = 11;
                        mob_1 = "";
                        mob_1 = getStringBetweenTwoCharacters(s.toString(),"+","");
                        break;
                    default:
                        country_numbers = 11;
                        // code block
                }
            }
        });
    }
    public void logout(View view) {
        showWarningDialog();
    }
    public void back(View view) {
        handler.removeCallbacks(runnable);
        Intent i = new Intent(Settings.this, Home.class);
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
    public void Genders_Change(View view) {
        showGenderDialog();
    }
    public void Messenger_Change(View view) {
        showMessengerDialog();
    }
    public void Telegram_Change(View view) {
        showTelegramDialog();
    }
    private void change_Gender1() {

       final Spinner spinner = (Spinner) view_Setting_Gender.findViewById(R.id.spin_settings);// spinner.getSelectedItem().toString()
        //link
        if (!spinner.getSelectedItem().toString().matches("Select gender operation")){
            String url = IP.toString() + "Talaka/Users/Update/Genders_Req.php?email=" + Email.toString() + "&gender=" + spinner.getSelectedItem().toString() ;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);
                            String status = employee.getString("Status");

                            if (status.toString().equals("Succesfully")){
                                String value = employee.getString("Value");
                                TextView genders_txt =  (TextView) findViewById(R.id.genders_Settings);
                                genders_txt.setText(value.toString());
                               // Toast.makeText(Settings.this, "Updated : " + value.toString(), Toast.LENGTH_SHORT).show();
                                change_Gender2();
                            } else {
                                Toast.makeText(Settings.this, "something went worng.!" , Toast.LENGTH_SHORT).show();
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
              Toast.makeText
                    (getApplicationContext(), "invalid selection", Toast.LENGTH_SHORT)
                  .show();
        }
    }
    private void change_Gender2() {

        final Spinner spinner = (Spinner) view_Setting_Gender.findViewById(R.id.spin_settings2);// spinner.getSelectedItem().toString()
        //link
        if (!spinner.getSelectedItem().toString().matches("Select your gender")){
            String url = IP.toString() + "Talaka/Users/Update/Genders.php?email=" + Email.toString() + "&gender=" + spinner.getSelectedItem().toString() ;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);
                            String status = employee.getString("Status");

                            if (status.toString().equals("Succesfully")){
                                String value = employee.getString("Value");
                                TextView genders_txt =  (TextView) findViewById(R.id.genders_Settings);
                                genders_txt.setText(value.toString());
                                Toast.makeText(Settings.this, "Updated : " + value.toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Settings.this, "something went worng.!" , Toast.LENGTH_SHORT).show();
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
            Toast.makeText
                            (getApplicationContext(), "Updated Required Gender Operation. Successfully\ninvalid selection [your gender]", Toast.LENGTH_LONG)
                    .show();
        }
    }
    private void change_Messenger() {
        //    View view = LayoutInflater.from(Settings.this).inflate(
        //          R.layout.genders_dialog,
        //          (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        //    );

        final EditText editText = (EditText) view_Setting_Messenger.findViewById(R.id.edit_messenger_settings);

            String url = IP.toString() + "alaka/Users/Update/Messenger_Req.php?email=" + Email.toString() + "&messenger=" + editText.getText().toString();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);
                            String status = employee.getString("Status");

                            if (status.toString().equals("Succesfully")){
                                String value = employee.getString("Value");
                                TextView genders_txt =  (TextView) findViewById(R.id.messenger_Settings);
                                genders_txt.setText(value.toString());
                                Toast.makeText(Settings.this, "Updated : " + value.toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Settings.this, "something went worng.!" , Toast.LENGTH_SHORT).show();
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
    private void change_Telegram() {

        //    View view = LayoutInflater.from(Settings.this).inflate(
        //          R.layout.genders_dialog,
        //          (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        //    );

        final EditText editText = (EditText) view_Setting_Telegram.findViewById(R.id.edit_telegram_settings);

        String url = IP.toString() + "Talaka/Users/Update/Telegram_Req.php?email=" + Email.toString() + "&telegram=" + editText.getText().toString().replace("@","");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        String status = employee.getString("Status");

                        if (status.toString().equals("Succesfully")){
                            String value = employee.getString("Value");
                            TextView genders_txt =  (TextView) findViewById(R.id.telegram_Settings);
                            genders_txt.setText(value.toString());
                            Toast.makeText(Settings.this, "Updated : " + value.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Settings.this, "something went worng.!" , Toast.LENGTH_SHORT).show();
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
    private void change_Username() {

        final EditText editText = (EditText) view_Setting_Username.findViewById(R.id.edit_telegram_settings);

        String url = IP.toString() + "Talaka/Users/Update/Username.php?email=" + Email.toString() + "&username=" + editText.getText().toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        String status = employee.getString("Status");
                        TextView genders_txt =  (TextView) findViewById(R.id.username_Settings);
                        if (status.toString().equals("Succesfully")){
                            String value = employee.getString("Value");
                            genders_txt.setText(value.toString());
                            Toast.makeText(Settings.this, "Updated : " + value.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            if (status.toString().equals("Duplicated")){
                                Toast.makeText(Settings.this, "Use another username.!" , Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Settings.this, "something went worng.!" , Toast.LENGTH_SHORT).show();
                            }
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
    private void change_mobile() {


        final EditText editText = (EditText) view_Setting_Mobile.findViewById(R.id.edit_telegram_settings);

        if (!editText.getText().toString().equals("") && !editText.getText().toString().contains("null")  && !mob_1.toString().equals("") && !mob_2.toString().equals("") && editText.getText().toString().length() == country_numbers)  {

            // String url = IP.toString() + "Talaka/Users/Update/Mobile.php?email=" + Email.toString() + "&mobile=" + editText.getText().toString();
            String url = IP.toString() + "Talaka/Users/Update/Mobile.php?email=" + Email.toString() + "&mobile=" + mob_1.toString() + mob_2.toString();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);
                            String status = employee.getString("Status");
                            TextView genders_txt =  (TextView) findViewById(R.id.mobile_Settings);
                            if (status.toString().equals("Succesfully")){
                                String value = employee.getString("Value");
                                genders_txt.setText("+" + value.toString());
                                Toast.makeText(Settings.this, "Updated : +" + value.toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Settings.this, "something went worng.!" , Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Settings.this, "Invalid Info..!", Toast.LENGTH_SHORT).show();
        }



    }
    public void Call_History_Change(View view) {

        TextView call_history_txt =  (TextView) findViewById(R.id.Call_History_Settings);

        if (!call_history_txt.getText().toString().equals("Empty") && !call_history_txt.getText().toString().equals("Loading.!")){


            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Log","Call");
            editor.apply();

            Intent ii = new Intent(Settings.this, History_Log.class);
            startActivity(ii);
            finish();

        }

    }
    public void Recieve_History_Change(View view) {

        TextView recieve_history_txt =  (TextView) findViewById(R.id.Recieve_History_Settings);


        if (!recieve_history_txt.getText().toString().equals("Empty") && !recieve_history_txt.getText().toString().equals("Loading.!")) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Log","Recieve");
            editor.apply();

            Intent ii = new Intent(Settings.this, History_Log.class);
            startActivity(ii);
            finish();

        }



    }
    public void Username_change(View view) {
        TextView textView = findViewById(R.id.username_Settings);
        if (textView.getText().toString().matches("") && textView.getText().toString().matches("null")){
            showUsernameDialog();
        }}
    public void Mobile_Change(View view) {
        showMobileDialog();
    }
    public static String getStringBetweenTwoCharacters(String input, String to, String from)
    {
        return input.substring(input.indexOf(to), input.lastIndexOf(from));
    }
}