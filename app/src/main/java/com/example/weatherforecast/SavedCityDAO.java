package com.example.weatherforecast;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pushparajparab on 10/15/16.
 */
public class SavedCityDAO {
    private SQLiteDatabase db;
    public SavedCityDAO(SQLiteDatabase db)
    {
        this.db = db;
    }


    public long save(SavedCity savedCity)
    {
        ContentValues values = new ContentValues();
        values.put(SavedCityTable.COLUMN_CITY_NAME,savedCity.getCityName());
        values.put(SavedCityTable.COLUMN_COUNTRY_NAME,savedCity.getCountryName());
        values.put(SavedCityTable.COLUMN_TEMPERATURE,savedCity.getTemperature());
        values.put(SavedCityTable.COLUMN_DATE,savedCity.getDate());
        values.put(SavedCityTable.COLUMN_FAVORITE,savedCity.isFavorite());

        return db.insert(SavedCityTable.TABLE_NAME,null,values);

    }

    public boolean update(SavedCity savedCity)
    {
        ContentValues values = new ContentValues();
        values.put(SavedCityTable.COLUMN_CITY_NAME,savedCity.getCityName());
        values.put(SavedCityTable.COLUMN_COUNTRY_NAME,savedCity.getCountryName());
        values.put(SavedCityTable.COLUMN_TEMPERATURE,savedCity.getTemperature());
        values.put(SavedCityTable.COLUMN_DATE,savedCity.getDate());
        values.put(SavedCityTable.COLUMN_FAVORITE,savedCity.isFavorite());

        return db.update(SavedCityTable.TABLE_NAME,values,SavedCityTable.COLUMN_ID + "=?", new String[]{savedCity.get_id()+""}) > 0;

    }

    public boolean delete(SavedCity savedCity)
    {

        return db.delete(SavedCityTable.TABLE_NAME,SavedCityTable.COLUMN_ID + "=?", new String[]{savedCity.get_id()+""}) > 0;

    }


    public SavedCity getSavedCity(long id)
    {
        SavedCity savedCity = null;

        Cursor c = db.query(true,SavedCityTable.TABLE_NAME,new String[]{
                SavedCityTable.COLUMN_ID
                ,SavedCityTable.COLUMN_CITY_NAME
                ,SavedCityTable.COLUMN_COUNTRY_NAME
                ,SavedCityTable.COLUMN_TEMPERATURE
                ,SavedCityTable.COLUMN_DATE
                ,SavedCityTable.COLUMN_FAVORITE},
                SavedCityTable.COLUMN_ID + "=?",new String[]{id +""},null,null,null,null);

if(c!= null && c.moveToFirst())
{
    savedCity = BuildNoteFromCursor(c);
    if(!c.isClosed()) {
        c.close();
    }// good practice
}

        return savedCity;
    }

    private SavedCity BuildNoteFromCursor(Cursor c)
    {
        SavedCity savedCity = null;
        if(c != null)
        {
            savedCity = new SavedCity();
            savedCity.set_id(c.getLong(0));
            savedCity.setCityName(c.getString(1));
            savedCity.setCountryName(c.getString(2));
            savedCity.setTemperature(c.getString(3));
            savedCity.setDate(c.getString(4));
            savedCity.setFavorite(c.getInt(5) != 0);

        }
        return savedCity;
    }


    public List<SavedCity> getAll()
    {
        List<SavedCity> savedCities = new ArrayList<SavedCity>();
        Cursor c =  db.query(SavedCityTable.TABLE_NAME,
                new String[]{SavedCityTable.COLUMN_ID
                        ,SavedCityTable.COLUMN_CITY_NAME
                        ,SavedCityTable.COLUMN_COUNTRY_NAME
                        ,SavedCityTable.COLUMN_TEMPERATURE
                        ,SavedCityTable.COLUMN_DATE
                        ,SavedCityTable.COLUMN_FAVORITE}
        ,null,null,null,null,null);

        if(c!= null && c.moveToFirst()) {

            do{
                SavedCity savedCity = BuildNoteFromCursor(c);
                if(savedCity!= null)
                {
                    savedCities.add(savedCity);
                }

            }while (c.moveToNext());
            if(!c.isClosed()) {
                c.close();
            }//
        }


        return savedCities;
    }

}

