package com.example.nathan.prco303app_foodr;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class SettingsActivity extends ActionBarActivity {

    Intent receivedIntent;
    String user;
    userSettingsClass settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        receivedIntent = getIntent();
        user = receivedIntent.getStringExtra("userID");
        try {
            settings = new getSettings().execute(user).get();
        }catch(InterruptedException e)
        {

        }catch(ExecutionException e)
        {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    private class getSettings extends AsyncTask<String, Void, userSettingsClass>
    {

        @Override
        protected userSettingsClass doInBackground(String... user) {
            return getSettingClass(user[0]);
        }

        private userSettingsClass getSettingClass(String user)
        {
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

                for(int i = 0; i < typeData.length(); i++)
                {
                    typeList.add(typeData.getString(i));
                }

                newSettings = new userSettingsClass(settingData.getInt("Radius"), typeList);
            }catch(IOException e)
            {

            }catch(JSONException e)
            {

            }


            return newSettings;
        }

        private String readIt(InputStream stream){
            try{

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                StringBuilder responseStr = new StringBuilder();
                String inputStr;

                while((inputStr=reader.readLine()) !=null){
                    responseStr.append(inputStr);
                }

                return responseStr.toString();

            } catch (UnsupportedEncodingException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class postSettings extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... params) {
            return null;
        }
    }
}
