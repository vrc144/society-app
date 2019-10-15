package com.example.societyapp.ui.notice;

public class Notice {

    String title,body,date,time;

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Notice(String title, String body, String date, String time) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.time = time;
    }
}
