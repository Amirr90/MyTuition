package com.mytuition.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.databinding.PopularDocViewBinding;
import com.mytuition.interfaces.AdapterInterface;
import com.mytuition.models.TeacherModel;

public class PopularTeachersAdapter extends ListAdapter<TeacherModel, PopularTeachersAdapter.PopularVH> {
    AdapterInterface adapterInterface;

    public PopularTeachersAdapter(AdapterInterface adapterInterface) {
        super(TeacherModel.itemCallback);
        this.adapterInterface = adapterInterface;
    }

    @NonNull
    @Override
    public PopularVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PopularDocViewBinding popularDocViewBinding = PopularDocViewBinding.inflate(inflater, parent, false);
        popularDocViewBinding.setRecommendedInterface(adapterInterface);
        return new PopularVH(popularDocViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularVH holder, int position) {

        TeacherModel doctorModel = getItem(position);
        holder.popularDocViewBinding.setDoctor(doctorModel);


        if (null != doctorModel.getSpeciality() && !doctorModel.getSpeciality().isEmpty())
            holder.popularDocViewBinding.textView77.setVisibility(View.VISIBLE);

        else holder.popularDocViewBinding.textView77.setVisibility(View.GONE);

    }

    public static class PopularVH extends RecyclerView.ViewHolder {
        PopularDocViewBinding popularDocViewBinding;

        public PopularVH(PopularDocViewBinding popularDocViewBinding) {
            super(popularDocViewBinding.getRoot());
            this.popularDocViewBinding = popularDocViewBinding;
        }
    }
}

