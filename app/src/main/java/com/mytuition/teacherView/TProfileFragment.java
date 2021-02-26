package com.mytuition.teacherView;

import android.content.DialogInterface;
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

import com.google.firebase.auth.FirebaseAuth;
import com.mytuition.R;
import com.mytuition.databinding.FragmentTProfileBinding;
import com.mytuition.models.TeacherModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.mytuition.utility.AppUtils.getAllSpeciality;
import static com.mytuition.utility.AppUtils.showToolbar;


public class TProfileFragment extends Fragment {
    private static final String TAG = "TProfileFragment";

    FragmentTProfileBinding binding;
    NavController navController;
    TeacherModel teacherModel;
    List<Integer> mSelectedItems;

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
            }
        });

        setSpinnerData();
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
        // set the dialog title
        builder.setTitle("Choose One or More")
                .setMultiChoiceItems(choices, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // if the user checked the item, add it to the selected items
                            mSelectedItems.add(which);
                        } else if (mSelectedItems.contains(which)) {
                            // else if the item is already in the array, remove it
                            mSelectedItems.remove(Integer.valueOf(which));
                        }

                        // you can also add other codes here,
                        // for example a tool tip that gives user an idea of what he is selecting
                        // showToast("Just an example description.");
                    }

                })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        // user clicked OK, so save the mSelectedItems results somewhere
                        // here we are trying to retrieve the selected items indices
                        String selectedIndex = "";
                        for (Integer i : mSelectedItems) {
                            selectedIndex += choices[i] + ", ";
                        }

                        String msg = ("Selected index: " + selectedIndex);

                        binding.tvSelectedSubjects.setText(selectedIndex);
                        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the AlertDialog in the screen
                    }
                })

                .show();
    }


    @Override
    public void onResume() {
        super.onResume();
        showToolbar(requireActivity());
    }
}