package com.ja.sur5ive.services;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import static android.content.Context.LOCATION_SERVICE;

public class LocationService {

//    private static LocationService instance = new LocationService();
//
//    public static LocationService getInstance() {
//        return instance;
//    }
//
//    public Location lastKnownLocation = null;
//
//    private final long LOCATION_REFRESH_TIME = 60000;
//    private final float LOCATION_REFRESH_DISTANCE = 10f;
//
//    private LocationManager mLocationManager;
//
//    private final LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(final Location location) {
//            lastKnownLocation = location;
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    };
//
//    public void initializeLocation() {
//        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//        } else {
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
//                    LOCATION_REFRESH_DISTANCE, mLocationListener);
//        }
//    }
//
//    public LocationService() {
//        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//        } else {
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
//                    LOCATION_REFRESH_DISTANCE, mLocationListener);
//        }
//
//
//    }

}
