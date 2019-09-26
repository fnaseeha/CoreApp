package com.example.user.lankabellapps.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.fragments.HomeFragment;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.models.Account;
import com.example.user.lankabellapps.models.Customers;
import com.example.user.lankabellapps.models.DownloadItemModel;
import com.example.user.lankabellapps.models.LocationRegisterWithCustomer;
import com.example.user.lankabellapps.popups.AppResetConfirmation;
import com.example.user.lankabellapps.popups.SingleItemListPopup;
import com.example.user.lankabellapps.services.backgroundservices.SingleShotLocationProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewCustomerActivity extends AppCompatActivity {


    FloatingActionButton fab;

    @Bind(R.id.ct_view_user_collapsing_toolbar)
    CollapsingToolbarLayout mCollapseToolbar;

    @Bind(R.id.tv_view_custermer_nicbr)
    TextView mNicBr;

    @Bind(R.id.tv_view_custermer_account)
    TextView mAccount;

    @Bind(R.id.tv_view_custermer_monthrevenu)
    TextView mMonthRevenue;

    @Bind(R.id.tv_view_custermer_os)
    TextView mOs;

    int customerLocationAvailability = 0;
    String cusId;
    double currentLongi, currentLati;
    Customers currentCustomer;
    LocationManager mLocationManager;
    ProgressDialog mProgressGetingLocationDialog;

    LocationRegisterWithCustomer locationRegisterWithCustomer;
    List<LocationRegisterWithCustomer> locationRegisterWithCustomersList;

    ColoredSnackbar coloredSnackbar;
    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        ButterKnife.bind(this);

        cusId = getIntent().getExtras().getString("id");
        if (cusId != null) {
            Customers tempCus = new Customers();
            List<Customers> cusList = new ArrayList<>();

            cusList = tempCus.getFilteredByCompanyCode(cusId);
            currentCustomer = cusList.get(0);
        } else {
            System.out.println("Customer not available");

        }

        locationRegisterWithCustomer = new LocationRegisterWithCustomer();
        locationRegisterWithCustomersList = new ArrayList<>();


        fab = (FloatingActionButton) findViewById(R.id.actionButton);


        init();
    }

    private void init() {

        mCollapseToolbar.setTitle(currentCustomer.getNamem());
        // mCollapseToolbar.setCollapsedTitleTextColor();
        Account account = new Account();
        //mAccount.
        mNicBr.setText(currentCustomer.getCompanyCode());
        mOs.setText(currentCustomer.getOs());
        mAccount.setText(String.valueOf(account.getAccountsByCompanyCode(cusId).size()));

        mProgressGetingLocationDialog = new ProgressDialog(this);
        mProgressGetingLocationDialog.setMessage("Please wait...");
        mProgressGetingLocationDialog.setCancelable(false);
        mProgressGetingLocationDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        coloredSnackbar = new ColoredSnackbar(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        locationRegisterWithCustomersList = locationRegisterWithCustomer.getLocationRegisterDetailsByCusCode(cusId);

        if (locationRegisterWithCustomersList.isEmpty()) {
            fab.setImageResource(R.drawable.location_marker_white);
        } else {
            fab.setImageResource(R.drawable.map_white);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationRegisterWithCustomersList.isEmpty()) {
                    new AppResetConfirmation(ViewCustomerActivity.this).ShowConfirmation(4, "");
                } else {
                    currentLati = locationRegisterWithCustomersList.get(0).getLati();
                    currentLongi = locationRegisterWithCustomersList.get(0).getLongi();

                    openMapActivity(1);
                }

            }
        });
    }

    public void getLocation() {

        //mProgressGetingLocationDialog.show();

        singleLocationRequest(this);

        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        //TODO
                        //System.out.println("999999999999999");
                        mProgressGetingLocationDialog.dismiss();

                    }
                });


            }
        };

        mTimer1.schedule(mTt1, 1000 * 10, 1000 * 10);

//        Intent intent = new Intent(this, MapLocationActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

//    private final LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(final Location location) {
//            //your code here
//
//            mProgressGetingLocationDialog.dismiss();
//
//            Intent intent = new Intent(ViewCustomerActivity.this, ShowCurrentLocationActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
//
//
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

//    private boolean isLocationEnabled() {
//        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//    }

//    public void toggleGPSUpdates(View view) {
//        if(!checkLocation())
//            return;
//        Button button = (Button) view;
//        if(button.getText().equals(getResources().getString(R.string.pause))) {
//            mLocationManager.removeUpdates(locationListenerGPS);
//            button.setText(R.string.resume);
//        }
//        else {
//            mLocationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, 2 * 60 * 1000, 10, locationListenerGPS);
//            button.setText(R.string.pause);
//        }
//    }


    public void singleLocationRequest(Context context) {
//        // when you need location
//        // if inside activity context = this;
//
//        SingleShotLocationProvider.requestSingleUpdate(context, new SingleShotLocationProvider.LocationCallback() {
//            @Override
//            public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
//
//                Log.d("Location", "my location is " + location.toString());
//                currentLongi = location.latitude;
//                currentLati = location.longitude;
//
//                System.out.println("==================" + location.latitude);
//
////                    String address, address1 = "", address2 = "", address3 = "";
////
////                    if (!currentCus.getAddress_line_1().equals("null")) {
////                        address1 = currentCus.getAddress_line_1();
////                    }
////
////                    if (!currentCus.getAddress_line_2().equals("null")) {
////                        address2 = currentCus.getAddress_line_2().trim();
////                    }
////
////
////                    if (!currentCus.getAddress_line_3().equals("null")) {
////                        address3 = currentCus.getAddress_line_3();
////                    }
////
////                    address = address1 + " " + address2 + " " + address3;
//
//
//                mProgressGetingLocationDialog.dismiss();
////
//                openMapActivity();
//                //runnableCode.stop
//            }
//
//            @Override
//            public void notLocationAvailable(String msg) {
//                coloredSnackbar.dismissSnacBar();
//                coloredSnackbar.showSnackBar(msg, coloredSnackbar.TYPE_ERROR, 2000);
//            }
//        });

        openMapActivity(0);
    }

    private void openMapActivity(int i) {

        switch (i){
            case 0:

                Intent intent = new Intent(ViewCustomerActivity.this, LocationShowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", cusId);
                bundle.putInt("status", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                break;

            case 1:

                Intent intent1 = new Intent(ViewCustomerActivity.this, LocationShowActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putDouble("lati", currentLati);
                bundle1.putDouble("long", currentLongi);
                bundle1.putString("id", cusId);
                bundle1.putInt("status", 1);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                break;
        }



    }


}



