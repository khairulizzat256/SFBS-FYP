package com.fyp.sfbs_fyp.Model;

public class Company {
    private String companyID;
    private String companyName;
    private String companyAddress;
    private String eventName;
    private String service;

    public Company(String companyID, String companyName, String companyAddress, String eventName, String service) {
        this.companyID = companyID;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.eventName = eventName;
        this.service = service;
    }
    public Company() {
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
