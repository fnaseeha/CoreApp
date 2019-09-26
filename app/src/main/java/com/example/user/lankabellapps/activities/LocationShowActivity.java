package com.example.user.lankabellapps.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.helper.TimeFormatter;
import com.example.user.lankabellapps.models.LocationRegisterWithCustomer;
import com.example.user.lankabellapps.popups.AppResetConfirmation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Thejan on 10/15/2016.
 */

/**
 * This Activity has used to display the map.
 * Map will not display untill SHA1 Key not suited with the  map Api key.
 */

public class LocationShowActivity extends FragmentActivity
        implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, com.google.android.gms.location.LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;

    Location location, mCurrentLocation;

    String fromActivity, userID;
    int status;

    Double Lati, Longi, tempLati, tempLongi;

    String isRegistered;

    ColoredSnackbar coloredSnackbar;

    @Bind(R.id.btn_conform_location_onmap)
    Button mConformLocation;

    @Bind(R.id.btn_update_location_onmap)
    Button mUpdateLocation;

    @Bind(R.id.tv_title)
    TextView title;

    @Bind(R.id.tap_text)
    TextView mTapTextView;

    @Bind(R.id.iv_menu)
    ImageView mBack;

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_show);

        ButterKnife.bind(this);
        userID = getIntent().getExtras().getString("id");
        status = getIntent().getExtras().getInt("status");
        fromActivity = getIntent().getExtras().getString("activity");

        if(status == 1) {
            Lati = getIntent().getExtras().getDouble("lati");
            Longi = getIntent().getExtras().getDouble("long");


            tempLati = getIntent().getExtras().getDouble("lati");
            tempLongi = getIntent().getExtras().getDouble("long");
        }

        //isRegistered = getIntent().getExtras().getString("register");
        coloredSnackbar = new ColoredSnackbar(this);

        // mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //drawCircle(latLng,100,mMap);

        setUpMap();

        init();
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        }
    }

    private void init() {
        title.setText("Map");
        mBack.setImageResource(R.drawable.back);
        LocationRegisterWithCustomer locationRegisterWithCustomer = new LocationRegisterWithCustomer();
        List<LocationRegisterWithCustomer> locationRegisterWithCustomerList = new ArrayList<>();

        locationRegisterWithCustomerList = locationRegisterWithCustomer.getLocationRegisterDetailsByCusCode(userID);

        isRegistered = "true";
        if (locationRegisterWithCustomerList.isEmpty()) {
            mConformLocation.setVisibility(View.VISIBLE);
            mUpdateLocation.setVisibility(View.GONE);
        } else if (locationRegisterWithCustomerList.get(0).isSynced.equals("false")) {
            mConformLocation.setVisibility(View.GONE);
            mUpdateLocation.setVisibility(View.VISIBLE);
            mUpdateLocation.setEnabled(false);
            isRegistered = "false";
        } else {
            mConformLocation.setVisibility(View.GONE);
            mUpdateLocation.setVisibility(View.GONE);
        }

    }

    private void setMapLocation() {

        mMap.clear();

        MarkerOptions mp1 = new MarkerOptions();
        //mp1.position(new LatLng(Lati, Longi));

        //LatLng latLng = new LatLng(Lati, Longi);

        mp1.position(new LatLng(Lati, Longi));
        LatLng latLng = new LatLng(Lati, Longi);

        mp1.draggable(true);
        mp1.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.addMarker(mp1);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 25f));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng position = marker.getPosition();

                Toast.makeText(
                        LocationShowActivity.this,
                        "Lat " + position.latitude + " "
                                + "Long " + position.longitude,
                        Toast.LENGTH_LONG).show();
                return true;
            }
        });

        mTapTextView.setText("Long " + Longi + " / " + "Lati " + Lati);

    }

    private void setUpMap() //If the setUpMapIfNeeded(); is needed then...
    {


    }


    private void drawCircle(LatLng point) {

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(100);

        // Border color of the circle
        circleOptions.strokeColor(Color.RED);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

    }


    @Override
    public void onMapClick(LatLng point) {
        // mTapTextView.setText("tapped, point=" + point);
        //mTapTextView.setText(getGeocoderAddress(point).get(0).getAddressLine(1) + getGeocoderAddress(point).get(0).getAddressLine(2));

//        Location locationPrevios = new Location("point Previos");
//
//        locationPrevios.setLatitude(Lati);
//        locationPrevios.setLongitude(Longi);
//
//        Location locationCurrent = new Location("point Current");
//
//        locationCurrent.setLatitude(point.latitude);
//        locationCurrent.setLongitude(point.longitude);
//
//        float distance = locationPrevios.distanceTo(locationCurrent);


//            if (fromActivity.equals(Constants.CUSTOMER_SEARCH_ACTIVITY) || fromActivity.equals(Constants.LOCATION_REGISTER_ACTIVITY)) {
//                mConformLocation.setVisibility(View.VISIBLE);
//            } else {
//                mConformLocation.setVisibility(View.GONE);
//            }

//        LocationRegisterWithCustomer locationRegisterWithCustomer = new LocationRegisterWithCustomer();
//        List<LocationRegisterWithCustomer> locationRegisterWithCustomerList = new ArrayList<>();

//        if(distance <200) {

            if (isRegistered.equals("false")) {
                this.Lati = point.latitude;
                this.Longi = point.longitude;
                mUpdateLocation.setEnabled(true);
                setMapLocation();
            }
//        }else{
//            coloredSnackbar.showSnackBar("You can only select");
//        }
    }

    @Override
    public void onMapLongClick(LatLng point) {
        //mTapTextView.setText("long pressed, point=" + point);

    }

    @OnClick(R.id.iv_menu)
    public void OnClickBack(View view) {
        onBackPressed();
    }


    @OnClick(R.id.btn_conform_location_onmap)
    public void OnClickLocationRegister(View view) {

        if(mCurrentLocation != null) {

            new AppResetConfirmation(this).ShowConfirmation(5, "");
        }else{
            new ColoredSnackbar(this).showSnackBar("Please select a location manually...",new ColoredSnackbar(this).TYPE_WARING, 2000);
        }
    }

    @OnClick(R.id.btn_update_location_onmap)
    public void OnClickLocationUpdate(View view) {
        if(mCurrentLocation != null) {
            new AppResetConfirmation(this).ShowConfirmation(6, "");
        }else{
            new ColoredSnackbar(this).showSnackBar("Location not changed...",new ColoredSnackbar(this).TYPE_WARING, 2000);
        }
    }


    public Location getLcoation() {

//        Location location = null;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(this.LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
        } else {
            //this.canGetLocation = true;
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        5000,
                        10, (LocationListener) this);
                Log.d("activity", "LOC Network Enabled");
                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        Log.d("activity", "LOC by Network");
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            5000,
                            5, (LocationListener) this);
                    Log.d("activity", "RLOC: GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            Log.d("activity", "RLOC: loc by GPS");

                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                        }

                    }
                }
            }
        }
        return location;
    }





    public List<Address> getGeocoderAddress(LatLng location) {
        if (location != null) {

            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

            try {
                /**
                 * Geocoder.getFromLocation - Returns an array of Addresses
                 * that are known to describe the area immediately surrounding the given latitude and longitude.
                 */
                List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);

                return addresses;
            } catch (IOException e) {
                //e.printStackTrace();
                Log.e("Address", "Impossible to connect to Geocoder", e);
            }
        }

        return null;
    }

    public void onDestroyView() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(1);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void locationConformation() {
        Intent intent = new Intent();

        Bundle bundle = new Bundle();

        bundle.putDouble("lati", Lati);
        bundle.putDouble("longi", Longi);

        intent.putExtras(bundle);

        setResult(2, intent);
        finish();
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

    }

    public Polygon drawCircle(LatLng center, int radius, GoogleMap map) {
        // Clear the map to remove the previous circle
        map.clear();
        // Generate the points
        List<LatLng> points = new ArrayList<LatLng>();
        int totalPonts = 30; // number of corners of the pseudo-circle
        for (int i = 0; i < totalPonts; i++) {
            points.add(getPoint(center, radius, i * 2 * Math.PI / totalPonts));
        }
        // Create and return the polygon
        return map.addPolygon(new PolygonOptions().addAll(points).strokeWidth(2).strokeColor(0x700a420b));
    }

    private LatLng getPoint(LatLng center, int radius, double angle) {
        // Get the coordinates of a circle point at the given angle
        double east = radius * Math.cos(angle);
        double north = radius * Math.sin(angle);

        int EARTH_RADIUS = 6371000;

        double cLat = center.latitude;
        double cLng = center.longitude;
        double latRadius = EARTH_RADIUS * Math.cos(cLat / 180 * Math.PI);

        double newLat = cLat + (north / EARTH_RADIUS / Math.PI * 180);
        double newLng = cLng + (east / latRadius / Math.PI * 180);

        return new LatLng(newLat, newLng);
    }


    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//             = mMap.addMarker(new MarkerOptions().position(loc));
//            if(mMap != null){
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
//            }
            mCurrentLocation = location;
            Lati = mCurrentLocation.getLatitude();
            Longi = mCurrentLocation.getLongitude();
        }
    };

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, LocationShowActivity.this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        mCurrentLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,25f));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 25f));

        mTapTextView.setText("Long " + mCurrentLocation.getLatitude() + " / " + "Lati " + mCurrentLocation.getLongitude());

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LocationShowActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
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
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
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
        map.setMyLocationEnabled(true);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(myLocationChangeListener);
        mMap = map;


        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);


//        LatLng latLng = new LatLng(Lati, Longi);

//        mMap.addCircle(new CircleOptions()
//                .center(latLng)
//                .radius(100)
//                .strokeWidth(2)
//                .strokeWidth(Color.RED)
//                .fillColor(Color.TRANSPARENT));
       // drawCircle(latLng);
       // setMapLocation();




//        mGoogleMap=googleMap;
//        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services

        if(status != 1) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                } else {
                    //Request Location Permission
                    checkLocationPermission();
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }else{
            setMapLocation();
        }






        //setMapLocation(map);
    }

    public void updateLocation(int i) {
        switch (i) {
            case 1:

                LocationRegisterWithCustomer locationRegisterWithCustomer = new LocationRegisterWithCustomer();
                locationRegisterWithCustomer.setCusCode(userID);
                locationRegisterWithCustomer.setLati(Lati);
                locationRegisterWithCustomer.setLongi(Longi);
                locationRegisterWithCustomer.setRegisteredDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd-MMM-yyyy"));
                locationRegisterWithCustomer.setIsSynced("false");
                locationRegisterWithCustomer.setIsConformed(Constants.LOCATION_REGISTER_PENDING);
                locationRegisterWithCustomer.save();

                coloredSnackbar.showSnackBar("Location saved...", coloredSnackbar.TYPE_OK, 2000);
                break;

            case 2:

                LocationRegisterWithCustomer locationRegisterWithCustomer1 = new LocationRegisterWithCustomer();

                locationRegisterWithCustomer1.updateLcoation(userID, String.valueOf(Lati), String.valueOf(Longi), TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

                coloredSnackbar.showSnackBar("Location updated...", coloredSnackbar.TYPE_OK, 2000);

                break;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
            }
        }, 1500);
    }
}