package com.example.weatherforecast;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by pushparajparab on 10/19/16.
 */
public class CityWeatherHourAdapter extends RecyclerView.Adapter<CityWeatherHourAdapter.CityWeatherHourAdapterViewHolder> {
    ArrayList<Weather> mWeathers;
    Context mContext;
    int recourseID;
    private LayoutInflater inflater;
    private boolean mTempType;
    private String BASE_IMAGE_URL = "http://api.openweathermap.org/img/w/";


    public CityWeatherHourAdapter(ArrayList<Weather> weathers, Context context,int recourseId, boolean tType)
    {
        this.inflater  = LayoutInflater.from(context);
        this.mContext = context;
        this.recourseID = recourseId;
        this.mWeathers = weathers;
        this.mTempType = tType;

    }

    @Override
    public CityWeatherHourAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(this.recourseID,parent,false);
        return new CityWeatherHourAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityWeatherHourAdapterViewHolder holder, int position) {
        Weather weather = this.mWeathers.get(position);

        DateFormat format = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        String outPut = format.format(weather.getFromDate());

        holder.temperature.setText(
                this.mTempType ?  weather.getTemperatureInF(): weather.getTemperatureInC()
        );
        holder.condition.setText( CityWeather.titleCase( weather.getClouds().value) );
        holder.pressure.setText(weather.getPressure().value + " " + weather.getPressure().name);
        holder.humidity.setText(weather.getHumidity().value + "" + weather.getHumidity().name);
        holder.time.setText(outPut);
        holder.wind.setText(weather.getWindSpeed().value
        + " " + "mps" +", " + weather.getWindDirection().value + "° " +
                weather.getWindDirection().name
        );

        String url= BASE_IMAGE_URL + weather.getIcon().value + ".png";
        Picasso.with(mContext).load(url).placeholder(R.drawable.loading).into(holder.largeIcon);


    }

    @Override
    public int getItemCount() {
        return mWeathers.size();
    }

    class CityWeatherHourAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView time, temperature,condition,pressure,humidity,wind;
        private ImageView largeIcon;
        private View container;

        public CityWeatherHourAdapterViewHolder(View itemView) {
            super(itemView);
            largeIcon = (ImageView)itemView.findViewById(R.id.imageViewLargeIcon);
            time = (TextView)itemView.findViewById(R.id.textViewTime);
            temperature = (TextView)itemView.findViewById(R.id.textViewTemp_detail);
            condition = (TextView)itemView.findViewById(R.id.textViewCondition);
            pressure = (TextView)itemView.findViewById(R.id.textViewPressure);
            humidity = (TextView)itemView.findViewById(R.id.textViewHumidity);
            wind = (TextView)itemView.findViewById(R.id.textViewWind);
            container = itemView.findViewById(R.id.recycleViewDetailWeather);

        }
    }
}