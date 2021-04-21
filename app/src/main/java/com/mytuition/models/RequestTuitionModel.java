package com.mytuition.models;

public class RequestTuitionModel {
    String teacherId;
    String reqDate;
    String uid;
    String reqTime;
    String name;
    String tuitionFor;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTuitionFor() {
        return tuitionFor;
    }

    public void setTuitionFor(String tuitionFor) {
        this.tuitionFor = tuitionFor;
    }
}
