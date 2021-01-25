package com.mytuition.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mytuition.R;
import com.mytuition.databinding.SubSpecialityViewBinding;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.Utils;
import com.mytuition.views.activity.ParentScreen;

import org.json.JSONException;
import org.json.JSONObject;

public class SubSpecialityAdapter extends ListAdapter<TeacherModel, SubSpecialityAdapter.SubSpecialityVH> {
    private static final String TAG = "SubSpecialityAdapter";
    SubSpecialityInterface subSpecialityInterface;

    public SubSpecialityAdapter(SubSpecialityInterface subSpecialityInterface) {
        super(TeacherModel.itemCallback);
        this.subSpecialityInterface = subSpecialityInterface;
    }

    @NonNull
    @Override
    public SubSpecialityVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SubSpecialityViewBinding subSpecialityViewBinding = SubSpecialityViewBinding.inflate(inflater, parent, false);
        return new SubSpecialityVH(subSpecialityViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubSpecialityVH holder, int position) {

        final TeacherModel teacherModel = getItem(position);
        holder.subSpecialityViewBinding.setTeacher(teacherModel);

        holder.subSpecialityViewBinding.tvFreeDemoTag.setVisibility(teacherModel.isDemoClassFree() ? View.VISIBLE : View.GONE);
        holder.subSpecialityViewBinding.btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(teacherModel);
                Log.d(TAG, "onClickTeacherModel: " + teacherModel.toString());
                Log.d(TAG, "onClickTeacher: " + jsonString.toString());
                try {
                    JSONObject request = new JSONObject(jsonString);
                    subSpecialityInterface.onItemClick(request.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        holder.subSpecialityViewBinding.mailLAyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("docModel", teacherModel.toString());
                ParentScreen.getInstance().navigate(R.id.action_teachersListBySubjectFragment_to_teacherProfileFragment, bundle);
                //addNewTeacher(teacherModel);
            }
        });

    }

    private void addNewTeacher(TeacherModel teacherModel) {
        Utils.getFirebaseReference("Teachers").child(String.valueOf(System.currentTimeMillis())).setValue(teacherModel);
    }

    public class SubSpecialityVH extends RecyclerView.ViewHolder {
        SubSpecialityViewBinding subSpecialityViewBinding;

        public SubSpecialityVH(SubSpecialityViewBinding subSpecialityViewBinding) {
            super(subSpecialityViewBinding.getRoot());
            this.subSpecialityViewBinding = subSpecialityViewBinding;
        }
    }

    public interface SubSpecialityInterface {
        void onItemClick(String item);
    }
}
