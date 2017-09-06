package com.example.weatherforecast;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pushparajparab on 10/18/16.
 */
public class Value implements Parcelable {
    String name,value;

    public Value(String name, String value) {
        this.name = name;
        this.value = value;
    }

    protected Value(Parcel in) {
        name = in.readString();
        value = in.readString();
    }

    public static final Creator<Value> CREATOR = new Creator<Value>() {
        @Override
        public Value createFromParcel(Parcel in) {
            return new Value(in);
        }

        @Override
        public Value[] newArray(int size) {
            return new Value[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
    }
}
