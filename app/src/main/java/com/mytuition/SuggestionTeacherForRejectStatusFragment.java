package com.mytuition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mytuition.databinding.FragmentSuggestionTeacherForRejectStatusBinding;
import com.mytuition.utility.App;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class SuggestionTeacherForRejectStatusFragment extends BottomSheetDialogFragment {


    FragmentSuggestionTeacherForRejectStatusBinding binding;
    String tuitionId, teacherName;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSuggestionTeacherForRejectStatusBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (null == getArguments())
            dismiss();
        tuitionId = getArguments().getString(AppConstant.TUITION_ID);
        teacherName = getArguments().getString(AppConstant.TEACHER_NAME);
        binding.btnAssignTeacher.setOnClickListener(v -> reAssignTuitionToTeachers());

        binding.textView67.setText(String.format("%s Rejected your tuition request", teacherName));
    }

    private void reAssignTuitionToTeachers() {
        Map<String, Object> map = new HashMap<>();
        map.put(AppConstant.REQUEST_TYPE, AppConstant.REQUEST_TYPE_BY_CLASS);
        map.put(AppConstant.TEACHER_ID, "");
        map.put(AppConstant.REQUEST_STATUS, AppConstant.REQUEST_STATUS_PENDING);
        if (AppUtils.isNetworkConnected(requireActivity()))
            AppUtils.getFirestoreReference().collection(AppConstant.REQUEST_TUITION).document(tuitionId)
                    .update(map)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(App.context, "assigned  successfully, teacher will shortly call you!!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }).addOnFailureListener(e -> Toast.makeText(App.context, "try again !!", Toast.LENGTH_SHORT).show());
        else Toast.makeText(App.context, "no internet", Toast.LENGTH_SHORT).show();
    }
}