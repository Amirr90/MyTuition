package com.mytuition.models;

public class TuitionModel {
    public TeacherModel teacherModel;
    public ParentModel parentModel;
    public String parentId;
    public String teacherId;
    public long timestamp;
    public String requestStatus;
    public String requestActionTimestamp;
    public String requestTimeSLot;
    public String classIds;

    public String getClassIds() {
        return classIds;
    }

    public void setClassIds(String classIds) {
        this.classIds = classIds;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getRequestTimeSLot() {
        return requestTimeSLot;
    }

    public void setRequestTimeSLot(String requestTimeSLot) {
        this.requestTimeSLot = requestTimeSLot;
    }

    public String getRequestActionTimestamp() {
        return requestActionTimestamp;
    }

    public void setRequestActionTimestamp(String requestActionTimestamp) {
        this.requestActionTimestamp = requestActionTimestamp;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public TeacherModel getTeacherModel() {
        return teacherModel;
    }

    public void setTeacherModel(TeacherModel teacherModel) {
        this.teacherModel = teacherModel;
    }

    public ParentModel getParentModel() {
        return parentModel;
    }

    public void setParentModel(ParentModel parentModel) {
        this.parentModel = parentModel;
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
