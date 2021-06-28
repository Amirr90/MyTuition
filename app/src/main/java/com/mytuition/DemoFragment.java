package com.mytuition;

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

import com.mytuition.databinding.FragmentDemoBinding;
import com.mytuition.models.TeacherModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.mytuition.utility.AppUtils.getAllSpeciality;
import static com.mytuition.utility.Utils.getCityList;
import static com.mytuition.utility.Utils.getStateList;

public class DemoFragment extends Fragment {
    private static final String TAG = "DemoFragment";

    FragmentDemoBinding binding;
    List<Integer> mSelectedItems;
    String higherEducation, frontImageUrl, backImageUrl;
    TeacherModel teacherModel;

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

        teacherModel = new TeacherModel();
       /* if (null != FirebaseAuth.getInstance().getCurrentUser())
            teacherModel.setMobile(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());*/
        binding.setTeacherProfile(teacherModel);

        settingDropDownAdapters();


        binding.btnSubmit.setOnClickListener(v -> {
            Log.d(TAG, "onSubmitBtn Clicked !! : " + binding.getTeacherProfile().toString());
        });

    }

    private void settingDropDownAdapters() {
        //city Adapter

        Log.d(TAG, "settingDropDownAdapters: " + teacherModel.toString());
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


        binding.tvSelectedSubjectsLayout.setOnClickListener(view -> {
            showAllSubjectDialog();

        });


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
        mSelectedItems = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        final String[] choices = new String[getAllSpeciality().size()];
        for (int a = 0; a < getAllSpeciality().size(); a++)
            choices[a] = getAllSpeciality().get(a);
        builder.setTitle("Choose One or More")
                .setMultiChoiceItems(choices, null, (dialog, which, isChecked) -> {
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
}