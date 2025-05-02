package com.official.tmrnty.Extra;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.official.tmrnty.R;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.android.volley.RequestQueue;
import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.official.tmrnty.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class List_Log extends AppCompatActivity {
    RequestQueue requestQueue;
    String Subjects,Descriptions;

    EditText inputSearch;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<Integer> hiddenPositions = new ArrayList<>();
    SharedPreferences preferences;

    int posi;
    String Selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        requestQueue = Volley.newRequestQueue(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        inputSearch = (EditText) findViewById(R.id.search_assess_question);
        listView = (ListView)findViewById(R.id.listView1);
        Selected = preferences.getString("Train","");

        Source();
      //  Toast.makeText(this,  str2.toString(), Toast.LENGTH_SHORT).show();

//        String str =getSharedPreferences("Choice", MODE_PRIVATE).getString("Choice","");

//        String str3 =getSharedPreferences("Woman", MODE_PRIVATE).getString("Woman","");


//        switch (str2.toString()){
//
//            case "2:Traps":
//                male_front_traps();
//                break;
//
//            case "3:Shoulders":
//                male_front_shoulders();
//                break;
//            case "4:Chest":
//                male_Chest();
//                break;
//            case "5:Biceps":
//                male_Biceps();
//                break;
//
//            case "6:Forearms":
//                male_Forearms();
//                break;
//
//            case "7:Abdominals":
//                male_Abdominals();
//                break;
//
//            case "8:Quads":
//                male_Quads();
//                break;
//            case "9:Calves":
//                male_Calves();
//                break;
//        }
    }

    private void Source(){
        String IP = "https://raw.githubusercontent.com/DevIA3kl/Tmrnty/main/Man/Man.txt";
        String url = IP.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray(Selected.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);


                        Subjects = employee.getString("Source");
                        //////////////////

                        Filter();

                        //////////////////////////////


                    }
                } catch (JSONException e) {
                  //  e.printStackTrace();
                    Source();
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



    private String check(String checker){
        if (checker.contains("-")){
            return checker = "-";
        }
        if (checker.contains("+")){
            return checker = "+";
        }
        if (checker.contains("*")){
            return checker = "*";
        }
        return checker;
    }

    private void Filter(){
        String[] words =  Subjects.split(","); // \\r\\n
        List<String> fruits_list = new ArrayList<String>(Arrays.asList(words));

        // Create an ArrayAdapter from List
        final  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                if(convertView==null)
                {
                    convertView= LayoutInflater.from(List_Log.this).inflate(R.layout.model,parent,false);
                }


                TextView que = convertView.findViewById(R.id.Subject);
                TextView Description = convertView.findViewById(R.id.Description);
                ImageView imageView = convertView.findViewById(R.id.Img_source_training);

                String str = fruits_list.get(position);

                int index0 = str.indexOf("[");
                int index00 = str.indexOf("/");
                String indox = str.substring(index0 + 1, index00);

//"https://raw.githubusercontent.com/DevIA3kl/Tmrnty/main/Man/assets/" + indox.toString() + ".gif"


                Picasso.get().load("https://raw.githubusercontent.com/DevIA3kl/Tmrnty/main/Man/assets/" + indox.toString() + ".gif").into(imageView);

                switch (check(fruits_list.get(position))){

                    case "-":
                        int index1 = str.indexOf("-");
                        int index2 = str.indexOf("[");
                        String First = str.substring(index1 + 1, index2);
                        que.setText(First);
                        Description.setText("Difficulty : Beginner");
                        break;

                    case "+":
                        int index3 = str.indexOf("+");
                        int index4 = str.indexOf("[");
                        String Second = str.substring(index3 + 1, index4);
                        que.setText(Second);
                        Description.setText("Difficulty : Intermediate");
                        break;

                    case "*":
                        int index5 = str.indexOf("*");
                        int index6 = str.indexOf("[");
                        String Third = str.substring(index5 + 1, index6);
                        que.setText(Third);
                        Description.setText("Difficulty : Advanced");
                        break;
                }



//                check(fruits_list.get(position));



                // Generate ListView Item using TextView
                return convertView;
            }

        };

        // Collections.reverse(fruits_list); // ADD THIS LINE TO REVERSE ORDER!
        // DataBind ListView with items from ArrayAdapter
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //   Toast.makeText(MainActivity.this,arrayAdapter.getItem(i), Toast.LENGTH_SHORT).show();

//                Toast.makeText(List_Log.this,String.valueOf(i+1) , Toast.LENGTH_SHORT).show();


                SharedPreferences settings = getSharedPreferences("Selected_Train", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Selected_Train", arrayAdapter.getItem(i));
                editor.commit();


                SharedPreferences settings2 = getSharedPreferences("Selected_Train_Index", 0);
                SharedPreferences.Editor editor2 = settings2.edit();
                editor2.putString("Selected_Train_Index", String.valueOf(i+1));
                editor2.commit();

                Intent ii = new Intent(List_Log.this, Training.class);
                startActivity(ii);
                finish();

            }

        });






        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                arrayAdapter.getFilter().filter(cs);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }
        });




        ////////////
        // Spinner_install();
    }



    public void back(View view) {
        Intent ii = new Intent(List_Log.this, Main.class);
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
}