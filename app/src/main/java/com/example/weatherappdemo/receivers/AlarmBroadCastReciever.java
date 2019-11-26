package com.example.weatherappdemo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.weatherappdemo.helper.EventBroadcastHelper;

public class AlarmBroadCastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        EventBroadcastHelper.sendRefreshApi();
    }


}
