package com.mytuition.utility;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.mytuition.utility.AppConstant.USERS;
import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.views.activity.ChooseLoginTypeScreen.getUserMap;

public class Utils {
    private static final String TAG = "Utils";

    public static final String LOGIN_TYPE = "login_type";
    public static final String LOGIN_TYPE_PARENT = "parent";
    public static final String LOGIN_TYPE_TEACHER = "teacher";


    public static void updateUI(final String loginType) {
        if (getUid() == null) {
            return;
        }

        getFirestoreReference().collection(USERS).document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    getFirestoreReference().collection(USERS).document(getUid()).update(getUserMap(loginType))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                }
                            });
                } else
                    getFirestoreReference().collection(USERS).document(getUid()).set(getUserMap(loginType))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                }
                            });
            }
        });

    }
}
