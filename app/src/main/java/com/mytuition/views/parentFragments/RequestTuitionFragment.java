package com.mytuition.views.parentFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.mytuition.R;
import com.mytuition.databinding.FragmentRequestTuitionBinding;
import com.mytuition.models.ParentModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.models.TuitionModel;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import static com.mytuition.utility.Utils.getFirebaseReference;

public class RequestTuitionFragment extends Fragment {
    private static final String TAG = "RequestTuitionFragment";

    public static final String REQUEST_TUITION = "TuitionRequest";
    public static final String REQUEST_STATUS_PENDING = "Pending";
    public static final String TIME_SLOT = "slot";
    public static final String REQUEST_STATUS_ACCEPTED = "Accepted";
    public static final String REQUEST_STATUS_REJECTED = "Rejected";
    public static final String PARENT_ID = "parentId";
    public static final String TEACHER_ID = "teacherId";
    FragmentRequestTuitionBinding requestTuitionBinding;
    NavController navController;

    ParentModel parentModel;
    TeacherModel teacherModel;

    String timeSlot = null;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requestTuitionBinding = FragmentRequestTuitionBinding.inflate(getLayoutInflater());
        return requestTuitionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        if (getArguments() == null)
            return;
        parentModel = getParentModel();
        teacherModel = getTeacherModel();

        requestTuitionBinding.setParent(parentModel);
        requestTuitionBinding.setTeacher(teacherModel);


        timeSlot = getArguments().getString(TIME_SLOT);
        requestTuitionBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTuition();
            }
        });

    }

    private void requestTuition() {

        final TuitionModel tuitionModel = new TuitionModel();
        tuitionModel.setParentModel(getParentModel());
        tuitionModel.setTeacherModel(getTeacherModel());
        tuitionModel.setRequestStatus(REQUEST_STATUS_PENDING);
        tuitionModel.setRequestTimeSLot(timeSlot);
        tuitionModel.setParentId(getParentModel().getId());
        tuitionModel.setTeacherId(getTeacherModel().getId());

        //check for tuition request
        AppUtils.showRequestDialog(requireActivity());
        AppUtils.getFirestoreReference().collection(REQUEST_TUITION)
                .whereEqualTo(PARENT_ID, getParentModel().getId())
                .whereEqualTo(TEACHER_ID, getTeacherModel().getId())
                .whereEqualTo("requestStatus", REQUEST_STATUS_PENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSuccess: " + queryDocumentSnapshots.getDocuments());
                        if (!queryDocumentSnapshots.isEmpty()) {
                            AppUtils.hideDialog();
                            Toast.makeText(requireActivity(), "You already requested to this Teacher", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        AppUtils.getFirestoreReference().collection(REQUEST_TUITION).add(tuitionModel)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        AppUtils.hideDialog();
                                        navController.navigate(R.id.action_requestTuitonFragment_to_requestTuitionSuccessfullyFragment);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                AppUtils.hideDialog();
                                Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


    }

    private TeacherModel getTeacherModel() {
        TeacherModel teacherModel = new TeacherModel();
        teacherModel.setName("Teacher Name");
        teacherModel.setExperience("5");
        teacherModel.setSpeciality("Computer");
        teacherModel.setFee("500");
        teacherModel.setRating("5");
        teacherModel.setReview("500");
        teacherModel.setId("987654321");
        teacherModel.setImage("https://img.pngio.com/hd-teach-blogger-round-logo-png-transparent-png-image-download-teach-png-533_533.png");
        return teacherModel;
    }

    private ParentModel getParentModel() {
        ParentModel parentModel = new ParentModel();
        parentModel.setAddress("RajajiPuram");
        parentModel.setGender("male");
        parentModel.setImage("https://img.pngio.com/hd-teach-blogger-round-logo-png-transparent-png-image-download-teach-png-533_533.png");
        parentModel.setMobile("9044865611");
        parentModel.setName("Amir");
        parentModel.setDob("12/12/2000");
        parentModel.setEmail("aamirr.1232@gmail.com");
        parentModel.setId("123456789");
        return parentModel;
    }


}