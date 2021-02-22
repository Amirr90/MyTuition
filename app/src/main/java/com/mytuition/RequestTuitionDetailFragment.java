package com.mytuition;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mytuition.databinding.FragmentRequestTuitionBinding;
import com.mytuition.databinding.FragmentRequestTuitionDetailBinding;
import com.mytuition.databinding.FragmentRequestedTuitionDetailBinding;
import com.mytuition.models.TeacherModel;
import com.mytuition.models.TuitionModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.views.activity.ParentScreen;
import com.mytuition.views.parentFragments.ParentDashboardFragment;
import com.mytuition.views.parentFragments.RequestTuitionFragment;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getTimeFormat;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.AppUtils.hideDialog;
import static com.mytuition.utility.AppUtils.parseDate;
import static com.mytuition.utility.AppUtils.showRequestDialog;
import static com.mytuition.utility.Utils.getFirebaseReference;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_ACCEPTED;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_PENDING;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_REJECTED;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_TUITION;
import static com.mytuition.views.parentFragments.SelectTimeSlotsFragment.TEACHER;


public class RequestTuitionDetailFragment extends Fragment {
    private static final String TAG = "RequestTuitionDetailFra";

    FragmentRequestTuitionDetailBinding requestTuitionBinding;
    NavController navController;
    TuitionModel tuitionModel;

    int selectedPosition = -1;

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


        AppUtils.showRequestDialog(requireActivity());
        getTuitionDetails();

        requestTuitionBinding.tvViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTeacherProfile();
            }
        });
        requestTuitionBinding.btnChangeTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToChangeTeacher();
            }
        });

    }

    private void getTeacherProfile() {
        showRequestDialog(requireActivity());
        Log.d(TAG, "getTeacherProfile: " + tuitionModel.getTeacherId());
        getFirebaseReference("Teachers").child(tuitionModel.getTeacherId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideDialog();
                TeacherModel teacherModel = dataSnapshot.getValue(TeacherModel.class);
                if (teacherModel != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("docModel", teacherModel.toString());
                    navController.navigate(R.id.action_DetailsFragment2_to_teacherProfileFragment, bundle);
                } else {
                    Log.d(TAG, "onDataChange: " + dataSnapshot.toString());
                    Toast.makeText(requireActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "DatabaseError: " + databaseError.toString());
                Toast.makeText(requireActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTuitionDetails() {
        if (getUid() != null)
            AppUtils.getFirestoreReference().collection(RequestTuitionFragment.REQUEST_TUITION).document(getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            hideDialog();
                            tuitionModel = documentSnapshot.toObject(TuitionModel.class);
                            if (null != tuitionModel) {
                                requestTuitionBinding.setTeacher(tuitionModel.getTeacherModel());
                                requestTuitionBinding.setTuition(tuitionModel);
                                updateRequestStatus(tuitionModel);
                                Log.d(TAG, "onSuccess: " + tuitionModel.toString());
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideDialog();
                    Toast.makeText(requireActivity(), "try again", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                }
            });
    }

    private void updateRequestStatus(TuitionModel tuitionModel) {
        switch (tuitionModel.getRequestStatus()) {
            case REQUEST_STATUS_PENDING:
                requestTuitionBinding.btLoading.setAnimation(R.raw.waiting);
                break;
            case REQUEST_STATUS_ACCEPTED:
                requestTuitionBinding.btLoading.setAnimation(R.raw.accepted);
                break;
            case REQUEST_STATUS_REJECTED:
                requestTuitionBinding.btLoading.setAnimation(R.raw.rejected);
                break;
        }
        requestTuitionBinding.btLoading.playAnimation();

        requestTuitionBinding.tvTime.setText(getTimeFormat(tuitionModel.getTimestamp(), "EEE, d MMM yyyy h:mm a"));
        if (!tuitionModel.requestStatus.equals(REQUEST_STATUS_PENDING) && null != tuitionModel.getRequestActionTimestamp())
            requestTuitionBinding.tvTimeAccepted.setText(getTimeFormat(tuitionModel.getRequestActionTimestamp(), "EEE, d MMM yyyy h:mm a"));
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }

    public void showDialogToChangeTeacher() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        final String[] choices = {"Less experience", "Less educated", "Timing issue", "Fee Issue"};
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
            AppUtils.getFirestoreReference().collection(REQUEST_TUITION).document(getUid()).update(getUpdateMap(choice)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AppUtils.hideDialog();
                    Toast.makeText(requireActivity(), "request Submitted successfully !!", Toast.LENGTH_SHORT).show();
                    ParentScreen.getInstance().onSupportNavigateUp();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    AppUtils.hideDialog();
                    Toast.makeText(requireActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private Map<String, Object> getUpdateMap(String choice) {
        Map<String, Object> objectsMap = new HashMap<>();
        objectsMap.put("requestStatus", REQUEST_STATUS_PENDING);
        objectsMap.put("teacherModel", null);
        objectsMap.put("reason", choice);
        objectsMap.put("rejectedTeacherId", tuitionModel.getTeacherId());
        objectsMap.put("requestActionTimestamp", System.currentTimeMillis());
        return objectsMap;
    }

    private void showToast(String s) {
        Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
    }
}