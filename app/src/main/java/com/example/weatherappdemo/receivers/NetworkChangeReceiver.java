package com.example.weatherappdemo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;


import com.example.weatherappdemo.utils.AppContext;
import com.example.weatherappdemo.utils.LogUtils;
import com.example.weatherappdemo.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private static List<OnNetworkChangeListener> onNetworkChangeListeners = new ArrayList<>();
    private static List<NetworkChangeReceiver> onNetworkChangeReceiver = new ArrayList<>();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        LogUtils.debug("NetworkChangeReceiver onReceive");

        boolean wasConnected = NetworkUtil.isInternetAvailable;

        try {
            NetworkUtil.getInstance(context).initialize();

            boolean isConnected = NetworkUtil.isInternetAvailable;

            if (wasConnected == isConnected) {
                return;
            }

            LogUtils.debug(" NetworkChangeReceiver onReceive " + NetworkUtil.isInternetAvailable);

            for (OnNetworkChangeListener onNetworkChangeListener : onNetworkChangeListeners) {
                try {
                    onNetworkChangeListener.onNetworkChange(NetworkUtil.isInternetAvailable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void unregister(OnNetworkChangeListener onNetworkChangeListener) {
        try {
            onNetworkChangeListeners.remove(onNetworkChangeListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void register(OnNetworkChangeListener onNetworkChangeListener, NetworkChangeReceiver mNetWorkChangeReciver) {
        try {
            if (!onNetworkChangeListeners.contains(onNetworkChangeListener)) {
                onNetworkChangeListeners.add(onNetworkChangeListener);
                onNetworkChangeReceiver.add(mNetWorkChangeReciver);
                AppContext.getInstance().getContext().registerReceiver(mNetWorkChangeReciver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public interface OnNetworkChangeListener {
        void onNetworkChange(boolean isConnected);
    }

}
