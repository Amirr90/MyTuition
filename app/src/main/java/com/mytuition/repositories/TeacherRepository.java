package com.mytuition.repositories;

import androidx.lifecycle.MutableLiveData;

import com.mytuition.interfaces.Api;
import com.mytuition.models.TeacherModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.mytuition.utility.AppUtils.getSlots;

@Singleton
public class TeacherRepository {

    Api api;

    MutableLiveData<List<TeacherModel.TimeSlotModel>> mutableLiveDataTimeSlots;

   /*  @Inject
    public TeacherRepository(Api api) {
        this.api = api;
    }*/

    @Inject
    public TeacherRepository() {
    }

    public MutableLiveData<List<TeacherModel.TimeSlotModel>> getTeacherTimeSlots() {
        if (null == mutableLiveDataTimeSlots) {
            mutableLiveDataTimeSlots = new MutableLiveData<>();
            addData();
        }
        return mutableLiveDataTimeSlots;
    }

    private void addData() {
        List<TeacherModel.TimeSlotModel> timeSlotsModelList = new ArrayList<>();
        timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Morning", getSlots(false, 6, 12)));
        timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Noon", getSlots(false, 12, 17)));
        timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Evening", getSlots(false, 17, 21)));
        timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Night", getSlots(false, 21, 24)));
        mutableLiveDataTimeSlots.setValue(timeSlotsModelList);
    }
}
