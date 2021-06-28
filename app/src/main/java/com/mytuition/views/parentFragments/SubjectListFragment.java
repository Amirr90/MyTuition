package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mytuition.adapters.SubjectAdapter;
import com.mytuition.databinding.FragmentSubjectListBinding;
import com.mytuition.utility.AppUtils;
import com.mytuition.viewHolder.ParentViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SubjectListFragment extends Fragment {

    FragmentSubjectListBinding subjectListBinding;
    SubjectAdapter subjectAdapter;

    ParentViewHolder viewModel;
    private static final String TAG = "SubjectListFragment";

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        subjectListBinding = FragmentSubjectListBinding.inflate(getLayoutInflater());
        return subjectListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        subjectAdapter = new SubjectAdapter(requireActivity());
        subjectListBinding.specRec.setAdapter(subjectAdapter);


        AppUtils.showRequestDialog(requireActivity());

        viewModel = new ViewModelProvider(requireActivity()).get(ParentViewHolder.class);
        viewModel.getSpecialityList().observe(getViewLifecycleOwner(), specialityModels -> {
            AppUtils.hideDialog();
            subjectAdapter.submitList(specialityModels);
            Log.d(TAG, "onChanged: " + specialityModels.size());
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }
}