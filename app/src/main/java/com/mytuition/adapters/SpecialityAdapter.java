package com.mytuition.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.databinding.SymptomsViewBinding;
import com.mytuition.interfaces.AdapterInterface;
import com.mytuition.models.SpecialityModel;

import static com.mytuition.views.parentFragments.SpecialityFragment.specialitiesIds;

public class SpecialityAdapter extends ListAdapter<SpecialityModel, SpecialityAdapter.SymptomVH> {
    AdapterInterface adapterInterface;

    public SpecialityAdapter(AdapterInterface adapterInterface) {
        super(SpecialityModel.itemCallback);
        this.adapterInterface = adapterInterface;
    }


    @NonNull
    @Override
    public SymptomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SymptomsViewBinding symptomsViewBinding = SymptomsViewBinding.inflate(inflater, parent, false);
        symptomsViewBinding.setAdapterInterface(adapterInterface);
        return new SymptomVH(symptomsViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SymptomVH holder, int position) {
        SpecialityModel symptomModel = getItem(position);
        holder.symptomsViewBinding.setSymptoms(symptomModel);
        if (null != specialitiesIds)
            holder.symptomsViewBinding.radioButton5.setChecked(specialitiesIds.contains(String.valueOf(symptomModel.getId())));

    }

    public class SymptomVH extends RecyclerView.ViewHolder {
        SymptomsViewBinding symptomsViewBinding;

        public SymptomVH(SymptomsViewBinding symptomsViewBinding) {
            super(symptomsViewBinding.getRoot());
            this.symptomsViewBinding = symptomsViewBinding;
        }
    }
}
