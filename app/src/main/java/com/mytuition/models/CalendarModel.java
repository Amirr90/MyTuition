package com.mytuition.models;

public class CalendarModel {
    String date;
    String day;
    String dateSend;

    public CalendarModel(String date, String day, String dateSend) {
        this.date = date;
        this.day = day;
        this.dateSend = dateSend;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getDateSend() {
        return dateSend;
    }
}
