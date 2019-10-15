package com.example.societyapp;

public class OldVisitor {

    String name,contactNumber,vehicleNumber,reasonOfVisit,image,guardId,status,date,time;

    public OldVisitor(String name, String contactNumber, String vehicleNumber, String reasonOfVisit, String image, String guardId, String status, String date, String time) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.vehicleNumber = vehicleNumber;
        this.reasonOfVisit = reasonOfVisit;
        this.image = image;
        this.guardId = guardId;
        this.status = status;
        this.date = date;
        this.time = time;
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

    public String getGuardId() {
        return guardId;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
