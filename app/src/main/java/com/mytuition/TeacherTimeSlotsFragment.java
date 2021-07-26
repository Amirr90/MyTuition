package com.mytuition;

import android.app.AlertDialog;
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

import com.google.firebase.firestore.FieldValue;
import com.mytuition.adapters.TeacherTimingPrimaryAdapter;
import com.mytuition.databinding.FragmentTeacherTimeSlotsBinding;
import com.mytuition.interfaces.TeacherTimingInterface;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.App;
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


        primaryAdapter = new TeacherTimingPrimaryAdapter(this);

        viewModel.slotList().observe(getViewLifecycleOwner(), teacherModel -> {
            setSlots(teacherModel);
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

    private void setSlots(TeacherModel teacherModel) {
        List<TeacherModel.TimeSlotModel> slotModels = new ArrayList<>();
        Map<String, Object> slotsMap = teacherModel.getSlots();
        if (null == slotsMap)
            return;
        Log.d("TAG", "addData: " + slotsMap.toString());
        if (slotsMap.containsKey("Morning")) {
            ArrayList<String> slots = (ArrayList<String>) slotsMap.get("Morning");
            if (null != slots)
                if (!slots.isEmpty())
                    slotModels.add(new TeacherModel.TimeSlotModel("Morning", slots));
        }
        if (slotsMap.containsKey("Noon")) {
            ArrayList<String> slots = (ArrayList<String>) slotsMap.get("Noon");
            if (null != slots)
                if (!slots.isEmpty())
                    slotModels.add(new TeacherModel.TimeSlotModel("Noon", slots));
        }
        if (slotsMap.containsKey("Evening")) {
            ArrayList<String> slots = (ArrayList<String>) slotsMap.get("Evening");
            if (null != slots)
                if (!slots.isEmpty())
                    slotModels.add(new TeacherModel.TimeSlotModel("Evening", slots));
        }
        if (slotsMap.containsKey("Night")) {
            ArrayList<String> slots = (ArrayList<String>) slotsMap.get("Night");
            if (null != slots)
                if (!slots.isEmpty())
                    slotModels.add(new TeacherModel.TimeSlotModel("Night", slots));
        }


        teacherModel.setTimeSlots(slotModels);
        primaryAdapter.submitList(teacherModel.getTimeSlots());

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
                    navController.navigateUp();
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
        new AlertDialog.Builder(requireActivity()).setMessage("Deleting  slot")
                .setPositiveButton("Yes", (dialog, which) -> deleteSlot(type, slot)).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
    }

    private void deleteSlot(String type, String slot) {
        AppUtils.getFirestoreReference().collection(AppUtils.Teachers).document(getUid()).update(
                AppUtils.SLOTS + "." + type, FieldValue.arrayRemove(slot)
        ).addOnSuccessListener(aVoid -> Toasty.success(App.context, "slot removed successfully !!", Toast.LENGTH_SHORT).show());
    }

}