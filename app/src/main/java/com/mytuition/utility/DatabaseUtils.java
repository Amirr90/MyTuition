package com.mytuition.utility;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mytuition.interfaces.DatabaseCallbackInterface;
import com.mytuition.models.SpecialityModel;

import java.util.ArrayList;
import java.util.List;

import static com.mytuition.adapters.DashboardPatientAdapter1.SPECIALITY;

public class DatabaseUtils {
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

}
