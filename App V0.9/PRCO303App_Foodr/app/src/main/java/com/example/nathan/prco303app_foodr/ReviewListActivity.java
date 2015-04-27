package com.example.nathan.prco303app_foodr;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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


public class ReviewListActivity extends ActionBarActivity {

    ArrayList<reviewClass> reviewList;
    ListView viewReviews;
    Intent receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        reviewList = new ArrayList<reviewClass>();
        receivedIntent = getIntent();
        String pid = receivedIntent.getStringExtra("pid");
        try {
            reviewList = new getReviewList().execute(pid).get();
        } catch(InterruptedException e)
        {

        }catch(ExecutionException e)
        {

        }

        viewReviews= (ListView)findViewById(R.id.revView);
        ReviewAdapter adapter = new ReviewAdapter(this, reviewList);
        viewReviews.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_review_list, menu);
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

    private class getReviewList extends AsyncTask<String, Void, ArrayList<reviewClass>>
    {

        @Override
        protected ArrayList<reviewClass> doInBackground(String... pid) {
            return getList(pid[0]);
        }

        private ArrayList<reviewClass> getList(String pid)
        {
            ArrayList<reviewClass> reviewList = new ArrayList<reviewClass>();
            String jsonString = "";
            InputStream is= null;

            try{
                URL url = new URL("http://10.0.2.2/review?uid=none&pid=" + pid);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                is = conn.getInputStream();
                jsonString = readIt(is);

                JSONArray reviewArray = new JSONArray(jsonString);
                JSONObject reviewData = new JSONObject();
                reviewClass reviewObject = new reviewClass();

                for(int i = 0; i < reviewArray.length(); i++)
                {
                    reviewData = reviewArray.getJSONObject(i);
                    reviewObject = new reviewClass(reviewData.getString("review"), reviewData.getString("place_id"), reviewData.getString("user_id"), reviewData.getInt("rating"));
                    reviewList.add(reviewObject);
                }
            }catch(IOException e)
            {

            }catch(JSONException e)
            {

            }

            return reviewList;
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
