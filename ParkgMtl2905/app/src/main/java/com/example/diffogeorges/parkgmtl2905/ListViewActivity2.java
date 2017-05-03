package com.example.diffogeorges.parkgmtl2905;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewActivity2 extends AppCompatActivity {

    ListView list;
    public final int n = 100; //juste pour ne pas laisser la liste vide...
    MyAdapter adapter;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        list = (ListView)findViewById(R.id.list_parking);
        adapter = new MyAdapter();
        list.setAdapter(adapter);
    }

    public class MyAdapter extends BaseAdapter{

        LayoutInflater inflater;

        public MyAdapter(){
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return n;
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

//            int latitude = (int) location.getLatitude();
//            int longitude = (int) location.getLongitude();
              int latitude = 70;
              int longitude = 80;

            if(v==null){
                v = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            }
            TextView tv = (TextView)v.findViewById(android.R.id.text1);
            TextView tv2 = (TextView)v.findViewById(android.R.id.text2);

            tv.setText("Stationnement: "+((Integer)position).toString()+"");
            tv2.setText("Vous etes à une latitude de "+((Integer)latitude).toString()+" et longitude de "+((Integer)longitude).toString());
            return v;
        }
    }

}