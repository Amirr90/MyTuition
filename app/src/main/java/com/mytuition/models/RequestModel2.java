package com.mytuition.models;

public class RequestModel2 {
    String tuitionId;
    String userId;
    String location;
    String area;
    String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTuitionId() {
        return tuitionId;
    }

    public void setTuitionId(String tuitionId) {
        this.tuitionId = tuitionId;
    }
}
