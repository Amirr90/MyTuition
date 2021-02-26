package com.mytuition.teacherView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.mytuition.databinding.FragmentTProfileBinding;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.interfaces.UploadImageInterface;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.mytuition.utility.AppUtils.getAllSpeciality;
import static com.mytuition.utility.AppUtils.hideDialog;
import static com.mytuition.utility.AppUtils.showToolbar;


public class TProfileFragment extends Fragment {
    private static final String TAG = "TProfileFragment";
    private static final int REQUEST_CODE_FRONT_IMAGE = 11;
    private static final int REQUEST_CODE_BACK_IMAGE = 12;

    FragmentTProfileBinding binding;
    NavController navController;
    TeacherModel teacherModel;
    List<Integer> mSelectedItems;
    String higherEducation, frontImageUrl, backImageUrl;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTProfileBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        teacherModel = new TeacherModel();
        if (null != FirebaseAuth.getInstance().getCurrentUser())
            teacherModel.setMobile(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        binding.setTeacherProfile(teacherModel);


        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + binding.getTeacherProfile().toString());
                if (allFieldIsFilled()) {
                    uploadImage();
                }
            }
        });

        setSpinnerData();

        binding.ivAadharFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(REQUEST_CODE_FRONT_IMAGE);
            }
        });
        binding.ivAadharBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(REQUEST_CODE_BACK_IMAGE);
            }
        });
    }

    private void selectImage(int tag) {
        ImagePicker.Companion.with(this)
                .crop(4f, 4f)
                .compress(512)
                .maxResultSize(1080, 1080)
                .start(tag);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FRONT_IMAGE: {
                    Uri uri = data.getData();
                    binding.ivAadharFront.setImageURI(uri);
                    frontImageUrl = uri.toString();
                    Log.d(TAG, "onActivityResult: FrontUri" + data.getData());
                    break;
                }
                case REQUEST_CODE_BACK_IMAGE: {
                    Uri uri = data.getData();
                    binding.ivAadharBack.setImageURI(uri);
                    backImageUrl = uri.toString();
                    Log.d(TAG, "onActivityResult: BackUri" + data.getData());
                    break;
                }
            }
        }
    }

    private void uploadImage() {
        AppUtils.showRequestDialog(requireActivity());
        List<Bitmap> imageUri = new ArrayList<>();
        imageUri.add(((BitmapDrawable) binding.ivAadharFront.getDrawable()).getBitmap());
        imageUri.add(((BitmapDrawable) binding.ivAadharBack.getDrawable()).getBitmap());
        AppUtils.uploadImages(imageUri, new UploadImageInterface() {
            @Override
            public void onSuccess(Object msg) {
                List<String> uploadedImageUri = (List<String>) msg;
                if (!uploadedImageUri.isEmpty()) {
                    teacherModel.setAadharFrontImage(uploadedImageUri.get(0));
                    teacherModel.setAadharBackImage(uploadedImageUri.get(1));
                }

                AppUtils.updateTeacherProfile(teacherModel, new ApiInterface() {
                    @Override
                    public void onSuccess(Object obj) {
                        hideDialog();
                        Toast.makeText(requireActivity(), (String) obj, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(String msg) {
                        hideDialog();
                        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailed(String msg) {

                hideDialog();
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean allFieldIsFilled() {
        teacherModel = binding.getTeacherProfile();
        if (null == teacherModel.getName() || teacherModel.getName().isEmpty()) {
            Toast.makeText(requireActivity(), "Name required !!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (null == teacherModel.getFatherName() || teacherModel.getFatherName().isEmpty()) {
            Toast.makeText(requireActivity(), "Father's name required !!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (null == teacherModel.getEmail() || teacherModel.getEmail().isEmpty()) {
            Toast.makeText(requireActivity(), "email required !!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (null == teacherModel.getAddress() || teacherModel.getAddress().isEmpty()) {
            Toast.makeText(requireActivity(), "Address required !!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (null == teacherModel.getLandMark() || teacherModel.getLandMark().isEmpty()) {
            Toast.makeText(requireActivity(), "landMark required !!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (null == teacherModel.getCity() || teacherModel.getCity().isEmpty()) {
            Toast.makeText(requireActivity(), "city required !!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (null == teacherModel.getState() || teacherModel.getState().isEmpty()) {
            Toast.makeText(requireActivity(), "State required !!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (null == higherEducation || higherEducation.isEmpty()) {
            Toast.makeText(requireActivity(), "highest education required !!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (null == frontImageUrl || frontImageUrl.isEmpty()) {
            Toast.makeText(requireActivity(), "Select Aadhar's front Image!!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (null == backImageUrl || backImageUrl.isEmpty()) {
            Toast.makeText(requireActivity(), "Select Aadhar's back image !!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (null == teacherModel.getSchoolName() || teacherModel.getSchoolName().isEmpty()) {
            Toast.makeText(requireActivity(), "School/College Name required !!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setSpinnerData() {

        //Speciality Spinner
        List<String> specialities = getAllSpeciality();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, specialities);
        binding.specialitySpinner.setAdapter(adapter);

        //Highest Education Spinner
        String[] items = new String[]{"High School", "Intermediate", "Graduate", "Post Graduate", "P.hd"};
        ArrayAdapter<String> highestEducationAdapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        binding.highestEducationSpinner.setAdapter(highestEducationAdapter);


        //Monthly Fee Spinner
        String[] fee = new String[]{"₹1000-₹2000", "₹2000-₹2500", "₹2500-₹3000", "₹3000-₹3500", "₹3500-₹4000", "₹4000-₹4500", "₹5000-₹5500", "₹6000-₹6500", "₹6500-₹7000", "₹10,000"};
        ArrayAdapter<String> feeAdapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, fee);
        binding.monthlyFeeSpinner.setAdapter(feeAdapter);

        //Per Visit Spinner
        String[] preVisit = new String[]{"₹100-₹200", "₹200-₹250", "₹250-₹300", "₹300-₹350"};
        ArrayAdapter<String> preVisitAdapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, preVisit);
        binding.perVisitSpinner.setAdapter(preVisitAdapter);

        //Experience Spinner
        String[] experience = new String[]{"0(Fresher)", "1", "2", "3", "4", "5", "6", "7", "8+ years"};
        ArrayAdapter<String> experienceAdapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, experience);
        binding.expSpinner.setAdapter(experienceAdapter);


        binding.btnAllSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllSubjectDialog();
            }
        });


    }

    private void showAllSubjectDialog() {
        mSelectedItems = new ArrayList<Integer>();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        final String[] choices = new String[getAllSpeciality().size()];
        for (int a = 0; a < getAllSpeciality().size(); a++)
            choices[a] = getAllSpeciality().get(a);
        builder.setTitle("Choose One or More")
                .setMultiChoiceItems(choices, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mSelectedItems.add(which);
                        } else if (mSelectedItems.contains(which)) {
                            mSelectedItems.remove(Integer.valueOf(which));
                        }

                        // you can also add other codes here,
                        // for example a tool tip that gives user an idea of what he is selecting
                        // showToast("Just an example description.");
                    }

                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String selectedIndex = "";
                for (Integer i : mSelectedItems) {
                    selectedIndex += choices[i] + ", ";
                }

                String msg = ("Selected index: " + selectedIndex);
                binding.tvSelectedSubjects.setText(selectedIndex);
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        }).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        showToolbar(requireActivity());
    }


}