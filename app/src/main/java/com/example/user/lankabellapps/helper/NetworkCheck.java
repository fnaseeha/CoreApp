package com.example.user.lankabellapps.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.user.lankabellapps.activities.LoginActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * Created by Thejan Thrimanna on 7/21/2015.
 */
public class NetworkCheck {
    public NetworkCheck() {
    }

    public boolean NetworkConnectionCheck(Context con) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager)
                    con.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        }catch (Exception e){
            return false;
        }
    }


//    public boolean isInternetAvailable() {
//        boolean success = false;
//        try {
//            URL url = new URL("https://google.com");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(10000);
//            connection.connect();
//            success = connection.getResponseCode() == 200;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return success;
//    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }


}
