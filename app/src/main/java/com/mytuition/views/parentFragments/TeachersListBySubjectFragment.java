package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mytuition.R;
import com.mytuition.adapters.SubSpecialityAdapter;
import com.mytuition.databinding.FragmentTeachersListBySubjectBinding;
import com.mytuition.models.TeacherModel;
import com.mytuition.responseModel.TeacherRequestModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.viewHolder.ParentViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.mytuition.adapters.DashboardPatientAdapter1.SPECIALITY;


public class TeachersListBySubjectFragment extends Fragment implements SubSpecialityAdapter.SubSpecialityInterface {

    FragmentTeachersListBySubjectBinding bySubjectBinding;
    NavController navController;
    SubSpecialityAdapter adapter;
    String specialityName = null;

    ParentViewHolder viewModel;
    TeacherRequestModel requestModel;


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


        if (null == getArguments())
            return;

        specialityName = getArguments().getString(SPECIALITY);
        adapter = new SubSpecialityAdapter(this);
        bySubjectBinding.subSpecialityRec.setAdapter(adapter);

        requestModel = new TeacherRequestModel();

        AppUtils.showRequestDialog(requireActivity());

        viewModel = new ViewModelProvider(requireActivity()).get(ParentViewHolder.class);

        requestModel.setSpecialityId(specialityName);
        viewModel.getTeacher(requestModel).observe(getViewLifecycleOwner(), new Observer<List<TeacherModel>>() {
            @Override
            public void onChanged(List<TeacherModel> teacherModels) {
                AppUtils.hideDialog();
                adapter.submitList(teacherModels);


            }
        });


    }


    @Override
    public void onItemClick(String item) {
        Bundle bundle = new Bundle();
        bundle.putString("docModel", item);
        navController.navigate(R.id.action_teachersListBySubjectFragment_to_selectTimeSlotsFragment, bundle);
    }
}