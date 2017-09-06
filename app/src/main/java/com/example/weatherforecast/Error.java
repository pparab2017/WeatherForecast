package com.example.weatherforecast;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pushparajparab on 10/9/16.
 */
public class Error implements Parcelable {


    public Error(){}
    protected Error(Parcel in) {
        errorObject = in.readString();
        errorString = in.readString();
    }

    public static final Creator<Error> CREATOR = new Creator<Error>() {
        @Override
        public Error createFromParcel(Parcel in) {
            return new Error(in);
        }

        @Override
        public Error[] newArray(int size) {
            return new Error[size];
        }
    };

    public String getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(String errorObject) {
        this.errorObject = errorObject;
    }

    String errorObject;

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    String errorString;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(errorObject);
        dest.writeString(errorString);
    }
}
