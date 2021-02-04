package com.mytuition.interfaces;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("getTeacherListByClass")
    Call<Api> getTeacherListByClass(
            @Query("") Dashboard dashboard);
}
