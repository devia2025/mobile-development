package com.official.talaka.More;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.official.talaka.Activity.Home;
import com.official.talaka.Activity.Settings;
import com.official.talaka.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private Handler handler = new Handler();
    private boolean isBusy = false;//this flag to indicate whether your async task completed or not
    private boolean stop = false;//this flag to indicate whether your button stop clicked
    String Username,Call_History_Saved,Recieve_History_Saved;
    SharedPreferences sharedPreferences;
    String IP = "http://192.168.1.100/";
    RequestQueue requestQueue;
    String Call_History,Recieve_History,Work_type;
    SharedPreferences preferences;
    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Talaka Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.image)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread

      //  MainActivity mainActivity = new MainActivity();
     //  mainActivity.testy();

        //stopSelf();

        handler.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
             //   if(!isBusy) callAysncTask();
             //   System.out.println("Starting...");
                if(!stop) startHandler();
            }
        }, 0);



        return START_NOT_STICKY;
    }

    private void callAysncTask()
    {
        //TODO

        if(!stop) startHandler();
    }

    private void startHandler()
    {
        handler.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {

                if(!isBusy) callAysncTask();


             //   Home mainActivity = new Home();
             //   mainActivity.History_Log();

              //  Toast.makeText(ForegroundService.this.getApplicationContext(),"My Awesome service toast...",Toast.LENGTH_SHORT).show();

                History_Log();
            }
        }, 12000);

        //TODO

    }



    private void History_Log(){
        //link

            Username = preferences.getString("Username", "");
            Call_History_Saved = preferences.getString("Call_History", "");

            Recieve_History_Saved = preferences.getString("Recieve_History", "");
            Work_type = preferences.getString("Worker", "");


            String url = IP.toString() + "Talaka/Users/Info/Check.php?username=" + Username.toString();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray jsonArray1 = response.getJSONArray("Info");


                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject employee = jsonArray1.getJSONObject(i);
                            ///  Call_Rate = employee.getString("Call_Rate");
                            ///   Recieve_Rate  = employee.getString("Recieve_Rate");


                            Call_History = employee.getString("Call_History");
                            Recieve_History  = employee.getString("Recieve_History");


                            if (Work_type.toString().equals("Call")){

                                /////////////
                                if (Call_History.toString().equals("")){
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("Call_History","");
                                    editor.apply();
                                }
                                //////////////////

                                if (!employee.getString("Call_History").toString().equals("")){
                                    if (!Call_History_Saved.toString().equals(employee.getString("Call_History"))){
                                        Notifi1();
                                        Stoping();

                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("Call_History",employee.getString("Call_History"));

                                        editor.apply();



                                    }
                                }

                            }else{

                                if (Work_type.toString().equals("Online")){


                                    if (Recieve_History.toString().equals("")){

                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("Recieve_History","");

                                        editor.apply();

                                    }
                                    ////////////
                                    if (!Recieve_History.toString().equals("")){
                                        if (!Recieve_History_Saved.toString().equals(employee.getString("Recieve_History"))){
                                            Notifi2();
                                            Stoping();

                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("Recieve_History",employee.getString("Recieve_History"));

                                            editor.apply();





                                        }
                                    }
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
                    //error.printStackTrace();
                }
            });
            requestQueue.add(request);


    }

private void Stoping(){
    try {
        Intent serviceIntent = new Intent(ForegroundService.this.getApplicationContext(), ForegroundService.class);
        stopService(serviceIntent);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    private void Notifi1(){
    Uri SoundUri = RingtoneManager.getDefaultUri((RingtoneManager.TYPE_NOTIFICATION));
    NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

    if (Build.VERSION.SDK_INT > 25) {
        NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        Ringtone r = RingtoneManager.getRingtone(getBaseContext(), SoundUri);
        r.play();
    }

    Intent Call_log = new Intent(this, Settings.class);
    PendingIntent result = PendingIntent.getActivity(this, 1, Call_log, PendingIntent.FLAG_UPDATE_CURRENT);

    NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
            .setVibrate(new long[] { 350, 350})
            .setSmallIcon(R.drawable.image)
            .setContentTitle("Talaka-Notification |Online")
            .setSound(SoundUri)
            .setContentText("Show More ▼")
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setStyle(new NotificationCompat.BigTextStyle().bigText("Online User Viewed You\nFrom Settings > Call History"))
            .setContentIntent(result);
    notificationManager.notify(2, notification.build());

}
    private void Notifi2(){
        Uri SoundUri = RingtoneManager.getDefaultUri((RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT > 25) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

            Ringtone r = RingtoneManager.getRingtone(getBaseContext(), SoundUri);
            r.play();
        }

        Intent Call_log = new Intent(this, Settings.class);
        PendingIntent result = PendingIntent.getActivity(this, 1, Call_log, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setVibrate(new long[] { 350, 350})
                .setSmallIcon(R.drawable.image)
                .setContentTitle("Talaka-Notification |Caller")
                .setSound(SoundUri)
                .setContentText("Show More ▼")
                .setGroupSummary(true)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Caller User Viewed You\nFrom Settings > Recieve History"))
                .setContentIntent(result);
        notificationManager.notify(2, notification.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

       Intent serviceIntent = new Intent(this, ForegroundService.class);
      stopService(serviceIntent);

        stopForeground(false);

      //  System.exit(0);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

    }



}