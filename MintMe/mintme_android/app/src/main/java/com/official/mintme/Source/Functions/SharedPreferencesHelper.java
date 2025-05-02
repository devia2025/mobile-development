package com.official.mintme.Source.Functions;

import android.content.Context;
import android.content.SharedPreferences;
import com.official.mintme.Source.utils.Config;

public class SharedPreferencesHelper {
    private final SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(
                Config.get(Config.ConfigKeys.SHARED_PREFERENCES_NAME),
               Context.MODE_PRIVATE);
    }

    public void saveString(String key, String value) {
        if (key != null) {
            sharedPreferences.edit()
                    .putString(key, value)
                    .apply();
        }
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
}
