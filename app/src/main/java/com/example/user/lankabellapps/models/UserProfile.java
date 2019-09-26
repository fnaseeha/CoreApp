package com.example.user.lankabellapps.models;

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
@Table(name = "UserProfile")
public class UserProfile extends Model {

    @Column(name = "UserName")
    public String userName;

    @Column(name = "Password")
    public String password;

    @Column(name = "SimNo")
    public String simNo;

    @Column(name = "CollectorCode")
    public String collectorCode;

    @Column(name = "InAttendence")
    public String attendencIn;

    @Column(name = "OutAttendenc")
    public String attendencOut;

    public UserProfile(){
        super();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSimNo() {
        return simNo;
    }

    public void setSimNo(String simNo) {
        this.simNo = simNo;
    }

    public String getCollectorCode() {
        return collectorCode;
    }


    public String getAttendencIn() {
        return attendencIn;
    }

    public void setAttendencIn(String attendencIn) {
        this.attendencIn = attendencIn;
    }

    public String getAttendencOut() {
        return attendencOut;
    }

    public void setAttendencOut(String attendencOut) {
        this.attendencOut = attendencOut;
    }

    public void setCollectorCode(String collectorCode) {
        this.collectorCode = collectorCode;
    }

    public List<UserProfile> getAllUsers(){
        return new Select()
                .from(UserProfile.class)
                .execute();
    }

//    public void updateAttendenceIN(String addedTime, String userName){
//        new Update(Account.class)
//                .set("AttendenceIn = ?", addedTime)
//                .where("UserName = ?", userName)
//                .execute();
//    }



    public void updateAttendenceIN(String timeM, String userName){
        new Update(UserProfile.class)
                .set("InAttendence = ?", timeM)
                .where("UserName = ?", userName)
                .execute();
    }

    public void updateAttendenceOUT(String timeM, String userName){
        new Update(UserProfile.class)
                .set("OutAttendenc = ?", timeM)
                .where("UserName = ?", userName)
                .execute();
    }

    public void clearTable(){
        new Delete().from(UserProfile.class).execute();
    }


}
