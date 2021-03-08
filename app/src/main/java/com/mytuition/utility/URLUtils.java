package com.mytuition.utility;

import com.mytuition.interfaces.Api;

public class URLUtils {
    public static final String BASE_URL_NEW_API = "https://us-central1-mytuition-41294.cloudfunctions.net/";

    public static Api getAPIServiceForParent() {
        return RetrofitClient.getClient(BASE_URL_NEW_API).create(Api.class);
    }
}
