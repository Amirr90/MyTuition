package com.mytuition;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.gson.Gson;
import com.mytuition.databinding.FragmentAcceptTuitionBootomBinding;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.utility.App;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.mytuition.utility.AppUtils.getUid;


public class AcceptTuitionBottomFragment extends BottomSheetDialogFragment {

    private static final String TAG = "AcceptTuitionBottomFrag";


    RequestTuitionModel requestTuitionModel;
    FragmentAcceptTuitionBootomBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAcceptTuitionBootomBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() == null)
            dismiss();

        Gson gson = new Gson();
        requestTuitionModel = gson.fromJson(getArguments().getString("RequestTuitionModel"), RequestTuitionModel.class);
        binding.setTuitionModel(requestTuitionModel);

        Log.d(TAG, "onViewCreated: " + requestTuitionModel.getId());

        binding.btnAcceptTuition.setOnClickListener(view2 -> {
            updateTuitionStatus();
        });


    }

    private void updateTuitionStatus() {

        DocumentReference reference = AppUtils.getFirestoreReference().collection(AppConstant.REQUEST_TUITION).document(requestTuitionModel.getId());
        //check for tuition Status
        reference.get().addOnSuccessListener(documentSnapshot -> {
            RequestTuitionModel requestTuitionModel = documentSnapshot.toObject(RequestTuitionModel.class);
            if (null != requestTuitionModel) {
                if (requestTuitionModel.getActive() && requestTuitionModel.getReqStatus().equalsIgnoreCase(AppConstant.REQUEST_STATUS_PENDING)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("reqStatus", AppConstant.REQUEST_STATUS_ACCEPTED);
                    map.put("teacherId", getUid());
                    map.put("active", false);
                    map.put("acceptedAt", System.currentTimeMillis());
                    map.put("name", AppUtils.getString("name", requireActivity()));
                    reference.update(map)
                            .addOnSuccessListener(aVoid -> {
                                dismiss();
                                Toasty.success(requireActivity(), "Tuition accepted successfully !!", Toasty.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> Toasty.success(requireActivity(), "some thing went wrong !!", Toasty.LENGTH_SHORT).show());

                } else showToast("Tuition is hired by someone else !!");
            } else showToast("something went wrong !!");

        });

    }

    private void showToast(String msg) {
        dismiss();
        Toasty.warning(App.context, msg, Toasty.LENGTH_SHORT).show();
    }
}