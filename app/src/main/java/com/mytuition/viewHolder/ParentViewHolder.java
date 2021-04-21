package com.mytuition.viewHolder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mytuition.models.SpecialityModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.repositories.ParentRepo;
import com.mytuition.responseModel.TeacherRequestModel;

import java.util.List;

public class ParentViewHolder extends ViewModel {

    ParentRepo repo = new ParentRepo();

    public LiveData<List<SpecialityModel>> getSpecialityList() {
        return repo.getSpecialityList();
    }

    public LiveData<List<TeacherModel>> getTeacher(TeacherRequestModel model) {
        return repo.getTeacher(model);
    }
}
