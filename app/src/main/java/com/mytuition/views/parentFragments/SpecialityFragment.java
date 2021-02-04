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
import static com.mytuition.utility.AppUtils.getClassData;
import static com.mytuition.utility.DatabaseUtils.getSubjectData;
import static com.mytuition.utility.Utils.getFirebaseReference;


public class SpecialityFragment extends Fragment implements AdapterInterface {
    private static final String TAG = "SpecialityFragment";


    FragmentSpecialityBinding specialityBinding;
    NavController navController;
    SpecialityAdapter specialityAdapter;
    public static List<String> specialitiesIds;

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

        specialitiesIds = new ArrayList<>();

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
        String id = String.valueOf(specialityModel.getName());
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