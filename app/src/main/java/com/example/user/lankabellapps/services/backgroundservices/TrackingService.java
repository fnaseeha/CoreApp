package com.example.user.lankabellapps.services.backgroundservices;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import com.example.user.lankabellapps.helper.ManualSync;
import com.example.user.lankabellapps.helper.NetworkCheck;
import com.example.user.lankabellapps.helper.TimeFormatter;
import com.example.user.lankabellapps.models.Attendence;
import com.example.user.lankabellapps.models.TimeCap;
import com.example.user.lankabellapps.models.TrackingData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TrackingService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    TrackerManager mTracking;

    public static final String MSGPARSE = "svrServiceActions";

    private Location currentBestLocation;
    private static long MIN_TIME_BW_UPDATES = 30 * 1000;
    private static float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final int TWO_MINUTES = 1000 * 60 * 1;

    private static boolean isRunning = false;
    public boolean isRunningSyncTracking = false;

    public enum Action {
        START,
        STOP,
        GPSERROR,
        GETLOCATION
    }


    public Handler handler = null, handlerTracking = null, handlerStatus = null;
    public static Runnable runnable = null, runnableTracking = null, runnableStatus = null;

    public TrackingService() {
    }

    @Override
    public void onCreate() {

        super.onCreate();

        mTracking = new TrackerManager(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        createLocationRequest();

        Attendence attendence = new Attendence();
        List<Attendence> attendenceList = new ArrayList<>();

        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new java.util.Date().getTime(), "dd/MM/yyyy"));

        System.out.println("CCCCCCCCCCCCCCCCCCCCC");
        if (!attendenceList.isEmpty()) {

            if (attendenceList.get(0).getAttendenceOut().equals("")) {

                if (!isRunning) {
                    // gps.start();
                    synTracking();

                    isRunning = true;
                    startLocationUpdates();
                    System.out.println("Check Tracking Service Test 1");
                }

            }
        }


    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(8000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Attendence attendence = new Attendence();
            List<Attendence> attendenceList = new ArrayList<>();

            attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new java.util.Date().getTime(), "dd/MM/yyyy"));


            if (!attendenceList.isEmpty()) {

                if (attendenceList.get(0).getAttendenceOut().equals("")) {

                    startService(new Intent(this, SampleTrackingService.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lovationRequestConnect() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        createLocationRequest();
    }

    protected void startLocationUpdates() {
        System.out.println("VVVVVVVVVVVVVVVVVVVVV");
        try {

            if (mGoogleApiClient.isConnected()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            } else {
                lovationRequestConnect();
                //Utils.writeToLogFileWithTime("Google api client is not connected yet");
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            }

//            if (Build.VERSION.SDK_INT >= 23) {
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //Utils.writeToLogFileWithTime("TrackingService -- No permission for location access");
//                    if (mGoogleApiClient.isConnected()) {
//                        LocationServices.FusedLocationApi.requestLocationUpdates(
//                                mGoogleApiClient, mLocationRequest, this);
//                    } else {
//                        lovationRequestConnect();
//                        //Utils.writeToLogFileWithTime("Google api client is not connected yet");
//                        LocationServices.FusedLocationApi.requestLocationUpdates(
//                                mGoogleApiClient, mLocationRequest, this);
//                    }
//                }
//            } else {
//
//                if (mGoogleApiClient.isConnected()) {
//                    LocationServices.FusedLocationApi.requestLocationUpdates(
//                            mGoogleApiClient, mLocationRequest, this);
//                } else {
//                    lovationRequestConnect();
//                    //Utils.writeToLogFileWithTime("Google api client is not connected yet");
//                    LocationServices.FusedLocationApi.requestLocationUpdates(
//                            mGoogleApiClient, mLocationRequest, this);
//                }
//            }
        }catch (Exception e){
            System.out.println("-----------------");
            e.printStackTrace();
        }
    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("On Start command...");

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        createLocationRequest();

        Attendence attendence = new Attendence();
        List<Attendence> attendenceList = new ArrayList<>();

        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new java.util.Date().getTime(), "dd/MM/yyyy"));

        System.out.println("DDDDDDDDDDDDD");
        if (!attendenceList.isEmpty()) {

            if (attendenceList.get(0).getAttendenceOut().equals("")) {

                if (!isRunning) {
                    // gps.start();
                    synTracking();

                    isRunning = true;

                    System.out.println("Check Tracking Service Test 1");
                }

            }
        }

        return START_STICKY;//super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Attendence attendence = new Attendence();
        List<Attendence> attendenceList = new ArrayList<>();

        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new java.util.Date().getTime(), "dd/MM/yyyy"));


        if (!attendenceList.isEmpty()) {

            if (attendenceList.get(0).getAttendenceOut().equals("")) {

                Intent intent = new Intent("com.cureapp.CUSTOM_INTENT");
                intent.setClass(getBaseContext(),TrackBroadCast.class);
                sendBroadcast(intent);

                synTracking();
                startLocationUpdates();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        //Log.d("get a Location ", String.valueOf(location.getAccuracy()));
        Intent intent = new Intent("m.getLoc");
        sendBroadcast(intent);
       // System.out.println("get a Location " + location.getAccuracy() + "  " + location.getTime());
        System.out.println("* get a Location " + location.getLatitude() + "  " + location.getLongitude());
        if (location.getAccuracy() <= 60) {
            if (isBetterLocation(location, currentBestLocation)) {
                currentBestLocation = location;

                SimpleDateFormat smp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                String locTime = smp.format(location.getTime());

                TrackingData tr = new TrackingData();
                if (tr.getTrackingDataByDate(locTime).isEmpty()) {
                    System.out.println("--Accuracy--" + location.getAccuracy());
                    System.out.println(locTime);

                    TrackingData trackingService = new TrackingData();

                    trackingService.setAddedTime(locTime);
                    trackingService.setLati(location.getLatitude());
                    trackingService.setLongi(location.getLongitude());
                    trackingService.setAccuracy(String.valueOf(location.getAccuracy()));
                    trackingService.setSpeed(String.valueOf(location.getSpeed()));
                    trackingService.setCollectorID("");
                    trackingService.setUseTime("'"+TimeFormatter.changeTimeFormat("MM/dd/yyyy HH:mm:ss", "yyyy/MM/dd HH:mm:ss", locTime) + "'");
                    trackingService.setIsScyned("false");

                    trackingService.save();

                }
            }


            // mTracking.saveLocation(location);
        }
    }


    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        if (location.distanceTo(currentBestLocation) < MIN_DISTANCE_CHANGE_FOR_UPDATES) {
            return false;
        }

        if ((location.getTime() - currentBestLocation.getTime()) < MIN_TIME_BW_UPDATES) {
            return false;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 30;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public void synTracking() {

        final long synctime = 1000 * 10;
        //final long synctime =1000 * 5;
        handlerTracking = new Handler();
        runnableTracking = new Runnable() {


            @Override
            public void run() {
                isRunningSyncTracking = true;
                handlerTracking.postDelayed(runnableTracking, synctime);
//                    wakeUpScreen();
                    System.out.println("======Waked Up=====");


                try {
                    Log.d("Sync Tracking", "Tracking syncing");

                        if(new NetworkCheck().NetworkConnectionCheck(getBaseContext())) {
                            ManualSync autoSync = new ManualSync();
                            autoSync.syncAll();
                        }


                    Attendence attendence = new Attendence();
                    List<Attendence> attendenceList = new ArrayList<>();

                    attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new java.util.Date().getTime(), "dd/MM/yyyy"));


                    if (!attendenceList.isEmpty()) {

                        if (attendenceList.get(0).getAttendenceOut().equals("")) {


                            if (mLocationRequest == null) {
                                Intent intent = new Intent("com.cureapp.CUSTOM_INTENT");
                                intent.setClass(getBaseContext(),TrackBroadCast.class);
                                sendBroadcast(intent);
                            }
//                            }
                        } else {
                            stopLocationUpdates();
                            onDestroy();

                            handler.removeCallbacks(runnableTracking);
                        }
                    } else {
                        //gps.stop();
                        stopLocationUpdates();
                        onDestroy();
                    }


                } catch (Exception e) {
                    Log.d("Sync Tracking", e.toString());
                }
            }


        };
        handlerTracking.postDelayed(runnableTracking, synctime);
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        try {


            Intent intent = new Intent("com.cureapp.CUSTOM_INTENT");
            intent.setClass(getBaseContext(),TrackBroadCast.class);
            sendBroadcast(intent);
            Log.d("On Task Removed", "Task Removed");


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
