package com.example.weatherappdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.weatherappdemo.R;


public class NetworkUtil {

    public static boolean isInternetAvailable;
    private static Context context;
//    public boolean connectivityType;

    private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;

    private static NetworkUtil networkUtil;

    private NetworkUtil(Context context) {
        this.context = context;
    }

    public static NetworkUtil getInstance(Context context) {
        if (networkUtil == null) {
            networkUtil = new NetworkUtil(context);
            NetworkUtil.context = context;
        }
        return networkUtil;
    }

    public NetworkUtil initialize() {
        isInternetAvailable = isConnected();
        return networkUtil;
    }

    private int getConnectivityStatus() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    private boolean isConnected() {
        int connectivityStatus = getConnectivityStatus();
        if(connectivityStatus == TYPE_WIFI || connectivityStatus == TYPE_MOBILE) {
            return true;
        }
        else if (connectivityStatus == TYPE_NOT_CONNECTED){
            return false;
        }
        return false;
    }

    public static void handleNoInternet(Context context){
        ToastUtils.showInternetFailure(context, context.getResources().getString(R.string.no_internet));
    }


//    public String getConnectivityStatusString(Context context) {
//        int conn = NetworkUtil.getConnectivityStatus(context);
//        String status = null;
//        if (conn == NetworkUtil.TYPE_WIFI) {
//            status = "Wifi enabled";
//        } else if (conn == NetworkUtil.TYPE_MOBILE) {
//            status = "Mobile data enabled";
//        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
//            status = "Not connected to Internet";
//        }
//        return status;
//    }
}