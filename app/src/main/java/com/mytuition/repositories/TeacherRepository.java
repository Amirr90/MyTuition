package com.mytuition.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mytuition.interfaces.Api;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.mytuition.utility.AppUtils.getUid;

@Singleton
public class TeacherRepository {

    Api api;

    MutableLiveData<TeacherModel> mutableLiveDataTimeSlots;

   /*  @Inject
    public TeacherRepository(Api api) {
        this.api = api;
    }*/

    @Inject
    public TeacherRepository() {
    }

    public MutableLiveData<TeacherModel> getTeacherTimeSlots() {
        if (null == mutableLiveDataTimeSlots) {
            mutableLiveDataTimeSlots = new MutableLiveData<>();
        }
        addData();
        return mutableLiveDataTimeSlots;
    }

    private void addData() {
       /* List<TeacherModel.TimeSlotModel> timeSlotsModelList = new ArrayList<>();
        timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Morning", getSlots(false, 6, 12)));
        timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Noon", getSlots(false, 12, 17)));
        timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Evening", getSlots(false, 17, 21)));
        timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Night", getSlots(false, 21, 24)));*/

        AppUtils.getFirestoreReference().collection(AppUtils.Teachers).document(getUid()).get().addOnSuccessListener(documentSnapshot -> {
            TeacherModel teacherModel = documentSnapshot.toObject(TeacherModel.class);

            // Log.d(TAG, "addData: " + teacherModel.getTimeSlots());

            mutableLiveDataTimeSlots.setValue(teacherModel);
        /*    Log.d(TAG, "onSuccess: TeacherModel" + teacherModel.getTimeSlots().toString());
            for (int a = 0; a < teacherModel.getTimeSlots().size(); a++) {
                for (int b = 0; b < teacherModel.getTimeSlots().get(a).getSlots().size(); b++) {
                    String type = teacherModel.getTimeSlots().get(a).getType();
                    String slot = teacherModel.getTimeSlots().get(a).getSlots().get(b);
                    addSlots(type, slot);
                    Log.d(TAG, "getDataFromDatabase: " + type + " " + slot);
                }

            }*/



            Map<String, Object> slotsMap = teacherModel.getTimeSlotsDemo();
            Log.d("TAG", "addData: "+slotsMap.toString());
        });


    }
}
