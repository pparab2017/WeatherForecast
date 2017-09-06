package com.example.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, SavedCityAdapter.ItemClickCityCallBack {

    EditText textCity ,textcountry ;
    TextView title;
    Button btnSearch;
    static final String CITY_COUNTRY ="city_country";
    static final int CITY_WEATHER = 001;
    private RecyclerView recyclerView;
    private SavedCityAdapter savedCityAdapter;
    private boolean tType= false;
    List<SavedCity> allSavedCityList;
    final static String TEMPERATURE_UNIT = "temperatureUnit";
    final static String TEMPERATURE_KEY = "temperatureKey";
    final static int MAIN_ACTIVITY = 0011;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myMenu = getMenuInflater();
        myMenu.inflate(R.menu.menu_main_one,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case MAIN_ACTIVITY:
                    BindCity();
                    break;
                case CITY_WEATHER:
                   // BindCity();
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.itemSetting:
                Intent setting  = new Intent(MainActivity.this,Setting.class);
                startActivityForResult(setting,MAIN_ACTIVITY);
                break;
    }
        return super.onOptionsItemSelected(item);
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            SetTheAppIcon();
        textCity = (EditText) findViewById(R.id.editTextCity);
        textcountry = (EditText) findViewById(R.id.editTextCountry);
        btnSearch = (Button) findViewById(R.id.buttonSearch);
        btnSearch.setOnClickListener(this);
        title =(TextView)findViewById(R.id.textViewTitleSaved);

        BindCity();
}


    private void BindCity()
    {


        recyclerView =null;
        SharedPreferences mPrefs = getSharedPreferences(MainActivity.TEMPERATURE_UNIT,MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String type = mPrefs.getString(TEMPERATURE_KEY,null);
        if(type!=null) {
            tType = (type.equals("F")) ? true : false;
        }
        DataBaseManager manager;
        manager = new DataBaseManager(MainActivity.this);
        allSavedCityList = new ArrayList<SavedCity>();
        allSavedCityList = manager.getAllCities();
        manager.close();

        title.setText("Saved Cities");
        title.setPadding(20,20,20,20);
        if(allSavedCityList.size() <= 0)
        {
            title.setText("There are no cities to display. Search the city from the search box and save.");
            title.setPadding(0,100,0,0);
        }

        Collections.sort(allSavedCityList, new Comparator<SavedCity>() {
            @Override
            public int compare(SavedCity lhs, SavedCity rhs) {
                if (lhs.isFavorite()){
                    return -1;
                }
                if (rhs.isFavorite()){
                    return 1;
                }
                return 0;

            }


        });

        recyclerView = (RecyclerView) findViewById(R.id.recycleViewSavedCity);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
        savedCityAdapter = new SavedCityAdapter(allSavedCityList,MainActivity.this,R.layout.item_each_city,tType);
        recyclerView.setAdapter(savedCityAdapter);


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        BindCity();
    }


    @Override
    public void onClick(View v) {


        switch (v.getId())
        {
            case R.id.buttonSearch:

                String city = textCity.getText().toString().trim();
                String country = textcountry.getText().toString().trim();
                if(!city.isEmpty() && !country.isEmpty()) {

                    city = city.contains(" ") ? city.replace(" ", "_") : city;

                    Intent cityWeather = new Intent(MainActivity.this, CityWeather.class);
                    cityWeather.putExtra(CITY_COUNTRY, new String[]{city,country});
                    startActivityForResult(cityWeather, CITY_WEATHER);
                    textCity.setText("");
                    textcountry.setText("");

                }
                else
                {
                    Toast.makeText(MainActivity.this, getResources().getText(R.string.errorNoCityState),Toast.LENGTH_LONG).show();
                }
                break;

        }
    }



    @Override
    public void OnItemClick(int p,boolean favorite) {
        SavedCity city = allSavedCityList.get(p);
        DataBaseManager manager;
        manager = new DataBaseManager(MainActivity.this);
        city.setFavorite(favorite);
        manager.updateCity(city);
        manager.close();
        BindCity();
    }

    @Override
    public void OnItemDelete(int p) {
        SavedCity city = allSavedCityList.get(p);

        DataBaseManager manager;
        manager = new DataBaseManager(MainActivity.this);
        manager.deleteCity(city);
        manager.close();

        BindCity();
        Toast.makeText(MainActivity.this,"Deleted",Toast.LENGTH_LONG).show();

    }

    private void SetTheAppIcon()
    {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }
}
