package com.sharebuttons.weather.Forecast;

import com.sharebuttons.weather.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Monkey D Luffy on 7/6/2015.
 */
public class CurrentWeather {
    private String mIcon;
    private long mTime;
    private int color;
    private int mTemperature;
    private String mSummary;

    public CurrentWeather(int time, String icon, int temp) {
        mTime = time;
        mTemperature = temp;
        mIcon = icon;
    }


    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public void setTemperature(int temperature) {
        mTemperature = temperature;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getDay() {
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
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

        switch (mIcon) {
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
