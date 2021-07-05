package com.mytuition;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mytuition.databinding.FragmentWriteTestimonialsDialogBinding;
import com.mytuition.models.TestimonialsModel;
import com.mytuition.utility.App;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.Utils.getParentModel;

public class WriteTestimonialsDialog extends BottomSheetDialogFragment {
    private static final String TAG = "WriteTestimonialsDialog";


    FragmentWriteTestimonialsDialogBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWriteTestimonialsDialogBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSubmitTestimonialsReview.setOnClickListener(v -> {
            if (!Objects.requireNonNull(binding.etReason.getText()).toString().isEmpty()) {
                writeReview();
            } else
                Toasty.warning(App.context, "write review", Toast.LENGTH_SHORT, true).show();
        });
    }

    private void writeReview() {
        AppUtils.showRequestDialog(requireActivity());
        TestimonialsModel testimonialsModel = new TestimonialsModel();
        testimonialsModel.setComment(Objects.requireNonNull(binding.etReason.getText()).toString());
        testimonialsModel.setImage(getParentModel(requireActivity()).getImage());
        testimonialsModel.setTimestamp(System.currentTimeMillis());
        testimonialsModel.setUid(getUid());
        testimonialsModel.setName(getParentModel(requireActivity()).getName());


        if (getUid() != null) {
            AppUtils.getFirestoreReference().collection(AppConstant.TESTIMONIALS).document(getUid()).set(testimonialsModel).addOnSuccessListener(aVoid -> {
                AppUtils.hideDialog();
                dismiss();
                Toasty.success(App.context, "Added successfully!!", Toast.LENGTH_SHORT, true).show();
            }).addOnFailureListener(e -> {
                AppUtils.hideDialog();
                Toasty.error(App.context, "something went wrong, try again", Toast.LENGTH_SHORT, true).show();
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            });
        }
    }
}