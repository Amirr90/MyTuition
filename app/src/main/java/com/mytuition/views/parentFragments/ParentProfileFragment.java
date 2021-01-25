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
import com.mytuition.BR;
import com.mytuition.R;
import com.mytuition.databinding.FragmentParentProfileBinding;
import com.mytuition.databinding.FragmentTeacherProfileBinding;
import com.mytuition.models.ParentModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.mytuition.utility.AppConstant.USERS;
import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.AppUtils.isEmailValid;
import static com.mytuition.utility.Utils.getParentModel;
import static com.mytuition.utility.Utils.setParentModel;

public class ParentProfileFragment extends Fragment {
    private static final String TAG = "ParentProfileFragment";


    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String EMAIL = "email";
    FragmentParentProfileBinding profileBinding;
    NavController navController;

    ParentModel parentModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileBinding = FragmentParentProfileBinding.inflate(getLayoutInflater());
        return profileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        parentModel = getParentModel(requireActivity());

        profileBinding.setUser(parentModel);

        profileBinding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAllFieldCheck() && null != getUid()) {
                    AppUtils.showRequestDialog(requireActivity());
                    getFirestoreReference().collection(USERS).document(getUid())
                            .update(getUserMap(parentModel))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    AppUtils.hideDialog();
                                    Toast.makeText(requireActivity(), "Profile Updated !!", Toast.LENGTH_SHORT).show();
                                    setParentModel(requireActivity(),parentModel);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AppUtils.hideDialog();
                            Toast.makeText(requireActivity(), "try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private Map<String, Object> getUserMap(ParentModel parentModel) {
        Map<String, Object> map = new HashMap<>();
        map.put(NAME, parentModel.getName());
        map.put(ADDRESS, parentModel.getAddress());
        map.put(EMAIL, parentModel.getEmail());
        return map;
    }

    private boolean isAllFieldCheck() {

        if (null == parentModel) {
            Log.d(TAG, "isAllFieldCheck: null");
            return false;
        } else if (profileBinding.editTextTextPersonName.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (profileBinding.editTextTextPersonEmail.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Enter email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isEmailValid(profileBinding.editTextTextPersonEmail.getText().toString())) {
            Toast.makeText(requireActivity(), "Enter valid email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (profileBinding.editTextTextPersonNumber.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Enter mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (profileBinding.editTextTextPersonAddress.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Enter address", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            parentModel.setName(profileBinding.editTextTextPersonName.getText().toString());
            parentModel.setAddress(profileBinding.editTextTextPersonAddress.getText().toString());
            parentModel.setEmail(profileBinding.editTextTextPersonEmail.getText().toString());
            return true;
        }
    }
}