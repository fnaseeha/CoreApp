package com.example.user.lankabellapps.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.telephony.SubscriptionManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.helper.NetworkCheck;
import com.example.user.lankabellapps.helper.TimeFormatter;
import com.example.user.lankabellapps.helper.Utils;
import com.example.user.lankabellapps.models.TimeCap;
import com.example.user.lankabellapps.models.UserProfile;
import com.example.user.lankabellapps.services.apimodels.GetCommonResponseModel;
import com.example.user.lankabellapps.services.backgroundservices.GPSLocation;
import com.example.user.lankabellapps.services.restinterface.SyncDataServices;
import com.example.user.lankabellapps.services.sync.LoginSync;
import com.example.user.lankabellapps.services.sync.UpdateVersionSync;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import static com.example.user.lankabellapps.helper.Constants.GPS_INTERVAL;

public class LoginActivity extends AppCompatActivity implements LoginSync.LoginSyncEvetns,
        UpdateVersionSync.UpdateVersionEvents {

    @Bind(R.id.btn_login)
    AppCompatButton btnLogin;

    @Bind(R.id.etv_username)
    EditText etvUserName;

    @Bind(R.id.etv_pword)
    EditText etvPassword;

    @Bind(R.id.ib_password_show_hide)
    ImageButton ibtnShowHodePassword;

    ColoredSnackbar coloredSnackbar;
    ProgressDialog dialog;
    LocationManager locationManager;
    boolean isGPSEnabled;
    ArrayList < String > AllSerialNumbers;
    boolean trySecondTime = false;
    LinearLayout llSimSerial;
    TextView phoneSerial,
            dbSerial,a_phone,a_version;
    UserProfile userProfile;
    List < UserProfile > userProfileList = new ArrayList < >();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        userProfile = new UserProfile();
        AllSerialNumbers = Utils.getSerialNumbers(this);

        init();

    }

    private void init() {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        coloredSnackbar = new ColoredSnackbar(this);

        ibtnShowHodePassword.setTag("0");
        llSimSerial = findViewById(R.id.llSimSerial);
        llSimSerial.setVisibility(View.GONE);
        phoneSerial = findViewById(R.id.phoneSerial);
        dbSerial = findViewById(R.id.dbSerial);
        a_phone = findViewById(R.id.a_phone);
        a_version = findViewById(R.id.a_version);

        TimeCap timeCap = new TimeCap();
        List < TimeCap > timeCapList = timeCap.getTimeCapByName(Constants.TodayLogedIn); //today's logged in time

        if (!timeCapList.isEmpty()) { //today logged previously or not

            if (!timeCapList.get(0).getDate().equals("")) { // logged in today successfully
                String lastLogedDate = TimeFormatter.changeTimeFormat("yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd", timeCapList.get(0).getDate()); //yyyy-MM-dd HH:mm:ss - last login time
                String today = TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy/MM/dd"); //current time

                System.out.println("* lastLogedDate " + lastLogedDate);
                System.out.println("* today " + today);
                if (lastLogedDate.equals(today)) { //lastLogDate == today compares only date

                    //userProfile = new UserProfile();
                    userProfileList = userProfile.getAllUsers();
                    LoginSync loginSync = new LoginSync(this);
                    String simNo = userProfileList.get(0).getSimNo();

                    System.out.println("* sim " + simNo);
                    if (Utils.getSimSerialNumber.equals("")) {

                        //coloredSnackbar.showSnackBar(" Sim Card is not detected Please try again", coloredSnackbar.TYPE_ERROR, 2500);
                        System.out.println(" Sim Card is not detected Please try again");
                    } else if (userProfileList.size() > 0) {

                        if (simNo.equals("")) {
                            if (new NetworkCheck().NetworkConnectionCheck(this)) {
                                loginSync.login(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), AllSerialNumbers.get(0));
                               // loginSync.login(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), "8994029702649458050");
                            } else {
                                coloredSnackbar.showSnackBar("Inserted Sim number is not match with Server sim Number Please contact your supervisor", coloredSnackbar.TYPE_ERROR, 2500);
                            }

                        } else {
                            System.out.println("* simNo not null ");
                            if (new NetworkCheck().NetworkConnectionCheck(this)) {
                                loginSync.login(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), AllSerialNumbers.get(0));
                              //  loginSync.login(userProfileList.get(0).getUserName(), userProfileList.get(0).getPassword(), "8994029702649458050");
                            } else {
                                if (AllSerialNumbers.size() == 1 && AllSerialNumbers.get(0).equals(simNo)) {
                                    System.out.println("* AllSerialNumbers.size()==1 , 1 sim");
                                    startMainActivity();
                                } else if (AllSerialNumbers.size() > 1 && (AllSerialNumbers.get(0).equals(simNo) || AllSerialNumbers.get(1).equals(simNo))) {
                                    System.out.println("* AllSerialNumbers.size()==2 , 2 sim");
                                    startMainActivity();
                                } else {
                                    coloredSnackbar.showSnackBar("Inserted Sim number is not match with Server sim Number Please contact your supervisor", coloredSnackbar.TYPE_ERROR, 2500);
                                }
                            }
                        }
                    }
                }

            } else {
                System.out.println("* timeCapList.get(0).getDate().equals(\"\")");
            }
        } else {
            System.out.println("* timeCapList is Empty");
        }

    }

    @OnClick(R.id.ib_password_show_hide)
    public void OnClickShowHidePassword(View view) {
        if (ibtnShowHodePassword.getTag().equals("0")) {
            etvPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ibtnShowHodePassword.setTag("1");
            ibtnShowHodePassword.setImageResource(R.drawable.password_show);
        } else {
            ibtnShowHodePassword.setTag("0");
            etvPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ibtnShowHodePassword.setImageResource(R.drawable.password_hide);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @OnClick(R.id.btn_login)
    public void OnClickRegister(View view) {

        try {
            InputMethodManager imm = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.CUPCAKE) {
                imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            }
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch(Exception e) {
            e.printStackTrace();
        }

        trySecondTime = false;

        if (etvPassword.getText().toString().equals("") || etvUserName.getText().toString().equals("")) {
            coloredSnackbar.showSnackBar("Fill all the fields...", coloredSnackbar.TYPE_ERROR, 2000);
        } else {

            AllSerialNumbers = Utils.getSerialNumbers(this);

            if (new NetworkCheck().NetworkConnectionCheck(this)) {
                AllSerialNumbers = Utils.getSerialNumbers(this);
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (isGPSEnabled) {
                    dialogshow();
                    dialog.show();

                    TimeCap timeCap = new TimeCap();
                    List < TimeCap > timeCapList = new ArrayList < >();
                    timeCapList = timeCap.getAllData();

                    try {
                        AllSerialNumbers = Utils.getSerialNumbers(this);
                        System.out.println("* sm 1st " + AllSerialNumbers.get(0));

                        if (AllSerialNumbers.get(0) == null) {
                            dialog.dismiss();
                            coloredSnackbar.showSnackBar("Please Try again No Sim Card detected", coloredSnackbar.TYPE_ERROR, 2500);

                        } else {

                            if (timeCapList.isEmpty()) {

                                LoginSync loginSync = new LoginSync(this);

                             loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), AllSerialNumbers.get(0));
                            // loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), "8994029702649458050");
                                //loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), "8994031090615047297F");
                              // loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), "8994034280218853343f");
                              // loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), "8994029702982976734f");
                            } else {

                                TimeCap timeCap1 = new TimeCap();
                                List < TimeCap > timeCapListLoginTime = new ArrayList < >();

                                timeCapListLoginTime = timeCap1.getTimeCapByName(Constants.LastLoginTime);

                                long currentTime = new Date().getTime();

                                //  long timeDifference = currentTime - (TimeFormatter.changeStringTimeToLong(timeCapListLoginTime.get(0).getDate(), "yyyy-MM-dd HH:mm:ss"));

                                if (false) { //timeDifference >= 1800000 //false
                                    LoginSync loginSync = new LoginSync(this);

                                   loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), AllSerialNumbers.get(0));
                                   //loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), "8994029702649458050");
                                  // loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), "8994031090615047297F");
                                   // loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), "8994029702649458050");

                                } else {

                                    userProfileList = userProfile.getAllUsers();
                                    AllSerialNumbers = Utils.getSerialNumbers(this);
                                    if (userProfileList.get(0).getUserName().equals(etvUserName.getText().toString()) && userProfileList.get(0).getPassword().equals(etvPassword.getText().toString())) {

                                        if (new NetworkCheck().NetworkConnectionCheck(this)) {
                                            LoginSync loginSync = new LoginSync(this);
                                           loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), AllSerialNumbers.get(0));
                                            //loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), "8994029702649458050");
                                           // loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), "8994029702649458050");

                                        } else {
                                            if (userProfileList.get(0).getSimNo().equals(AllSerialNumbers.get(0))) {
                                                startMainActivity();
                                            } else {
                                                coloredSnackbar.showSnackBar("Inserted Sim number is not match with Server sim Number Please contact your supervisor", coloredSnackbar.TYPE_ERROR, 2500);
                                            }
                                        }

                                    } else {

                                        if (!userProfileList.get(0).getUserName().equals(etvUserName.getText().toString())) {

                                            coloredSnackbar.showSnackBar("Incorrect Username", coloredSnackbar.TYPE_ERROR, 2500);
                                        } else if (!userProfileList.get(0).getPassword().equals(etvPassword.getText().toString())) {

                                            coloredSnackbar.showSnackBar("Incorrect Password...", coloredSnackbar.TYPE_ERROR, 2500);
                                        } else if ((!userProfileList.get(0).getPassword().equals(etvPassword.getText().toString())) && (!userProfileList.get(0).getUserName().equals(etvUserName.getText().toString()))) {
                                            coloredSnackbar.showSnackBar("Incorrect Username and Password...", coloredSnackbar.TYPE_ERROR, 2500);
                                        }

                                    }

                                    dialogDisrmiss();
                                }

                            }
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    coloredSnackbar.showSnackBar("Please on the GPS", coloredSnackbar.TYPE_ERROR, 2500);
                }

            } else {

                UserProfile userProfile = new UserProfile();
                List < UserProfile > userProfilesList = new ArrayList < >();
                AllSerialNumbers = Utils.getSerialNumbers(this);
                userProfilesList = userProfile.getAllUsers();
                if (userProfilesList.size() > 0) {
                    String simNo = userProfilesList.get(0).getSimNo();
                    if (userProfilesList.get(0).getUserName().equals(etvUserName.getText().toString()) && userProfilesList.get(0).getPassword().equals(etvPassword.getText().toString())) {

                        if (AllSerialNumbers.size() == 1 && AllSerialNumbers.get(0).equals(simNo)) {
                            System.out.println("* AllSerialNumbers.size()==1 , 1 sim");
                            startMainActivity();
                        } else if (AllSerialNumbers.size() > 1 && (AllSerialNumbers.get(0).equals(simNo) || AllSerialNumbers.get(1).equals(simNo))) {
                            System.out.println("* AllSerialNumbers.size()==2 , 2 sim");
                            startMainActivity();
                        } else if (AllSerialNumbers.size() == 0) {
                            // coloredSnackbar.showSnackBar("Inserted Sim number is not match with Server sim Number Please contact your supervisor", coloredSnackbar.TYPE_ERROR, 2500);
                            coloredSnackbar.showSnackBar("your sim card is not detected Please try again", coloredSnackbar.TYPE_ERROR, 2500);
                        }

                    } else {
                        coloredSnackbar.showSnackBar("Username or Password is wrong", coloredSnackbar.TYPE_ERROR, 2500);
                    }

                } else {
                    coloredSnackbar.showSnackBar("Can not connect to the internet. Please check", coloredSnackbar.TYPE_ERROR, 2500);
                }

            }

        }
    }

    private void startMainActivity() {
        TimeCap timeCap = new TimeCap();

        if (timeCap.getTimeCapByName(Constants.TodayLogedIn).isEmpty()) { //checking weather the time cap is empty?
            TimeCap timeCap2 = new TimeCap();
            timeCap2.setRawName(Constants.TodayLogedIn);
            timeCap2.setDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"));

            timeCap2.save(); // saving new loging details
        } else { // if time cap list didn't empty
            TimeCap timeCap2 = new TimeCap();
            timeCap2.updateMasterDataTime(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"), Constants.TodayLogedIn); // updating new loing details

        }

        coloredSnackbar.showSnackBar("Login Success...", coloredSnackbar.TYPE_OK, 2000);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.zoom_in, R.anim.fade_out);
    }

    @Override
    public void onLoginSuccess(GetCommonResponseModel update) {
        try {

            if (update != null) {
                if (update.getStatus().equals("1")) {
                    updateVersion();
                    llSimSerial.setVisibility(View.GONE);
                    //code here
					/*if(update.getServerTime())
                 {

                 }
                 else
                     {

                     }*/
                    Constants.SERIAL_NUMBER = update.getId();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sMobileDate = TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy-MM-dd HH:mm:ss");

                    Date MobileDate = formatter.parse(sMobileDate);

                    String sservertime = update.getServerTime();

                    Date servertime = formatter.parse(sservertime);

                    DateTime phoneDate = new DateTime(MobileDate);
                    DateTime serverDate = new DateTime(servertime);

                    Integer diff = Minutes.minutesBetween(phoneDate, serverDate).getMinutes();

                    Log.d("Get Phone date :", phoneDate.toString());
                    long diffN = Math.abs(diff);

                    if ((diffN < 10)) {

                        loginConfirm();

                    } else {

                        dialogDisrmiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        builder.setTitle("                  Error");
                        builder.setMessage("Please Correct Date & Time In Your Phone...");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                //dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                        coloredSnackbar.showSnackBar("Please Correct Date & Time In Your Phone...", coloredSnackbar.TYPE_ERROR, 2500);

                    }

                } else {

                    if (AllSerialNumbers.size() > 1 && !trySecondTime) { //
                        System.out.println(" * retry login " + AllSerialNumbers.get(1));
                        //coloredSnackbar.showSnackBar("Retry..", coloredSnackbar.TYPE_ERROR, 2000);
                        retrySecondTimeLogin();
                        trySecondTime = true;
                    } else {
                        dialogDisrmiss();
                        if (!update.getId().equals("0")) {
                            ShowLastDigits(update.getId(), AllSerialNumbers);

                            coloredSnackbar.showSnackBar(update.getData(), coloredSnackbar.TYPE_ERROR, 2000);
							/* if(!Utils.getSimSerialNumber(this).equals(update.getId())){
                            }else{
                                coloredSnackbar.showSnackBar("Username / Password is wrong", coloredSnackbar.TYPE_ERROR, 2000);
                            }*/
                        } else {
                            coloredSnackbar.showSnackBar("Login Failed... " + update.getData(), coloredSnackbar.TYPE_ERROR, 2000);
                        }
                    }
                    /*update*/
                    // coloredSnackbar.showSnackBar(update.getData(), coloredSnackbar.TYPE_ERROR, 2000);
                }
            } else {

                dialogDisrmiss();
                if (AllSerialNumbers.size() > 1 && !trySecondTime) {
                    retrySecondTimeLogin();
                    trySecondTime = true;
                } else {
                    coloredSnackbar.showSnackBar("Login Failed...", coloredSnackbar.TYPE_ERROR, 2000);
                }

            }
        } catch(Exception e) {
           e.printStackTrace();
            coloredSnackbar.showSnackBar("Login Failed... " + e.getMessage(), coloredSnackbar.TYPE_ERROR, 2000);
        }

    }

    private void updateVersion() {
        String version = "1.5"; //1.6.2
        String newversion = version;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = pInfo.versionName;
            if (version.length() == 5) {
                newversion = version.substring(0, 3) + version.substring(4);
            } else if (version.length() == 7) {
                newversion = version.substring(0, 3) + version.substring(4, 5) + version.substring(6, 7);
            } else {
                newversion = version;
            }
        } catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("* new version " + newversion);
		/* String myName = "1.6.2";
        String newName="";
        //if(j.lenght())
       // System.out.println("Sum of x+y = " + j.length());
        if( myName.length()==5){
             newName = myName.substring(0,3)+myName.substring(4);
        }else if(myName.length()==7){
             newName = myName.substring(0,3)+myName.substring(4,5)+myName.substring(6,7);
        }*/
        UpdateVersionSync updateVersionSync = new UpdateVersionSync(this);
        updateVersionSync.updateVersion(etvUserName.getText().toString().trim(), "1", newversion);

    }

    private void ShowLastDigits(String serverNumber, ArrayList < String > phoneNumber) {

        // String gg = phoneNumber.get(0);

        System.out.println("* phoneNumber.size() " + phoneNumber.size());

        llSimSerial.setVisibility(View.VISIBLE);

                if(phoneNumber.size()>1){
                    phoneSerial.setText("PHONE SIM SERIAL : "+ phoneNumber.get(0)+" , "+phoneNumber.get(1));
                }else if(phoneNumber.size()==1){
                    phoneSerial.setText("PHONE SIM SERIAL : "+ phoneNumber.get(0));
                }
        dbSerial.setText("DB SIM SERIAL : " + serverNumber);

        //        phoneSerial.setText("PHONE SIM SERIAL : ..."+ phoneNumber.substring(phoneNumber.length() - 4));
        //        dbSerial.setText("DB SIM SERIAL : ..."+ serverNumber.substring(serverNumber.length() - 4));
    }

    @Override
    protected void onPause() {
        super.onPause();

        dialogDisrmiss();
    }

    private void dialogDisrmiss() {
        if ((dialog != null) && dialog.isShowing()) dialog.dismiss();
        dialog = null;
    }

    private void dialogshow() {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
    }

    private void loginConfirm() {

    //    System.out.println("* login confirm "+sim_number);
        TimeCap timeCap = new TimeCap();
        List < TimeCap > timeCapList = new ArrayList < >();
        timeCapList = timeCap.getAllData();

        if (timeCapList.isEmpty()) {
            new UserProfile().clearTable();
            UserProfile userProfile = new UserProfile();

            userProfile.setUserName(etvUserName.getText().toString().trim());
            userProfile.setPassword(etvPassword.getText().toString().trim());
            userProfile.setAttendencIn("1");
            userProfile.setAttendencOut("1");
            userProfile.setCollectorCode("");
          //  userProfile.setSimNo(sim_number);
            if (!Constants.SERIAL_NUMBER.equals("")) {
                userProfile.setSimNo(Constants.SERIAL_NUMBER);
            }

            userProfile.save();

            TimeCap timeCap1 = new TimeCap();
            timeCap1.setRawName(Constants.LastLoginTime);
            timeCap1.setDate(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"));

        } else {
            TimeCap timeCap1 = new TimeCap();
            timeCap1.updateMasterDataTime(TimeFormatter.convertStringTimetoMilliseconds(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"), Constants.LastLoginTime);
        }

        dialogDisrmiss();

        startMainActivity();
    }

    @Override
    public void onLoginFailed(String message, String id) {

        //        if(AllSerialNumbers.size()>1 && !trySecondTime){
        //            retrySecondTimeLogin();
        //            trySecondTime = true;
        //        }else{
        dialogDisrmiss();
        if (!id.equals("0")) {
            if (!Utils.getSimSerialNumber(this).equals(id)) {
                coloredSnackbar.showSnackBar(message, coloredSnackbar.TYPE_ERROR, 2000);
            } else {
                coloredSnackbar.showSnackBar("Server Error", coloredSnackbar.TYPE_ERROR, 2000);
            }
        } else {
            coloredSnackbar.showSnackBar("Login Failed... " + message, coloredSnackbar.TYPE_ERROR, 2000);
        }

        //      }

        //loginConfirm();
    }

    private void retrySecondTimeLogin() {
        if (AllSerialNumbers.size() > 1) {
            LoginSync loginSync = new LoginSync(this);
            loginSync.login(etvUserName.getText().toString().trim(), etvPassword.getText().toString().trim(), AllSerialNumbers.get(1));
        }

    }

    @Override
    public void updateVersionSuccess(GetCommonResponseModel update) {
        if (update.getStatus().equals("1")) {
            try {
                if (!update.getData().equals("")) {
                    GPS_INTERVAL = Integer.parseInt(update.getData());
                }
                System.out.println("* update version is success " + update.getData());
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("* update version is falied " + update.getData());
        }
    }

    @Override
    public void updateVersionFailed(String update) {
        System.out.println("* update version is falied " + update);
    }
}