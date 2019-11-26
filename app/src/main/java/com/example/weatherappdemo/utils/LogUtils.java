package com.example.weatherappdemo.utils;

import android.util.Log;

import com.example.weatherappdemo.MyApplication;


public class LogUtils {
    public static void networkError(String msg) {
        log("Network", "networkError" + msg);
    }

    public static void networkSuccess(String msg) {
        log("Network", "networkSuccess " + msg);
    }

    public static void xmppDebug(String msg) {
        log("XmppVarun", "Varun XMPP : " + msg);
    }

    public static void database(String msg) {
        log("DataBaseVarun", "SQL: " + msg);
    }

    public static void debug(String msg) {
        log("VarunDebug", msg);
    }

    private static void log(String tag, String msg) {
        if (MyApplication.SHOW_LOG) {
            Log.d(tag, msg);
        }
    }

}
