package com.example.weatherappdemo.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.weatherappdemo.MyApplication;
import com.example.weatherappdemo.R;


public class ToastUtils {
    public static boolean showServerMessage = true;

    public static void showFailureResponse(Context context, int messageResource) {
        showFailureResponse(context, context.getString(messageResource));
    }

    public static void showFailureResponse(Context context, String message) {
        if (message.toLowerCase().equals("internet_error") || message.toLowerCase().contains("UnknownHostException".toLowerCase())) {
            message = context.getString(R.string.no_internet);
        }

        if (showServerMessage && !message.isEmpty()) {
            showToast(context, message, Toast.LENGTH_SHORT);
        } else {
            showToast(context, context.getResources().getString(R.string.please_try_later), Toast.LENGTH_SHORT);
        }
    }

    public static void showErrorOnLive(Context context, String message) {
        if (MyApplication.TOAST_ERROR_LIVE) {
            showToast(context, message, Toast.LENGTH_LONG);
        }
    }

    public static void show(Context context, String message, int duration) {
        showToast(context, message, duration);
    }

    public static void show(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int messageResId) {
        showToast(context, context.getString(messageResId), Toast.LENGTH_SHORT);
    }

    public static void showInternetFailure(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    private static Toast toast;

    public static void showToast(Context context, String message, int duration) {
        if (message.isEmpty()) {
            return;
        }
        if (toast == null)
            toast = Toast.makeText(context, message, duration);
        else
            toast.setText(message);
        toast.show();

    }
}

