package com.example.user.lankabellapps.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created by Thejan on 2016-11-01.
 */
@Table(name = "LocationData")
public class TrackingData extends Model {

    @Column(name = "collectorId")
    public String CollectorID;

    @Column(name = "Lati")
    public Double lati;

    @Column(name = "Longi")
    public Double longi;

    @Column(name = "addedTime")
    public String addedTime;

    @Column(name = "accracy")
    public String accuracy;

    @Column(name = "speed")
    public String speed;

    @Column(name = "useTime")
    public String useTime;

    @Column(name = "isSynced")
    public String isScyned;

    public String getCollectorID() {
        return CollectorID;
    }

    public void setCollectorID(String collectorID) {
        CollectorID = collectorID;
    }

    public Double getLati() {
        return lati;
    }

    public void setLati(Double lati) {
        this.lati = lati;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getIsScyned() {
        return isScyned;
    }

    public void setIsScyned(String isScyned) {
        this.isScyned = isScyned;
    }

    public List<TrackingData> getAllLocationData() {
        return new Select()
                .from(TrackingData.class)
                .execute();
    }

    public void updateIsSynced(String addedTime, String status) {
        new Update(TrackingData.class)
                .set("isSynced = ?", status)
                .where("addedTime = ?", addedTime)
                .execute();
    }


    public List<TrackingData> getUpdatesByisSynced() {
        return new Select()
                .from(TrackingData.class)
                .where("isSynced = ?", "false")
                .execute();
    }

    public List<TrackingData> getTrackingDataByDate(String date) {
        return new Select()
                .from(TrackingData.class)
                .where("addedTime = ?", date)
                .execute();
    }

    public List<TrackingData> getTrackingDataByDateOnly(String date) {
        return new Select()
                .from(TrackingData.class)
                .where("addedTime LIKE  '%"+date+"%' AND isSynced = 'false'")
                .execute();
    }




    public void clearTable() {
        new Delete().from(TrackingData.class).execute();
    }


    public void deleteSycnedTrackingData() {
        new Delete().from(TrackingData.class).where("isSynced = ?", "true").execute();
    }
}
