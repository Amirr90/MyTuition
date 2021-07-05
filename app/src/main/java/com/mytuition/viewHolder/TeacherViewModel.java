package com.mytuition.viewHolder;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mytuition.models.TeacherModel;
import com.mytuition.repositories.TeacherRepository;

import javax.inject.Inject;

public class TeacherViewModel extends ViewModel {
    private static final String TAG = "TeacherViewModel";

    @Inject
    TeacherRepository repository;

    @Inject
    public TeacherViewModel(TeacherRepository repository) {
        this.repository = repository;
        Log.d(TAG, "TeacherViewModel: injected successfully !!");
    }

    public LiveData<TeacherModel> slotList() {
        return repository.getTeacherTimeSlots();
    }
}
