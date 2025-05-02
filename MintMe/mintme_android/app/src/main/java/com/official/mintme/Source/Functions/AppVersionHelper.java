package com.official.mintme.Source.Functions;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.official.mintme.Source.utils.Config;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppVersionHelper {

    public static void checkAppVersion(Activity activity) {
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            Config.setVersionName(pInfo.versionName);
            Loader.loadWebview(activity, activity.getIntent());
            new Thread(() -> checkForUpdate(activity)).start();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void checkForUpdate(Activity activity) {
        try {
            URL url = new URL(Config.get(Config.ConfigKeys.LATEST_VERSION));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);

            int responseCode = httpURLConnection.getResponseCode();
            String line;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                JSONArray jsonArray = new JSONArray(response.toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String andoridValue = jsonObject.getString("Android");
                activity.runOnUiThread(() -> {
                    if (!andoridValue.equals(Config.get(Config.ConfigKeys.VERSION_NAME))) {
                        Loader.getWebView().loadUrl(Config.get(Config.ConfigKeys.UPDATE_FILE_PATH));
                    } else {
                        Loader.getWebView().loadUrl(Config.getAssetLink());
                    }
                });

                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.runOnUiThread(() -> {
            Loader.getWebView().loadUrl(Config.getAssetLink());
        });
    }
}
