package com.mytuition.responseModel;

import com.mytuition.models.SpecialityModel;

import java.util.List;

public class SpecialityRes {

    Integer responseCode;
    String responseMessage;
    List<SpecialityModel> responseValue;

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<SpecialityModel> getResponseValue() {
        return responseValue;
    }
}
