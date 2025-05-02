package com.official.talaka.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
//import com.github.zagum.speechrecognitionview.RecognitionProgressView;
//import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;
//import com.official.talaka.More.HoldListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.official.talaka.More.CountDownTimer;
import com.official.talaka.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

//                recognitionProgressView.stop();
//                        recognitionProgressView.play();
public class Assess extends AppCompatActivity implements CountDownTimer.OnCountDownListener {
    private String Email,Username,Lvl,urii;
    String IP = "http://192.168.1.100/";
    RequestQueue requestQueue;
    MediaPlayer mediaPlayer;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    SharedPreferences preferences;

    MediaPlayer mp;
    ///////

    int Questions_I_Reached;
    String Question_Type,The_Question;
    int Currently_Que;
    String change_que_type;
    load_media myAsyncTask;
    private String answer_1,answer_2,answer_3,answer_4,Correct_Answer;
    TextView answer_btn1,answer_btn2,answer_btn3,answer_btn4,timer;
    String my_rec,My_Rate,Server_Rate,Full_Answer;
    String First,Second;
    Boolean play_speaker;
    TextView question,total_question,rate_result,streak_result;
    Intent speechRecognizerIntent;
    SpeechRecognizer speechRecognizer;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1;
    private final CountDownTimer CountDownTimer = new CountDownTimer(1, 0, 1, this);
    ImageView mic_speak,listner;
    String Right_Questions_count,image_src;
    String Status;
    private AdView mAdView;
    Boolean Speaker_Check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assess);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        requestQueue = Volley.newRequestQueue(this);
        Username = preferences.getString("Username", "");
        Email = preferences.getString("Email", "");
        Lvl = preferences.getString("Lvl", "");
       // change_que_type = preferences.getString("Type_Que_navi", "");
        change_que_type = "";
        answer_btn1 = findViewById(R.id.answer_1);
        answer_btn2 = findViewById(R.id.answer_2);
        answer_btn3 = findViewById(R.id.answer_3);
        answer_btn4 = findViewById(R.id.answer_4);
         question = findViewById(R.id.assess_question);
         total_question = findViewById(R.id.last_question);
         rate_result = findViewById(R.id.question_rate);
         streak_result = findViewById(R.id.streak_rate);
         timer = findViewById(R.id.timer);
         mic_speak = findViewById(R.id.Mic_assess);
        listner = findViewById(R.id.speak_assess);
       // recognitionProgressView = findViewById(R.id.recognition_view);

        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra("calling_package", Assess.this.getPackageName());
        speechRecognizerIntent.putExtra("android.speech.extra.DICTATION_MODE", true);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
        play_speaker = false;
        Speaker_Check = false;
        mic_speak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (!Speaker_Check){
                    Toast.makeText(Assess.this, "Please tap the speaker sign and listen to the question before you record.", Toast.LENGTH_LONG).show();
                }else{
                    switch (motionEvent.getAction()){

                        case MotionEvent.ACTION_DOWN:
                            mic_speak.setImageResource(R.drawable.mic2);
                            listner.setEnabled(false);
                            startRecognition();
                            return true;


                        case MotionEvent.ACTION_UP:
                            mic_speak.setImageResource(R.drawable.mic);
                            listner.setEnabled(true);
                            stopRecognition();
                            return true;

                    }
                }



                return false;
            }
        });

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                mic_speak.setImageResource(R.drawable.mic);
                listner.setEnabled(true);
                stopRecognition();
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

                mic_speak.setImageResource(R.drawable.mic);
                listner.setEnabled(true);
                stopRecognition();

                ArrayList<String> data = bundle.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                my_rec = data.get(0);
                my_rate();
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

       // preferences.edit().remove("Currently_Que").commit();

    }

    @Override
    public void onCountDownActive(String time) {

        long ii = CountDownTimer.getMinutesTillCountDown();
        int kk = (int) ii;

        long i = CountDownTimer.getSecondsTillCountDown();
        int k = (int) i;

        timer.post(() -> timer.setText(time));

        if (kk == 01) {
            timer.setTextColor(Color.parseColor("#2bc62b")); //c8ce0e
        }else {
                if (k > 15) {
                timer.setTextColor(Color.parseColor("#2bc62b")); //c8ce0e
            } else {
                timer.setTextColor(Color.parseColor("#cc2516"));
        }
        }


        mAdView = findViewById(R.id.ads_assess);
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
    public void onCountDownFinished() {

        timer.post(() -> {

            timer.setTextColor(Color.parseColor("#999999"));
            timer.setText("00:00");
            Wrong();
            Update();
            CountDownTimer.reset();



        });



      //  timer.setText("00:00");



//
//       Update();


    //    CountDownTimer.reset();



//        textView.post(() -> {
//            textView.setText("Finished");
//            start.setEnabled(true);
//            resume.setEnabled(false);
//        });


    }


    @Override
    public void onResume(){
        super.onResume();
        timer.setTextColor(Color.parseColor("#999999"));
        timer.setText("");
        Checking1();

        TextView streak = findViewById(R.id.streak_rate);
        streak.setText("Undefined");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    protected void onUserLeaveHint()
//    {
//        Toast.makeText(Assess.this, "lol", Toast.LENGTH_SHORT).show();
//        super.onUserLeaveHint();
//    }


    public boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPause() {
        if (isApplicationSentToBackground(this)){
            // Do what you want to do on detecting Home Key being Pressed
            CountDownTimer.reset();
        }
        super.onPause();
    }


    public void Assess_Back(View view) {
       // handler.removeCallbacks(runnable);



        Cancel();

        Intent i = new Intent(Assess.this, Home.class);
        startActivity(i);
        finish();
    }


    private void Cancel(){
        if (myAsyncTask != null){
            myAsyncTask.cancel(true);
        }

        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    @Override
    protected void onDestroy() {
        Cancel();
        super.onDestroy();
    }

    private void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);
    }

    public void Listen(View view) {

        if (!play_speaker){

            Speaker_Check = true;
            play_speaker = true;

            ImageView imageView1 = findViewById(R.id.speak_assess);
            ImageView imageView2 = findViewById(R.id.Mic_assess);

            imageView1.setEnabled(true);
            imageView2.setEnabled(false);

            imageView1.setImageResource(R.drawable.speak2);


            myAsyncTask = new load_media();
            myAsyncTask.execute();
        } else {

            play_speaker = false;

            ImageView imageView = findViewById(R.id.speak_assess);
            imageView.setImageResource(R.drawable.speak);
            imageView.setEnabled(true);


            ImageView imageView00 = findViewById(R.id.Mic_assess);
            imageView00.setEnabled(true);

            Cancel();
        }



    }

    private void startRecognition() {

        if (ContextCompat.checkSelfPermission(Assess.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            speechRecognizer.startListening(speechRecognizerIntent);
        }


    }
    private void stopRecognition() {
        speechRecognizer.stopListening();
    }

    private void Checking1(){
        First = "0";
        Second = "0";

       // http://192.168.1.100/Talaka/Users/Assess/Check.php?lvl=1&id=1&username=soldivano
        String url = IP + "Talaka/Users/Acc/Check.php?email=" + Email.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                TextView Cur_Question_Num = findViewById(R.id.assess_que_number);

                try {
                    JSONArray jsonArray2 = response.getJSONArray("Info");



                    for (int ii = 0; ii < jsonArray2.length(); ii++) {
                        JSONObject employeee = jsonArray2.getJSONObject(ii);

                        Questions_I_Reached = Integer.valueOf(employeee.getString("Assess_Question"));

                    //    Toast.makeText(Assess.this,preferences.getString("Currently_Que", ""), Toast.LENGTH_SHORT).show();

                        if(preferences.getString("Currently_Que", "").equalsIgnoreCase(""))
                        {
                        Currently_Que = Questions_I_Reached + 1;

                        Cur_Question_Num.setText("Question : " + String.valueOf(Currently_Que));
                            preferences.edit().remove("Currently_Que").commit();
                            Checking2();
                        }else {

                            /////

                            if(preferences.getString("Currently_Que", "").equals("Next")){
                                Currently_Que = Integer.valueOf(Cur_Question_Num.getText().toString().replace("Question : ",""))+1;
                                Cur_Question_Num.setText("Question : " + String.valueOf(Currently_Que));
                                preferences.edit().remove("Currently_Que").commit();
                                Checking2();
                            } else {
                                if(preferences.getString("Currently_Que", "").equals("Previous")){

                                   //   Toast.makeText(Assess.this,Cur_Question_Num.getText().toString(), Toast.LENGTH_SHORT).show();

                                 //   Toast.makeText(Assess.this,String.valueOf(Currently_Que).toString(), Toast.LENGTH_SHORT).show();


                                    //    Currently_Que = Integer.valueOf(Cur_Question_Num.getText().toString().replace("Question : ",""))-1;
                                    //   Cur_Question_Num.setText("Question : " + String.valueOf(Currently_Que));
                                    //   preferences.edit().remove("Currently_Que").commit();
                                    //   Checking2();


                                    Currently_Que = Integer.valueOf(Cur_Question_Num.getText().toString().replace("Question : ",""))-1;
                                    Cur_Question_Num.setText("Question : " + String.valueOf(Currently_Que));
                                    preferences.edit().remove("Currently_Que").commit();
                                    Checking2();

                                }else {
                                  //  Toast.makeText(Assess.this,preferences.getString("Currently_Que", ""), Toast.LENGTH_SHORT).show();
                                    Currently_Que = Integer.valueOf(preferences.getString("Currently_Que", ""));
                                    Cur_Question_Num.setText("Question : " + String.valueOf(Currently_Que));
                                    preferences.edit().remove("Currently_Que").commit();
                                    Checking2();
                                }
                            }

                            /////



                        }

                    }

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


    private void Checking2(){

        String url = IP + "Talaka/Users/Assess/Check.php?id=" + Currently_Que + "&lvl=" + Lvl + "&username=" + Username;
     // Toast.makeText(Assess.this, IP.toString(), Toast.LENGTH_SHORT).show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONArray jsonArray2 = response.getJSONArray("Info");



                    for (int ii = 0; ii < jsonArray2.length(); ii++) {
                        JSONObject employeee = jsonArray2.getJSONObject(ii);

                        Status = employeee.getString("Status");

                        if (!Status.toString().equals("No_Data")){
                            Question_Type = employeee.getString("Type");
                            The_Question = employeee.getString("Subject");
                            answer_1 = employeee.getString("Answer_A");
                            answer_2 = employeee.getString("Answer_B");
                            answer_3 = employeee.getString("Answer_C");
                            answer_4 = employeee.getString("Answer_D");
                            urii = employeee.getString("Audio");
                            Full_Answer = employeee.getString("Full_Answer");
                            Server_Rate = employeee.getString("Success_Rate");

                            //Integer num = Integer.parseInt(employeee.getString("Assess_Question"))+ 1;
                            Integer num;
                            num = Integer.parseInt(employeee.getString("Assess_Question")) + 1;
                            total_question.setText(String.valueOf(num));
//                            if (employeee.getString("Assess_Question").equals("-1")){
//                                num = Integer.parseInt(employeee.getString("Assess_Question")) + 1;
//                                total_question.setText(String.valueOf(num));
//                            }else {
//                                num = Integer.parseInt(employeee.getString("Assess_Question"));
//                                total_question.setText(String.valueOf(num));
//                            }




                            Right_Questions_count = employeee.getString("Assess_Question_Right");
                            image_src = employeee.getString("Url_Source");

                            Checking3();
                            return;
                        }else {
                            Checking3();
                        }
                    }

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

    private void Checking3(){

        RelativeLayout answer_layout = findViewById(R.id.answers_layout);
        LinearLayout Lister_Speak = findViewById(R.id.linear_mic_speak);
        LinearLayout Confirm_linear = findViewById(R.id.linear_confirm);


        ImageView confirmer = findViewById(R.id.Checking);

        ImageView next_que = findViewById(R.id.que_next);
        ImageView prev_que = findViewById(R.id.que_back);

        if (Status.toString().equals("No_Data")){
            RelativeLayout relativeLayout = findViewById(R.id.answers_layout);
            relativeLayout.setVisibility(View.GONE);

            total_question.setText(String.valueOf(Currently_Que - 1));

            question.setText("No More Questions.\nat this time");

            answer_layout.setVisibility(View.GONE);

            ImageView quiz_img = findViewById(R.id.quiz_img);
            quiz_img.setImageDrawable(getDrawable(R.drawable.image));

            next_que.setImageResource(R.drawable.next_que_2);
            next_que.setEnabled(false);


            TextView Que_Type = findViewById(R.id.Que_Type);
            Que_Type.setText("");


            timer.setText("");


            rate_result.setText("Undefined");

            timer.setTextColor(Color.parseColor("#999999"));
            CountDownTimer.reset();


            return;
        }

        if (!Status.equals("No_Data") || !Status.equals("")){

            TextView Que_Type = findViewById(R.id.Que_Type);
            Que_Type.setText("Type : ( " + Question_Type.toString() + " )");

            TextView timer = findViewById(R.id.timer);


            Speaker_Check = false;

            if (Question_Type.toString().equals("Repeat")){

                try {
                    ImageView imageView1 = findViewById(R.id.quiz_img);
                    Picasso.get().load(image_src.toString()).placeholder(R.drawable.image).into(imageView1);
                }catch (Exception e) {
                }

                answer_layout.setVisibility(View.VISIBLE);
                question.setText(The_Question.toString());

                answer_btn1.setVisibility(View.GONE);
                answer_btn2.setVisibility(View.GONE);
                answer_btn3.setVisibility(View.GONE);
                answer_btn4.setVisibility(View.GONE);

                Lister_Speak.setWeightSum(2);
                mic_speak.setVisibility(View.VISIBLE);
                listner.setVisibility(View.VISIBLE);
                Lister_Speak.setVisibility(View.VISIBLE);

                //auto
                First = "0";

                Confirm_linear.setVisibility(View.GONE);
                confirmer.setVisibility(View.GONE);

                if (Currently_Que > Questions_I_Reached){
                    next_que.setImageResource(R.drawable.next_que_2);
                    next_que.setEnabled(false);
                    Lister_Speak.setWeightSum(2);
                    mic_speak.setVisibility(View.VISIBLE);


                    CountDownTimer.reset();
                    CountDownTimer.start(false);
                  //  timer.setText("1:00");
                }else {
                    timer.setTextColor(Color.parseColor("#999999"));
                    timer.setText("00:00");

                    CountDownTimer.reset();

                    next_que.setImageResource(R.drawable.next_que);
                    next_que.setEnabled(true);
                    Lister_Speak.setWeightSum(1);
                    mic_speak.setVisibility(View.GONE);
                }


                if (Currently_Que == 0){
                    prev_que.setImageResource(R.drawable.prev_que_2);
                    prev_que.setEnabled(false);
                }else {
                    prev_que.setImageResource(R.drawable.prev_que);
                    prev_que.setEnabled(true);
                }



                //////////////////////////

                DecimalFormat decimalFormat;
                decimalFormat = new DecimalFormat("#.#");

                double result;

                if (Integer.valueOf(total_question.getText().toString()) > 0 ){
                    result = Double.parseDouble(Right_Questions_count.toString()) / Double.parseDouble(total_question.getText().toString());
                    rate_result.setText(decimalFormat.format(result*100));
                }else {
                    rate_result.setText("0");
                }

                return;
            }

            if (Question_Type.toString().equals("Choose")){

                try {
                    ImageView imageView1 = findViewById(R.id.quiz_img);
                    Picasso.get().load(image_src.toString()).placeholder(R.drawable.image).into(imageView1);
                }catch (Exception e) {
                }

                answer_layout.setVisibility(View.VISIBLE);
                question.setText(The_Question.toString().replace("*","..."));

                answer_btn1.setVisibility(View.VISIBLE);
                answer_btn2.setVisibility(View.VISIBLE);
                answer_btn3.setVisibility(View.VISIBLE);
                answer_btn4.setVisibility(View.VISIBLE);

                Lister_Speak.setWeightSum(1);
                mic_speak.setVisibility(View.GONE);
                listner.setVisibility(View.VISIBLE);
                Lister_Speak.setVisibility(View.VISIBLE);

                Confirm_linear.setVisibility(View.GONE);
                confirmer.setVisibility(View.GONE);

                if (answer_1.toString().equals("")){
                    answer_btn1.setVisibility(View.GONE);
                }else {
                    answer_btn1.setVisibility(View.VISIBLE);
                }

                if (answer_2.toString().equals("")){
                    answer_btn2.setVisibility(View.GONE);
                }else {
                    answer_btn2.setVisibility(View.VISIBLE);
                }

                if (answer_3.toString().equals("")){
                    answer_btn3.setVisibility(View.GONE);
                }else {
                    answer_btn3.setVisibility(View.VISIBLE);
                }

                if (answer_4.toString().equals("")){
                    answer_btn4.setVisibility(View.GONE);
                }else {
                    answer_btn4.setVisibility(View.VISIBLE);
                }

                answer_btn1.setText(answer_1.toString().replace("(Correct)",""));
                answer_btn2.setText(answer_2.toString().replace("(Correct)",""));
                answer_btn3.setText(answer_3.toString().replace("(Correct)",""));
                answer_btn4.setText(answer_4.toString().replace("(Correct)",""));


                if (Currently_Que > Questions_I_Reached){

                    next_que.setImageResource(R.drawable.next_que_2);
                    next_que.setEnabled(false);

                    answer_btn1.setEnabled(true);
                    answer_btn2.setEnabled(true);
                    answer_btn3.setEnabled(true);
                    answer_btn4.setEnabled(true);

                    answer_btn1.setTextColor(Color.parseColor("#ffffff"));
                    answer_btn2.setTextColor(Color.parseColor("#ffffff"));
                    answer_btn3.setTextColor(Color.parseColor("#ffffff"));
                    answer_btn4.setTextColor(Color.parseColor("#ffffff"));


                    if (answer_1.toString().contains("(Correct)")){
                        Correct_Answer = answer_1.toString().replace("(Correct)","");}

                    if (answer_2.toString().contains("(Correct)")){
                        Correct_Answer = answer_2.toString().replace("(Correct)","");}

                    if (answer_3.toString().contains("(Correct)")){
                        Correct_Answer = answer_3.toString().replace("(Correct)","");}

                    if (answer_4.toString().contains("(Correct)")){
                        Correct_Answer = answer_4.toString().replace("(Correct)","");}


                    CountDownTimer.reset();
                    CountDownTimer.start(false);

                }else {

                    timer.setTextColor(Color.parseColor("#999999"));
                    timer.setText("00:00");

                    CountDownTimer.reset();

                    next_que.setImageResource(R.drawable.next_que);
                    next_que.setEnabled(true);
//                    Lister_Speak.setWeightSum(1);
//                    mic_speak.setVisibility(View.GONE);

                    answer_btn1.setEnabled(false);
                    answer_btn2.setEnabled(false);
                    answer_btn3.setEnabled(false);
                    answer_btn4.setEnabled(false);

                    if (answer_1.toString().contains("(Correct)")){

                        answer_btn1.setTextColor(Color.parseColor("#11d811"));
                        answer_btn2.setTextColor(Color.parseColor("#ffffff"));
                        answer_btn3.setTextColor(Color.parseColor("#ffffff"));
                        answer_btn4.setTextColor(Color.parseColor("#ffffff"));
                    }

                    if (answer_2.toString().contains("(Correct)")){

                        answer_btn1.setTextColor(Color.parseColor("#ffffff"));
                        answer_btn2.setTextColor(Color.parseColor("#11d811"));
                        answer_btn3.setTextColor(Color.parseColor("#ffffff"));
                        answer_btn4.setTextColor(Color.parseColor("#ffffff"));
                    }

                    if (answer_3.toString().contains("(Correct)")){

                        answer_btn1.setTextColor(Color.parseColor("#ffffff"));
                        answer_btn2.setTextColor(Color.parseColor("#ffffff"));
                        answer_btn3.setTextColor(Color.parseColor("#11d811"));
                        answer_btn4.setTextColor(Color.parseColor("#ffffff"));
                    }

                    if (answer_4.toString().contains("(Correct)")){

                        answer_btn1.setTextColor(Color.parseColor("#ffffff"));
                        answer_btn2.setTextColor(Color.parseColor("#ffffff"));
                        answer_btn3.setTextColor(Color.parseColor("#ffffff"));
                        answer_btn4.setTextColor(Color.parseColor("#11d811"));
                    }
                }


                if (Currently_Que == 0){
                    prev_que.setImageResource(R.drawable.prev_que_2);
                    prev_que.setEnabled(false);
                }else {
                    prev_que.setImageResource(R.drawable.prev_que);
                    prev_que.setEnabled(true);
                }



                //////////////////////////

                DecimalFormat decimalFormat;
                decimalFormat = new DecimalFormat("#.#");

                double result;

                if (Integer.valueOf(total_question.getText().toString()) > 0 ){
                    result = Double.parseDouble(Right_Questions_count.toString()) / Double.parseDouble(total_question.getText().toString());
                    rate_result.setText(decimalFormat.format(result*100));
                }else {
                    rate_result.setText("0");
                }

                return;
            }

            if (Question_Type.toString().equals("True / False")){

                try {
                    ImageView imageView1 = findViewById(R.id.quiz_img);
                    Picasso.get().load(image_src.toString()).placeholder(R.drawable.image).into(imageView1);
                }catch (Exception e) {
                }

                answer_layout.setVisibility(View.VISIBLE);
                question.setText(The_Question.toString());

                answer_btn1.setVisibility(View.VISIBLE);
                answer_btn2.setVisibility(View.VISIBLE);
                answer_btn3.setVisibility(View.GONE);
                answer_btn4.setVisibility(View.GONE);

                Lister_Speak.setWeightSum(1);
                mic_speak.setVisibility(View.GONE);
                listner.setVisibility(View.VISIBLE);
                Lister_Speak.setVisibility(View.VISIBLE);

                Confirm_linear.setVisibility(View.GONE);
                confirmer.setVisibility(View.GONE);


                if (answer_1.toString().equals("")){
                    answer_btn1.setVisibility(View.GONE);
                }else {
                    answer_btn1.setVisibility(View.VISIBLE);
                }

                if (answer_2.toString().equals("")){
                    answer_btn2.setVisibility(View.GONE);
                }else {
                    answer_btn2.setVisibility(View.VISIBLE);
                }

                    answer_btn3.setVisibility(View.GONE);
                    answer_btn4.setVisibility(View.GONE);


                answer_btn1.setText(answer_1.toString().replace("(Correct)",""));
                answer_btn2.setText(answer_2.toString().replace("(Correct)",""));


                if (Currently_Que > Questions_I_Reached){


                    next_que.setImageResource(R.drawable.next_que_2);
                    next_que.setEnabled(false);

                    answer_btn1.setEnabled(true);
                    answer_btn2.setEnabled(true);

                    answer_btn1.setTextColor(Color.parseColor("#ffffff"));
                    answer_btn2.setTextColor(Color.parseColor("#ffffff"));


                    if (answer_1.toString().contains("(Correct)")){
                        Correct_Answer = answer_1.toString().replace("(Correct)","");}

                    if (answer_2.toString().contains("(Correct)")){
                        Correct_Answer = answer_2.toString().replace("(Correct)","");}

                    CountDownTimer.reset();
                    CountDownTimer.start(false);
                }else {

                    timer.setTextColor(Color.parseColor("#999999"));
                    timer.setText("00:00");

                    answer_btn1.setEnabled(false);
                    answer_btn2.setEnabled(false);

                    if (answer_1.toString().contains("(Correct)")){

                        answer_btn1.setTextColor(Color.parseColor("#11d811"));
                        answer_btn2.setTextColor(Color.parseColor("#ffffff"));
                    }

                    if (answer_2.toString().contains("(Correct)")){

                        answer_btn1.setTextColor(Color.parseColor("#ffffff"));
                        answer_btn2.setTextColor(Color.parseColor("#11d811"));
                    }


                    CountDownTimer.reset();
                    next_que.setImageResource(R.drawable.next_que);
                    next_que.setEnabled(true);
//                    Lister_Speak.setWeightSum(1);
//                    mic_speak.setVisibility(View.GONE);
                }

//Currently_Que == 1
                if (Currently_Que == 0){
                    prev_que.setImageResource(R.drawable.prev_que_2);
                    prev_que.setEnabled(false);
                }else {
                    prev_que.setImageResource(R.drawable.prev_que);
                    prev_que.setEnabled(true);
                }



                //////////////////////////

                DecimalFormat decimalFormat;
                decimalFormat = new DecimalFormat("#.#");

                double result;

                if (Integer.valueOf(total_question.getText().toString()) > 0 ){
                    result = Double.parseDouble(Right_Questions_count.toString()) / Double.parseDouble(total_question.getText().toString());
                    rate_result.setText(decimalFormat.format(result*100));
                }else {
                    rate_result.setText("0");
                }

                return;
            }


            return;
        }

        if (Status.equals("")){
            RelativeLayout relativeLayout = findViewById(R.id.answers_layout);
            relativeLayout.setVisibility(View.GONE);

            total_question.setText("Error..!");
            question.setText("Error..!");
            rate_result.setText("Error..!");
            streak_result.setText("Error..!");
            return;
        }

    }


    private void Correctly(){

        mp = MediaPlayer.create(this, R.raw.correct);
        mp.start();

        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.correct, null);
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(view);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
        alertDialog.setCancelable(false);


        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };


        handler.postDelayed(runnable, 1500);


    }

    private void Wrong(){

        mp = MediaPlayer.create(this, R.raw.wrong);
        mp.start();

        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.wrong, null);
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(view);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
        alertDialog.setCancelable(false);


        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };


        handler.postDelayed(runnable, 1500);


    }

    public void btn1(View view) {

        answer_btn2.setTextColor(Color.parseColor("#ffffff"));
        answer_btn3.setTextColor(Color.parseColor("#ffffff"));
        answer_btn4.setTextColor(Color.parseColor("#ffffff"));
        answer_btn1.setTextColor(Color.parseColor("#c8ce0e"));


        LinearLayout linearLayout1 = findViewById(R.id.linear_confirm);
        linearLayout1.setVisibility(View.VISIBLE);


        ImageView imageView1  = findViewById(R.id.Checking);
      //  RecognitionProgressView recognitionProgressView  = findViewById(R.id.recognition_view);
        imageView1.setVisibility(View.VISIBLE);
       // recognitionProgressView.setVisibility(View.GONE);

        ScrollView scrollView = findViewById(R.id.assess_scroll);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });


        ////
//        play_speaker = false;
//
//        ImageView imageView = findViewById(R.id.speak_assess);
//        imageView.setEnabled(true);
//        imageView.setImageResource(R.drawable.speak);
//        //     imageView2.setVisibility(View.VISIBLE);
//
//        ImageView imageView00 = findViewById(R.id.Mic_assess);
//        imageView00.setEnabled(true);
//
//        Cancel();
        ///

    }

    public void btn2(View view) {
        answer_btn2.setTextColor(Color.parseColor("#c8ce0e"));
        answer_btn1.setTextColor(Color.parseColor("#ffffff"));
        answer_btn3.setTextColor(Color.parseColor("#ffffff"));
        answer_btn4.setTextColor(Color.parseColor("#ffffff"));

//        if (Correct_Answer.equals(answer_btn2.getText().toString())){
//            First = "1";
//        }else {
//            First = "0";
//        }

        LinearLayout linearLayout1 = findViewById(R.id.linear_confirm);
        linearLayout1.setVisibility(View.VISIBLE);

//        linearLayout1.setWeightSum(1);

        ImageView imageView1  = findViewById(R.id.Checking);
     //   RecognitionProgressView recognitionProgressView  = findViewById(R.id.recognition_view);
        imageView1.setVisibility(View.VISIBLE);
   //     recognitionProgressView.setVisibility(View.GONE);

        ScrollView scrollView = findViewById(R.id.assess_scroll);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });


        ////
//        play_speaker = false;
//
//        ImageView imageView = findViewById(R.id.speak_assess);
//        imageView.setEnabled(true);
//        imageView.setImageResource(R.drawable.speak);
//        //     imageView2.setVisibility(View.VISIBLE);
//
//        ImageView imageView00 = findViewById(R.id.Mic_assess);
//        imageView00.setEnabled(true);
//
//        Cancel();
        ///
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this, "Requires RECORD_AUDIO permission", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.RECORD_AUDIO },
                    REQUEST_RECORD_AUDIO_PERMISSION_CODE);
        }
    }


    public void assess_log(View view) {

        CountDownTimer.reset();

        Intent ii = new Intent(Assess.this, Assess_Log.class);
        startActivity(ii);
        finish();
    }

    public void Confirm(View view) {



        LinearLayout confirm_linear = findViewById(R.id.linear_confirm);

        if (Question_Type.toString().equals("Choose") || Question_Type.toString().equals("True / False")){


            answer_btn1.setEnabled(false);
            answer_btn2.setEnabled(false);
            answer_btn3.setEnabled(false);
            answer_btn4.setEnabled(false);

            LinearLayout linearLayout = findViewById(R.id.linear_mic_speak);
            linearLayout.setWeightSum(2);
            mic_speak.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);

            confirm_linear.setVisibility(View.GONE);


            int color1=answer_btn1.getCurrentTextColor();
            String hexColor1 = String.format("#%06X", (0xFFFFFF & color1));

            int color2=answer_btn2.getCurrentTextColor();
            String hexColor2 = String.format("#%06X", (0xFFFFFF & color2));





            if (hexColor1.toString().equals("#C8CE0E")){
                if (Correct_Answer.equals(answer_btn1.getText().toString())){
                    First = "1";
                }else {
                    First = "0";
                }
            }else {

                if (hexColor2.toString().equals("#C8CE0E")){
                    if (Correct_Answer.equals(answer_btn2.getText().toString())){
                        First = "1";
                    }else {
                        First = "0";
                    }
                }

            }

            ////////////////////////////
        }



//        ImageView imageView = findViewById(R.id.Mic_assess);
//        ImageView imageView2 = findViewById(R.id.speak_assess);
//        imageView.setVisibility(View.VISIBLE);
//        imageView2.setVisibility(View.VISIBLE);
//
//        LinearLayout linearLayout = findViewById(R.id.linear_mic_speak);
//        linearLayout.setWeightSum(2);
//
//
//        ImageView imageView4 = findViewById(R.id.Checking);
//
//
//        linearLayout1.setWeightSum(1);
//
//        imageView4.setVisibility(View.GONE);


        ScrollView scrollView = findViewById(R.id.assess_scroll);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });



    }


    class load_media extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  tvInfo.setText("Start");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Your background method
            // initializing media player
            mediaPlayer = new MediaPlayer();

            // below line is use to set the audio
            // stream type for our media player.
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            // below line is use to set our
            // url to our media player.
            try {
                mediaPlayer.setDataSource(urii);
                // below line is use to prepare
                // and start our media player.
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {


//                        try {
                            //  Block of code to try
                            ImageView imageView = findViewById(R.id.speak_assess);
                            imageView.setEnabled(true);
                            imageView.setImageResource(R.drawable.speak);
                            //     imageView2.setVisibility(View.VISIBLE);

                            ImageView imageView00 = findViewById(R.id.Mic_assess);
                            imageView00.setEnabled(true);

                            play_speaker = false;
//                        }
//                        catch(Exception e) {
//                            //  Block of code to handle errors
//                        }


                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
         //   tvInfo.setText("Finish");
        }

    }

    public void que_next(View view) {
        ////
        ImageView imageView = findViewById(R.id.speak_assess);
        imageView.setEnabled(true);
        imageView.setImageResource(R.drawable.speak);
        //     imageView2.setVisibility(View.VISIBLE);

        ImageView imageView00 = findViewById(R.id.Mic_assess);
        imageView00.setEnabled(true);
        Cancel();
        ////Previous

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Currently_Que","Next");
        editor.apply();


//
//        change_que_type = "next";

//        TextView textView = findViewById(R.id.assess_que_number);
//        Integer prev = Integer.valueOf(textView.getText().toString().replace("Question : ",""))+1;
//        Currently_Que_Num = prev;
//
//        textView.setText("Question : " + prev);
        Checking1();

    }

    public void que_back(View view) {
        ////
        ImageView imageView = findViewById(R.id.speak_assess);
        imageView.setEnabled(true);
        imageView.setImageResource(R.drawable.speak);
        //     imageView2.setVisibility(View.VISIBLE);

        ImageView imageView00 = findViewById(R.id.Mic_assess);
        imageView00.setEnabled(true);
        Cancel();
        ////

//       change_que_type = "previous";




        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Currently_Que","Previous");
        editor.apply();

        Checking1();




//        TextView textView = findViewById(R.id.assess_que_number);
//        //  int que = Integer.valueOf(textView.getText().toString().replace("Question : ","")) - 1;
//        //  questions = String.valueOf(que)
//        Integer prev = Integer.valueOf(textView.getText().toString().replace("Question : ",""))-1;
//        Currently_Que_Num = prev;
//
//        textView.setText("Question : " + prev);

        // android:id="@+id/assess_que_number"

//        TextView textView = findViewById(R.id.assess_que_number);
//        String Que = textView.getText().toString().replace("Question : ","");
//
//        int previous_que = Integer.valueOf(Que.toString()) - 1;

    }

    private void my_rate() {

        String Original = Full_Answer.toLowerCase();
        String Copy = my_rec;

        Collection<String> listOne = new ArrayList<String>(Arrays.asList(Original.split(" ")));
        Collection<String> listTwo = new ArrayList<String>(Arrays.asList(Copy.split(" ")));


        List<String> sourceList = new ArrayList<String>(listOne);
        List<String> destinationList = new ArrayList<String>(listTwo);

        sourceList.removeAll( listTwo );
        destinationList.removeAll( listOne );

        int Original_Count,Copy_Count;
        Original_Count =  listOne.size();
        Copy_Count =  sourceList.size();

        My_Rate = String.valueOf (100 - Copy_Count * 100 / Original_Count);
        show_result();
    }

    private void show_result(){
        builder = new AlertDialog.Builder(Assess.this, R.style.AlertDialogTheme);
        @SuppressLint("ResourceType") View view = LayoutInflater.from(Assess.this).inflate(
                R.xml.result_dialog,
                findViewById(R.id.layoutDialogContainer)
        );
        builder.setCancelable(false);
        builder.setView(view);
        alertDialog = builder.create();
        ((TextView) view.findViewById(R.id.res2)).setText("My Record : " + my_rec);
        ((TextView) view.findViewById(R.id.res3)).setText("Original : " + Full_Answer.toLowerCase());
        ((TextView) view.findViewById(R.id.res1)).setText("My Rate :" + My_Rate + " %");


        int res,my_res;

        my_res = Integer.valueOf(My_Rate);
        res = Integer.valueOf(Server_Rate);

       //


        if (my_res >= res){
            if (Question_Type.toString().equals("Repeat")){
                Second = "2";
            }else {

//                Second = "1";


                if (my_rec.length() > Full_Answer.length()){
                    Second = "0";
                }else {
                    Second = "1";
                }

            }
            ((TextView) view.findViewById(R.id.res4)).setText("Status : Accepted");
        }else {
            Second = "0";
            ((TextView) view.findViewById(R.id.res4)).setText("Status : Rejected");
        }




     //   Toast.makeText(Assess.this,  IP.toString() + "Talaka/Users/Assess/Next.php?username=" + Username.toString() + "&result=" + String.valueOf(Currently_Que_Num) + "(" + First.toString() + "-" + Second.toString() + "),", Toast.LENGTH_SHORT).show();
        Update();
        view.findViewById(R.id.result_dialo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

//                recognitionProgressView.stop();
//                recognitionProgressView.play();

                ScrollView scrollView = findViewById(R.id.assess_scroll);
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_UP);
                    }
                });

//                LinearLayout linearLayout1 = findViewById(R.id.linear_confirm);
//                linearLayout1.setVisibility(View.GONE);
//                linearLayout1.setWeightSum(1);
//
//                LinearLayout linearLayout2 = findViewById(R.id.linear_mic_speak);
//                linearLayout2.setWeightSum(1);
//
//                ImageView imageView = findViewById(R.id.speak_assess);
//                imageView.setVisibility(View.VISIBLE);
//
//
//                ImageView imageView2  = findViewById(R.id.Checking);
//                imageView2.setVisibility(View.VISIBLE);

                if (my_res >= res){
                   Correctly();
                }else {
              Wrong();
                }
                // Toast.makeText(Home.this, "No", Toast.LENGTH_SHORT).show();
            }
        });

        if (alertDialog.getWindow()!= null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }

    private void Update(){
        String url;
        int resul = Integer.valueOf(First.toString() + Second.toString());

        if (resul >= 1){
           // resul = Integer.valueOf(Right_Questions_count) + 1;
             url = IP + "Talaka/Users/Assess/Next.php?username=" + Username + "&correct=1" + "&result=" + String.valueOf(Questions_I_Reached + 1) + "(" + First + "-" + Second + "),";

            //url = IP + "Talaka/Users/Assess/Next.php?username=" + Username + "&result=" + String.valueOf(resul) + "(" + First + "-" + Second + "),";
        }else {
          //  resul = Integer.valueOf(Right_Questions_count);
            url = IP + "Talaka/Users/Assess/Next.php?username=" + Username + "&correct=0" + "&result=" + String.valueOf(Questions_I_Reached + 1) + "(" + First + "-" + Second + "),";

            //  url = IP + "Talaka/Users/Assess/Next.php?username=" + Username + "&result=" + String.valueOf(resul) + "(" + First + "-" + Second + "),";
        }

        //String url = IP + "Talaka/Users/Assess/Next.php?username=" + Username + "&result=" + String.valueOf(Questions_I_Reached + 1) + "(" + First + "-" + Second + "),";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONArray jsonArray2 = response.getJSONArray("result");



                    for (int ii = 0; ii < jsonArray2.length(); ii++) {
                        JSONObject employeee = jsonArray2.getJSONObject(ii);


                        if (employeee.getString("Status").equals("Succesfully")){
                            preferences.edit().remove("Currently_Que").commit();
                            Checking1();
                        }else {
                            Toast.makeText(Assess.this, "something went wrong.!", Toast.LENGTH_SHORT).show();
                        }

                    }

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



}
