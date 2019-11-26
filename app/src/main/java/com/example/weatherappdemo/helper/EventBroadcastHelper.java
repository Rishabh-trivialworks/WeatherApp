package com.example.weatherappdemo.helper;


import com.example.weatherappdemo.eventbus.Events;
import com.example.weatherappdemo.eventbus.GlobalBus;


public class EventBroadcastHelper {


    public static void sendRefreshApi() {
        GlobalBus.getBus().post(new Events.RefreshApi());
    }

}
