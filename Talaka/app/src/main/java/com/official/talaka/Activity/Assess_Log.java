package com.official.talaka.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.official.talaka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Assess_Log extends AppCompatActivity {
    EditText inputSearch;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<Integer> hiddenPositions = new ArrayList<>();
    RequestQueue requestQueue;
    String IP = "http://192.168.1.100/";
    String Tracking,Username,Email;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assess_log);

        inputSearch = (EditText) findViewById(R.id.search_assess_question);
        listView = (ListView)findViewById(R.id.listView1);

        requestQueue = Volley.newRequestQueue(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Username = preferences.getString("Username", "");
        Email = preferences.getString("Email", "");
        get_User_Info();
    }
    private void get_User_Info() {
        //link
        String url = IP.toString() + "Talaka/Users/Acc/Check.php?email=" + Email.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Info");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);

                        //String cal0l = employee.getString("Call_History");
                        //  Toast.makeText(History_Log.this,cal0l, Toast.LENGTH_SHORT).show();


                        if (employee.getString("Assess_Question_Answered").equals("")){
                            Tracking = "0";
                        }else {
                            Tracking = employee.getString("Assess_Question_Answered"); //(?m)^[ \t]*\r?\n
                            Continue();
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


    private void Continue(){

        String[] words =  Tracking.split(","); // \\r\\n
        List<String> fruits_list = new ArrayList<String>(Arrays.asList(words));

        // Create an ArrayAdapter from List
        final  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                if(convertView==null)
                {
                    convertView= LayoutInflater.from(Assess_Log.this).inflate(R.layout.model,parent,false);
                }


                TextView que = convertView.findViewById(R.id.nameTextView);
                TextView nameTxt = convertView.findViewById(R.id.propellantTextView);
                TextView percentTxt = convertView.findViewById(R.id.percent);
                //  nameTxt.setText(fruits_list.get(position));
                ImageView imageView = convertView.findViewById(R.id.spacecraftImageView);

                String str = fruits_list.get(position);
                int index1 = str.indexOf("(");
                int index2 = str.indexOf(")");
                int index3 = str.indexOf("-");
                String Number = str.substring(0, index1);

                String First = str.substring(index1 + 1, index3);
                String Second = str.substring(index3 + 1, index2);

                que.setText( "Question #" + Number.toString());

                int result = Integer.valueOf(First) + Integer.valueOf(Second);
                // relativeLayout.se(Color.parseColor("#086308"));



              //  Toast.makeText(Assess_Log.this,String.valueOf(Second), Toast.LENGTH_SHORT).show();
                if (String.valueOf(Second).equals("2")){
                    nameTxt.setText("Record : " + Second.toString().replace("2","☑"));

                }else {
                    nameTxt.setText("Answer : " + First.toString().replace("1","☑").replace("0","☒") + " | Record : " + Second.toString().replace("1","☑").replace("0","☒") );
                }

                if (result == 0){
                    imageView.setImageResource(R.drawable.wrong);
                    percentTxt.setText("0 %");
                    percentTxt.setTextColor(Color.parseColor("#ff0000"));
                }

                if (result == 1){
                    imageView.setImageResource(R.drawable.right);
                    percentTxt.setText("50 %");
                    percentTxt.setTextColor(Color.parseColor("#cec635"));
                }

                if (result == 2){

                    imageView.setImageResource(R.drawable.right);
                    percentTxt.setText("100 %");
                    percentTxt.setTextColor(Color.parseColor("#086308"));
                }



                // Generate ListView Item using TextView
                return convertView;
            }

        };

        Collections.reverse(fruits_list); // ADD THIS LINE TO REVERSE ORDER!
        // DataBind ListView with items from ArrayAdapter
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String str = arrayAdapter.getItem(i);
                int index1 = str.indexOf("(");
                String Number = str.substring(0, index1);

            //    Toast.makeText(Assess_Log.this,Number.toString(), Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Currently_Que",Number.toString());
                editor.apply();

                //  if(arrayAdapter.getItem(i).toString().equals("Accounts")){
                       Intent ii = new Intent(Assess_Log.this, Assess.class);
                       startActivity(ii);
                       finish();
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

    public void back(View view) {
        Intent ii = new Intent(Assess_Log.this, Assess.class);
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