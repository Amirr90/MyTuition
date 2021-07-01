
package com.mytuition;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mytuition.adapters.TeacherTimingPrimaryAdapter;
import com.mytuition.databinding.FragmentTeacherTimeSlotsBinding;
import com.mytuition.interfaces.TeacherTimingInterface;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.viewHolder.TeacherViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    NavController navController;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeacherTimeSlotsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        initList();

        getDataFromDatabase();

        primaryAdapter = new TeacherTimingPrimaryAdapter(this);

        viewModel.slotList().observe(getViewLifecycleOwner(), teacherModel -> {
            primaryAdapter.submitList(teacherModel.getTimeSlots());
            binding.setTeacherModel(teacherModel);
        });

        binding.recMain.setAdapter(primaryAdapter);


        binding.btnSubmitTiming.setOnClickListener(v -> {
            Log.d(TAG, "onClick: " + timeSlotModelList.toString());
            boolean isAvailableForSunday = binding.switchSunday.isChecked();
            boolean isAvailableForDemoClass = binding.switchDemoClass.isChecked();
            updateTiming(isAvailableForSunday, isAvailableForDemoClass, timeSlotModelList);
        });

        binding.btnAddNewSlots.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("timeSlots", binding.getTeacherModel().toString());
            navController.navigate(R.id.action_teacherTimeSlotsFragment_to_addNewSlotsFragment, bundle);
        });

    }

    private void getDataFromDatabase() {
       /* AppUtils.getFirestoreReference().collection(AppUtils.Teachers).document(getUid()).get().addOnSuccessListener(documentSnapshot -> {
            TeacherModel teacherModel = documentSnapshot.toObject(TeacherModel.class);
            binding.setTeacherModel(teacherModel);
            Log.d(TAG, "onSuccess: TeacherModel" + teacherModel.getTimeSlots().toString());
            for (int a = 0; a < teacherModel.getTimeSlots().size(); a++) {
                for (int b = 0; b < teacherModel.getTimeSlots().get(a).getSlots().size(); b++) {
                    String type = teacherModel.getTimeSlots().get(a).getType();
                    String slot = teacherModel.getTimeSlots().get(a).getSlots().get(b);
                    addSlots(type, slot);
                    Log.d(TAG, "getDataFromDatabase: " + type + " " + slot);
                }

            }

        });*/


    }

    private void updateTiming(boolean isAvailableForSunday, boolean availableForDemoClass, List<TeacherModel.TimeSlotModel> timeSlotModelList) {
        AppUtils.showRequestDialog(requireActivity());
        Map<String, Object> map = new HashMap<>();
        map.put("timeSlots", timeSlotModelList);
        map.put("availableForDemoClass", availableForDemoClass);
        map.put("availableForSunday", isAvailableForSunday);
        AppUtils.getFirestoreReference().collection(AppUtils.Teachers).document(getUid()).update(map)
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

        //addSlots(type, slot);
    }

    private void addSlots(String type, String slot) {
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