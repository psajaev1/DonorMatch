package edu.upenn.cis350.donormatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    class RequestCode {
        static final int MAIN_ACTIVITY_NUM = 1;
        static final int USER_AVAILABILITY_ACTIVITY_NUM = 2;
        static final int DATE_ACTIVITY_NUM = 3;
        static final int LOGIN_SCREEN = 4;
        static final int MESSAGE_SCREEN = 5;
    }

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_USER_LOCATION_CODE = 99;
    private DrawerLayout d1;
    private ActionBarDrawerToggle t1;
    private NavigationView nv;

    TextView mTv;
    Button mBtn;
    Calendar c;
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.DonorMatch);
        mapFragment.getMapAsync(this);



        // all code below within onCreate for navigation menu
        d1 = (DrawerLayout)findViewById(R.id.activity_maps);
        t1 = new ActionBarDrawerToggle(this, d1,R.string.Open, R.string.Close);

        d1.addDrawerListener(t1);
        t1.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                Intent i = null;
                switch(item.getItemId())
                {
                    case R.id.user_availability:
                        i = new Intent(nv.getContext(), AvailabilityActivity.class);
                        startActivityForResult(i, RequestCode.USER_AVAILABILITY_ACTIVITY_NUM);
                        break;
                    case R.id.messages:
                        i = new Intent(nv.getContext(), MessageActivity.class);
                        startActivityForResult(i, RequestCode.MESSAGE_SCREEN);
                        break;
                }
                return true;
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t1.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }






    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        String temp_address = "";
        String address = "";
        List<String> list_address = new ArrayList<String>();
        List<String> list_name = new ArrayList<String>();
        List<Integer> address_index = new ArrayList<Integer>();
        List<Integer> zip_index = new ArrayList<Integer>();
        List<Integer> name_index = new ArrayList<Integer>();
        List<Integer> cap_index = new ArrayList<Integer>();
        // code to see if I can make a marker on the map using Mongo
        try {
            System.out.println("Does it get to here");
            String userzip = "19104";
            URL url = new URL("http://10.0.2.2:3000/findDrives?zip="+userzip);
            AccessWebTask task = new AccessWebTask("GET");
            task.execute(url);
            temp_address = task.get();

            for (int index1 = temp_address.indexOf("address"); index1 >= 0; index1 = temp_address.indexOf("address", index1 + 1)){
                address_index.add(index1);
            }

            for (int index2 = temp_address.indexOf("zip"); index2 >= 0; index2 = temp_address.indexOf("zip", index2 + 1)){
                zip_index.add(index2);
            }

            // extracts name and address from json string obejct
            for (int index1 = temp_address.indexOf("name"); index1 >= 0; index1 = temp_address.indexOf("name", index1 + 1)){
                name_index.add(index1);
            }

            for (int index2 = temp_address.indexOf("capacity"); index2 >= 0; index2 = temp_address.indexOf("capacity", index2 + 1)){
                cap_index.add(index2);
            }

            for (int a = 0; a < zip_index.size(); a++){
                int indexaddr = address_index.get(a);
                int indexzip = zip_index.get(a);
                int indexname = name_index.get(a);
                int indexcap = cap_index.get(a);
                String real_address = temp_address.substring((indexaddr + 10), (indexzip - 3));
                String real_name = temp_address.substring((indexname + 7), (indexcap - 3));
                real_address += ", Philadelphia, PA";
                System.out.println("address in main should be below this");
                System.out.println(real_address);
                list_address.add(real_address);
                list_name.add(real_name);
            }

        } catch (Exception e){
            e.toString();
        }

        // getting latitude, longtitude for address, then adds marker to map
        Geocoder coder = new Geocoder(this);
        for (int i = 0; i < list_address.size(); i++) {

            try {

                address = list_address.get(i);
                String bname = list_name.get(i);
                List<Address> addresses = coder.getFromLocationName(address, 1);
                double lat = addresses.get(0).getLatitude();
                double lng = addresses.get(0).getLongitude();

                LatLng bloodloc = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(bloodloc).title(bname));
                System.out.println("getting here means the marker should have been added");


            } catch (IOException e) {
                System.out.println("is it hitting the exception");
                e.printStackTrace();
            }
        }

        System.out.println("this is the end of the on map ready method");
    }

    public boolean checkUserLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_USER_LOCATION_CODE);
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Request_USER_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if (googleApiClient == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission Deniedasdf", Toast.LENGTH_SHORT).show();
                }
                return;

        }
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(LocationServices.API)
                            .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (currentUserLocationMarker != null){
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("User Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if (googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



}
