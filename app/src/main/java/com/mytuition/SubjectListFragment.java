package com.mytuition;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mytuition.adapters.SubjectAdapter;
import com.mytuition.databinding.FragmentSubjectListBinding;
import com.mytuition.models.SubjectModel;

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
        subjectAdapter.submitList(getSubjectData());
    }

    private List<SubjectModel> getSubjectData() {
        List<SubjectModel> subjectModels = new ArrayList<>();
        for (int a = 0; a < 16; a++) {
            SubjectModel subjectModel = new SubjectModel();
            subjectModel.setId(String.valueOf(System.currentTimeMillis()));
            subjectModel.setImage("https://img.pngio.com/hd-teach-blogger-round-logo-png-transparent-png-image-download-teach-png-533_533.png");
            subjectModel.setSubjectName("English");
            subjectModel.setTeachers("12");
            subjectModels.add(subjectModel);
        }

        return subjectModels;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }
}