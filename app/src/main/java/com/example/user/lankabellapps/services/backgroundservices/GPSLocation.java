package com.example.user.lankabellapps.services.backgroundservices;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

/**
 * Created by chathura on 11/20/16.
 */

public class GPSLocation {

    private int id;
    private String UserName = "";
    private double Lat;
    private double lon;
    private Date takenAt;
    private double accuracy;
    private String gpsSource;
    private String uuid;
    private int isSynced =0;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getGpsSource() {
        return gpsSource;
    }

    public void setGpsSource(String gpsSource) {
        this.gpsSource = gpsSource;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }


    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put("userId",getUserName());
        cv.put("lat",getLat());
        cv.put("lon",getLon());
        cv.put("accuracy",getAccuracy());
        cv.put("takenAt", getTakenAt().getTime());
        cv.put("gpsSource",getGpsSource());
        cv.put("uuid",getUuid());
        cv.put("is_synced",getIsSynced());
        return  cv;
    }


    public static GPSLocation getDBInstance(Cursor cursor){
        GPSLocation gpsLocation = new GPSLocation();
        int index = 0;
        gpsLocation.setId(cursor.getInt(index++));
        gpsLocation.setUserName(cursor.getString(index++));
        gpsLocation.setLat(Double.parseDouble(cursor.getString(index++)));
        gpsLocation.setLon(Double.parseDouble(cursor.getString(index++)));
        gpsLocation.setAccuracy(Double.parseDouble(cursor.getString(index++)));
        gpsLocation.setTakenAt(new Date(cursor.getLong(index++)));
        gpsLocation.setGpsSource(cursor.getString(index++));
        gpsLocation.setUuid(cursor.getString(index++));
        gpsLocation.setIsSynced(cursor.getInt(index++));
        return gpsLocation;
    }


    @Override
    public String toString() {
        return "GPSLocation{" +
                "id=" + id +
                ", UserName='" + UserName + '\'' +
                ", Lat=" + Lat +
                ", lon=" + lon +
                ", takenAt=" + takenAt +
                ", accuracy=" + accuracy +
                ", gpsSource='" + gpsSource + '\'' +
                ", uuid='" + uuid + '\'' +
                ", isSynced=" + isSynced +
                '}';
    }
}
