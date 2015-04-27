package com.example.nathan.prco303app_foodr;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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


public class UserMapActivity extends FragmentActivity
    implements OnMapReadyCallback{

    private List<placeClass> placeValues;
    Intent receivedIntent;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);

        receivedIntent = getIntent();
        userID = receivedIntent.getStringExtra("UID");

        placeValues = new ArrayList<placeClass>();
        String url = "http://10.0.2.2/favourite?id=" + userID;
        try {
            placeValues = new getFavouriteList().execute(url).get();
        }catch(InterruptedException e)
        {

        }catch(ExecutionException e)
        {

        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_map, menu);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*
        googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(50.3719165,-4.13601979))).setTitle("Marker"); */

        String[] location;
        double lat = 0;
        double lng = 0;

        for(int i = 0; i < placeValues.size(); i++)
        {
            location = placeValues.get(i).getPlaceLocation().split(",");
            lat = Double.parseDouble(location[0]);
            lng = Double.parseDouble(location[1]);

            googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(lat,lng))).setTitle(placeValues.get(i).getPlaceName());
        }
    }

    private class getFavouriteList extends AsyncTask<String, Void, List<placeClass>>
    {

        @Override
        protected List<placeClass> doInBackground(String... urls) {
            return getFavourites(urls[0]);
        }

        private List<placeClass> getFavourites(String url)
        {
            List<placeClass> favouriteList = new ArrayList<placeClass>();
            placeClass newPlace;
            InputStream is = null;
            String jsonString = "";
            String placeName;

            try{
                URL getURL = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) getURL.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                is = conn.getInputStream();
                jsonString = readIt(is);

                JSONArray favouriteArray = new JSONArray(jsonString);
                JSONObject favouriteObject;

                for(int i = 0; i < favouriteArray.length(); i++)
                {
                    favouriteObject = favouriteArray.getJSONObject(i);
                    newPlace = new placeClass(favouriteObject.getString("name"), favouriteObject.getString("placeid"), favouriteObject.getString("location"));
                    favouriteList.add(newPlace);
                }

            }catch(IOException e){

            } catch(JSONException e)
            {

            }


            return favouriteList;
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
