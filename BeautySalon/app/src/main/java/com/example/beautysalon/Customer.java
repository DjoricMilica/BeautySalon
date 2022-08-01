package com.example.beautysalon;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;
    private String mobilePhone;
    private String password;

    public Customer(){

    }
    public Customer(String firstName, String lastName, String eMail, String mobileNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = eMail;
        this.mobilePhone = mobileNumber;
        //this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
