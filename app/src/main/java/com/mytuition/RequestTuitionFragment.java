package com.mytuition;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mytuition.databinding.FragmentRequestTuitionBinding;
import com.mytuition.models.ParentModel;
import com.mytuition.models.TeacherModel;

import org.jetbrains.annotations.NotNull;

public class RequestTuitionFragment extends Fragment {

    FragmentRequestTuitionBinding requestTuitionBinding;
    NavController navController;

    ParentModel parentModel;
    TeacherModel teacherModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requestTuitionBinding = FragmentRequestTuitionBinding.inflate(getLayoutInflater());
        return requestTuitionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        parentModel = getParentModel();
        teacherModel = getTeacherModel();

        requestTuitionBinding.setParent(parentModel);
        requestTuitionBinding.setTeacher(teacherModel);

        requestTuitionBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_requestTuitonFragment_to_requestTuitionSuccessfullyFragment);
            }
        });

    }

    private TeacherModel getTeacherModel() {
        TeacherModel teacherModel = new TeacherModel();
        teacherModel.setName("Teacher Name");
        teacherModel.setExperience("5");
        teacherModel.setSpeciality("Computer");
        teacherModel.setFee("500");
        teacherModel.setRating("5");
        teacherModel.setReview("500");
        teacherModel.setImage("https://img.pngio.com/hd-teach-blogger-round-logo-png-transparent-png-image-download-teach-png-533_533.png");
        return teacherModel;
    }

    private ParentModel getParentModel() {
        ParentModel parentModel = new ParentModel();
        parentModel.setAddress("RajajiPuram");
        parentModel.setGender("male");
        parentModel.setImage("https://img.pngio.com/hd-teach-blogger-round-logo-png-transparent-png-image-download-teach-png-533_533.png");
        parentModel.setMobile("9044865611");
        parentModel.setName("Amir");
        parentModel.setDob("12/12/2000");
        parentModel.setEmail("aamirr.1232@gmail.com");
        return parentModel;
    }


}