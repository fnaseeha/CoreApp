package com.example.user.lankabellapps.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by user on 2017-07-06.
 */
@Table(name = "MerchantsCities")
public class MerchantsCities extends Model{

    @Column(name = "PostalCode")
    public String postalCode;

    @Column(name = "City")
    public String city;

    @Column(name = "Region")
    public String region;

    @Column(name = "PostOfficeName")
    public String PostOfficeName;

    @Column(name = "Area")
    public String area;

    @Column(name = "Lattitude")
    public String latitude;

    @Column(name = "Longtitude")
    public String longtitude;

    @Column(name = "CURRENT_CHANGES")
    public String currentChanges;

    @Column(name = "APP_ID")
    public String APP_ID;

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostOfficeName() {
        return PostOfficeName;
    }

    public void setPostOfficeName(String postOfficeName) {
        PostOfficeName = postOfficeName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getCurrentChanges() {
        return currentChanges;
    }

    public void setCurrentChanges(String currentChanges) {
        this.currentChanges = currentChanges;
    }

    public String getAPP_ID() {
        return APP_ID;
    }

    public void setAPP_ID(String APP_ID) {
        this.APP_ID = APP_ID;
    }


    public List<MerchantsCities> getAllMerchantsCities(){
        return new Select()
                .from(MerchantsCities.class)
                .execute();
    }

    public void clearTable(){
        new Delete().from(MerchantsCities.class).execute();
    }


}
