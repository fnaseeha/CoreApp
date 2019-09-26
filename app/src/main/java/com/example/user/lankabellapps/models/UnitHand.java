package com.example.user.lankabellapps.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "UnitHand")
public class UnitHand extends Model{

    @Column(name = "SerialNo")
    public String SerialNo;

    @Column(name = "ItemCode")
    public String ItemCode;

    @Column(name = "Status")
    public String Status;

    @Column(name = "IssueNo")
    public String IssueNo;

    @Column(name = "IssuedBy")
    public String IssuedBy;

    @Column(name = "IssuedDate")
    public String IssuedDate;

    @Column(name = "Type")
    public String Type;

    public UnitHand(){
        super();
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getIssueNo() {
        return IssueNo;
    }

    public void setIssueNo(String issueNo) {
        IssueNo = issueNo;
    }

    public String getIssuedBy() {
        return IssuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        IssuedBy = issuedBy;
    }

    public String getIssuedDate() {
        return IssuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        IssuedDate = issuedDate;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public List<UnitHand> getAlUnitHand() {
        return new Select()
                .from(UnitHand.class)
                .execute();
    }
    public List<UnitHand> getSelectedUnitHand(String type) {
        if(type.equalsIgnoreCase("All")){
            return new Select()
                    .from(UnitHand.class)
                    .execute();
        }else {
            return new Select()
                    .from(UnitHand.class)
                    .where("Type = ?", type)
                    .execute();
        }
    }

    public int getUnitCount(String type){
        return  new Select()
                    .from(UnitHand.class)
                    .where("Type = ?", type)
                    .execute().size();
    }
    public int getAllUnitCount(){
        return  new Select()
                    .from(UnitHand.class)
                    .execute().size();
    }

    public void clearTable() {
        new Delete().from(UnitHand.class)
                .execute();
    }
}
