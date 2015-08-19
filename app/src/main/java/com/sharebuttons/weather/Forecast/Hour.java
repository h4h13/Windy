package com.sharebuttons.weather.Forecast;

import com.sharebuttons.weather.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Monkey D Luffy on 7/25/2015.
 */
public class Hour {
    private long mTime;
    private String mDay;
    private double mTemperature;
    private String mIcon;


    public Hour(String day, double temperature, String icon) {
        mTemperature = temperature;
        mIcon = icon;
        mDay = day;
    }


    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public Hour(long time, double temperature, String icon) {
        mTemperature = temperature;
        mIcon = icon;
        mTime = time;
    }

    public String getIcon() {
        return mIcon;
    }

    public long getTime() {
        return mTime;
    }

    public double getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public String getFormattedTime() {
        SimpleDateFormat format = new SimpleDateFormat("K a");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(new Date(mTime * 1000));
    }

    public String getFormatedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("H");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(new Date());
    }

    public int getIconId() {

        int iconId = R.drawable.sunny;
        int timeNow = Integer.parseInt(getFormatedTime());
        boolean nightTime = timeNow < 6 || timeNow > 18;

        switch (getIcon()) {
            case "clear-day":
                iconId = R.drawable.sunny;
                break;
            case "clear-night":
                iconId = R.drawable.moon;
                break;
            case "rain":
                if (nightTime) {
                    iconId = R.drawable.cloudy_night_rain;
                } else {
                    iconId = R.drawable.cloudy_day_rain;
                }
                break;
            case "snow":
                if (nightTime) {
                    iconId = R.drawable.snow_night;
                } else {
                    iconId = R.drawable.snow_day;
                }
                break;
            case "sleet":
                if (nightTime) {
                    iconId = R.drawable.sleet_night;
                } else {
                    iconId = R.drawable.sleet_day;
                }
                break;
            case "wind":
                if (nightTime) {
                    iconId = R.drawable.wind;
                } else {
                    iconId = R.drawable.windy;
                }
                break;
            case "fog":

                iconId = R.drawable.fog;
                break;
            case "cloudy":
                iconId = R.drawable.cloud;
                break;
            case "partly-cloudy-day":
                iconId = R.drawable.cloudy_day;
                break;
            case "partly-cloudy-night":
                iconId = R.drawable.cloudy_night;
                break;
        }
        return iconId;
    }

}
