package com.nexchanges.hailyo.utils;

import android.util.Log;

import com.nexchanges.hailyo.BuildConfig;

/**
 * Created by AbhishekWork on 27/06/15.
 */
public class Logger {

    private static final String TAG = "Logger";

    public enum LogLevel {
        VERBOSE, DEBUG, INFO, ERROR, WARN;
    }

    public static void writeLogs(LogLevel logLevel, String message) {
        if (BuildConfig.DEBUG) {
            switch (logLevel) {
                case DEBUG:
                    Log.d(TAG, message);
                    break;
                case VERBOSE:
                    Log.v(TAG, message);
                    break;
                case INFO:
                    Log.i(TAG, message);
                    break;
                case ERROR:
                    Log.e(TAG, message);
                    break;
                case WARN:
                    Log.w(TAG, message);
                    break;
            }
        }

    }
}

