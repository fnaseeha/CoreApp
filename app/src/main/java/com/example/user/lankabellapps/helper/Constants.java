package com.example.user.lankabellapps.helper;

/**
 * Created by user on 2017-03-31.
 */

public class Constants {

    //public static final String Baseurl = "119.235.1.59:8090/";
    //public static final String BaseUrlTOCoreApp = "119.235.1.88:8091/";

    //Live 119.235.1.88:8091/
    //Test 119.235.1.59:8070/


    //    //###############Live###############
       // public static final String BaseUrlTOCoreApp = "119.235.1.88:8091/";
       // public static final String imageUrl = "http://119.235.1.88:8092/ImageUpload.aspx?MerchantID=";
//    //##################################


    //###############Test###############
//  public static final String BaseUrlTOCoreApp = "119.235.1.59:4010/"; //Test
  public static final String BaseUrlTOCoreApp = "119.235.1.88:8091/";// Live

 // public static final String BaseUrlTOCoreApp = "10.1.3.105:8091/";// Local
   //// public static final String BaseUrlTOCoreApp = "119.235.1.59:4010/"; //8070 Given to TrackingApp
  public static final String imageUrl = "http://119.235.1.59:8090/ImageUpload.aspx?MerchantID=";
    //##################################


    public static final String LOGIN_SUCCESSFUL = "Login Successful!";

    public static final String IN = "IN";
    public static final String OUT = "OUT";

    //Colomn names

    public static final String  LastLoginTime = "LastLoginTime";
    public static final String TodayLogedIn = "TodayLogedIn";
    public static final String CheckNewAppsTime = "CheckNewAppsTime";
    public static final String CheckAppUpdate = "CheckAppUpdate";

    public static final String ATTENDENCEACTIVITY = "ATTENDENCEACITVITY";
    public static final String CUSTOMER_FRAGMENT = "CUSTOMER_FRAGMENT";
    public static final String LOCATION_REGISTER_PENDING = "PEN";
    public static String ALAM_COUNT = "ALAM_COUNT";

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    private static final int UPDATE_INTERVAL_IN_SECONDS = 60;
    // Update frequency in milliseconds
    public static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 60;
    // A fast frequency ceiling in milliseconds
    public static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    // Stores the lat / long pairs in a text file
    public static final String LOCATION_FILE = "sdcard/location.txt";
    // Stores the connect / disconnect data in a text file
    public static final String LOG_FILE = "sdcard/log.txt";

    public static final String RUNNING = "runningInBackground"; // Recording data in background

    public static final String APP_PACKAGE_NAME = "com.example.user.lankabellapps";

    public static final String HOME_FRAGMETN = "HOME FRAGMENT";
    public static final String CUSTOMERS_FRAGMENT = "CUSTOMERS FRAGMENT";
    public static final String MERCHANTS_FRAGMETN = "MERCHANTS FRAGMENT";

    public static boolean STOP_SENDING_SMS = false;
    public static final String ATTENDACE_MOBILE_NUMBER = "+94115760927"; //94115760927 //94774713454

    public static String ATTENDANCE_STATUS_OUT="OK";
    public static String ATTENDANCE_STATUS_IN="OK";
    public static  int LEAVE_STATUS = 0; // 0=No Leave,1=Leave apllied
    public static  int CORRECTDATETIME_STATUS = 0;
    public static String SERVER_TIME_COFIRM = "";
    public static String SERIAL_NUMBER = "";
    public static final int GPS_REQUEST = 1001;
    public static int GPS_INTERVAL = 2000;

    /**
     * Suppress default constructor for noninstantiability
     */
    private Constants() {
        throw new AssertionError();
    }


}
