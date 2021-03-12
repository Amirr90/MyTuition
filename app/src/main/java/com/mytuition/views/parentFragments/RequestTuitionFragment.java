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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.mytuition.R;
import com.mytuition.databinding.FragmentRequestTuitionBinding;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.ParentModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.models.TuitionModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.DatabaseUtils;

import org.jetbrains.annotations.NotNull;

import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.AppUtils.objectToJSONArray;
import static com.mytuition.utility.AppUtils.objectToJSONObject;
import static com.mytuition.utility.Utils.getFirebaseReference;
import static com.mytuition.utility.Utils.getParentModel;
import static com.mytuition.views.parentFragments.SelectTimeSlotsFragment.TEACHER;

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
    String classId = null;

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
        parentModel = getParentModel(requireActivity());

        if (null != getArguments().getString(TEACHER)) {
            String jsonString = getArguments().getString(TEACHER);
            Gson gson = new Gson();
            teacherModel = gson.fromJson(jsonString, TeacherModel.class);
            requestTuitionBinding.setTeacher(teacherModel);
        }


        if (null != getArguments().getString("class"))
            classId = getArguments().getString("class");


        requestTuitionBinding.setParent(parentModel);
        timeSlot = getArguments().getString(TIME_SLOT);
        requestTuitionBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTuition();
                //makeTuitionRequest();
            }
        });

    }

    private void makeTuitionRequest() {
        final TuitionModel tuitionModel = new TuitionModel();
        tuitionModel.setRequestTimeSLot(timeSlot);
        tuitionModel.setParentId(getUid());
        tuitionModel.setRequestStatus(REQUEST_STATUS_PENDING);
        tuitionModel.setParentModel(parentModel);
        tuitionModel.setTeacherModel(teacherModel);
        tuitionModel.setTeacherId(teacherModel.getId());
        tuitionModel.setClassIds(classId);
        Log.d(TAG, "makeTuitionRequest: " + objectToJSONArray(tuitionModel));
        DatabaseUtils.requestTuition(tuitionModel, timeSlot, new ApiInterface() {
            @Override
            public void onSuccess(Object obj) {
                AppUtils.hideDialog();
                navController.navigate(R.id.action_requestTuitonFragment_to_requestTuitionSuccessfullyFragment);
            }

            @Override
            public void onFailed(String msg) {
                AppUtils.hideDialog();
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        Log.d(TAG, "makeTuitionRequest: " + objectToJSONObject(tuitionModel));


    }

    private void requestTuition() {

        final TuitionModel tuitionModel = new TuitionModel();
        tuitionModel.setRequestTimeSLot(timeSlot);
        tuitionModel.setParentId(getUid());
        tuitionModel.setRequestStatus(REQUEST_STATUS_PENDING);
        tuitionModel.setParentModel(parentModel);
        if (classId == null) {
            tuitionModel.setTeacherModel(teacherModel);
            tuitionModel.setTeacherId(teacherModel.getId());
            AppUtils.showRequestDialog(requireActivity());
            AppUtils.getFirestoreReference().collection(REQUEST_TUITION)
                    .whereEqualTo(PARENT_ID, getUid())
                    .whereEqualTo(TEACHER_ID, teacherModel.getId())
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
                            if (null != getUid())
                                AppUtils.getFirestoreReference().collection(REQUEST_TUITION).document(getUid())
                                        .set(tuitionModel)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
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

        } else {
            tuitionModel.setClassIds(classId);
            if (null != getUid())
                AppUtils.getFirestoreReference().collection(REQUEST_TUITION).document(getUid())
                        .set(tuitionModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
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

        //check for tuition request


    }


}