package com.mytuition.views.parentFragments;

import android.app.AlertDialog;
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

import com.firebase.ui.auth.data.model.User;
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
    public static final String REQUEST_TUITION = "TuitionRequest";
    public static final String REQUEST_STATUS_PENDING = "Pending";
    public static final String TIME_SLOT = "slot";
    public static final String REQUEST_STATUS_ACCEPTED = "Accepted";
    public static final String REQUEST_STATUS_REJECTED = "Rejected";
    private static final String TAG = "RequestTuitionFragment";
    FragmentRequestTuitionBinding requestTuitionBinding;
    NavController navController;

    ParentModel parentModel;
    TeacherModel teacherModel;

    String timeSlot = null;
    String date;
    String classId = null;

    RequestTuitionModel requestTuitionModel;
    User user;

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

        if (null == getParentModel(requireActivity()) || (null == getParentModel(requireActivity()).getName()) || getParentModel(requireActivity()).getName().isEmpty())
            navController.navigate(R.id.action_requestTuitonFragment_to_parentProfileFragment);


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
        requestTuitionBinding.btnConfirm.setOnClickListener(view1 -> {
            if (initReqModel()) {
                createTuitionReq();
            } else
                Toast.makeText(requireActivity(), "Something went wrong, try again !!", Toast.LENGTH_SHORT).show();
        });

    }

    private boolean initReqModel() {
        String tuitionFor;
        if (null != getArguments())
            tuitionFor = getArguments().getString(AppConstant.SPECIALITY_NAME);
        else tuitionFor = "";

        // String name = requestTuitionBinding.editTextTextPersonName2.getText().toString();
        requestTuitionModel = new RequestTuitionModel();
        requestTuitionModel = new RequestTuitionModel();
        requestTuitionModel.setName(null == teacherModel ? "Not Defined" : teacherModel.getName());
        requestTuitionModel.setReqDate(date);
        requestTuitionModel.setUpdateOnWhatsApp(requestTuitionBinding.radioWhatsAppBtn.isChecked());
        requestTuitionModel.setReqTime(timeSlot);
        requestTuitionModel.setUid(getUid());
        requestTuitionModel.setTeacherId(null == teacherModel ? "" : teacherModel.getId());
        requestTuitionModel.setTuitionFor(null == classId ? tuitionFor : classId);
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
                .setPositiveButton("Dismiss", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

}