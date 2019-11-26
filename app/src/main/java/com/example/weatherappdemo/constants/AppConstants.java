package com.example.weatherappdemo.constants;

public class AppConstants {

    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;

    public static class Pref {
        public static final String NAME = "weatherapp";
        public static final String WEATHERDATA = "weatherdata";
    }

    public static class ApiParamKey {
        public static String DEBUG = "debug";
        public static final String APP_LANGUAGE = "Content-Language";
        public static final String APP_TIMEZONE = "timeZone";
        public static final String APP_VERSION = "appVersion";

    }



    public static final class Url {

        public static String BASE_SERVICE_LIVE = "http://api.openweathermap.org";

        public static final String LOCATION = "/data/2.5/weather";


    }

}
