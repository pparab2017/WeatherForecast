package com.example.weatherforecast;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pushparajparab on 10/18/16.
 */
public class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherAdapter.CityWeatherHolder>{

    private ArrayList<DayWeather> dayWeathers;
    private LayoutInflater inflater;
    private int recourseID;
    private Context mContext;
    private boolean mTempType = false;
    private String BASE_IMAGE_URL = "http://api.openweathermap.org/img/w/";



    private ItemClickCallBack itemClickCallBack;

    public interface ItemClickCallBack
    {
        void OnItemClick(int p);
        //if next method.
    }


    public CityWeatherAdapter(ArrayList<DayWeather> weathersData, Context context, int recourseId, boolean tType)
    {
        this.inflater = LayoutInflater.from(context);
        this.dayWeathers = weathersData;
        this.recourseID = recourseId;
        this.mContext = context;
        this.mTempType = tType;
        this.itemClickCallBack = (ItemClickCallBack) context;

    }

    @Override
    public CityWeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(this.recourseID,parent,false);
        return new CityWeatherHolder(view);
    }

    @Override
    public void onBindViewHolder(CityWeatherHolder holder, int position) {
        DayWeather dayWeather = dayWeathers.get(position);
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        String outPut = format.format(dayWeather.getDate());


        holder.date.setText(outPut);
        holder.temperature.setText(
                this.mTempType ? dayWeather.getTemperatureInF()  : dayWeather.getAverageTemperatureInC()
        );

        String url= BASE_IMAGE_URL + dayWeather.getAvgIcon() + ".png";
        Picasso.with(mContext).load(url).placeholder(R.drawable.loading).into(holder.icon);


    }

    @Override
    public int getItemCount() {
        return dayWeathers.size();
    }





    class CityWeatherHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView date,temperature;
        private ImageView icon;
        private View container;



        public CityWeatherHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.textViewDate);
            temperature = (TextView) itemView.findViewById(R.id.textViewTemperature);
            icon = (ImageView) itemView.findViewById(R.id.imageViewIcon);
            container = itemView.findViewById(R.id.cityWeatherRoot);


            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.cityWeatherRoot:

                    itemClickCallBack.OnItemClick(getAdapterPosition());
                    break;

            }
        }
    }

}
