package com.example.user.lankabellapps.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thejan on 2016-10-11.
 */
public class Utils implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static String getSimSerialNumber = "";
    public static TelephonyManager mTelephonyMgr;


    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch ( Exception e ) {
            return false;
        }

    }

    public static ArrayList<String> getSerialNumbers(Context context) {
        SubscriptionManager manager = null;
        ArrayList<String> SerialNumbers = new ArrayList<>();

     /*   mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);


        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {

            try {

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
                }
                manager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                //assert manager != null;
                if (manager != null) {
                    System.out.println("* manager not null");
                    List<SubscriptionInfo> infoList = manager.getActiveSubscriptionInfoList();
             //       System.out.println("* info list " + infoList.size());
                if(infoList != null) {
                    for (int i = 0; i < infoList.size(); i++) {

                        SubscriptionInfo info = infoList.get(i);
                        String serialNumber = info.getIccId();
                        SerialNumbers.add(serialNumber);

                    }
                }else{
                    System.out.println("* infoList is null");
                }

                } else {
                    System.out.println("* manager is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
            SerialNumbers.add(getSimSerialNumber);
//
        }*/

       /* SerialNumbers.clear();

        	SerialNumbers.add("8994084111800545576f");//1317196*/
        //SerialNumbers.add("8994029702857736510F");//2453 live
       // SerialNumbers.add("89940102166881325622");//2453 test
        SerialNumbers.add("89940102166881325622");//9127426 test
        return SerialNumbers;
    }


    @SuppressLint("HardwareIds")
    public static String getSimSerialNumber(Context context) {

        mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);


        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (Build.VERSION.SDK_INT >= 23) {
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
            } else {
                //TODO
                getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();

            }
        } else {
            getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();

        }
        return getSimSerialNumber;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                System.out.println("*onRequestPermissionsResult");
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO

                    getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
                }
                break;

            default:
                break;
        }
    }

}
