package com.mytuition.utility;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.TeacherModel;

import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getUid;

public class TeacherProfile {
    private static final String TAG = "TeacherProfile";

    public static void getProfile(final ApiInterface apiInterface) {
        getFirestoreReference().collection(AppUtils.Teachers)
                .document(getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (null == documentSnapshot) {
                        apiInterface.onFailed("User not found !!");
                    } else {
                        TeacherModel teacherModel = documentSnapshot.toObject(TeacherModel.class);
                        if (null == teacherModel) {
                            apiInterface.onFailed("User not found !!");
                            setData();
                        } else
                            apiInterface.onSuccess(teacherModel);
                    }
                }).addOnFailureListener(e -> {
            Log.d(TAG, "getProfile: " + e.getLocalizedMessage());
            apiInterface.onFailed(e.getLocalizedMessage());
        });


    }

    private static void setData() {
        DocumentReference documentReference = getFirestoreReference()
                .collection(AppUtils.Teachers)
                .document(getUid());
        TeacherModel teacherModel = new TeacherModel();
        teacherModel.setProfile(new TeacherModel.Profile());
        teacherModel.setAcademicInformation(new TeacherModel.AcademicInformation());
        teacherModel.setTeachingProfile(new TeacherModel.TeachingProfile());
        documentReference.set(teacherModel);
    }
}
