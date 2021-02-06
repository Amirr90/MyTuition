package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.R;
import com.mytuition.databinding.DashBoardViewHorizontal1Binding;
import com.mytuition.models.TeacherModel;

import java.util.Random;

public class TeacherAdapter extends ListAdapter<TeacherModel, TeacherAdapter.DashBoardVH2> {
    Integer[] cards = new Integer[]{R.drawable.box_one,
            R.drawable.box_two,
            R.drawable.box_three,
            R.drawable.box_four,
            R.drawable.box_five,
            R.drawable.box_six};

    public TeacherAdapter() {
        super(TeacherModel.itemCallback);
    }

    @NonNull
    @Override
    public DashBoardVH2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DashBoardViewHorizontal1Binding dashBoardViewBinding = DashBoardViewHorizontal1Binding.inflate(inflater, parent, false);
        return new DashBoardVH2(dashBoardViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DashBoardVH2 holder, int position) {

        final TeacherModel teacherModel = getItem(position);
        holder.dashBoardViewBinding.setDashboard2(teacherModel);

        Random rand = new Random();
/*        int pos = rand.nextInt(5);
        holder.dashBoardViewBinding.imageView22.setBackgroundResource(cards[pos]);*/
        holder.dashBoardViewBinding.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("docModel", teacherModel.toString());
                ParentDashboardFragment.getInstance().navController.navigate(R.id.action_parentDashboardFragment2_to_teacherProfileFragment, bundle);

            }
        });

    }

    public class DashBoardVH2 extends RecyclerView.ViewHolder {
        DashBoardViewHorizontal1Binding dashBoardViewBinding;

        public DashBoardVH2(DashBoardViewHorizontal1Binding dashBoardViewBinding) {
            super(dashBoardViewBinding.getRoot());
            this.dashBoardViewBinding = dashBoardViewBinding;
        }
    }
}
