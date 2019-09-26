package com.example.user.lankabellapps.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created by user on 2017-07-07.
 */
@Table(name = "TSRDetails")
public class TSRDetails extends Model {

    @Column(name = "EpfNo")
    public String EpfNo;

    @Column(name = "NextMerchantNo")
    public String NextMerchantNo;

    public String getEpfNo() {
        return EpfNo;
    }

    public void setEpfNo(String epfNo) {
        EpfNo = epfNo;
    }

    public String getNextMerchantNo() {
        return NextMerchantNo;
    }

    public void setNextMerchantNo(String nextMerchantNo) {
        NextMerchantNo = nextMerchantNo;
    }

    public List<TSRDetails> getAllData(){
        return new Select()
                .from(TSRDetails.class)
                .execute();
    }

    public void updateIsSynced( String nextMerchantNumber) {
        new Update(TSRDetails.class)
                .set("NextMerchantNo = ?", nextMerchantNumber)
                .execute();
    }

    public void clearTable(){
        new Delete().from(TSRDetails.class).execute();
    }
}
