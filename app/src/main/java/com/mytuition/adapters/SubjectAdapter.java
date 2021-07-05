package com.mytuition.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.R;
import com.mytuition.databinding.SpecialitiyViewBinding;
import com.mytuition.models.SpecialityModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.views.activity.ParentScreen;

import static com.mytuition.adapters.DashboardPatientAdapter1.SPECIALITY;

public class SubjectAdapter extends ListAdapter<SpecialityModel, SubjectAdapter.SpecialityVH> {
    public static final String ID = "id";
    private static final String TAG = "SpecialityAdapter";
    Activity activity;

    public SubjectAdapter(Activity activity) {
        super(SpecialityModel.itemCallback);
        this.activity = activity;
    }

    @NonNull
    @Override
    public SpecialityVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SpecialitiyViewBinding specialitiyViewBinding = SpecialitiyViewBinding.inflate(inflater, parent, false);
        return new SpecialityVH(specialitiyViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialityVH holder, int position) {

        final SpecialityModel subjectModel = getItem(position);
        holder.specialitiyViewBinding.setSubject(subjectModel);


        holder.specialitiyViewBinding.llspeality.setOnClickListener(v -> {
            // AddNewSpeciality(subjectModel);
            AppUtils.hideSoftKeyboard(activity);
            Bundle bundle = new Bundle();
            bundle.putString(ID, String.valueOf(subjectModel.getId()));
            bundle.putString(SPECIALITY, subjectModel.getName());
            ParentScreen.getInstance().navigate(R.id.action_subjectListFragment_to_teachersListBySubjectFragment, bundle);
        });


    }

    private void AddNewSpeciality(SpecialityModel subjectModel) {
        AppUtils.getFirestoreReference().collection("Speciality").add(subjectModel);
    }

    public static class SpecialityVH extends RecyclerView.ViewHolder {
        SpecialitiyViewBinding specialitiyViewBinding;

        public SpecialityVH(SpecialitiyViewBinding specialitiyViewBinding) {
            super(specialitiyViewBinding.getRoot());
            this.specialitiyViewBinding = specialitiyViewBinding;
        }
    }


}

