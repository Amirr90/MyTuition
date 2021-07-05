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
import com.mytuition.adapters.PopularTeachersAdapter;
import com.mytuition.adapters.RecommendedTeachersAdapter;
import com.mytuition.databinding.FragmentTeacherListBinding;
import com.mytuition.interfaces.AdapterInterface;
import com.mytuition.interfaces.DatabaseCallbackInterface;
import com.mytuition.models.RequestModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.DatabaseUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.mytuition.utility.AppConstant.TEACHER_ID;
import static com.mytuition.utility.DatabaseUtils.getResponse;
import static com.mytuition.utility.Utils.getTeacherModel;


public class TeacherListFragment extends Fragment implements AdapterInterface {
    private static final String TAG = "TeacherListFragment";
    FragmentTeacherListBinding teacherListBinding;
    NavController navController;
    RecommendedTeachersAdapter doctorsAdapter;
    PopularTeachersAdapter popularDoctorsAdapter;

    String specialityId = null;

    List<String> classIds;


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

        if (null == getArguments())
            return;

        classIds = new ArrayList<>();
        specialityId = getArguments().getString("id");
        classIds = new ArrayList<>();
        String[] ids = specialityId.split(",");
        for (String a : ids) {
            Log.d(TAG, "specialityId: " + a);
            classIds.add(a);
        }


        doctorsAdapter = new RecommendedTeachersAdapter(this);
        popularDoctorsAdapter = new PopularTeachersAdapter(this);

        teacherListBinding.recommendedRec.setAdapter(doctorsAdapter);
        teacherListBinding.popularRec.setAdapter(popularDoctorsAdapter);


        RequestModel requestModel = new RequestModel();
        requestModel.getTeacherModel().setId(specialityId);

        getResponse(DatabaseUtils.getTeacherListByClass(requestModel), new DatabaseCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {
                List<TeacherModel> teacherModelList = (List<TeacherModel>) obj;
                popularDoctorsAdapter.submitList(teacherModelList);
                doctorsAdapter.submitList(getPopularTeacherData());
                popularDoctorsAdapter.submitList(getPopularTeacherData());

            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private List<TeacherModel> getPopularTeacherData() {
        List<TeacherModel> teacherModels = new ArrayList<>();
        for (int a = 0; a < 10; a++) {
            teacherModels.add(getTeacherModel(""));
        }
        return teacherModels;
    }


    @Override
    public void onItemClicked(Object o) {
        TeacherModel teacherModel = (TeacherModel) o;
        Bundle bundle = new Bundle();
        bundle.putString(TEACHER_ID, teacherModel.getId());
        navController.navigate(R.id.action_teacherListFragment_to_teacherProfileFragment, bundle);
    }
}