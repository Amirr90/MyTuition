package com.mytuition.utility;

import android.util.Log;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mytuition.interfaces.Api;
import com.mytuition.interfaces.DatabaseCallbackInterface;
import com.mytuition.models.RequestModel;
import com.mytuition.models.SpecialityModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.responseModel.ApiResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mytuition.adapters.DashboardPatientAdapter1.SPECIALITY;
import static com.mytuition.adapters.DashboardPatientAdapter1.TEACHERS;
import static com.mytuition.utility.Utils.getTeacherModel;

public class DatabaseUtils {
    private static final String TAG = "DatabaseUtils";
    private static final String CLASS = "class";

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


}
