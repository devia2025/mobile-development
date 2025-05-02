package com.official.talaka.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.official.talaka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class History_Log extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String Status;
    EditText inputSearch;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<Integer> hiddenPositions = new ArrayList<>();
    String Username;
    RequestQueue requestQueue;
    String IP = "http://192.168.1.100/";
    String Calls,Onlines;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_log);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Status = preferences.getString("Log", "");
        EditText editText = (EditText) findViewById(R.id.inputSearch);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        listView = (ListView)findViewById(R.id.listView1);

        if (Status.toString().equals("Call")){
            editText.setHint("Search Call History ...");
        }else {
            if (Status.toString().equals("Recieve")){
                editText.setHint("Search Receive History ...");
            }
        }

        requestQueue = Volley.newRequestQueue(this);
        get_User_Info();

    }

    public void back(View view) {
        Intent ii = new Intent(History_Log.this, Settings.class);
        startActivity(ii);
        finish();
    }
    private void get_User_Info() {
        //link
        String url = IP.toString() + "Talaka/Users/Acc/Check.php?email=" + preferences.getString("Email", "");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Info");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);
                        if (Status.toString().equals("Call")){
                            Calls = employee.getString("Call_History").replaceAll("(?m)^[ \\t]*\\r?\\n","");
                            Call();
                        }else {
                            if (Status.toString().equals("Recieve")){
                                Onlines = employee.getString("Recieve_History").replaceAll("(?m)^[ \\t]*\\r?\\n","");
                                Online();
                            }
                        }
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void Call(){
        String[] words =  Calls.split("\n"); // \\r\\n
        List<String> fruits_list = new ArrayList<String>(Arrays.asList(words));

        // Create an ArrayAdapter from List
        final  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.parseColor("#999999"));

                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f); //or whatever size you want

                // Generate ListView Item using TextView
                return view;
            }

        };

        // DataBind ListView with items from ArrayAdapter
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //   Toast.makeText(MainActivity.this,arrayAdapter.getItem(i), Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("UserActivity_username",arrayAdapter.getItem(i).toString());
                editor.putString("UserActivity_Type","Call");
                editor.putString("Rate","Yes");
                editor.apply();



                    Intent ii = new Intent(History_Log.this, User_Activity.class);
                    startActivity(ii);
                    finish();




                //  if(arrayAdapter.getItem(i).toString().equals("Accounts")){
                //       Intent ii = new Intent(MainActivity.this, Users.class);
                //       startActivity(ii);
                //       finish();
                //   }

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



    }
    private void Online(){
        String[] words =  Onlines.split("\n");
        List<String> fruits_list = new ArrayList<String>(Arrays.asList(words));

        // Create an ArrayAdapter from List
        final  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.parseColor("#999999"));

                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f); //or whatever size you want

                // Generate ListView Item using TextView
                return view;
            }

        };

        // DataBind ListView with items from ArrayAdapter
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //   Toast.makeText(MainActivity.this,arrayAdapter.getItem(i), Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("UserActivity_username",arrayAdapter.getItem(i).toString());
                editor.putString("UserActivity_Type","Recieve");
                editor.putString("Rate","Yes");
                editor.apply();




                    Intent ii = new Intent(History_Log.this, User_Activity.class);
                    startActivity(ii);
                    finish();






                //  if(arrayAdapter.getItem(i).toString().equals("Accounts")){
                //       Intent ii = new Intent(MainActivity.this, Users.class);
                //       startActivity(ii);
                //       finish();
                //   }

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



    }
}