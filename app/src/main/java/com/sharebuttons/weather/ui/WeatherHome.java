package com.sharebuttons.weather.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.sharebuttons.weather.R;
import com.sharebuttons.weather.adapter.WeekDayAdapter;
import com.sharebuttons.weather.anim.AnimationHolder;
import com.sharebuttons.weather.db.ForecastDataSource;
import com.sharebuttons.weather.db.ForecastHelper;
import com.sharebuttons.weather.service.MyLocation;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Monkey D Luffy on 7/26/2015.
 */
public class WeatherHome extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    @Bind(R.id.weekDayList)
    RecyclerView mRecyclerView;
    @Bind(R.id.temperatureValue)
    TextView mTemperature;
    @Bind(R.id.weatherImageView)
    ImageView mIcon;
    @Bind(R.id.locationLabel)
    TextView mLocationLabel;
    @Bind(R.id.weekDayLabel)
    TextView mWeekLabel;
    @Bind(R.id.windSpeed)
    TextView mWindSpeedValue;
    @Bind(R.id.humidityValue)
    TextView mHumidityValue;
    @Bind(R.id.precipValue)
    TextView mPrecipValue;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.summaryLabel)
    TextView mSummaryLabel;

    SharedPreferences mSharedPreferences;

    private double latitude = 0.0;
    private double longitude = 0.0;
    private Geocoder geocoder;
    // private GPSTracker mGPSTracker;
    private ForecastDataSource mDataSource;
    private WeekDayAdapter mAdapter;


    private static final String TAG = WeatherHome.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_home);
        ButterKnife.bind(this);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        classInit();
        getLocation();
        recyclerViewInit();


        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }


        displayLocation();
    }

    MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            Log.v("Location", location + "");
            if ((latitude = location.getLatitude()) == 0.0) {
                alertUserAboutError();
            } else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                getForecast(latitude, longitude);

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("latitude", String.valueOf(latitude));
                editor.putString("longitude", String.valueOf(longitude));
                editor.apply();

                Log.v("Location", location + " " + latitude + " " + longitude);
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    private void recyclerViewInit() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void getLocation() {
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getApplicationContext(), locationResult);


       /* try {
            if (mGPSTracker.canGetLocation()) {
                latitude = mGPSTracker.getLatitude();
                longitude = mGPSTracker.getLongitude();
                Log.v("Location", latitude + " " + longitude);
                getForecast(latitude, longitude);
            } else {

                alertUserAboutError();
                Toast.makeText(getApplicationContext(), "net", Toast.LENGTH_LONG).show();
            }
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Check the Network or Loaction is tuned on", Toast.LENGTH_LONG).show();
        }*/
    }

    private void getForecast(double latitude, double longitude) {
        String forecastURL = "https://api.forecast.io/forecast/cb5d1c74594d94d127b8548df410cc9f/" + latitude + "," + longitude;
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(forecastURL).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertUserAboutError();
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonData = response.body().string();
                        try {
                            Log.v("JSON DATA", jsonData);
                            getWeatherData(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alertUserAboutError();
                }
            });
        }
    }

    private void getDailyData(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray dataArray = daily.getJSONArray("data");
        mDataSource.deleteAll();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject data = dataArray.getJSONObject(i);
            ContentValues values = new ContentValues();
            values.put(ForecastHelper.COLUMN_TIME, data.getInt("time"));
            values.put(ForecastHelper.COLUMN_SUMMARY, data.optString("summary"));
            values.put(ForecastHelper.COLUMN_ICON, data.optString("icon"));
            values.put(ForecastHelper.COLUMN_SUNRISE_TIME, data.optInt("sunriseTime"));
            values.put(ForecastHelper.COLUMN_SUNSET_TIME, data.optInt("sunsetTime"));
            values.put(ForecastHelper.COLUMN_MOON_PHASE, data.optInt("moonPhase"));
            values.put(ForecastHelper.COLUMN_PRECIPINTENSITY, data.optDouble("precipIntensity"));
            values.put(ForecastHelper.COLUMN_PRECIPINTENSITYMAXTIME, data.optDouble("precipIntensityMaxTime"));
            values.put(ForecastHelper.COLUMN_PRECIPINTENSITYMAX, data.optDouble("precipIntensityMax"));
            values.put(ForecastHelper.COLUMN_PRECIPPROBABILITY, data.optDouble("precipProbability"));
            values.put(ForecastHelper.COLUMN_PRECIPTYPE, data.optString("precipType"));
            values.put(ForecastHelper.COLUMN_TEMPERATUREMIN, data.optDouble("temperatureMin"));
            values.put(ForecastHelper.COLUMN_TEMPERATUREMINTIME, data.optInt("temperatureMinTime"));
            values.put(ForecastHelper.COLUMN_TEMPERATUREMAX, data.optDouble("temperatureMax"));
            values.put(ForecastHelper.COLUMN_TEMPERATUREMAXTIME, data.optInt("temperatureMaxTime"));
            values.put(ForecastHelper.COLUMN_APPARENTTEMPERATUREMIN, data.optDouble("apparentTemperatureMin"));
            values.put(ForecastHelper.COLUMN_APPARENTTEMPERATUREMINTIME, data.optInt("apparentTemperatureMinTime"));
            values.put(ForecastHelper.COLUMN_APPARENTTEMPERATUREMAX, data.optDouble("apparentTemperatureMax"));
            values.put(ForecastHelper.COLUMN_APPARENTTEMPERATUREMAXTIME, data.optInt("apparentTemperatureMaxTime"));
            values.put(ForecastHelper.COLUMN_DEW_POINT, data.optDouble("dewPoint"));
            values.put(ForecastHelper.COLUMN_HUMIDITY, data.optDouble("humidity"));
            values.put(ForecastHelper.COLUMN_WIND_SPEED, data.optDouble("windSpeed"));
            values.put(ForecastHelper.COLUMN_WIND_BEARING, data.optDouble("windBearing"));
            values.put(ForecastHelper.COLUMN_VISIBILITY, data.optDouble("visibility"));
            values.put(ForecastHelper.COLUMN_CLOUD_COVER, data.optDouble("cloudCover"));
            values.put(ForecastHelper.COLUMN_PRESSURE, data.optDouble("pressure"));
            values.put(ForecastHelper.COLUMN_OZONE, data.optDouble("ozone"));
            mDataSource.insert(values);
        }

    }

    private void getWeatherData(String jsonData) throws JSONException {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("isLaunched", true);
        editor.apply();
        getForecastData(jsonData);
        getDailyData(jsonData);
    }

    Cursor cursor = null;

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            //mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {

        cursor = mDataSource.readToday();
        updateViews(cursor);

        Cursor week = mDataSource.readDay();
        updateWeek(week);
    }


    private void updateWeek(Cursor cursor) {
        toggleRefresh();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int temp = cursor.getInt(cursor.getColumnIndex(ForecastHelper.COLUMN_TEMPERATUREMAX));
            int time = cursor.getInt(cursor.getColumnIndex(ForecastHelper.COLUMN_TIME));
            String icon = cursor.getString(cursor.getColumnIndex(ForecastHelper.COLUMN_ICON));

            mAdapter.addItems(time, temp, icon);

            cursor.moveToNext();
        }
    }


    private void updateViews(Cursor cursor) {
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {

            int time = cursor.getInt(cursor.getColumnIndex(ForecastHelper.COLUMN_TODAY_TIME));
            int icon = getIconId(cursor.getString(cursor.getColumnIndex(ForecastHelper.COLUMN_ICON)));
            double humidity = cursor.getDouble(cursor.getColumnIndex(ForecastHelper.COLUMN_TODAY_HUMIDITY));
            double precip = cursor.getDouble(cursor.getColumnIndex(ForecastHelper.COLUMN_TODAY_PRECIPVALUE));
            String summary = cursor.getString(cursor.getColumnIndex(ForecastHelper.COLUMN_SUMMARY));
            try {
                List<Address> addresses = geocoder
                        .getFromLocation(Double.parseDouble(mSharedPreferences.getString("latitude", "0.0")), //latitude
                                Double.parseDouble(mSharedPreferences.getString("longitude", "0.0")), 1);     //longitude
                if (addresses.get(0).getLocality() != null) {
                    mLocationLabel.setText(addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mWeekLabel.setText(getToday(time));
            mIcon.setImageResource(icon);
            mSummaryLabel.setText(summary);
            mHumidityValue.setText((int) (humidity * 100) + " %");
            mPrecipValue.setText((int) (precip * 100) + " %");
            setKmToM(cursor);
            cursor.moveToNext();
        }
        AnimationHolder.animateFab(mIcon);
        AnimationHolder.animateTextView(mTemperature);
    }

    private void setKmToM(Cursor cursor) {
        toggleRefresh();
        cursor.moveToFirst();
        int temp = cursor.getColumnIndex(ForecastHelper.COLUMN_TODAY_TEMPERATURE);
        double wind = cursor.getDouble(cursor.getColumnIndex(ForecastHelper.COLUMN_TODAY_WIND_SPEED));

        if (mSharedPreferences.getBoolean("mile_km", false)) {
            mWindSpeedValue.setText(convertMileToKm(wind) + " kph");
        } else {
            mWindSpeedValue.setText(wind + " mph");
        }
        if (mSharedPreferences.getBoolean("key_celsius", false)) {
            mTemperature.setText(convertFahrenheitToCelsius(cursor.getString(temp)) + "\u00B0");
        } else {
            mTemperature.setText(cursor.getString(temp) + "\u00B0");
        }
    }

    //Convert Mile to Km
    private int convertMileToKm(double windSpeed) {
        return (int) Math.round(windSpeed * 1.60934);
    }

    // Converts to celsius
    private int convertFahrenheitToCelsius(String fahrenheit) {
        return Math.round((Integer.valueOf(fahrenheit) - 32) * 5 / 9);
    }

    public String getToday(int time) {
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(new Date(time * 1000));
    }

    private void getForecastData(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        JSONObject currently = forecast.getJSONObject("currently");

        mDataSource.deleteToday();
        //add values to database
        ContentValues values = new ContentValues();
        values.put(ForecastHelper.COLUMN_TODAY_LATITUDE, latitude);
        values.put(ForecastHelper.COLUMN_TODAY_LONGITUDE, longitude);
        values.put(ForecastHelper.COLUMN_TODAY_TIME, currently.optString("time"));
        values.put(ForecastHelper.COLUMN_TODAY_ICON, currently.optString("icon"));
        values.put(ForecastHelper.COLUMN_TODAY_SUMMARY, currently.optString("summary"));
        values.put(ForecastHelper.COLUMN_TODAY_TEMPERATURE, currently.optInt("temperature"));
        values.put(ForecastHelper.COLUMN_TODAY_WIND_SPEED, currently.optDouble("windSpeed"));
        values.put(ForecastHelper.COLUMN_TODAY_WIND_BEARING, currently.optLong("windBearing"));
        values.put(ForecastHelper.COLUMN_TODAY_PRECIPVALUE, currently.optDouble("precipProbability"));
        values.put(ForecastHelper.COLUMN_TODAY_HUMIDITY, currently.optDouble("humidity"));
        values.put(ForecastHelper.COLUMN_TODAY_PRESSURE, currently.optDouble("pressure"));
        values.put(ForecastHelper.COLUMN_TODAY_DEW_POINT, currently.optDouble("dewPoint"));
        values.put(ForecastHelper.COLUMN_TODAY_OZONE, currently.optDouble("ozone"));
        values.put(ForecastHelper.COLUMN_TODAY_VISIBILITY, currently.optDouble("visibility"));
        values.put(ForecastHelper.COLUMN_TODAY_CLOUD_COVER, currently.optDouble("cloudCover"));

        mDataSource.insertTodayTable(values);
    }


    private void classInit() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        //mGPSTracker = new GPSTracker(getApplicationContext());
        mDataSource = new ForecastDataSource(getApplicationContext());
        geocoder = new Geocoder(getApplicationContext());
        mAdapter = new WeekDayAdapter(getApplicationContext());
    }


    @Override
    protected void onResume() {
        mDataSource.open();
        Log.v("GPS", "onResume");
        if (mSharedPreferences.getBoolean("isLaunched", false)) {
            updateDisplay();
        }


        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        mDataSource.close();

        stopLocationUpdates();

        super.onPause();
    }


    private void alertUserAboutError() {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TITLE", "Oops Sorry!");
        bundle.putString("MESSAGE", "Check Location services is enabled or Make sure you have working network connection");
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "error_dialog");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    public String getFormatedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("H");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(new Date());
    }

    private int getIconId(String icon) {

        int iconId = R.drawable.sunny;
        int timeNow = Integer.parseInt(getFormatedTime());
        boolean nightTime = timeNow < 6 || timeNow > 18;

        switch (icon) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(WeatherHome.this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("key_celsius") || key.equals("mile_km")) {
            setKmToM(cursor);
        }
    }

    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            Log.v("LocationFound", latitude + ", " + longitude);

        } else {

            Log.v("LocationFound", "(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    /**
     * Method to toggle periodic location updates
     */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }
}
