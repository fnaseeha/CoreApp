package com.example.user.lankabellapps.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
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
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.activities.AttendenceActivity;
import com.example.user.lankabellapps.activities.MainActivity;
import com.example.user.lankabellapps.adapters.MainMenuAdapter;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.helper.CommonHelperClass;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.helper.Featch;
import com.example.user.lankabellapps.helper.ItemClickSupport;
import com.example.user.lankabellapps.helper.NetworkCheck;
import com.example.user.lankabellapps.helper.TimeFormatter;
import com.example.user.lankabellapps.helper.Utils;
import com.example.user.lankabellapps.models.Account;
import com.example.user.lankabellapps.models.Attendence;
import com.example.user.lankabellapps.models.AvailableApps;
import com.example.user.lankabellapps.models.Customers;
import com.example.user.lankabellapps.models.DownloadItemModel;
import com.example.user.lankabellapps.models.LocationRegisterWithCustomer;
import com.example.user.lankabellapps.models.MainMenuObject;
import com.example.user.lankabellapps.models.Merchants;
import com.example.user.lankabellapps.models.MerchantsCities;
import com.example.user.lankabellapps.models.SingleItemModel;
import com.example.user.lankabellapps.models.TSRDetails;
import com.example.user.lankabellapps.models.TimeCap;
import com.example.user.lankabellapps.models.TrackingData;
import com.example.user.lankabellapps.models.UserProfile;
import com.example.user.lankabellapps.popups.AppResetConfirmation;
import com.example.user.lankabellapps.popups.MainMenuOptionspopup;
import com.example.user.lankabellapps.popups.SingleItemListPopup;
import com.example.user.lankabellapps.popups.UpdateAppsPopup;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.backgroundservices.TrackingService;
import com.example.user.lankabellapps.services.sync.AttendenctSync;
import com.example.user.lankabellapps.services.sync.GetAccountsListSync;
import com.example.user.lankabellapps.services.sync.GetCustomerListSync;
import com.example.user.lankabellapps.services.sync.GetLocationRegistrationsSync;
import com.example.user.lankabellapps.services.sync.GetMerchantsCitiesSync;
import com.example.user.lankabellapps.services.sync.GetMerchantsSync;
import com.example.user.lankabellapps.services.sync.GetTSRDeailsSync;
import com.example.user.lankabellapps.services.sync.LocationRegisterSync;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import com.example.user.lankabellapps.services.sync.LoginSync;
import com.google.gson.Gson;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class HomeFragment extends Fragment implements MainMenuAdapter.SingleItemClickListener, SingleItemListPopup.SingleItemPopupItemClickListener, Featch.featchDone, GetCustomerListSync.GetCustomerListEvents, GetAccountsListSync.GetAccountsListEvents, GetMerchantsCitiesSync.GetCitiesEvents, GetTSRDeailsSync.GetTSRDetailsEvents, GetMerchantsSync.GetMerchantsList, UpdateAppsPopup.ButtonClicksUpdatePopups, AttendenctSync.GetAttendenceEvents, MainMenuOptionspopup.SingleItemPopupItemClickListener, LocationRegisterSync.LocationRegisterEvents, GetLocationRegistrationsSync.GetLocationRegistrationFeatchEvents {


    FloatingActionButton fab;

    TextView mTilte, mAvaialveNo, mNewApps;
    RecyclerView mRecycleView;
    ImageButton mMainOptions, mCheckTracking, mCheckForNewUpdates;
    ImageView mMenu;
    LinearLayout mAppAvailableBar;

    JSONArray cusObject = null, accObject = null, cityObject = null, merchantsObject = null, locationRegObj = null;

    BroadcastReceiver sentSmsBroadcastCome;


    ColoredSnackbar coloredSnackbar;
    ProgressDialog mProgressDialog;

    String currentAppName;
    DownloadTask downloadTask;

    View view;

    DrawerLayout drawer;

    MainMenuObject currentSelected;

    List<AvailableApps> availableAllApps = new ArrayList<>(), availableNotInstalledAppsList = new ArrayList<>(), availableInstalled = new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //ButterKnife.bind(getActivity(), view);
        mTilte = (TextView) view.findViewById(R.id.tv_title);
        mRecycleView = (RecyclerView) view.findViewById(R.id.rv_main_icon_view);
        mMainOptions = (ImageButton) view.findViewById(R.id.ib_main_options);
        mAvaialveNo = (TextView) view.findViewById(R.id.tv_no_of_apps);
        mNewApps = (TextView) view.findViewById(R.id.tv_new_apps);
        mCheckTracking = (ImageButton) view.findViewById(R.id.ib_tracking_check);
        mCheckForNewUpdates = (ImageButton) view.findViewById(R.id.ib_check_for_new_updates);
        mMenu = (ImageView) view.findViewById(R.id.iv_menu);
        mAppAvailableBar = (LinearLayout) view.findViewById(R.id.ll_app_available_bar);


        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Updating Applications");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

                final List<DownloadItemModel> list = new ArrayList<>();

                SingleItemListPopup dialogPopup = new SingleItemListPopup(getActivity(), HomeFragment.this, layoutInflater, "");
                dialogPopup.makeDialog("New Applications", true, list, "account");

            }
        });

        mMainOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CustomerSearchLongPressPopup customerSearchLongPressPopup = new CustomerSearchLongPressPopup(getActivity());
//                customerSearchLongPressPopup.show("");

                new AppResetConfirmation(getActivity()).ShowConfirmation(1, "Options");

            }
        });

        mCheckForNewUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CustomerSearchLongPressPopup customerSearchLongPressPopup = new CustomerSearchLongPressPopup(getActivity());
//                customerSearchLongPressPopup.show("");
                if (new NetworkCheck().NetworkConnectionCheck(getActivity())) {
                    callFetach();
                    downloadMasterData();
                    System.out.println("test test");

                    coloredSnackbar.showSnackBar("Checking for new Updates...", coloredSnackbar.TYPE_UPDATE, 5000);
                } else {
                    coloredSnackbar.showSnackBar("Check your internet connection...", coloredSnackbar.TYPE_ERROR, 2000);
                }

            }
        });

        mMenu.setVisibility(View.VISIBLE);
        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START, true);
            }
        });


//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//
//        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
//        drawer.setBackgroundColor(getResources().getColor(R.color.background_color));
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                getActivity(), drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);
        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.setBackgroundColor(getResources().getColor(R.color.background_color));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        init();
        //checkAppsAvailability();


        try {

            AvailableApps availableApps = new AvailableApps();
            PackageManager manager1 = getActivity().getPackageManager();
            PackageInfo info1 = null;
            try {
                info1 = manager1.getPackageInfo(
                        availableApps.getFromAppId("1").get(0).getPackagename(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String version = info1.versionName;


            float dbVersion = Float.parseFloat(availableApps.getFromAppId("1").get(0).getVersion());

            String newversion;

            if( version.length()==5){
                newversion = version.substring(0,3)+version.substring(4);
            }else if(version.length()==7){
                newversion = version.substring(0,3)+version.substring(4,5)+version.substring(6,7);
            }else{
                newversion = version;
            }
            float NewVersion1 = Float.parseFloat(newversion);
            System.out.println("* NewVersion1 "+NewVersion1);
            System.out.println("* dbVersion "+dbVersion);

            if (NewVersion1 < dbVersion) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Core App");
                builder.setMessage("Update available to the Core App");
                builder.setPositiveButton("OK", null);
                builder.show();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

//        try{
//
//            List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);
//
//            for (int i = 0; i < packs.size(); i++) {
//                PackageInfo p = packs.get(i);
////                if ((isSystemPackage(p) == false)) {
//                System.out.println("* packageName "+p.packageName);//com.lk.lankabell.android.activity.tsr
//                    String appName = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
//                   // System.out.println("* appName "+appName);
////                    Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
////                    res.add(new AppList(appName, icon));
////                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        return view;


    }


    public void checkAppsAvailability() {
        availableNotInstalledAppsList.clear();
        availableInstalled.clear();
        for (AvailableApps a : availableAllApps) {

            String tempPackagename = a.getPackagename();
            //
            if (!a.getAppId().matches("1|2|3")) {
                if (!CommonHelperClass.appInstalledOrNot(getContext(), tempPackagename)) {
                    availableNotInstalledAppsList.add(a);
                } else {
                    availableInstalled.add(a);
                }
            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        addApplications();
        AvailableApps availableApps = new AvailableApps();
        availableAllApps = availableApps.getAllApps();
        mNewApps.setText("New Apps");


        TrackingData trackingData = new TrackingData();
        trackingData.deleteSycnedTrackingData();

        checkAppsAvailability();
        setAvailableAppLable();
        updateApplications();
        setAdapter();
        syncAllAttendence();

    }


    private void setAvailableAppLable() {
        try {

            mAvaialveNo.setText(String.valueOf(availableNotInstalledAppsList.size()));

            if (mAvaialveNo.getText().toString().equals("0")) {
                mAppAvailableBar.setVisibility(View.GONE);
            } else {
                mAppAvailableBar.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void addApplications() {

        TimeCap timeCap = new TimeCap();

        List<TimeCap> timeCapList = new ArrayList<>();
        List<TimeCap> timeCapList1 = new ArrayList<>();
        timeCapList = timeCap.getTimeCapByName(Constants.CheckNewAppsTime);
        timeCapList1 = timeCap.getTimeCapByName(Constants.CheckAppUpdate);

        if (timeCapList.isEmpty()) {
            TimeCap timeCap1 = new TimeCap();
            timeCap1.setRawName(Constants.CheckNewAppsTime);
            timeCap1.setDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"));
            timeCap1.save();

            TimeCap timeCap2 = new TimeCap();
            timeCap2.setRawName(Constants.CheckAppUpdate);
            timeCap2.setDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"));
            timeCap2.save();

            callFetach();
            downloadMasterData();


        } else {
            long x = (new Date().getTime() - TimeFormatter.changeStringTimeToLong(timeCapList.get(0).getDate(), "yyyy-MM-dd HH:mm:ss"));
            long y = (new Date().getTime() - TimeFormatter.changeStringTimeToLong(timeCapList1.get(0).getDate(), "yyyy-MM-dd HH:mm:ss"));
            System.out.println(timeCapList.get(0).getDate());
            if (7200000 < x) {
                TimeCap timeCap1 = new TimeCap();
                timeCap1.updateMasterDataTime(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"), Constants.CheckNewAppsTime);
                callFetach();
                downloadMasterData();
                System.out.println("ttttttttttttttttttttttttttttttttttt");
            }
//            if(10000 < y){
//
//            }
        }
    }

    private void callFetach() {
        Featch featch = new Featch(this);
        featch.featchAppDetails();

    }

    public void updateApplications() {
        try {

            AvailableApps availableApps1 = new AvailableApps();
            List<AvailableApps> availableAppsesList = new ArrayList<>();
            availableAppsesList = availableApps1.getAllApps();
            for (AvailableApps a : availableAppsesList) {
                if (!a.getAppId().matches("1|2|3")) {
                    PackageManager manager = getActivity().getPackageManager();
                    PackageInfo info = null;
                    try {
                        info = manager.getPackageInfo(a.getPackagename(), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                      //  System.out.println(e);
                    }

                    String version = info.versionName;
                    String newversion = version;
                    if (version.length() == 5) {
                        newversion = version.substring(0, 3) + version.substring(4);
                    } else if (version.length() == 7) {
                        newversion = version.substring(0, 3) + version.substring(4, 5) + version.substring(6, 7);
                    } else {
                        newversion = version;
                    }
                    System.out.println(a.getAppName() + "* ve " + version + "a.g  " + a.getVersion()+"newversion "+newversion);

                    if(Float.parseFloat(newversion)>= Float.parseFloat(a.getVersion())){
                        //no need to update
                        AvailableApps availableApps = new AvailableApps();
                        availableApps.updateUpdateAvailable(a.getAppId(), 0);
                    }else {
                        //forcefully update
                        AvailableApps availableApps = new AvailableApps();
                        availableApps.updateUpdateAvailable(a.getAppId(), 1);
                    }

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

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

    private void downloadTSRDetails() {
        UserProfile userProfile = new UserProfile();
        GetTSRDeailsSync tsrDeailsSync = new GetTSRDeailsSync(this);
        tsrDeailsSync.getTsrDetails(userProfile.getAllUsers().get(0).getUserName(), userProfile.getAllUsers().get(0).getPassword());
    }

    public void downloadMasterData() {
        syncLocationsReg();
        downloadTSRDetails();
        downloadCus();
        downloadAccounts();
        downloadCities();
        downloadMerchants();
        downloadLocationReg();
        syncAllAttendence();

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

    private void setAdapter() {
        AvailableApps availableApps = new AvailableApps();
        availableAllApps = availableApps.getAllApps();
        checkAppsAvailability();
        setAvailableAppLable();

        recycleViewLoad();

        List<MainMenuObject> mainMenuObjectList = new ArrayList<>();

        mainMenuObjectList.clear();

        mainMenuObjectList.add(new MainMenuObject("Attendance\n&\nLeave", "1", R.drawable.attendence_icon, "", 0, ""));
//        mainMenuObjectList.add(new MainMenuObject(availableAppsList.get(), "2", R.drawable.tsr));
//        mainMenuObjectList.add(new MainMenuObject("Collector App", "3", R.drawable.collector_app));

        for (AvailableApps a : availableInstalled) {
            // if (!a.getAppId().equals("1") || !a.getAppId().equals("2") || !a.getAppId().equals("3")) {
            mainMenuObjectList.add(new MainMenuObject(
                    a.getAppName(),
                    a.getAppId(),
                    CommonHelperClass.getApplicationIcon(getActivity(),
                    a.getIconName()),
                    a.getPackagename(),
                    a.getUpdateAvailable(),
                    a.getIconUrl()));
            //}
            System.out.println("********");
            System.out.println("* "+a);
            System.out.println("********");
        }


        try {
            MainMenuAdapter mainMenuAdapter = new MainMenuAdapter(getActivity(), mainMenuObjectList, this, "");
            mRecycleView.setAdapter(mainMenuAdapter);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    @SuppressLint("SetTextI18n")
    private void showAlertDialog(int leave_attendence_icons) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        View layoutView = getLayoutInflater().inflate(leave_attendence_icons, null);
        dialogBuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        LinearLayout ic_attendance = layoutView.findViewById(R.id.ic_attendance);
        LinearLayout ic_leave = layoutView.findViewById(R.id.ic_leave);

        dialogBuilder.setView(layoutView);
        dialogBuilder.show();

        ic_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                Intent intetn = new Intent(getActivity(), AttendenceActivity.class);
                startActivity(intetn);
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

            }
        });
        ic_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                Intent intetn = new Intent(getActivity(), LeaveMainFragment.class);
                startActivity(intetn);
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });
    }

    private void init() {
        mTilte.setText("Lanka Bell Applications");


//        try {
//
//            sentSmsBroadcastCome = new BroadcastReceiver() {
//
//                @Override
//                public void onReceive(Context context, final Intent intent) {
//                    // Toast.makeText(context, "Get Location", Toast.LENGTH_SHORT).show();
//
//                    // mCheckTracking.setVisibility(View.VISIBLE);
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    }, 1000);
//
//                }
//            };
//            IntentFilter filterSend = new IntentFilter();
//            filterSend.addAction("m.getLoc");
//            getActivity().registerReceiver(sentSmsBroadcastCome, filterSend);
//        } catch (Exception e) {
//            System.out.println(e);
//        }

        syncAllAttendence();


        coloredSnackbar = new ColoredSnackbar(getActivity());
        downloadTask = new DownloadTask(getActivity());


        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask = new DownloadTask(getActivity());
                downloadTask.cancel(true);
            }
        });
        recycleViewLoad();

        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });


//        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener(){
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
//                if (dy > 0 ||dy<0 && fab.isShown())
//                    fab.hide();
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE){
//                    fab.show();
//                }
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });

    }

    private void syncAllAttendence() {

        Attendence attendence = new Attendence();

        List<Attendence> attendenceList = new ArrayList<>();

        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

        if (!attendenceList.isEmpty()) {
            if (attendenceList.get(0).getSyncTimeIn().equals("")) {
                SyncAttendence(attendenceList.get(0).getAttendenceIn(), Constants.IN, attendenceList.get(0).getTeamCode(), attendenceList.get(0).getMeterValue1(), attendenceList.get(0).getBikeNumber(),attendenceList.get(0).getRallType());
            }
            if(!attendenceList.get(0).getAttendenceOut().equals("")) {
                if (attendenceList.get(0).getSyncTimeOut().equals("")) {
                    SyncAttendence(attendenceList.get(0).getAttendenceOut(), Constants.OUT, attendenceList.get(0).getTeamCode(), attendenceList.get(0).getMeterValue2(), attendenceList.get(0).getBikeNumber(), attendenceList.get(0).getRallType());
                }
            }
        }
    }

    private void recycleViewLoad() {
        try {
            mRecycleView.setHasFixedSize(true);
            mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecycleView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
            mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
                //throw new PackageManager.NameNotFoundException();
            }
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public void onClickSingleItem(MainMenuObject selectedItem, int position) {

        //coloredSnackbar.showSnackBar("Testing", coloredSnackbar.TYPE_OK, 3000);
        currentSelected = selectedItem;
        try {
            System.out.println(selectedItem.itemKey);

            if (position == 0) {
               // showAlertDialog(R.layout.leave_attendence_icons);
                Intent intetn = new Intent(getActivity(), AttendenceActivity.class);
                startActivity(intetn);
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            } else {

                if (selectedItem.updateAvailable == 1) {
                    new UpdateAppsPopup(getActivity(), this).ShowConfirmation(1, "");
                } else {
                    openSelectedApp();
                }
            }


        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public void onLongClickSingleItem(MainMenuObject selectedItem, int position) {
        //Toast.makeText(getActivity(), selectedItem.itemText, Toast.LENGTH_SHORT).show();

        final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        MainMenuOptionspopup mainMenuOptionspopup = new MainMenuOptionspopup(getActivity(), HomeFragment.this, layoutInflater, "");
        mainMenuOptionspopup.makeDialog("", selectedItem);

    }

//    private void itemClicEvents(){
//        ItemClickSupport.addTo(mRecycleView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
//
//
//                return true;
//            }
//
//
//        });
//    }

    private void openSelectedApp() {
        System.out.println("* currentSelected.itemKey "+currentSelected.itemKey);
        System.out.println("* currentSelected.packagename "+currentSelected.packagename);
        switch (currentSelected.itemKey) {
            case "0":
                System.out.println("* 0 ");
                break;


            default:
                System.out.println("* default ");
                //openApp(getActivity(), selectedItem.packagename);

                Intent intent2 = new Intent(currentSelected.packagename);



                UserProfile userProfile1 = new UserProfile();
                TSRDetails tsrDetails = new TSRDetails();
                try {

                    intent2.putExtra("package", getActivity().getPackageName());
                    intent2.putExtra("username", userProfile1.getAllUsers().get(0).getUserName());
                    intent2.putExtra("pword", userProfile1.getAllUsers().get(0).getPassword());
                    intent2.putExtra("SimNo", userProfile1.getAllUsers().get(0).getSimNo());
                    intent2.putExtra("epf", tsrDetails.getAllData().get(0).getEpfNo());
                    intent2.putExtra("CollectorCode", userProfile1.getAllUsers().get(0).getCollectorCode());
                    intent2.putExtra("attendenceIn", userProfile1.getAllUsers().get(0).getAttendencIn());
                    intent2.putExtra("attendenceOut", userProfile1.getAllUsers().get(0).getAttendencOut());
                }catch (Exception e){

                    e.printStackTrace();
                }
                //try{
//
//            List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);
//
//            for (int i = 0; i < packs.size(); i++) {
//                PackageInfo p = packs.get(i);
////                if ((isSystemPackage(p) == false)) {
//                System.out.println("* packageName "+p.packageName);//com.lk.lankabell.android.activity.tsr
//                    String appName = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
//                   // System.out.println("* appName "+appName);
////                    Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
////                    res.add(new AppList(appName, icon));
////                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

                try {
                    startActivity(intent2);
                }catch (Exception e){
                    System.out.println("* Intent Exception "+e.getCause()+" : "+e.getMessage()+" : "+e.getLocalizedMessage());
                    e.printStackTrace();
                }
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);


                break;
        }
    }

    @Override
    public void onClickSingleItem(SingleItemModel selectedItem, String tag, int position) {

    }

    @Override
    public void onClickOk(List<SingleItemModel> list) {

    }

    public void doUpdateApps() {
        currentAppName = currentSelected.itemText;
        mProgressDialog.setMessage("Updating " + currentAppName);
        mProgressDialog.show();
        try {
            AvailableApps availableApps = new AvailableApps();
            List<AvailableApps> availableAppsList = new ArrayList<>();
            availableAppsList = availableApps.getFromAppId(currentSelected.itemKey);
            downloadTask = new DownloadTask(getActivity());
            downloadTask.execute("http://" + availableAppsList.get(0).getUrl().trim());
        } catch (Exception e) {
            System.out.println(e);
            Toast.makeText(getActivity(), "Check the internet connection..."+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void dialogResult(Context c, int i) {

        switch (i) {
            case 1:
                new AppResetConfirmation(c).ShowConfirmation(1, "Options");

                break;
        }

    }

    @Override
    public void onSuccess() {
        //addApplications();
        AvailableApps availableApps = new AvailableApps();
        availableAllApps = availableApps.getAllApps();
        checkAppsAvailability();
        setAvailableAppLable();
        updateApplications();
        setAdapter();
    }

    @Override
    public void onFailed() {
        if (new NetworkCheck().NetworkConnectionCheck(getActivity())) {
            callFetach();
        } else {
            coloredSnackbar.showSnackBar("Check your internet connection...", coloredSnackbar.TYPE_ERROR, 2000);
        }
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
        }
    }

    @Override
    public void getCustomerListFaile(String message) {
        if (new NetworkCheck().NetworkConnectionCheck(getActivity())) {
            coloredSnackbar.showSnackBar("Pleas Refresh...", coloredSnackbar.TYPE_WARING, 2000);
        } else {
            coloredSnackbar.showSnackBar("Check your internet connection...", coloredSnackbar.TYPE_ERROR, 2000);
        }

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
        }

    }

    @Override
    public void getAccountsListFaile(String message) {
        if (new NetworkCheck().NetworkConnectionCheck(getActivity())) {
            //downloadAccounts();
            coloredSnackbar.showSnackBar("Please Refresh...", coloredSnackbar.TYPE_WARING, 2000);
        } else {
            coloredSnackbar.showSnackBar("Check your internet connection...", coloredSnackbar.TYPE_ERROR, 2000);
        }
    }

    @Override
    public void getCitiesSuccess(String update) {
        try {
            cityObject = new JSONArray(update);
            MerchantsCities merchantsCities = new MerchantsCities();
            merchantsCities.clearTable();

            new CitiesSaving().execute("");

            System.out.println("===============================");
        } catch (JSONException e) {
            e.printStackTrace();
            //checkUpdateStatus();
        }
    }

    @Override
    public void getCitiesFaile(String message) {
        //checkUpdateStatus();
        if (new NetworkCheck().NetworkConnectionCheck(getActivity())) {
            //downloadAccounts();
            coloredSnackbar.showSnackBar("Please Refresh...", coloredSnackbar.TYPE_WARING, 2000);
        } else {
            coloredSnackbar.showSnackBar("Check your internet connection...", coloredSnackbar.TYPE_ERROR, 2000);
        }
    }

//    public class checkUpdateAyncTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//        }
//    }

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


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void getTSRDetailsFaile(String message) {
        //>

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

        }
    }

    @Override
    public void getMerchantsListFaile(String message) {

    }

    @Override
    public void onButtonClcik(int x) {
        if (x == 1) {
            doUpdateApps();
        } else {
            openSelectedApp();
        }
    }

    @Override
    public void onClickSingleItem(MainMenuObject selectedItem, String tag, int position) {
        ((MainActivity) getActivity()).longpressResuls(selectedItem, tag);
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

        }
    }

    @Override
    public void getLocationRegistrationFeatchFaile(String message) {

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

            System.out.println("Location Register details ok");

        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {


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
                LocationRegisterSync locationRegisterSync = new LocationRegisterSync(HomeFragment.this);
                locationRegisterSync.LocationRegister(userProfile.getAllUsers().get(0).getUserName(), userProfile.getAllUsers().get(0).getPassword(),"1" ,l.getCusCode(),userProfile.getAllUsers().get(0).getUserName(), String.valueOf(l.getLongi()),String.valueOf(l.getLati()), userProfile.getAllUsers().get(0).getUserName(),l.getRegisteredDate(),l.getProvince(),l.getDistrict(),l.getCity(),"",l.getIsConformed());
            }


            return "";
        }


        @Override
        protected void onPostExecute(String result) {
           // ++successCount;
            System.out.println("Account details ok");
           // checkUpdateStatus();
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

                    merchants.save();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            return "";
        }


        @Override
        protected void onPostExecute(String result) {

        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {


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
                ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null) {
                Toast.makeText(context, "Check the internet connection... ", Toast.LENGTH_LONG).show();
            } else {
                installApk();
            }
            //Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void installApk() {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri uri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/")+currentAppName+".apk"));
//        intent.setDataAndType(uri, "application/vnd.android.package-archive");
//        mContext.startActivity(intent);

//        Intent promptInstall = new Intent(Intent.ACTION_VIEW)
//                .setDataAndType(Uri.parse("file://"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)),
//                        "application/vnd.android.package-archive");
//        mContext.startActivity(promptInstall);

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Uri uri = Uri.parse("file://"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)); // a directory
//        intent.setDataAndType(uri, "*/*");
//        mContext.startActivity(Intent.createChooser(intent, "Open folder"));

        //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
//                    + Environment.DIRECTORY_DOWNLOADS+"/");
//            intent.setDataAndType(uri,"file");
//            mContext.startActivity(Intent.createChooser(intent, "Open folder"));


//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse("file://"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+currentAppName+".apk"),
//                        "application/vnd.android.package-archive");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
//        mContext.startActivity(intent);

        // mContext.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

//        Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() +  "/Local/Internal storage/Download");
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setDataAndType(selectedUri , "text/csv");
//
//        if(intent.resolveActivityInfo(mContext.getPackageManager(),0)!=null){
//            mContext.startActivity(Intent.createChooser(intent, "Open folder"));
//        }else{
//            Toast.makeText(mContext, "Go to the Downloads Folder to Install the app", Toast.LENGTH_LONG).show();
//        }
        Notification.Builder notification = new Notification.Builder(getActivity());
//        Intent install_intent = new Intent(Intent.ACTION_VIEW);
//        install_intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + currentAppName + ".apk")), "application/vnd.android.package-archive");
//        getActivity().startActivity(install_intent);


        File toInstall = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + currentAppName + ".apk");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", toInstall);
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getActivity().startActivity(intent);
            //mListDialog.dismiss();
        } else {
            Uri apkUri = Uri.fromFile(toInstall);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
            //mListDialog.dismiss();
        }


        //mListDialog.dismiss();


//        PendingIntent pending = PendingIntent.getActivity(mContext,0, install_intent, 0);
//
//        NotificationManager nm = (NotificationManager) mContext
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notification.setContentIntent(pending)
//                .setSmallIcon(R.drawable.collector_app)
//                .setWhen(System.currentTimeMillis())
//                .setAutoCancel(true)
//                .setContentTitle(currentAppName)
//                .setContentText(currentAppName);
//        Notification n = notification.build();
//
//        nm.notify(1, n);

        //((MainActivity) mContext).showNotification("",0,"");


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

        }


        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    public void downloadCities() {
        UserProfile userProfile = new UserProfile();
        GetMerchantsCitiesSync getMerchantsCitiesSync = new GetMerchantsCitiesSync(HomeFragment.this);
        getMerchantsCitiesSync.setAttendence(userProfile.getAllUsers().get(0).getUserName());
    }

    public void CallFromActivity() {
        AvailableApps availableApps = new AvailableApps();
        availableAllApps = availableApps.getAllApps();
        checkAppsAvailability();
        setAvailableAppLable();
        updateApplications();
        setAdapter();
    }

    public void SyncAttendence(String date, String type, String temCode, String meterValue, String bikeCode, String category) {

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)getContext(),
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.SEND_SMS},
                        1200);
            }
        }
        UserProfile userProfile = new UserProfile();
        List<UserProfile> userProfileList = new ArrayList<>();

        userProfileList = userProfile.getAllUsers();

        AttendenctSync attendenctSync = new AttendenctSync(this);
        String serial_number = Constants.SERIAL_NUMBER;
        System.out.println("Serial number "+serial_number);
        if(serial_number.equals("") ){
            attendenctSync.setAttendence(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), date, type, Utils.getSimSerialNumber(getActivity()), temCode, meterValue, bikeCode, category);
        }else{
            attendenctSync.setAttendence(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), date, type, serial_number, temCode, meterValue, bikeCode, category);
        }


    }

    @Override
    public void getAttendenceSyncSuccess(String update, String type) {
        System.out.println(update);
        if (!update.equals("") && update != null) {

            if(update.equals("1")) {
                if (type.equals(Constants.IN)) {
                    Attendence attendence = new Attendence();

                    attendence.updateSyncIn(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy HH:mm:ss"), TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
                } else if (type.equals(Constants.OUT)) {
                    Attendence attendence = new Attendence();

                    attendence.updateSyncOut(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy HH:mm:ss"), TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

                }
            }
        }
    }

    @Override
    public void getAttendenceSyncFail(String message, String type) {

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

    }


}
