package com.fyp.sfbs_fyp.Model;

import java.sql.Time;

public class Facility {
    private String facilityName;
    private String facilityID;
    private double facilityPrice;
    private Time facilityOpenTime;
    private Time facilityCloseTime;

    public Facility(String facilityName, String facilityID, double facilityPrice, Time facilityOpenTime, Time facilityCloseTime) {
        this.facilityName = facilityName;
        this.facilityID = facilityID;
        this.facilityPrice = facilityPrice;
        this.facilityOpenTime = facilityOpenTime;
        this.facilityCloseTime = facilityCloseTime;
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

    public Time getFacilityOpenTime() {
        return facilityOpenTime;
    }

    public void setFacilityOpenTime(Time facilityOpenTime) {
        this.facilityOpenTime = facilityOpenTime;
    }

    public Time getFacilityCloseTime() {
        return facilityCloseTime;
    }

    public void setFacilityCloseTime(Time facilityCloseTime) {
        this.facilityCloseTime = facilityCloseTime;
    }

  
}
