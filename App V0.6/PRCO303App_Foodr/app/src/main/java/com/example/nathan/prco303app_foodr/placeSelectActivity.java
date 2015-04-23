package com.example.nathan.prco303app_foodr;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class placeSelectActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    private TextView textName;
    private TextView textVicinity;
    private TextView textTypes;
    private Button noButton;
    private Button yesButton;
    private Button detailButton;
    private TextView lngText;
    private TextView latText;
    LocationRequest requestLocat;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Boolean requestingLocationUpdated = true;
    List<placeClass> placeList = new ArrayList<placeClass>();
    int index = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_select);
        textName = (TextView)findViewById(R.id.placeNameView);
        textVicinity = (TextView)findViewById(R.id.placeVicinityView);
        textTypes = (TextView)findViewById(R.id.placeTypesView);
        noButton = (Button)findViewById(R.id.noButton);
        yesButton = (Button)findViewById(R.id.yesButton);
        detailButton = (Button)findViewById(R.id.detailButton);
        latText = (TextView) findViewById(R.id.latText);
        lngText = (TextView) findViewById(R.id.lngText);
        requestLocat = new LocationRequest();
        requestLocat.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        requestLocat.setInterval(90); //Every 15 minutes
        requestLocat.setFastestInterval(90);
        requestLocat.setSmallestDisplacement(1);
        buildGoogleApiClient();

        mGoogleApiClient.connect();
        //Put async task here
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_favourites:
                openFavourites();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openFavourites()
    {
        Intent favouriteIntent = new Intent(this, favouriteListActivity.class);
        startActivity(favouriteIntent);
    }

    public void yesClickHandler(View view)
    {
        String userId = "userID1";
        placeClass newPlace = placeList.get(index);
        favouriteClass favouriteObject = new favouriteClass(userId, newPlace);
        new sendFavouriteTask().execute(favouriteObject);


        index++;
        if(index < placeList.size()) {
            textName.setText(placeList.get(index).getPlaceName());
            textVicinity.setText(placeList.get(index).getPlaceVicinity());
            String typesStr = "";
            for(int i =0; i < placeList.get(index).getPlaceTypes().size(); i++)
            {
                if(i == 0) {
                    typesStr = placeList.get(index).getPlaceTypes().get(i);
                }
                else{
                    typesStr += "|" + placeList.get(index).getPlaceTypes().get(i);
                }
            }
            textTypes.setText(typesStr);
        }
        else
        {
            textName.setText("end reached");
        }



    }

    public void noClickHandler(View view)
    {
        index++;
        if(index < placeList.size()) {
            textName.setText(placeList.get(index).getPlaceName());
            textVicinity.setText(placeList.get(index).getPlaceVicinity());
            String typesStr = "";
            for(int i =0; i < placeList.get(index).getPlaceTypes().size(); i++)
            {
                if(i == 0) {
                    typesStr = placeList.get(index).getPlaceTypes().get(i);
                }
                else{
                    typesStr += "|" + placeList.get(index).getPlaceTypes().get(i);
                }
            }
            textTypes.setText(typesStr);
        }
        else
        {
            textName.setText("end reached");
        }


    }

    public void detailClickHandler(View view)
    {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        placeClass selectedPlace = placeList.get(index);
        detailIntent.putExtra("placeName",selectedPlace.getPlaceName());
        detailIntent.putExtra("placeID", selectedPlace.getPlaceID());
        detailIntent.putExtra("placeTypes", textTypes.getText());
        startActivity(detailIntent);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);



        //AsyncTask here
        if (mLastLocation != null) {

      //      latText.setText(String.valueOf(mLastLocation.getLatitude()));
      //      lngText.setText(String.valueOf(mLastLocation.getLongitude()));
            String userID = "userID1";
            String location = String.valueOf(mLastLocation.getLatitude()) + "," + String.valueOf(mLastLocation.getLongitude());
            String strURL = "http://10.0.2.2/place?uid=" + userID + "&location=" + location;
            try {
                placeList = new getPlacesTask().execute(strURL).get();
            } catch (InterruptedException e) {

            } catch (ExecutionException e) {

            }
        } else{
            String userID = "userID1";
            String location="50.3719165,-4.13601979";
            String strURL = "http://10.0.2.2/place?uid=" + userID + "&location=" + location;
            try {
                placeList = new getPlacesTask().execute(strURL).get();
            } catch (InterruptedException e) {

            } catch (ExecutionException e) {

            }
        }
        if(requestingLocationUpdated)
        {
            startLocationUpdates();
        }

    }

    protected void startLocationUpdates()
    {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, requestLocat,
                 this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        updateUI();
        String userID = "userID1";
        String locationStr = String.valueOf(mLastLocation.getLatitude()) + "," + String.valueOf(mLastLocation.getLongitude());
        String strURL = "http://10.0.2.2/place?uid=" + userID + "&location=" + locationStr;

        try {
            placeList = new getPlacesTask().execute(strURL).get();
        }catch(InterruptedException e)
        {

        } catch(ExecutionException e)
        {

        }

    }

    public void updateUI()
    {
        lngText.setText(String.valueOf(mLastLocation.getLongitude()));
        latText.setText(String.valueOf(mLastLocation.getLatitude()));
    }

    private class getPlacesTask extends AsyncTask<String, Void, List<placeClass>>
    {

        @Override
        protected List<placeClass> doInBackground(String... urls) {
            return searchPlaces(urls[0]);
        }

        protected void onPostExecute(List<placeClass> result)
        {
            placeClass place = result.get(0);
            textName.setText(place.getPlaceName());
            textVicinity.setText(place.getPlaceVicinity());
            String typesText="";
            List<String> typeList = place.getPlaceTypes();
            for (int i=0; i < typeList.size(); i++)
            {
                if(i==0)
                {
                    typesText = typeList.get(i);
                }

                typesText += "|" + typeList.get(i);
            }
            textTypes.setText(typesText);
        }

        private List<placeClass> searchPlaces(String url)
        {
            List<placeClass> placeList = new ArrayList<placeClass>();
            String jsonString;
            InputStream is = null;

            try{
                URL getURL = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) getURL.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                is = conn.getInputStream();
         //       int size = is.available();
                jsonString = readIt(is);

                JSONArray placeArray = new JSONArray(jsonString);
                JSONObject placeData = new JSONObject();
                JSONArray typeData = new JSONArray();
                placeClass newPlace;
                String[] typesArr;
                String test = "";

                for(int i = 0; i < placeArray.length(); i++)
                {

                    placeData = placeArray.getJSONObject(i);
                    typeData = placeData.getJSONArray("types");
                    typesArr = new String[typeData.length()];
                    for(int j = 0; j < typeData.length(); j++)
                    {
                        typesArr[j] = typeData.getString(j);

                    }



                    newPlace = new placeClass(placeData.getString("name"), placeData.getString("placeid"), placeData.getString("location"), placeData.getString("vicinity"), typesArr);

                    placeList.add(newPlace);
                }

            } catch(IOException e){

            } catch (JSONException e)
            {

            }

            return placeList;
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

    private class sendFavouriteTask extends AsyncTask<favouriteClass, Void, Void>
    {

        @Override
        protected Void doInBackground(favouriteClass... favourite) {
            return postTask(favourite[0]);
        }

        private Void postTask(favouriteClass favouritedPlace)
        {
          //
          // String url = "http://10.0.2.2/favourite";
            JSONObject favouriteData = new JSONObject();
            JSONObject placeData = new JSONObject();
            JSONArray typeData = new JSONArray();
            String jsonString = "";

            try {
                favouriteData.put("user_id", favouritedPlace.getUserID());
                placeData.put("name", favouritedPlace.getPlaceInformation().getPlaceName());
                placeData.put("placeid", favouritedPlace.getPlaceInformation().getPlaceID());
                placeData.put("location", favouritedPlace.getPlaceInformation().getPlaceLocation());
                placeData.put("vicinity", favouritedPlace.getPlaceInformation().getPlaceVicinity());
                for(int i = 0; i < favouritedPlace.getPlaceInformation().getPlaceTypes().size(); i++)
                {
                    typeData.put(favouritedPlace.getPlaceInformation().getPlaceTypes().get(i));
                }
                //placeData.put("types", favouritedPlace.getPlaceInformation().getPlaceTypes());
                placeData.put("types", typeData);
                favouriteData.put("place", placeData);
                jsonString = favouriteData.toString();



            }catch (JSONException e){

            }

            try{
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
            } catch(IOException e){

            }


            return null;
        }
    }
}
