package com.official.mintme;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ValueCallback;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import com.official.mintme.Source.Functions.AppVersionHelper;
import com.official.mintme.Source.Functions.Loader;
import com.official.mintme.Source.Functions.SharedPreferencesHelper;
import com.official.mintme.Source.utils.Config;
import android.Manifest;
import android.widget.TextView;
import java.util.Objects;

public class Main extends AppCompatActivity {

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this);
        Intent appLinkIntent = getIntent();
        Uri appLinkData = appLinkIntent.getData();

        String lastVisit = sharedPreferencesHelper.getString(
                Config.get(Config.ConfigKeys.WEBSITE_URL), null);

        Config.setAssetLink((appLinkData != null && !appLinkData.toString().isEmpty())
            ? appLinkData.toString()
            : (lastVisit != null && !lastVisit.isEmpty())
            ? lastVisit
            : Config.get(Config.ConfigKeys.WEBSITE_URL));
        AppVersionHelper.checkAppVersion(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right,
                systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Config.getFileChooserResultCode()) {
            ValueCallback<Uri[]> filePathCallback = Config.getFilePathCallback();

            if (filePathCallback != null) {
                Uri[] results = null;

                if (resultCode == RESULT_OK && data != null) {
                    results = new Uri[]{data.getData()};
                }

                filePathCallback.onReceiveValue(results);
                Config.setFilePathCallback(null);
            }
        }
    }

    @Override
    public void onBackPressed() {
        String previousUrl = Loader.getPreviousUrl();

        if (Loader.getWebView().canGoBack()) {

            if (previousUrl != null && previousUrl.contains(Config.get(Config.ConfigKeys.OFFLINE_FILE_PATH))){
                return;
            }else{
                Loader.getWebView().goBack();
            }
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.alert, null);
            TextView dialogMessage = dialogLayout.findViewById(R.id.dialog_message);
            dialogMessage.setText(Config.get(Config.ConfigKeys.CLOSE_APP_ALERT_DIALOG));
            AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogLayout)
                .setCancelable(false)
                .setPositiveButton(Config.get(Config.ConfigKeys.DIALOG_ACCEPT), (dialogInterface, id) -> {
                    super.onBackPressed();
                })
                .setNegativeButton(Config.get(Config.ConfigKeys.DIALOG_DECLINE), null)
                .create();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.rounded_border);
            dialog.show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Loader.getWebView().saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Loader.getWebView().restoreState(savedInstanceState);
    }
}
