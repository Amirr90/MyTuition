package com.mytuition;

import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.mytuition.databinding.FragmentRequestTuitionDetailBinding;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;
import com.mytuition.views.activity.ParentScreen;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getTimeFormat;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.AppUtils.hideDialog;


public class RequestTuitionDetailFragment extends Fragment {
    private static final String TAG = "RequestTuitionDetailFra";

    FragmentRequestTuitionDetailBinding requestTuitionBinding;
    NavController navController;

    int selectedPosition = -1;

    TeacherModel teacherModel;

    RequestTuitionModel requestTuitionModel;

    public static final String REQUEST_TUITION = "TuitionRequest";
    public static final String REQUEST_STATUS_PENDING = "Pending";
    public static final String REQUEST_STATUS_PENDING_S = "pending";
    public static final String TIME_SLOT = "slot";
    public static final String REQUEST_STATUS_ACCEPTED = "Accepted";
    public static final String REQUEST_STATUS_REJECTED = "Rejected";
    public static final String REQUEST_STATUS_ACCEPTED_S = "accepted";
    public static final String REQUEST_STATUS_REJECTED_S = "rejected";

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




        requestTuitionBinding.tvViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teacherModel != null) {

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(teacherModel);
                    try {
                        JSONObject request = new JSONObject(jsonString);
                        Bundle bundle = new Bundle();
                        bundle.putString("docModel", request.toString());
                        navController.navigate(R.id.action_DetailsFragment2_to_teacherProfileFragment, bundle);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(requireActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestTuitionBinding.btnChangeTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToChangeTeacher();
            }
        });


        requestTuitionBinding.btnCancelTuitionReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstant.TUITION_ID, requestTuitionModel.getId());
                bundle.putString(AppConstant.TEACHER_ID, requestTuitionModel.getTeacherId());
                navController.navigate(R.id.action_DetailsFragment2_to_cancelTuitionReqDialog, bundle);
            }
        });
    }

    private void getTeacherProfile() {
        getFirestoreReference().collection(AppConstant.TEACHER).document(requestTuitionModel.getTeacherId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                hideDialog();
                if (documentSnapshot.exists()) {
                    teacherModel = documentSnapshot.toObject(TeacherModel.class);
                    requestTuitionBinding.setTeacher(teacherModel);
                }
            }
        });

    }

    private void getTuitionDetails() {
        if (getArguments() != null) {
            String jsonString = getArguments().getString(AppConstant.TUITION_MODEL);
            Gson gson = new Gson();
            requestTuitionModel = gson.fromJson(jsonString, RequestTuitionModel.class);

            Log.d(TAG, "getTuitionDetails: " + requestTuitionModel.toString());
            requestTuitionBinding.setTuition(requestTuitionModel);
            updateRequestStatus(requestTuitionModel);

            if (null != requestTuitionModel.getTeacherId() && !requestTuitionModel.getTeacherId().isEmpty())
                getTeacherProfile();
        }


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
                .setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        selectedPosition = arg1;
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (selectedPosition < 0) {
                    Toast.makeText(requireActivity(), "Select an issue", Toast.LENGTH_SHORT).show();
                } else {
                    swapTeacher(choices[selectedPosition]);
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        }).show();

    }

    private void swapTeacher(String choice) {
        AppUtils.showRequestDialog(requireActivity());


        if (getUid() != null)
            AppUtils.getFirestoreReference().collection(REQUEST_TUITION)
                    .document(requestTuitionModel.getId()).update(getUpdateMap(choice))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            hideDialog();
                            Toast.makeText(requireActivity(), "request Submitted successfully !!", Toast.LENGTH_SHORT).show();
                            ParentScreen.getInstance().onSupportNavigateUp();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideDialog();
                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                    Toast.makeText(requireActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                }
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