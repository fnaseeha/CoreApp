package com.example.user.lankabellapps.services.backgroundservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.user.lankabellapps.activities.AttendenceActivity;
import com.example.user.lankabellapps.helper.DateTimeSelectView;
import com.example.user.lankabellapps.models.TrackingData;

import java.util.List;

import static com.example.user.lankabellapps.activities.AttendenceActivity.unsyncCount;


/**
 * Created by Thejan on 1/28/2017.
 */
public class SaveTrackings extends BroadcastReceiver {


    double lat, lon;
    String speed, accuracy, usetime, addedtime;

    @Override
    public void onReceive(Context context, Intent intent) {


        SampleTrackingService.Action todo = (SampleTrackingService.Action) intent.getSerializableExtra(SampleTrackingService.MSGPARSE);

        //  Log.d("TrackingService", todo.toString());
        //System.out.println("AAAAAAAAAAAA");


        switch (todo) {
            case START:

                break;

            case STOP:

                break;

            case GPSERROR:
                //showMessage("GPS Error", "Can't get a GPS lock.");
                break;

            case GETLOCATION:
                lat = intent.getDoubleExtra("latitude", 0);
                lon = intent.getDoubleExtra("longitude", 0);
                speed = intent.getStringExtra("speed");
                accuracy = intent.getStringExtra("accuracy");
                usetime = intent.getStringExtra("usetime");
                addedtime = intent.getStringExtra("addedtime");

                System.out.println("BroadCatsing");


                Log.d("Service latitude", Double.toString(lat));
                //DisplayMap(lat, lon);

                addToDB(lat,lon,addedtime,accuracy,speed,usetime);

                break;

        }



//IntentFilter intentFilter = new IntentFilter("android.intent.action.TRACK");
//        this.registerReceiver(mReceiver, intentFilter);

    }


    private void addToDB(double lati, double longi, String addedtime, String accuracy, String speed, String usetime) {

        System.out.println("Saved Tracking");
        System.out.println("* Saving Tracking");

        TrackingData trackingService = new TrackingData();

        trackingService.setAddedTime(addedtime);
        trackingService.setLati(lati);
        trackingService.setLongi(longi);
        trackingService.setAccuracy(accuracy);
        trackingService.setSpeed(speed);
        trackingService.setCollectorID("");
        trackingService.setUseTime(usetime);
        trackingService.setIsScyned("false");

        trackingService.save();

        List<TrackingData> gg1 = new TrackingData().getTrackingDataByDateOnly(DateTimeSelectView.getCurrentTimeOnly());// getTrackingDataByDate
        // List<TrackingData> gg =tr.getUpdatesByisSynced();
        //  System.out.println("* gg "+gg.size());
        System.out.println("* gg1 "+gg1.size());


        try{
            if(gg1.size()>0 && AttendenceActivity.unsyncCount != null){
                AttendenceActivity.unsyncCount.setText("UnSynced GPS count : "+gg1.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
