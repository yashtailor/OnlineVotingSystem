package com.example.onlinevotingsystem;

public class User {
    String fName,lName,gender,dob,aadharNum,hasSubmitted,isVerified,message,userId;

    public User(){
        //no argument user
    }

    public User(String fName, String lName, String gender, String dob, String aadharNum,String hasSubmitted,String isVerified,String message,String userId) {
        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
        this.dob = dob;
        this.aadharNum = aadharNum;
        this.hasSubmitted = hasSubmitted;
        this.isVerified = isVerified;
        this.message = message;
        this.userId = userId;
    }

    public String getAadharNum() {
        return aadharNum;
    }

    public String getDob() {
        return dob;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getGender() {
        return gender;
    }

    public String getHasSubmitted() {
        return hasSubmitted;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setAadharNum(String aadharNum) {
        this.aadharNum = aadharNum;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setHasSubmitted(String hasSubmitted) {
        this.hasSubmitted = hasSubmitted;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
