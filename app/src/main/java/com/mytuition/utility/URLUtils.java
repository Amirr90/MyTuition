package com.mytuition.utility;

import com.mytuition.interfaces.Api;

public class URLUtils {
    public static final String BASE_URL_NEW_API = "http://52.172.134.222:205/api/v1.0/Patient/";
    public static final String BASE_URL_NEW_API_DOCTOR = "http://52.172.134.222:205/api/v1.0/Doctor/";
    public static final String BASE_URL_NEW_API_PHARMACY = "http://52.172.134.222:205/api/v1.0/Pharmacy/";

    public static Api getAPIServiceForParent() {
        return RetrofitClient.getClient(BASE_URL_NEW_API).create(Api.class);
    }
}
