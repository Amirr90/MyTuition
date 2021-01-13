package com.mytuition;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mytuition.adapters.SubSpecialityAdapter;
import com.mytuition.databinding.FragmentTeachersListBySubjectBinding;
import com.mytuition.models.TeacherModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class TeachersListBySubjectFragment extends Fragment implements SubSpecialityAdapter.SubSpecialityInterface {

    private static final String TAG = "TeachersListBySubjectFr";
    FragmentTeachersListBySubjectBinding bySubjectBinding;
    NavController navController;
    SubSpecialityAdapter adapter;
    String SpecialityId = null;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bySubjectBinding = FragmentTeachersListBySubjectBinding.inflate(getLayoutInflater());
        return bySubjectBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        adapter = new SubSpecialityAdapter(this);
        bySubjectBinding.subSpecialityRec.setAdapter(adapter);
        adapter.submitList(getTeacherDataBySpecialityId(SpecialityId));


    }


    private List<TeacherModel> getTeacherDataBySpecialityId(String specialityId) {
        List<TeacherModel> teacherModels = new ArrayList<>();
        for (int a = 0; a < 10; a++) {
            TeacherModel teacherModel = new TeacherModel();
            teacherModel.setName("Teacher Name");
            teacherModel.setExperience("5");
            teacherModel.setSpeciality("Computer");
            teacherModel.setFee("500");
            teacherModel.setRating("5");
            teacherModel.setReview("500");
            teacherModel.setImage("https://img.pngio.com/hd-teach-blogger-round-logo-png-transparent-png-image-download-teach-png-533_533.png");
            teacherModels.add(teacherModel);
        }
        return teacherModels;
    }

    @Override
    public void onItemClick(String item) {
        Log.d(TAG, "onItemClick: " + item);
        Bundle bundle = new Bundle();
        bundle.putString("docModel", item);
        navController.navigate(R.id.action_teachersListBySubjectFragment_to_teacherProfileFragment, bundle);
    }
}