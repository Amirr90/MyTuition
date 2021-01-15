package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.util.Log;
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
import com.mytuition.adapters.SubSpecialityAdapter;
import com.mytuition.databinding.FragmentTeachersListBySubjectBinding;
import com.mytuition.interfaces.DatabaseCallbackInterface;
import com.mytuition.models.SpecialityModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.DatabaseUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.mytuition.adapters.DashboardPatientAdapter1.SPECIALITY;


public class TeachersListBySubjectFragment extends Fragment implements SubSpecialityAdapter.SubSpecialityInterface {

    private static final String TAG = "TeachersListBySubjectFr";
    FragmentTeachersListBySubjectBinding bySubjectBinding;
    NavController navController;
    SubSpecialityAdapter adapter;
    String specialityName = null;


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


        AppUtils.showRequestDialog(requireActivity());
        DatabaseUtils.getTeacherBySpecialityName(specialityName, new DatabaseCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {
                AppUtils.hideDialog();
                adapter.submitList((List<TeacherModel>) obj);
            }

            @Override
            public void onFailed(String msg) {
                AppUtils.hideDialog();
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onItemClick(String item) {

        Log.d(TAG, "onItemClick: " + item);
        Bundle bundle = new Bundle();
        bundle.putString("docModel", item);
        navController.navigate(R.id.action_teachersListBySubjectFragment_to_selectTimeSlotsFragment, bundle);
    }
}