package com.fyp.sfbs_fyp.Model;

import java.sql.Date;
import java.sql.Time;

public class Booking {
    private int bookingID;
    private String bookingType;
    private Date bookingDate;
    private Time bookingStartTime;
    private Time bookingEndTime;
    private String description;
    private Facility facilityID;
    private Customer customerID;
    private Company companyID;

    //Constructor with CompanyID
    public Booking(int bookingID, String bookingType, Date bookingDate, Time bookingStartTime, Time bookingEndTime, String description, Facility facilityID, Customer customerID, Company companyID) {
        this.bookingID = bookingID;
        this.bookingType = bookingType;
        this.bookingDate = bookingDate;
        this.bookingStartTime = bookingStartTime;
        this.bookingEndTime = bookingEndTime;
        this.description = description;
        this.facilityID = facilityID;
        this.customerID = customerID;
        this.companyID = companyID;
    }
    
    //Constructor without CompanyID
    public Booking(int bookingID, String bookingType, Date bookingDate, Time bookingStartTime, Time bookingEndTime,
            String description, Facility facilityID, Customer customerID) {
        this.bookingID = bookingID;
        this.bookingType = bookingType;
        this.bookingDate = bookingDate;
        this.bookingStartTime = bookingStartTime;
        this.bookingEndTime = bookingEndTime;
        this.description = description;
        this.facilityID = facilityID;
        this.customerID = customerID;
    }


    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Time getBookingStartTime() {
        return bookingStartTime;
    }

    public void setBookingStartTime(Time bookingStartTime) {
        this.bookingStartTime = bookingStartTime;
    }

    public Time getBookingEndTime() {
        return bookingEndTime;
    }

    public void setBookingEndTime(Time bookingEndTime) {
        this.bookingEndTime = bookingEndTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Facility getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(Facility facilityID) {
        this.facilityID = facilityID;
    }

    public Customer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Customer customerID) {
        this.customerID = customerID;
    }

    public Company getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Company companyID) {
        this.companyID = companyID;
    }
}
