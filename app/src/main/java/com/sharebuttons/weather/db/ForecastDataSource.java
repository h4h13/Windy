package com.sharebuttons.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Monkey D Luffy on 7/13/2015.
 */
public class ForecastDataSource {
    private SQLiteDatabase mDatabase;
    private ForecastHelper mForecastHelper;
    private Context mContext;

    public ForecastDataSource(Context context) {
        mContext = context;
        mForecastHelper = new ForecastHelper(mContext);
    }

    //open
    public void open() {
        mDatabase = mForecastHelper.getWritableDatabase();
    }

    //close
    public void close() {
        mDatabase.close();
    }

    //insert
    public long insert(ContentValues values) {
        mDatabase.beginTransaction();
        long id;
        try {
            id = mDatabase.insert(ForecastHelper.TABLE_TEMPERATURES, null, values);
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
        return id;
    }

    public long insertHourly(ContentValues values) {
        mDatabase.beginTransaction();
        long id;
        try {
            id = mDatabase.insert(ForecastHelper.TABLE_HOUR, null, values);
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
        return id;
    }

    public long insertTodayTable(ContentValues values) {
        mDatabase.beginTransaction();
        long id;
        try {
            id = mDatabase.insert(ForecastHelper.TABLE_TODAY, null, values);
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
        return id;
    }

    //delete
    public void deleteAll() {
        mDatabase.delete(ForecastHelper.TABLE_TEMPERATURES, null, null);
    }

    public void deleteToday() {
        mDatabase.delete(ForecastHelper.TABLE_TODAY, null, null);
    }

    public void deleteHour() {
        mDatabase.delete(ForecastHelper.TABLE_HOUR, null, null);
    }

    //select
    public Cursor readToday() {
        String[] columns = {
                ForecastHelper.COLUMN_TODAY_TIME,
                ForecastHelper.COLUMN_TODAY_ICON,
                ForecastHelper.COLUMN_TODAY_SUMMARY,
                ForecastHelper.COLUMN_TODAY_TEMPERATURE,
                ForecastHelper.COLUMN_TODAY_PRECIPVALUE,
                ForecastHelper.COLUMN_TODAY_PRESSURE,
                ForecastHelper.COLUMN_TODAY_DEW_POINT,
                ForecastHelper.COLUMN_TODAY_HUMIDITY,
                ForecastHelper.COLUMN_TODAY_VISIBILITY,
                ForecastHelper.COLUMN_TODAY_WIND_SPEED,
                ForecastHelper.COLUMN_TODAY_WIND_BEARING,
                ForecastHelper.COLUMN_TODAY_CLOUD_COVER,
                ForecastHelper.COLUMN_TODAY_OZONE};
        return mDatabase.query(ForecastHelper.TABLE_TODAY, columns, null, null, null, null, null);
    }

    public Cursor readDay() {
        String[] columns = {
                ForecastHelper.COLUMN_TIME,
                ForecastHelper.COLUMN_TEMPERATUREMAX,
                ForecastHelper.COLUMN_TEMPERATUREMIN,
                ForecastHelper.COLUMN_ICON};
        return mDatabase.query(ForecastHelper.TABLE_TEMPERATURES, columns, null, null, null, null, null);
    }

    public Cursor readHour() {
        String[] columns = {
                ForecastHelper.COLUMN_HOUR_TIME,
                ForecastHelper.COLUMN_HOUR_ICON,
                ForecastHelper.COLUMN_HOUR_TEMPERATURE};
        return mDatabase.query(ForecastHelper.TABLE_HOUR, columns, null, null, null, null, null);
    }
}
