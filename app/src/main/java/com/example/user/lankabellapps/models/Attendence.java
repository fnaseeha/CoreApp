package com.example.user.lankabellapps.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created by Thejan on 2016-12-20.
 */
@Table(name = "Attendence")
public class Attendence extends Model {

    @Column(name = "UserId")
    public String userName;

    @Column(name = "AttendenceIn")
    public String attendenceIn;

    @Column(name = "AttendenceOut")
    public String attendenceOut;

    @Column(name = "AttendenceDate")
    public String attendenceDate;

    @Column(name = "TeamCode")
    public String TeamCode;

    @Column(name = "BikeNumber")
    public String BikeNumber;

    @Column(name = "MeterValue1")
    public String MeterValue1;

    @Column(name = "MeterValue2")
    public String MeterValue2;

    @Column(name = "SyncTimeIn")
    public String syncTimeIn;

    @Column(name = "SyncTimeOut")
    public String syncTimeOut;

    @Column(name = "rallType")
    public String rallType;

    @Column(name = "issyncON")
    private String issyncON;

    @Column(name = "issyncOFF")
    private String issyncOFF;



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAttendenceIn() {
        return attendenceIn;
    }

    public void setAttendenceIn(String attendenceIn) {
        this.attendenceIn = attendenceIn;
    }

    public String getAttendenceOut() {
        return attendenceOut;
    }

    public void setAttendenceOut(String attendenceOut) {
        this.attendenceOut = attendenceOut;
    }

    public String getAttendenceDate() {
        return attendenceDate;
    }

    public void setAttendenceDate(String attendenceDate) {
        this.attendenceDate = attendenceDate;
    }

    public String getSyncTime() {
        return syncTimeIn;
    }

    public void setSyncTime(String syncTime) {
        this.syncTimeIn = syncTime;
    }

    public String getTeamCode() {
        return TeamCode;
    }

    public void setTeamCode(String teamCode) {
        TeamCode = teamCode;
    }

    public String getBikeNumber() {
        return BikeNumber;
    }

    public void setBikeNumber(String bikeNumber) {
        BikeNumber = bikeNumber;
    }

    public String getMeterValue() {
        return MeterValue1;
    }

    public void setMeterValue(String meterValue) {
        MeterValue1 = meterValue;
    }

    public String getMeterValue1() {
        return MeterValue1;
    }

    public void setMeterValue1(String meterValue1) {
        MeterValue1 = meterValue1;
    }

    public String getMeterValue2() {
        return MeterValue2;
    }

    public void setMeterValue2(String meterValue2) {
        MeterValue2 = meterValue2;
    }


    public String getSyncTimeIn() {
        return syncTimeIn;
    }

    public void setSyncTimeIn(String syncTimeIn) {
        this.syncTimeIn = syncTimeIn;
    }

    public String getSyncTimeOut() {
        return syncTimeOut;
    }

    public void setSyncTimeOut(String syncTimeOut) {
        this.syncTimeOut = syncTimeOut;
    }

    public String getRallType() {
        return rallType;
    }

    public void setRallType(String rallType) {
        this.rallType = rallType;
    }

    public List<Attendence> getAllAttendenc() {
        return new Select()
                .from(Attendence.class)
                .execute();
    }

    public List<Attendence> getAttendenceByDate(String date) {
        return new Select()
                .from(Attendence.class)
                .where("AttendenceDate = ?", date)
                .execute();
    }

    public List<Attendence> getAttendanceSyncInFlag(String date,String id){
        return new Select()
                .from(Attendence.class)
                .where("issyncON = ? AND AttendenceDate = ?",id,date)
                .execute();
    }
    public List<Attendence> getAttendanceSyncOffFlag(String date,String id){
        return new Select()
                .from(Attendence.class)
                .where("issyncOFF = ? AND AttendenceDate = ?",id,date)
                .execute();
    }


    public void updateAttendenceIN(String timeM, String teamCode, String bikeNumber, String metervalue, String date) {

        String updateSet = "AttendenceIn = ? ," +
                "TeamCode = ? ," +
                "BikeNumber = ? ," +
                "MeterValue1 = ?";

        new Update(Attendence.class)
                .set(updateSet, timeM, teamCode, bikeNumber, metervalue)
                .where("AttendenceDate = ?", date)
                .execute();
    }


//    public void updateLcoation(String cusCode, String lati, String longi, String province, String district, String city, String date){
//
//        String updateSet = "lati = ? ," +
//                "longi = ? ," +
//                "province = ? ," +
//                "district = ? ," +
//                "city = ? ," +
//                "registeredDate = ?";
//
//        new Update(LocationRegisterWithCustomer.class)
//
//                .set(updateSet, lati, longi, province , district , city, date)
//                .where("cusCode = ? ", cusCode)
//                .execute();
//    }


    public void updateSynFlagIn(String syncId){

       new Update(Attendence.class)
               .set("issyncON = ? ",syncId)
               .execute();
    }
    public void updateSynFlagOut(String syncId){

       new Update(Attendence.class)
               .set("issyncOFF = ? ",syncId)
               .execute();
    }
    public void updateAttendenceOut(String timeM, String teamCode, String bikeNumber, String metervalue, String date) {

        String updateSet = "AttendenceOut = ? ," +
                "TeamCode = ? ," +
                "BikeNumber = ? ," +
                "MeterValue2 = ?";

        new Update(Attendence.class)
                .set(updateSet, timeM, teamCode, bikeNumber, metervalue)
                .where("AttendenceDate = ?", date)
                .execute();
    }


    public void updateSyncIn(String syncTime, String date) {


        new Update(Attendence.class)
                .set("SyncTimeIn = ?", syncTime)
                .where("AttendenceDate = ?", date)
                .execute();
    }

    public void updateSyncOut(String syncTime, String date) {


        new Update(Attendence.class)
                .set("SyncTimeOut = ?", syncTime)
                .where("AttendenceDate = ?", date)
                .execute();
    }

    public void clearTable() {
        new Delete().from(Attendence.class).execute();
    }


    public String getIssyncON() {
        return issyncON;
    }

    public void setIssyncON(String issyncON) {
        this.issyncON = issyncON;
    }

    public String getIssyncOFF() {
        return issyncOFF;
    }

    public void setIssyncOFF(String issyncOFF) {
        this.issyncOFF = issyncOFF;
    }
}
