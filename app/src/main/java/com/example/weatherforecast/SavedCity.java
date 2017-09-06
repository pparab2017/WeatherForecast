package com.example.weatherforecast;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pushparajparab on 10/19/16.
 */
public class SavedCity implements Parcelable {
    public SavedCity(String cityName, String countryName, String temperature, String date, boolean favorite) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.temperature = temperature;
        this.date = date;
        this.favorite = favorite;
    }

    public SavedCity(){}
    private long _id;
    private String cityName,countryName, temperature;
    private String date;

    public String getDate() {
        return date;
    }


    public String getDateInRequiredFormat() {

        try {
            DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            Date date = format.parse(getDate());
            DateFormat reqFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            String outPut = reqFormat.format(date);
            return outPut;
        }
        catch (Exception e)
        {
            Log.d("debug",e.getMessage());
        }
        return date;
    }

    public void setDate(String dateString) {
        this.date = dateString;
    }





    boolean favorite;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    //region getters and setters
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getTemperature() {
        return temperature;
    }

    private double getTemperatureInDouble()
    {
        return Double.parseDouble( getTemperature());
    }


    public String getTemperatureInC()
    {
        return getTemperature() + "° C";

    }

    public String getTemperatureInF()
    {

        double tempInF;
        tempInF = (getTemperatureInDouble() * 1.8) +  32;
        tempInF = DayWeather.round(tempInF,2);
        return tempInF + "° F";
    }



    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    //endregion

    protected SavedCity(Parcel in) {
        cityName = in.readString();
        countryName = in.readString();
        temperature = in.readString();
        favorite = in.readByte() != 0;
        date = in.readString();
    }

    public static final Creator<SavedCity> CREATOR = new Creator<SavedCity>() {
        @Override
        public SavedCity createFromParcel(Parcel in) {
            return new SavedCity(in);
        }

        @Override
        public SavedCity[] newArray(int size) {
            return new SavedCity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeString(countryName);
        dest.writeString(temperature);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeString(date);
    }
}
