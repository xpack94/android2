package com.example.diffogeorges.parkgmtl2905;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener ,NavigationView.OnNavigationItemSelectedListener{

    GoogleMap myGoogleMap;
    private Button liste_parking;
    static TextView temperatureTextView;
    double lat;
    double lng;
    //
    // j'essaye un nouveau truc ici pour la localisation...
    //
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private Circle mCircle;

    double radiusInMeters = 100.0;
    int strokeColor = 0xffff0000; //quelques couleurs par defaut...
    int shadeColor = 0x44ff0000; //Encore une couleur... (On prends du rose)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        getSupportActionBar().setTitle("Parkg Mtl 2905");

        //Initialisation de la Map...
        initMap();
        liste_parking = (Button)findViewById(R.id.button_parking);
        liste_parking.setOnClickListener(this);
        temperatureTextView=(TextView) findViewById(R.id.temperature);
        DownloadTask task=new DownloadTask();
        GPSTracker tracker = new GPSTracker(this);
             lat= tracker.getLatitude();
            lng= tracker.getLongitude();
             Log.e("e", "test " );
            task.execute("http://api.openweathermap.org/data/2.5/weather?lat=45.5088&lon=-73.5879&appid=71d8026993d97f5c32a0c5164ab02b9e");

    }


    @Override
    public void onClick(View MaVue) {
        switch (MaVue.getId()){
            case R.id.button_parking:
                Intent myIntent = new Intent(MapActivity.this, ListViewActivity2.class);
                MapActivity.this.startActivity(myIntent);
                Toast.makeText(getApplicationContext(),"Liste des stationnements disponibles",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void initMap() {
        MapFragment monFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        monFragment.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        //Si l'activité n'est pas utilisée, elle se met en pause...
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myGoogleMap=googleMap;

        //Initialisation de Google Play Services...
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Permission de localisation...
                buildGoogleApiClient();
                myGoogleMap.setMyLocationEnabled(true);
            } else {
                //Requete de Permission de Localisation...
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            myGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void goToLocation(double latitude, double longitude, float zoom) {
        LatLng LL = new LatLng(latitude, longitude);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LL, zoom);
        myGoogleMap.moveCamera(update);
    }


    //Geolocalisation...
    public void geolocate(View view) throws IOException {
        EditText entre_adresse = (EditText) findViewById(R.id.edit_adresse);
        String location = entre_adresse.getText().toString();
        TextView Coordonnees = (TextView) findViewById(R.id.text_coordonnees);

        Geocoder gc = new Geocoder(this);
        List<android.location.Address> list = gc.getFromLocationName(location, 1);
        android.location.Address myAddress = list.get(0);
        String locality = myAddress.getLocality();

        Toast.makeText(this, locality, Toast.LENGTH_SHORT).show();

        double latitude = myAddress.getLatitude();
        double longitude = myAddress.getLongitude();
        goToLocation(latitude, longitude, 18);
        Coordonnees.setText("Votre latitude est "+latitude+" et longitude "+longitude);
        setMonMarqueur(locality, latitude, longitude);


    }

    Marker marqueur;
    private void setMonMarqueur(String locality, double latitude, double longitude) {
        //Car on ne souhaite pas avoir plusieurs marqueurs sur la carte...
        if(marqueur!=null){
            marqueur.remove();
        }

        //Rajouter un marqueur à la destination...
        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(new LatLng(latitude,longitude))
                .snippet("Votre Adresse");

        marqueur = myGoogleMap.addMarker(options);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //On souhaite marquer la position courante...
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Position courante");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mCurrLocationMarker = myGoogleMap.addMarker(markerOptions);

        CircleOptions addCircle = new CircleOptions().center(latLng).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = myGoogleMap.addCircle(addCircle);

        //mouvement de la map
        myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        myGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //Arret de GooglePlayServices...
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Permission de localisation Invalide")
                        .setMessage("L'application a besoin d'une permission. Veillez 'Accepter' pour utiliser les fonctionnalités")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        myGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    Toast.makeText(this, "permission rejetée", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment f = null;
        if(id==R.id.favoris){

        }else if (id==R.id.preferences){


        }else if(id==R.id.deconexion){

        }else if(id==R.id.mesReservations){

        }else{

        }
        return true;
    }
}
