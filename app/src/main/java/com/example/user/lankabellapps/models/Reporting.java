package com.example.user.lankabellapps.models;

public class Reporting {

    private String empNo;
    private String Name;
    private String date;
    private String NoOfDay;
    private String Substitute;
    private String type;

    public Reporting(String empNo, String name, String date, String noOfDay, String substitute, String type) {
        this.empNo = empNo;
        Name = name;
        this.date = date;
        NoOfDay = noOfDay;
        Substitute = substitute;
        this.type = type;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNoOfDay() {
        return NoOfDay;
    }

    public void setNoOfDay(String noOfDay) {
        NoOfDay = noOfDay;
    }

    public String getSubstitute() {
        return Substitute;
    }

    public void setSubstitute(String substitute) {
        Substitute = substitute;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
