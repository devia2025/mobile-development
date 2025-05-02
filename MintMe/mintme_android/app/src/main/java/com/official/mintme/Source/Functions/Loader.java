package com.official.mintme.Source.Functions;

import static androidx.core.content.ContextCompat.startActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.net.http.SslError;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.official.mintme.R;
import com.official.mintme.Source.utils.Config;
import java.util.ArrayList;

public class Loader {
    private static WebView web;
    private static final int TRUNCATED_CHARS = 90000;
    private static SharedPreferencesHelper sharedPreferencesHelper;
    private static final ArrayList<String> urls = new ArrayList<>();

    @SuppressLint({"SetJavaScriptEnabled","JavascriptInterface"})
    public static void loadWebview(Activity activity, Intent intent) {
        sharedPreferencesHelper = new SharedPreferencesHelper(activity);
        ProgressBar progressBar = activity.findViewById(R.id.progressBar);
        CookieManager cookieManager = CookieManager.getInstance();

        web = activity.findViewById(R.id.web);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFD700, PorterDuff.Mode.SRC_IN);
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(web, true);
        CookieHelper.getDefaultSession();
        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                web.setVisibility(View.VISIBLE);

                if (!url.contains(Config.get(Config.ConfigKeys.OFFLINE_FILE_PATH))) {
                    sharedPreferencesHelper.saveString(Config.get(Config.ConfigKeys.WEBSITE_URL), url);
                    storeUrl(url);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Ping(view);
            }

            @SuppressLint("WebViewClientOnReceivedSslError")
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.cancel(); // ensures that the WebView does not proceed with the insecure connection
                NetworkChecker.handleSSLException(activity, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                if (!url.startsWith(Config.get(Config.ConfigKeys.WEBSITE_URL))) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(activity, intent, null);
                    return true;
                }

                view.loadUrl(url);
                return true;
            }
        });
        web.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            downloadFile(url, activity);
        });
        web.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Config.setFilePathCallback(filePathCallback);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(Config.get(Config.ConfigKeys.CHOOSE_FILE_FILTER));
                activity.startActivityForResult(Intent.createChooser(intent,
                        Config.get(Config.ConfigKeys.CHOOSE_FILE)), Config.getFileChooserResultCode());
                return true;
            }

        });

        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setUserAgentString(Config.get(Config.ConfigKeys.USER_AGENT));
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        web.addJavascriptInterface(new WebAppInterface(activity), Config.get(Config.ConfigKeys.ANDROID_WEB_INTERFACE));
        cookieManager.flush();
    }

    private static void Ping(WebView web){
        NetworkChecker.pingServer(new NetworkChecker.PingCallback() {

            @Override
            public void onPingResult(boolean isConnected) {
                if (!isConnected) {
                    storeUrl(Config.get(Config.ConfigKeys.OFFLINE_FILE_PATH));

                    if (web != null) {
                        web.post(() -> web.loadUrl(Config.get(Config.ConfigKeys.OFFLINE_FILE_PATH)));
                    }
                }
            }
        });
    }

    private static void downloadFile(String url, Activity activity) {
        if (url.startsWith(Config.get(Config.ConfigKeys.BLOB_UTIL))) {
            RetrieveData.retrieveAndShowFileContent(activity);
        } else {
            web.evaluateJavascript(Config.get(Config.ConfigKeys.WEB_SOURCE),
                new ValueCallback<String>() {

                    @Override
                    public void onReceiveValue(String html) {
                        RetrieveData.saveLocalData(html.substring(TRUNCATED_CHARS),activity);
                    }
                }
            );
        }
    }

    public static class WebAppInterface {
        Activity activity;

        WebAppInterface(Activity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        public void loadLastUrl() {
            String lastVisit = sharedPreferencesHelper.getString(
                    Config.get(Config.ConfigKeys.LAST_VISIT), null);

            if (lastVisit != null && !lastVisit.isEmpty()) {
                activity.runOnUiThread(() -> {
                    web.loadUrl(lastVisit);
                });
            }
        }

        @JavascriptInterface
        public void updateApp() {
            String packageName = activity.getPackageName();
            Intent intent = new Intent(Intent.ACTION_VIEW);

            try {
                intent.setData(Uri.parse(Config.get(Config.ConfigKeys.SHORT_STORE_INTENT_URL) + packageName));
                activity.startActivity(intent);
            } catch (android.content.ActivityNotFoundException error) {
                intent.setData(Uri.parse(Config.get(Config.ConfigKeys.FULL_STORE_INTENT_URL) + packageName));
                activity.startActivity(intent);
            }
        }
    }

    private static void storeUrl(String url) {
        if (!urls.contains(url)) {
            if (urls.size() >= 3) {
                urls.remove(0);
            }
            urls.add(url);
        }
    }

    public static String getPreviousUrl() {
        if (urls.size() > 1) {
            return urls.get(urls.size() - 2);
        }
        return null;
    }

    public static WebView getWebView() {
        return web;
    }
}
