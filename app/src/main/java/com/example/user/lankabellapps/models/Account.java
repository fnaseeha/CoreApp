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
@Table(name = "Account")
public class Account extends Model {


  //  {\"CpIdnumber\":\"BUS\",\"AcAccountCategory\":\"ACJU\",\"AccountName\":\"01\",\"Title\":\"COM\",\"AddressLine1\":\"510634\",\"AddressLine2\":\"ALL CEYLON JAMMIYYATHUL ULAMA-PRESIDENT\",\"AddressLine3\":\"MS.\",\"City\":\"211\",\"PostalCode\":\"ORABI PASHA STREET\",\"BillrunCode\":null,\"Province\":\"COLOMBO 10\",\"District\":\"00000\",\"CreditLimit\":\"KRISHP\"}

    //{\"CpIdnumber\":\"01\/6436\",\"AcAccountCategory\":\"MED\",\"AccountName\":\"ARADHANA NET CAFE\",\"Title\":\"M\/S.\",\"AddressLine1\":\"NO:51\",\"AddressLine2\":\"2ND FLOOR\",\"AddressLine3\":\"NEW SHOPPING COMPLEX\",\"City\":\"KURUNEGALA\",\"PostalCode\":\"00000\",\"BillrunCode\":null,\"Province\":\"NORTH WESTERN\",\"District\":\"KURUNEGALA\",\"CreditLimit\":30000,\"AccountCode\":\"3007364\",\"AccountType\":\"B\",\"SalesPerson\":\"NIPUNAU\"}

    @Column(name = "CompanyName")
    public String companyName;

    @Column(name = "CpIdnumber")
    public String CpIdnumber;

    @Column(name = "Accountcode")
    public String accountCode;

    @Column(name = "AccountName")
    public String accountName;

    @Column(name = "Title")
    public String title;

    @Column(name = "AddressLine1")
    public String addressLine1;

    @Column(name = "AddressLine2")
    public String addressLine2;

    @Column(name = "AddressLine3")
    public String addressLine3;

    @Column(name = "City")
    public String city;

    @Column(name = "Province")
    public String province;

    @Column(name = "District")
    public String district;

    @Column(name = "PostalCode")
    public String postalCode;

    @Column(name = "AccountType")
    public String accountType;

    @Column(name = "SalesPerson")
    public String salesPerson;

    @Column(name = "TopCustomerState")
    public String topCustomerState;

    @Column(name = "CreditLimit")
    public String creditLimit;

    @Column(name = "RevenueRegion")
    public String revenueRegion;

    @Column(name = "DiscountedStatus")
    public String discountedStatus;

    @Column(name = "CompanyCode")
    public String companyCode;

    @Column(name = "RentalCaregory  ")
    public String rentalCaregory;

    @Column(name = "OS")
    public String os;

    @Column(name = "Payment")
    public int payment;

    @Column(name = "DueDate")
    public String dueDate;

    @Column(name = "BillrunCode")
    public String BillrunCode;

//    @Column(name = "SalesPerson")
//    public String SalesPerson;

    public Account(){
        super();
    }



    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
    }

    public String getTopCustomerState() {
        return topCustomerState;
    }

    public void setTopCustomerState(String topCustomerState) {
        this.topCustomerState = topCustomerState;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getRevenueRegion() {
        return revenueRegion;
    }

    public void setRevenueRegion(String revenueRegion) {
        this.revenueRegion = revenueRegion;
    }

    public String getDiscountedStatus() {
        return discountedStatus;
    }


    public void setDiscountedStatus(String discountedStatus) {
        this.discountedStatus = discountedStatus;
    }



    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getRentalCaregory() {
        return rentalCaregory;
    }

    public void setRentalCaregory(String rentalCaregory) {
        this.rentalCaregory = rentalCaregory;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCpIdnumber() {
        return CpIdnumber;
    }

    public void setCpIdnumber(String cpIdnumber) {
        CpIdnumber = cpIdnumber;
    }

    public String getBillrunCode() {
        return BillrunCode;
    }

    public void setBillrunCode(String billrunCode) {
        BillrunCode = billrunCode;
    }



    public List<Account> getAllAcounts(){
        return new Select()
                .from(Account.class)
                .execute();
    }

    public List<Account> getAccountsByCompanyCode(String companyCode){
        return new Select()
                .from(Account.class)
                .where("CompanyCode = ?",companyCode)
                .execute();
    }

    public List<Account> getAccountsByAccountCode(String accountCode){
        return new Select()
                .from(Account.class)
                .where("Accountcode = ?",accountCode)
                .execute();
    }

    public void updateAccount(String accountNo, int payment ){
        new Update(Account.class)
                .set("Payment = ?", payment)
                .where("Accountcode = ?", accountNo)
                .execute();
    }

    public void updateDueDate(String date, String accCode ){
        new Update(Account.class)
                .set("DueDate = ?", date)
                .where("Accountcode = ?", accCode)
                .execute();
    }



    public void clearTable(){
        new Delete().from(Account.class).execute();
    }


}
