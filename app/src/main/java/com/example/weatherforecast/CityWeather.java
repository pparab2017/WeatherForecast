package com.example.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;



public class CityWeather extends AppCompatActivity
        implements WeatherAsyncTask.connectToCityWeather
        ,CityWeatherAdapter.ItemClickCallBack{

    private RecyclerView recyclerView;
    private CityWeatherAdapter cityWeatherAdapter;
    private CityWeatherHourAdapter cityWeatherHourAdapter;
    private ArrayList<DayWeather> CityDayWeatherList;
    private TextView topTag,detailTag;
    private boolean tType= false; // This is CELSIUS, true will be FAHRENHEIT
    String[] cityCountryName;
    final static int CITY_ACTIVITY = 00111;
    final static String API_KEY = "d989d38f988591245c8b27817f6a32be";
    final String KEY ="{KEY}",COUNTRY ="{COUNTRY}", CITY ="{CITY}";
    final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?q={CITY},{COUNTRY}&mode=xml&appid={KEY}";
    long defaultTimer = 5000;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myMenu = getMenuInflater();
        myMenu.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case CITY_ACTIVITY:
                    BindLists();
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String city = (cityCountryName[0].contains("_") ?
                (cityCountryName[0].replace("_"," ")) :
                (cityCountryName[0])) ;

        String Country =  cityCountryName[1];

        switch (item.getItemId()) {

            case R.id.itemSaveCity:

                DataBaseManager manager;
                manager = new DataBaseManager(this);
                List<SavedCity> allSavedCity = new ArrayList<SavedCity>();
                allSavedCity = manager.getAllCities();
                boolean update = false;
                SavedCity toUpdate = new SavedCity();

                if(allSavedCity.size()==0)
                {
                    manager.saveCity(new SavedCity(titleCase(city),
                            Country.toUpperCase(),
                            CityDayWeatherList.get(0).getAverageTemperature()+"",
                            new Date().toString(),
                            false));
                    Toast.makeText(CityWeather.this, getResources().getText(R.string.city_saved),Toast.LENGTH_LONG).show();
                }
                else {
                    for (SavedCity currentCity:allSavedCity) {
                        if(currentCity.getCityName().trim().toLowerCase().equals(city.trim().toLowerCase())
                                && currentCity.getCountryName().trim().toLowerCase().equals(Country.trim().toLowerCase())) {
                            update = true;
                            toUpdate = new SavedCity(titleCase(city),
                                    Country.toUpperCase(),
                                    CityDayWeatherList.get(0).getAverageTemperature() + "",
                                    new Date().toString(),
                                    false);
                            toUpdate.set_id(currentCity.get_id());
                            toUpdate.setFavorite(currentCity.isFavorite());
                         }
                    }
                    if(update) {
                        manager.updateCity(toUpdate);
                        Toast.makeText(CityWeather.this, getResources().getText(R.string.city_Updated), Toast.LENGTH_LONG).show();
                    }
                    else {
                        manager.saveCity(new SavedCity(titleCase(city),
                                Country.toUpperCase(),
                                CityDayWeatherList.get(0).getAverageTemperature() + "",
                                new Date().toString(),
                                false));
                        Toast.makeText(CityWeather.this, getResources().getText(R.string.city_saved), Toast.LENGTH_LONG).show();
                    }

                }
                break;
            case R.id.itemSetting:
                Intent setting  = new Intent(CityWeather.this,Setting.class);
                startActivityForResult(setting,CITY_ACTIVITY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);


        if(getIntent().getExtras().containsKey(MainActivity.CITY_COUNTRY))
        {
            cityCountryName = getIntent().getExtras().getStringArray(MainActivity.CITY_COUNTRY);


            new WeatherAsyncTask(this).execute(generateURL());

        }

        topTag = (TextView)findViewById(R.id.textViewTopView);
        detailTag = (TextView)findViewById(R.id.textViewDetailView);



        String cityCountry = (cityCountryName[0].contains("_") ?
                (titleCase(cityCountryName[0].replace("_"," "))) :
                (titleCase(cityCountryName[0])) )+", " + (cityCountryName[1].toUpperCase());
        topTag.setText("Daily Forecast for " +cityCountry  );
    }


    public static String titleCase(String input)
    {
        String[] words = input.toString().split(" ");
        StringBuilder sb = new StringBuilder();
        if (words[0].length() > 0) {
            sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
            for (int i = 1; i < words.length; i++) {
                sb.append(" ");
                sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
            }
        }
        String titleCaseValue = sb.toString();
        return titleCaseValue;
    }

    private String generateURL()
    {
        String city = cityCountryName[0];
        String country = cityCountryName[1];
        HashMap<String,String> urlGenerate = new HashMap<>();

        urlGenerate.put(KEY, API_KEY);
        urlGenerate.put(COUNTRY, country);
        urlGenerate.put(CITY, city);

        String finalUrl ="";
        String thisBaseUrl = BASE_URL;
        for(HashMap.Entry<String, String> val : urlGenerate.entrySet())
        {
            finalUrl = thisBaseUrl.replace(val.getKey(), val.getValue());
            thisBaseUrl = finalUrl;
        }
        return finalUrl;
    }



    private void BindLists()
    {
        SharedPreferences mPrefs = getSharedPreferences(MainActivity.TEMPERATURE_UNIT,MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String type = mPrefs.getString(MainActivity.TEMPERATURE_KEY,null);
        if(type!=null) {
            tType = (type.equals("F")) ? true : false;
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewTopWeather);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        cityWeatherAdapter = new CityWeatherAdapter(CityDayWeatherList,this,R.layout.item_each_day,tType);
        cityWeatherAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(cityWeatherAdapter);
        loadDefault(0);
    }

    @Override
    public void returnAllCityWeather(ArrayList<DayWeather> weathers) {
        CityDayWeatherList = weathers;
        if(weathers.get(0).isError())
        {
            Toast.makeText(CityWeather.this,weathers.get(0).getError().getErrorString(),Toast.LENGTH_SHORT).show();
            CountDownTimer timer = new CountDownTimer(defaultTimer, 1000) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    cancel();
                    Intent toSend = new Intent();
                    setResult(RESULT_OK,toSend);
                    finish();
                }
            }.start();
        }
        else {
            BindLists();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void loadDefault(int pos)
    {
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewDetailWeather);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        DayWeather dayWeather = CityDayWeatherList.get(pos);
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        String outPut = format.format(dayWeather.getDate());

        detailTag.setText("Three Hourly Forecast on " + outPut);
        cityWeatherHourAdapter = new CityWeatherHourAdapter(CityDayWeatherList.get(pos).getDayObject(),this,R.layout.item_each_hour,tType);
        recyclerView.setAdapter(cityWeatherHourAdapter);
    }


    @Override
    public void OnItemClick(int p) {

        loadDefault(p);

    }
}
