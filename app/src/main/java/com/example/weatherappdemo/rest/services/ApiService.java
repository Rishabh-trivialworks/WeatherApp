package com.example.weatherappdemo.rest.services;


import com.example.weatherappdemo.constants.AppConstants;
import com.example.weatherappdemo.rest.Response.ResponseModel;
import com.example.weatherappdemo.rest.model.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface ApiService {


    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.LOCATION)
    Call<ResponseModel<Weather>> getWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("id") int id,
            @Query("APPID") String appid);

}
