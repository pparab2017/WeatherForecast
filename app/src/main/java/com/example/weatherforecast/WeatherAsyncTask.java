package com.example.weatherforecast;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pushparajparab on 10/18/16.
 */
public class WeatherAsyncTask extends AsyncTask<String, Void, ArrayList<DayWeather>> {

private connectToCityWeather sendDataBack;
    ProgressDialog progressDialog;

    public WeatherAsyncTask(connectToCityWeather connectToCityWeather)
    {
        this.sendDataBack = connectToCityWeather;
    }


    interface connectToCityWeather
    {
       public void returnAllCityWeather(ArrayList<DayWeather> weathers);
        public Context getContext();

    }

    @Override
    protected void onPostExecute(ArrayList<DayWeather> weathers) {

        sendDataBack.returnAllCityWeather(weathers);
        progressDialog.dismiss();
        super.onPostExecute(weathers);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context inNeed = sendDataBack.getContext();
        progressDialog = new ProgressDialog(inNeed);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected ArrayList<DayWeather> doInBackground(String... params) {


        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK)
            {
                InputStream inputStream = connection.getInputStream();
                return WeatherParser.PullParser.GetWeatherFromInputStream(inputStream);
            }
            else
            {
                ArrayList<DayWeather> toReturn = new ArrayList<DayWeather>();
                DayWeather error = new DayWeather();
                error.setError(true);
                Error error1 = new Error();
                error1.setErrorString("No City returned");
                error.setError(error1);
                toReturn.add(error);
                return toReturn;

            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return null;
    }
}
