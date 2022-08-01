package com.example.beautysalon;

public class Reservation {
    String time;
    String date;
    String customerID;
    String serviceID;

    public Reservation(){

    }
    public Reservation(String time, String date, String customerID, String serviceID) {
        this.time = time;
        this.date = date;
        this.customerID = customerID;
        this.serviceID = serviceID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}
