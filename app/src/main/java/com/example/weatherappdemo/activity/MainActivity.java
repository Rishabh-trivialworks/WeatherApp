package com.example.weatherappdemo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.weatherappdemo.R;
import com.example.weatherappdemo.constants.AppConstants;
import com.example.weatherappdemo.eventbus.Events;
import com.example.weatherappdemo.receivers.AlarmBroadCastReciever;
import com.example.weatherappdemo.rest.Response.ResponseModel;
import com.example.weatherappdemo.rest.RestCallBack;
import com.example.weatherappdemo.rest.RestServiceFactory;
import com.example.weatherappdemo.rest.model.Weather;
import com.example.weatherappdemo.utils.GpsUtils;
import com.example.weatherappdemo.utils.NetworkUtil;
import com.example.weatherappdemo.utils.TempStorage;
import com.example.weatherappdemo.utils.ToastUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.cityText)
    TextView cityText;
    @BindView(R.id.condDescr)
    TextView condDescr;
    @BindView(R.id.temp)
    TextView temp;
    @BindView(R.id.press)
    TextView press;
    @BindView(R.id.windSpeed)
    TextView windSpeed;
    @BindView(R.id.windDeg)
    TextView windDeg;
    @BindView(R.id.humidityValue)
    TextView hummidity;

    int id = 7778677;
    String apikey;
    Context context;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private double latitude, longitude;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Weather App ");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // butterknife view binding or view injection library in our app.
        ButterKnife.bind(this);
        context = this;
        apikey = getString(R.string.app_key);
        EventBus.getDefault().register(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        new GpsUtils(this).turnGPSOn(isGPSEnable -> {
            // turn on GPS
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
            }
        };

        setAlarm();
        getLocation();

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity) context, location -> {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    if (TempStorage.getWeather() == null) {
                        hitApiToGetWather();
                    } else {
                        ResponseModel<Weather> weatherData = TempStorage.getWeather();
                        cityText.setText(String.format(weatherData.getName() + "," + weatherData.getSys().getCountry()));
                        condDescr.setText(String.format(weatherData.getWeather().get(0).getMain() + "(" + weatherData.getWeather().get(0).getDescription() + ")"));
                        temp.setText(String.format("" + Math.round((weatherData.getMain().getTemp() - 273.15)) + "�C"));
                        hummidity.setText(String.format("" + weatherData.getMain().getHumidity()));
                        press.setText(String.format("" + weatherData.getMain().getPressure() + " hPa"));
                        windSpeed.setText(String.format("" + weatherData.getWind().getSpeed() + " mps"));
                        windDeg.setText(String.format("" + weatherData.getWind().getDeg() + "�"));
                    }
                } else {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                if (TempStorage.getWeather() == null) {
                                    hitApiToGetWather();
                                } else {
                                    ResponseModel<Weather> weatherData = TempStorage.getWeather();
                                    cityText.setText(String.format(weatherData.getName() + "," + weatherData.getSys().getCountry()));
                                    condDescr.setText(String.format(weatherData.getWeather().get(0).getMain() + "(" + weatherData.getWeather().get(0).getDescription() + ")"));
                                    temp.setText(String.format("" + Math.round((weatherData.getMain().getTemp() - 273.15)) + "�C"));
                                    hummidity.setText(String.format("" + weatherData.getMain().getHumidity()));
                                    press.setText(String.format("" + weatherData.getMain().getPressure() + " hPa"));
                                    windSpeed.setText(String.format("" + weatherData.getWind().getSpeed() + " mps"));
                                    windDeg.setText(String.format("" + weatherData.getWind().getDeg() + "�"));
                                }
                            } else {
                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        }
                    });

                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void setAlarm() {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadCastReciever.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);


        // setRepeating() lets you specify a precise custom interval--in this case,
        // 120 minutes.
        assert alarmMgr != null;
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 60 * 120, alarmIntent);
    }

    private void hitApiToGetWather() {
        // for network connectivity if there is mobile or WI-FI available then only api will connect to server and get response
        if (!NetworkUtil.isInternetAvailable) {
            NetworkUtil.handleNoInternet(context);
            return;
        }
        Call<ResponseModel<Weather>> responseModelCall = RestServiceFactory.createService().getWeather(latitude, longitude, id, apikey);

        responseModelCall.enqueue(new RestCallBack<ResponseModel<Weather>>() {
            @Override
            public void onFailure(Call<ResponseModel<Weather>> call, String message) {
                ToastUtils.show(context, message);
            }

            @Override
            public void onResponse(Call<ResponseModel<Weather>> call, Response<ResponseModel<Weather>> restResponse, ResponseModel<Weather> response) {
                if (response != null && response.getCod() == 200) {
                    TempStorage.setWeather(response);
                    cityText.setText(String.format(response.getName() + "," + response.getSys().getCountry()));
                    condDescr.setText(String.format(response.getWeather().get(0).getMain() + "(" + response.getWeather().get(0).getDescription() + ")"));
                    temp.setText(String.format("" + Math.round((response.getMain().getTemp() - 273.15)) + "�C"));
                    hummidity.setText(String.format("" + response.getMain().getHumidity()));
                    press.setText(String.format("" + response.getMain().getPressure() + " hPa"));
                    windSpeed.setText(String.format("" + response.getWind().getSpeed() + " mps"));
                    windDeg.setText(String.format("" + response.getWind().getDeg() + "�"));
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshApi(final Events.RefreshApi refreshApi) {
        if (TempStorage.weather != null) {
            hitApiToGetWather();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
