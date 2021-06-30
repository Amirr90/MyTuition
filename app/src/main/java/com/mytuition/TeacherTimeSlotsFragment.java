package com.mytuition;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mytuition.adapters.TeacherTimingPrimaryAdapter;
import com.mytuition.databinding.FragmentTeacherTimeSlotsBinding;
import com.mytuition.interfaces.TeacherTimingInterface;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.viewHolder.TeacherViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import es.dmoral.toasty.Toasty;

import static com.mytuition.utility.AppUtils.getUid;


public class TeacherTimeSlotsFragment extends DaggerFragment implements TeacherTimingInterface {

    private static final String TAG = "TeacherTimeSlotsFragmen";

    FragmentTeacherTimeSlotsBinding binding;
    @Inject
    TeacherViewModel viewModel;


    TeacherTimingPrimaryAdapter primaryAdapter;
    List<TeacherModel.TimeSlotModel> timeSlotModelList;
    List<String> morning, noon, evening, night;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeacherTimeSlotsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initList();
        primaryAdapter = new TeacherTimingPrimaryAdapter(this);
        viewModel.slotList().observe(getViewLifecycleOwner(), timeSlotModels -> primaryAdapter.submitList(timeSlotModels));

        binding.recMain.setAdapter(primaryAdapter);

        binding.btnSubmitTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + timeSlotModelList.toString());
                updateTiming(timeSlotModelList);
            }
        });

    }

    private void updateTiming(List<TeacherModel.TimeSlotModel> timeSlotModelList) {
        AppUtils.showRequestDialog(requireActivity());
        AppUtils.getFirestoreReference().collection(AppUtils.Teachers).document(getUid()).update("timeSlots", timeSlotModelList)
                .addOnSuccessListener(aVoid -> {
                    AppUtils.hideDialog();
                    Toasty.success(requireActivity(), "Time Slots updated successfully !!", Toast.LENGTH_SHORT).show();
                    clearAllList();
                }).addOnFailureListener(e -> {
            AppUtils.hideDialog();
            Toasty.warning(requireActivity(), "something went wrong !!", Toast.LENGTH_SHORT).show();

        });
    }

    private void clearAllList() {
        morning.clear();
        noon.clear();
        evening.clear();
        night.clear();
        timeSlotModelList.clear();
    }

    private void initList() {
        morning = new ArrayList<>();
        evening = new ArrayList<>();
        noon = new ArrayList<>();
        night = new ArrayList<>();
        timeSlotModelList = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtils.showToolbar(requireActivity());
    }


    @Override
    public void onClick(String type, String slot) {
        Log.d(TAG, "onItemClicked: " + type + "  " + slot);

        TeacherModel.TimeSlotModel timeSlotModel = new TeacherModel.TimeSlotModel();

        if (type.equalsIgnoreCase("Morning")) {
            if (!morning.contains(slot)) {
                morning.add(slot);
                timeSlotModel.setType(type);
                timeSlotModel.setSlots(morning);
            }
        } else if (type.equalsIgnoreCase("Noon")) {
            if (!noon.contains(slot)) {
                noon.add(slot);
                timeSlotModel.setType(type);
                timeSlotModel.setSlots(noon);
            }
        } else if (type.equalsIgnoreCase("Evening")) {
            if (!evening.contains(slot)) {
                evening.add(slot);
                timeSlotModel.setType(type);
                timeSlotModel.setSlots(evening);
            }
        } else if (type.equalsIgnoreCase("Night")) {
            if (!night.contains(slot)) {
                night.add(slot);
                timeSlotModel.setType(type);
                timeSlotModel.setSlots(night);
            }
        }

        timeSlotModelList.add(timeSlotModel);
    }
}