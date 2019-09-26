package com.example.user.lankabellapps.helper;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by thejan on 2017-04-05.
 */

public class CommonHelperClass {

    public static boolean appInstalledOrNot(Context context, String uri) {

        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    public static int getApplicationIcon(Context context, String name){

        try {

            int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            return id;
        }catch (Exception e){

        }

        return 0;
    }

}
