package com.mytuition.responseModel;

import com.mytuition.models.RequestTuitionModel;
import com.mytuition.models.TeacherModel;

import java.util.List;

public class TuitionDetailResponse {
    Integer responseCode;
    String responseMessage;
    List<Tuition> responseValue;

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<Tuition> getResponseValue() {
        return responseValue;
    }

    public static class Tuition {
        List<RequestTuitionModel> tuitionModel;
        TeacherModel teacherModel;

        public List<RequestTuitionModel> getTuitionModel() {
            return tuitionModel;
        }

        public TeacherModel getTeacherModel() {
            return teacherModel;
        }
    }
}
