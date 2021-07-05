package com.mytuition.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.databinding.UserListViewBinding;

public class TeacherViewHolder extends RecyclerView.ViewHolder {

    public UserListViewBinding binding;

    public TeacherViewHolder(@NonNull UserListViewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
