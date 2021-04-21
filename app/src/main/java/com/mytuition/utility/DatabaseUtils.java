package com.mytuition.utility;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mytuition.interfaces.Api;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.interfaces.DatabaseCallbackInterface;
import com.mytuition.models.RequestModel;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.models.SpecialityModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.models.TuitionModel;
import com.mytuition.responseModel.ApiResponse;
import com.mytuition.responseModel.SpecialityRes;
import com.mytuition.responseModel.TeacherRequestModel;
import com.mytuition.responseModel.TeacherRes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mytuition.adapters.DashboardPatientAdapter1.SPECIALITY;
import static com.mytuition.adapters.DashboardPatientAdapter1.TEACHERS;

public class DatabaseUtils {
    public static final String TAG = "DatabaseUtils";
    public static final String CLASS = "class";
    public static final String PRIORITY = "priority";

    public static void getSubjectData(final DatabaseCallbackInterface databaseCallbackInterface) {
        final List<SpecialityModel> specialityModels = new ArrayList<>();
        Utils.getFirebaseReference(SPECIALITY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SpecialityModel specialityModel = postSnapshot.getValue(SpecialityModel.class);
                    specialityModels.add(specialityModel);
                }
                databaseCallbackInterface.onSuccess(specialityModels);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseCallbackInterface.onFailed(databaseError.getMessage());
            }
        });
    }


    public static void getResponse(Call<ApiResponse> call, final DatabaseCallbackInterface demoAoiInterface) {
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {

                if (response.code() == 200) {
                    ApiResponse responseModel = response.body();
                    if (null == responseModel)
                        return;
                    if (responseModel.getResponseCode() == 1) {
                        demoAoiInterface.onSuccess(responseModel);
                    } else demoAoiInterface.onFailed(responseModel.getResponseMessage());

                } else
                    demoAoiInterface.onFailed("Failed to read Data " + response.code());

            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                AppUtils.hideDialog();
                demoAoiInterface.onFailed(t.getLocalizedMessage());
            }
        });


    }

    public static Call<ApiResponse> getTeacherListByClass(RequestModel model) {
        return URLUtils.getAPIServiceForParent().getTeacherListByClass(model);
    }


    public static void getTeacherBySpecialityName(String speciality, final DatabaseCallbackInterface databaseCallbackInterface) {
        final List<TeacherModel> specialityModels = new ArrayList<>();
        Utils.getFirebaseReference(TEACHERS)
                .limitToFirst(40)
                .orderByChild("speciality")
                .equalTo(speciality)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 0) {
                            databaseCallbackInterface.onFailed("No Teacher Found");
                            return;
                        }

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            TeacherModel specialityModel = postSnapshot.getValue(TeacherModel.class);
                            specialityModels.add(specialityModel);
                        }
                        databaseCallbackInterface.onSuccess(specialityModels);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                        databaseCallbackInterface.onFailed(databaseError.getMessage());
                    }
                });
    }

    public static void getTopTeacherData(final DatabaseCallbackInterface databaseCallbackInterface) {
        final List<TeacherModel> models = new ArrayList<>();
        Utils.getFirebaseReference(TEACHERS)
                .orderByChild(PRIORITY)
                .limitToFirst(15)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            TeacherModel value = postSnapshot.getValue(TeacherModel.class);
                            models.add(value);
                            Log.d(TAG, "onDataChange: " + value.getName());
                        }
                        databaseCallbackInterface.onSuccess(models);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseCallbackInterface.onFailed(databaseError.getMessage());
                    }
                });
    }

    public static void requestTuition(TuitionModel model, String timeSlots, final ApiInterface apiCallbackInterface) {
        try {
            final Api api = URLUtils.getAPIServiceForParent();
            Call<ApiResponse> dashBoardResCall = api.requestTuition(
                    model.getParentModel().getId(),
                    model.getTeacherModel().getId(),
                    model.getParentModel().getName(),
                    timeSlots,
                    model.getRequestTimeSLot(),
                    model.getClassIds());
            dashBoardResCall.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {

                    if (response.code() == 200) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse.getResponseCode() == 1) {
                            apiCallbackInterface.onSuccess(apiResponse.getResponseMessage());
                        } else {
                            apiCallbackInterface.onFailed(apiResponse.getResponseMessage());
                        }
                    } else apiCallbackInterface.onFailed(response.message());

                }

                @Override
                public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                    apiCallbackInterface.onFailed(t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getSpecialityData(final ApiInterface apiCallbackInterface) {
        try {
            final Api api = URLUtils.getAPIServiceForParent();
            Call<SpecialityRes> call = api.getAllSpecialityData();
            call.enqueue(new Callback<SpecialityRes>() {
                @Override
                public void onResponse(@NotNull Call<SpecialityRes> call, @NotNull Response<SpecialityRes> response) {

                    if (response.code() == 200) {
                        SpecialityRes apiResponse = response.body();
                        if (null != apiResponse) {
                            if (apiResponse.getResponseCode() == 1) {
                                apiCallbackInterface.onSuccess(apiResponse.getResponseValue());
                            } else {
                                apiCallbackInterface.onFailed(apiResponse.getResponseMessage());
                            }
                        } else apiCallbackInterface.onFailed(response.message());
                    } else apiCallbackInterface.onFailed(response.message());

                }

                @Override
                public void onFailure(@NotNull Call<SpecialityRes> call, @NotNull Throwable t) {
                    apiCallbackInterface.onFailed(t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            apiCallbackInterface.onFailed(e.getLocalizedMessage());
        }
    }

    public static void getTeacher(TeacherRequestModel model, final ApiInterface apiCallbackInterface) {
        try {
            final Api api = URLUtils.getAPIServiceForParent();
            Call<TeacherRes> call = api.getTeacher(model);
            call.enqueue(new Callback<TeacherRes>() {
                @Override
                public void onResponse(@NotNull Call<TeacherRes> call, @NotNull Response<TeacherRes> response) {

                    if (response.code() == 200) {
                        TeacherRes apiResponse = response.body();
                        if (null != apiResponse) {
                            if (apiResponse.getResponseCode() == 1) {
                                apiCallbackInterface.onSuccess(apiResponse.getResponseValue());
                            } else {
                                apiCallbackInterface.onFailed(apiResponse.getResponseMessage());
                            }
                        } else apiCallbackInterface.onFailed(response.message());
                    } else apiCallbackInterface.onFailed(response.message());

                }

                @Override
                public void onFailure(@NotNull Call<TeacherRes> call, @NotNull Throwable t) {
                    apiCallbackInterface.onFailed(t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            apiCallbackInterface.onFailed(e.getLocalizedMessage());
        }
    }

    public static void reqTuition(RequestTuitionModel model, final ApiInterface apiCallbackInterface) {
        try {
            final Api api = URLUtils.getAPIServiceForParent();
            Call<TeacherRes> call = api.reqTuition(model);
            call.enqueue(new Callback<TeacherRes>() {
                @Override
                public void onResponse(@NotNull Call<TeacherRes> call, @NotNull Response<TeacherRes> response) {

                    if (response.code() == 200) {
                        TeacherRes apiResponse = response.body();
                        if (null != apiResponse) {
                            if (apiResponse.getResponseCode() == 1) {
                                apiCallbackInterface.onSuccess(apiResponse.getResponseMessage());
                            } else {
                                apiCallbackInterface.onFailed(apiResponse.getResponseMessage());
                            }
                        } else apiCallbackInterface.onFailed(response.message());
                    } else apiCallbackInterface.onFailed(response.message());

                }

                @Override
                public void onFailure(@NotNull Call<TeacherRes> call, @NotNull Throwable t) {
                    apiCallbackInterface.onFailed(t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            apiCallbackInterface.onFailed(e.getLocalizedMessage());
        }
    }
}
