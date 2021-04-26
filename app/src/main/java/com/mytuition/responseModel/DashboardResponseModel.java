package com.mytuition.responseModel;

import com.mytuition.models.DashboardModel;

import java.util.List;

public class DashboardResponseModel {
    Integer responseCode;
    String responseMessage;
    List<DashboardModel> responseValue;

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<DashboardModel> getResponseValue() {
        return responseValue;
    }
}
