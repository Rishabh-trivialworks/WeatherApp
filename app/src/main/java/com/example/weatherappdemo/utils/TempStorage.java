package com.example.weatherappdemo.utils;


import com.example.weatherappdemo.constants.AppConstants;
import com.example.weatherappdemo.rest.Response.ResponseModel;

import com.example.weatherappdemo.rest.model.Weather;
import com.shawnlin.preferencesmanager.PreferencesManager;



public class TempStorage {


    public static ResponseModel<Weather> weather;


    public static String version = "not available";



    public static ResponseModel<Weather> getWeather() {
        return weather;
    }

    public static void setWeather(ResponseModel<Weather> weather) {
        PreferencesManager.putObject(AppConstants.Pref.WEATHERDATA, weather);
    }

//    public static int getUserId() {
//        return userId;
//    }
//
//    public static void setUserId(int userId) {
//        TempStorage.userId = userId;
//    }


}
