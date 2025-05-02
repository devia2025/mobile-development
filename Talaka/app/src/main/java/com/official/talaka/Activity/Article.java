package com.official.talaka.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.official.talaka.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class Article extends AppCompatActivity{
    SharedPreferences sharedPreferences;
    String Lvl;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    private View mTarget1,mTarget2,mTarget3,mTarget4;
    SharedPreferences preferences;
    String IP = "http://192.168.1.100/";
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mTarget1 = findViewById(R.id.tittle_article);
        mTarget2 = findViewById(R.id.article_back);
        mTarget3 = findViewById(R.id.scrole_article);


        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(mTarget1);// after start,just click mTarget view, rope is not init
        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(mTarget2);// after start,just click mTarget view, rope is not init
        YoYo.with(Techniques.BounceInDown).duration(1000).playOn(mTarget3);// after start,just click mTarget view, rope is not init

        requestQueue = Volley.newRequestQueue(this);
        progressBar = findViewById(R.id.progressBar_Article);
        relativeLayout = findViewById(R.id.article_relative);

        progressBar.setVisibility(View.VISIBLE);
        get_User_Info();

        mAdView = findViewById(R.id.ads_article);
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

    public void Article_Back(View view) {


        JcPlayerView jcPlayerView;
        jcPlayerView = findViewById(R.id.jcplayer);
        jcPlayerView.kill();
        
        Intent i = new Intent(Article.this, Practice.class);
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


    private void get_User_Info() {

        Lvl = preferences.getString("Lvl", "");
      //  Toast.makeText(Article.this, Lvl.toString() , Toast.LENGTH_SHORT).show();


        //link
        String url = IP.toString() + "Talaka/Users/Article/Check.php?lvl=" + Lvl.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        String subject = employee.getString("Subject");
                        String description = employee.getString("Description");
                        String url = employee.getString("Url");
                        String type = employee.getString("Type");



                      //  Toast.makeText(Article.this, subject.toString() + "+" + description.toString() + "+" + url.toString() + "+" + type.toString() + "+", Toast.LENGTH_SHORT).show();


                        progressBar.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);

                        ImageView imageView;
                        imageView=(ImageView)findViewById(R.id.image_article);

                        TextView textView;
                        textView=(TextView)findViewById(R.id.topic_subject);
                        textView.setText(subject.toString() + "\n\n" + description.toString());


                        JcPlayerView jcPlayerView;

                        jcPlayerView = findViewById(R.id.jcplayer);
// Toast.makeText(Article.this, type.toString(), Toast.LENGTH_SHORT).show();


                        if (type.toString().equals("Picture")){
                            String image = employee.getString("Image");

                            imageView.setVisibility(View.VISIBLE);
                            jcPlayerView.setVisibility(View.VISIBLE);
                            //Picasso.with(Article.this).load(image.toString()).error(R.drawable.image).into(imageView);
                             Picasso.get().load(image.toString()).placeholder(R.drawable.image).into(imageView);


                            ArrayList<JcAudio> jcAudios = new ArrayList<>();
                            jcAudios.add(JcAudio.createFromURL(subject.toString(),url.toString()));

                            jcPlayerView.initPlaylist(jcAudios, null);
                            //jcplayerView.createNotification(); // default icon
                          //  jcPlayerView.createNotification(R.drawable.image); // Your icon resource

                            textView.setTextIsSelectable(false);
                          //  Button button = findViewById(R.id.btnNext);
                          //  button.setVisibility(View.GONE);

                        } else {

                            if (type.toString().equals("Video")){
                              //  VideoView videoView = findViewById(R.id.video_article);
                               // videoView.setVisibility(View.VISIBLE);
                            //    videoView.setVideoPath("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
                              //  videoView.start();

                                VideoView videoView;
                                videoView = findViewById(R.id.video_article);


                                FrameLayout frameLayout;
                                frameLayout = findViewById(R.id.video_article_frame);
                                frameLayout.setVisibility(View.VISIBLE);

                                videoView.setVideoPath(url.toString());
                                MediaController mediaController = new MediaController(Article.this);
                                mediaController.setAnchorView(videoView);
                                videoView.setMediaController(mediaController);
                                videoView.start();

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

}