package com.mytuition.responseModel;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse {

    Integer responseCode;
    String responseMessage;
    List<Object> responseValue = new ArrayList<>();

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<?> getResponseValue() {
        return responseValue;
    }
}
