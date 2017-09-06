package com.example.weatherforecast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pushparajparab on 10/18/16.
 */
public class WeatherParser {

    private static String START_NODE="forecast";
    public WeatherParser(String startNode)
    {
        this.START_NODE = startNode;
    }

    static class PullParser
    {

        static ArrayList<DayWeather> GetWeatherFromInputStream(InputStream inputStream) throws XmlPullParserException, IOException {
            XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
            pullParser.setInput(inputStream,"UTF-8");
            int event = pullParser.getEventType();
            boolean flag = true;
            ArrayList<DayWeather> dayWeathers = new ArrayList<DayWeather>();
            ArrayList<Weather> weathers = new ArrayList<Weather>();
            DayWeather dayWeather = new DayWeather();
            Weather toAdd = new Weather();

            while (event != XmlPullParser.END_DOCUMENT & flag)
            {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if(pullParser.getName().equals("forecast"))
                            flag = false;
                        break;

                    case XmlPullParser.END_TAG:
                        break;
                }
                event=pullParser.next();
            }
            Date CurrentDate = null;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if(pullParser.getName().equals("time")) {
                            toAdd = new Weather();
                            dayWeather = new DayWeather();
                            toAdd.setFromDate(pullParser.getAttributeValue(null,"from"));
                            toAdd.setToDate(pullParser.getAttributeValue(null,"to"));
                            dayWeather.setDate(toAdd.getFromDateOnly());
                        }

                        else if(pullParser.getName().equals("symbol")) {
                            toAdd.setIcon(new Value(
                                    pullParser.getAttributeValue(null,"name"),
                                    pullParser.getAttributeValue(null,"var")
                            )
                            );
                        }

                        else if(pullParser.getName().equals("temperature")) {
                            toAdd.setTemperature(new Value(
                                            pullParser.getAttributeValue(null,"unit"),
                                            pullParser.getAttributeValue(null,"value")
                                    )
                            );
                        }

                        else if(pullParser.getName().equals("windDirection")) {

                            toAdd.setWindDirection(new Value(
                                            pullParser.getAttributeValue(null,"code"),
                                            pullParser.getAttributeValue(null,"deg")
                                    )
                            );
                        }
                        else if(pullParser.getName().equals("windSpeed")) {

                            toAdd.setWindSpeed(new Value(
                                            pullParser.getAttributeValue(null,"name"),
                                            pullParser.getAttributeValue(null,"mps")
                                    )
                            );
                        }
                        else if(pullParser.getName().equals("pressure")) {
                            toAdd.setPressure(new Value(
                                            pullParser.getAttributeValue(null, "unit"),
                                            pullParser.getAttributeValue(null, "value")
                                    )
                            );
                        }
                        else if(pullParser.getName().equals("humidity")) {
                            toAdd.setHumidity(new Value(
                                            pullParser.getAttributeValue(null, "unit"),
                                            pullParser.getAttributeValue(null, "value")
                                    )
                            );
                        }
                        else if(pullParser.getName().equals("clouds")) {
                            toAdd.setClouds(new Value(
                                            pullParser.getAttributeValue(null, "unit"),
                                            pullParser.getAttributeValue(null, "value")
                                    )
                            );
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        if (pullParser.getName().equals("time")) {
                            //dayWeather.
                            weathers.add(toAdd);
                            if(dayWeathers.size() > 0) {
                                if (!dayWeathers.get(dayWeathers.size() - 1).getDate().equals(dayWeather.getDate())) {
                                    dayWeathers.add(dayWeather);
                                }
                            }
                            else {
                                dayWeathers.add(dayWeather);
                            }


                            //toAdd.getFromDate().

                            toAdd = null;
                            dayWeather = null;
                            break;
                        }
                }
                event=pullParser.next();


            }
            for(int i=0;i<dayWeathers.size();i++)
            {
                ArrayList<Weather> toAddToMain = new ArrayList<Weather>();
                for(int j=0;j<weathers.size();j++)
                {
                    if(dayWeathers.get(i).getDate().equals(weathers.get(j).getFromDateOnly()))
                    {
                        toAddToMain.add(weathers.get(j));
                    }
                }
                dayWeathers.get(i).setDayObject(toAddToMain);
            }

            return dayWeathers;


        }
    }

}
