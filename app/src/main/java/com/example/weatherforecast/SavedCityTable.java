package com.example.weatherforecast;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by pushparajparab on 10/15/16.
 */
public class SavedCityTable {
    static  final String TABLE_NAME = "savedCity";
    static  final String COLUMN_ID = "_id";
    static  final String COLUMN_CITY_NAME = "cityName";
    static  final String COLUMN_COUNTRY_NAME = "countryName";
    static  final String COLUMN_TEMPERATURE = "temperature";
    static  final String COLUMN_FAVORITE = "favorite";
    static  final String COLUMN_DATE = "date";

    static public void onCreate(SQLiteDatabase db)
    {
        StringBuilder  sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_NAME + " ( "  );
        sb.append(COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,  "  );
        sb.append(COLUMN_CITY_NAME + " TEXT NOT NULL, ");
        sb.append(COLUMN_COUNTRY_NAME + " TEXT NOT NULL, ");
        sb.append(COLUMN_TEMPERATURE + " TEXT NOT NULL, ");
        sb.append(COLUMN_DATE + " TEXT NOT NULL, ");
        sb.append(COLUMN_FAVORITE + " NUMERIC NOT NULL );"  );

       try {
           db.execSQL(sb.toString());
       }
       catch (SQLException ex)
       {
           ex.printStackTrace();
       }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        SavedCityTable.onCreate(db);
    }
}
