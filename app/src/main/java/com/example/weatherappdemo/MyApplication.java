package com.example.weatherappdemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.HandlerThread;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.example.weatherappdemo.constants.AppConstants;
import com.example.weatherappdemo.receivers.NetworkChangeReceiver;
import com.example.weatherappdemo.rest.Response.ResponseModel;
import com.example.weatherappdemo.rest.model.Weather;
import com.example.weatherappdemo.utils.AppContext;
import com.example.weatherappdemo.utils.LogUtils;
import com.example.weatherappdemo.utils.NetworkUtil;
import com.example.weatherappdemo.utils.TempStorage;
import com.shawnlin.preferencesmanager.PreferencesManager;

import java.util.ArrayList;
import java.util.List;


public class MyApplication extends MultiDexApplication implements NetworkChangeReceiver.OnNetworkChangeListener, Application.ActivityLifecycleCallbacks {

    private Context context;

    public final static ApiMode apiMode = ApiMode.LIVE;
    public final static boolean SHOW_LOG = true;
    public final static boolean RETROFIT_SHOW_LOG = true;
    public final static boolean TOAST_ERROR_LIVE = false;
    public final static boolean API_DEBUG = false;

    public final static List<Activity> ACTIVITIES = new ArrayList<>();
    public static boolean isAppForeground;
    private NetworkChangeReceiver mNetWorkChangeReciver;

    public enum ApiMode {
        LIVE
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();

        registerActivityLifecycleCallbacks(this);
        MultiDex.install(this);
        AppContext.getInstance().setContext(this);


        initialize(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkChangeReceiver(), filter);

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(new NetworkChangeReceiver(), intentFilter);


        mNetWorkChangeReciver = new NetworkChangeReceiver();
        NetworkChangeReceiver.register(this, mNetWorkChangeReciver);
        setTempStorage();
    }

    public static void initialize(Application context) {
        try {
            NetworkUtil.getInstance(context).initialize();
            // Preference Manger is using for localy storing the object.so that everytime api will not called.
            new PreferencesManager(context).setName(AppConstants.Pref.NAME).init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTempStorage() {
        ResponseModel<Weather> weather = PreferencesManager.getObject(AppConstants.Pref.WEATHERDATA, ResponseModel.class);
        if (weather != null) {
            TempStorage.weather = weather;
        }
    }


    @Override
    public void onNetworkChange(boolean isConnected) {

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        ACTIVITIES.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!isAppForeground) {
            isAppForeground = true;
            LogUtils.debug("App is in Foreground");
            onAppForeground();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ACTIVITIES.remove(activity);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.debug("MyApplication onTrimMemory " + level);
        NetworkChangeReceiver.unregister(this);

    }

    private void onAppForeground() {
        LogUtils.debug("AppStatus: Foreground");
    }

    private void onAppBackground() {
        LogUtils.debug("AppStatus: Background");
    }


}
