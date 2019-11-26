package com.example.weatherappdemo.rest;

import com.example.weatherappdemo.R;
import com.example.weatherappdemo.rest.Response.ResponseModel;
import com.example.weatherappdemo.utils.AppContext;
import com.example.weatherappdemo.utils.NetworkUtil;
import com.example.weatherappdemo.utils.ToastUtils;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public abstract class RestCallBack<T> implements Callback<T> {

    public abstract void onFailure(Call<T> call, String message);

    public abstract void onResponse(Call<T> call, Response<T> restResponse, T response);

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (call.isCanceled()) {
            onFailure(call, "");
            return;
        }

        if (NetworkUtil.isInternetAvailable) {
            if (t.getLocalizedMessage() != null)
                onFailure(call, t.getLocalizedMessage());
            else
                onFailure(call, "");
        } else
            onFailure(call, AppContext.getInstance().getContext().getString(R.string.no_internet));

        try {
            ToastUtils.showErrorOnLive(AppContext.getInstance().getContext(), t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        int index = call.request().url().toString().indexOf("?");
        if (index == -1)
            index = call.request().url().toString().length();


    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onResponse(call, response, response.body());
        } else {
            try {
                Gson gson = new Gson();
                ResponseModel responseModel = gson.fromJson(response.errorBody().string(), ResponseModel.class);

                onFailure(call, responseModel.message);

            } catch (Exception e) {
                e.printStackTrace();
                onFailure(call, response.code() + " : " + response.message());
            }
        }
    }


}
