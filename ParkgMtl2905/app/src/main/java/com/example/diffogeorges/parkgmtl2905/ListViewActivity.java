package com.example.diffogeorges.parkgmtl2905;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.diffogeorges.parkgmtl2905.Parking.Feature;

import java.io.IOException;

public class ListViewActivity extends AppCompatActivity {

    ListView list;
    MyAdapter adapter;
    Feature feat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        list = (ListView)findViewById(R.id.list_parking);

        RunApi run = new RunApi();
        run.execute();
    }

    public class RunApi extends AsyncTask<String, Object, Feature>{

        @Override
        protected Feature doInBackground(String... params) {
            WebApiParking web = new WebApiParking();

            try{
                feat = web.run();
            }catch(IOException e){}
            return feat;
        }

        @Override
        protected void onPostExecute(Feature feature) {
            super.onPostExecute(feature);
            adapter = new MyAdapter();
            list.setAdapter(adapter);

        }
    }


    public class MyAdapter extends BaseAdapter{

        LayoutInflater inflater;

        public MyAdapter(){
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return feat.properties.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v==null){
                v = inflater.inflate(R.layout.rangee, parent, false);
            }
            TextView tv1 = (TextView)v.findViewById(R.id.descript1);
//            TextView tv2 = (TextView)v.findViewById(R.id.descript2);
//            TextView tv3 = (TextView)v.findViewById(R.id.descript3);
//            TextView tv4 = (TextView)v.findViewById(R.id.descript4);
//            TextView tv5 = (TextView)v.findViewById(R.id.descript5);

            String description1 = feat.properties.get(position).DESCRIPTION_RPA;
//            String description2 = feat.properties.get(position).DESCRIPTION_REP;
//            String description3 = feat.properties.get(position).NOM_ARR;
//            float longitude = feat.properties.get(position).Longitude;
//            float latitude = feat.properties.get(position).Latitude;


            tv1.setText(description1);
//            tv2.setText(description2);
//            tv3.setText(description3);
//            tv4.setText((int) longitude);
//            tv5.setText((int) latitude);
            return v;
        }
    }

}
