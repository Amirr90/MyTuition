package com.mytuition.utility;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.TeacherModel;

import static com.mytuition.utility.AppUtils.getUid;

public class TeacherProfile {
    public static void getProfile(final ApiInterface apiInterface) {
        if (null != getUid())
            AppUtils.getFirestoreReference().collection("Users").document(getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (null == documentSnapshot)
                        apiInterface.onFailed("User not found !!");
                    else {
                        apiInterface.onSuccess(documentSnapshot.toObject(TeacherModel.class));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    apiInterface.onFailed(e.getLocalizedMessage());
                }
            });
        else apiInterface.onFailed("User logged out !!");

    }
}
