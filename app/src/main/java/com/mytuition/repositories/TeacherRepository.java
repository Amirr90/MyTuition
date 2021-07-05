package com.mytuition.repositories;

import androidx.lifecycle.MutableLiveData;

import com.mytuition.interfaces.Api;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;

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
        AppUtils.getFirestoreReference().collection(AppUtils.Teachers)
                .document(getUid())
                .addSnapshotListener((value, error) -> {
                    if (null == error && null != value) {
                        TeacherModel teacherModel = value.toObject(TeacherModel.class);
                        mutableLiveDataTimeSlots.setValue(teacherModel);
                    }
                });
    }
}
