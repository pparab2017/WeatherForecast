package com.example.weatherforecast;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by pushparajparab on 10/15/16.
 */
public class DataBaseManager {

    private Context mContext;
    private DataBaseOpenHelper openHelper;
    private SQLiteDatabase db;
    private SavedCityDAO savedCityDAO;

    public DataBaseManager(Context mContext) {
        this.mContext = mContext;
        openHelper = new DataBaseOpenHelper(this.mContext);
        db = openHelper.getWritableDatabase();
        savedCityDAO = new SavedCityDAO(db);
    }

    public void deleteDb()
    {
        mContext.deleteDatabase("mySavedCity.db");
    }

    public void close()
    {
        if(db!=null)
        {
            db.close();
        }
    }

    public SavedCityDAO getSavedCityDAO()
    {
        return this.savedCityDAO;
    }

    public long saveCity(SavedCity note)
    {
        return  this.savedCityDAO.save(note);
    }

    public boolean updateCity(SavedCity note)
    {
        return  this.savedCityDAO.update(note);
    }

    public boolean deleteCity(SavedCity note)
    {
        return  this.savedCityDAO.delete(note);
    }

    public SavedCity getCity(long id)
    {
        return  this.savedCityDAO.getSavedCity(id);
    }

    public List<SavedCity> getAllCities()
    {
        return  this.savedCityDAO.getAll();
    }
}
