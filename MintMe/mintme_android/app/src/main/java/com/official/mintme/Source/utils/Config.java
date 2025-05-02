package com.official.mintme.Source.utils;

import android.net.Uri;
import android.webkit.ValueCallback;
import java.util.EnumMap;
import java.util.Map;

public class Config {
    private static ValueCallback<Uri[]> filePathCallback;
    private static String SESSION_TOKEN;
    private static String SESSION_EXPIRED;
    private static String ASSET_LINK;
    private static final Map<ConfigKeys, String> configMap = new EnumMap<>(ConfigKeys.class);
    public enum ConfigKeys {
        WEBSITE_URL,
        USER_AGENT,
        OFFLINE_FILE_PATH,
        UPDATE_FILE_PATH,
        LAST_VISIT,
        FILE_CHOOSER_RESULT_CODE,
        VERSION_NAME,
        LATEST_VERSION,
        CHOOSE_FILE,
        CHOOSE_FILE_FILTER,
        SHARED_PREFERENCES_NAME,
        INTERNET_CHECKER_URL,
        DEFAULT_SESSION_KEY,
        MAIN_SESSION_KEY,
        MAIN_SESSION_VALUE,
        SESSION_EXPIRE_STRING,
        SESSION_HEADER_FIELD,
        OLD_VERSION_MESSAGE,
        SHORT_STORE_INTENT_URL,
        FULL_STORE_INTENT_URL,
        DIALOG_RETRY,
        DIALOG_ACCEPT,
        DIALOG_DECLINE,
        ANDROID_WEB_INTERFACE,
        DOWNLOAD_TITLE,
        DOWNLOAD_FINISHED,
        DOWNLOAD_FAILED,
        BLOB_UTIL,
        WEB_SOURCE,
        WRITE_PERMISSION_TOAST,
        BACKUP_CODE_FILENAME,
        FILE_REMOVED,
        TWO_FACTORY_TITLE,
        CANEL_DIALOG,
        COPY_DIALOG,
        COPIED_TO_CLIPBOARD,
        EXIT_DIALOG,
        SAVE_FILE_ERROR,
        CLOSE_APP_ALERT_DIALOG,
        WEB_ERROR_DEFAULT,
        WEB_ERROR_NOT_TRUSTED,
        WEB_ERROR_SSL_EXPIRED,
        WEB_ERROR_SSL_IDMISMATCH,
        WEB_ERROR_SSL_NOTYETVALID,
    }
    static {
        configMap.put(ConfigKeys.WEBSITE_URL, "https://www.mintme.com/");
        configMap.put(ConfigKeys.USER_AGENT, "Mozilla/5.0 (Linux; Android 10; Pixel 3; rv:83.0) Gecko/83.0 Firefox/83.0");
        configMap.put(ConfigKeys.OFFLINE_FILE_PATH, "//android_asset/html/offline.html");
        configMap.put(ConfigKeys.UPDATE_FILE_PATH, "//android_asset/html/update.html");
        configMap.put(ConfigKeys.LAST_VISIT, "Url");
        configMap.put(ConfigKeys.FILE_CHOOSER_RESULT_CODE, "1");
        configMap.put(ConfigKeys.LATEST_VERSION, "https://www.mintme.com/mobile-version.json");
        configMap.put(ConfigKeys.CHOOSE_FILE, "Choose File");
        configMap.put(ConfigKeys.CHOOSE_FILE_FILTER, "*/*");
        configMap.put(ConfigKeys.SHARED_PREFERENCES_NAME, "Helper");
        configMap.put(ConfigKeys.INTERNET_CHECKER_URL, "https://www.gstatic.com/generate_204");
        configMap.put(ConfigKeys.DEFAULT_SESSION_KEY, "mercureAuthorization=");
        configMap.put(ConfigKeys.MAIN_SESSION_KEY, "MintMeDeviceSession=");
        configMap.put(ConfigKeys.MAIN_SESSION_VALUE, "; Path=/; SameSite=None; Secure; Expires=");
        configMap.put(ConfigKeys.SESSION_EXPIRE_STRING, "expires=");
        configMap.put(ConfigKeys.SESSION_HEADER_FIELD, "Set-Cookie");
        configMap.put(ConfigKeys.OLD_VERSION_MESSAGE, "An older version of the app is currently installed.");
        configMap.put(ConfigKeys.SHORT_STORE_INTENT_URL, "market://details?id=");
        configMap.put(ConfigKeys.FULL_STORE_INTENT_URL, "https://play.google.com/store/apps/details?id=");
        configMap.put(ConfigKeys.DIALOG_RETRY, "TRY AGAIN");
        configMap.put(ConfigKeys.DIALOG_ACCEPT, "YES");
        configMap.put(ConfigKeys.DIALOG_DECLINE, "NO");
        configMap.put(ConfigKeys.ANDROID_WEB_INTERFACE, "Android");
        configMap.put(ConfigKeys.DOWNLOAD_TITLE, "Downloading your 2fa, please wait...");
        configMap.put(ConfigKeys.DOWNLOAD_FINISHED, "Download complete: ");
        configMap.put(ConfigKeys.DOWNLOAD_FAILED, "Download failed. Please try again.");
        configMap.put(ConfigKeys.BLOB_UTIL, "blob:");
        configMap.put(ConfigKeys.WEB_SOURCE, "(function() { return document.documentElement.outerHTML; })();");
        configMap.put(ConfigKeys.WRITE_PERMISSION_TOAST, "Permission required to write to external storage");
        configMap.put(ConfigKeys.BACKUP_CODE_FILENAME, "backup-codes.txt");
        configMap.put(ConfigKeys.FILE_REMOVED, "File not found. It may have been moved or deleted");
        configMap.put(ConfigKeys.TWO_FACTORY_TITLE, "Please save 2FA Codes: ");
        configMap.put(ConfigKeys.CANEL_DIALOG, "CANCEL");
        configMap.put(ConfigKeys.COPY_DIALOG, "COPY");
        configMap.put(ConfigKeys.COPIED_TO_CLIPBOARD, "Copied to clipboard");
        configMap.put(ConfigKeys.EXIT_DIALOG, "EXIT");
        configMap.put(ConfigKeys.SAVE_FILE_ERROR, "Error saving data file");
        configMap.put(ConfigKeys.CLOSE_APP_ALERT_DIALOG, "Are you sure you want to close MintMe app?");
        configMap.put(ConfigKeys.WEB_ERROR_DEFAULT, "SSL Certificate error.");
        configMap.put(ConfigKeys.WEB_ERROR_NOT_TRUSTED, "The Certificate is not trusted.");
        configMap.put(ConfigKeys.WEB_ERROR_SSL_EXPIRED, "The certificate has expired.");
        configMap.put(ConfigKeys.WEB_ERROR_SSL_IDMISMATCH, "The certificate Hostname mismatch.");
        configMap.put(ConfigKeys.WEB_ERROR_SSL_NOTYETVALID, "The certificate is not yet valid.");
    }


    public static String get(ConfigKeys key) {return configMap.get(key);}

    public static void setFilePathCallback(ValueCallback<Uri[]> callback) {filePathCallback = callback;}

    public static ValueCallback<Uri[]> getFilePathCallback() {return filePathCallback;}

    public static void setSessionDetails(String token, String expired) {
        SESSION_TOKEN = token;
        SESSION_EXPIRED = expired;
    }

    public static String getSessionToken() {return SESSION_TOKEN;}

    public static String getSessionExpired() {return SESSION_EXPIRED;}

    public static void setAssetLink(String assetLink) {ASSET_LINK = assetLink;}

    public static String getAssetLink() {return ASSET_LINK;}

    public static int getFileChooserResultCode() {
        return Integer.parseInt(configMap.get(ConfigKeys.FILE_CHOOSER_RESULT_CODE));
    }

    public static void setVersionName(String version) {configMap.put(ConfigKeys.VERSION_NAME, version);}
}
