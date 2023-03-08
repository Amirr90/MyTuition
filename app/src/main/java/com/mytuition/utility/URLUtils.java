package com.mytuition.utility;

import com.mytuition.interfaces.Api;

public class URLUtils {
    //Old Project
    //public static final String BASE_URL_NEW_API = "https://us-central1-mytuition-41294.cloudfunctions.net/";

    //New Project
    public static final String BASE_URL_NEW_API = "https://us-central1-my-tuition-f9b41.cloudfunctions.net/";

    public static Api getAPIServiceForParent() {
        return RetrofitClient.getClient(BASE_URL_NEW_API).create(Api.class);
    }
}
