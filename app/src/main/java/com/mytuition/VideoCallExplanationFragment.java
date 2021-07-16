package com.mytuition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.mytuition.databinding.FragmentVideoCallExplainationBinding;

import org.jetbrains.annotations.NotNull;


public class VideoCallExplanationFragment extends Fragment {

    FragmentVideoCallExplainationBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoCallExplainationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.ivBack.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
    }
}