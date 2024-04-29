package com.fyp.sfbs_fyp.Model;

public class Staff {

    private String staffID;
    private String staffName;
    private String staffEmail;
    private String staffPhone;
    private String staffRole;

    public Staff(String staffID, String staffName, String staffEmail, String staffPhone, String staffRole) {
        this.staffID = staffID;
        this.staffName = staffName;
        this.staffEmail = staffEmail;
        this.staffPhone = staffPhone;
        this.staffRole = staffRole;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }
}
