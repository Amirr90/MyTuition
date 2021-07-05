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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mytuition.databinding.FragmentSingleTuitionDetailBinding;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.RequestModel2;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.responseModel.TuitionDetailResponse;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.DatabaseUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.mytuition.RequestTuitionDetailFragment.REQUEST_STATUS_ACCEPTED_S;
import static com.mytuition.RequestTuitionDetailFragment.REQUEST_STATUS_PENDING;
import static com.mytuition.RequestTuitionDetailFragment.REQUEST_STATUS_PENDING_S;
import static com.mytuition.RequestTuitionDetailFragment.REQUEST_STATUS_REJECTED_S;
import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getTimeFormat;
import static com.mytuition.utility.AppUtils.hideDialog;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_ACCEPTED;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_REJECTED;


public class SingleTuitionDetailFragment extends Fragment {
    private static final String TAG = "SingleTuitionDetailFrag";


    FragmentSingleTuitionDetailBinding binding;
    NavController navController;

    TeacherModel teacherModel;

    RequestTuitionModel requestTuitionModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSingleTuitionDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        binding.btnActionTeacher.setOnClickListener(v -> allotTeacher());
        binding.tvSpec.setOnClickListener(v -> changeStatusDialog());
    }

    private void changeStatusDialog() {
        new AlertDialog.Builder(requireActivity())
                .setTitle("Three Buttons")
                .setMessage("Where do you want to go?")
                .setIcon(R.drawable.app_icon)
                .setPositiveButton(AppConstant.ACCEPT,
                        (dialog, id) -> {
                            dialog.cancel();
                            updateStatus(AppConstant.REQUEST_STATUS_ACCEPTED);
                        })
                .setNegativeButton(AppConstant.REJECT,
                        (dialog, id) -> {
                            updateStatus(AppConstant.REQUEST_STATUS_REJECTED);
                            dialog.cancel();
                        }).show();
    }

    private void updateStatus(String requestStatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("reqStatus", requestStatus);
        map.put(AppConstant.ACTION_TIMESTAMP, System.currentTimeMillis());
        AppUtils.showRequestDialog(requireActivity());
        AppUtils.getFirestoreReference().collection(AppConstant.REQUEST_TUITION)
                .document(requestTuitionModel.getId())
                .update(map).addOnSuccessListener(aVoid -> {
            AppUtils.hideDialog();
            Toast.makeText(requireActivity(), "Updated Successfully !!", Toast.LENGTH_SHORT).show();
            getTuitionDetails();
        }).addOnFailureListener(e -> {
            AppUtils.hideDialog();
            Toast.makeText(requireActivity(), "Something went wrong, try again !!", Toast.LENGTH_SHORT).show();
        });
    }


    private void allotTeacher() {
        if (null == requestTuitionModel)
            return;
        SingleTuitionDetailFragmentDirections.ActionSingleTuitionDetailFragmentToAllotTeacherFragment action = SingleTuitionDetailFragmentDirections.actionSingleTuitionDetailFragmentToAllotTeacherFragment();
        action.setId(requestTuitionModel.getId());
        navController.navigate(action);
    }

    private void getTeacherProfile() {
        getFirestoreReference().collection(AppConstant.TEACHER).document(requestTuitionModel.getTeacherId()).get().addOnSuccessListener(documentSnapshot -> {
            hideDialog();
            if (documentSnapshot.exists()) {
                teacherModel = documentSnapshot.toObject(TeacherModel.class);
                binding.setTeacher(teacherModel);
                if (null != teacherModel.getTeachingProfile().getExperience())
                    binding.imageView9.setImageResource(AppUtils.setExperience(teacherModel.getTeachingProfile().getExperience()));
            }
        });

    }


    private void getTuitionDetails() {
        if (getArguments() != null) {
            final String tuitionId = SingleTuitionDetailFragmentArgs.fromBundle(getArguments()).getTuitionId();
            RequestModel2 model = new RequestModel2();
            model.setTuitionId(tuitionId);
            DatabaseUtils.getTuitionDetail(model, new ApiInterface() {
                @Override
                public void onSuccess(Object obj) {
                    AppUtils.hideDialog();
                    List<TuitionDetailResponse.Tuition> tuitions = (List<TuitionDetailResponse.Tuition>) obj;
                    if (null != tuitions && !tuitions.isEmpty()) {
                        requestTuitionModel = tuitions.get(0).getTuitionModel().get(0);
                        Log.d(TAG, "getTuitionDetails: " + requestTuitionModel.toString());
                        binding.setTuition(requestTuitionModel);
                        updateRequestStatus(requestTuitionModel);


                        if (null != requestTuitionModel.getTeacherId() && !requestTuitionModel.getTeacherId().isEmpty())
                            getTeacherProfile();
                    }

                }

                @Override
                public void onFailed(String msg) {
                    AppUtils.hideDialog();
                    Toast.makeText(requireActivity(), "could't find tuition Detail !!", Toast.LENGTH_SHORT).show();
                }
            });


        }


    }

    private void updateRequestStatus(RequestTuitionModel tuitionModel) {
        switch (tuitionModel.getReqStatus()) {
            case REQUEST_STATUS_PENDING:
            case REQUEST_STATUS_PENDING_S:
                binding.btLoading.setAnimation(R.raw.waiting);
                break;
            case REQUEST_STATUS_ACCEPTED:
            case REQUEST_STATUS_ACCEPTED_S:
                binding.btLoading.setAnimation(R.raw.accepted);
                break;
            case REQUEST_STATUS_REJECTED:
            case REQUEST_STATUS_REJECTED_S:
                binding.btLoading.setAnimation(R.raw.rejected);
                break;
        }
        binding.btLoading.playAnimation();

        binding.tvTime.setText(getTimeFormat(tuitionModel.getTimestamp(), "EEE, d MMM yyyy h:mm a"));
        /*if (!tuitionModel.getReqStatus().equals(REQUEST_STATUS_PENDING) && null != tuitionModel.getRequestActionTimestamp())
            requestTuitionBinding.tvTimeAccepted.setText(getTimeFormat(tuitionModel.getRequestActionTimestamp(), "EEE, d MMM yyyy h:mm a"));
*/
    }

    @Override
    public void onResume() {
        super.onResume();
        getTuitionDetails();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }
}