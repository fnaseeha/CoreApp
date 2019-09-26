package com.example.user.lankabellapps.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.helper.Alert;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.helper.DateTimeSelectView;
import com.example.user.lankabellapps.helper.NetworkCheck;
import com.example.user.lankabellapps.helper.TimeFormatter;
import com.example.user.lankabellapps.helper.Utils;
import com.example.user.lankabellapps.models.Attendence;
import com.example.user.lankabellapps.models.TSRDetails;
import com.example.user.lankabellapps.models.TrackingData;
import com.example.user.lankabellapps.models.UserProfile;
import com.example.user.lankabellapps.popups.AppResetConfirmation;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.backgroundservices.SampleTrackingService;
import com.example.user.lankabellapps.services.sync.AttendenctSync;
import com.example.user.lankabellapps.services.sync.CheckMyAttendneceSync;
import com.example.user.lankabellapps.services.sync.GetTotalGpsCountSync;
import com.example.user.lankabellapps.services.sync.LeaveSync;
import com.example.user.lankabellapps.sms.ISMSCallbacks;
import com.example.user.lankabellapps.sms.SMSManager;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Attendece add in this activity,
 * Until the user mark the IN user can not mark OUT
 * One attendece raw add for the one date in Attendece table.
 * <p/>
 * <p/>
 * Have to create... Tracking and other background threads shoud start when the mark of IN ,
 * When OUT mark all the threads should stop as the requirement.
 */
public class  AttendenceActivity extends AppCompatActivity implements AttendenctSync.GetAttendenceEvents, ISMSCallbacks, CheckMyAttendneceSync.GetAttendenceEvents,LeaveSync.GetLeaveEvents, GetTotalGpsCountSync.GetTotalGps {


    @Bind(R.id.ll_text_view)
    LinearLayout mEditTextLayout;

    @Bind(R.id.LeaveRemark)
    EditText leaveremark;

    @Bind(R.id.sp_rall)
    Spinner mRall;

    @Bind(R.id.sp_rall3)
    Spinner mRall3;

    @Bind (R.id.sp_rall2)
    Spinner mRall2;


    @Bind(R.id.btn_attendence_submit)
    Button mSubmit;

    @Bind(R.id.btn_check_my_attendence)
    Button mCheckAttendence;

    @Bind(R.id.tv_attendence_team_code)
    EditText mTeamCode;

    @Bind(R.id.tv_attendence_bike_number)
    EditText mBikeNumber;

    @Bind(R.id.tv_attendence_meter_value)
    EditText mMeterValue;

    @Bind(R.id.tv_check_my_attendence_in)
    TextView mCheckMyAttendenceIn;

    @Bind(R.id.tv_check_my_attendence_out)
    TextView mCheckMyAttendenceOut;

    @Bind(R.id.btn_attendence_toggle)
    Button mAttendenceTogle;

    @Bind(R.id.tv_title)
    TextView mTitle;


    @Bind(R.id.iv_menu)
    ImageView mBack;

    @Bind(R.id.ll_attendence_background)
    LinearLayout mBackground;

    @Bind(R.id.ll_support_layout)
    LinearLayout mSupportLayout;



    public static TextView unsyncCount;
    public static TextView totalGpsCount;

    String[] arraySpinner;
    String[] arraySpinner2;
    String[] arraySpinner3;



    String toggleStatus = Constants.IN;

    UserProfile userProfile;

    List<UserProfile> userProfileList = new ArrayList<>();

    ColoredSnackbar coloredSnackbar;

    int appNameStatues = 1, currenttype = 0;
    int appNameStatues2 = 1;
    int appNameStatues3 = 1;


    private SMSManager mSMS;

    private String team_sms_id = "123456789";

    private String msg_team;
    private String msg;

    WifiConfiguration wifiConfig;

    ProgressDialog mCommonProgressDialog;

    String ssid = "BellsolutionsIT", pword = "BellSol@123";

    //TimerTask hourlyTask;
    //Timer timer;

    ProgressDialog dialog;

    TextInputLayout MeterValueLayout,BikeCodeLayout,TeamCodeLayout;
    Alert alert_loc;
    GetTotalGpsCountSync getTotalGpsCountSync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);

        ButterKnife.bind(this);
        unsyncCount = findViewById(R.id.unsyncGpsCount);
        totalGpsCount = findViewById(R.id.totalGpsCount);
        coloredSnackbar = new ColoredSnackbar(this);
        getTotalGpsCountSync = new GetTotalGpsCountSync(this);
        userProfile = new UserProfile();

        userProfileList = userProfile.getAllUsers();
        init();
        mTitle.setText("Attendance");
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        List<TrackingData> unSyncCounts = new TrackingData().getTrackingDataByDateOnly(DateTimeSelectView.getCurrentTimeOnly());// getTrackingDataByDate


        if(unSyncCounts.size()>0){
            unsyncCount.setVisibility(View.VISIBLE);
        }else{
            unsyncCount.setVisibility(View.GONE);
        }
        unsyncCount.setText("UnSynced GPS count : "+unSyncCounts.size());

        if(new NetworkCheck().NetworkConnectionCheck(this)){
            if(userProfileList.size()>0){
                getTotalGpsCountSync.getTotalGps(userProfileList.get(0).getUserName());
            }
        }

        if(ViewableDate()){
            totalGpsCount.setVisibility(View.VISIBLE);
        }else{
            totalGpsCount.setVisibility(View.GONE);
        }

        alert_loc = new Alert(AttendenceActivity.this);

        CheckGPS();

//        wifiManager.setWifiEnabled(true);
    }


    public boolean contains(int[] array, int key) {
        Arrays.sort(array);
        return Arrays.binarySearch(array, key) >= 0;
    }


    private boolean ViewableDate() {

        /*QA Nilhan brother said,  If attendance marked date = 10,15,21,22,24,29,30 show tracking points..*/
        int dates[] = {10,15,21,22,24,29,30};

        for(int d:dates){
            if(d==DateTimeSelectView.getTodayDate()){
                return true;
            }
        }

        return false;
    }

    private void init() {


        MeterValueLayout = findViewById(R.id.MeterValueLayout);
        BikeCodeLayout = findViewById(R.id.BikeCodeLayout);
        TeamCodeLayout = findViewById(R.id.TeamCodeLayout);

        mMeterValue.addTextChangedListener(new MyTextWatcher(mMeterValue));
        mBikeNumber.addTextChangedListener(new MyTextWatcher(mBikeNumber));
        mTeamCode.addTextChangedListener(new MyTextWatcher(mTeamCode));

        this.arraySpinner = new String[]{
                "Card Sales Attendance", "Other Attendance" ,"Leave"
        };

        mSMS = new SMSManager(this, this);

        // RA get app names from the table

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        mRall.setAdapter(adapter);
        mRall.setPrompt("Select a Rall");

        mBack.setImageResource(R.drawable.back);

        mRall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                checkAttendencStatus();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        this.arraySpinner2 = new String[]{
                "Medical", "Casual" ,"Annual"
        };

        //mSMS = new SMSManager(this, this);

        // RA get app names from the table

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner2);
        mRall2.setAdapter(adapter2);
        mRall2.setPrompt("Select a Rall");

        mBack.setImageResource(R.drawable.back);

        mRall2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                checkLeaveStatus();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        this.arraySpinner3 = new String[]{
                "Full Day","Morning Half Day", "Evening Half Day"
        };

        //mSMS = new SMSManager(this, this);

        // RA get app names from the table

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner3);
        mRall3.setAdapter(adapter3);
        mRall3.setPrompt("Select a Rall");

        mBack.setImageResource(R.drawable.back);

        mRall2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                checkLeaveDayType();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        if (mRall.getSelectedItem().toString().equals("Card Sales Attendance")) {
            mEditTextLayout.setVisibility(View.VISIBLE);
            mSupportLayout.setVisibility(View.GONE);
            appNameStatues = 1;

        } else if (mRall.getSelectedItem().toString().equals("Other Attendance")){
            mEditTextLayout.setVisibility(View.GONE);
            mSupportLayout.setVisibility(View.GONE);
            appNameStatues = 2;
        }
        else if (mRall.getSelectedItem().toString().equals("Leave")){
            mEditTextLayout.setVisibility(View.GONE);
            mSupportLayout.setVisibility(View.VISIBLE);
            mAttendenceTogle.setVisibility(View.GONE);
            appNameStatues = 3;
            //MSpinner.setVisibility(View.GONE);
            mRall.setVisibility(View.GONE);
        }

        //mMobileTime.setText(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "hh:mm a"));


        Attendence attendence = new Attendence();

        List<Attendence> attendenceList = new ArrayList<>();


        String today = TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            cal.setTime(sdf.parse(today));// all done
            cal.add(Calendar.DATE, -1);
        } catch (ParseException e) {
            e.printStackTrace();


        }

        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(cal.getTimeInMillis(), "dd/MM/yyyy"));

        if (!attendenceList.isEmpty()) {
            //mMeterValue.setText(attendenceList.get(0).getMeterValue());
            mBikeNumber.setText(attendenceList.get(0).getBikeNumber());
            mTeamCode.setText(attendenceList.get(0).getTeamCode());
        }


        mCommonProgressDialog = new ProgressDialog(this);
        // Set your ProgressBar Message
        mCommonProgressDialog.setMessage("Please wait...");
        mCommonProgressDialog.setIndeterminate(false);
        mCommonProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mCommonProgressDialog.setCancelable(false);


    }

    private void checkAttendencStatus() {

        if (mRall.getSelectedItem().toString().equals("Card Sales Attendance")) {
            mEditTextLayout.setVisibility(View.VISIBLE);
            mSupportLayout.setVisibility(View.GONE);
            appNameStatues = 1;

        } else if (mRall.getSelectedItem().toString().equals("Other Attendance")){
            mEditTextLayout.setVisibility(View.GONE);
            mSupportLayout.setVisibility(View.GONE);
            appNameStatues = 2;
        }

        else if (mRall.getSelectedItem().toString().equals("Leave")){
            mEditTextLayout.setVisibility(View.GONE);
            mSupportLayout.setVisibility(View.VISIBLE);
            mAttendenceTogle.setVisibility(View.GONE);
            appNameStatues = 3;
            //MSpinner.setVisibility(View.GONE);
            mRall.setVisibility(View.GONE);
        }
    }

    private void checkLeaveStatus() {

        if (mRall2.getSelectedItem().toString().equals("Medical")) {
            appNameStatues2 = 1;

        } else if (mRall2.getSelectedItem().toString().equals("Casual")){
            appNameStatues2 = 2;

        }
        else if (mRall2.getSelectedItem().toString().equals("Annual")){
            appNameStatues2 = 3;

        }

        else {

        }


    }

    private void checkLeaveDayType() {

        if (mRall3.getSelectedItem().toString().equals("Full Day")) {
            appNameStatues3 = 1;

        } else if (mRall3.getSelectedItem().toString().equals("Morning Half Day")){
            appNameStatues3 = 2;

        }
        else if (mRall3.getSelectedItem().toString().equals("Evening Half Day")){
            appNameStatues3 = 3;

        }

        else {

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            CheckGPS();
            Attendence attendence = new Attendence();

            if(new NetworkCheck().NetworkConnectionCheck(this)){
                if(userProfileList.size()>0){
                    getTotalGpsCountSync.getTotalGps(userProfileList.get(0).getUserName());
                }
            }

            List<Attendence> attendenceList = new ArrayList<>();

            attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

            if (attendenceList.isEmpty()) {
                toggleStatus = Constants.IN;
                mAttendenceTogle.setBackgroundResource(R.drawable.attendence_in);
                //   mBackground.setBackgroundResource(R.drawable.attendence_background_in);
                mAttendenceTogle.setText("IN");
            } else {
                if (attendenceList.get(0).getAttendenceOut().equals(""))
                {
                    if(attendenceList.get(0).getRallType()!= null){
                        if(attendenceList.get(0).getRallType().equals("")){
                            setRallStatus(- 1);
                        }else{
                            setRallStatus(Integer.parseInt(attendenceList.get(0).getRallType()) - 1);
                        }
                    }

                }



                if (attendenceList.get(0).getAttendenceIn().equals("")) {
                    toggleStatus = Constants.IN;
                    mAttendenceTogle.setBackgroundResource(R.drawable.attendence_in);
                    //   mBackground.setBackgroundResource(R.drawable.attendence_background_in);
                    mAttendenceTogle.setText("IN");


                } else {
                    toggleStatus = Constants.OUT;
                    mAttendenceTogle.setBackgroundResource(R.drawable.attendence_out);
                    // mBackground.setBackgroundResource(R.drawable.attendence_background_out);
                    mAttendenceTogle.setText("OUT");

                    mBikeNumber.setText(attendenceList.get(0).getBikeNumber());
                    mTeamCode.setText(attendenceList.get(0).getTeamCode());


                    if (!attendenceList.get(0).getAttendenceIn().equals("") && attendenceList.get(0).getAttendenceOut().equals("") && attendenceList.get(0).getSyncTimeIn().equals("")) {
                        //SyncAttendence(attendenceList.get(0).getAttendenceIn(), Constants.IN);
                    } else if (!attendenceList.get(0).getAttendenceIn().equals("") && !attendenceList.get(0).getAttendenceOut().equals("") && attendenceList.get(0).getSyncTimeOut().equals("")) {
                        //SyncAttendence(attendenceList.get(0).getAttendenceOut(), Constants.OUT);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setRallStatus(int i) {
        mRall.setSelection(i);
        mRall.setEnabled(false);
        checkAttendencStatus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @OnClick(R.id.iv_menu)
    public void OnClickBack(View view) {
        onBackPressed();
    }


    @OnClick(R.id.btn_attendence_submit)
    public void OnClickSubmit(View view) {

        switch (appNameStatues) {

            case 1:
                CheckGPS();
                if (mTeamCode.getText().toString().equals("") || mBikeNumber.getText().toString().equals("") || mMeterValue.getText().toString().equals("")) {
                    coloredSnackbar.showSnackBar("Fill all fields", coloredSnackbar.TYPE_ERROR, 2000);
                }else{

                    if (!validateText(mTeamCode,"Team Code ",TeamCodeLayout)) {
                        return;
                    }

                    if (!validateText(mBikeNumber,"Bike Number ",BikeCodeLayout)) {
                        return;
                    }

                    if (!validateMeterText(mMeterValue,"Meter Value ",MeterValueLayout)) {
                        return;
                    }

                    System.out.println("###########################################3");
                    attendenceMark();
                }


               /* if (mTeamCode.getText().toString().equals("") || mBikeNumber.getText().toString().equals("") || mMeterValue.getText().toString().equals("")) {
                    coloredSnackbar.showSnackBar("Fill all fields", coloredSnackbar.TYPE_ERROR, 2000);
                } else {

                    System.out.println("###########################################3");
                    attendenceMark();
                }*/
                break;


            default:
                System.out.println("###########################################2");

                attendenceMark();
                break;

        }

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

    public void showLocationAlert(String message) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(AttendenceActivity.this);
        dialog.setTitle("Enable Location")
                .setMessage(message)
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }
    private boolean CheckGPS() {

        try {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            System.out.println("* provider " + provider);
            // int j = getLocationMode(getApplicationContext());

            if (provider.equalsIgnoreCase("gps")) {
                alert_loc.showLocationAlert("Your Location Mode is not High priority.\nPlease Set Location to " +
                        "High Priority");
                return false;
            } else if (provider.equalsIgnoreCase("network")) {
                alert_loc.showLocationAlert("Your Location Mode is not High priority.\nPlease Set Location to " +
                        "High Priority");
                return false;
            } else if (provider.contains("network,gps")||provider.contains("gps,network")) {
                System.out.println("* gps is enable");
                return true;
            } else {
                alert_loc.showLocationAlert("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void getTotalGpsSuccess(GetCommonResponseModel update) {
        if(update.getStatus().equals("1")){
            if(update.getData().equals("")){
                totalGpsCount.setText("Monthly Gps count up to Today : 0");
            }else{
                totalGpsCount.setText("Monthly Gps count up to Today : "+update.getData());
            }
        }
    }

    @Override
    public void getTotalGpsFailed(String message) {
        System.out.println("get total Error");
        totalGpsCount.setText("Monthly Gps count up to Today  : 0");
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.tv_attendence_team_code:
                    //validateText(EditText text,String name,TextInputLayout layout)
                    validateText(mTeamCode,"Team Code ",TeamCodeLayout);
                    break;
                case R.id.tv_attendence_bike_number:
                    validateText(mBikeNumber,"Bike Number ",BikeCodeLayout);
                    break;
                case R.id.tv_attendence_meter_value:
                    validateMeterText(mMeterValue,"Meter Value ",MeterValueLayout);
                    break;
            }
        }

    }

    private boolean validateMeterText(EditText text, String name, TextInputLayout layout) {
        String txt = text.getText().toString().trim();

        if (txt.isEmpty()) {
            layout.setError(name +" is Empty");
            requestFocus(layout);
            return false;
        } else if (!isValidMeter(txt)) {
            layout.setError(name + " is Invalid");
            requestFocus(layout);
            return false;
        } else {
            layout.setErrorEnabled(false);
            return true;
        }

    }

    //  //MeterValueLayout,BikeCodeLayout,TeamCodeLayout;
    public boolean isValid(String editTextInput) {
        boolean isInputValid = false;

        if (editTextInput.equals("")) {
            return isInputValid;
        }else{

            String expression = "[a-zA-Z0-9\\s-]{1,20}";
            CharSequence inputStr = editTextInput;

            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches()) {
                isInputValid = true;
            }
            return isInputValid;
         }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateText(EditText text,String name,TextInputLayout layout) {
        String txt = text.getText().toString().trim();

        if (txt.isEmpty()) {
            layout.setError(name +" is Empty");
            requestFocus(layout);
            return false;
        } else if (!isValid(name)) {
            layout.setError(name + " is Invalid");
            requestFocus(layout);
            return false;
        } else {
            layout.setErrorEnabled(false);
            return true;
        }
    }

    public boolean isValidMeter(String editTextInput) {
        boolean isInputValid = false;

        if (editTextInput.equals("")) {
            return isInputValid;
        }else{
            String expression = "[0-9.\\s]{1,20}";
            CharSequence inputStr = editTextInput;

            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches()) {
                isInputValid = true;
            }
            return isInputValid;
         }
    }



    private void connectToSSID(final WifiManager wifiManager, final String ssid, final int type) {

        wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.preSharedKey = String.format("\"%s\"", pword);

        //       wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//remember id
        final int[] check = {0};
        final int[] netId = {0};
//        wifiManager.setWifiEnabled(true);
        List<WifiConfiguration> wifiArray = new ArrayList<>();
        wifiArray = wifiManager.getConfiguredNetworks();

        if (wifiArray != null) {
            for (WifiConfiguration wifiConfiguration : wifiArray) {
                //System.out.println(wifiConfiguration.SSID);
                //System.out.println(wifiConfiguration.BSSID);
                //  System.out.println(wifiConfiguration.preSharedKey);
                if (wifiConfiguration.SSID.equals(String.format("\"%s\"", ssid))) {
                    ++check[0];
                    netId[0] = wifiConfiguration.networkId;
                }
            }

            if (check[0] == 0) {
                netId[0] = wifiManager.addNetwork(wifiConfig);
            }

            wifiManager.disconnect();
            wifiManager.enableNetwork(netId[0], true);
            currenttype = type;
            wifiManager.reconnect();
        } else {

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    List<WifiConfiguration> wifiArray = new ArrayList<>();
                    wifiArray = wifiManager.getConfiguredNetworks();

                    for (WifiConfiguration wifiConfiguration : wifiArray) {
                        //System.out.println(wifiConfiguration.SSID);
                        //System.out.println(wifiConfiguration.BSSID);
                        //  System.out.println(wifiConfiguration.preSharedKey);
                        if (wifiConfiguration.SSID.equals(String.format("\"%s\"", ssid))) {
                            ++check[0];
                            netId[0] = wifiConfiguration.networkId;
                        }
                    }

                    if (check[0] == 0) {
                        netId[0] = wifiManager.addNetwork(wifiConfig);
                    }

                    wifiManager.disconnect();
                    wifiManager.enableNetwork(netId[0], true);
                    currenttype = type;
                    wifiManager.reconnect();


                }
            }, 4000);


            // coloredSnackbar.showSnackBar("Try Again...", coloredSnackbar.TYPE_WARING, 2000);
        }

    }

    private BroadcastReceiver myWifiStateListener = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                ConnectivityManager cm = (ConnectivityManager) getSystemService(
                        Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED && wifiNetwork.isConnected()) {

                    System.out.println(wifiConfig.SSID);


                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo info = wifiManager.getConnectionInfo();
                    String ssidReal = info.getSSID();


                    if (ssidReal.equals(String.format("\"%s\"", ssid))) {
                        //hourlyTask.cancel();
                        // timer.cancel();

                        System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");

                        //System.out.println("Connected to the BellsolutionsIT");
                        //AttendenceActivity.this.unregisterReceiver(myWifiStateListener);
                        //killWifiNetwork();
                        switch (currenttype) {
                            case 1:

                                Attendence attendence = new Attendence();

                                List<Attendence> attendenceList = new ArrayList<>();

                                attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

                                if (attendenceList.isEmpty()) {
                                    coloredSnackbar.showSnackBar("You should mark your IN first", coloredSnackbar.TYPE_ERROR, 2000);
                                } else {
                                    if (attendenceList.get(0).getAttendenceIn().equals("")) {
                                        coloredSnackbar.showSnackBar("You should mark your IN first", coloredSnackbar.TYPE_ERROR, 2000);
                                    } else {

                                        if (attendenceList.get(0).getAttendenceOut().equals("")) {


                                            Attendence attendenceToSync = new Attendence();
                                            List<Attendence> attendenceListToSync = new ArrayList<>();
                                            attendenceListToSync = attendenceToSync.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));


                                            SyncAttendence(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "MM/dd/yyyy HH:mm:ss"), Constants.OUT, mTeamCode.getText().toString(), mMeterValue.getText().toString(), mBikeNumber.getText().toString(), String.valueOf(appNameStatues));


                                        } else {
                                            coloredSnackbar.showSnackBar("You have already marked OUT", coloredSnackbar.TYPE_ERROR, 2000);
                                        }
                                    }
                                }
                                break;

                            case 2:


                                Attendence attendenceToSync = new Attendence();
                                List<Attendence> attendenceListToSync = new ArrayList<>();
                                attendenceListToSync = attendenceToSync.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
                                SyncAttendence(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "MM/dd/yyyy HH:mm:ss"), Constants.IN, mTeamCode.getText().toString(), mMeterValue.getText().toString(), mBikeNumber.getText().toString(), String.valueOf(appNameStatues));

                                break;
                        }

                    }

                    //Log.d("Network", "Internet YAY");
                } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                    //Log.d("Network", "No internet :(");
                    System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                }
            }
//            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                System.out.println("ddddddddddddddddddddd");
//                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            System.out.println("cccccccccccccccccccccccccccc");

        }

    };


    @OnClick(R.id.btn_check_my_attendence)
    public void OnClickCheckMyAttendence(View view) {

//        int j = getLocationMode(getApplicationContext());
//        System.out.println("* location de "+j);

        CheckGPS();
        //sendAttendanceSMS();
        if (mRall.getSelectedItem().toString().equals("Leave"))
        {
            /*Attendence attendence = new Attendence();

            List<Attendence> attendenceList = new ArrayList<>();

            attendenceList = attendence.getLeaveByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));*/

            int Leavestts = Constants.LEAVE_STATUS;

            if (Leavestts == 0) {
                coloredSnackbar.showSnackBar("No Leave for today.", coloredSnackbar.TYPE_ERROR, 2000);
            }
            /*else if(Leavestts == 1){
                //coloredSnackbar.showSnackBar("No connection please retry", coloredSnackbar.TYPE_ERROR, 2000);
                coloredSnackbar.showSnackBar("No Leave Request For Today", coloredSnackbar.TYPE_ERROR, 2000);
            }*/
            else if(Leavestts == 1) {
            //coloredSnackbar.showSnackBar("No connection please retry", coloredSnackbar.TYPE_ERROR, 2000);
            coloredSnackbar.showSnackBar("You Have A Leave Today", coloredSnackbar.TYPE_ERROR, 2000);
        }
        }
        else {
            Attendence attendence = new Attendence();

            List<Attendence> attendenceList = new ArrayList<>();

            attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));


            if (!attendenceList.isEmpty()) {
                mCheckMyAttendenceIn.setText("IN " + attendenceList.get(0).getAttendenceIn());
                mCheckMyAttendenceOut.setText("OUT " + attendenceList.get(0).getAttendenceOut());

                System.out.println(attendenceList.get(0).getAttendenceIn());

                if (Constants.ATTENDANCE_STATUS_OUT.equalsIgnoreCase("NO")) {
                    if(Constants.ATTENDANCE_STATUS_IN.equalsIgnoreCase("NO")){
                        mCheckMyAttendenceIn.setTextColor(Color.RED);
                    }else if(Constants.ATTENDANCE_STATUS_IN.equalsIgnoreCase("OK")){
                        mCheckMyAttendenceIn.setTextColor(Color.GREEN);
                    }

					mCheckMyAttendenceOut.setTextColor(Color.RED);
                } else if (Constants.ATTENDANCE_STATUS_IN.equalsIgnoreCase("NO")) {
                    mCheckMyAttendenceIn.setTextColor(Color.RED);

                }  else {
                    mCheckMyAttendenceIn.setTextColor(Color.GREEN);
                    mCheckMyAttendenceOut.setTextColor(Color.GREEN);
                }
//            if(attendenceList.get(0).getSyncTimeOut().isEmpty()){
//                mCheckMyAttendenceOut.setTextColor(Color.RED);
//            }

            } else {
                coloredSnackbar.showSnackBar("No Attendance for today.", coloredSnackbar.TYPE_ERROR, 2000);
            }
        }
    }


    private void attendenceMark() {
        System.out.println("### inside attenedenceMark");


        try {
            CheckGPS();
            if( Constants.SERVER_TIME_COFIRM.equals("")){
                System.out.println(" server time is null");

                //server time is null so can't check time is valid or not

                if (mRall.getSelectedItem().toString().equals("Leave")){

                    new AppResetConfirmation(this).ShowConfirmation(11, Constants.ATTENDENCEACTIVITY);
                        /*Attendence attendence = new Attendence();

                        List<Attendence> attendenceList = new ArrayList<>();

                        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

                        if (attendenceList.isEmpty()) {


                            new AppResetConfirmation(this).ShowConfirmation(10, Constants.ATTENDENCEACTIVITY);
                        }

                        else
                            {
                                if (attendenceList.get(0).getAttendenceOut().equals(""))
                                {
                                    coloredSnackbar.showSnackBar("Please Mark OUT Before Mark Leave", coloredSnackbar.TYPE_ERROR, 2000);
                                }
                                else
                                    {
                                        new AppResetConfirmation(this).ShowConfirmation(10, Constants.ATTENDENCEACTIVITY);
                                    }

                            }*/

                } else {

                    markAttendence();

                }
            }else{
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sMobileDate = TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy-MM-dd HH:mm:ss");

                Date MobileDate = formatter.parse(sMobileDate);


                // Constants.SERVER_TIME_COFIRM = ServerTime;
                String sservertime = Constants.SERVER_TIME_COFIRM;

                Date servertime = formatter.parse(sservertime);

                DateTime phoneDate = new DateTime(MobileDate);
                DateTime serverDate = new DateTime(servertime);

                System.out.println("### sservertime "+sservertime);
                Integer diff = Minutes.minutesBetween(phoneDate, serverDate).getMinutes();
                Log.d("Get Phone date :", phoneDate.toString());
                long diffN = Math.abs(diff);
                System.out.println("### diffN "+diffN);
                if ((diffN < 50)) {


                    if (mRall.getSelectedItem().toString().equals("Leave")){

                        new AppResetConfirmation(this).ShowConfirmation(11, Constants.ATTENDENCEACTIVITY);
                        /*Attendence attendence = new Attendence();

                        List<Attendence> attendenceList = new ArrayList<>();

                        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

                        if (attendenceList.isEmpty()) {


                            new AppResetConfirmation(this).ShowConfirmation(10, Constants.ATTENDENCEACTIVITY);
                        }

                        else
                            {
                                if (attendenceList.get(0).getAttendenceOut().equals(""))
                                {
                                    coloredSnackbar.showSnackBar("Please Mark OUT Before Mark Leave", coloredSnackbar.TYPE_ERROR, 2000);
                                }
                                else
                                    {
                                        new AppResetConfirmation(this).ShowConfirmation(10, Constants.ATTENDENCEACTIVITY);
                                    }

                            }*/

                    } else {

                        markAttendence();

                    }

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

            }

        }catch (Exception e){
            Toast.makeText(this,"Server Time Error.",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            System.out.println(e);
        }



    }

    private void markAttendence() {
        if(new TSRDetails().getAllData().size()>0){

            msg = mTeamCode.getText().toString().trim() + " " + new TSRDetails().getAllData().get(0).getEpfNo().trim() + " " + "1";
            msg_team = mTeamCode.getText().toString().trim() + " " + mBikeNumber.getText().toString().trim() + " " + mMeterValue.getText().toString().trim() + " " + "1";

        }

        if (toggleStatus.equals(Constants.IN)) {
            Attendence attendence = new Attendence();

            List<Attendence> attendenceList = new ArrayList<>();

            attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

            if (attendenceList.isEmpty()) {


                new AppResetConfirmation(this).ShowConfirmation(3, Constants.ATTENDENCEACTIVITY);


            } else {
                coloredSnackbar.showSnackBar("You have already marked IN", coloredSnackbar.TYPE_ERROR, 2000);
            }
        } else {

            Attendence attendence = new Attendence();
            List<Attendence> attendenceList = new ArrayList<>();

            attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

            if (attendenceList.isEmpty()) {
                coloredSnackbar.showSnackBar("You should mark your IN first", coloredSnackbar.TYPE_ERROR, 2000);
            } else {
                if (attendenceList.get(0).getAttendenceOut().equals("")) {

                    new AppResetConfirmation(this).ShowConfirmation(2, Constants.ATTENDENCEACTIVITY);
                } else {
                    coloredSnackbar.showSnackBar("You have already marked OUT", coloredSnackbar.TYPE_ERROR, 2000);
                }
            }
        }
    }

    @OnClick(R.id.btn_attendence_toggle)
    public void OnClickToggle(View view) {
        switch (toggleStatus) {
            case Constants.IN:
                toggleStatus = Constants.OUT;
                mAttendenceTogle.setBackgroundResource(R.drawable.attendence_out);
                // mBackground.setBackgroundResource(R.drawable.attendence_background_out);
                mAttendenceTogle.setText("OUT");
                break;

            case Constants.OUT:
                toggleStatus = Constants.IN;
                mAttendenceTogle.setBackgroundResource(R.drawable.attendence_in);
                //   mBackground.setBackgroundResource(R.drawable.attendence_background_in);
                mAttendenceTogle.setText("IN");
                break;
        }
    }

    public void DialogConfermation(int i) {


        switch (mRall.getSelectedItemPosition()) {
            case 0:

                switch (i) {
                    case 1:

                        Attendence attendence = new Attendence();

                        List<Attendence> attendenceList = new ArrayList<>();

                        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

                        if (attendenceList.isEmpty()) {
                            coloredSnackbar.showSnackBar("You should mark your IN first", coloredSnackbar.TYPE_ERROR, 2000);
                        } else {
                            if (attendenceList.get(0).getAttendenceIn().equals("")) {
                                coloredSnackbar.showSnackBar("You should mark your IN first", coloredSnackbar.TYPE_ERROR, 2000);
                            } else {

                                if (attendenceList.get(0).getAttendenceOut().equals("")) {

                                    Attendence attendence1 = new Attendence();
                                    attendence1.updateAttendenceOut(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "MM/dd/yyyy HH:mm:ss"), mTeamCode.getText().toString(), mBikeNumber.getText().toString(), mMeterValue.getText().toString(), TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

                                    coloredSnackbar.showSnackBar("OUT marked", coloredSnackbar.TYPE_OK, 2000);

//                            Intent svrintent = new Intent(getBaseContext(), TrackingService.class);
//                            getBaseContext().stopService(svrintent);

                                    Intent svrintent = new Intent(getBaseContext(), SampleTrackingService.class);
                                    getBaseContext().stopService(svrintent);

                                    Attendence attendenceToSync = new Attendence();
                                    List<Attendence> attendenceListToSync = new ArrayList<>();
                                    attendenceListToSync = attendenceToSync.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));


                                    SyncAttendence(attendenceListToSync.get(0).getAttendenceOut(), Constants.OUT, attendenceListToSync.get(0).getTeamCode(),
                                            attendenceListToSync.get(0).getMeterValue2(), attendenceListToSync.get(0).getBikeNumber(), attendenceListToSync.get(0).getRallType());


                                } else {
                                    coloredSnackbar.showSnackBar("You have already marked OUT", coloredSnackbar.TYPE_ERROR, 2000);
                                }
                            }
                        }
                        break;

                    case 2:


                        CheckGPS();
                        Attendence attendence1 = new Attendence();

                        attendence1.setAttendenceDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
                        attendence1.setAttendenceIn(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "MM/dd/yyyy HH:mm:ss"));
                        attendence1.setAttendenceOut("");
                        attendence1.setTeamCode(mTeamCode.getText().toString());
                        attendence1.setBikeNumber(mBikeNumber.getText().toString());
                        attendence1.setMeterValue1(mMeterValue.getText().toString());
                        attendence1.setMeterValue2("");
                        attendence1.setSyncTimeIn("");
                        attendence1.setSyncTimeOut("");
                        attendence1.setRallType(String.valueOf(appNameStatues));
                        attendence1.save();

                        coloredSnackbar.showSnackBar("IN marked", coloredSnackbar.TYPE_OK, 2000);

//                Intent svrintent = new Intent(getBaseContext(), TrackingService.class);
//                getBaseContext().startService(svrintent);

//                Intent intent = new Intent("com.cureapp.CUSTOM_INTENT");
//                intent.setClass(getBaseContext(),TrackBroadCast.class);
//                sendBroadcast(intent);

//                Intent svrintent = new Intent(getBaseContext(), TrackingService.class);
//                this.startService(svrintent);
//                Intent intent = new Intent("");
//                intent.setAction("com.android.trackingstart");
//                sendBroadcast(intent);

                        Intent svrintent = new Intent(getBaseContext(), SampleTrackingService.class);
                        getBaseContext().startService(svrintent);

                        setRallStatus(mRall.getSelectedItemPosition());


                        toggleStatus = Constants.OUT;
                        mAttendenceTogle.setBackgroundResource(R.drawable.attendence_out);
                        // mBackground.setBackgroundResource(R.drawable.attendence_background_out);
                        mAttendenceTogle.setText("OUT");

                        Attendence attendenceToSync = new Attendence();
                        List<Attendence> attendenceListToSync = new ArrayList<>();
                        attendenceListToSync = attendenceToSync.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
                        SyncAttendence(attendenceListToSync.get(0).getAttendenceIn(), Constants.IN, attendenceListToSync.get(0).getTeamCode(),
                                attendenceListToSync.get(0).getMeterValue1(), attendenceListToSync.get(0).getBikeNumber(), attendenceListToSync.get(0).getRallType());


                        break;
                }


                break;

            case 1:

                //

//         Timer timer = new Timer ();
//         TimerTask hourlyTask = new TimerTask () {
//            @Override
//            public void run () {
//                // your code here...
//                if(mCommonProgressDialog.isShowing()) {
//                    mCommonProgressDialog.dismiss();
//                    //AttendenceActivity.this.unregisterReceiver(myWifiStateListener);
//                    coloredSnackbar.showSnackBar("Can not connect to the server...", coloredSnackbar.TYPE_ERROR, 2000);
//                }
//
//            }
//        };


                /*final Handler handler = new Handler();

                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (mCommonProgressDialog.isShowing()) {
                            mCommonProgressDialog.dismiss();
                            //AttendenceActivity.this.unregisterReceiver(myWifiStateListener);
                            coloredSnackbar.showSnackBar("Can not connect to the server...", coloredSnackbar.TYPE_ERROR, 2000);
                        }

                    }
                }, 8000);
*/

// schedule the task to run starting now and then every hour...
                //timer.schedule (hourlyTask, 1000*15);

                /*WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                final IntentFilter filters = new IntentFilter();
                filters.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                //       filters.addAction("android.net.wifi.STATE_CHANGE");
                //       filters.addAction("android.net.wifi.WIFI_HOTSPOT_CLIENTS_CHANGED");
                //       filters.addAction(wifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
                //      filters.addAction(wifiManager.EXTRA_SUPPLICANT_CONNECTED);

                this.registerReceiver(myWifiStateListener, filters);

                if (!wifiManager.isWifiEnabled()) {
//                    wifiManager.setWifiEnabled(true);

                    System.out.println("Connecting to the wifi");

                } else {

                    System.out.println("Connected to the wifi");

                }

//                wifiManager.setWifiEnabled(true);
                connectToSSID(wifiManager, ssid, i);*/


                switch (i) {
                    case 1:
                        CheckGPS();
                        Attendence attendence = new Attendence();

                        List<Attendence> attendenceList = new ArrayList<>();

                        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

                        if (attendenceList.isEmpty()) {
                            coloredSnackbar.showSnackBar("You should mark your IN first", coloredSnackbar.TYPE_ERROR, 2000);
                        } else {
                            if (attendenceList.get(0).getAttendenceIn().equals("")) {
                                coloredSnackbar.showSnackBar("You should mark your IN first", coloredSnackbar.TYPE_ERROR, 2000);
                            } else {

                                if (attendenceList.get(0).getAttendenceOut().equals("")) {

                                    String teamcode = "123", bikenumber = "123", metervalue = "123";
                                    Attendence attendence1 = new Attendence();
                                    attendence1.updateAttendenceOut(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "MM/dd/yyyy HH:mm:ss"), teamcode, bikenumber, metervalue, TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

                                    coloredSnackbar.showSnackBar("OUT marked", coloredSnackbar.TYPE_OK, 2000);

//                            Intent svrintent = new Intent(getBaseContext(), TrackingService.class);
//                            getBaseContext().stopService(svrintent);

                                    Intent svrintent = new Intent(getBaseContext(), SampleTrackingService.class);
                                    getBaseContext().stopService(svrintent);

                                    Attendence attendenceToSync = new Attendence();
                                    List<Attendence> attendenceListToSync = new ArrayList<>();
                                    attendenceListToSync = attendenceToSync.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));

                                    //String teamcode = "123", bikenumber = "123", metervalue = "123";
                                    SyncAttendence(attendenceListToSync.get(0).getAttendenceOut(), Constants.OUT, teamcode, metervalue, bikenumber, attendenceListToSync.get(0).getRallType());


                                } else {
                                    coloredSnackbar.showSnackBar("You have already marked OUT", coloredSnackbar.TYPE_ERROR, 2000);
                                }
                            }
                        }
                        break;

                    case 2:

                        CheckGPS();
                        Attendence attendence1 = new Attendence();

                        attendence1.setAttendenceDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
                        attendence1.setAttendenceIn(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "MM/dd/yyyy HH:mm:ss"));
                        attendence1.setAttendenceOut("");
                        attendence1.setTeamCode("123");
                        attendence1.setBikeNumber("123");
                        attendence1.setMeterValue1("123");
                        attendence1.setMeterValue2("");
                        attendence1.setSyncTimeIn("");
                        attendence1.setSyncTimeOut("");
                        attendence1.setRallType(String.valueOf(appNameStatues));
                        attendence1.save();

                        coloredSnackbar.showSnackBar("IN marked", coloredSnackbar.TYPE_OK, 2000);

//                Intent svrintent = new Intent(getBaseContext(), TrackingService.class);
//                getBaseContext().startService(svrintent);

//                Intent intent = new Intent("com.cureapp.CUSTOM_INTENT");
//                intent.setClass(getBaseContext(),TrackBroadCast.class);
//                sendBroadcast(intent);

//                Intent svrintent = new Intent(getBaseContext(), TrackingService.class);
//                this.startService(svrintent);
//                Intent intent = new Intent("");
//                intent.setAction("com.android.trackingstart");
//                sendBroadcast(intent);

                        Intent svrintent = new Intent(getBaseContext(), SampleTrackingService.class);
                        getBaseContext().startService(svrintent);

                        setRallStatus(mRall.getSelectedItemPosition());


                        toggleStatus = Constants.OUT;
                        mAttendenceTogle.setBackgroundResource(R.drawable.attendence_out);
                        // mBackground.setBackgroundResource(R.drawable.attendence_background_out);
                        mAttendenceTogle.setText("OUT");

                        Attendence attendenceToSync = new Attendence();
                        List<Attendence> attendenceListToSync = new ArrayList<>();
                        attendenceListToSync = attendenceToSync.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
                        String teamcode = "123", bikenumber = "123", metervalue = "123";
                        SyncAttendence(attendenceListToSync.get(0).getAttendenceIn(), Constants.IN, teamcode, metervalue, bikenumber, attendenceListToSync.get(0).getRallType());


                        break;
                }




                break;

            case 2:


                //Attendence attendence2 = new Attendence();

                //attendence2.setAttendenceDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
                //attendence2.setAttendenceIn(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "MM/dd/yyyy HH:mm:ss"));
                // attendence2.setAttendenceOut("");
                //attendence2.save();

                //coloredSnackbar.showSnackBar("Leave marked", coloredSnackbar.TYPE_OK, 2000);

                // Attendence attendenceToSync2 = new Attendence();
                // List<Attendence> attendenceListToSync2 = new ArrayList<>();
                // attendenceListToSync2 = attendenceToSync2.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
                //  SyncLeave(attendenceToSync2.attendenceDate,attendenceToSync2.rallType);

//                Attendence attendenceToSync = new Attendence();
//                List<Attendence> attendenceListToSync = new ArrayList<>();
//                attendenceListToSync = attendenceToSync.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
                /*Attendence attendence1 = new Attendence();
                attendence1.setLeavedate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));*/


                SyncLeave(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "MM/dd/yyyy"));

                break;



        }


    }


    public void SyncAttendence(String date, String type, String temCode, String meterValue, String bikeCode, String category) {

        UserProfile userProfile = new UserProfile();
        List<UserProfile> userProfileList = new ArrayList<>();

        userProfileList = userProfile.getAllUsers();

        AttendenctSync attendenctSync = new AttendenctSync(this);
        String serial_number;
        System.out.println("* Constants.SERIAL_NUMBER -> "+Constants.SERIAL_NUMBER);

        if(!Constants.SERIAL_NUMBER.equals("")){
            serial_number = Constants.SERIAL_NUMBER;

        }else if(!userProfileList.get(0).getSimNo().equals("")){
            serial_number =userProfileList.get(0).getSimNo();

        }else{
            serial_number = Utils.getSimSerialNumber(this);
        }
      //  System.out.println("* Serial number "+serial_number);
        /*if(serial_number.equals("") ){
            attendenctSync.setAttendence(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), date, type, Utils.getSimSerialNumber(this), temCode, meterValue, bikeCode, category);
        }else{*/
            attendenctSync.setAttendence(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), date, type, serial_number, temCode, meterValue, bikeCode, category);
      //  }

    }

    public void SyncLeave(String date) {

        UserProfile userProfile = new UserProfile();
        List<UserProfile> userProfileList = new ArrayList<>();

        userProfileList = userProfile.getAllUsers();

        String Leavetype =  mRall2.getSelectedItem().toString();
        String Daytype = mRall3.getSelectedItem().toString();
        LeaveSync   leavesnc = new LeaveSync(this);
        String Remark = leaveremark.getText().toString();
        String LeaveMarkDate = TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "MM/dd/yyyy HH:mm:ss") ;
        leavesnc.setLeave(userProfileList.get(0).getUserName(),userProfileList.get(0).getPassword(),date,Remark,Daytype,Leavetype,LeaveMarkDate );

    }

    public void sendAttendanceSMS(String type) {

        Toast.makeText(this, "Tying to send sms", Toast.LENGTH_LONG).show();

        try {
            ///android.permission.SEND_SMS

            List<UserProfile> userProfilesList = new ArrayList<>();
             userProfilesList = new UserProfile().getAllUsers();
            if (type.equals(Constants.IN)) {

                if( userProfilesList.size()>0)
                 //   msg = mTeamCode.getText().toString().trim() + " " + new TSRDetails().getAllData().get(0).getEpfNo().trim() + " " + "1";
                    msg = mTeamCode.getText().toString().trim() + " " + userProfilesList.get(0).getUserName().trim() + " " + "1";
                // send individual attendance sms
              //  mSMS.sendSMS(Constants.ATTENDACE_MOBILE_NUMBER, msg, 1);

            } else {
                if(userProfilesList.size()>0)
                //    msg = mTeamCode.getText().toString().trim() + " " + new TSRDetails().getAllData().get(0).getEpfNo().trim() + " " + "2";
                    msg = mTeamCode.getText().toString().trim() + " " + userProfilesList.get(0).getUserName().trim() + " " + "2";
                // send individual attendance sms
                //mSMS.sendSMS(Constants.ATTENDACE_MOBILE_NUMBER, msg, 2);
            }

            if (ContextCompat.checkSelfPermission(AttendenceActivity.this,
                    Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(AttendenceActivity.this,
                        Manifest.permission.SEND_SMS)) {

                } else {
                    ActivityCompat.requestPermissions(AttendenceActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            1201);
                }
            }else {
                // Permission already granted

                sendAttendanceSMSBody();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //sendTeamSMS();
    }

    private void sendAttendanceSMSBody() {
        mSMS.sendSMS(Constants.ATTENDACE_MOBILE_NUMBER, msg, 2);
    }

    private void sendTeamSMS(String type) {
        // TODO Auto-generated method stub
        // send team attendance sms


        try {

            if (type.equals(Constants.IN)) {
                msg_team = mTeamCode.getText().toString().trim() + " " + mBikeNumber.getText().toString().trim() + " " + mMeterValue.getText().toString().trim() + " " + "1";
            } else {
                msg_team = mTeamCode.getText().toString().trim() + " " + mBikeNumber.getText().toString().trim() + " " + mMeterValue.getText().toString().trim() + " " + "2";
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (ContextCompat.checkSelfPermission(AttendenceActivity.this,
                    Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(AttendenceActivity.this,
                        Manifest.permission.SEND_SMS)) {

                } else {
                    ActivityCompat.requestPermissions(AttendenceActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            1200);
                }
            }else {
                // Permission already granted

                sendTeamSMSBody();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void sendTeamSMSBody() {

        mSMS.sendSMS(Constants.ATTENDACE_MOBILE_NUMBER, msg_team, Integer.parseInt(team_sms_id));
    }


    @Override
    public void getAttendenceSyncSuccess(String update, String type) {
        //Error with parameter passing
        if(type.equalsIgnoreCase("IN")){
            Constants.ATTENDANCE_STATUS_IN="OK";
            coloredSnackbar.showSnackBar(update, coloredSnackbar.TYPE_OK, 2000);
        }
        if(type.equalsIgnoreCase("OUT")){
            Constants.ATTENDANCE_STATUS_OUT="OK";
            coloredSnackbar.showSnackBar(update, coloredSnackbar.TYPE_OK, 2000);
        }
        checkAttendence();
    }

    private void killWifiNetwork() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        int networkId = wifiManager.getConnectionInfo().getNetworkId();
//
//        wifiManager.removeNetwork(networkId);
//        wifiManager.saveConfiguration();

        //for (wifiManager.getConfiguredNetworks())

        List<WifiConfiguration> wifiArray = new ArrayList<>();
        wifiArray = wifiManager.getConfiguredNetworks();

        if (wifiArray != null) {

            for (WifiConfiguration wifiConfiguration : wifiArray) {
                if (wifiConfiguration.SSID.equals(String.format("\"%s\"", ssid))) {
                    int networkId = wifiManager.getConnectionInfo().getNetworkId();
                    wifiManager.removeNetwork(networkId);
                    wifiManager.saveConfiguration();
                }
            }
        } else {
            coloredSnackbar.showSnackBar("Try Again...", coloredSnackbar.TYPE_WARING, 2000);
        }


    }

    @Override
    public void getAttendenceSyncFail(String message, String type) {

        coloredSnackbar.showSnackBar("Attendence Not marked. Try again...", coloredSnackbar.TYPE_ERROR, 2000);
		
		Constants.ATTENDANCE_STATUS_OUT="NO";
        if(type.equalsIgnoreCase("IN")){
            Constants.ATTENDANCE_STATUS_IN = "NO";
            Attendence attendence1 = new Attendence();
            attendence1.updateSynFlagIn("0");
        }
        if(type.equalsIgnoreCase("OUT")){
            Constants.ATTENDANCE_STATUS_OUT="NO";
            Attendence attendence1 = new Attendence();
            attendence1.updateSynFlagOut("0");
        }


        if (mRall.getSelectedItemPosition() == 0) {
           //
           // sendTeamSMS(type);
           // sendAttendanceSMS(type);
        }

//        killWifiNetwork();
        mCommonProgressDialog.dismiss();
    }


    @Override
    public void onSmsSent(String text) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Attendance SMS Sent", Toast.LENGTH_SHORT).show();
        if (!text.equals(team_sms_id)) {
            // dbh.updateAttendaceSMSStatus(text, 1);
        }
    }

    @Override
    public void onSmsDelivered(String text) {
        // TODO Auto-generated method stub
        if (!text.equals(team_sms_id)) {
            //dbh.updateAttendaceSMSStatus(text, 2);
        }
        Toast.makeText(this, "Attendance SMS Delivered", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendTeamSMSBody();
                    // permission was granted, yay! Do the

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 1201: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendAttendanceSMSBody();
                    // permission was granted, yay! Do the

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    public void onSmsSendFail(String msgid, int failType) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Attendance SMS Send Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSmsDeliveryFail(String msgid, int failType) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Attendance SMS Delivert Failed", Toast.LENGTH_LONG).show();
    }


    private void checkAttendence() {

        UserProfile userProfile = new UserProfile();
        List<UserProfile> userProfilesList = new ArrayList<>();

        userProfilesList = userProfile.getAllUsers();

        Attendence attendence = new Attendence();

        List<Attendence> attendenceList = new ArrayList<>();

        attendenceList = attendence.getAttendenceByDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
//        if(attendenceList.isEmpty()) {
        CheckMyAttendneceSync checkMyAttendneceSync = new CheckMyAttendneceSync(this);
        checkMyAttendneceSync.GetAttendenceData(userProfilesList.get(0).getUserName(), userProfilesList.get(0).getPassword());
//        }
    }

    //@Override
   /* public void getAttendenceSyncSuccess(String update,String ServerTime) {
        try {
            Constants.ATTENDANCE_STATUS_OUT="OK"; //
//            killWifiNetwork();
            mCommonProgressDialog.dismiss();
            AttendenceActivity.this.unregisterReceiver(myWifiStateListener);
        } catch (Exception e) {
            System.out.println(e);
        }
    }*/

    @Override
    public void getAttendenceSuccess(String update, String Servertime) {
        System.out.println("**** Servertime"+Servertime);



        Constants.ATTENDANCE_STATUS_OUT="OK";

        if (!update.equals("") && update != null) {



//            if (update.equals("1")) {
//                if (type.equals(Constants.IN)) {
//
//                    //###############################################################
//
////                    Attendence attendence1 = new Attendence();
////
////                    attendence1.setAttendenceDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
////                    attendence1.setAttendenceIn(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "MM/dd/yyyy HH:mm:ss"));
////                    attendence1.setAttendenceOut("");
////                    attendence1.setTeamCode(mTeamCode.getText().toString());
////                    attendence1.setBikeNumber(mBikeNumber.getText().toString());
////                    attendence1.setMeterValue1(mMeterValue.getText().toString());
////                    attendence1.setMeterValue2("");
////                    attendence1.setSyncTimeIn("");
////                    attendence1.setSyncTimeOut("");
////                    attendence1.setRallType(String.valueOf(appNameStatues));
////                    attendence1.save();
//
//                    coloredSnackbar.showSnackBar("IN marked", coloredSnackbar.TYPE_OK, 2000);
//
////                Intent svrintent = new Intent(getBaseContext(), TrackingService.class);
////                getBaseContext().startService(svrintent);
//
////                Intent intent = new Intent("com.cureapp.CUSTOM_INTENT");
////                intent.setClass(getBaseContext(),TrackBroadCast.class);
////                sendBroadcast(intent);
//
////                Intent svrintent = new Intent(getBaseContext(), TrackingService.class);
////                this.startService(svrintent);
////                Intent intent = new Intent("");
////                intent.setAction("com.android.trackingstart");
////                sendBroadcast(intent);
//
//                    Intent svrintent = new Intent(getBaseContext(), SampleTrackingService.class);
//                    getBaseContext().startService(svrintent);
//
//                    setRallStatus(mRall.getSelectedItemPosition());
//
//
//                    toggleStatus = Constants.OUT;
//                    mAttendenceTogle.setBackgroundResource(R.drawable.attendence_out);
//                    // mBackground.setBackgroundResource(R.drawable.attendence_background_out);
//                    mAttendenceTogle.setText("OUT");
//
//                    //###############################################################
//
//                    Attendence attendence = new Attendence();
//                    attendence.updateSyncIn(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy HH:mm:ss"), TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
//
//
//                } else if (type.equals(Constants.OUT)) {
//
//                    Attendence attendence1 = new Attendence();
//                    attendence1.updateAttendenceOut(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "MM/dd/yyyy HH:mm:ss"), mTeamCode.getText().toString(), mBikeNumber.getText().toString(), mMeterValue.getText().toString(), TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
//
//
//                    coloredSnackbar.showSnackBar("OUT marked", coloredSnackbar.TYPE_OK, 2000);
//
//                    Intent svrintent = new Intent(getBaseContext(), SampleTrackingService.class);
//                    getBaseContext().stopService(svrintent);
//
//
//                    Attendence attendence = new Attendence();
//                    attendence.updateSyncOut(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy HH:mm:ss"), TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "dd/MM/yyyy"));
//
//                }
//            }
     //       coloredSnackbar.showSnackBar(update, coloredSnackbar.TYPE_OK, 2000);
            checkAttendence();
        }
    }

    @Override
    public void getAttendenceFaile(String message) {
        try {
//            killWifiNetwork();
            mCommonProgressDialog.dismiss();
            AttendenceActivity.this.unregisterReceiver(myWifiStateListener);
            Constants.ATTENDANCE_STATUS_OUT="NO"; //
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void getLeaveSuccess(String update,String status) {

        Constants.LEAVE_STATUS=1;
        System.out.println(update);
        String msg = update.toString();
        if (!update.equals("") && update != null) {

            if (status.equals("1")) {
                //coloredSnackbar.showSnackBar("Success",coloredSnackbar.TYPE_OK,2000);
                coloredSnackbar.showSnackBar(msg, coloredSnackbar.TYPE_OK, 2000);
            }
            //else if (status.equals("999"))
            else
            {
                coloredSnackbar.showSnackBar(msg, coloredSnackbar.TYPE_ERROR, 2000);
            }
        }
        else
        {
            coloredSnackbar.showSnackBar("No connection please retry", coloredSnackbar.TYPE_ERROR, 2000);
        }

    }

    @Override
    public void getLeaveFaile(String message) {

        coloredSnackbar.showSnackBar("No connection please retry", coloredSnackbar.TYPE_ERROR, 2000);


        //sendAttendanceSMS();
        //sendTeamSMS();


    }

    private void dialogDisrmiss(){
        if ((dialog != null) && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }

}