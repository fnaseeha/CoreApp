package com.example.user.lankabellapps.models;

public class Sustitute {

    private String empNo;
    private String Name;
    private String date;
    private String NoOfDay;
    private String type;

    public Sustitute(String empNo, String name, String date, String noOfDay,  String type) {
        this.empNo = empNo;
        Name = name;
        this.date = date;
        NoOfDay = noOfDay;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
