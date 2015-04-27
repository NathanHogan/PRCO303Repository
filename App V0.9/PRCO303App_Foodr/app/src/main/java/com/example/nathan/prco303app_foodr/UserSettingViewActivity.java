package com.example.nathan.prco303app_foodr;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class UserSettingViewActivity extends ActionBarActivity {

    Intent receivedIntent;
    String user;
    userSettingsClass settings;
    TextView radius;
    SeekBar radiusSeek;
    CheckBox food;
    CheckBox bakery;
    CheckBox cafe;
    CheckBox establishment;
    CheckBox takeaway;
    CheckBox restaurant;
    CheckBox delivery;
    CheckBox bar;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting_view);

        radius = (TextView) findViewById(R.id.radiusText);
        radiusSeek = (SeekBar) findViewById(R.id.radiusBar);
        food = (CheckBox) findViewById(R.id.foodCheck);
        bakery = (CheckBox) findViewById(R.id.bakeryCheck);
        cafe = (CheckBox) findViewById(R.id.cafeCheck);
        establishment = (CheckBox) findViewById(R.id.establishmentCheck);
        takeaway = (CheckBox) findViewById(R.id.mealTakeawayCheck);
        restaurant = (CheckBox) findViewById(R.id.restaurantCheck);
        delivery = (CheckBox) findViewById(R.id.mealDeliveryCheck);
        bar = (CheckBox) findViewById(R.id.barCheck);
        submit = (Button) findViewById(R.id.submitButton);

        receivedIntent = getIntent();
        user = receivedIntent.getStringExtra("userID");
        try {
            settings = new getSettings().execute(user).get();
        }catch(InterruptedException e)
        {

        }catch(ExecutionException e)
        {

        }
        initialiseUI();

    }

    private void initialiseUI()
    {
        radius.setText(String.valueOf(settings.getRadius()));
        radiusSeek.setProgress(settings.getRadius());
        String type = "";

        for(int i = 0; i < settings.getTypes().size(); i++)
        {
            type = settings.getTypes().get(i);

            switch(type){
                case "bar":
                    bar.setChecked(true);
                    break;
                case "restaurant":
                    restaurant.setChecked(true);
                    break;
                case "food":
                    food.setChecked(true);
                    break;
                case "bakery":
                    bakery.setChecked(true);
                    break;
                case "cafe":
                    cafe.setChecked(true);
                    break;
                case "establishment":
                    establishment.setChecked(true);
                    break;
                case "meal_delivery":
                    delivery.setChecked(true);
                    break;
                case "meal_takeaway":
                    takeaway.setChecked(true);
                    break;

            }
        }
    }

    public void submitHandler(View view)
    {
        ArrayList<String> typeArray = new ArrayList<String>();

        if (bar.isChecked())
        {
            typeArray.add("bar");
        }

        if (restaurant.isChecked())
        {
            typeArray.add("restaurant");
        }

        if (food.isChecked())
        {
            typeArray.add("food");
        }

        if (bakery.isChecked())
        {
            typeArray.add("bakery");
        }

        if (cafe.isChecked())
        {
            typeArray.add("cafe");
        }

        if (establishment.isChecked())
        {
            typeArray.add("establishment");
        }

        if (delivery.isChecked())
        {
            typeArray.add("meal_delivery");
        }

        if (takeaway.isChecked()){
            typeArray.add("meal_takeaway");
        }


        settings.setTypes(typeArray);
        settings.setUID(user);
        new postSettings().execute(settings);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_setting_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class getSettings extends AsyncTask<String, Void, userSettingsClass> {

        @Override
        protected userSettingsClass doInBackground(String... user) {
            return getSettingClass(user[0]);
        }

        private userSettingsClass getSettingClass(String user) {
            userSettingsClass newSettings = new userSettingsClass();
            String jsonString = "";
            InputStream is = null;
            String url = "http://10.0.2.2/person?id=" + user;

            try {
                URL getURL = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) getURL.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                is = conn.getInputStream();
                jsonString = readIt(is);
                JSONObject settingData = new JSONObject(jsonString);
                JSONArray typeData = new JSONArray();
                typeData = settingData.getJSONArray("Types");
                ArrayList<String> typeList = new ArrayList<String>();

                for (int i = 0; i < typeData.length(); i++) {
                    typeList.add(typeData.getString(i));
                }

                newSettings = new userSettingsClass(settingData.getInt("Radius"), typeList);
            } catch (IOException e) {

            } catch (JSONException e) {

            }


            return newSettings;
        }

        private String readIt(InputStream stream) {
            try {

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                StringBuilder responseStr = new StringBuilder();
                String inputStr;

                while ((inputStr = reader.readLine()) != null) {
                    responseStr.append(inputStr);
                }

                return responseStr.toString();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class postSettings extends AsyncTask<userSettingsClass, Void, Void>
    {

        @Override
        protected Void doInBackground(userSettingsClass... setting) {
            return postSettings(setting[0]);
        }

        private Void postSettings(userSettingsClass settingsToPost)
        {
            String url = "http://10.0.2.2/person?id=" + settingsToPost.getUID();
            JSONObject settingsData = new JSONObject();
            JSONArray typesArray = new JSONArray();
            String jsonString = "";

            try{
                for(int i = 0; i < settingsToPost.getTypes().size(); i++)
                {
                    typesArray.put(settingsToPost.getTypes().get(i));
                }
                settingsData.put("Radius", settingsToPost.getRadius());
                settingsData.put("Types", typesArray);
                jsonString = settingsData.toString();
            }catch(JSONException e)
            {

            }

            try{
                URL urlConn = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) urlConn.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");

                conn.setChunkedStreamingMode(0);
                conn.connect();



                OutputStream out = conn.getOutputStream();
                byte[] outputInBytes = jsonString.getBytes("UTF-8");
                out.write(outputInBytes);
                out.flush();

                int response = conn.getResponseCode();
            } catch(IOException e){

            }


            return null;
        }
    }
}


