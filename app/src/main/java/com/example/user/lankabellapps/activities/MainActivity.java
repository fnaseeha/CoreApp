package com.example.user.lankabellapps.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.fragments.LeaveMainFragment;
import com.example.user.lankabellapps.fragments.CustomerMainFragment;
import com.example.user.lankabellapps.fragments.HomeFragment;
import com.example.user.lankabellapps.fragments.MerchantsListFragment;
import com.example.user.lankabellapps.helper.Alert;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.helper.Featch;
import com.example.user.lankabellapps.helper.NetworkCheck;
import com.example.user.lankabellapps.helper.TimeFormatter;
import com.example.user.lankabellapps.helper.Utils;
import com.example.user.lankabellapps.models.Account;
import com.example.user.lankabellapps.models.Attendence;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.models.AvailableApps;
import com.example.user.lankabellapps.models.BankList;
import com.example.user.lankabellapps.models.CusVisit;
import com.example.user.lankabellapps.models.Customers;
import com.example.user.lankabellapps.models.LocationRegisterWithCustomer;
import com.example.user.lankabellapps.models.MainMenuObject;
import com.example.user.lankabellapps.models.Merchants;
import com.example.user.lankabellapps.models.MerchantsCities;
import com.example.user.lankabellapps.models.TSRDetails;
import com.example.user.lankabellapps.models.TimeCap;
import com.example.user.lankabellapps.models.UnitHand;
import com.example.user.lankabellapps.models.UserProfile;
import com.example.user.lankabellapps.popups.AppResetConfirmation;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.backgroundservices.GetHandInUnit;
import com.example.user.lankabellapps.services.backgroundservices.SampleTrackingService;
import com.example.user.lankabellapps.services.sync.AttendenctSync;
import com.example.user.lankabellapps.services.sync.CheckMyAttendneceSync;
import com.example.user.lankabellapps.services.sync.GetAccountsListSync;
import com.example.user.lankabellapps.services.sync.GetCustomerListSync;
import com.example.user.lankabellapps.services.sync.GetLocationRegistrationsSync;
import com.example.user.lankabellapps.services.sync.GetMerchantsCitiesSync;
import com.example.user.lankabellapps.services.sync.GetMerchantsSync;
import com.example.user.lankabellapps.services.sync.GetRequireAppSync;
import com.example.user.lankabellapps.services.sync.GetTSRDeailsSync;
import com.example.user.lankabellapps.services.sync.LocationRegisterSync;
import com.example.user.lankabellapps.services.sync.LoginSync;
import com.example.user.lankabellapps.services.sync.MerchantSync;
import com.example.user.lankabellapps.services.sync.MerchantUpdateSync;
import com.example.user.lankabellapps.services.sync.UpdatePhoneDetailSync;
import com.example.user.lankabellapps.services.sync.VisitLogSync;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements CheckMyAttendneceSync.GetAttendenceEvents, Featch.featchDone,
        GetCustomerListSync.GetCustomerListEvents, GetAccountsListSync.GetAccountsListEvents, GetMerchantsCitiesSync.GetCitiesEvents,
        GetTSRDeailsSync.GetTSRDetailsEvents, GetMerchantsSync.GetMerchantsList, MerchantUpdateSync.updateMerchantsList, MerchantSync.MerchantSyncEvents,
        LocationRegisterSync.LocationRegisterEvents, GetLocationRegistrationsSync.GetLocationRegistrationFeatchEvents, VisitLogSync.VisitSyncEvents,
        AttendenctSync.GetAttendenceEvents, GetHandInUnit.GetHandEvents,GetRequireAppSync.GetRequireEvents, LoginSync.LoginSyncEvetns, UpdatePhoneDetailSync.UpdatePhoneDataEvents {


    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    TextView mNaveHeaderName;

    String currentVisibleFragment = Constants.HOME_FRAGMETN;

    ProgressDialog mProgressDialog, mPleaseWaitDialog, mProgressUpdateDialog, mCommonProgressDialog;

    ColoredSnackbar coloredSnackbar;

    FragmentTransaction transaction;

    JSONArray cusObject = null, accObject = null, cityObject = null, merchantsObject = null, locationRegObj = null,unitHandObj = null,requireAppObj;

    protected PowerManager.WakeLock mWakeLock;

    int currentId, successCount = 0, checkUpdateesCallCount = 0;

    DrawerLayout drawer;

    NavigationView navigationView;

    String currentAppName = "Core App";

    int clickedItem = 0;
    ActionBarDrawerToggle drawerToggle;

    DownloadTask downloadTask;
    ProgressDialog dialog;
    Alert alert;
//    @Bind(R.id.tv_nav_bar_header)
//    TextView mNavHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        alert = new Alert(MainActivity.this);

//        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.ACTION_INSTALL_PACKAGE);
//        List<ResolveInfo> pkgAppsList = this.getPackageManager().queryIntentActivities( mainIntent, 0);
//
//        for (int i = 0; i<pkgAppsList.size(); i++){
//            System.out.println(pkgAppsList.get(i));
//        }
        coloredSnackbar = new ColoredSnackbar(this);
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.main_menu_bottom_nav);

        fragment = new HomeFragment();
        currentId = R.id.home;
        transaction.replace(R.id.main_container, fragment).commit();

       //checkLogin();


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                hideSoftKeyboard();
                int id = item.getItemId();
                transaction = fragmentManager.beginTransaction();
                switch (id) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        transaction.setCustomAnimations(R.anim.left_in, R.anim.slide_to_right);

                        currentVisibleFragment = Constants.HOME_FRAGMETN;

                        break;
                    case R.id.nsvigstion:
                        fragment = new CustomerMainFragment();
                        if (currentId == R.id.home) {
                            transaction.setCustomAnimations(R.anim.slide_to_left, R.anim.left_out);
                        } else {
                            transaction.setCustomAnimations(R.anim.left_in, R.anim.slide_to_right);
                        }
                        //transaction.setCustomAnimations(R.anim.slide_to_left, R.anim.left_out);

                        currentVisibleFragment = Constants.CUSTOMER_FRAGMENT;

                        break;

                    case R.id.merchants:
                        fragment = new MerchantsListFragment();
                        transaction.setCustomAnimations(R.anim.slide_to_left, R.anim.left_out);
                        //transaction.setCustomAnimations(R.anim.slide_to_left, R.anim.left_out);
                        currentVisibleFragment = Constants.MERCHANTS_FRAGMETN;
                        break;
                }


                if (id != currentId) {
                    transaction.replace(R.id.main_container, fragment).commit();
                    currentId = id;
                    return true;
                }

                return false;


            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        Menu nav_Menu = navigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                final int id = item.getItemId();
                clickedItem = id;
                Animation leftMove = AnimationUtils.loadAnimation(MainActivity.this, R.anim.left_out);
                navigationView.setAnimation(leftMove);
                drawer.closeDrawer(GravityCompat.START, true);

                return true;
            }
        });

        View header = navigationView.getHeaderView(0);

        mNaveHeaderName = (TextView) header.findViewById(R.id.tv_nav_bar_header);


        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                switch (clickedItem) {

                    case R.id.nav_logout:
                        //intentNav = new Intent(HomeActivity.this, TrackingActivity.class);
                        //selectedPopup = Constants.type_cdma;

                        //new AppResetConfirmation(MainActivity.this).ShowConfirmation(2, "");
                        new AppResetConfirmation(MainActivity.this).ShowConfirmation(1, "Options");
                        break;

                    case R.id.updates:
                        new CheckInternet().execute("");

                        break;

                    case R.id.nav_my_profile:

                        Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                        break;

                        case R.id.leave:

                        Intent intent_l = new Intent(MainActivity.this, LeaveMainFragment.class);
                        startActivity(intent_l);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                        break;
                    case R.id.unit_in_hand:

                    Intent intent_unit = new Intent(MainActivity.this, UnitInHandActivity.class);
                    startActivity(intent_unit);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                    break;
                    case R.id.nav_help:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://119.235.1.59:8080/how-to/"));
                        startActivity(browserIntent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                    break;

                    case R.id.nav_4g_network:

                        Intent intent1 = new Intent(MainActivity.this, WebViewActivity.class);
                        Bundle b = new Bundle();
                        b.putString("url", "https://www.google.com/maps/d/embed?mid=112VwNeFabYJOOj_R-1Ya1A0wpWQ&ll=6.9920020894932655%2C80.02616705749517&z=10");//https://www.google.com/maps/d/embed?mid=112VwNeFabYJOOj_R-1Ya1A0wpWQ&ll=6.9920020894932655%2C80.02616705749517&z=10
                        //http://www.lankabell.com/4g_coverage_map.html
                        intent1.putExtras(b);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                        break;

                    case R.id.nav_branch_network:

                        Intent intent2 = new Intent(MainActivity.this, WebViewActivity.class);
                        Bundle b1 = new Bundle();
                        b1.putString("url", "https://www.google.com/maps/d/embed?mid=112VwNeFabYJOOj_R-1Ya1A0wpWQ&ll=6.9838235495652325%2C80.08521857116705&z=10"); //http://www.lankabell.com/branch_network.html
                        intent2.putExtras(b1);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                        break;

                    case R.id.nav_car_read:

//                        Intent intent3 = new Intent(MainActivity.this, ReadRFIDActivity.class);
//                        startActivity(intent3);
//                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                        break;


                    case R.id.nav_update_core:

                        try {
                            AvailableApps availableApps = new AvailableApps();

                            PackageManager manager = getPackageManager();
                            PackageInfo info = null;

                            try {
                            //    System.out.println("* availableApps.getFromAppId(\"1\").get(0).getPackagename() "+availableApps.getFromAppId("1").get(0).getPackagename());
                                info = manager.getPackageInfo(availableApps.getFromAppId("1").get(0).getPackagename(), 0);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String version = null;
                            if (info != null) {
                                version = info.versionName;
                            }else{
                                snakBarCommon("Please Refresh and try again ", new ColoredSnackbar(MainActivity.this).TYPE_ERROR, 2000);
                            }

                            float dbVersion = Float.parseFloat(availableApps.getFromAppId("1").get(0).getVersion());
                            String newversion = "1.0";

                            if(version != null){
                                if( version.length()==5){
                                    newversion = version.substring(0,3)+version.substring(4);
                                }else if(version.length()==7){
                                    newversion = version.substring(0,3)+version.substring(4,5)+version.substring(6,7);
                                }else{
                                    newversion = version;
                                }
                            }

                            float NewVersion1 = Float.parseFloat(newversion);
                            System.out.println("* NewVersion1 "+NewVersion1);
                            System.out.println("* dbVersion "+dbVersion);
//1.63 // 1.6
                            if (dbVersion == NewVersion1) {
                                snakBarCommon("No Updates Available", new ColoredSnackbar(MainActivity.this).TYPE_WARING, 2000);
                            } else {
                                new AppResetConfirmation(MainActivity.this).ShowConfirmation(9, "");
                            }


                            //if (availableApps.getFromAppId("1").get(0).getVersion().equals(version)) {

                        } catch (Exception e) {
                            e.printStackTrace();
                           // System.out.println(e);
                        }

                        //if()
                        break;
                }

                clickedItem = 0;
            }

            @Override
            public void onDrawerStateChanged(int newState) {


            }
        });


        init();

        //addDumyData();

    }

    private void checkLogin() {
        if(new NetworkCheck().NetworkConnectionCheck(this)){
            UserProfile userProfile = new UserProfile();
            List<UserProfile> userProfileList = new ArrayList<>();
            userProfileList = userProfile.getAllUsers();
            LoginSync loginSync = new LoginSync(this);
            String simNo = userProfileList.get(0).getSimNo();

            loginSync.login(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), userProfileList.get(0).getSimNo());
        }
    }

    public void doUpdateCore() {
        //snakBarCommon("Update Available", new ColoredSnackbar(MainActivity.this).TYPE_OK, 2000);

        mProgressUpdateDialog.setMessage("Updating Core App");
        mProgressUpdateDialog.show();
        try {
            AvailableApps a = new AvailableApps();
            List<AvailableApps> availableAppsList = new ArrayList<>();
            availableAppsList = a.getFromAppId("1");
            System.out.println("* availableAppsList "+availableAppsList.size());
            downloadTask = new DownloadTask(MainActivity.this);
            downloadTask.execute("http://" + availableAppsList.get(0).getUrl().trim());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            //Toast.makeText(this, "Check the internet connection...", Toast.LENGTH_LONG).show();
            snakBarCommon("Check the internet connection...", new ColoredSnackbar(MainActivity.this).TYPE_ERROR, 2000);
        }
    }


    private void callFetach() {
        Featch featch = new Featch(this);
        featch.featchAppDetails();
    }
    private void dialogDisrmiss(){
        if ((dialog != null) && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }

    private void addDumyData() {

        new Customers().clearTable();

        Customers customers1 = new Customers();
        customers1.setAccountNo("12345");
        customers1.setNamem("HNB");
        customers1.setAddress("Dehiwala");
        customers1.setOs("10500");
        customers1.setDueDate("06/07/2016");
        customers1.setLastPayment("3000");
        customers1.setMode("Cash");
        customers1.setRecivedBy("12312");
        customers1.setStatus("PD");
        customers1.setContactNo("0711122334");
        customers1.setRegister("false");
        customers1.setLati(0.0);
        customers1.setLongi(0.0);
        customers1.setCompanyCode("12");
        customers1.setProvince("WESTERN");
        customers1.setDistrict("COLOMBO");
        customers1.setCity("Dehiwala");


        customers1.save();

        Customers customers2 = new Customers();
        customers2.setAccountNo("23123");
        customers2.setNamem("HNB");
        customers2.setAddress("Rathmalana");
        customers2.setOs("10400");
        customers2.setDueDate("08/07/2016");
        customers2.setLastPayment("1500");
        customers2.setMode("Cheque");
        customers2.setRecivedBy("1212");
        customers2.setStatus("TD");
        customers2.setContactNo("0774124517");
        customers2.setRegister("false");
        customers2.setLati(0.0);
        customers2.setLongi(0.0);
        customers2.setCompanyCode("123");
        customers2.setProvince("WESTERN");
        customers2.setDistrict("COLOMBO");
        customers2.setCity("Rathmalana");

        customers2.save();

        Customers customers3 = new Customers();
        customers3.setAccountNo("123212");
        customers3.setNamem("NDB");
        customers3.setAddress("23 Maradana");
        customers3.setOs("10200");
        customers3.setDueDate("06/07/2016");
        customers3.setLastPayment("1000");
        customers3.setMode("Cash");
        customers3.setRecivedBy("12332");
        customers3.setStatus("OGB");
        customers3.setContactNo("0714544789");
        customers3.setRegister("true");
        customers3.setLati(0.0);
        customers3.setLongi(0.0);
        customers3.setCompanyCode("1234");
        customers3.setProvince("WESTERN");
        customers3.setDistrict("COLOMBO");
        customers3.setCity("Maradana");

        customers3.save();

    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void init() {
        UserProfile userProfile = new UserProfile();
        List<UserProfile> userProfilesList = new ArrayList<>();

        userProfilesList = userProfile.getAllUsers();

        mPleaseWaitDialog = new ProgressDialog(this);
        // Set your ProgressBar Message
        mPleaseWaitDialog.setMessage("Please Wait!");
        mPleaseWaitDialog.setIndeterminate(false);
        mPleaseWaitDialog.setMax(100);
        mPleaseWaitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mPleaseWaitDialog.setCancelable(false);

        mProgressUpdateDialog = new ProgressDialog(this);
        // Set your ProgressBar Message
        mProgressUpdateDialog.setMessage("Please Wait!");
        mProgressUpdateDialog.setIndeterminate(false);
        mProgressUpdateDialog.setMax(100);
        mProgressUpdateDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressUpdateDialog.setCancelable(false);

        mProgressDialog = new ProgressDialog(this);
        // Set your ProgressBar Title
        mProgressDialog.setTitle("Checking for New Updates.");
        mProgressDialog.setIcon(R.drawable.refresh);
        // Set your ProgressBar Message
        mProgressDialog.setMessage("Please Wait!");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(1000);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Show ProgressBar
        mProgressDialog.setCancelable(false);
        //  mProgressDialog.setCanceledOnTouchOutside(false);
        //mProgressDialog.show();

        //checkAttendence();
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

        Attendence attendence = new Attendence();
        List<Attendence> attendenceList = new ArrayList<>();

        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new java.util.Date().getTime(), "dd/MM/yyyy"));


        if (!attendenceList.isEmpty()) {

                CheckGPS();

                if (attendenceList.get(0).getAttendenceOut().equals("")) {
                    Intent svrintent = new Intent(this, SampleTrackingService.class);
                    getBaseContext().startService(svrintent);
                }
        }

        addBankList();

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask = new DownloadTask(MainActivity.this);
                downloadTask.cancel(true);
            }
        });
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

    private boolean CheckGPS() {

        try {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            System.out.println("* provider " + provider);

//        int j = getLocationMode(getApplicationContext());

            if(provider.equalsIgnoreCase("gps") ){
                alert.showLocationAlert("Your Location Mode is not High priority.\nPlease Set Location to " +
                        "High Priority");
                return false;
            }else if(provider.equalsIgnoreCase("network")){
                alert.showLocationAlert("Your Location Mode is not High priority.\nPlease Set Location to " +
                        "High Priority");
                return false;
            }else if(provider.contains("network,gps")||provider.contains("gps,network")){
                System.out.println("* gps is enable");
                return true;
            }else{
                alert.showLocationAlert("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app");
                return false;
            }
            }catch (Exception e){
                e.printStackTrace();
            }
        return false;
        }

    private void checkAttendence() {

        UserProfile userProfile = new UserProfile();
        List<UserProfile> userProfilesList = new ArrayList<>();

        userProfilesList = userProfile.getAllUsers();

        Attendence attendence = new Attendence();

        List<Attendence> attendenceList = new ArrayList<>();

        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
        if(attendenceList.isEmpty()) {
            CheckMyAttendneceSync checkMyAttendneceSync = new CheckMyAttendneceSync(this);
            checkMyAttendneceSync.GetAttendenceData(userProfilesList.get(0).getUserName(), userProfilesList.get(0).getPassword());
        }
        else if(!attendenceList.isEmpty()&& Constants.ATTENDANCE_STATUS_OUT.equals("NO")){
            new AppResetConfirmation(MainActivity.this).ShowConfirmation(10, "Options");
        }
        else if(!attendenceList.isEmpty()&& Constants.ATTENDANCE_STATUS_IN.equals("NO")){
            new AppResetConfirmation(MainActivity.this).ShowConfirmation(10, "Options");
        }
    }

    private void addBankList() {

        new BankList().clearTable();

        BankList bankList = new BankList();
        bankList.setCode("7852");
        bankList.setName("Alliance Finance Company PLC");
        bankList.save();


        BankList bankList1 = new BankList();
        bankList1.setCode("7463");
        bankList1.setName("Amana Bank PLC");
        bankList1.save();

        BankList bankList2 = new BankList();
        bankList2.setCode("7472");
        bankList2.setName("Axis Bank");
        bankList2.save();

        BankList bankList3 = new BankList();
        bankList3.setCode("7010");
        bankList3.setName("Bank of Ceylon");
        bankList3.save();

        BankList bankList4 = new BankList();
        bankList4.setCode("7481");
        bankList4.setName("Cargills Bank Limited");
        bankList4.save();

        BankList bankList5 = new BankList();
        bankList5.setCode("8004");
        bankList5.setName("Central Bank of Sri Lanka");
        bankList5.save();

        BankList bankList6 = new BankList();
        bankList6.setCode("7825");
        bankList6.setName("Central Finance PLC");
        bankList6.save();

        BankList bankList7 = new BankList();
        bankList7.setCode("7047");
        bankList7.setName("Citi Bank");
        bankList7.save();

        BankList bankList8 = new BankList();
        bankList8.setCode("7746");
        bankList8.setName("Citizen Development Business Finance PLC");
        bankList8.save();

        BankList bankList9 = new BankList();
        bankList9.setCode("7056");
        bankList9.setName("Commercial Bank PLC");
        bankList9.save();

        BankList bankList10 = new BankList();
        bankList10.setCode("7870");
        bankList10.setName("Commercial Credit & Finance PLC");
        bankList10.save();

        BankList bankList11 = new BankList();
        bankList11.setCode("7807");
        bankList11.setName("Commercial Leasing and Finance");
        bankList11.save();

        BankList bankList12 = new BankList();
        bankList12.setCode("7205");
        bankList12.setName("Deutsche Bank");
        bankList12.save();

        BankList bankList13 = new BankList();
        bankList13.setCode("7454");
        bankList13.setName("DFCC Bank PLC");
        bankList13.save();

        BankList bankList14 = new BankList();
        bankList14.setCode("7074");
        bankList14.setName("Habib Bank Ltd");
        bankList14.save();

        BankList bankList15 = new BankList();
        bankList15.setCode("7083");
        bankList15.setName("Hatton National Bank PLC");
        bankList15.save();

        BankList bankList16 = new BankList();
        bankList16.setCode("7737");
        bankList16.setName("HDFC Bank");
        bankList16.save();

        BankList bankList17 = new BankList();
        bankList17.setCode("7092");
        bankList17.setName("Hongkong Shanghai Bank");
        bankList17.save();

        BankList bankList18 = new BankList();
        bankList18.setCode("7384");
        bankList18.setName("ICICI Bank Ltd");
        bankList18.save();

        BankList bankList19 = new BankList();
        bankList19.setCode("7108");
        bankList19.setName("Indian Bank");
        bankList19.save();

        BankList bankList20 = new BankList();
        bankList20.setCode("7117");
        bankList20.setName("Indian Overseas Bank");
        bankList20.save();

        BankList bankList21 = new BankList();
        bankList21.setCode("7834");
        bankList21.setName("Kanrich Finance Limited");
        bankList21.save();

        BankList bankList22 = new BankList();
        bankList22.setCode("7861");
        bankList22.setName("Lanka Orix Finance PLC");
        bankList22.save();

        BankList bankList23 = new BankList();
        bankList23.setCode("7773");
        bankList23.setName("LB Finance PLC");
        bankList23.save();

        BankList bankList24 = new BankList();
        bankList24.setCode("7269");
        bankList24.setName("MCB Bank Ltd");
        bankList24.save();

//        BankList bankList25 = new BankList();
//        bankList25.setCode("7269");
//        bankList25.setName("MCB Bank Ltd");
//        bankList25.save();

        BankList bankList26 = new BankList();
        bankList26.setCode("7913");
        bankList26.setName("Mercantile Investment and Finance PLC");
        bankList26.save();

        BankList bankList27 = new BankList();
        bankList27.setCode("7898");
        bankList27.setName("Merchant Bank of Sri Lanka & Finance PLC");
        bankList27.save();

        BankList bankList28 = new BankList();
        bankList28.setCode("7214");
        bankList28.setName("National Development Bank PLC");
        bankList28.save();

        BankList bankList29 = new BankList();
        bankList29.setCode("7719");
        bankList29.setName("National Savings Bank");
        bankList29.save();

        BankList bankList30 = new BankList();
        bankList30.setCode("7162");
        bankList30.setName("Nations Trust Bank PLC");
        bankList30.save();

        BankList bankList31 = new BankList();
        bankList31.setCode("7311");
        bankList31.setName("Pan Asia Banking Corporation PLC");
        bankList31.save();

        BankList bankList32 = new BankList();
        bankList32.setCode("7135");
        bankList32.setName("Peoples Bank");
        bankList32.save();

        BankList bankList33 = new BankList();
        bankList33.setCode("7922");
        bankList33.setName("People’s Leasing & Finance PLC");
        bankList33.save();

        BankList bankList34 = new BankList();
        bankList34.setCode("7296");
        bankList34.setName("Public Bank");
        bankList34.save();

        BankList bankList35 = new BankList();
        bankList35.setCode("7755");
        bankList35.setName("Regional Development Bank");
        bankList35.save();

        BankList bankList36 = new BankList();
        bankList36.setCode("7278");
        bankList36.setName("Sampath Bank PLC");
        bankList36.save();

        BankList bankList37 = new BankList();
        bankList37.setCode("7728");
        bankList37.setName("Sanasa Development Bank");
        bankList37.save();

        BankList bankList38 = new BankList();
        bankList38.setCode("7782");
        bankList38.setName("Senkadagala Finance PLC");
        bankList38.save();

        BankList bankList39 = new BankList();
        bankList39.setCode("7287");
        bankList39.setName("Seylan Bank PLC");
        bankList39.save();

        BankList bankList40 = new BankList();
        bankList40.setCode("7038");
        bankList40.setName("Standard Chartered Bank");
        bankList40.save();

        BankList bankList41 = new BankList();
        bankList41.setCode("7144");
        bankList41.setName("State Bank of India");
        bankList41.save();

        BankList bankList42 = new BankList();
        bankList42.setCode("7764");
        bankList42.setName("State Mortgage & Investment Bank");
        bankList42.save();

        BankList bankList43 = new BankList();
        bankList43.setCode("7302");
        bankList43.setName("Union Bank of Colombo PLC");
        bankList43.save();

        BankList bankList44 = new BankList();
        bankList44.setCode("7816");
        bankList44.setName("Vallibel Finance PLC");
        bankList44.save();

    }

    private void setupWindowAnimations() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        //if(!isTrackingServiceRunning(TrackingService.class,this)){
//        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//        System.out.println("* provider "+provider);
        CheckGPS();
       // checkLogin();
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    "com.example.user.lankabellapps", 0);

        String version = info.versionName;

        mNaveHeaderName.setText("Core App" + " " + version);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        TrackingData trackingData = new TrackingData();
//        List<TrackingData> trackingDataList = new ArrayList<>();
//
//        trackingDataList = trackingData.getAllLocationData();
//        System.out.println("Location size  " + trackingDataList.size());
//        for (TrackingData t : trackingDataList) {
//            System.out.println("Location = " + t.getAccuracy() + "  " + t.isScyned);
//        }

        checktheLocationPermission();
        Attendence attendence = new Attendence();
        List<Attendence> attendenceList = new ArrayList<>();

        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new java.util.Date().getTime(), "dd/MM/yyyy"));


        if (!attendenceList.isEmpty()) {

            CheckGPS();

                if (attendenceList.get(0).getAttendenceOut().equals("")) {
                    Intent svrintent = new Intent(this, SampleTrackingService.class);
                    getBaseContext().startService(svrintent);
                }


//            if (attendenceList.get(0).getAttendenceOut().equals("")) {
//
//                //  if (!isTrackingServiceRunning(SampleTrackingService.class, this)) {
//
//                Intent svrintent = new Intent(getBaseContext(), SampleTrackingService.class);
//                getBaseContext().startService(svrintent);
//
//                //}
//
//            }
        }

        syncUpdatedMerchants();
        syncMerchants();

//        UserProfile userProfile = new UserProfile();
//        List<UserProfile> userProfilesList = new ArrayList<>();
//
//        userProfilesList = userProfile.getAllUsers();

//        CheckMyAttendneceSync checkMyAttendneceSync = new CheckMyAttendneceSync(this);
//        checkMyAttendneceSync.GetAttendenceData(userProfilesList.get(0).getUserName(), userProfilesList.get(0).getPassword());

        checkAttendence();



    }

    private void checktheLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2000);
        } else {
            // permission has been granted, continue as usual

        }
    }


    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 2000) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                snakBarCommon("You have given the Permission...", new ColoredSnackbar(this).TYPE_OK, 2000);

                Attendence attendence = new Attendence();
                List<Attendence> attendenceList = new ArrayList<>();

                attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new java.util.Date().getTime(), "dd/MM/yyyy"));


                if (!attendenceList.isEmpty()) {
                    CheckGPS();
                    if (attendenceList.get(0).getAttendenceOut().equals("")) {

                        //  if (!isTrackingServiceRunning(SampleTrackingService.class, this)) {

                        Intent svrintent = new Intent(getBaseContext(), SampleTrackingService.class);
                        getBaseContext().startService(svrintent);

                        //}

                    }
                }

            } else {
                // Permission was denied or request was cancelled
                snakBarCommon("Please give the Location Permission...", new ColoredSnackbar(this).TYPE_WARING, 2000);

            }
        }else{
            checktheLocationPermission();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //coloredSnackbar.showSnackBar("Can't back the System. Please Logout the System", coloredSnackbar.TYPE_WARING, 1300);

        if (currentId != R.id.home) {
//            transaction = fragmentManager.beginTransaction();
//            fragment = new HomeFragment();
//            currentId = R.id.home;
//            transaction.setCustomAnimations(R.anim.left_in, R.anim.slide_to_right);
//            transaction.replace(R.id.main_container, fragment).commit();
//            currentVisibleFragment = Constants.HOME_FRAGMETN;
            View view = bottomNavigation.findViewById(R.id.home);
            view.performClick();
//            bottomNavigation.setSelectedItemId(R.id.home);
//            bottomNavigation.performAccessibilityAction("")

        }


    }

    public void CallAppReset() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

        TimeCap timeCap2 = new TimeCap();
        timeCap2.updateMasterDataTime("", Constants.TodayLogedIn);


        finish();
    }

    public void dialogResult(int i) {
        switch (i) {
            case 1:
                new HomeFragment().dialogResult(this, 1);
                break;
        }
    }

    private boolean isTrackingServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service already", "running");
                return true;
            }
        }
        Log.i("Service not", "running");
        return false;
    }


    public void showNotification(String appName, int icon, String path) {

//        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://path_to_file.apk"));
//        notificationIntent.setClassName("com.android.browser",
//                "com.android.browser.BrowserActivity");

//        Intent install_intent = new Intent(Intent.ACTION_VIEW);
//        install_intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/"+ appName)),"application/vnd.android.package-archive");
//        PendingIntent pending = PendingIntent.getActivity(this,0, install_intent, 0);
//
//
//
//
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(icon)
//                .setContentIntent(pending)
//                .setContentTitle("Download Application")
//                .setWhen(10000);
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//// mId allows you to update the notification later on.
//
//
//        notificationBuilder.notify(1, notificationBuilder.build());
    }




    @Override
    public void getAttendenceSyncSuccess(String update, String type) {
        System.out.println("* type "+type);
 		
		Constants.ATTENDANCE_STATUS_OUT="OK";
        List<Attendence> allData = new ArrayList<>();
        allData = new Attendence().getAllAttendenc();

        if(type.equalsIgnoreCase("IN")){
            syncAttendenceOut();
            Constants.ATTENDANCE_STATUS_IN="OK";

            if(allData.size()==1){
                allData.get(0).setIssyncON("1");

            }
        }
        if(type.equalsIgnoreCase("OUT")){
            if(allData.size()==1){
                allData.get(0).setIssyncOFF("1");

            }
            updateLocation();
            Constants.ATTENDANCE_STATUS_OUT="OK";
        }





    }

    @Override
    public void getAttendenceSyncFail(String message, String type) {

        if(type.equalsIgnoreCase("IN")){
            Constants.ATTENDANCE_STATUS_IN="NO";
        }
        if(type.equalsIgnoreCase("OUT")){
            Constants.ATTENDANCE_STATUS_OUT="NO";
        }
        System.out.println(" getAttendenceSyncFail message "+message);
    }

    @Override
    public void getAttendenceSuccess(String update, String ServerTime) {

        Constants.ATTENDANCE_STATUS_OUT="OK";
        System.out.println("** ServerTime "+ServerTime);

        //Constants.CORRECTDATETIME_STATUS = 0;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sMobileDate = TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy-MM-dd HH:mm:ss");

            Date MobileDate = formatter.parse(sMobileDate);



            Constants.SERVER_TIME_COFIRM = ServerTime;
            String sservertime = Constants.SERVER_TIME_COFIRM;

            Date servertime = formatter.parse(sservertime);

            DateTime phoneDate = new DateTime(MobileDate);
            DateTime serverDate = new DateTime(servertime);

            Integer diff = Minutes.minutesBetween(phoneDate, serverDate).getMinutes();
            Log.d("Get Phone date :", phoneDate.toString());
            long diffN = Math.abs(diff);


            //  checkAttendence();




            if ((diffN < 10)) {

                dialogDisrmiss();


            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("                  Error");
                builder.setMessage("Please Correct Date & Time In Your Phone...");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        //dialog.dismiss();
                    }
                });
                /*builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });*/
                AlertDialog alert = builder.create();
                alert.show();

                // new AppResetConfirmation(this).ShowConfirmation(14, Constants.ATTENDENCEACTIVITY);
                coloredSnackbar.showSnackBar("Please Correct Date & Time In Your Phone...", coloredSnackbar.TYPE_ERROR, 2500);
            }
            //  updateLocation();
        }
        catch (Exception e){
            System.out.println(e);
        }
        // coloredSnackbar.showSnackBar("Please Correct Date & Time In Your Phone...", coloredSnackbar.TYPE_ERROR, 2500);
    }

    @Override
    public void getAttendenceFaile(String message) {
        Constants.ATTENDANCE_STATUS_OUT="NO";

//        if (new NetworkCheck().NetworkConnectionCheck(this)) {
//            try {
//                UserProfile userProfile = new UserProfile();
//                List<UserProfile> userProfilesList = new ArrayList<>();
//
//                userProfilesList = userProfile.getAllUsers();
//
//                CheckMyAttendneceSync checkMyAttendneceSync = new CheckMyAttendneceSync(this);
//                checkMyAttendneceSync.GetAttendenceData(userProfilesList.get(0).getUserName(), userProfilesList.get(0).getPassword());
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//        }

        checkUpdateStatus();


    }




    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START, true);
    }

    @Override
    public void onSuccess() {
        //addApplications();
        if (currentVisibleFragment.equals(Constants.HOME_FRAGMETN)) {
            ((HomeFragment) fragment).CallFromActivity();
        }
        ++successCount;
        System.out.println("App details ok");
        checkUpdateStatus();
//        }else{
//
//        }
    }

    private void checkUpdateStatus() {
        ++checkUpdateesCallCount;

        if (checkUpdateesCallCount >= 8) {
            if (successCount >= 8) {
                mProgressDialog.dismiss();
                snakBarCommon("Data Refresh Success", coloredSnackbar.TYPE_OK, 2000);
                onBackPressed();
                checkGPSVersion();

            } else {
                mProgressDialog.dismiss();
                snakBarCommon("Data Refresh Not Success", coloredSnackbar.TYPE_ERROR, 2000);
                checkGPSVersion();
            }

            checkUpdateesCallCount = 0;
            successCount = 0;
        }
    }

    private void checkGPSVersion() {
        try {
            System.out.println("*getRequireApp");
            GetRequireAppSync getRequireAppSync = new GetRequireAppSync(MainActivity.this);
            getRequireAppSync.getRequireApp();
//            String gps_version  =  getApplicationContext().getPackageManager().getPackageInfo("com.google.android.gms",0).versionName;
//            String map_version  =  getApplicationContext().getPackageManager().getPackageInfo("com.google.android.apps.maps",0).versionName;
//            Log.i("Tag", "Using Map Version: " + map_version);
//            alert.BuildAlertBox("current google map Version "+map_version+" \n"+"current google play service Version "+gps_version);
        } catch (Exception e) {
            System.out.println("*getRequireApp Errror "+e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void onFailed() {
//        if (new NetworkCheck().NetworkConnectionCheck(MainActivity.this)) {
//            callFetach();
//        } else {
//            coloredSnackbar.showSnackBar("Check your internet connection...", coloredSnackbar.TYPE_ERROR, 2000);
//        }
        checkUpdateStatus();
    }

    public void downloadCus() {
        UserProfile userProfile = new UserProfile();
        GetCustomerListSync getCustomerListSync = new GetCustomerListSync(this);
        getCustomerListSync.getCustomerList(userProfile.getAllUsers().get(0).getUserName(), userProfile.getAllUsers().get(0).getPassword());

    }

    public void downloadAccounts() {

        UserProfile userProfile = new UserProfile();
        GetAccountsListSync getAccountsListSync = new GetAccountsListSync(this);
        getAccountsListSync.getAccountsList(userProfile.getAllUsers().get(0).getUserName(), userProfile.getAllUsers().get(0).getPassword());

    }

    public void downloadCities() {
        UserProfile userProfile = new UserProfile();
        GetMerchantsCitiesSync getMerchantsCitiesSync = new GetMerchantsCitiesSync(this);
        getMerchantsCitiesSync.setAttendence(userProfile.getAllUsers().get(0).getUserName());
    }

    public void downloadMasterData() {

        syncLocationsReg();
        downloadCus();
        downloadAccounts();
        downloadCities();
        downloadTSRDetails();
        downloadMerchants();

        downloadLocationReg();

        syncVisits();
        syncAttendenceIn();
        syncAttendenceOut();
        downloadUnitHand();
        //syncPhoneData();


      //  updateLocation();

    }

    private void syncPhoneData() {
        mProgressDialog.setMessage("Downloading ...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
        UpdatePhoneDetailSync updatePhoneDetailSync = new UpdatePhoneDetailSync(this);
        List<UserProfile> userProfileList = new ArrayList<>();
        UserProfile userProfile = new UserProfile();

        userProfileList  = userProfile.getAllUsers();
        /*   a_phone.setText("Phone Version : "+Build.DEVICE);
        a_version.setText("Android Version : "+Build.VERSION.RELEASE);*/
        try {
            updatePhoneDetailSync.updatePhoneVersion(userProfileList.get(0).getUserName(), Build.DEVICE, Build.VERSION.RELEASE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void downloadUnitHand() {
        mProgressDialog.setMessage("Downloading ...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
        GetHandInUnit getHandInUnit = new GetHandInUnit(this);
        List<UserProfile> userProfileList = new ArrayList<>();
        UserProfile userProfile = new UserProfile();

        userProfileList  = userProfile.getAllUsers();

        getHandInUnit.GetUnits(userProfileList.get(0).getUserName());
    }

    private void syncAttendenceOut() {
        new AttendenceSyncOut().execute("");
    }

    private void updateLocation() {

        List<UserProfile> userProfileList = new ArrayList<>();
        UserProfile userProfile = new UserProfile();

        userProfileList  = userProfile.getAllUsers();

        CheckMyAttendneceSync checkMyAttendneceSync = new CheckMyAttendneceSync(this);
        checkMyAttendneceSync.GetAttendenceData(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword());

    }

    private void syncAttendenceIn() {
        new AttendenceSyncIn().execute("");
    }

    private void syncVisits() {
      new SyncVisit().execute("");
    }

    private void downloadLocationReg() {

        UserProfile userProfile = new UserProfile();
        GetLocationRegistrationsSync getLocationRegistrationsSync = new GetLocationRegistrationsSync(this);
        getLocationRegistrationsSync.getLocationRegistration(userProfile.getAllUsers().get(0).getUserName(), userProfile.getAllUsers().get(0).getPassword());

    }

    private void syncLocationsReg() {
        new LocationRegisterSyncAsync().execute("");
    }

    private void downloadMerchants() {
        UserProfile userProfile = new UserProfile();
        GetMerchantsSync getMerchantsSync = new GetMerchantsSync(this);
        getMerchantsSync.getCustomerList(userProfile.getAllUsers().get(0).getUserName(), userProfile.getAllUsers().get(0).getPassword());

    }

    private void downloadTSRDetails() {
        UserProfile userProfile = new UserProfile();
        GetTSRDeailsSync tsrDeailsSync = new GetTSRDeailsSync(this);
        tsrDeailsSync.getTsrDetails(userProfile.getAllUsers().get(0).getUserName(), userProfile.getAllUsers().get(0).getPassword());
    }

    @Override
    public void getCustoimerListSuccess(GetCommonResponseModel update) {
        try {
            cusObject = new JSONArray(update.getData());
            //
            Customers customers = new Customers();
            customers.clearTable();
            //saveDataToTheDatabase(obj);

            new AyncCus().execute("");


        } catch (JSONException e) {
            e.printStackTrace();
            checkUpdateStatus();
        }
    }

    @Override
    public void getCustomerListFaile(String message) {
        checkUpdateStatus();
//        if (new NetworkCheck().NetworkConnectionCheck(MainActivity.this)) {
//            //downloadCus();
//        } else {
//            coloredSnackbar.showSnackBar("Check your internet connection...", coloredSnackbar.TYPE_ERROR, 2000);
//        }

    }

    @Override
    public void getAccountsListSuccess(GetCommonResponseModel update) {

        try {
            accObject = new JSONArray(update.getData());
            Account account = new Account();
            account.clearTable();

            new AyncAccounts().execute("");
        } catch (JSONException e) {
            e.printStackTrace();
            checkUpdateStatus();
        }


    }

    @Override
    public void getAccountsListFaile(String message) {
//        if (new NetworkCheck().NetworkConnectionCheck(this)) {
//            downloadAccounts();
//        } else {
//            coloredSnackbar.showSnackBar("Check your internet connection...", coloredSnackbar.TYPE_ERROR, 2000);
//        }

        checkUpdateStatus();
    }

    @Override
    public void getCitiesSuccess(String update) {
        try {
            cityObject = new JSONArray(update);
            MerchantsCities merchantsCities = new MerchantsCities();
            merchantsCities.clearTable();

            new CitiesSaving().execute("");
        } catch (JSONException e) {
            e.printStackTrace();
            checkUpdateStatus();
        }
    }

    @Override
    public void getCitiesFaile(String message) {
        checkUpdateStatus();
    }

    @Override
    public void getTSRDetailsSuccess(String update) {
        //>
        try {

            JSONObject jb = new JSONObject(update);

            new TSRDetails().clearTable();

            TSRDetails tsrDetails = new TSRDetails();

            tsrDetails.setEpfNo(jb.getString("EpfNo"));
            tsrDetails.setNextMerchantNo(jb.getString("NextMerchantNo"));

            tsrDetails.save();

            ++successCount;
            System.out.println("TSR details ok");
            checkUpdateStatus();

        } catch (Exception e) {

        }
    }

    @Override
    public void getTSRDetailsFaile(String message) {
        //>
        checkUpdateStatus();
    }

    @Override
    public void getMerchantsListSuccess(GetCommonResponseModel update) {

        try {
            merchantsObject = new JSONArray(update.getData());
            Merchants merchants = new Merchants();
            merchants.clearTableNotSynced();

            new MerchantsSave().execute("");
        } catch (JSONException e) {
            e.printStackTrace();
            checkUpdateStatus();
        }
    }

    @Override
    public void getMerchantsListFaile(String message) {
        checkUpdateStatus();
    }

    private void syncMerchants() {//sending merchants up

        try {

            TSRDetails tsrDetails = new TSRDetails();

            Merchants merchants1 = new Merchants();
            List<Merchants> merchantsList = new ArrayList<>();

            merchantsList = merchants1.getAllNotSyncMerchants();

            for (Merchants m : merchantsList) {
                MerchantSync merchantSync = new MerchantSync(this);
                merchantSync.setMerchants(tsrDetails.getAllData().get(0).getEpfNo(), m.getMerchantId(), m.getMerchantName(), m.getAddress(), m.getTelephone(), m.getMcity(), m.getBankAccId(), m.getBank(), m.getNic(),m.getBankAccName());
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private void syncUpdatedMerchants() {

        try {

            TSRDetails tsrDetails = new TSRDetails();

            Merchants merchants1 = new Merchants();
            List<Merchants> merchantsList = new ArrayList<>();

            merchantsList = merchants1.getUpdatedMerchants();

            for (Merchants m : merchantsList) {
                MerchantUpdateSync merchantSync = new MerchantUpdateSync(this);
                merchantSync.updateMerchants(tsrDetails.getAllData().get(0).getEpfNo(), m.getMerchantId(), m.getMerchantName(), m.getAddress(), m.getTelephone(), m.getMcity(), m.getBankAccId(), m.getBank(), m.getNic(),m.getBankAccName());
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    @Override
    public void updateMerchantsListSuccess(String update, String merchantId) {
        if (update.equals("1")) {
            new Merchants().updateIsSynced(merchantId);
        }
    }

    @Override
    public void updateerchantsListFaile(String message) {

    }

    @Override
    public void getMerchantSyncSuccess(String update, String merchantId) {
        if (update.equals("1")) {
            new Merchants().updateIsSynced(merchantId);
        }
    }

    @Override
    public void getMerchantSynceFaile(String message) {

    }

    public void longpressResuls(MainMenuObject selectedItem, String tag) {

        switch (tag) {
            case "2":
                if (selectedItem.itemKey.equals("1")) {
                    snakBarCommon("Can not Uninstall Attendence...", new ColoredSnackbar(this).TYPE_WARING, 2000);
                } else {
                    //mPleaseWaitDialog.setMessage("Uninstalling " + selectedItem.itemText);

//                    Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
//                    intent.setData(Uri.parse(selectedItem.packagename));
//                    intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
//                    startActivityForResult(intent, 1);

                    Uri packageURI = Uri.parse("package:" + selectedItem.packagename);
                    Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI);
                    startActivityForResult(uninstallIntent, 1);


                }

                break;

            case "1":

                AvailableApps availableApps = new AvailableApps();


                Toast.makeText(this, selectedItem.itemText + " " + availableApps.getFromAppId(selectedItem.itemKey).get(0).getVersion(), Toast.LENGTH_LONG).show();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //mPleaseWaitDialog.dismiss();
            //mPleaseWaitDialog.setMessage("Please Wait...");
            if (resultCode == RESULT_OK) {
                Log.d("TAG", "onActivityResult: user accepted the (un)install");
                // snakBarCommon("Successfuly uninstalled...",new ColoredSnackbar(this).TYPE_OK,2000);
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("TAG", "onActivityResult: user canceled the (un)install");
                // snakBarCommon("Uninstall failed....",new ColoredSnackbar(this).TYPE_ERROR,2000);
            } else if (resultCode == RESULT_FIRST_USER) {
                Log.d("TAG", "onActivityResult: failed to (un)install");
                // snakBarCommon("Uninstall failed....",new ColoredSnackbar(this).TYPE_ERROR,2000);
            }
        }
    }

    @Override
    public void onLocationRegisterSuccess(GetCommonResponseModel update, String cusId, String accId) {
        if (update.getData() != null) {
            if (!update.getData().equals(Constants.LOGIN_SUCCESSFUL)) {


                    LocationRegisterWithCustomer locationRegisterWithCustomer = new LocationRegisterWithCustomer();
                    locationRegisterWithCustomer.updateIsSynced(cusId, "true");

            }
        }
    }

    @Override
    public void onLocationRegisterFailed(String message) {
           checkUpdateStatus();
    }

    @Override
    public void getLocationRegistrationFeatchSuccess(String update) {
        try {
            locationRegObj = new JSONArray(update);
            //
            LocationRegisterWithCustomer locationRegisterWithCustomer = new LocationRegisterWithCustomer();
            locationRegisterWithCustomer.clearTable();
            //saveDataToTheDatabase(obj);

            new LocationRegisterGetAync().execute("");


        } catch (JSONException e) {
            e.printStackTrace();
            checkUpdateStatus();
        }
    }

    @Override
    public void getLocationRegistrationFeatchFaile(String message) {
        checkUpdateStatus();

    }

    @Override
    public void onVisitSuccess(GetCommonResponseModel update, String addedDate) {
        CusVisit cusVisit = new CusVisit();
        cusVisit.updateIsSynced(addedDate,"1");
        System.out.println("#################### Visit Success ");
    }

    @Override
    public void onVisitFailed(String message) {

    }

    @Override
    public void GetHandSuccess(GetCommonResponseModel update) {
        mProgressDialog.dismiss();

        try {
            if(update.getId().equals("1")){
                unitHandObj = new JSONArray(update.getData());
                UnitHand unitHand = new UnitHand();
                unitHand.clearTable();

                new UnitHandSave().execute("");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getRequireAppSuccess(GetCommonResponseModel response) {
        try {
            String gps_ver ="99 99",map_ver = "9999",googl_ver = "999";
            try {
                 gps_ver = getApplicationContext().getPackageManager().getPackageInfo("com.google.android.gms", 0).versionName;
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                map_ver = getApplicationContext().getPackageManager().getPackageInfo("com.google.android.apps.maps", 0).versionName;
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                googl_ver = getApplicationContext().getPackageManager().getPackageInfo("com.google.android.googlequicksearchbox", 0).versionName;
            }catch (Exception e){
                e.printStackTrace();
            }



//            if(map_ver.equals("9999")){
//                try {
//                    map_ver = getApplicationContext().getPackageManager().getPackageInfo("com.google.android.apps.mapslite", 0).versionName;
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            if(googl_ver.equals("999")){
//                try {
//                    googl_ver = getApplicationContext().getPackageManager().getPackageInfo("com.google.android.apps.searchlite", 0).versionName;
//                }catch (Exception e){
//                    e.printStackTrace();//com.google.android.instantapps.supervisor
//                }
//            }
            System.out.println("* gps_ver "+gps_ver);
            System.out.println("* map_ver "+map_ver);
            System.out.println("* googl_ver "+googl_ver);

            String dd[] = gps_ver.split(" ");
            String gps_vers[]= dd[0].split("\\.");

            String gps_version_phone = gps_vers[0] + ".";
            for(int k=1;k<gps_vers.length;k++){
                gps_version_phone += gps_vers[k];
            }
            String map_vers[]= map_ver.split("\\.");
            String map_version_phone = map_vers[0] + ".";

            for(int k=1;k<map_vers.length;k++){
                map_version_phone += map_vers[k];
            }
            String google_version_phone = "999";

            if(!googl_ver.equals("999")){
                String googl_ver_array[]= googl_ver.split("\\.");
                google_version_phone = googl_ver_array[0] + ".";
                for(int j=1;j<googl_ver_array.length-1;j++){
                    google_version_phone += googl_ver_array[j];
                }

            }



            System.out.println("google_version_phone = " + google_version_phone);

            if(response.getData() != null){
                requireAppObj = new JSONArray(response.getData());

                String gps_version_latest = "0";
                String map_version_latest = "0";
                String google_version_latest = "0";

                for(int i=0;i<requireAppObj.length();i++){
                    try {
                        JSONObject row = requireAppObj.getJSONObject(i);

                        switch (row.getString("AppId")) {
                            case "50":
                                map_version_latest = row.getString("RequiredVersion");
                                System.out.println("* map_version_latest "+map_version_latest);
                                break;
                            case "51":
                                gps_version_latest = row.getString("RequiredVersion");
                                System.out.println("* gps_version_latest "+gps_version_latest);
                                break;
                            case "52":
                                google_version_latest = row.getString("RequiredVersion");
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                String service_msg = "";

                System.out.println("*0 service_msg "+service_msg.length());//google_version_phone
                if(Float.parseFloat(map_version_latest) > Float.parseFloat(map_version_phone)){
                    if(service_msg.length()>0){
                        service_msg += ", \'Google Map\'";
                    }else{
                        service_msg += "\'Google Map\'";
                    }
                }
                System.out.println("*1 service_msg "+service_msg.length());
                if(Float.parseFloat(gps_version_latest) > Float.parseFloat(gps_version_phone)){
                    if(service_msg.length()>0){
                        service_msg += ", \'Google Play services\'";
                    }else{
                        service_msg += "\'Google Play services\'";
                    }
                }
                System.out.println("*2 service_msg "+service_msg.length());
                if(Float.parseFloat(google_version_latest) > Float.parseFloat(google_version_phone)){
                    if(service_msg.length()>0){
                        service_msg += "and \'Google\'";
                    }else{
                        service_msg += "\'Google\'";
                    }
                }
                System.out.println("* map_latest "+map_version_latest+" map_version_phone "+map_version_phone);
                System.out.println("* gps_latest "+gps_version_latest+" gps_version_phone "+gps_version_phone);
                System.out.println("* google_version_latest "+google_version_latest+" google_version_phone "+google_version_phone);

                if(service_msg.length()>0){
                    alert.BuildAlertBox("Please update "+service_msg);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//
    }

    @Override
    public void getRequireAppError(String message) {
        System.out.println("mess "+message);
    }

    @Override
    public void onLoginSuccess(GetCommonResponseModel update) {
        if (update != null) {

            if(update.getStatus().equals("1")){
                TimeCap timeCap = new TimeCap();
                List<TimeCap> timeCapList = new ArrayList<>();
                timeCapList = timeCap.getAllData();

                if (timeCapList.isEmpty()) {


                    TimeCap timeCap1 = new TimeCap();
                    timeCap1.setRawName(Constants.LastLoginTime);
                    timeCap1.setDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"));

                } else {
                    TimeCap timeCap1 = new TimeCap();
                    timeCap1.updateMasterDataTime(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"), Constants.LastLoginTime);
                }
            }else{
                Intent ff = new Intent(this,LoginActivity.class);
                startActivity(ff);
            }

        }

    }

    @Override
    public void onLoginFailed(String message, String id) {
        Intent ff = new Intent(this,LoginActivity.class);
        startActivity(ff);
    }

    @Override
    public void UpdatePhoneDataSuccess(GetCommonResponseModel update) {
        if(update.getStatus().equals("1")){
            System.out.println("Phone Data uploaded successfully");
        }else{
            System.out.println("Phone Data not uploaded");
        }
    }

    @Override
    public void UpdatePhoneDataError(String message) {
        System.out.println("Phone Data not uploaded "+message);
    }

    /**/
    private class UnitHandSave extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            for(int i=0;i<unitHandObj.length();i++){

                try {
                    JSONObject row = unitHandObj.getJSONObject(i);

                    UnitHand unitHand = new UnitHand();
                    unitHand.setIssuedBy(row.getString("IssuedBy"));
                    unitHand.setIssuedDate(row.getString("IssuedDate"));
                    unitHand.setIssueNo(row.getString("IssueNo"));
                    unitHand.setItemCode(row.getString("ItemCode"));
                    unitHand.setSerialNo(row.getString("SerialNo"));
                    unitHand.setStatus(row.getString("Status"));
                    unitHand.setType(row.getString("Type"));

                    unitHand.save();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            return "Executed";
        }
    }
    /**/

    @Override
    public void GetHandError(String message) {
        mProgressDialog.dismiss();


        System.out.println("Error while downloading Unit Hands");

    }


    private class SyncVisit extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {



            UserProfile userProfile = new UserProfile();


            CusVisit cusVisit = new CusVisit();
            for(CusVisit c: cusVisit.getAllDataNotSynced() ){
                VisitLogSync visitLogSync = new VisitLogSync(MainActivity.this);
                visitLogSync.Visits(userProfile.getAllUsers().get(0).getUserName(), userProfile.getAllUsers().get(0).getPassword(),
                        c.getCusName(),c.getContactNo(), c.getRemarks(), c.getDatetime(), String.valueOf(c.getLati()), String.valueOf(c.getLati()),c.getAddress(),c.getCusId());

            }



            return "";
        }
    }

    private class AttendenceSyncIn extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {


            Attendence attendenceToSync = new Attendence();
            List<Attendence> attendenceListToSync = new ArrayList<>();
            List<Attendence> attendenceListToSyncOut = new ArrayList<>();
           // attendenceListToSync = attendenceToSync.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
            attendenceListToSync = attendenceToSync.getAttendanceSyncInFlag((TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy")),"0");
            attendenceListToSyncOut = attendenceToSync.getAttendanceSyncOffFlag((TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy")),"0");


            if(attendenceListToSync.size()>0) {
                SyncAttendence(attendenceListToSync.get(0).getAttendenceIn(), Constants.IN, attendenceListToSync.get(0).getTeamCode(),
                        attendenceListToSync.get(0).getMeterValue1(), attendenceListToSync.get(0).getBikeNumber(), attendenceListToSync.get(0).getRallType());
            }else{
                if(attendenceListToSyncOut.size()>0){
                    SyncAttendence(attendenceListToSyncOut.get(0).getAttendenceOut(), Constants.OUT, attendenceListToSyncOut.get(0).getTeamCode(),
                            attendenceListToSyncOut.get(0).getMeterValue1(), attendenceListToSyncOut.get(0).getBikeNumber(), attendenceListToSyncOut.get(0).getRallType());
                }

            }



            return "";
        }
    }
    private class AttendenceSyncOut extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {


            Attendence attendenceToSync = new Attendence();
            List<Attendence> attendenceListToSync = new ArrayList<>();
            //attendenceListToSync = attendenceToSync.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
            attendenceListToSync = attendenceToSync.getAttendanceSyncOffFlag((TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy")),"0");
            System.out.println("****** attendenceListToSync size out "+attendenceListToSync.size());
            if(attendenceListToSync.size()>0){
                SyncAttendence(attendenceListToSync.get(0).getAttendenceOut(), Constants.OUT, attendenceListToSync.get(0).getTeamCode(),
                        attendenceListToSync.get(0).getMeterValue1(), attendenceListToSync.get(0).getBikeNumber(), attendenceListToSync.get(0).getRallType());
            }


            return "";
        }
    }

    public void SyncAttendence(String date, String type, String temCode, String meterValue, String bikeCode, String category) {

        UserProfile userProfile = new UserProfile();
        List<UserProfile> userProfileList = new ArrayList<>();

        userProfileList = userProfile.getAllUsers();

        AttendenctSync attendenctSync = new AttendenctSync(this);
        String serial_number = Constants.SERIAL_NUMBER;
        System.out.println("Serial number "+serial_number);
        if(serial_number.equals("") ){
            attendenctSync.setAttendence(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), date, type, Utils.getSimSerialNumber(this), temCode,
                    meterValue, bikeCode, category);
        }else{
            attendenctSync.setAttendence(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), date, type, serial_number, temCode,
                    meterValue, bikeCode, category);
        }


    }
    private class LocationRegisterSyncAsync extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            LocationRegisterWithCustomer locationRegisterWithCustomer = new LocationRegisterWithCustomer();
            List<LocationRegisterWithCustomer> locationRegisterWithCustomersList = new ArrayList<>();

            locationRegisterWithCustomersList = locationRegisterWithCustomer.getUpdatesByisSynced();

            for(LocationRegisterWithCustomer  l: locationRegisterWithCustomer.getUpdatesByisSynced()){
                UserProfile userProfile = new UserProfile();
                LocationRegisterSync locationRegisterSync = new LocationRegisterSync(MainActivity.this);
                locationRegisterSync.LocationRegister(userProfile.getAllUsers().get(0).getUserName(), userProfile.getAllUsers().get(0).getPassword(),"1" ,l.getCusCode(),userProfile.getAllUsers().get(0).getUserName(), String.valueOf(l.getLongi()),String.valueOf(l.getLati()), userProfile.getAllUsers().get(0).getUserName(),l.getRegisteredDate(),l.getProvince(),l.getDistrict(),l.getCity(),"",l.getIsConformed());
            }


            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            ++successCount;
            System.out.println("Account details ok");
            checkUpdateStatus();
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {


        }

    }

    private class LocationRegisterGetAync extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            for (int i = 0; i < locationRegObj.length(); i++) {

                try {
                    JSONObject row = locationRegObj.getJSONObject(i);

                    LocationRegisterWithCustomer locationRegisterWithCustomer = new LocationRegisterWithCustomer();

                    locationRegisterWithCustomer.setCusCode(row.getString("CustomerId"));
                    locationRegisterWithCustomer.setLati(Double.parseDouble(row.getString("Latitude")));
                    locationRegisterWithCustomer.setLongi(Double.parseDouble(row.getString("Longtitude")));
                    locationRegisterWithCustomer.setProvince(row.getString("Province"));
                    locationRegisterWithCustomer.setDistrict(row.getString("District"));
                    locationRegisterWithCustomer.setCity(row.getString("City"));
                    locationRegisterWithCustomer.setRegisteredDate(row.getString("AddedDateStr"));
                    locationRegisterWithCustomer.setIsSynced("true");
                    locationRegisterWithCustomer.setIsConformed(row.getString("Status"));

                    locationRegisterWithCustomer.save();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            ++successCount;
            System.out.println("Location Register details ok");
            checkUpdateStatus();
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {


        }

    }

    private class MerchantsSave extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            for (int i = 0; i < merchantsObject.length(); i++) {

                try {
                    JSONObject row = merchantsObject.getJSONObject(i);

                    Merchants merchants = new Merchants();
                    merchants.setMerchantId(row.getString("SALES_PERSON_CODE"));
                    merchants.setMerchantName(row.getString("SALES_PERSON_NAME"));
                    merchants.setMcity(row.getString("REGION"));
                    merchants.setTelephone(row.getString("TELEPHONE_NO"));
                    merchants.setAddress(row.getString("ADDRESS"));
                    merchants.setAgrimentStatus(row.getString("IS_AGREEMENT"));


                    if (row.getString("BANK").equals("null")) {
                        merchants.setBank("");
                    } else {
                        merchants.setBank(row.getString("BANK"));
                    }

                    if (row.getString("ACC_OWNER_NAME").equals("null")) {
                        merchants.setBankAccName("");
                    } else {
                        merchants.setBankAccName(row.getString("ACC_OWNER_NAME"));
                    }


                    if (row.getString("ACC_NO").equals("null")) {
                        merchants.setBankAccId("");
                    } else {
                        merchants.setBankAccId(row.getString("ACC_NO"));
                    }

                    if (row.getString("NIC").equals("null")) {
                        merchants.setNic("");
                    } else {
                        merchants.setNic(row.getString("NIC"));
                    }

                    merchants.setIsSynced("true");


                    merchants.save();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            ++successCount;
            System.out.println("Merchants details ok");
            checkUpdateStatus();
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {


        }

    }

    private class AyncCus extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            for (int i = 0; i < cusObject.length(); i++) {

                try {
                    JSONObject row = cusObject.getJSONObject(i);

                    Customers customers = new Customers();

                    customers.setCompanyCode(row.getString("Id_number"));
                    customers.setNamem(row.getString("Profile_name"));
                    customers.setTitle(row.getString("Title"));
                    customers.setAddress_line_1(row.getString("Address_line_1"));
                    customers.setAddress_line_2(row.getString("Address_line_2"));
                    customers.setAddress_line_3(row.getString("Address_line_3"));
                    customers.setCity(row.getString("City"));
                    customers.setPostal_code(row.getString("Postal_code"));
                    //customers.setModified_by(row.getString("Modified_on"));
                    customers.setStatus(row.getString("Profile_status"));
                    customers.setD_first_name(row.getString("D_first_name"));
                    customers.setD_last_name(row.getString("D_last_name"));
                    customers.setDistrict(row.getString("District"));
                    customers.setProvince(row.getString("Province"));
                    customers.setContactNo(row.getString("Contact_phone_no"));
                    customers.setRegister("false");
                    customers.setLati(0.0);
                    customers.setLongi(0.0);
//                    Random r = new Random();
//                    int i1 = r.nextInt(20000 - 10000) + 1250;
//                    customers.setOs(String.valueOf(i1));

                    customers.setOs(row.getString("Account_outstanding"));

                    customers.save();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            ++successCount;
            System.out.println("Customer details ok");
            checkUpdateStatus();
        }


        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    private class AyncAccounts extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            for (int i = 0; i < accObject.length(); i++) {

                try {
                    JSONObject row = accObject.getJSONObject(i);

                    Account account = new Account();

                    account.setCompanyCode(row.getString("CpIdnumber"));
                    account.setAccountType(row.getString("AcAccountCategory"));
                    account.setAccountName(row.getString("AccountName"));
                    account.setTitle(row.getString("Title"));
                    account.setAddressLine1(row.getString("AddressLine1"));
                    account.setAddressLine2(row.getString("AddressLine2"));
                    account.setAddressLine3(row.getString("AddressLine3"));
                    account.setCity(row.getString("City"));
                    account.setPostalCode(row.getString("PostalCode"));
                    account.setBillrunCode(row.getString("BillrunCode"));
                    account.setProvince(row.getString("Province"));
                    account.setDistrict(row.getString("District"));
                    account.setCreditLimit(row.getString("CreditLimit"));
                    account.setAccountCode(row.getString("AccountCode"));
                    account.setAccountType(row.getString("AccountType"));
                    account.setOs(row.getString("Account_outstanding"));
                    // account.setDueDate(row.get);


                    Random r = new Random();
                    int Low = 1;
                    int High = 30;
                    int Result = r.nextInt(High - Low) + Low;

                    Random r1 = new Random();
                    int Low1 = 1;
                    int High1 = 12;
                    int Result1 = r.nextInt(High1 - Low1) + Low1;

                    account.setDueDate(Result + "/" + Result1 + "/2016");

                    account.save();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            ++successCount;
            System.out.println("Account details ok");
            checkUpdateStatus();
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {


        }

    }

    private class CitiesSaving extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            for (int i = 0; i < cityObject.length(); i++) {

                try {

                    JSONObject row = cityObject.getJSONObject(i);

                    MerchantsCities merchantsCities = new MerchantsCities();
                    merchantsCities.setPostalCode(row.getString("POSTAL_CODE"));
                    merchantsCities.setCity(row.getString("CITY"));
                    merchantsCities.setRegion(row.getString("REGION"));
                    merchantsCities.setPostOfficeName(row.getString("POST_OFFICE_NAME"));
                    merchantsCities.setArea(row.getString("AREA"));
                    merchantsCities.setLatitude(row.getString("LATITUDE"));
                    merchantsCities.setLongtitude(row.getString("LONGITUDE"));
                    merchantsCities.setCurrentChanges(row.getString("CURRENT_CHANGES"));
                    merchantsCities.setAPP_ID(row.getString("APP_ID"));

                    merchantsCities.save();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            ++successCount;
            System.out.println("Merchants City details ok");
            checkUpdateStatus();
        }


        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    public void snakBarCommon(String message, String x, int l) {
        coloredSnackbar.showSnackBar(message, x, l);

        //bottomNavigation.setNotification(notification, bottomNavigation.getItemsCount() - 1);
    }

    public void progressBar(String msg, int type) {

        switch (type) {

            case 0:

                if(mCommonProgressDialog != null){
                    mCommonProgressDialog.dismiss();
                }

                break;

            case 1:
                mCommonProgressDialog = new ProgressDialog(this);
                // Set your ProgressBar Message
                mCommonProgressDialog.setMessage(msg);
                mCommonProgressDialog.setIndeterminate(false);
                mCommonProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mCommonProgressDialog.setCancelable(false);

                mCommonProgressDialog.show();

                break;


        }
    }

    private class CheckInternet extends AsyncTask<String, Void, String> {

        boolean check = false;

        @Override
        protected String doInBackground(String... params) {

            check = new NetworkCheck().isInternetAvailable();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            mPleaseWaitDialog.dismiss();

            downloadUpdates(check);
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mPleaseWaitDialog.show();
        }
    }

    private void downloadUpdates(boolean check) {
        if (new NetworkCheck().NetworkConnectionCheck(MainActivity.this)) {
            if (check) {
                mProgressDialog.show();
                callFetach();
                downloadMasterData();
                System.out.println("test test");
            } else {
                mProgressDialog.dismiss();
                coloredSnackbar.showSnackBar("Check your internet connection...", coloredSnackbar.TYPE_ERROR, 2000);
            }

            //coloredSnackbar.showSnackBar("Checking for new Updates...", coloredSnackbar.TYPE_UPDATE, 5000);
        } else {
            mProgressDialog.dismiss();
            coloredSnackbar.showSnackBar("Check your internet connection...", coloredSnackbar.TYPE_ERROR, 2000);
        }
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();

                String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                String fileName = currentAppName + ".apk";
                destination += fileName;
                File file = new File(destination);
                if (file.exists()) {
//                //file.delete() - test this, I think sometimes it doesnt work
                    file.delete();
                }

                final Uri uri = Uri.parse("file://" + destination);


                output = new FileOutputStream(destination);


                //            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//            String fileName = currentAppName + ".apk";
//            destination += fileName;
//            final Uri uri = Uri.parse("file://" + destination);
//
//            //Delete update file if exists
//            File file = new File(destination);
//            if (file.exists())
//                //file.delete() - test this, I think sometimes it doesnt work
//                file.delete();
//
//            //get url of app on server
//            //String url = Main.this.getString(R.string.update_app_url);
//
//
//            request = new DownloadManager.Request(Uri.parse("http://" + currentUrl.trim()));
//            request.setDescription("Downloading " + currentAppName);
//            request.setTitle(currentAppName);
//
//            request.setDestinationUri(uri);
//
//
//            if (isStoragePermissionGranted()) {
//                manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
//                downloadId = manager.enqueue(request);
//                downloadingComplete();
//            } else {
//                Log.v("Popup Installing", "Permission is revoked");
//                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            }


                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressUpdateDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressUpdateDialog.setIndeterminate(false);
            mProgressUpdateDialog.setMax(100);
            mProgressUpdateDialog.setProgress(progress[0]);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressUpdateDialog.dismiss();
            if (result != null) {
                //Toast.makeText(context, "Check the internet connection... ", Toast.LENGTH_LONG).show();
                snakBarCommon("Check the internet connection...", new ColoredSnackbar(MainActivity.this).TYPE_ERROR, 2000);
            } else {
                installApk();
            }
            //Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void installApk() {

        Notification.Builder notification = new Notification.Builder(this);

        File toInstall = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + currentAppName + ".apk");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", toInstall);
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
            //mListDialog.dismiss();
        } else {
            Uri apkUri = Uri.fromFile(toInstall);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //mListDialog.dismiss();
        }


    }


//    public interface FromMainToHome{
//        public void runTheeSuccess();
//    }
}