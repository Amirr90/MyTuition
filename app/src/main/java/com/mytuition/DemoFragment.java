package com.mytuition;

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

import com.firebase.ui.auth.AuthUI;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.mytuition.databinding.FragmentDemoBinding;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.interfaces.UploadImageInterface;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.views.SplashScreen;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;
import static com.mytuition.utility.AppUtils.getAllSpeciality;
import static com.mytuition.utility.AppUtils.getMobileNumber;
import static com.mytuition.utility.AppUtils.hideDialog;
import static com.mytuition.utility.Utils.getCityList;
import static com.mytuition.utility.Utils.getStateList;

public class DemoFragment extends Fragment {
    private static final String TAG = "DemoFragment";
    private static final int REQUEST_CODE_FRONT_IMAGE = 11;
    private static final int REQUEST_CODE_BACK_IMAGE = 12;

    FragmentDemoBinding binding;
    List<Integer> mSelectedItems;
    String higherEducation, frontImageUrl, backImageUrl;

    TeacherModel teacherModel;

    boolean[] listSelected;
    NavController navController;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDemoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        mSelectedItems = new ArrayList<>();

        listSelected = getSelectedItemsList();


        getTeacherProfile();


        binding.btnSubmit.setOnClickListener(v -> {
            Log.d(TAG, "onSubmitBtn Clicked !! : " + binding.getTeacherProfile().toString());
            if (allFieldIsFilled()) {
                if (null != frontImageUrl && null != backImageUrl)
                    uploadImage();
                else updateProfile();
            }
        });

        binding.tvSelectedSubjectsLayout.setEndIconOnClickListener(view2 -> {
            showAllSubjectDialog();
        });

        binding.ivAadharFront.setOnClickListener(v -> selectImage(REQUEST_CODE_FRONT_IMAGE));
        binding.ivAadharBack.setOnClickListener(v -> selectImage(REQUEST_CODE_BACK_IMAGE));
        binding.btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(requireActivity())
                .setMessage("Do you really want to logout?")
                .setIcon(R.drawable.app_icon)
                .setPositiveButton("Yes",
                        (dialog, id) -> {
                            dialog.cancel();
                            logout();
                        })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel()).show();

    }

    private void logout() {
        AppUtils.showRequestDialog(requireActivity());
        AuthUI.getInstance()
                .signOut(requireActivity())
                .addOnCompleteListener(task -> {
                    hideDialog();
                    Intent intent = new Intent(requireActivity(), SplashScreen.class);
                    startActivity(intent);
                    Toast.makeText(requireActivity(), "logged out successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                });
    }

    private void getTeacherProfile() {
        if (null == getArguments()) {
            teacherModel = new TeacherModel();
            teacherModel.setProfile(new TeacherModel.Profile());
            teacherModel.setAcademicInformation(new TeacherModel.AcademicInformation());
            teacherModel.setTeachingProfile(new TeacherModel.TeachingProfile());
        } else {
            Gson gson = new Gson();
            teacherModel = gson.fromJson(getArguments().getString("teacherModel"), TeacherModel.class);
            Log.d(TAG, "getTeacherProfile: " + teacherModel);

            if (null != teacherModel && null != teacherModel.getAcademicInformation() && null != teacherModel.getProfile()) {
                binding.setTeacherProfile(teacherModel);
                List<String> selectedSubjects = teacherModel.getTeachingProfile().getTeachingSubject();
                Log.d(TAG, "getTeacherProfile: " + selectedSubjects);
                String selectedIndex = "";

                if (null != selectedSubjects && !selectedSubjects.isEmpty())
                    for (int a = 0; a < selectedSubjects.size(); a++) {
                        selectedIndex += selectedSubjects.get(a) + ", ";

                        for (int b = 0; b < getAllSpeciality().size(); b++) {
                            if (getAllSpeciality().get(b).equalsIgnoreCase(selectedSubjects.get(a))) {
                                mSelectedItems.add(b);
                                listSelected[b] = true;
                                Log.d(TAG, "getTeacherProfile: " + getAllSpeciality().get(b));
                            }
                        }

                    }

                binding.tvSelectedSubjects.setText(selectedIndex);
            }
        }


        binding.etNumber.setText(getMobileNumber());
        binding.setTeacherProfile(teacherModel);
        settingDropDownAdapters();
    }

    private boolean allFieldIsFilled() {
        teacherModel = binding.getTeacherProfile();
        /*if (null == teacherModel.getName() || teacherModel.getName().isEmpty()) {
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
        }*/
        return true;
    }


    private void selectImage(int tag) {
        ImagePicker.Companion.with(this)
                .crop(4f, 4f)
                .compress(256)
                .maxResultSize(540, 540)
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

    private void settingDropDownAdapters() {
        //city Adapter
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, getCityList());
        binding.etCity.setAdapter(cityAdapter);


        //state adapter
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, getStateList());
        binding.etState.setAdapter(stateAdapter);


        //Highest Education Spinner
        final String[] items = new String[]{"High School", "Intermediate", "Graduate", "Post Graduate", "P.hd"};
        ArrayAdapter<String> highestEducationAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        binding.etHighestEducation.setAdapter(highestEducationAdapter);


        //Monthly Fee Spinner
        String[] fee = new String[]{"₹1000-₹2000", "₹2000-₹2500", "₹2500-₹3000", "₹3000-₹3500", "₹3500-₹4000", "₹4000-₹4500", "₹5000-₹5500", "₹6000-₹6500", "₹6500-₹7000", "₹10,000"};
        ArrayAdapter<String> feeAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, fee);
        binding.monthlyFeeSpinner.setAdapter(feeAdapter);

        //Per Visit Spinner
        String[] preVisit = new String[]{"₹100-₹200", "₹200-₹250", "₹250-₹300", "₹300-₹350"};
        ArrayAdapter<String> preVisitAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, preVisit);
        binding.perVisitSpinner.setAdapter(preVisitAdapter);

        //Experience Spinner
        String[] experience = new String[]{"0(Fresher)", "1", "2", "3", "4", "5", "6", "7", "8+ years"};
        ArrayAdapter<String> experienceAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, experience);
        binding.expSpinner.setAdapter(experienceAdapter);


        //Speciality Spinner
        List<String> specialities = getAllSpeciality();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, specialities);
        binding.etSpecialistIn.setAdapter(adapter);


        if (null != teacherModel.getProfile() && null != teacherModel.getAcademicInformation() && null != teacherModel.getProfile())
            settingData();
    }

    private void settingData() {
        binding.etState.setText(teacherModel.getProfile().getState(), false);
        binding.etHighestEducation.setText(teacherModel.getAcademicInformation().getHighestEducation(), false);
        binding.monthlyFeeSpinner.setText(String.valueOf(teacherModel.getTeachingProfile().getMonthlyFee()), false);
        binding.perVisitSpinner.setText(String.valueOf(teacherModel.getTeachingProfile().getPerVisitFee()), false);
        binding.expSpinner.setText(String.valueOf(teacherModel.getTeachingProfile().getExperience()), false);
        binding.etSpecialistIn.setText(String.valueOf(teacherModel.getTeachingProfile().getExpertIn()), false);
        binding.etCity.setText(teacherModel.getProfile().getCity(), false);
    }

    private void showAllSubjectDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        final String[] choices = new String[getAllSpeciality().size()];
        for (int a = 0; a < getAllSpeciality().size(); a++)
            choices[a] = getAllSpeciality().get(a);
        builder.setTitle("Choose One or More")
                .setMultiChoiceItems(choices, listSelected, (dialog, which, isChecked) -> {
                    if (isChecked) {
                        mSelectedItems.add(which);
                    } else if (mSelectedItems.contains(which)) {
                        mSelectedItems.remove(Integer.valueOf(which));
                    }
                }).setPositiveButton("OK", (dialog, id) -> {
            String selectedIndex = "";
            for (Integer i : mSelectedItems) {
                selectedIndex += choices[i] + ", ";
            }

            String msg = ("Selected index: " + selectedIndex);
            binding.tvSelectedSubjects.setText(selectedIndex);
            Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();

        }).setNegativeButton("Cancel", (dialog, id) -> {
        }).show();
    }

    private boolean[] getSelectedItemsList() {
        boolean[] booleanList = new boolean[getAllSpeciality().size()];
        for (int a = 0; a < getAllSpeciality().size(); a++) {
            booleanList[a] = false;
        }
        return booleanList;
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
                    Log.d(TAG, "onSuccess: " + uploadedImageUri.toString());
                    TeacherModel.Profile profile = binding.getTeacherProfile().getProfile();
                    if (!uploadedImageUri.isEmpty())
                        profile.setAadharFrontImage(uploadedImageUri.get(0));
                    if (uploadedImageUri.size() > 1)
                        profile.setAadharBackImage(uploadedImageUri.get(1));
                    teacherModel.setProfile(profile);

                }


                //Adding teaching subjects
                updateProfile();
            }

            @Override
            public void onFailed(String msg) {

                hideDialog();
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateProfile() {
        TeacherModel.TeachingProfile teachingProfile = binding.getTeacherProfile().getTeachingProfile();
        List<String> list = new ArrayList<>();
        for (Integer index : mSelectedItems) {
            list.add(getAllSpeciality().get(index));
        }
        teachingProfile.setTeachingSubject(list);

        teacherModel.setTeachingProfile(teachingProfile);
        if (teacherModel.getProfile().getVerified() == null)
            teacherModel.getProfile().setVerified(false);
        AppUtils.updateTeacherProfile(teacherModel, new ApiInterface() {
            @Override
            public void onSuccess(Object obj) {
                hideDialog();
                Toasty.success(requireActivity(), (String) obj, Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.teacherTimeSlotsFragment);
            }

            @Override
            public void onFailed(String msg) {
                hideDialog();
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}