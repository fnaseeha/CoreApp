package com.example.user.lankabellapps.services.backgroundservices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.models.TimeCap;


/**
 * Created by Thejan on 2016-11-07.
 */


public class TrackBroadCast extends BroadcastReceiver {

//    public static AlarmManager mgr;
//    public static PendingIntent pi;
   // public static int x = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent startServiceIntent1 = new Intent(context, BackgroundService.class);
//        context.startService(startServiceIntent1);

        Intent svrintent = new Intent(context, SampleTrackingService.class);
        context.startService(svrintent);

        System.out.println("BroadcaseReciver Worked");


        final AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, SampleTrackingService.class);

        TimeCap timeCap = new TimeCap();

        if(!timeCap.getTimeCapByName(Constants.ALAM_COUNT).isEmpty()) {
            int x = Integer.parseInt(timeCap.getTimeCapByName(Constants.ALAM_COUNT).get(0).getDate());
            final PendingIntent pi = PendingIntent.getService(context, x, i, 0);
            mgr.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 1000, pi);
            ++x;
        }


//        mgr.setAlarm(AlarmManager.ELAPSED_REALTIME,
//                SystemClock.elapsedRealtime() + 1000, 1000, pi);
//
//
//
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("AlamManager Removed");
//                mgr.cancel(pi);
//            }
//        }, 10000);
//

    }


}