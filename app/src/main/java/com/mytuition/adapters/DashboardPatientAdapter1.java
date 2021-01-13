package com.mytuition.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.R;
import com.mytuition.databinding.DashBoardViewBinding;
import com.mytuition.models.DashboardModel1;
import com.mytuition.views.activity.ParentScreen;

public class DashboardPatientAdapter1 extends ListAdapter<DashboardModel1, DashboardPatientAdapter1.DashboardModelVH> {

    Integer[] images = new Integer[]{
            R.drawable.tuitor_image,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground};


    public DashboardPatientAdapter1() {
        super(DashboardModel1.itemCallback);

    }

    @NonNull
    @Override
    public DashboardModelVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DashBoardViewBinding dashBoardViewBinding = DashBoardViewBinding.inflate(inflater, parent, false);
        return new DashboardModelVH(dashBoardViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardModelVH holder, final int position) {

        DashboardModel1 dashboardModel1 = getItem(position);
        holder.dashBoardViewBinding.setDashboard1(dashboardModel1);
        holder.dashBoardViewBinding.imageView21.setImageResource(images[position]);
        holder.dashBoardViewBinding.cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0)
                    ParentScreen.getInstance().navigate(R.id.action_parentDashboardFragment2_to_specialityFragment);
                else if (position == 1)
                    ParentScreen.getInstance().navigate(R.id.action_parentDashboardFragment2_to_subjectListFragment);
                else if (position == 2)
                    ParentScreen.getInstance().navigate(R.id.action_parentDashboardFragment2_to_tuitorByClassFragment);
                else if (position == 3)
                    ParentScreen.getInstance().navigate(R.id.action_parentDashboardFragment2_to_tuitorByClassFragment);

            }
        });

        holder.dashBoardViewBinding.textView55.setText(dashboardModel1.getDescription());


    }

    public class DashboardModelVH extends RecyclerView.ViewHolder {
        DashBoardViewBinding dashBoardViewBinding;

        public DashboardModelVH(DashBoardViewBinding dashBoardViewBinding) {
            super(dashBoardViewBinding.getRoot());
            this.dashBoardViewBinding = dashBoardViewBinding;
        }
    }
}
