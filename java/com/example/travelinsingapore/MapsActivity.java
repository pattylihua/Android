package com.example.travelinsingapore;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,SharedPreferences.OnSharedPreferenceChangeListener{

    private GoogleMap mMap;
    private Marker marker;
    private EditText place;
    private Button search;
    private Button favorite;
    private String name;
    private AddressInfo current;
    SharedPreferences sharedPref;
    private mfDbHelper DbHelper;
    public SQLiteDatabase mfDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        boolean checked = sharedPref.getBoolean(MainActivity.key[1],false);
        changeSomeAttribute(MainActivity.key[1], checked);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DbHelper = new mfDbHelper(this);
        mfDb = DbHelper.getWritableDatabase();

        search = (Button)findViewById(R.id.search);
        favorite = (Button)findViewById(R.id.addtofavorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(MyFavorites.Entry.COL_NAME, current.name );
                cv.put(MyFavorites.Entry.COL_ADRESS, current.address);
                mfDb.insert(MyFavorites.Entry.TABLE_NAME, null ,cv );
                Toast.makeText(MapsActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });
        place = (EditText)findViewById(R.id.searchPlace);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        checked = sharedPref.getBoolean(MainActivity.key[1],false);
        changeSomeAttribute(MainActivity.key[1], checked);
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private void changeSomeAttribute(String key, boolean checked) {
        if(key.equals(MainActivity.key[1])) {
            if(checked) {
                setTheme(R.style.NightTheme);
            } else {
                setTheme(R.style.AppTheme);
            }
        } else if (key.equals(MainActivity.key[0])) {
            if (checked) {
                place.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                search.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                favorite.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
            } else {
                place.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                search.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                favorite.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            }
        }
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
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.KEY);
        message = Typo.getResults(message, SomeAddress.address);
        name = message;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        LatLng sydney = new LatLng(-34, 151);
        marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        try{
            addresses = geocoder.getFromLocationName(message,1);
            double latitude = addresses.get(0).getLatitude();
            double longitude = addresses.get(0).getLongitude();
            getAddress(latitude,longitude);
            LatLng destinyPlace = new LatLng(latitude,longitude);
            marker.setPosition(destinyPlace);
            float zoomLevel = 15;
            mMap.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(destinyPlace));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void search(View view){
        String a = place.getText().toString().trim();
        a = Typo.getResults(a, SomeAddress.address);
        name  = a;
        Toast.makeText(this, a, Toast.LENGTH_LONG).show();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try{
            addresses = geocoder.getFromLocationName(a,1);
            double latitude = addresses.get(0).getLatitude();
            double longitude = addresses.get(0).getLongitude();
            getAddress(latitude,longitude);
            LatLng destinyPlace = new LatLng(latitude,longitude);
            marker.setPosition(destinyPlace);
            float zoomLevel = 15;
            mMap.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(destinyPlace));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    intent = new Intent(MapsActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                case R.id.navigation_plan:
                    intent = new Intent(MapsActivity.this, PlanActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;

                case R.id.navigation_more:
                    intent = new Intent(MapsActivity.this, MoreActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String add = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address currentLocation = addresses.get(0);
            add = currentLocation.getAddressLine(0);
            add = add + " " + currentLocation.getCountryName();
            add = add + "\n" + currentLocation.getCountryCode();
            add = add + " " + currentLocation.getPostalCode();
            add = add + " " + currentLocation.getSubThoroughfare();
            current = new AddressInfo(name, add);
            marker.setTitle(add);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        for(int i=0;i<MainActivity.key.length;i++) {
            boolean checked = sharedPref.getBoolean(MainActivity.key[i],false);
            changeSomeAttribute(MainActivity.key[i], checked);
        }
        recreate();
    }

    static class AddressInfo {
        public AddressInfo(String name, String address) {
            this.name = name;
            this.address = address;
        }

        String name;
        String address;
    }
}
