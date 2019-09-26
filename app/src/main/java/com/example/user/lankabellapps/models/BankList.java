package com.example.user.lankabellapps.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by user on 2017-07-12.
 */
@Table(name = "Bank")
public class BankList extends Model {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BankList> getAllBanks(){
        return new Select()
                .from(BankList.class)
                .execute();
    }

    public void clearTable(){
        new Delete().from(BankList.class).execute();
    }

}
