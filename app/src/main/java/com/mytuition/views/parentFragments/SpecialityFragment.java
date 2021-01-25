package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mytuition.R;
import com.mytuition.adapters.SpecialityAdapter;
import com.mytuition.databinding.FragmentSpecialityBinding;
import com.mytuition.interfaces.AdapterInterface;
import com.mytuition.interfaces.DatabaseCallbackInterface;
import com.mytuition.models.SpecialityModel;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mytuition.adapters.DashboardPatientAdapter1.SPECIALITY;
import static com.mytuition.utility.DatabaseUtils.getSubjectData;
import static com.mytuition.utility.Utils.getFirebaseReference;


public class SpecialityFragment extends Fragment implements AdapterInterface {
    private static final String TAG = "SpecialityFragment";


    FragmentSpecialityBinding specialityBinding;
    NavController navController;
    SpecialityAdapter specialityAdapter;
    public static List<String> specialitiesIds = new ArrayList<>();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        specialityBinding = FragmentSpecialityBinding.inflate(getLayoutInflater());
        getFirebaseReference(SPECIALITY).keepSynced(true);
        return specialityBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        specialityBinding.symptomsRec.setAdapter(specialityAdapter);


        specialityAdapter = new SpecialityAdapter(this);

        specialityBinding.symptomsRec.setAdapter(specialityAdapter);

        AppUtils.showRequestDialog(requireActivity());

        specialityAdapter.submitList(getClassData());


        specialityBinding.btnProceedOnSymptomPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (specialitiesIds.isEmpty()) {
                    Toast.makeText(requireActivity(), R.string.select_speciality, Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuilder ids = new StringBuilder();
                for (String id : specialitiesIds)
                    ids.append(id + ",");
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", ids.toString());
                    navController.navigate(R.id.action_specialityFragment_to_teacherListFragment, bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onItemClicked: " + e.getLocalizedMessage());
                }
            }
        });
    }

    private List<SpecialityModel> getClassData() {
        List<SpecialityModel> specialityModels = new ArrayList<>();

        specialityModels.add(new SpecialityModel("","Class 1","1",false));
        specialityModels.add(new SpecialityModel("","Class 2","2",false));
        specialityModels.add(new SpecialityModel("","Class 3","3",false));
        specialityModels.add(new SpecialityModel("","Class 4","4",false));
        specialityModels.add(new SpecialityModel("","Class 5","5",false));
        specialityModels.add(new SpecialityModel("","Class 6","6",false));
        specialityModels.add(new SpecialityModel("","Class 7","7",false));
        specialityModels.add(new SpecialityModel("","Class 8","8",false));
        specialityModels.add(new SpecialityModel("","Class 9 (UP Board)","9",false));
        specialityModels.add(new SpecialityModel("","Class 10 (UP Board)","10",false));
        specialityModels.add(new SpecialityModel("","Class 10 (ICSE Board)","11",false));
        specialityModels.add(new SpecialityModel("","Class 10 (ICSE Board)","12",false));
        specialityModels.add(new SpecialityModel("","Class 11 (UP Board)","13",false));
        specialityModels.add(new SpecialityModel("","Class 11 (UP Board)","14",false));
        specialityModels.add(new SpecialityModel("","Class 12 (ISE Board)","15",false));
        specialityModels.add(new SpecialityModel("","Class 12 (ISE Board)","16",false));
        AppUtils.hideDialog();
        return specialityModels;

    }


    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }

    @Override
    public void onItemClicked(Object o) {

        SpecialityModel specialityModel = (SpecialityModel) o;
        if (null == specialityModel)
            return;
        String id = String.valueOf(specialityModel.getId());
        if (specialitiesIds.contains(id)) {
            specialitiesIds.remove(id);
        } else specialitiesIds.add(id);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        specialitiesIds.clear();
    }
}