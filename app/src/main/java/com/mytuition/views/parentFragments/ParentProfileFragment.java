package com.mytuition.views.parentFragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mytuition.BR;
import com.mytuition.R;
import com.mytuition.databinding.FragmentParentProfileBinding;
import com.mytuition.databinding.FragmentTeacherProfileBinding;
import com.mytuition.models.ParentModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.FileUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
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


    boolean isPicChange = false;

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
                                    setParentModel(requireActivity(), parentModel);
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

        profileBinding.tvChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) profileBinding.tvChangeProfile.getTag();
                selectImage(tag);
            }
        });
    }

    private void selectImage(int tag) {
        ImagePicker.Companion.with(this)
                .crop(4f, 4f)                    //Crop image(Optional), Check Customization for more option
                .compress(512)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (null != data) {

                try {
                    Log.d(TAG, "onActivityResult: "+data.getData());
                    Uri uri = data.getData();
                    profileBinding.profileImage.setImageURI(uri);
                    isPicChange = true;
                    File file = FileUtil.from(requireActivity(), uri);
                    uploadImage(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onActivityResult: "+e.getLocalizedMessage());
                }
            }

        }


    }

    private void uploadImage(File file) {
        Log.d(TAG, "uploadImage: " + file);
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