package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mytuition.R;
import com.mytuition.databinding.FragmentTeacherDashboardBinding;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.TeacherProfile;

import org.jetbrains.annotations.NotNull;


public class TeacherDashboardFragment extends Fragment {


    FragmentTeacherDashboardBinding binding;
    NavController navController;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeacherDashboardBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        checkForProfileComplete();
    }

    private void checkForProfileComplete() {
        AppUtils.showRequestDialog(requireActivity());
        TeacherProfile.getProfile(new ApiInterface() {
            @Override
            public void onSuccess(Object obj) {
                AppUtils.hideDialog();
                TeacherModel teacherModel = (TeacherModel) obj;
                if (null != teacherModel) {
                    if (!teacherModel.getName().isEmpty()) {
                        Toast.makeText(requireActivity(), "incomplete profile !!", Toast.LENGTH_SHORT).show();
                        //navController.navigate(R.id.action_teacherDashboardFragment_to_TProfileFragment);
                        navController.navigate(R.id.action_teacherDashboardFragment_to_demoFragment);
                    }
                }

            }

            @Override
            public void onFailed(String msg) {
                AppUtils.hideDialog();
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}