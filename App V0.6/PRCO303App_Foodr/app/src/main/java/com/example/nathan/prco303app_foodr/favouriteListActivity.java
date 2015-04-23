package com.example.nathan.prco303app_foodr;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.List;
import java.util.concurrent.ExecutionException;


public class favouriteListActivity extends ListActivity {

    private TextView text;
    private List<String> listValues;
    private List<placeClass> placeValues;
    String userID = "userID1";
    String selectedItem;
    int selectedPosition;
    ArrayAdapter<String> myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        text = (TextView) findViewById(R.id.mainText);

        listValues = new ArrayList<String>();
        placeValues = new ArrayList<placeClass>();
        String url = "http://10.0.2.2/favourite?id=" + userID;
        try {
            placeValues = new getFavouriteList().execute(url).get();
            populateList();
        }catch(InterruptedException e)
        {

        }catch(ExecutionException e)
        {

        }

     /*   listValues.add("Android");
        listValues.add("iOS");
        listValues.add("Symbian");
        listValues.add("Blackberry");
        listValues.add("Windows Phone"); */

        myAdapter =
                new ArrayAdapter<String>(this, R.layout.row_layout,
                R.id.listText, listValues);

        setListAdapter(myAdapter);
    }

    public void populateList()
    {
        String placeName;
        for(int i =0; i < placeValues.size(); i++)
        {
            placeName = placeValues.get(i).getPlaceName();
            listValues.add(placeName);
        }
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id){
        super.onListItemClick(list,view,position,id);

         selectedItem = (String)getListView().getItemAtPosition(position);
         selectedPosition = position;

        text.setText("You clicked " + selectedItem + " at position " + position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favourite_list, menu);
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

    public void deleteHandler(View view)
    {
        if(!listValues.isEmpty()) {
            String id = placeValues.get(selectedPosition).getPlaceID();
            String url = "http://10.0.2.2/favourite?uid=" + userID + "&pid=" + id;
            new deleteFavourite().execute(url);
            placeValues.clear();
            listValues.clear();
            myAdapter.notifyDataSetChanged();

            url = "http://10.0.2.2/favourite?id=" + userID;
            try {
                placeValues = new getFavouriteList().execute(url).get();
            } catch (InterruptedException e) {

            } catch (ExecutionException e) {

            }
            populateList();
        }
    }

    public void reviewHandler(View view)
    {
        if(!listValues.isEmpty()) {
            Intent reviewIntent = new Intent(this, ReviewActivity.class);
            String pid = placeValues.get(selectedPosition).getPlaceID();
            reviewIntent.putExtra("place_id", pid);
            reviewIntent.putExtra("user_id", userID);
            startActivity(reviewIntent);
        }
    }

    public void viewHandler(View view)
    {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        placeClass selectedPlace = placeValues.get(selectedPosition);
        detailIntent.putExtra("placeID", selectedPlace.getPlaceID());
        detailIntent.putExtra("placeName", selectedPlace.getPlaceName());

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

    private class deleteFavourite extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... urls) {
            return deleteFavourite(urls[0]);
        }

        private Void deleteFavourite(String url)
        {
            String jsonString = "";
            try {
                URL getURL = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) getURL.openConnection();
                conn.setRequestMethod("DELETE");
                conn.connect();

                OutputStream out = conn.getOutputStream();
                byte[] outputInBytes = jsonString.getBytes("UTF-8");
                out.write(outputInBytes);
                out.flush();

                int response = conn.getResponseCode();
            } catch(IOException e)
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
