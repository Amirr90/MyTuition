package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mytuition.R;
import com.mytuition.databinding.FragmentRequestTuitionSuccessfullyBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class RequestTuitionSuccessfullyFragment extends Fragment {

    FragmentRequestTuitionSuccessfullyBinding binding;
    NavController navController;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRequestTuitionSuccessfullyBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.btnBookingDetails.setOnClickListener(v -> navController.navigate(R.id.action_requestTuitionSuccessfullyFragment_to_tuitionListFragment));
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
    }
}