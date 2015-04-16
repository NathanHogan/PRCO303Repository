package com.example.nathan.prco303app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

    private TextView name;
    private TextView id;
    private Button connect;
    private Button next;
    private Button previous;
    private List<Place> placeList = new ArrayList<Place>();
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (TextView) findViewById(R.id.nameText);
        id = (TextView) findViewById(R.id.idText);
        connect = (Button) findViewById(R.id.connectBtn);
        next = (Button) findViewById(R.id.nextBtn);
        previous = (Button) findViewById(R.id.previousBtn);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void connectHandler(View view)
    {
        String url = "http://10.0.2.2/test";
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected())
        {
            try {
                placeList = new DownloadWebpageTask().execute(url).get();
            }catch(InterruptedException e){

            }catch(ExecutionException e){

            }



        }else{
            name.setText("No network");
        }


    }

    public void nextHandler(View view)
    {
        index++;
        name.setText(placeList.get(index).getName());
        id.setText(placeList.get(index).getId());
    }

    public void previousHandler(View view)
    {
        index--;
        name.setText(placeList.get(index).getName());
        id.setText(placeList.get(index).getId());
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, List<Place>> {

        @Override
        protected List<Place> doInBackground(String... urls) {

                return downloadURL(urls[0]);

        }

        protected void onPostExecute(List<Place> result){
                name.setText(result.get(0).getName());
                id.setText(result.get(0).getId());


        }

        private List<Place> downloadURL(String myurl){
            InputStream is = null;
            List<Place> placeList = new ArrayList<Place>();
            Place newPlace;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                is = conn.getInputStream();
                String jsonString = null;
                JSONArray jsonArrayData = null;
                JSONObject jsonData = null;
                int size = is.available();
                byte[] bytes = new byte[size];
                jsonString = readIt(is);
                is.close();

                jsonArrayData = new JSONArray(jsonString);

                for(int i = 0; i < jsonArrayData.length(); i++){
                    jsonData = jsonArrayData.getJSONObject(i);
                    newPlace = new Place(jsonData.getString("ID"), jsonData.getString("Name"));
                    placeList.add(newPlace);
                }
            } catch( MalformedURLException e){
                e.printStackTrace();
            } catch ( IOException e){
                e.printStackTrace();
            } catch ( JSONException e){
                e.printStackTrace();
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

}
