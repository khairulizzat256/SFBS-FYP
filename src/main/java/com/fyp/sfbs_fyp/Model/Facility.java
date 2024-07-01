package com.fyp.sfbs_fyp.Model;


public class Facility {
    private String facilityName;
    private String facilityID;
    private double facilityPrice;
    private String facilityOpenTime;
    private String facilityCloseTime;

    public Facility(String facilityName, String facilityID, double facilityPrice, String facilityOpenTime, String facilityCloseTime) {
        this.facilityName = facilityName;
        this.facilityID = facilityID;
        this.facilityPrice = facilityPrice;
        this.facilityOpenTime = facilityOpenTime;
        this.facilityCloseTime = facilityCloseTime;
    }
    public Facility() {
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public double getFacilityPrice() {
        return facilityPrice;
    }

    public void setFacilityPrice(double facilityPrice) {
        this.facilityPrice = facilityPrice;
    }

    public String getFacilityOpenTime() {
        return facilityOpenTime;
    }

    public void setFacilityOpenTime(String facilityOpenTime) {
        this.facilityOpenTime = facilityOpenTime;
    }

    public String getFacilityCloseTime() {
        return facilityCloseTime;
    }

    public void setFacilityCloseTime(String facilityCloseTime) {
        this.facilityCloseTime = facilityCloseTime;
    }

    public String toString() {
        return "Facility Name: " + facilityName + "\nFacility ID: " + facilityID + "\nFacility Price: " + facilityPrice + "\nFacility Open Time: " + facilityOpenTime + "\nFacility Close Time: " + facilityCloseTime;
    }

  
}
