package com.mytuition.interfaces;

import com.mytuition.models.RequestModel;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.responseModel.ApiResponse;
import com.mytuition.responseModel.SpecialityRes;
import com.mytuition.responseModel.TeacherRequestModel;
import com.mytuition.responseModel.TeacherRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("getTeacherListByClass")
    Call<ApiResponse> getTeacherListByClass(
            @Query("") RequestModel requestModel);

    @GET("requestTuition")
    Call<ApiResponse> requestTuition(
            @Query("uid") String uid,
            @Query("teacherId") String teacherId,
            @Query("name") String name,
            @Query("reqDate") String reqDate,
            @Query("reqTime") String reqTime,
            @Query("tuitionFor") String tuitionFor
    );

    @GET("getSpeciality")
    Call<SpecialityRes> getAllSpecialityData();

    @POST("teacher")
    Call<TeacherRes> getTeacher(@Body TeacherRequestModel model);

    @POST("requestTuition")
    Call<TeacherRes> reqTuition(@Body RequestTuitionModel model);
}
