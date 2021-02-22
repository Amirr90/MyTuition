package com.mytuition.teacherView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.mytuition.databinding.FragmentTProfileBinding;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import static com.mytuition.utility.AppUtils.showToolbar;


public class TProfileFragment extends Fragment {

    FragmentTProfileBinding binding;
    NavController navController;
    TeacherModel teacherModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTProfileBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        teacherModel = new TeacherModel();
        if (null != FirebaseAuth.getInstance().getCurrentUser())
            teacherModel.setMobile(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        binding.setTeacherProfile(teacherModel);

    }

    @Override
    public void onResume() {
        super.onResume();
        showToolbar(requireActivity());
    }
}