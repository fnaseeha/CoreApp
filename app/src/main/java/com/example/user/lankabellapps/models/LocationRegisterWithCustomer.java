package com.example.user.lankabellapps.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created by user on 2016-12-14.
 */


@Table(name = "LocationRegisterWithCustomer")
public class LocationRegisterWithCustomer extends Model {


    @Column(name = "cusCode")
    public String cusCode;

    @Column(name = "lati")
    public double lati;

    @Column(name = "longi")
    public double longi;

    @Column(name = "province")
    public String province;

    @Column(name = "district")
    public String district;

    @Column(name = "city")
    public String city;

    @Column(name = "isSynced")
    public String isSynced;

    @Column(name = "isConformed")
    public String isConformed;

    @Column(name = "registeredDate")
    public String registeredDate;

    public String getCusCode() {
        return cusCode;
    }

    public void setCusCode(String cusCode) {
        this.cusCode = cusCode;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }


    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }

    public String getIsConformed() {
        return isConformed;
    }

    public void setIsConformed(String isConformed) {
        this.isConformed = isConformed;
    }


    public List<LocationRegisterWithCustomer> getAll(){
        return new Select()
                .from(LocationRegisterWithCustomer.class)
                .execute();
    }

    public List<LocationRegisterWithCustomer> getLocationRegisterDetailsByCusCode(String cussCode){
        return new Select()
                .from(LocationRegisterWithCustomer.class)
                .where("cusCode = ?", cussCode)
                .execute();
    }

    public void updateLcoation(String cusCode, String lati, String longi, String date){

        String updateSet = "lati = ? ," +
                "longi = ? ," +
                "registeredDate = ?";

        new Update(LocationRegisterWithCustomer.class)

                .set(updateSet, lati, longi, date)
                .where("cusCode = ? ", cusCode)
                .execute();
    }


    public List<LocationRegisterWithCustomer> getUpdatesByisSynced(){
        return new Select()
                .from(LocationRegisterWithCustomer.class)
                .where("isSynced = ?", "false")
                .execute();
    }

    public void updateIsSynced(String cusID, String status){
        new Update(LocationRegisterWithCustomer.class)
                .set("isSynced = ?",status)
                .where("cusCode = ?", cusID)
                .execute();
    }

    public void clearTable(){
        new Delete().from(LocationRegisterWithCustomer.class).execute();
    }


}
