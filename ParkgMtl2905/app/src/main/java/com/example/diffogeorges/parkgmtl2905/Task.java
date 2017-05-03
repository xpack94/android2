package com.example.diffogeorges.parkgmtl2905;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;



public class Task extends Activity {

    TextView temperatureTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature);
        temperatureTextView = (TextView) findViewById(R.id.temperature);
        DownloadTask task=new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?lat=45.5088&lon=-73.5879&appid=71d8026993d97f5c32a0c5164ab02b9e");



    }
}
