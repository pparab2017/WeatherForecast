package com.example.weatherforecast;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pushparajparab on 10/18/16.
 */
public class DayWeather implements Parcelable{

    public DayWeather()
    {}

    private Date date;

    private Error error;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    private boolean isError;


    @Override
    public String toString() {
        return "DayWeather{" +
                "date=" + date +
                '}';
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public String getTemperatureInF()
    {
        double tempInF = getAverageTemperature();
        tempInF = (tempInF * 1.8) +  32;
        tempInF = DayWeather.round(tempInF,2);
        return tempInF + "° F";
    }


    public String getAvgIcon()
    {
        int iconToGet =  DayObject.size() / 2 ;
        return DayObject.get(iconToGet).getIcon().value;

    }


    public String getAverageTemperatureInC()
    {
        return getAverageTemperature() + "° C";
    }

    public double getAverageTemperature()
    {
        double total = 0, avg =0;
        for(int i =0;i< DayObject.size();i++)
        {
            total = total + Double.parseDouble( DayObject.get(i).getTemperature().value) ;
        }
        avg = total/DayObject.size();
        avg =round(avg, 2);
        return avg ;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private ArrayList<Weather> DayObject;

    public ArrayList<Weather> getDayObject() {
        return DayObject;
    }

    public void setDayObject(ArrayList<Weather> dayObject) {
        DayObject = dayObject;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    protected DayWeather(Parcel in) {
        DayObject = in.createTypedArrayList(Weather.CREATOR);
        date = (Date)in.readValue(Date.class.getClassLoader());
        error = in.readParcelable(Error.class.getClassLoader());
        isError = (boolean)in.readValue(boolean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(DayObject);
        dest.writeValue(date);
        dest.writeValue(error);
        dest.writeValue(isError);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DayWeather> CREATOR = new Creator<DayWeather>() {
        @Override
        public DayWeather createFromParcel(Parcel in) {
            return new DayWeather(in);
        }

        @Override
        public DayWeather[] newArray(int size) {
            return new DayWeather[size];
        }
    };
}
