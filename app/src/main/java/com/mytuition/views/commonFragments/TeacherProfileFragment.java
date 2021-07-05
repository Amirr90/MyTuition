package com.mytuition.views.commonFragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.mytuition.R;
import com.mytuition.databinding.FragmentTeacherProfileBinding;
import com.mytuition.models.TeacherModel;

import org.jetbrains.annotations.NotNull;

import static com.mytuition.utility.AppUtils.getJSONFromModel;

public class TeacherProfileFragment extends Fragment {
    private static final String TAG = "TeacherProfileFragment";

    FragmentTeacherProfileBinding teacherProfileBinding;
    NavController navController;
    TeacherModel teacherModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        teacherProfileBinding = FragmentTeacherProfileBinding.inflate(getLayoutInflater());
        return teacherProfileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        if (null == getArguments())
            return;

        String jsonString = getArguments().getString("docModel");
        Gson gson = new Gson();

        teacherModel = gson.fromJson(jsonString, TeacherModel.class);
        teacherProfileBinding.setTeacher(teacherModel);

        setAboutMe(teacherModel);


        teacherProfileBinding.btnRequestTuition.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("docModel", getJSONFromModel(teacherModel));
            navController.navigate(R.id.action_teacherProfileFragment_to_selectTimeSlotsFragment, bundle);
        });
    }

    private void setAboutMe(TeacherModel teacherModel) {
        String text;
        if (teacherModel.getAbout().isEmpty()) {
            text = "Hi there, I'm " + teacherModel.getName() + " and I teach all subjects and specialization in " + teacherModel.getSpeciality() + ". I have been teaching for over " + teacherModel.getTeachingProfile().getExperience() + " years and have education experience in " + teacherModel.getAcademicInformation().getHighestEducation() + ". When you walk in my classroom you will notice.";
        } else text = teacherModel.getAbout();
        teacherProfileBinding.textView105.setText(text);

        Drawable img = teacherProfileBinding.textView92.getContext().getResources().getDrawable(R.drawable.ic_baseline_verified_user_24);

        if (teacherModel.getProfile().getVerified())
            teacherProfileBinding.textView92.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        else
            teacherProfileBinding.textView92.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

    }
}