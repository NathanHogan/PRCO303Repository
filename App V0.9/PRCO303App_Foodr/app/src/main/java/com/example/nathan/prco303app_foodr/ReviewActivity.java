package com.example.nathan.prco303app_foodr;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

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
import java.util.concurrent.ExecutionException;


public class ReviewActivity extends ActionBarActivity {
    private String userID;
    private String placeID;
    private Intent receivedIntent;
    private reviewClass review;
    EditText reviewWriter;
    RatingBar reviewRater;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        receivedIntent = getIntent();
        reviewRater = (RatingBar) findViewById(R.id.reviewRating);
        reviewWriter = (EditText) findViewById(R.id.reviewText);
        submitButton = (Button) findViewById(R.id.submitButton);
        placeID = receivedIntent.getStringExtra("place_id");
        userID = receivedIntent.getStringExtra("user_id");
        String url = "http://10.0.2.2/review?uid=" + userID + "&pid=" + placeID;
        try {
            review = new getReviewTask().execute(url).get();
        } catch(InterruptedException e)
        {

        }catch(ExecutionException e)
        {

        }
        updateUI();
    }

    public void updateUI()
    {
        if(review.getReviewPID() != "null")
        {
            reviewWriter.setText(review.getReviewText());
            reviewRater.setRating((float)review.getReviewRate()/2);
        }
    }


    public void submitHandler(View view)
    {
        float rating = reviewRater.getRating() * 2;
        int intRate = (int)rating;

        review = new reviewClass(reviewWriter.getText().toString(), placeID, userID, intRate);
        new postReviewTask().execute(review);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_review, menu);
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

    private class getReviewTask extends AsyncTask<String, Void,reviewClass>
    {

        @Override
        protected reviewClass doInBackground(String... urls) {
            return getReview(urls[0]);
        }

        private reviewClass getReview(String url)
        {
            reviewClass newReview = new reviewClass();
            InputStream is = null;
            String jsonString = "";

            try {
                URL getURL = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) getURL.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                is = conn.getInputStream();
                jsonString = readIt(is);

                JSONObject reviewData = new JSONObject(jsonString);
                newReview = new reviewClass(reviewData.getString("review"), reviewData.getString("place_id"), reviewData.getString("user_id"), reviewData.getInt("rating"));

            }catch(IOException e)
            {

            }catch(JSONException e)
            {

            }


            return newReview;
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

    private class postReviewTask extends AsyncTask<reviewClass,Void,Void>
    {

        @Override
        protected Void doInBackground(reviewClass... review) {
            return postTask(review[0]);
        }

        private Void postTask(reviewClass postReview)
        {
            JSONObject reviewData = new JSONObject();
            String jsonString = "";

            try {
                reviewData.put("user_id", postReview.getReviewUID());
                reviewData.put("place_id", postReview.getReviewPID());
                reviewData.put("rating", postReview.getReviewRate());
                reviewData.put("review", postReview.getReviewText());
                jsonString = reviewData.toString();
            }catch(JSONException e)
            {

            }

            try{
                URL url = new URL("http://10.0.2.2/review");
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
