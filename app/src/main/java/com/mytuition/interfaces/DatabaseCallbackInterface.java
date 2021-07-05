package com.mytuition.interfaces;

public interface DatabaseCallbackInterface {
    void onSuccess(Object obj);

    void onFailed(String msg);
}
