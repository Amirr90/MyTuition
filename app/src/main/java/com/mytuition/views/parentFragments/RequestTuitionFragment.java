package com.mytuition.views.parentFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.mytuition.R;
import com.mytuition.databinding.FragmentRequestTuitionBinding;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.ParentModel;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.DatabaseUtils;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;

import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.Utils.getParentModel;

public class RequestTuitionFragment extends Fragment {
    private static final String TAG = "RequestTuitionFragment";


    public static final String REQUEST_TUITION = "TuitionRequest";
    public static final String REQUEST_STATUS_PENDING = "Pending";
    public static final String TIME_SLOT = "slot";
    public static final String REQUEST_STATUS_ACCEPTED = "Accepted";
    public static final String REQUEST_STATUS_REJECTED = "Rejected";

    FragmentRequestTuitionBinding requestTuitionBinding;
    NavController navController;

    ParentModel parentModel;
    TeacherModel teacherModel;

    String timeSlot = null;
    String date;
    String classId = null;

    RequestTuitionModel requestTuitionModel;

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

        if (null != getArguments()) {

            date = getArguments().getString(AppConstant.DATE);
            timeSlot = getArguments().getString(TIME_SLOT);

            String jsonString = getArguments().getString(AppConstant.TEACHER_MODEL);
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
                if (initReqModel()) {
                    createTuitionReq();
                } else
                    Toast.makeText(requireActivity(), "Something went wrong, try again !!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean initReqModel() {
        requestTuitionModel = new RequestTuitionModel();
        requestTuitionModel.setName(null == parentModel ? "" : parentModel.getName());
        requestTuitionModel.setReqDate(date);
        requestTuitionModel.setReqTime(timeSlot);
        requestTuitionModel.setUid(getUid());
        requestTuitionModel.setTeacherId(null == teacherModel ? "" : teacherModel.getId());
        requestTuitionModel.setTuitionFor(null == classId ? AppConstant.ALL : classId);
        requestTuitionModel.setReqType(null == classId ? AppConstant.REQUEST_TYPE_BY_TEACHER : AppConstant.REQUEST_TYPE_BY_CLASS);
        return null != requestTuitionModel;
    }

    private void createTuitionReq() {

        AppUtils.showRequestDialog(requireActivity());
        DatabaseUtils.reqTuition(requestTuitionModel, new ApiInterface() {
            @Override
            public void onSuccess(Object obj) {
                AppUtils.hideDialog();
                Toasty.success(requireActivity(), (String) obj, Toast.LENGTH_SHORT, true).show();
                navController.navigate(R.id.action_requestTuitonFragment_to_requestTuitionSuccessfullyFragment);
            }

            @Override
            public void onFailed(String msg) {
                AppUtils.hideDialog();
                showFailedDialog(msg);

            }
        });
    }

    private void showFailedDialog(String msg) {
        new AlertDialog.Builder(requireActivity()).setTitle("Failed to request Tuition")
                .setMessage(msg)
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

}