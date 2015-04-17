package com.example.nathan.prco303app;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class AddFavouriteService extends IntentService {
  /*  // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "com.example.nathan.prco303app.action.FOO";
    public static final String ACTION_BAZ = "com.example.nathan.prco303app.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.example.nathan.prco303app.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.example.nathan.prco303app.extra.PARAM2"; */

    public AddFavouriteService() {
        super("AddFavouriteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            Place favedPlace = new Place(intent.getStringExtra("placeID"), intent.getStringExtra("placeName"), intent.getStringExtra("placeLocation"));
            String userID = intent.getStringExtra("userID");
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

            }catch(IOException e){

            }
        }
    }


}
