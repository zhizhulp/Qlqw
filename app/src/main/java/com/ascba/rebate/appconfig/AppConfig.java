package com.ascba.rebate.appconfig;

import android.content.Context;
import android.content.SharedPreferences;

import com.ascba.rebate.application.MyApplication;

/**
 * Created by 李平 on 2017/8/11.
 * SharedPreferences管理
 */

public class AppConfig {
    private static AppConfig appConfig;

    private SharedPreferences preferences;


    private AppConfig() {
        preferences = MyApplication.getInstance().getSharedPreferences("app_config", Context.MODE_PRIVATE);
    }

    public static AppConfig getInstance() {
        if (appConfig == null)
            synchronized (AppConfig.class) {
                if (appConfig == null)
                    appConfig = new AppConfig();
            }
        return appConfig;
    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public void putString(String key, String value) {
        if (value != null && !value.equals(getString(key, null))) {
            preferences.edit().putString(key, value).apply();
        }
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public void putLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public void clearXml() {
        preferences.edit().clear().apply();
    }
}
