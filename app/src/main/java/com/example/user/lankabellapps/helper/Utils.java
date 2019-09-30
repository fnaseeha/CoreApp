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

//        mTelephonyMgr = (TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//
//        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
//
//
//        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
//
//            try {
//
//                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
//                }
//                manager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
//                //assert manager != null;
//                if (manager != null) {
//                    System.out.println("* manager not null");
//                    List<SubscriptionInfo> infoList = manager.getActiveSubscriptionInfoList();
//             //       System.out.println("* info list " + infoList.size());
//                if(infoList != null) {
//                    for (int i = 0; i < infoList.size(); i++) {
//
//                        SubscriptionInfo info = infoList.get(i);
//                        String serialNumber = info.getIccId();
//                        SerialNumbers.add(serialNumber);
//
//                    }
//                }else{
//                    System.out.println("* infoList is null");
//                }
//
//                } else {
//                    System.out.println("* manager is null");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//
//
//            getSimSerialNumber = mTelephonyMgr.getSimSerialNumber();
//            SerialNumbers.add(getSimSerialNumber);
////
////			SerialNumbers.clear();
////		//	SerialNumbers.add("8994034060115581639f");
////			SerialNumbers.add("8994084111800545576"); //4107
//
//            // SerialNumbers.add("8994029702649458050"); //131796
//
//            //   SerialNumbers.add("8994036031116012502"); //129141 LIVE USER
//
//
////                         "8994035110313941827F"); //115 live
////                         "8994029702958154753f"); //9127426
////                          "8994084111800545576f"); //1039 Test
////                          "8994036031116012658F"); //1039
////                          "8994084111800545576");
////                          "8994035110313941819f");//246
////                          "8994084111800545576"); //319
////                        "8994084111800545576");//test use 131796
//        }
//
        SerialNumbers.clear();

        	SerialNumbers.add("89940102166881325622");
        return SerialNumbers;
    }


    @SuppressLint("HardwareIds")
    public static String getSimSerialNumber(Context context) {

        mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);


        // String serial = "";
        // serial = "8994034060115581886f";
        // serial = "8994038240713654220f";
        // serial = "8994038240713654440f";
        // serial = "8994038240713654330";
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
//2453 94
        //   getSimSerialNumber = "89940102166881325622"; //131796
        //  getSimSerialNumber = "89940102166881325624"; //3267
        // if (getSimSerialNumber == null) {
        //getSimSerialNumber = "8994035020514027062"; //3361
        // getSimSerialNumber = "8994038240713654220 ";
//         getSimSerialNumber = "8994038030314143073f"; //Upul
        //getSimSerialNumber = "8994034061116853811f"; //1699
//         getSimSerialNumber = "8994035150914853617f"; //Motier 104336
//        getSimSerialNumber = "8994038030314889808"; //Motier 104336
        //getSimSerialNumber = "8994038240713654220f";// QA phone
        //getSimSerialNumber = "8994038240713654238f";// 1039 phone
        //	 getSimSerialNumber = "8994034060115581886f";//username=a pw=a
//		getSimSerialNumber = "8994038240713656233f";// WICK phone
        // String getSimSerialNumber = "8994035260310788463f";//niroshanR
        // }
        // if (getSimSerialNumber.trim().equals("")) {
        // getSimSerialNumber = serial;
        // return serial;
        // }
        // // String getSimNumber = mTelephonyMgr.getLine1Number();
        // if (getSimSerialNumber.contains("89940101113304949761")) {
//		 getSimSerialNumber = "8994034060115581902f";
        // }

        // getSimSerialNumber = "8994038240713654440"; // 2154
        // getSimSerialNumber = "8994038240713654220f";
        // chathura
        // getSimSerialNumber = "8994034060115581886f";
        // Wickrama
        // getSimSerialNumber = "8994038240713656233f";
        // david
        // getSimSerialNumber = "8994038240713655953";

        // if(!getSimSerialNumber.contains("f")){
        // getSimSerialNumber += "f";
        // }
        // Kalpia - 1461
        // getSimSerialNumber = "8994035230212657074";
        // Dasanayake - 2474
        // getSimSerialNumber = "8994038240713652737";
        // Vidura - 1209
        // getSimSerialNumber = "8994034060115582223f";
        // Janaka - 1500
        // getSimSerialNumber = "8994034060115582231f";
        // Karunaraja - 4107
        // getSimSerialNumber = "8994035230212657231";

        // kumara - 3699
        // getSimSerialNumber = "8994038240713655896";
        // mangula - 9125206
        // getSimSerialNumber = "8994034060115582090f";
        // Karunaraja - 4107
        // getSimSerialNumber = "8994035230212657231";
        // Rohan Predip - 76
        // getSimSerialNumber = "8994034060115581951f";
        // Rohan Predip - 68
        // getSimSerialNumber = "8994034060115581969f";
        // - 9119499
        // getSimSerialNumber = "8994034011014955986f";
        // - 124358
        // getSimSerialNumber = "8994038240713654212f";
        // - 327
        // getSimSerialNumber = "8994034011014955960f";
        // - 2026
        // getSimSerialNumber = "8994034060115581647f";
        // getSimSerialNumber = "8994034060115582181f";
        // getSimSerialNumber = "8994034060115581530f";
        // - 92092
        // getSimSerialNumber = "8994034060115582199f";
        // - 2482
        // getSimSerialNumber = "8994034060115581555f";
        // kumara - 9121052
        // getSimSerialNumber = "8994034060115581712f";

//        if(getSimSerialNumber == null){
//            getSimSerialNumber = "";
//        }
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
