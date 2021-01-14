package com.mytuition;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mytuition.adapters.SubjectAdapter;
import com.mytuition.databinding.FragmentSubjectListBinding;
import com.mytuition.interfaces.DatabaseCallbackInterface;
import com.mytuition.models.SpecialityModel;
import com.mytuition.models.SubjectModel;
import com.mytuition.utility.DatabaseUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SubjectListFragment extends Fragment {

    FragmentSubjectListBinding subjectListBinding;
    SubjectAdapter subjectAdapter;

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


        DatabaseUtils.getSubjectData(new DatabaseCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {
                subjectAdapter.submitList((List<SpecialityModel>) obj);
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }
}