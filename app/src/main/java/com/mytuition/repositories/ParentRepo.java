package com.mytuition.repositories;

import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.DashboardModel;
import com.mytuition.models.RequestModel2;
import com.mytuition.models.SpecialityModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.responseModel.TeacherRequestModel;
import com.mytuition.responseModel.TuitionDetailResponse;
import com.mytuition.utility.App;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class ParentRepo {
    MutableLiveData<List<SpecialityModel>> mutableLiveSpecialityData;
    MutableLiveData<List<TeacherModel>> mutableLiveTeacherData;
    MutableLiveData<List<TuitionDetailResponse.Tuition>> mutableTuitionListLiveData;
    MutableLiveData<List<DashboardModel>> mutableDashboardLiveData;

    public LiveData<List<SpecialityModel>> getSpecialityList() {
        if (mutableLiveSpecialityData == null) {
            mutableLiveSpecialityData = new MutableLiveData<>();
            loadSpecialityData();
        }

        return mutableLiveSpecialityData;
    }

    private void loadSpecialityData() {
        DatabaseUtils.getSpecialityData(new ApiInterface() {
            @Override
            public void onSuccess(Object obj) {

                List<SpecialityModel> specialityModels = (List<SpecialityModel>) obj;
                if (null != specialityModels) {
                    mutableLiveSpecialityData.setValue(specialityModels);
                }
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    public LiveData<List<TeacherModel>> getTeacher(TeacherRequestModel model) {
        if (mutableLiveTeacherData == null) {
            mutableLiveTeacherData = new MutableLiveData<>();
        }
        loadTeacherData(model);
        return mutableLiveTeacherData;
    }

    private void loadTeacherData(final TeacherRequestModel model) {
        DatabaseUtils.getTeacher(model, new ApiInterface() {
            @Override
            public void onSuccess(Object obj) {
                List<TeacherModel> models = (List<TeacherModel>) obj;
                mutableLiveTeacherData.setValue(models);
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(App.context, msg, Toast.LENGTH_SHORT).show();
                List<TeacherModel> models = new ArrayList<>();
                mutableLiveTeacherData.setValue(models);
            }
        });
    }

    public LiveData<List<TuitionDetailResponse.Tuition>> getTuitionList(FragmentActivity fragmentActivity) {

        if (mutableTuitionListLiveData == null) {
            mutableTuitionListLiveData = new MutableLiveData<>();
        }
        loadTuitionListData(fragmentActivity);
        return mutableTuitionListLiveData;
    }

    private void loadTuitionListData(FragmentActivity fragmentActivity) {
        AppUtils.showRequestDialog(fragmentActivity);
        DatabaseUtils.getTuitionList(new ApiInterface() {
            @Override
            public void onSuccess(Object obj) {
                AppUtils.hideDialog();
                mutableTuitionListLiveData.setValue((List<TuitionDetailResponse.Tuition>) obj);
            }

            @Override
            public void onFailed(String msg) {
                AppUtils.hideDialog();
                Toast.makeText(App.context, msg, Toast.LENGTH_SHORT).show();
                List<TuitionDetailResponse.Tuition> tuition = new ArrayList<>();
                mutableTuitionListLiveData.setValue(tuition);
            }
        });
    }

    public LiveData<List<DashboardModel>> getDashboardData(RequestModel2 requestModel2, FragmentActivity fragmentActivity) {
        if (mutableDashboardLiveData == null) {
            mutableDashboardLiveData = new MutableLiveData<>();
            loadDashboardData(requestModel2, fragmentActivity);
        }

        return mutableDashboardLiveData;
    }

    private void loadDashboardData(RequestModel2 requestModel2, FragmentActivity fragmentActivity) {
        AppUtils.showRequestDialog(fragmentActivity);
        DatabaseUtils.getDashboardData(requestModel2, new ApiInterface() {
            @Override
            public void onSuccess(Object obj) {
                AppUtils.hideDialog();
                mutableDashboardLiveData.setValue((List<DashboardModel>) obj);
            }

            @Override
            public void onFailed(String msg) {
                AppUtils.hideDialog();
                Toast.makeText(App.context, "No data Found !! " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
