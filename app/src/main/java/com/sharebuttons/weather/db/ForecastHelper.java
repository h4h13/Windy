package com.sharebuttons.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Monkey D Luffy on 7/13/2015.
 */
public class ForecastHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "forecast.db";
    private static final int DB_VERSION = 11;

    public static final String TABLE_TEMPERATURES = "daily";
    public static final String TABLE_TODAY = "today";
    public static final String TABLE_HOUR = "hour";

    //hour table
    public static final String COLUMN_HOUR_ID = "_id";
    public static final String COLUMN_HOUR_TIME = "time";
    public static final String COLUMN_HOUR_TEMPERATURE = "temperature";
    public static final String COLUMN_HOUR_ICON = "icon";
    public static final String COLUMN_HOUR_PRECIPINTENSITY = "precipIntensity";
    public static final String COLUMN_HOUR_PRECIPPROBABILITY = "precipProbability";
    public static final String COLUMN_HOUR_APPARENTTEMPERATURE = "apparentTemperature";
    public static final String COLUMN_HOUR_HUMIDITY = "humidity";
    public static final String COLUMN_HOUR_SUMMARY = "summary";
    public static final String COLUMN_HOUR_WIND_SPEED = "wind_speed";
    public static final String COLUMN_HOUR_WIND_BEARING = "wind_bearing";
    public static final String COLUMN_HOUR_PRESSURE = "pressure";
    public static final String COLUMN_HOUR_DEW_POINT = "dew_point";
    public static final String COLUMN_HOUR_OZONE = "ozone";
    public static final String COLUMN_HOUR_CLOUD_COVER = "cloud_cover";
    public static final String COLUMN_HOUR_VISIBILITY = "visibility";

    private static final String DB_HOUR =
            "CREATE TABLE " + TABLE_HOUR + " ( " +
                    COLUMN_HOUR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HOUR_TIME + " LONG, " +
                    COLUMN_HOUR_TEMPERATURE + " REAL, " +
                    COLUMN_HOUR_SUMMARY + " VARCHAR(255), " +
                    COLUMN_HOUR_ICON + " VARCHAR(255), " +
                    COLUMN_HOUR_HUMIDITY + " DOUBLE, " +
                    COLUMN_HOUR_PRECIPINTENSITY + " DOUBLE, " +
                    COLUMN_HOUR_PRECIPPROBABILITY + " DOUBLE, " +
                    COLUMN_HOUR_APPARENTTEMPERATURE + " DOUBLE, " +
                    COLUMN_HOUR_WIND_SPEED + " DOUBLE, " +
                    COLUMN_HOUR_WIND_BEARING + " INT, " +
                    COLUMN_HOUR_PRESSURE + " DOUBLE, " +
                    COLUMN_HOUR_DEW_POINT + " DOUBLE, " +
                    COLUMN_HOUR_OZONE + " DOUBLE, " +
                    COLUMN_HOUR_CLOUD_COVER + " DOUBLE, " +
                    COLUMN_HOUR_VISIBILITY + " DOUBLE );";

    //dialy table
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_SUNRISE_TIME = "sunriseTime";
    public static final String COLUMN_SUNSET_TIME = "sunsetTime";
    public static final String COLUMN_MOON_PHASE = "moonPhase";
    public static final String COLUMN_PRECIPINTENSITY = "precipIntensity";
    public static final String COLUMN_PRECIPINTENSITYMAX = "precipIntensityMax";
    public static final String COLUMN_PRECIPINTENSITYMAXTIME = "precipIntensityMaxTime";
    public static final String COLUMN_PRECIPPROBABILITY = "precipProbability";
    public static final String COLUMN_PRECIPTYPE = "precipType";
    public static final String COLUMN_TEMPERATUREMIN = "temperatureMin";
    public static final String COLUMN_TEMPERATUREMINTIME = "temperatureMinTime";
    public static final String COLUMN_TEMPERATUREMAX = "temperatureMax";
    public static final String COLUMN_TEMPERATUREMAXTIME = "temperatureMaxTime";
    public static final String COLUMN_APPARENTTEMPERATUREMIN = "apparentTemperatureMin";
    public static final String COLUMN_APPARENTTEMPERATUREMINTIME = "apparentTemperatureMinTime";
    public static final String COLUMN_APPARENTTEMPERATUREMAX = "apparentTemperatureMax";
    public static final String COLUMN_APPARENTTEMPERATUREMAXTIME = "apparentTemperatureMaxTime";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_PRECIPVALUE = "precipvalue";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_WIND_SPEED = "wind_speed";
    public static final String COLUMN_WIND_BEARING = "wind_bearing";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_DEW_POINT = "dew_point";
    public static final String COLUMN_OZONE = "ozone";
    public static final String COLUMN_CLOUD_COVER = "cloud_cover";
    public static final String COLUMN_VISIBILITY = "visibility";

    private static final String DB_CREATE =
            "CREATE TABLE " +
                    TABLE_TEMPERATURES + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ICON + " TEXT, " +
                    COLUMN_SUMMARY + " TEXT, " +
                    COLUMN_TIME + " INTEGER, " +
                    COLUMN_SUNRISE_TIME + " INTEGER, " +
                    COLUMN_SUNSET_TIME + " INTEGER, " +
                    COLUMN_MOON_PHASE + " INTEGER, " +
                    COLUMN_PRECIPINTENSITY + " REAL, " +
                    COLUMN_PRECIPINTENSITYMAX + " REAL, " +
                    COLUMN_PRECIPINTENSITYMAXTIME + " INTEGER, " +
                    COLUMN_PRECIPPROBABILITY + " INTEGER, " +
                    COLUMN_PRECIPTYPE + " TEXT, " +
                    COLUMN_TEMPERATUREMIN + " INTEGER, " +
                    COLUMN_TEMPERATUREMINTIME + " INTEGER, " +
                    COLUMN_TEMPERATUREMAX + " INTEGER, " +
                    COLUMN_TEMPERATUREMAXTIME + " INTEGER, " +
                    COLUMN_APPARENTTEMPERATUREMIN + " INTEGER, " +
                    COLUMN_APPARENTTEMPERATUREMINTIME + " INTEGER, " +
                    COLUMN_APPARENTTEMPERATUREMAX + " INTEGER, " +
                    COLUMN_APPARENTTEMPERATUREMAXTIME + " INTEGER, " +
                    COLUMN_HUMIDITY + " REAL, " +
                    COLUMN_WIND_SPEED + " REAL, " +
                    COLUMN_WIND_BEARING + " REAL, " +
                    COLUMN_PRESSURE + " REAL, " +
                    COLUMN_DEW_POINT + " REAL, " +
                    COLUMN_OZONE + " REAL, " +
                    COLUMN_CLOUD_COVER + " REAL, " +
                    COLUMN_VISIBILITY + " REAL );";
    //For Tody Table
    public static final String COLUMN_TODAY_ID = "_id";
    public static final String COLUMN_TODAY_LATITUDE = "latitude";
    public static final String COLUMN_TODAY_LONGITUDE = "longitude";
    public static final String COLUMN_TODAY_TIME = "time";
    public static final String COLUMN_TODAY_TEMPERATURE = "temperature";
    public static final String COLUMN_TODAY_ICON = "icon";
    public static final String COLUMN_TODAY_PRECIPVALUE = "precipvalue";
    public static final String COLUMN_TODAY_HUMIDITY = "humidity";
    public static final String COLUMN_TODAY_SUMMARY = "summary";
    public static final String COLUMN_TODAY_WIND_SPEED = "wind_speed";
    public static final String COLUMN_TODAY_WIND_BEARING = "wind_bearing";
    public static final String COLUMN_TODAY_PRESSURE = "pressure";
    public static final String COLUMN_TODAY_DEW_POINT = "dew_point";
    public static final String COLUMN_TODAY_OZONE = "ozone";
    public static final String COLUMN_TODAY_CLOUD_COVER = "cloud_cover";
    public static final String COLUMN_TODAY_VISIBILITY = "visibility";

    private static final String DB_TODAY = "" +
            "CREATE TABLE " + TABLE_TODAY + " ( " +
            COLUMN_TODAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TODAY_TIME + " LONG, " +
            COLUMN_TODAY_LATITUDE + " DOUBLE, " +
            COLUMN_TODAY_LONGITUDE + " DOUBLE, " +
            COLUMN_TODAY_TEMPERATURE + " REAL, " +
            COLUMN_TODAY_SUMMARY + " VARCHAR(255), " +
            COLUMN_TODAY_ICON + " VARCHAR(255), " +
            COLUMN_TODAY_HUMIDITY + " DOUBLE, " +
            COLUMN_TODAY_PRECIPVALUE + " DOUBLE, " +
            COLUMN_TODAY_WIND_SPEED + " DOUBLE, " +
            COLUMN_TODAY_WIND_BEARING + " INT, " +
            COLUMN_TODAY_PRESSURE + " DOUBLE, " +
            COLUMN_TODAY_DEW_POINT + " DOUBLE, " +
            COLUMN_TODAY_OZONE + " DOUBLE, " +
            COLUMN_TODAY_CLOUD_COVER + " DOUBLE, " +
            COLUMN_TODAY_VISIBILITY + " DOUBLE );";


    public ForecastHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
        db.execSQL(DB_TODAY);
        db.execSQL(DB_HOUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPERATURES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODAY);
        onCreate(db);

    }
}
