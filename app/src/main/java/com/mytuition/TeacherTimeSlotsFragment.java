package com.mytuition;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.mytuition.databinding.FragmentTeacherTimeSlotsBinding;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;


public class TeacherTimeSlotsFragment extends Fragment {


    FragmentTeacherTimeSlotsBinding binding;
    Chip chip;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTeacherTimeSlotsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* for (int a = 0; a < 7; a++) {
            addDaysToChipGroup("Sunday " + a);
        }*/
    }

    private void addDaysToChipGroup(String title) {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        chip = (Chip) inflater.inflate(R.layout.item_chip, binding.chpDepartment, false);
        chip.setChecked(true);
        chip.setText(title);
        binding.chpDepartment.addView(chip);
        chip.setOnCloseIconClickListener(v -> {
            try {
                binding.chpDepartment.removeView(v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtils.showToolbar(requireActivity());
    }
}