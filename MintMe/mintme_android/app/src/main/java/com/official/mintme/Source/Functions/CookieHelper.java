package com.official.mintme.Source.Functions;

import android.os.AsyncTask;
import android.webkit.CookieManager;
import com.official.mintme.Source.utils.Config;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CookieHelper {

    public static void getDefaultSession() {
        new FetchCookiesTask().execute(Config.get(Config.ConfigKeys.WEBSITE_URL));
    }

    /** @noinspection deprecation*/
    private static class FetchCookiesTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            try {
                fetchCookiesFromUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    private void fetchCookiesFromUrl(String urlString) throws IOException {
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String setCookieHeader = urlConnection.getHeaderField(Config.get(Config.ConfigKeys.SESSION_HEADER_FIELD));
                if (setCookieHeader != null) {
                    processCookies(setCookieHeader);
                }
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private void processCookies(String setCookieHeader) {
        String[] cookies = setCookieHeader.split("; ");
        String token = null;
        String expired = null;

        for (String cookie : cookies) {
            String trimmedCookie = cookie.trim();

            if (trimmedCookie.startsWith(Config.get(Config.ConfigKeys.DEFAULT_SESSION_KEY))) { // Token index
                token = trimmedCookie.split("=")[1].trim();
            }

            if (trimmedCookie.startsWith(Config.get(Config.ConfigKeys.SESSION_EXPIRE_STRING))) { // Expiry index
                expired = trimmedCookie.split("=")[1].trim();
            }
        }

        if (token != null && expired != null) {
            Config.setSessionDetails(token, expired);
        }

        saveCookieManager();
    }

    static void saveCookieManager() {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(Config.get(Config.ConfigKeys.WEBSITE_URL));

        if (Config.getSessionToken() != null && !Config.getSessionToken().isEmpty()) {
            String cookieValue = Config.get(Config.ConfigKeys.MAIN_SESSION_KEY) + Config.getSessionToken() +
                    Config.get(Config.ConfigKeys.MAIN_SESSION_VALUE) + Config.getSessionExpired() + ";";

            if (cookies == null || !cookies.contains(Config.get(Config.ConfigKeys.MAIN_SESSION_KEY))) {
                cookieManager.setCookie(Config.get(Config.ConfigKeys.WEBSITE_URL), cookieValue);
            }

        }
    }
}
}
