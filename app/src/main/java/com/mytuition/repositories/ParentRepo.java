package com.mytuition.repositories;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.SpecialityModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.responseModel.TeacherRequestModel;
import com.mytuition.utility.App;
import com.mytuition.utility.DatabaseUtils;

import java.util.List;

public class ParentRepo {
    MutableLiveData<List<SpecialityModel>> mutableLiveSpecialityData;
    MutableLiveData<List<TeacherModel>> mutableLiveTeacherData;

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

    private void loadTeacherData(TeacherRequestModel model) {
        DatabaseUtils.getTeacher(model, new ApiInterface() {
            @Override
            public void onSuccess(Object obj) {
                mutableLiveTeacherData.setValue((List<TeacherModel>) obj);
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(App.context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
