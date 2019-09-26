package com.example.user.lankabellapps.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created by Thejan on 10/19/2016.
 */
@Table(name = "Customers")
public class Customers extends Model{


    @Column(name = "Ic_id_category")
    public String Ic_id_category;

    @Column(name = "Title")
    public String Title;

    @Column(name = "Address_line_1")
    public String Address_line_1;

    @Column(name = "Address_line_2")
    public String Address_line_2;

    @Column(name = "Address_line_3")
    public String Address_line_3;

    @Column(name = "Postal_code")
    public String Postal_code;

    @Column(name = "Modified_by")
    public String Modified_by;

    @Column(name = "Modified_on")
    public String Modified_on;

    @Column(name = "D_first_name")
    public String D_first_name;

    @Column(name = "D_last_name")
    public String D_last_name;

    @Column(name = "Master_profile_id")
    public String Master_profile_id;

    @Column(name = "Sales_person")
    public String Sales_person;



    @Column(name = "accountNo")
    public String accountNo;

    @Column(name = "CompanyName")
    public String namem;

    @Column(name = "CompanyCode")
    public String companyCode;

    @Column(name = "address")
    public String address;

    @Column(name = "contactNo")
    public String contactNo;

    @Column(name = "os")
    public String os;

    @Column(name = "dueDate")
    public String dueDate;

    @Column(name = "register")
    public String register;

    @Column(name = "locStatus")
    public String locStatus;

    @Column(name = "lastPayment")
    public String lastPayment;

    @Column(name = "status")
    public String status;

    @Column(name = "addedTime")
    public String time;

    @Column(name = "mode")
    public String mode;

    @Column(name = "RecivedBy")
    public String recivedBy;

    @Column(name = "Lati")
    public Double lati;

    @Column(name = "Longi")
    public Double longi;

    @Column(name = "Province")
    public String Province;

    @Column(name = "District")
    public String District;

    @Column(name = "City")
    public String City;

    public Customers(){
        super();
    }

    public String getIc_id_category() {
        return Ic_id_category;
    }

    public void setIc_id_category(String ic_id_category) {
        Ic_id_category = ic_id_category;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAddress_line_1() {
        return Address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        Address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return Address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        Address_line_2 = address_line_2;
    }

    public String getAddress_line_3() {
        return Address_line_3;
    }

    public void setAddress_line_3(String address_line_3) {
        Address_line_3 = address_line_3;
    }

    public String getPostal_code() {
        return Postal_code;
    }

    public void setPostal_code(String postal_code) {
        Postal_code = postal_code;
    }

    public String getModified_by() {
        return Modified_by;
    }

    public void setModified_by(String modified_by) {
        Modified_by = modified_by;
    }

    public String getModified_on() {
        return Modified_on;
    }

    public void setModified_on(String modified_on) {
        Modified_on = modified_on;
    }

    public String getD_first_name() {
        return D_first_name;
    }

    public void setD_first_name(String d_first_name) {
        D_first_name = d_first_name;
    }

    public String getD_last_name() {
        return D_last_name;
    }

    public void setD_last_name(String d_last_name) {
        D_last_name = d_last_name;
    }

    public String getMaster_profile_id() {
        return Master_profile_id;
    }

    public void setMaster_profile_id(String master_profile_id) {
        Master_profile_id = master_profile_id;
    }

    public String getSales_person() {
        return Sales_person;
    }

    public void setSales_person(String sales_person) {
        Sales_person = sales_person;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getNamem() {
        return namem;
    }

    public void setNamem(String namem) {
        this.namem = namem;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(String lastPayment) {
        this.lastPayment = lastPayment;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getRecivedBy() {
        return recivedBy;
    }

    public void setRecivedBy(String recivedBy) {
        this.recivedBy = recivedBy;
    }

    public Double getLati() {
        return lati;
    }

    public void setLati(Double lati) {
        this.lati = lati;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }


    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getLocStatus() {
        return locStatus;
    }

    public void setLocStatus(String locStatus) {
        this.locStatus = locStatus;
    }

    public List<Customers> getAllData(){
        return new Select()
                .from(Customers.class)
                .execute();
    }

    public List<Customers> getFilteredData(String companyName, String contactNo){
        return new Select()
                .from(Customers.class)
                .where("CompanyName = ?", companyName)
                .where("contactNo = ?", contactNo)
                .execute();
    }

    public List<Customers> getFilteredByCompanyCode(String companyCode){
        return new Select()
                .from(Customers.class)
                .where("CompanyCode = ?", companyCode)
                .execute();
    }

    public List<Customers> getFilteredByAccountNo(String accountNo){
        return new Select()
                .from(Customers.class)
                .where("accountNo = ?", accountNo)
                .execute();
    }

    public void clearTable(){
        new Delete().from(Customers.class).execute();
    }

    public void updateLocation(Double lati, Double longi, String hasLoc, String companyCode) {

        String updateSet = "Lati = ?," +
                "Longi = ?," +
                "register = ?";

//        new Update(Question.class)
//                .set(updateSet, answerID, userAnswerType)
//                .where(" QID = ? ", questionID)
//                .execute();
//

        /*, "Lati = ?", lati, "Longi = ?", longi*/

        new Update(Customers.class)
                .set(updateSet, lati, longi, hasLoc)
                .where("CompanyCode = ?", companyCode)
                .execute();

//        new Update(Customers.class)
//                .set("register = ?"/*, "Lati = ?", lati, "Longi = ?", longi*/)
//               // .set("register = ?", hasLoc)
//                .where("contactNo = ?", contact)
//                .execute();
    }

    public void updateRegister(String hasLoc, String locStatus , String companyCode) {

        String updateSet = "register = ?," +
                "locStatus = ?";

        new Update(Customers.class)
                .set(updateSet, hasLoc, locStatus)
                .where("CompanyCode = ?", companyCode)
                .execute();
    }

}
