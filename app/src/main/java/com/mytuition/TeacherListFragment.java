package com.mytuition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mytuition.adapters.PopularTeachersAdapter;
import com.mytuition.adapters.RecommendedTeachersAdapter;
import com.mytuition.databinding.FragmentTeacherListBinding;
import com.mytuition.interfaces.AdapterInterface;
import com.mytuition.models.TeacherModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class TeacherListFragment extends Fragment implements AdapterInterface {
    FragmentTeacherListBinding teacherListBinding;
    NavController navController;


    RecommendedTeachersAdapter doctorsAdapter;
    PopularTeachersAdapter popularDoctorsAdapter;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        teacherListBinding = FragmentTeacherListBinding.inflate(getLayoutInflater());
        return teacherListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        doctorsAdapter = new RecommendedTeachersAdapter(this);
        popularDoctorsAdapter = new PopularTeachersAdapter(this);

        teacherListBinding.recommendedRec.setAdapter(doctorsAdapter);
        teacherListBinding.popularRec.setAdapter(popularDoctorsAdapter);

        doctorsAdapter.submitList(getPopularTeacherData());
        popularDoctorsAdapter.submitList(getPopularTeacherData());
    }

    private List<TeacherModel> getPopularTeacherData() {
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
    public void onItemClicked(Object o) {

    }
}