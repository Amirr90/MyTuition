package com.mytuition.views.commonFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mytuition.R;
import com.mytuition.databinding.FragmentTeacherProfileBinding;
import com.mytuition.databinding.FragmentTuitorByClassBinding;
import com.mytuition.models.TeacherModel;

import org.jetbrains.annotations.NotNull;

import static com.mytuition.utility.AppUtils.getJSONFromModel;

public class TeacherProfileFragment extends Fragment {

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


        teacherProfileBinding.btnRequestTuition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("docModel", getJSONFromModel(teacherModel));
                navController.navigate(R.id.action_teacherProfileFragment_to_selectTimeSlotsFragment, bundle);
            }
        });
    }
}