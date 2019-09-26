package com.example.user.lankabellapps.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created by user on 2017-09-01.
 */

@Table(name = "CusVisit")
public class CusVisit extends Model {


    @Column(name = "cusId")
    public String cusId;

    @Column(name = "cusName")
    public String cusName;

    @Column(name = "address")
    public String address;

    @Column(name = "contactNo")
    public String contactNo;

    @Column(name = "datetime")
    public String datetime;

    @Column(name = "date")
    public String date;

    @Column(name = "lati")
    public String lati;

    @Column(name = "longi")
    public String longi;

    @Column(name = "Remarks")
    public String remarks;

    @Column(name = "isSycned")
    public String isSycned;

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void clearTable(){
        new Delete().from(CusVisit.class).execute();
    }

    public String getIsSycned() {
        return isSycned;
    }

    public void setIsSycned(String isSycned) {
        this.isSycned = isSycned;
    }

    public List<CusVisit> getAllData(){
        return new Select()
                .from(CusVisit.class)
                .execute();
    }

    public List<CusVisit> getAllDataNotSynced(){
        return new Select()
                .from(CusVisit.class)
                .where("isSycned = ?", "0")
                .execute();
    }

    public List<CusVisit> getVisitsByDate(String mdate){
        return new Select()
                .from(CusVisit.class)
                .where("date = ?", mdate)
                .execute();
    }


    public void updateIsSynced(String addedTime, String status) {
        new Update(CusVisit.class)
                .set("isSycned = ?", status)
                .where("datetime = ?", addedTime)
                .execute();
    }


}
