package com.mytuition.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.R;
import com.mytuition.databinding.SpecialitiyViewBinding;
import com.mytuition.models.SubjectModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.views.activity.ParentScreen;

public class SubjectAdapter extends ListAdapter<SubjectModel, SubjectAdapter.SpecialityVH> {
    private static final String TAG = "SpecialityAdapter";

    Activity activity;

    public SubjectAdapter(Activity activity) {
        super(SubjectModel.itemCallback);
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

        final SubjectModel subjectModel = getItem(position);
        holder.specialitiyViewBinding.setSubject(subjectModel);


        holder.specialitiyViewBinding.llspeality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideSoftKeyboard(activity);
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(subjectModel.getId()));
                ParentScreen.getInstance().navigate(R.id.action_subjectListFragment_to_teacherListFragment, bundle);
            }
        });


    }

    public class SpecialityVH extends RecyclerView.ViewHolder {
        SpecialitiyViewBinding specialitiyViewBinding;

        public SpecialityVH(SpecialitiyViewBinding specialitiyViewBinding) {
            super(specialitiyViewBinding.getRoot());
            this.specialitiyViewBinding = specialitiyViewBinding;
        }
    }


}

