package com.example.user.lankabellapps.services.backgroundservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.user.lankabellapps.LankaBellApps;
import com.example.user.lankabellapps.helper.ColoredSnackbar;

/**
 * Created by Thejan on 2017-08-04.
 */

public class ConnectivityReciver
        extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReciver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

//        if (connectivityReceiverListener != null) {
//            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
//        }

//        if(isConnected){
//
//        }else{
//            new ColoredSnackbar(context).showSnackBar("Please turn on Internet...", new ColoredSnackbar(context).TYPE_ERROR, 5000);
////            Toast.makeText(get, (String)data.result,
////                    Toast.LENGTH_LONG).show();
//        }
    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) LankaBellApps.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}