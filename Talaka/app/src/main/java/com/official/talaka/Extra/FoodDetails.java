package com.official.talaka.Extra;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.material.snackbar.Snackbar;
import com.official.talaka.Activity.Article;
import com.official.talaka.Activity.Home;
import com.official.talaka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoodDetails extends AppCompatActivity {
    TextView itemName, itemRating,Description_txt;
    RatingBar ratingBar;
    String subject, rating, imageUrl, Description,id,ID_User,Type_Article,url;
    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;
    Intent intent;
    String IP = "http://192.168.1.100/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_details);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        requestQueue = Volley.newRequestQueue(this);
        ID_User = sharedPreferences.getString("ID", "");
        YoYo.with(Techniques.BounceInLeft).duration(2000).playOn(findViewById(R.id.food_details_animation));
        intent = getIntent();
        id = intent.getStringExtra("id");
        subject = intent.getStringExtra("subject");
        Description = intent.getStringExtra("description");
        rating = intent.getStringExtra("rate");
        imageUrl = intent.getStringExtra("image");
        Type_Article = intent.getStringExtra("type");
        url = intent.getStringExtra("url");
        Checker();
    }
    public void Back(View view) {
        JcPlayerView jcPlayerView;
        jcPlayerView = findViewById(R.id.jcplayer);
        jcPlayerView.kill();

        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void Visit(){

        String url = IP.toString() + "Talaka/Users/Article/Article_Visit.php?id=" + id.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        String Status  = employee.getString("Status");
                        if (Status.toString().equals("Succesfully")){

                            ProgressBar progressBar = findViewById(R.id.progressBar_Article);
                            progressBar.setVisibility(View.GONE);

                            RelativeLayout relativeLayout = findViewById(R.id.Relative_article);
                            NestedScrollView scrollView = findViewById(R.id.Scroll_article);

                            relativeLayout.setVisibility(View.VISIBLE);
                            scrollView.setVisibility(View.VISIBLE);

                            //////////////////


                            itemName = findViewById(R.id.name);
                            itemName.setText(subject.toString());

                            Description_txt = findViewById(R.id.about_food);
                            Description_txt.setText(Description.toString());

                            itemRating = findViewById(R.id.rating);
                            ratingBar = findViewById(R.id.ratingBar);
                            itemRating.setText(rating.toString());
                            ratingBar.setRating(Float.parseFloat(rating.toString()));
                            //////////////////

                            Starting();
                        }else {

                            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Can't reach server.!\nPlease Check your internet", Snackbar.LENGTH_INDEFINITE);
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
    private void Checker(){

        String url = IP.toString() + "Talaka/Users/Article/Article_Like_Checker.php?id=" + id.toString() + "&user=" + ID_User.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        String Status  = employee.getString("Status");
                        if (Status.toString().equals("Successfully, Can Add it")){

                            ImageView imageView = findViewById(R.id.like);
                            imageView.setVisibility(View.VISIBLE);

                            //////////////////

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Visit();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }
    private void Like(){

        String url = IP.toString() + "Talaka/Users/Article/Article_Like.php?id=" + id.toString() + "&user=" + ID_User.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        String Status  = employee.getString("Status");
                        if (Status.toString().equals("Succesfully")){

                            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Succesfully, Thanks for Your Support.", Snackbar.LENGTH_INDEFINITE);
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

                            ImageView imageView = findViewById(R.id.like);
                            imageView.setVisibility(View.GONE);

                        }else {
                            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Can't reach server.!\nPlease Check your internet", Snackbar.LENGTH_INDEFINITE);
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
    public void Like(View view) {
        Like();
    }

    private void Starting(){
        RelativeLayout relativeLayout = findViewById(R.id.Relative_article);
        relativeLayout.setVisibility(View.VISIBLE);

        Description_txt.setTextIsSelectable(false);
        if(Type_Article.toString().matches("Picture")){

            JcPlayerView jcPlayerView;
            jcPlayerView = findViewById(R.id.jcplayer);
            jcPlayerView.setVisibility(View.VISIBLE);

            ImageView imageView = (ImageView) findViewById(R.id.image_article);
            imageView.setVisibility(View.VISIBLE);

            imageView.setClipToOutline(true);
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);

            ArrayList<JcAudio> jcAudios = new ArrayList<>();
            jcAudios.add(JcAudio.createFromURL(subject.toString(),url.toString()));
            jcPlayerView.initPlaylist(jcAudios, null);


            return;
        }

        if(Type_Article.toString().matches("Video")){

            FrameLayout frameLayout;
            frameLayout = findViewById(R.id.video_article);
            frameLayout.setVisibility(View.VISIBLE);


            VideoView videoView;
            videoView = findViewById(R.id.video);

            videoView.setVideoPath(url.toString());
            MediaController mediaController = new MediaController(FoodDetails.this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();

            return;
        }
    }
}