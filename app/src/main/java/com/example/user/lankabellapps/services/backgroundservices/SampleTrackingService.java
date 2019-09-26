package com.example.user.lankabellapps.services.backgroundservices;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.activities.AttendenceActivity;
import com.example.user.lankabellapps.helper.Alert;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.helper.DateTimeSelectView;
import com.example.user.lankabellapps.helper.GpsUtils;
import com.example.user.lankabellapps.helper.ManualSync;
import com.example.user.lankabellapps.helper.NetworkCheck;
import com.example.user.lankabellapps.helper.TimeFormatter;
import com.example.user.lankabellapps.models.Attendence;
import com.example.user.lankabellapps.models.TimeCap;
import com.example.user.lankabellapps.models.TrackingData;
import com.example.user.lankabellapps.services.apimodels.ResponseModal;
import com.example.user.lankabellapps.services.sync.GetLocationSync;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.user.lankabellapps.activities.AttendenceActivity.unsyncCount;


/**
 * Created by Thejan on 7/30/2017.
 */

public class SampleTrackingService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, GetLocationSync.GetLocationEvents {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    TrackerManager mTracking;
    int cid, lac;
    String mnc, mcc;

    public static final String MSGPARSE = "svrServiceActions";

    private Location currentBestLocation;
    private static long MIN_TIME_BW_UPDATES = 30 * 1000;
    private static float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final int TWO_MINUTES = 1000 * 60 * 1;

    public Alert alert_loc;
    public boolean isRunningSyncTracking = false;

    private static boolean isRunning = false;
    private boolean isGPS = false;
    int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 100;



    public enum Action {
        START,
        STOP,
        GPSERROR,
        GETLOCATION
    }


    public Handler handler = null, handlerTracking = null, handlerStatus = null;
    public static Runnable runnable = null, runnableTracking = null, runnableStatus = null;

    public SampleTrackingService() {
    }

    @Override
    public void onCreate() {

        super.onCreate();

        mTracking = new TrackerManager(this);
        alert_loc = new Alert(this);

        CheckGPS();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();


//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_SECURE_SETTINGS) !=
//                PackageManager.PERMISSION_GRANTED ) {
//
//
//            return;
//        }
//          //   Permission already granted
//            openGPSSettings();

        createLocationRequest();


    }

    private void check_ReadPhoneState_Permission() {
        System.out.println("* check_ReadPhoneState_Permission ");
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

        } else {
            SetDatas();
        }
    }

    private void SetDatas() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        try {
            GsmCellLocation cellLocation = null;

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cellLocation = (GsmCellLocation) (telephonyManager != null ? telephonyManager.getCellLocation() : null);
            System.out.println(cellLocation);

            if (cellLocation != null) {
                cid = cellLocation.getCid();
                lac = cellLocation.getLac();

            }
            //  int dd = telephonyManager.;

            //MCC+MNC
            if (telephonyManager != null) {
                mcc = telephonyManager.getNetworkOperator().substring(0, 3);
                mnc = telephonyManager.getNetworkOperator().substring(3);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void getLocationsSuccess(ResponseModal response) {

        if(response.getData()!= null){
         //   textGeo.setText(response.getData().getLat()+"  "+response.getData().getLon());


            SimpleDateFormat smp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            String locTime = smp.format(response.getData().getTime()); //1556088406


            TrackingData tr = new TrackingData();
            System.out.println("** tr.getTrackingDataByDate(locTime)"+tr.getTrackingDataByDate(locTime));
            System.out.println("* loctime "+locTime);

            if (tr.getTrackingDataByDate(locTime).isEmpty()) {
                System.out.println("--Accuracy--" + 1);
                System.out.println("* loc" + response.getData().getLon());
                System.out.println(locTime);
                System.out.println("** tracking saving");

                TrackingData trackingService = new TrackingData();

                trackingService.setAddedTime(locTime);
                trackingService.setLati(response.getData().getLat());
                trackingService.setLongi(response.getData().getLon());
                trackingService.setAccuracy(String.valueOf(99));
                trackingService.setSpeed("0");
                trackingService.setCollectorID("");
                trackingService.setUseTime("'"+TimeFormatter.changeTimeFormat("MM/dd/yyyy HH:mm:ss", "yyyy/MM/dd HH:mm:ss", locTime) + "'");
                trackingService.setIsScyned("false");

                trackingService.save();

                List<TrackingData> gg1 = new TrackingData().getTrackingDataByDateOnly(DateTimeSelectView.getCurrentTimeOnly());// getTrackingDataByDate
                // List<TrackingData> gg =tr.getUpdatesByisSynced();
                //  System.out.println("* gg "+gg.size());
                System.out.println("* gg1 "+gg1.size());
                int count = gg1.size();

                try {

                    if(count>0 && AttendenceActivity.unsyncCount != null ){

                        AttendenceActivity.unsyncCount.setText("UnSynced GPS count : " + gg1.size());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void getLocationsError(String message) {
        System.out.println("* setalite tracking error "+message);
    }

    private boolean CheckGPS() {

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")) { //if gps is disabled

            System.out.println("* gps is disabled");
//            alert_loc.showLocationAlert("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
//                    "use this app");
            return false;
        }else {
            System.out.println("* gps is enabled");
            return true;
        }
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(10000);
//        int j = getLocationMode(getApplicationContext());
//        System.out.println("* mode "+j);

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        System.out.println("* provider "+provider);
        // int j = getLocationMode(getApplicationContext());

        if(provider.equalsIgnoreCase("gps") ){
            mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        }else if(provider.equalsIgnoreCase("network") ){
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        }else if(provider.contains("network,gps")||provider.contains("gps,network") ){
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        }else{
            mLocationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
        }



    }


    private void openGPSSettings() {
        //Get GPS now state (open or closed)
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER);

        System.out.println("* gpsEnabled "+gpsEnabled);
        if (gpsEnabled) {
            System.out.println("* nnn ");
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.LOCATION_MODE, 0);
        } else {
            System.out.println("* kkk");
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.LOCATION_MODE, 3);
        }
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

                    String AttendenceInTime = attendenceList.get(0).getAttendenceIn(); //MM/dd/yyyy HH:mm:ss
                    long current_time = DateTimeSelectView.getCurrentTimeMilliSecond();
                    long att_in_time = DateTimeSelectView.ConvertToMilliseconds(AttendenceInTime);
                    System.out.println("* current_time "+current_time);
                    System.out.println("* att_in_time "+att_in_time);
                    long diff = current_time - att_in_time;
                   if(diff % 3600000 == 0){//if(diff % 3600000 == 0)

                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                            System.out.println("* inside tower ");
                            check_ReadPhoneState_Permission();
                            GetTowerLocation(mcc,mnc,lac,cid);
                        }
                       // turnGPSOn();
//                        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
//                            @Override
//                            public void gpsStatus(boolean isGPSEnable) {
//                                // turn on GPS
//                                isGPS = isGPSEnable;
//                            }
//                        });
//                        if(!isGPS){
//                            showLocationAlert();
//                            // gps  alert builder
//                        }
                  }

                    Intent trackStart = new Intent("android.intent.action.TRACKER");
                    this.startActivity(trackStart);

                    //startService(new Intent("android.intent.action.TRACKER"));

                }else{
                    Intent trackStop = new Intent("android.intent.action.TRACKER");
                    this.stopService(trackStop);
                   // stopService(new Intent("android.intent.action.TRACKER"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLocationAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getApplicationContext().startActivity(myIntent);
                    }
                })
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });

        dialog.show();
    }
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //Utils.writeToLogFileWithTime("TrackingService -- No permission for location access");
            return;
        }


        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        } else {
            //Utils.writeToLogFileWithTime("Google api client is not connected yet");
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_SECURE_SETTINGS) !=
//                PackageManager.PERMISSION_GRANTED ) {
//            System.out.println("* WRITE_SECURE_SETTINGS permission null");
//        }
        //   Permission already granted
       // openGPSSettings();
        createLocationRequest();

        mGoogleApiClient.connect();

        Attendence attendence = new Attendence();
        List<Attendence> attendenceList = new ArrayList<>();

        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new java.util.Date().getTime(), "dd/MM/yyyy"));


        if (!attendenceList.isEmpty()) {

            if (attendenceList.get(0).getAttendenceOut().equals("")) {

                if (!isRunning) {
                    autoSyncAll();
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

    public int getLocationMode(Context context)
    {
        try {
            return Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    return 0;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Attendence attendence = new Attendence();
        List<Attendence> attendenceList = new ArrayList<>();

        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new java.util.Date().getTime(), "dd/MM/yyyy"));


        if (!attendenceList.isEmpty()) {

            if (attendenceList.get(0).getAttendenceOut().equals("")) {


                String AttendenceInTime = attendenceList.get(0).getAttendenceIn(); //MM/dd/yyyy HH:mm:ss
                long current_time = DateTimeSelectView.getCurrentTimeMilliSecond();
                long att_in_time = DateTimeSelectView.ConvertToMilliseconds(AttendenceInTime);
                System.out.println("* current_time "+current_time);
                System.out.println("* att_in_time "+att_in_time);
                long diff = current_time - att_in_time;
               if(diff % 3600000 == 0){
                    //String mcc,String mnc,int lac,int cid
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                        check_ReadPhoneState_Permission();
                        GetTowerLocation(mcc,mnc,lac,cid);
                    }

//                    new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
//                        @Override
//                        public void gpsStatus(boolean isGPSEnable) {
//                            // turn on GPS
//                            isGPS = isGPSEnable;
//                        }
//                    });
//                    if(!isGPS){
//                        showLocationAlert();
//                        // gps  alert builder
//                    }
                }
                startLocationUpdates();
            }
        }
    }

    private void GetTowerLocation(String mcc,String mnc,int lac,int cid) {
        System.out.println("* GetTowerLocation");
        GetLocationSync ss = new GetLocationSync(SampleTrackingService.this);
        ss.getLocationsSync("1.1","open",mcc,mnc,lac,cid);
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
        System.out.println("get a Location " + location.getAccuracy() + "  " + location.getTime());
        System.out.println("get a Location " + location.getLatitude() + "  " + location.getLongitude());
        if (location.getAccuracy() <= 60) {
            if (isBetterLocation(location, currentBestLocation)) {
                currentBestLocation = location;

                SimpleDateFormat smp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                String locTime = smp.format(location.getTime());


                TrackingData tr = new TrackingData();
                System.out.println("** tr.getTrackingDataByDate(locTime)"+tr.getTrackingDataByDate(locTime));
                System.out.println("* loctime "+locTime);
//                if (tr.getTrackingDataByDate(locTime).isEmpty()) {
//                    System.out.println("--Accuracy--" + location.getAccuracy());
//                    System.out.println(locTime);
//                    Intent intent = new Intent("android.intent.action.TRACK").putExtra(MSGPARSE, Action.GETLOCATION);
//
//                    intent.putExtra("svrAction", Action.GETLOCATION);
//                    intent.putExtra("latitude", location.getLatitude());
//                    intent.putExtra("longitude", location.getLongitude());
//                    intent.putExtra("addedtime", locTime);
//                    intent.putExtra("accuracy", String.valueOf(location.getAccuracy()));
//                    intent.putExtra("speed", String.valueOf(location.getSpeed()));
//                    intent.putExtra("usetime", "'" + TimeFormatter.changeTimeFormat("MM/dd/yyyy HH:mm:ss", "yyyy/MM/dd HH:mm:ss", locTime) + "'");
//
//                    this.sendBroadcast(intent);
//
//                }

                if (tr.getTrackingDataByDate(locTime).isEmpty()) {
                    System.out.println("--Accuracy--" + location.getAccuracy());
                    System.out.println("* loc" + location.getLongitude());
                    System.out.println(locTime);
                    System.out.println("** tracking saving");

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

                    List<TrackingData> gg1 = new TrackingData().getTrackingDataByDateOnly(DateTimeSelectView.getCurrentTimeOnly());// getTrackingDataByDate
                    // List<TrackingData> gg =tr.getUpdatesByisSynced();
                    //  System.out.println("* gg "+gg.size());
                    System.out.println("* gg1 "+gg1.size());
                    int count = gg1.size();

                    try {

                        if(count>0 && AttendenceActivity.unsyncCount != null ){

                            AttendenceActivity.unsyncCount.setText("UnSynced GPS count : " + gg1.size());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }


            // mTracking.saveLocation(location);
        }else{
          //  if (isBetterLocation(location, currentBestLocation)) {
                currentBestLocation = location;

                SimpleDateFormat smp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                String locTime = smp.format(location.getTime());


                TrackingData tr = new TrackingData();
                System.out.println("** tr.getTrackingDataByDate(locTime)"+tr.getTrackingDataByDate(locTime));
                System.out.println("* loctime "+locTime);
//                if (tr.getTrackingDataByDate(locTime).isEmpty()) {
//                    System.out.println("--Accuracy--" + location.getAccuracy());
//                    System.out.println(locTime);
//                    Intent intent = new Intent("android.intent.action.TRACK").putExtra(MSGPARSE, Action.GETLOCATION);
//
//                    intent.putExtra("svrAction", Action.GETLOCATION);
//                    intent.putExtra("latitude", location.getLatitude());
//                    intent.putExtra("longitude", location.getLongitude());
//                    intent.putExtra("addedtime", locTime);
//                    intent.putExtra("accuracy", String.valueOf(location.getAccuracy()));
//                    intent.putExtra("speed", String.valueOf(location.getSpeed()));
//                    intent.putExtra("usetime", "'" + TimeFormatter.changeTimeFormat("MM/dd/yyyy HH:mm:ss", "yyyy/MM/dd HH:mm:ss", locTime) + "'");
//
//                    this.sendBroadcast(intent);
//
//                }

                if (tr.getTrackingDataByDate(locTime).isEmpty()) {
                    System.out.println("--Accuracy--" + location.getAccuracy());
                    System.out.println("* loc" + location.getLongitude());
                    System.out.println(locTime);
                    System.out.println("** tracking saving");

                    TrackingData trackingService = new TrackingData();

                    trackingService.setAddedTime(locTime);
                    trackingService.setLati(location.getLatitude());
                    trackingService.setLongi(location.getLongitude());
                    trackingService.setAccuracy("1"); //not correct location
                    trackingService.setSpeed(String.valueOf(location.getSpeed()));
                    trackingService.setCollectorID("");
                    trackingService.setUseTime("'"+TimeFormatter.changeTimeFormat("MM/dd/yyyy HH:mm:ss", "yyyy/MM/dd HH:mm:ss", locTime) + "'");
                    trackingService.setIsScyned("false");

                    trackingService.save();

                    List<TrackingData> gg1 = new TrackingData().getTrackingDataByDateOnly(DateTimeSelectView.getCurrentTimeOnly());// getTrackingDataByDate
                    // List<TrackingData> gg =tr.getUpdatesByisSynced();
                    //  System.out.println("* gg "+gg.size());
                    System.out.println("* gg1 "+gg1.size());
                    int count = gg1.size();

                    try {

                        if(count>0 && AttendenceActivity.unsyncCount != null ){

                            AttendenceActivity.unsyncCount.setText("UnSynced GPS count : " + gg1.size());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
           // }//
        }
    }


    /*private void turnGPSOn(){
        System.out.println("* turn oN GPS");
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            System.out.println("* gps is disabled");
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }else{
            System.out.println("* ps is enabled ");
        }


    }*/

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
        boolean isSignificantlyLessAccurate = accuracyDelta > 60;

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


    public void autoSyncAll() {
        try {

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

        } catch (Exception e) {
            Log.d("Auto sync all", e.toString());
        }
    }




    public void checkStarus() {
        try {

            handlerStatus = new Handler();
            runnableStatus = new Runnable() {
                public void run() {

                    if(mGoogleApiClient == null){
                        Intent intent = new Intent("com.cureapp.CUSTOM_INTENT");
                        intent.setClass(getBaseContext(),TrackBroadCast.class);
                        sendBroadcast(intent);
                    }

                    handlerStatus.postDelayed(runnableStatus, 1000 * 60 * 5);

                }
            };

            handlerStatus.postDelayed(runnableStatus, 1000 * 60 * 5);

        } catch (Exception e) {
            Log.d("Check Status", e.toString());
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        try {
////            mTimer.cancel();
////            timerTask.cancel();

            TimeCap timeCap = new TimeCap();

            if(timeCap.getTimeCapByName(Constants.ALAM_COUNT).size()>0){
                int x = Integer.parseInt(timeCap.getTimeCapByName(Constants.ALAM_COUNT).get(0).getDate());
                String m = "" + ++x;
                timeCap.updateMasterDataTime(m, Constants.ALAM_COUNT);

                Log.d("Count", m);

                Intent intent = new Intent("");
                intent.setAction("com.cureapp.CUSTOM_INTENT");
                sendBroadcast(intent);
                Log.d("On Task Removed", "Task Removed");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
