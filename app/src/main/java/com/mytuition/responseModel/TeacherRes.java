package com.mytuition.responseModel;

import com.mytuition.models.TeacherModel;

import java.util.List;

public class TeacherRes {
    Integer responseCode;
    String responseMessage;
    List<TeacherModel> responseValue;

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<TeacherModel> getResponseValue() {
        return responseValue;
    }
}
