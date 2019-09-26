package com.example.user.lankabellapps.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created by Thejan on 2017-07-05.
 */
@Table(name = "Merchants")
public class Merchants extends Model {

    @Column(name = "merchantId")
    public String merchantId;

    @Column(name = "merchantName")
    public String merchantName;

    @Column(name = "address")
    public String address;

    @Column(name = "regBy")
    public String regBy;

    @Column(name = "telephone")
    public String telephone;

    @Column(name = "city")
    public String mcity;

    @Column(name = "bankAccId")
    public String bankAccId;

    @Column(name = "bank")
    public String bank;

    @Column(name = "nic")
    public String nic;

    @Column(name = "agrimentStatus")
    public String agrimentStatus;

    @Column(name = "Lati")
    public String lati;

    @Column(name = "Longi")
    public String longi;

    @Column(name = "isSynced")
    public String isSynced;

    @Column(name = "bankAccName")
    public String bankAccName;

    public String getBankAccName() {
        return bankAccName;
    }

    public void setBankAccName(String bankAccName) {
        this.bankAccName = bankAccName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegBy() {
        return regBy;
    }

    public void setRegBy(String regBy) {
        this.regBy = regBy;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMcity() {
        return mcity;
    }

    public void setMcity(String mcity) {
        this.mcity = mcity;
    }

    public String getBankAccId() {
        return bankAccId;
    }

    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAgrimentStatus() {
        return agrimentStatus;
    }

    public void setAgrimentStatus(String agrimentStatus) {
        this.agrimentStatus = agrimentStatus;
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

    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }

    public List<Merchants> getAllMerchants() {
        return new Select()
                .from(Merchants.class)
                .execute();
    }

    public List<Merchants> getAllNotSyncMerchants() {
        return new Select()
                .from(Merchants.class)
                .where("isSynced = ?", "false")
                .execute();
    }

    public List<Merchants> getMerchantById(String merchantId) {
        return new Select()
                .from(Merchants.class)
                .where("merchantId = ?", merchantId)
                .execute();
    }

    public List<Merchants> getUpdatedMerchants() {
        return new Select()
                .from(Merchants.class)
                .where("isSynced = ?", "x")
                .execute();
    }

    public void updateIsSynced(String merchantId) {
        new Update(Merchants.class)
                .set("isSynced = ?", "true")
                .where("merchantId = ?", merchantId)
                .execute();
    }

    public void updateMerchantsNewAddedData(String address, String telephone, String merchantId, String mBank, String mAccountNumber, String mNic,String mbankAccName) {

        String updateSet = "address =?," +
                "telephone =?," +
                "bank = ?," +
                "bankAccId = ?," +
                "nic = ?," +
                "isSynced = ?,"+
                "bankAccName = ?";

        new Update(Merchants.class)
                .set(updateSet, address, telephone ,mBank, mAccountNumber, mNic, "x",mbankAccName)
                .where("merchantId = ?", merchantId)
                .execute();

    }

    public void clearTable() {
        new Delete().from(Merchants.class).execute();
    }

    public void clearTableNotSynced() {
        new Delete().from(Merchants.class)
                .where("isSynced = ?", "true")
                .execute();
    }


}
