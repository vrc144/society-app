package com.example.societyapp;

import android.util.Log;

import java.io.Serializable;

public class Resident implements Serializable {

    String userId;
    String password;
    String building;
    int floor;
    int flat;
    String name;
    Long contactNo;
    String email;

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getBuilding() {
        return building;
    }

    public int getFloor() {
        return floor;
    }

    public int getFlat() {
        return flat;
    }

    public String getName() {
        return name;
    }

    public Long getContactNo() {
        return contactNo;
    }

    public String getEmail() {
        return email;
    }

    public Resident(String userId, String password, String building, int floor, int flat, String name, Long contactNo, String email) {
        this.userId = userId;
        this.password = password;
        this.building = building;
        this.floor = floor;
        this.flat = flat;
        this.name = name;
        this.contactNo = contactNo;
        this.email = email;
    }

    public void display(){
        Log.i("Resident",userId+" "+password+" "+building+" "+floor+" "+flat+" "+name+" "+contactNo+" "+email);
    }

}
