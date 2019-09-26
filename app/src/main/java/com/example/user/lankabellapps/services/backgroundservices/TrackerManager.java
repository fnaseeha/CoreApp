package com.example.user.lankabellapps.services.backgroundservices;

import android.content.Context;
import android.location.Location;

import java.util.ArrayList;

/**
 * Created by chathura on 11/20/16.
 */

public class TrackerManager {

   // private final TrackingDao rTracking;
    private Context context;



    public TrackerManager(Context context) {
        this.context = context;
        //rTracking = new TrackingDao(context);
    }

    public int saveLocation(Location location){
//        GPSLocation gps = new GPSLocation();
//        gps.setIsSynced(0);
//        gps.setLon(location.getLongitude());
//        gps.setLat(location.getLatitude());
//        gps.setAccuracy(location.getAccuracy());
//        gps.setUserName(SharedPrefManager.getLoggedUser(this.context));
//        gps.setUuid(Utils.getSimSerialNumber(this.context));
//        gps.setTakenAt(new Date(location.getTime()));
//        gps.setGpsSource(location.getProvider());
//        return rTracking.insertGPSLocation(gps);
        return 0;
    }

    public ArrayList<GPSLocation> getUnSynced(boolean isMax){
        // return  rTracking.getUnSynced((isMax) ? CONSTANTS.LOCATION_UPDATE_MAX_LIMIT : CONSTANTS.LOCATION_UPDATE_LIMIT);
        return null;

    }

    public int updateSynced(ArrayList<GPSLocation> list){
//        try {
//            int rowCount =  rTracking.delete(list);
//            Utils.writeToLogFileWithTime("deleted locs : " + rowCount);
//            return  rowCount;
//        } catch (Exception e) {
//            e.printStackTrace();
//            Utils.writeToErrLogFileWithTime(e);
//        }
        return 0;
    }

}
