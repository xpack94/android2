package com.example.diffogeorges.parkgmtl2905;

import com.example.diffogeorges.parkgmtl2905.Parking.Feature;
import com.example.diffogeorges.parkgmtl2905.Parking.Racine;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by diffogeorges on 22/04/2017.
 */

public class WebApiParking {
    public String url;

    public WebApiParking(){
        url = "http://donnees.ville.montreal.qc.ca/dataset/8ac6dd33-b0d3-4eab-a334-5a6283eb7940/resource/52cecff0-2644-4258-a2d1-0c4b3b116117/download/signalisation.json";
    }

    public Feature run() throws IOException{
       OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Racine> jsonAdapter = moshi.adapter(Racine.class);
        Racine racine = jsonAdapter.fromJson(json);

        Feature feature;

        for(int i=0; i<racine.features.size();i++){
            if(racine.features.get(i).properties.equals("DESCRIPTION_RPA")){
                feature = racine.features.get(i);
                return feature;
            }
        }
        return null;
    }
}
