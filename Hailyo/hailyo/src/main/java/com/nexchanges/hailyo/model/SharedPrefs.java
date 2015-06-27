package com.nexchanges.hailyo.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by AbhishekWork on 22/06/15.
 */
public class SharedPrefs {
    //SharedPreferences file name
    private static String SHARED_PREFS_FILE_NAME = "hailyo_shared_prefs";

    //here you can centralize all your shared prefs keys
    public static String NAME_KEY = "nameKey";
    public static String EMAIL_KEY = "emailKey";
    public static String CURRENT_SPEC = "specKey";

    public static String PHOTO_KEY = "myphotoKey";
    public static String PROPERTY_KEY = "myphotoKey";
    public static String RENT_KEY = "myphotoKey";
    public static String DEPOSIT_KEY = "myphotoKey";
    public static String STARTDATE_KEY = "myphotoKey";
    public static String MY_ROLE_KEY = "myroleKey";
    public static String CURRENT_LOC_KEY = "location_key";
    public static String CURRENT_FLIPPER_VIEW = "flipper_key";
    public static String CURRENT_COUNTER_BROKER= "counter_broker_key";

    //get the SharedPreferences object instance
    //create SharedPreferences file if not present


    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    //Save Booleans
    public static void savePref(Context context, String key, boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }

    //Get Booleans
    public static boolean getBoolean(Context context, String key) {
        return getPrefs(context).getBoolean(key, false);
    }

    //Get Booleans if not found return a predefined default value
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPrefs(context).getBoolean(key, defaultValue);
    }

    //Strings
    public static void save(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }

    //Integers
    public static void save(Context context, String key, int value) {
        getPrefs(context).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        return getPrefs(context).getInt(key, 0);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getPrefs(context).getInt(key, defaultValue);
    }

    //Floats
    public static void save(Context context, String key, float value) {
        getPrefs(context).edit().putFloat(key, value).commit();
    }

    public static float getFloat(Context context, String key) {
        return getPrefs(context).getFloat(key, 0);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return getPrefs(context).getFloat(key, defaultValue);
    }

    //Longs
    public static void save(Context context, String key, long value) {
        getPrefs(context).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key) {
        return getPrefs(context).getLong(key, 0);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getPrefs(context).getLong(key, defaultValue);
    }

    //StringSets
    public static void save(Context context, String key, Set<String> value) {
        getPrefs(context).edit().putStringSet(key, value).commit();
    }

    public static Set<String> getStringSet(Context context, String key) {
        return getPrefs(context).getStringSet(key, null);
    }

    public static Set<String> getStringSet(Context context, String key, Set<String> defaultValue) {
        return getPrefs(context).getStringSet(key, defaultValue);
    }
}
