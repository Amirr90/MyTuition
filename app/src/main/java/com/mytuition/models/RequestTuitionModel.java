package com.mytuition.models;

public class RequestTuitionModel {
    String teacherId;
    String reqDate;
    String uid;
    String reqTime;
    String name;
    String tuitionFor;
    String reqType;
    String reqStatus;
    String id;
    Long acceptedAt;
    private long timestamp;
    private String time;
    private boolean active;
    private String date;

    public void setId(String id) {
        this.id = id;
    }

    public void setAcceptedAt(Long acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getAcceptedAt() {
        return acceptedAt;
    }

    public String getId() {
        return id;
    }




    public long getTimestamp() {
        return timestamp;
    }

    public String getTime() {
        return time;
    }

    public boolean isActive() {
        return active;
    }

    public String getDate() {
        return date;
    }

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

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

    @Override
    public String toString() {
        return "{" +
                "teacherId='" + teacherId + '\'' +
                ", reqDate='" + reqDate + '\'' +
                ", uid='" + uid + '\'' +
                ", reqTime='" + reqTime + '\'' +
                ", name='" + name + '\'' +
                ", tuitionFor='" + tuitionFor + '\'' +
                ", reqType='" + reqType + '\'' +
                ", reqStatus='" + reqStatus + '\'' +
                ", id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", time='" + time + '\'' +
                ", isActive=" + active +
                ", date='" + date + '\'' +
                '}';
    }
}
