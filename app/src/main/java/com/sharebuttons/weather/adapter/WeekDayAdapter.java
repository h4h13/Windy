package com.sharebuttons.weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharebuttons.weather.Forecast.CurrentWeather;
import com.sharebuttons.weather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monkey D Luffy on 7/26/2015.
 */
public class WeekDayAdapter extends RecyclerView.Adapter<WeekDayAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<CurrentWeather> mListData = new ArrayList<>();

    public WeekDayAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.weather_week_day, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CurrentWeather weather = mListData.get(position);
        holder.mDay.setText(weather.getDay());
        holder.mTemp.setText(weather.getTemperature() + "\u00B0");
        holder.mIcon.setImageResource(weather.getIconId());

    }

    public void addItems(int time, int temp, String icon) {
        CurrentWeather weather = new CurrentWeather(time, icon, temp);
        mListData.add(weather);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTemp, mDay;
        private ImageView mIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            mDay = (TextView) itemView.findViewById(R.id.weekDay);
            mTemp = (TextView) itemView.findViewById(R.id.weekTemp);
            mIcon = (ImageView) itemView.findViewById(R.id.weekIcon);
        }
    }

}
