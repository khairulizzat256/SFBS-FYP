package com.fyp.sfbs_fyp.Model;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;

public class Booking {

    @DocumentId
    private String bookingID;
    private String bookingType;
    private String bookingDate;
    private String bookingStartTime;
    private String bookingEndTime;
    private String description;
    private Facility facilityID;
    private Customer customerID;
    private Company companyID;
    private String status;
    private Double totalamount;

    
    
    
    public Booking(String bookingID, String bookingType, String bookingDate, String bookingStartTime,
            String bookingEndTime, String description, Facility facilityID, Customer customerID, Company companyID,
            String status, Double totalamount) {
        this.bookingID = bookingID;
        this.bookingType = bookingType;
        this.bookingDate = bookingDate;
        this.bookingStartTime = bookingStartTime;
        this.bookingEndTime = bookingEndTime;
        this.description = description;
        this.facilityID = facilityID;
        this.customerID = customerID;
        this.companyID = companyID;
        this.status = status;
        this.totalamount = totalamount;
    }

    //Constructor with CompanyID
    public Booking(String bookingID, String bookingType, String bookingDate, String bookingStartTime, String bookingEndTime, String description, Facility facilityID, Customer customerID, Company companyID) {
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
    public Booking(String bookingID, String bookingType, String bookingDate, String bookingStartTime, String bookingEndTime,
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


    public Booking() {
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingStartTime() {
        return bookingStartTime;
    }

    public void setBookingStartTime(String bookingStartTime) {
        this.bookingStartTime = bookingStartTime;
    }

    public String getBookingEndTime() {
        return bookingEndTime;
    }

    public void setBookingEndTime(String bookingEndTime) {
        this.bookingEndTime = bookingEndTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setTotalamount(Double totalamount) {
        this.totalamount = totalamount;
    }

    public Double getTotalamount() {
        return totalamount;
    }

    @PropertyName("facility")
    public Facility getFacilityID() {
        return facilityID;
    }
    @PropertyName("facility")
    public void setFacilityID(Facility facilityID) {
        this.facilityID = facilityID;
    }
    @PropertyName("customer")
    public Customer getCustomerID() {
        return customerID;
    }
    @PropertyName("customer")
    public void setCustomerID(Customer customerID) {
        this.customerID = customerID;
    }
    @PropertyName("company")
    public Company getCompanyID() {
        return companyID;
    }
    @PropertyName("company")
    public void setCompanyID(Company companyID) {
        this.companyID = companyID;
    }

    public String toString() {
        return "Booking ID: " + bookingID + "\nBooking Type: " + bookingType + "\nBooking Date: " + bookingDate
                + "\nBooking Start Time: " + bookingStartTime + "\nBooking End Time: " + bookingEndTime
                + "\nDescription: " + description + "\nFacility ID: " + facilityID + "\nCustomer ID: " + customerID
                + "\nCompany ID: " + companyID;
    }
}
