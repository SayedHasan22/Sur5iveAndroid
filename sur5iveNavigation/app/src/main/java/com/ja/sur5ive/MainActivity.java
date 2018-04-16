package com.ja.sur5ive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ja.sur5ive.activities.EditUserActivity;
import com.ja.sur5ive.activities.PinActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    public Location lastKnownLocation = null;

    public static final int REQUEST_LOCATION_PERMISSION = 99;

    private final long LOCATION_REFRESH_TIME = 10000;
    private final float LOCATION_REFRESH_DISTANCE = 1f;

    private LocationManager mLocationManager;

    private Activity activity = this;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            boolean firstLocation = lastKnownLocation == null && location != null;
            lastKnownLocation = location;
            if(location == null) {
                Log.i("Location","Location not found");
            } else {
                Log.i("Location","Location updated: "+location.toString());
            }
            if(firstLocation) {
                Toast.makeText(activity, "Location detected.", Toast.LENGTH_LONG);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get fragment manager
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft;

            switch (item.getItemId()) {
                case R.id.navigation_message:
                    // replace
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_view, new MessageFragment());
                    ft.commit();
//                    mTextMessage.setText(R.string.title_message);
                    return true;
                case R.id.navigation_contacts:
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_view, new ContactsFragment());
                    ft.commit();
//                    mTextMessage.setText(R.string.title_contacts);
                    return true;
//                case R.id.navigation_emergency:
//                    ft = fm.beginTransaction();
//                    ft.replace(R.id.content_view, new EmergencyFragment());
//                    ft.commit();
//                    mTextMessage.setText(R.string.title_emergency);
//                    return true;
                case R.id.navigation_user:
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_view, new UserFragment());
                    ft.commit();
//                    mTextMessage.setText(R.string.title_user);
                    return true;
                case R.id.navigation_history:
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_view, new HistoryFragment());
                    ft.commit();
//                    mTextMessage.setText(R.string.title_history);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupLocation();

        mTextMessage = findViewById(R.id.message);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        if(!sharedPref.getBoolean(getString(R.string.preference_usersetup),false)) {
            Intent intent = new Intent(this, EditUserActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        }
        else if(!sharedPref.getBoolean(getString(R.string.preference_pinsetup), false)) {
            Intent intent = new Intent(this, PinActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            this.finish();
        } else {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft;
            ft = fm.beginTransaction();
            ft.replace(R.id.content_view, new MessageFragment());
            ft.commit();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public void setupLocation() {
        //Initializing location

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                            LOCATION_REFRESH_DISTANCE, mLocationListener);
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME,
                            LOCATION_REFRESH_DISTANCE, mLocationListener);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
