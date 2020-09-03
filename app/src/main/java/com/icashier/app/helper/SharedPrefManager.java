package com.icashier.app.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharedPrefManager {
    private static SharedPrefManager instance;
    private static SharedPreferences sharedPreferences;

    public static SharedPrefManager getInstance(final Context context) {

        if (instance == null) {
            instance = new SharedPrefManager();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return instance;

    }


    public void saveString(final String key, final String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void saveBoolean(final String key, final Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(final String key, final Boolean defVal) {

        return sharedPreferences.getBoolean(key, defVal);
    }

    public String getString(String key, final String dval) {
        return sharedPreferences.getString(key, dval);
    }


    public void removeData(final String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
        editor.commit();

    }


    public void saveInt(final String key, final int value) {

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    public int getInt(final String key, final int defVal) {

        return sharedPreferences.getInt(key, defVal);
    }

    public void saveFloat(final String key, final float value) {

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();

    }

    public float getFloat(final String key, final float defVal) {

        return sharedPreferences.getFloat(key, defVal);
    }
}
