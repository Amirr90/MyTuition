package com.mytuition.utility;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mytuition.interfaces.DatabaseCallbackInterface;
import com.mytuition.models.SpecialityModel;
import com.mytuition.models.TeacherModel;

import java.util.ArrayList;
import java.util.List;

import static com.mytuition.adapters.DashboardPatientAdapter1.SPECIALITY;
import static com.mytuition.adapters.DashboardPatientAdapter1.TEACHERS;
import static com.mytuition.utility.Utils.getTeacherModel;

public class DatabaseUtils {
    private static final String TAG = "DatabaseUtils";

    public static void getSubjectData(final DatabaseCallbackInterface databaseCallbackInterface) {
        final List<SpecialityModel> specialityModels = new ArrayList<>();
        Utils.getFirebaseReference(SPECIALITY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
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

    public static void getPopularTeacher(String speciality, final DatabaseCallbackInterface databaseCallbackInterface) {
        final List<TeacherModel> specialityModels = new ArrayList<>();
        Utils.getFirebaseReference(TEACHERS)
                .limitToFirst(40)
                .equalTo(SPECIALITY, speciality)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (null == dataSnapshot && dataSnapshot.getChildrenCount() == 0) {
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
                        databaseCallbackInterface.onFailed(databaseError.getMessage());
                    }
                });
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
