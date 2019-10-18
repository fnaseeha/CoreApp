package com.example.user.lankabellapps.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created by thejan on 2016-10-19.
 */
@Table(name = "AvailableApps", id = BaseColumns._ID)
public class AvailableApps extends Model {

    @Column(name = "appId")
    public String appId;

    @Column(name = "appName")
    public String appName;

    @Column(name = "package")
    public String packagename;

    @Column(name = "iconName")
    public String iconName;

    @Column(name = "lastUpdatedName")
    public String date;

    @Column(name = "version")
    public String version;

    @Column(name = "url")
    public String url;

    @Column(name = "sysSync")
    public String sysSync;

    @Column(name = "iconUrl")
    public String iconUrl;

    @Column(name = "updateAvailable")
    public int updateAvailable;

    @Override
    public String toString() {
        return "AvailableApps{" +
                "appId='" + appId + '\'' +
                ", appName='" + appName + '\'' +
                ", packagename='" + packagename + '\'' +
                ", iconName='" + iconName + '\'' +
                ", date='" + date + '\'' +
                ", version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", sysSync='" + sysSync + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", updateAvailable=" + updateAvailable +
                '}';
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUpdateAvailable() {
        return updateAvailable;
    }

    public void setUpdateAvailable(int updateAvailable) {
        this.updateAvailable = updateAvailable;
    }

    public String getSysSync() {
        return sysSync;
    }

    public void setSysSync(String sysSync) {
        this.sysSync = sysSync;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<AvailableApps> getAllApps(){
        return new Select()
                .from(AvailableApps.class)
                .execute();
    }

    public List<AvailableApps> getFromAppId(String id){
        return new Select()
                .from(AvailableApps.class)
                .where("appId = ?", id)
                .execute();
    }

    public List<AvailableApps> getUpdateAvailableApps(){
        return  new Select()
                .from(AvailableApps.class)
                .where("updateAvailable = ?", 0)
                .execute();
    }

    public void updateUpdateAvailable(String appId, int x){

        new Update(AvailableApps.class)
                .set("updateAvailable =?", x)
                .where("appId = ?", appId)
                .execute();
    }

//    public void updateAttendenceIN(String addedTime, String userName){
//        new Update(Account.class)
//                .set("AttendenceIn = ?", addedTime)
//                .where("UserName = ?", userName)
//                .execute();
//    }



//    public void updateAttendenceIN(String timeM, String userName){
//        new Update(UserProfile.class)
//                .set("InAttendence = ?", timeM)
//                .where("UserName = ?", userName)
//                .execute();
//    }

//    public void updateAttendenceOUT(String timeM, String userName){
//        new Update(UserProfile.class)
//                .set("OutAttendenc = ?", timeM)
//                .where("UserName = ?", userName)
//                .execute();
//    }

    public void clearTable(){
        new Delete().from(AvailableApps.class).execute();
    }


}

