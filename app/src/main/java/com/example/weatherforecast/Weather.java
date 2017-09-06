package com.example.weatherforecast;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pushparajparab on 10/18/16.
 */
public class Weather implements Parcelable {

    private Date fromDate,toDate;
    private Value icon;
    private Value windDirection;
    private Value windSpeed;
    private Value pressure;
    private Value temperature;
    private Value humidity;
    private Value clouds;

    public Weather() {
    }


 //region Getters and Setters
    public Date getFromDate() {
        return fromDate;
    }

    public Date getFromDateOnly() {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String outPut = format.format(fromDate);
            Date date = format.parse(outPut);
           return date;
        }
        catch (Exception e)
        {
            Log.d("debug",e.getMessage());
        }
        return null;
    }

    public void setFromDate(String fromDate) {

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            Date date = format.parse(fromDate);
            this.fromDate = date;
        }
        catch (Exception e)
        {
            Log.d("debug",e.getMessage());
        }
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            Date date = format.parse(toDate);
            this.toDate = date;
        }
        catch (Exception e)
        {
            Log.d("debug",e.getMessage());
        }

    }

    public Value getIcon() {
        return icon;
    }

    public void setIcon(Value icon) {
        this.icon = icon;
    }

    public Value getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(Value windDirection) {
        this.windDirection = windDirection;
    }

    public Value getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Value windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Value getPressure() {
        return pressure;
    }

    public void setPressure(Value pressure) {
        this.pressure = pressure;
    }

    public Value getTemperature() {
        return temperature;
    }
// 30°C x 1.8 + 32

    public String getTemperatureInF()
    {
        double tempInF = Double.parseDouble(getTemperature().value);
        tempInF = (tempInF * 1.8) +  32;
        tempInF = DayWeather.round(tempInF,2);
        return tempInF + "° F";
    }

    public String getTemperatureInC() {
        return getTemperature().value + "° C";
    }

    public void setTemperature(Value temperature) {
        this.temperature = temperature;
    }

    public Value getHumidity() {
        return humidity;
    }

    public void setHumidity(Value humidity) {
        this.humidity = humidity;
    }

    public Value getClouds() {
        return clouds;
    }

    public void setClouds(Value clouds) {
        this.clouds = clouds;
    }

    //endregion

 //region toString
    @Override
    public String toString() {
        return "Weather{" +
                "fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", icon=" + icon +
                ", windDirection=" + windDirection +
                ", windSpeed=" + windSpeed +
                ", pressure=" + pressure +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", clouds=" + clouds +
                '}';
    }
 //endregion

    protected Weather(Parcel in) {
        icon = in.readParcelable(Value.class.getClassLoader());
        windDirection = in.readParcelable(Value.class.getClassLoader());
        windSpeed = in.readParcelable(Value.class.getClassLoader());
        pressure = in.readParcelable(Value.class.getClassLoader());
        temperature = in.readParcelable(Value.class.getClassLoader());
        humidity = in.readParcelable(Value.class.getClassLoader());
        clouds = in.readParcelable(Value.class.getClassLoader());
        fromDate =(Date) in.readValue(Date.class.getClassLoader());
        toDate =(Date) in.readValue(Date.class.getClassLoader());
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(icon, flags);
        dest.writeParcelable(windDirection, flags);
        dest.writeParcelable(windSpeed, flags);
        dest.writeParcelable(pressure, flags);
        dest.writeParcelable(temperature, flags);
        dest.writeParcelable(humidity, flags);
        dest.writeParcelable(clouds, flags);
        dest.writeValue(fromDate);
        dest.writeValue(toDate);
    }
}
