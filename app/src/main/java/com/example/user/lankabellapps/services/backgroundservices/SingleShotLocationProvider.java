package com.example.user.lankabellapps.services.backgroundservices;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Thejan on 10/26/2016.
 */

/**
 * Single Short location Provider to get the singel current location
 * For this Class mainly GPS, And Network uses.
 */
public class SingleShotLocationProvider {

    public static interface LocationCallback {
        public void onNewLocationAvailable(GPSCoordinates location);

        public void notLocationAvailable(String msg);
    }

    static LocationManager locationManager;
    static Context con;


    // calls back to calling thread, note this is for low grain: if you want higher precision, swap the
    // contents of the else and if. Also be sure to check gps permission/settings are allowed.
    // call usually takes <10ms
    public static void requestSingleUpdate(final Context context, final LocationCallback callback) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        con = context;
        if (isNetworkEnabled) {
            final Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setVerticalAccuracy(Criteria.ACCURACY_LOW);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(false);

            String providerCoarse = locationManager.getBestProvider(criteria, true);

            //locationManager.getLastKnownLocation(providerCoarse);

            //locationManager.requestLocationUpdates(providerCoarse, 1000, 5, (LocationListener) context);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
//                    if(location.getAccuracy()<100){
//                      //locationManager.requestLocationUpdates(criteria, new LocationListener());
//                    }else {
//                        callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
//                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                    callback.notLocationAvailable("Provider Disabled");
                }
            }, null);
        } else {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setVerticalAccuracy(Criteria.ACCURACY_LOW);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(false);

                //locationManager.getLastKnownLocation(criteria)

                String providerCoarse = locationManager.getBestProvider(criteria, true);

               // locationManager.getLastKnownLocation(providerCoarse);

               // locationManager.requestLocationUpdates(providerCoarse, 1000, 5, (LocationListener) context);


                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
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
                }, null);
            }
        }
    }


    // consider returning Location instead of this dummy wrapper class
    public static class GPSCoordinates {
        public Double longitude;
        public Double latitude;
        // public float Location loc = null;

        public GPSCoordinates(Double theLongitude, Double theLatitude) {
            longitude = theLongitude;
            latitude = theLatitude;
        }

        public GPSCoordinates(double theLongitude, double theLatitude) {
            longitude = theLongitude;
            latitude = theLatitude;
        }
    }

    public static void removeLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates((LocationListener) con);
    }
}
