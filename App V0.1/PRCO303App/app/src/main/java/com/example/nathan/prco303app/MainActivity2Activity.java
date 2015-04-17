package com.example.nathan.prco303app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity2Activity extends ActionBarActivity {
    private TextView address;
    private TextView phone;
    private TextView open;
    private TextView weekTimes;
    private Button favourite;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        address = (TextView) findViewById(R.id.addressText);
        phone = (TextView) findViewById(R.id.phoneText);
        open = (TextView) findViewById(R.id.openText);
        weekTimes = (TextView) findViewById(R.id.weekText);
        favourite = (Button) findViewById(R.id.favouriteButton);
        intent = getIntent();
        String id = intent.getStringExtra("placeID");
        String url = "http://10.0.2.2/detail?id="+id;
        new DetailedPlaceSearch().execute(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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

    public void FavouriteHandler(View view)
    {
        String userID = "userID1";
        String placeID = intent.getStringExtra("placeID");
        String location = intent.getStringExtra("placeLocation");
        String name = intent.getStringExtra("placeName");

        Intent serviceIntent = new Intent(this, AddFavouriteService.class);
        serviceIntent.putExtra("placeID", placeID);
        serviceIntent.putExtra("placeName", name);
        serviceIntent.putExtra("placeLocation", location);
        serviceIntent.putExtra("userID", userID);
        startService(serviceIntent);
       // new testFavourite().execute(serviceIntent);
    }

    private class DetailedPlaceSearch extends AsyncTask<String, Void, DetailedPlace>{


        @Override
        protected DetailedPlace doInBackground(String... urls) {

            return getPlace(urls[0]);

        }

        protected void onPostExecute(DetailedPlace result){
            if(result.getFormattedAddress() != null)
            address.setText(result.getFormattedAddress());

            if(result.getFormattedPhone() != null)
            phone.setText(result.getFormattedPhone());

            String week="";

            if(result.weekdayText != null){
            for(int i = 0; i < result.weekdayText.size(); i++)
            {
                week += " " + result.weekdayText.get(i);
            }
            weekTimes.setText(week);}

            if(result.getOpenNow() != null) {
                if (result.getOpenNow()) {
                    open.setText("True");
                } else {
                    open.setText("False");
                }
            }
        }


        private DetailedPlace getPlace(String myURL){
            DetailedPlace newPlace = null;
            InputStream is = null;
            HttpURLConnection conn = null;

            try {
                URL url = new URL(myURL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                is = conn.getInputStream();
                String jsonString = null;
                JSONArray jsonArrayData = null;
                JSONObject jsonData = null;
                int size = is.available();
                byte[] bytes = new byte[size];
                jsonString = readIt(is);
                List<String> weekList = new ArrayList<String>();
                is.close();

                jsonData = new JSONObject(jsonString);

                if(!jsonData.isNull("weekTimes")) {
                    jsonArrayData = jsonData.getJSONArray("weekTimes");

                    //  weekList = (ArrayList<String>)jsonData.get("weekTimes");

                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        weekList.add(jsonArrayData.get(i).toString());
                    }
                }

                if(jsonData.isNull("formattedAddress"))
                    jsonData.put("formattedAddress","No Address");

                if(jsonData.isNull("formattedPhone"))
                    jsonData.put("formattedPhone","No Phone");

                if(weekList.size() == 0)
                    weekList.add("No week Information");

                newPlace = new DetailedPlace(jsonData.getString("formattedAddress"), jsonData.getString("formattedPhone"), weekList, jsonData.getBoolean("openNow"));
            } catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }catch(JSONException e){
                e.printStackTrace();
            } finally{
                conn.disconnect();
                return newPlace;
            }

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

    private class testFavourite extends AsyncTask<Intent, Void, Void> {

        @Override
        protected Void doInBackground(Intent... intentParam) {
            Place favedPlace = new Place(intent.getStringExtra("placeID"), intent.getStringExtra("placeName"), intent.getStringExtra("placeLocation"));
            String userID = "userID101";
            JSONObject jsonData = new JSONObject();
            JSONObject placeData = new JSONObject();
            String jsonString = "";

            try {
                placeData.put("name", favedPlace.getName());
                placeData.put("location", favedPlace.getLocation());
                placeData.put("placeid", favedPlace.getId());
                jsonData.put("user_id", userID);
                jsonData.put("place", placeData);
                jsonString = jsonData.toString();
            }catch(JSONException e){

            }

            try {

                URL url = new URL("http://10.0.2.2/favourite");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                conn.setChunkedStreamingMode(0);
                conn.connect();



                OutputStream out = conn.getOutputStream();
                byte[] outputInBytes = jsonString.getBytes("UTF-8");
                out.write(outputInBytes);
                out.flush();

                int response = conn.getResponseCode();
              //  InputStream responseStr = new BufferedInputStream(conn.getInputStream());



                // jsonStream.flush();
                //   jsonStream.close();


                int bla = 2;

            }catch(IOException e){

            }
        return null;
        }
    }
}
