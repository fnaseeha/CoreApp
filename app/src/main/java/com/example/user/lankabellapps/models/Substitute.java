package com.example.user.lankabellapps.models;

public class Substitute {

    private String empNo;
    private String Name;
    private String Leavedate;
    private String AppliedDate;
    private String NoOfDay;
    private String type;

    public Substitute(String empNo, String name, String Leavedate, String AppliedDate, String noOfDay, String type) {
        this.empNo = empNo;
        Name = name;
        this.Leavedate = Leavedate;
        NoOfDay = noOfDay;
        this.type = type;
        this.AppliedDate = AppliedDate;
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

    public String getLeavedate() {
        return Leavedate;
    }

    public void setLeavedate(String leavedate) {
        this.Leavedate = leavedate;
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

    public String getAppliedDate() {
        return AppliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        AppliedDate = appliedDate;
    }
}
