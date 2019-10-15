package com.example.societyapp.ui.contacts;

public class Contact {

    String name,job,contactNumber;

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Contact(String name, String job, String contactNumber) {
        this.name = name;
        this.job = job;
        this.contactNumber = contactNumber;
    }
}
