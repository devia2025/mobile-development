package com.official.tmrnty.Extra;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.official.tmrnty.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.official.tmrnty.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class Training extends AppCompatActivity {
    String gif_1,gif_2;
    String Cam;
    ImageView Training_img;
    String Exsersies,Muscle,Selected_index;
    RequestQueue requestQueue;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        requestQueue = Volley.newRequestQueue(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        TextView exercise = findViewById(R.id.exercise_name);
        TextView dificulty = findViewById(R.id.dificult_lvl);
        Training_img = findViewById(R.id.Training_img);

        Cam = "1";

        String str2 =getSharedPreferences("Selected_Train", MODE_PRIVATE).getString("Selected_Train","");
        Selected_index = getSharedPreferences("Selected_Train_Index", MODE_PRIVATE).getString("Selected_Train_Index","");

        ////////////////
        int _prefix = str2.indexOf("[");
        int _Mid = str2.indexOf("/");
        int _Last = str2.indexOf("]");
        gif_1 = str2.substring(_prefix + 1, _Mid);
        gif_2 = str2.substring(_Mid + 1, _Last);
        /////////////////

      //  Muscle = getSharedPreferences("Man", MODE_PRIVATE).getString("Man","");
        Muscle = preferences.getString("Train", "");

        String gifUrl = "https://raw.githubusercontent.com/DevIA3kl/Tmrnty/main/Man/assets/" + gif_1.toString() +".gif";
        Glide.with( this )
                .load( gifUrl )
                .into( Training_img );

        char first = str2.charAt(0);

        int index6 = str2.indexOf("[");
        String Name_of_Exercise = str2.substring(0 + 1, index6);


        switch (Character.toString(first).toString()){
            case "-":
                exercise.setText(Name_of_Exercise.toString());
                dificulty.setText("Beginner");
                break;
            case "+":
                exercise.setText(Name_of_Exercise.toString());
                dificulty.setText("Intermediate");
                break;

            case "*":
                exercise.setText(Name_of_Exercise.toString());
                dificulty.setText("Advanced");
                break;
        }

        get_User_Info();


    }

    public void training_Back(View view) {
        Intent ii = new Intent(Training.this, List_Log.class);
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


    public void Next_Cam(View view) {
        TextView textView = findViewById(R.id.Cam_txt);
        ImageView Next = findViewById(R.id.Next_Cam);
        ImageView Back = findViewById(R.id.Back_Cam);


        if (Cam.toString().equals("1")){
            Cam = "2";
            textView.setText("CAM 2");
            Next.setColorFilter(ContextCompat.getColor(this, R.color.After), android.graphics.PorterDuff.Mode.SRC_IN);
            Back.setColorFilter(ContextCompat.getColor(this, R.color.No_After), android.graphics.PorterDuff.Mode.SRC_IN);


                    if (gif_2.toString().contains("_0")){
                        Glide.with(this)
                                .load(R.drawable.img)
                                .into(Training_img);
                    }else {
                        String gifUrl = "https://raw.githubusercontent.com/DevIA3kl/Tmrnty/main/Man/assets/" + gif_2.toString() + ".gif";
                        Glide.with(this)
                                .load(gifUrl)
                                .into(Training_img);
                    }



        }
    }


    public void Back_Cam(View view) {
        TextView textView = findViewById(R.id.Cam_txt);
        ImageView Back = findViewById(R.id.Back_Cam);
        ImageView Next = findViewById(R.id.Next_Cam);

        if (Cam.toString().equals("2")){
            Cam = "1";
            textView.setText("CAM 1");
            Back.setColorFilter(ContextCompat.getColor(this, R.color.After), android.graphics.PorterDuff.Mode.SRC_IN);
            Next.setColorFilter(ContextCompat.getColor(this, R.color.No_After), android.graphics.PorterDuff.Mode.SRC_IN);


                    String gifUrl = "https://raw.githubusercontent.com/DevIA3kl/Tmrnty/main/Man/assets/" + gif_1.toString() +".gif";
                    Glide.with( this )
                            .load( gifUrl )
                            .into( Training_img );


        }
    }

    private void get_User_Info() {
        //link
        String Third;

                int index5 = Muscle.indexOf(":");
                int index6 = Muscle.length();
                Third = Muscle.substring( index5+1, index6);







        //  Toast.makeText(My_Training.this,Selected_index.toString(), Toast.LENGTH_SHORT).show();

        //  Toast.makeText(My_Training.this,Third.toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(My_Training.this,Selected_index.toString(), Toast.LENGTH_SHORT).show(); + Exsersies.toString() +

//        Toast.makeText(Training.this,Muscle.toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(Training.this,Selected_index.toString(), Toast.LENGTH_SHORT).show();
//

                String url = "https://raw.githubusercontent.com/DevIA3kl/Tmrnty/main/Exercises.txt";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray(Muscle.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);

                        String Steps_Notes = employee.getString(Selected_index.toString());
                        String[] Steps =  Steps_Notes.split(",",4); // \\r\\n

                        TextView textView1 = findViewById(R.id.step_1);
                        TextView textView2 = findViewById(R.id.step_2);
                        TextView textView3 = findViewById(R.id.step_3);
                        TextView textView4 = findViewById(R.id.step_4);




                        if (!Steps[0].equals("")){
                            textView1.getBackground().setAlpha(15);
                            textView1.setVisibility(View.VISIBLE);
                            textView1.setText(Steps[0]);
                        }
                        if (!Steps[1].equals("")){
                            textView2.getBackground().setAlpha(35);
                            textView2.setVisibility(View.VISIBLE);
                            textView2.setText(Steps[1]);
                        }
                        if (!Steps[2].equals("")){
                            textView3.getBackground().setAlpha(55);
                            textView3.setVisibility(View.VISIBLE);
                            textView3.setText(Steps[2]);
                        }
                        if (!Steps[3].equals("")){
                            textView4.getBackground().setAlpha(70);
                            textView4.setVisibility(View.VISIBLE);
                            textView4.setText(Steps[3]);
                        }


                        //  Toast.makeText(History_Log.this,cal0l, Toast.LENGTH_SHORT).show();


                        //  results.append(firstName + ", " + String.valueOf(age) + ", " + mail +"\n\n");
                    }
                } catch (JSONException e) {
                    get_User_Info();
                   // e.printStackTrace();
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