package com.mytuition.viewHolder;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mytuition.models.DashboardModel;
import com.mytuition.models.RequestModel2;
import com.mytuition.models.SpecialityModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.repositories.ParentRepo;
import com.mytuition.responseModel.TeacherRequestModel;
import com.mytuition.responseModel.TuitionDetailResponse;

import java.util.List;

public class ParentViewHolder extends ViewModel {

    ParentRepo repo = new ParentRepo();

    public LiveData<List<SpecialityModel>> getSpecialityList() {
        return repo.getSpecialityList();
    }

    public LiveData<List<TeacherModel>> getTeacher(TeacherRequestModel model) {
        return repo.getTeacher(model);
    }

    public LiveData<List<TuitionDetailResponse.Tuition>> getTuitionList(FragmentActivity fragmentActivity) {
        return repo.getTuitionList(fragmentActivity);
    }

    public LiveData<List<DashboardModel>> getDashboardData(RequestModel2 requestModel2, FragmentActivity fragmentActivity) {
        return repo.getDashboardData(requestModel2, fragmentActivity);
    }
}
