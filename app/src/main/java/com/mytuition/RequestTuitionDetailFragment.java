package com.mytuition;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.mytuition.databinding.FragmentRequestTuitionDetailBinding;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.RequestModel2;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.responseModel.TuitionDetailResponse;
import com.mytuition.utility.App;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.DatabaseUtils;
import com.mytuition.views.activity.ParentScreen;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getTimeFormat;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.AppUtils.hideDialog;


public class RequestTuitionDetailFragment extends Fragment {
    public static final String REQUEST_TUITION = "TuitionRequest";
    public static final String REQUEST_STATUS_PENDING = "Pending";
    public static final String REQUEST_STATUS_PENDING_S = "pending";
    public static final String TIME_SLOT = "slot";
    public static final String REQUEST_STATUS_ACCEPTED = "Accepted";
    public static final String REQUEST_STATUS_REJECTED = "Rejected";
    public static final String REQUEST_STATUS_ACCEPTED_S = "accepted";
    public static final String REQUEST_STATUS_REJECTED_S = "rejected";
    private static final String TAG = "RequestTuitionDetailFra";
    FragmentRequestTuitionDetailBinding requestTuitionBinding;
    NavController navController;
    int selectedPosition = -1;
    TeacherModel teacherModel;
    RequestTuitionModel requestTuitionModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        requestTuitionBinding = FragmentRequestTuitionDetailBinding.inflate(getLayoutInflater());

        return requestTuitionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        requestTuitionBinding.tvViewProfile.setOnClickListener(v -> {
            if (teacherModel != null) {

                Gson gson = new Gson();
                String jsonString = gson.toJson(teacherModel);
                try {
                    JSONObject request = new JSONObject(jsonString);
                    Bundle bundle = new Bundle();
                    bundle.putString("docModel", request.toString());
                    bundle.putBoolean("tuitionPage", true);
                    navController.navigate(R.id.action_DetailsFragment2_to_teacherProfileFragment, bundle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(requireActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            }
        });

        requestTuitionBinding.btnChangeTeacher.setOnClickListener(v -> showDialogToChangeTeacher());


        requestTuitionBinding.btnCancelTuitionReq.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(AppConstant.TUITION_ID, requestTuitionModel.getId());
            bundle.putString(AppConstant.TEACHER_ID, requestTuitionModel.getTeacherId());
            navController.navigate(R.id.action_DetailsFragment2_to_cancelTuitionReqDialog, bundle);
        });

        requestTuitionBinding.animationViewAudioCall.setOnClickListener(v -> {
            performVoiceCall();
        });
    }

    private void performVoiceCall() {
        if (null != teacherModel && null != teacherModel.getPhoneNumber()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + teacherModel.getPhoneNumber()));
            startActivity(intent);
        } else
            Toast.makeText(App.context, "Mobile Number not updated !!", Toast.LENGTH_SHORT).show();
    }

    private void getTeacherProfile() {
        getFirestoreReference().collection(AppConstant.TEACHER)
                .document(requestTuitionModel.getTeacherId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    hideDialog();
                    if (documentSnapshot.exists()) {
                        teacherModel = documentSnapshot.toObject(TeacherModel.class);
                        requestTuitionBinding.setTeacher(teacherModel);
                        if (null != teacherModel.getTeachingProfile().getExperience())
                            requestTuitionBinding.imageView9.setImageResource(AppUtils.setExperience(teacherModel.getTeachingProfile().getExperience()));
                    }
                });

    }


    private void getTuitionDetails() {
        if (getArguments() != null) {
            AppUtils.showRequestDialog(requireActivity());
            final String tuitionId = RequestTuitionDetailFragmentArgs.fromBundle(getArguments()).getTuitionId();
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
                        requestTuitionBinding.setTuition(requestTuitionModel);
                        updateRequestStatus(requestTuitionModel);


                        if (null != requestTuitionModel.getTeacherId() && !requestTuitionModel.getTeacherId().isEmpty())
                            getTeacherProfile();

                        if (requestTuitionModel.getReqStatus().equalsIgnoreCase(AppConstant.REQUEST_STATUS_REJECTED)) {
                            showSuggestionDialog(requestTuitionModel);
                        }
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

    private void showSuggestionDialog(RequestTuitionModel requestTuitionModel) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.TEACHER_NAME, requestTuitionModel.getName());
        bundle.putString(AppConstant.TUITION_ID, requestTuitionModel.getId());
        navController.navigate(R.id.action_DetailsFragment2_to_suggestionTeacherForRejectStatusFragment, bundle);
    }

    private void updateRequestStatus(RequestTuitionModel tuitionModel) {
        switch (tuitionModel.getReqStatus()) {
            case REQUEST_STATUS_PENDING:
            case REQUEST_STATUS_PENDING_S:
                requestTuitionBinding.btLoading.setAnimation(R.raw.waiting);
                break;
            case REQUEST_STATUS_ACCEPTED:
            case REQUEST_STATUS_ACCEPTED_S:
                requestTuitionBinding.btLoading.setAnimation(R.raw.accepted);
                break;
            case REQUEST_STATUS_REJECTED:
            case REQUEST_STATUS_REJECTED_S:
                requestTuitionBinding.btLoading.setAnimation(R.raw.rejected);
                break;
        }
        requestTuitionBinding.btLoading.playAnimation();

        requestTuitionBinding.tvTime.setText(getTimeFormat(tuitionModel.getTimestamp(), "EEE, d MMM yyyy h:mm a"));
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

    public void showDialogToChangeTeacher() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        final String[] choices = {"Less experience", "Timing issue", "Fee Issue"};
        builder.setTitle("Please select the reason of change Teacher ")
                .setSingleChoiceItems(choices, 0, (arg0, arg1) -> selectedPosition = arg1).setPositiveButton("OK", (dialog, id) -> {
            if (selectedPosition < 0) {
                Toast.makeText(requireActivity(), "Select an issue", Toast.LENGTH_SHORT).show();
            } else {
                swapTeacher(choices[selectedPosition]);
            }

        }).setNegativeButton("Cancel", (dialog, id) -> {
        }).show();

    }

    private void swapTeacher(String choice) {
        AppUtils.showRequestDialog(requireActivity());

        if (getUid() != null)
            AppUtils.getFirestoreReference().collection(REQUEST_TUITION)
                    .document(requestTuitionModel.getId()).update(getUpdateMap(choice))
                    .addOnSuccessListener(aVoid -> {
                        hideDialog();
                        Toast.makeText(requireActivity(), "request Submitted successfully !!", Toast.LENGTH_SHORT).show();
                        ParentScreen.getInstance().onSupportNavigateUp();
                    }).addOnFailureListener(e -> {
                hideDialog();
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                Toast.makeText(requireActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            });
    }

    private Map<String, Object> getUpdateMap(String choice) {
        Map<String, Object> objectsMap = new HashMap<>();
        objectsMap.put("reqStatus", REQUEST_STATUS_PENDING);
        objectsMap.put("reason", choice);
        objectsMap.put("teacherId", "");
        objectsMap.put("rejectedTeacherId", requestTuitionModel.getTeacherId());
        objectsMap.put("requestActionTimestamp", System.currentTimeMillis());
        return objectsMap;
    }

    private void showToast(String s) {
        Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
    }


}