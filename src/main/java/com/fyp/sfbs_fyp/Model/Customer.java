package com.fyp.sfbs_fyp.Model;

public class Customer {
    private String customerID;
    private String customerName;
    private String customerPhone;
    private String customerEmail;

    // Constructor
    public Customer(String customerID, String customerName, String customerPhone, String customerEmail) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
    }

    public Customer() {
    }

    // Getters and Setters
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String toString() {
        return "Customer ID: " + customerID + "\nCustomer Name: " + customerName + "\nCustomer Phone: " + customerPhone + "\nCustomer Email: " + customerEmail;
    }
}
