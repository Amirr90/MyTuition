package com.mytuition.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.databinding.DashBoardViewHorizontal2Binding;
import com.mytuition.models.CoachingModel;

public class CoachingAdapter extends ListAdapter<CoachingModel, CoachingAdapter.DashboardVH> {
    public CoachingAdapter() {
        super(CoachingModel.itemCallback);
    }

    @NonNull
    @Override
    public DashboardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DashBoardViewHorizontal2Binding dashBoardViewBinding = DashBoardViewHorizontal2Binding.inflate(inflater, parent, false);
        return new DashboardVH(dashBoardViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardVH holder, int position) {

        CoachingModel topClinicsModel = getItem(position);
        holder.dashBoardViewBinding.setClinics(topClinicsModel);

    }

    public static class DashboardVH extends RecyclerView.ViewHolder {
        DashBoardViewHorizontal2Binding dashBoardViewBinding;


        public DashboardVH(DashBoardViewHorizontal2Binding dashBoardViewBinding) {
            super(dashBoardViewBinding.getRoot());
            this.dashBoardViewBinding = dashBoardViewBinding;
        }
    }
}
