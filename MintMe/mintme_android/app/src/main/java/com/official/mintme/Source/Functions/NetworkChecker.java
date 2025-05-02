package com.official.mintme.Source.Functions;

import android.app.Activity;
import com.official.mintme.R;
import java.util.Objects;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.official.mintme.Source.utils.Config;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.net.http.SslError;

public class NetworkChecker {

    public static void pingServer(final PingCallback callback) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(() -> {
            boolean isConnected;
            HttpURLConnection httpURLConnection = null;

            try {
                URL url = new URL(Config.get(Config.ConfigKeys.INTERNET_CHECKER_URL));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(2000);
                httpURLConnection.connect();
                isConnected = (httpURLConnection.getResponseCode() == 204);
            } catch (Exception e) {
                e.printStackTrace();
                isConnected = false;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            boolean finalIsConnected = isConnected;
            handler.post(() -> {
                if (callback != null) {
                    callback.onPingResult(finalIsConnected);
                }
            });
        });
    }

    public interface PingCallback {
        void onPingResult(boolean isConnected);
    }

    public static void handleSSLException(Activity activity, SslError error) {
        String errorMsg = getErrorMessageForSslError(error);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.alert, null);
        TextView dialogMessage = dialogLayout.findViewById(R.id.dialog_message);
        dialogMessage.setText(errorMsg);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogLayout)
                .setCancelable(false)
                .setPositiveButton(Config.get(Config.ConfigKeys.DIALOG_RETRY), (dialogInterface, id) -> {
                    System.exit(0);
                })
                .create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.rounded_border);
        dialog.show();
    }

    private static String getErrorMessageForSslError(SslError error) {
        switch (error.getPrimaryError()) {
            case SslError.SSL_UNTRUSTED:
                return Config.get(Config.ConfigKeys.WEB_ERROR_NOT_TRUSTED);
            case SslError.SSL_EXPIRED:
                return Config.get(Config.ConfigKeys.WEB_ERROR_SSL_EXPIRED);
            case SslError.SSL_IDMISMATCH:
                return Config.get(Config.ConfigKeys.WEB_ERROR_SSL_IDMISMATCH);
            case SslError.SSL_NOTYETVALID:
                return Config.get(Config.ConfigKeys.WEB_ERROR_SSL_NOTYETVALID);
            default:
                return Config.get(Config.ConfigKeys.WEB_ERROR_DEFAULT);
        }
    }
}
