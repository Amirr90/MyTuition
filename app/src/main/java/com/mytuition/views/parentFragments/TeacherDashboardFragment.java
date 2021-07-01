package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mytuition.R;
import com.mytuition.databinding.FragmentTeacherDashboardBinding;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.TeacherProfile;

import org.jetbrains.annotations.NotNull;

import dagger.android.support.DaggerFragment;


public class TeacherDashboardFragment extends DaggerFragment {
    private static final String TAG = "TeacherDashboardFragmen";


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

                    Log.d(TAG, "onSuccess: " + teacherModel.toString());
                    /*if (teacherModel.isProfileFilled()) {
                        Toast.makeText(requireActivity(), "incomplete profile !!", Toast.LENGTH_SHORT).show();
                        //navController.navigate(R.id.action_teacherDashboardFragment_to_TProfileFragment);
                        String model = getJSONFromModel(teacherModel);
                        Bundle bundle = new Bundle();
                        bundle.putString("teacherModel", model);
                        navController.navigate(R.id.action_teacherDashboardFragment_to_demoFragment, bundle);
                    }*/
                    navController.navigate(R.id.teacherTimeSlotsFragment);
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