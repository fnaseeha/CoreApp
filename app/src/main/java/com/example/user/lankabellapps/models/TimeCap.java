package com.example.user.lankabellapps.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created by Thejan on 2016-12-23.
 */
@Table(name = "TimeCap")
public class TimeCap extends Model {

    @Column(name = "rawName")
    public String rawName;

    @Column(name = "Date")
    public String date;

    //@Column(name = "")


    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<TimeCap> getAllData(){
        return new Select()
                .from(TimeCap.class)
                .execute();
    }

    public List<TimeCap> getTimeCapByName(String rawName){
        return new Select()
                .from(TimeCap.class)
                .where("rawName = ?", rawName)
                .execute();
    }

    public void updateMasterDataTime(String time, String rawName){

        new Update(TimeCap.class)

                .set("Date =?", time)
                .where("rawName = ?", rawName)
                .execute();
    }

    public void clearTable() {
        new Delete().from(TimeCap.class).execute();
    }

}
