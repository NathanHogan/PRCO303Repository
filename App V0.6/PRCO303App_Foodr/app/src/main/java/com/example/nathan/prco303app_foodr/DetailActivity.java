package com.example.nathan.prco303app_foodr;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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


public class DetailActivity extends ActionBarActivity {
    private TextView name;
    private TextView address;
    private TextView phone;
    private TextView open;
    private TextView type;
    private TextView week;
    private Intent receivedIntent;
    private String placeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        name = (TextView)findViewById(R.id.nameView);
        address = (TextView) findViewById(R.id.addressView);
        phone = (TextView) findViewById(R.id.phoneView);
        open = (TextView) findViewById(R.id.openView);
        type = (TextView) findViewById(R.id.typeView);
        week = (TextView) findViewById(R.id.weekView);
        receivedIntent = getIntent();
        String typeStr = "";
        placeID = receivedIntent.getStringExtra("placeID");
        String nameStr = receivedIntent.getStringExtra("placeName");
        if(receivedIntent.hasExtra("placeTypes")) {
             typeStr = receivedIntent.getStringExtra("placeTypes");
        }else
        {
             typeStr="";
        }
        String url = "http://10.0.2.2/detail?pid=" + placeID;
        new detailGet().execute(url);
        name.setText(nameStr);
        type.setText(typeStr);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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

    public void placeReviewHandler(View view)
    {
        Intent reviewIntent = new Intent(this, ReviewListActivity.class);
        reviewIntent.putExtra("pid", placeID);
        startActivity(reviewIntent);
    }

    private class detailGet extends AsyncTask<String, Void, detailClass>
    {
        @Override
        protected detailClass doInBackground(String... url) {
            return detailRequest(url[0]);
        }

        protected void onPostExecute(detailClass result)
        {
            address.setText(result.getAddress());
            phone.setText(result.getPhone());
            String weekText = "";
            for(int i = 0; i < result.getWeekTimes().size(); i++)
            {
                weekText += result.getWeekTimes().get(i) + " ";
            }
            week.setText(weekText);
        }

        private detailClass detailRequest(String url)
        {
            String jsonString;
            InputStream is = null;
            detailClass requestedPlace;

            try {
                URL getURL = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) getURL.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                is = conn.getInputStream();
                //       int size = is.available();
                jsonString = readIt(is);

                JSONObject detailObject = new JSONObject(jsonString);
                JSONArray weekArray = new JSONArray();
                weekArray = detailObject.getJSONArray("weekTimes");
                List<String> weekList = new ArrayList<String>();
                String weekText = "";

                for(int i = 0; i < weekArray.length(); i++)
                {
                    weekText = weekArray.getString(i);
                    weekList.add(weekText);
                }

                requestedPlace = new detailClass(detailObject.getString("formattedAddress"), detailObject.getString("formattedPhone"),weekList, detailObject.getBoolean("openNow"));


                return requestedPlace;
            }catch(IOException e)
            {

            } catch (JSONException e)
            {

            }
            return null;
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

}
