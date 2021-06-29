package com.mytuition.utility;

import android.util.Log;

import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.TeacherModel;

import static com.mytuition.utility.AppUtils.getUid;

public class TeacherProfile {
    public static void getProfile(final ApiInterface apiInterface) {
        AppUtils.getFirestoreReference().collection(AppUtils.Teachers).document(getUid())
                .get().addOnSuccessListener(documentSnapshot -> {
            if (null == documentSnapshot)
                apiInterface.onFailed("User not found !!");
            else {
                apiInterface.onSuccess(documentSnapshot.toObject(TeacherModel.class));

            }
        }).addOnFailureListener(e -> apiInterface.onFailed(e.getLocalizedMessage()));


    }
}
