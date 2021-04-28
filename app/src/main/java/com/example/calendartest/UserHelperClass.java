package com.example.calendartest;

public class UserHelperClass {
    private String firstname, lastname, phone, email, password;

    public UserHelperClass(){

    }

    public UserHelperClass(String firstname, String lastname, String phone, String email, String password){
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public String getFirstname() { return this.firstname; }
    public String getLastname() {
        return this.lastname;
    }
    public String getPhone() {
        return this.phone;
    }
    public String getEmail() {
        return this.email;
    }
    public String getPassword() { return password; }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) { this.password = password; }
}
