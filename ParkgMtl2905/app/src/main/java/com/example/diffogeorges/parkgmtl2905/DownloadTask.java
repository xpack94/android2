package com.example.diffogeorges.parkgmtl2905;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by Ilia- on 2017-05-02.
 */

public class DownloadTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        Log.d(TAG, "doInBackground: ENTERED");

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            while (data !=-1){
                char currentData = (char) data;
                result += currentData;
                data = reader.read();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG, "onPostExecute: ENTERED");

        try {
            Log.d(TAG, "onPostExecute: 0" );
            JSONObject jsonObject = new JSONObject(result);
            Log.d(TAG, "onPostExecute: 1"+ result );
            JSONObject weatherData = new JSONObject(jsonObject.getString("main"));
            Log.d(TAG, "onPostExecute: 2" );
            double temperature = Double.parseDouble(weatherData.getString("temp"));
            Log.d(TAG, "onPostExecute: 3" );
            int temperatureInt= (int)(temperature-273.15);
            Log.d(TAG, "onPostExecute: 4" );
            String placeName = jsonObject.getString("name");
            Log.d(TAG, "onPostExecute: 5" );
//
            MapActivity.temperatureTextView.setText(String.valueOf(temperatureInt)+"°");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
