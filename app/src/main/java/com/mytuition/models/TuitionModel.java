package com.mytuition.models;

public class TuitionModel {
    public TeacherModel teacherModel;
    public ParentModel parentModel;
    public String parentId;
    public String teacherId;
    public Long timestamp;
    public String requestStatus;
    public Long requestActionTimestamp;
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

    public Long getRequestActionTimestamp() {
        return requestActionTimestamp;
    }

    public void setRequestActionTimestamp(Long requestActionTimestamp) {
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
        if (null == timestamp)
            return System.currentTimeMillis();
        else return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{" +
                "teacherModel=" + teacherModel +
                ", parentModel=" + parentModel +
                ", parentId='" + parentId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", timestamp=" + timestamp +
                ", requestStatus='" + requestStatus + '\'' +
                ", requestActionTimestamp=" + requestActionTimestamp +
                ", requestTimeSLot='" + requestTimeSLot + '\'' +
                ", classIds='" + classIds + '\'' +
                '}';
    }
}
