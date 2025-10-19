package com.asdeveloper.bloodmate.Models;


public class User {
    private long id;
    private Long dob, lastdonation;
    private String phone, password, name, bloodgroup, emailid;
    private char gender;

    public User() {
    }

    public User(long id, Long dob, Long lastdonation, String phone, String password, String name, String bloodgroup, String emailid, char gender) {
        this.id = id;
        this.dob = dob;
        this.lastdonation = lastdonation;
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.bloodgroup = bloodgroup;
        this.emailid = emailid;
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public Long getLastdonation() {
        return lastdonation;
    }

    public void setLastdonation(Long lastdonation) {
        this.lastdonation = lastdonation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }



}
