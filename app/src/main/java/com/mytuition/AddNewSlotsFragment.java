package com.mytuition;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FieldValue;
import com.google.gson.Gson;
import com.mytuition.databinding.FragmentAddNewSlotsBinding;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.App;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.mytuition.utility.AppUtils.getJSONFromModel;
import static com.mytuition.utility.AppUtils.getSlots;
import static com.mytuition.utility.AppUtils.getUid;

public class AddNewSlotsFragment extends BottomSheetDialogFragment {

    private static final String TAG = "AddNewSlotsFragment";
    FragmentAddNewSlotsBinding binding;
    TeacherModel timeSlotModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddNewSlotsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() == null)
            dismiss();

        //getting TimeSlots
        Gson gson = new Gson();
        timeSlotModel = gson.fromJson(getJSONFromModel(getArguments().getString("timeSlots")), TeacherModel.class);
        //Log.d(TAG, "timeSlotModel: " + timeSlotModel.getTimeSlots().toString());
        addAutocompleteData();

        binding.etType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != s) {
                    String type = binding.etType.getText().toString();
                    Toast.makeText(requireContext(), type, Toast.LENGTH_SHORT).show();
                    addSlots(type);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.btnAddNewTimeSlots.setOnClickListener(v -> {
            String type = binding.etType.getText().toString();
            String slot = binding.etSlot.getText().toString();
            updateSlots(type, slot);
        });

    }

    private void updateSlots(String type, String slot) {
        AppUtils.getFirestoreReference().collection(AppUtils.Teachers)
                .document(getUid())
                .update(AppUtils.SLOTS + "." + type, FieldValue.arrayUnion(slot)).addOnFailureListener(e -> {
            Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
        }).addOnSuccessListener(aVoid -> {
            Toasty.success(App.context, "Slot added successfully !!", Toast.LENGTH_SHORT).show();
            dismiss();
        });


    }

    private void addSlots(String type) {
        List<String> slots = new ArrayList<>();
        if (type.equalsIgnoreCase("Morning")) {
            slots.addAll(getSlots(false, 6, 12));
        } else if (type.equalsIgnoreCase("Noon")) {
            slots.addAll(getSlots(false, 12, 16));
        } else if (type.equalsIgnoreCase("Evening")) {
            slots.addAll(getSlots(false, 16, 20));
        } else if (type.equalsIgnoreCase("Night")) {
            slots.addAll(getSlots(false, 20, 24));
        }


        final String[] slotsArray = new String[slots.size()];
        for (int a = 0; a < slots.size(); a++) {
            slotsArray[a] = slots.get(a);
        }
        ArrayAdapter<String> noonAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, slotsArray);
        binding.etSlot.setAdapter(noonAdapter);
        binding.etSlot.setText(slotsArray[0], false);
    }

    private void addAutocompleteData() {

        final String[] type = AppUtils.getSlotsType();
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, type);
        binding.etType.setAdapter(typeAdapter);
        addSlots("Morning");
    }
}