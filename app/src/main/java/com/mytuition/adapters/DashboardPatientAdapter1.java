package com.mytuition.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mytuition.R;
import com.mytuition.databinding.DashBoardViewBinding;
import com.mytuition.models.DashboardModel1;
import com.mytuition.models.SpecialityModel;
import com.mytuition.views.activity.ParentScreen;

import java.util.HashMap;
import java.util.Map;

import static com.mytuition.utility.Utils.getTeacherModel;

public class DashboardPatientAdapter1 extends ListAdapter<DashboardModel1, DashboardPatientAdapter1.DashboardModelVH> {

    private static final String TAG = "DashboardPatientAdapter";
    public static final String SPECIALITY = "Speciality";
    public static final String TEACHERS = "Teachers";
    Integer[] images = new Integer[]{
            R.drawable.teacher,
            R.drawable.classroom,
            R.drawable.id_card,
            R.drawable.placeholder};


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
                    ParentScreen.getInstance().navigate(R.id.action_parentDashboardFragment2_to_subjectListFragment);
                else if (position == 1)
                    ParentScreen.getInstance().navigate(R.id.action_parentDashboardFragment2_to_specialityFragment);
                else if (position == 2)
                    ParentScreen.getInstance().navigate(R.id.action_parentDashboardFragment2_to_tuitorByClassFragment);
                else if (position == 3) {
                    ParentScreen.getInstance().navigate(R.id.action_parentDashboardFragment2_to_tuitorByClassFragment);
                    createSpecialityData();
                }
            }
        });

        holder.dashBoardViewBinding.textView55.setText(dashboardModel1.getDescription());


    }

    private void createSpecialityData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child(TEACHERS);
        String specialityId = String.valueOf(System.currentTimeMillis());
        myRef.child(specialityId).setValue(getTeacherModel(specialityId)).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });

        Log.d(TAG, "createSpecialityData: " + specialityId);
    }

    private Object getSpecialityMap(String specialityId) {
        SpecialityModel specialityModel = new SpecialityModel();
        specialityModel.setIcon("");
        specialityModel.setName("English");
        specialityModel.setId(specialityId);
        specialityModel.setActive(true);
        return specialityModel;
    }

    public class DashboardModelVH extends RecyclerView.ViewHolder {
        DashBoardViewBinding dashBoardViewBinding;

        public DashboardModelVH(DashBoardViewBinding dashBoardViewBinding) {
            super(dashBoardViewBinding.getRoot());
            this.dashBoardViewBinding = dashBoardViewBinding;
        }
    }
}
