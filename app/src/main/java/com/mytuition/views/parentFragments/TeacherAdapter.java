package com.mytuition.views.parentFragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.R;
import com.mytuition.databinding.DashBoardViewHorizontal1Binding;
import com.mytuition.models.TeacherModel;

import java.util.Random;

public class TeacherAdapter extends ListAdapter<TeacherModel, TeacherAdapter.DashboadVH2> {
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
    public DashboadVH2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DashBoardViewHorizontal1Binding dashBoardViewBinding = DashBoardViewHorizontal1Binding.inflate(inflater, parent, false);
        return new DashboadVH2(dashBoardViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboadVH2 holder, int position) {

        TeacherModel healthProductDetailsModel = getItem(position);
        holder.dashBoardViewBinding.setDashboard2(healthProductDetailsModel);

        Random rand = new Random();
        int pos = rand.nextInt(5);
        holder.dashBoardViewBinding.imageView22.setBackgroundResource(cards[pos]);

    }

    public class DashboadVH2 extends RecyclerView.ViewHolder {
        DashBoardViewHorizontal1Binding dashBoardViewBinding;

        public DashboadVH2(DashBoardViewHorizontal1Binding dashBoardViewBinding) {
            super(dashBoardViewBinding.getRoot());
            this.dashBoardViewBinding = dashBoardViewBinding;
        }
    }
}
