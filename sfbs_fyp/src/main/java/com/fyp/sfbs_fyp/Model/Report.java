package com.fyp.sfbs_fyp.Model;

import java.sql.Date;

public class Report {
    private int reportID;
    private Date reportDate;
    private Booking bookingID;
    private Staff staffID;
    private double totalSale;

    // Constructor
    public Report(int reportID, Date reportDate, Booking bookingID, Staff staffID, double totalSale) {
        this.reportID = reportID;
        this.reportDate = reportDate;
        this.bookingID = bookingID;
        this.staffID = staffID;
        this.totalSale = totalSale;
    }

    // Getters and Setters
    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Booking getBookingID() {
        return bookingID;
    }

    public void setBookingID(Booking bookingID) {
        this.bookingID = bookingID;
    }

    public Staff getStaffID() {
        return staffID;
    }

    public void setStaffID(Staff staffID) {
        this.staffID = staffID;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }
}
