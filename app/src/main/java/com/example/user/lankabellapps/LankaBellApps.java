package com.example.user.lankabellapps;

import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.user.lankabellapps.helper.AppVisibilityDetector;
import com.example.user.lankabellapps.helper.ColoredSnackbar;
import com.example.user.lankabellapps.services.backgroundservices.ConnectivityReciver;

/**
 * Created by Thejan on 2016-10-19.
 */


public class LankaBellApps extends com.activeandroid.app.Application implements ConnectivityReciver.ConnectivityReceiverListener {

    private static LankaBellApps mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        ActiveAndroid.initialize(this);
        mInstance = this;

        //setConnectivityListener(this);

//        AppVisibilityDetector.init(LankaBellApps.this, new AppVisibilityDetector.AppVisibilityCallback() {
//            @Override
//            public void onAppGotoForeground() {
//                //app is from background to foreground
//                Toast.makeText(getBaseContext(), "App Forground",
//                    Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onAppGotoBackground() {
//                //app is from foreground to background
//                Toast.makeText(getBaseContext(), "App Background",
//                    Toast.LENGTH_LONG).show();
//            }
//        });


    }

    public static synchronized LankaBellApps getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReciver.ConnectivityReceiverListener listener) {
        ConnectivityReciver.connectivityReceiverListener = listener;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }




    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
//        System.out.println("internet offf onn");
//        new ColoredSnackbar(getBaseContext()).showSnackBar("Please turn on Internet...", new ColoredSnackbar(getBaseContext()).TYPE_ERROR, 5000);
//        if (!isConnected)
//            Toast.makeText(getBaseContext(), "Please turn on Internet..",
//                    Toast.LENGTH_LONG).show();
    }
}
