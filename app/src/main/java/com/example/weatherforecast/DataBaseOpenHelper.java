package com.example.weatherforecast;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pushparajparab on 10/15/16.
 */
public class DataBaseOpenHelper extends SQLiteOpenHelper {
static  final String DB_NAME = "mySavedCity.db";
    static final int DB_VERSION = 1;

    public DataBaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SavedCityTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SavedCityTable.onUpgrade(db,oldVersion,newVersion);
    }
}
