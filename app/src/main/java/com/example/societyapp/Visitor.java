package com.example.societyapp;

public class Visitor {

    String key,name,contactNumber,vehicleNumber,reasonOfVisit,image,building,floor,flat,guardId,status;
    String date,time;

    public Visitor(String key, String name, String contactNumber, String vehicleNumber, String reasonOfVisit, String image, String building, String floor, String flat, String guardId, String date, String time,String status) {
        this.key = key;
        this.name = name;
        this.contactNumber = contactNumber;
        this.vehicleNumber = vehicleNumber;
        this.reasonOfVisit = reasonOfVisit;
        this.image = image;
        this.building = building;
        this.floor = floor;
        this.flat = flat;
        this.guardId = guardId;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getReasonOfVisit() {
        return reasonOfVisit;
    }

    public String getImage() {
        return image;
    }

    public String getBuilding() {
        return building;
    }

    public String getFloor() {
        return floor;
    }

    public String getFlat() {
        return flat;
    }

    public String getGuardId() {
        return guardId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
