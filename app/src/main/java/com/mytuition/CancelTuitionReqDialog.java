package com.mytuition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mytuition.databinding.FragmentCancelTuitionReqDialogBinding;
import com.mytuition.utility.App;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class CancelTuitionReqDialog extends BottomSheetDialogFragment {


    FragmentCancelTuitionReqDialogBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCancelTuitionReqDialogBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCancelReqTuition.setOnClickListener(v -> {
            if (binding.etReason.getText().toString().isEmpty()) {
                Toasty.info(requireActivity(), "Please write reason for cancelling Tuition Request", Toast.LENGTH_SHORT, true).show();

            } else {
                updateRequestStatus();

            }
        });

    }

    private void updateRequestStatus() {
        if (getArguments() != null) {
            AppUtils.showRequestDialog(requireActivity());
            AppUtils.getFirestoreReference()
                    .collection(AppConstant.REQUEST_TUITION)
                    .document(getArguments().getString(AppConstant.TUITION_ID)).update(getUpdateMap()).addOnSuccessListener(aVoid -> {
                        AppUtils.hideDialog();
                        Toasty.success(App.context, "cancelled successfully !!", Toast.LENGTH_SHORT, true).show();
                        dismiss();

                    }).addOnFailureListener(e -> {
                        AppUtils.hideDialog();
                        Toasty.error(App.context, "try again !!", Toast.LENGTH_SHORT, true).show();

                    });
        }
    }

    private Map<String, Object> getUpdateMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("isActive", false);
        map.put("reqStatus", AppConstant.CANCELLED);
        map.put("requestActionTimestamp", System.currentTimeMillis());
        return map;
    }
}