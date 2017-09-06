package com.example.weatherforecast;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;

public class Setting extends AppCompatActivity {

    CharSequence type[] = {"C °","F °"};
    int selectedType =0;

    int defaultSelect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        LinearLayout layout = (LinearLayout)findViewById(R.id.tempType);
        layout.setOnClickListener(new View.OnClickListener() {
            SharedPreferences mPrefs = getSharedPreferences(MainActivity.TEMPERATURE_UNIT,MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            String getType = mPrefs.getString(MainActivity.TEMPERATURE_KEY,null);

            @Override
            public void onClick(View v) {
                if(getType!=null) {
                    defaultSelect = (getType.equals("C")) ? 0 : 1;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                builder.setTitle("Select Temperature Unit")
                        .setSingleChoiceItems(type,defaultSelect, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedType = which;

                            }
                        }).setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tType = "C";
                        if(selectedType == 0)
                        {
                            tType = "C";
                        }else
                        {
                            tType ="F";
                        }


                        prefsEditor.putString(MainActivity.TEMPERATURE_KEY, tType);
                        prefsEditor.commit();

                        Intent toSend = new Intent();
                        setResult(RESULT_OK,toSend);
                        finish();
                    }
                }).show();

            }
        });

    }
}
