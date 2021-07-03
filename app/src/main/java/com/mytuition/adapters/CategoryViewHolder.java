package com.mytuition.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.databinding.TuitionListViewBinding;
import com.mytuition.databinding.TuitionViewBinding;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public TuitionListViewBinding binding;
    public TuitionViewBinding tuitionViewBinding;

    public CategoryViewHolder(@NonNull TuitionListViewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public CategoryViewHolder(@NonNull TuitionViewBinding tuitionViewBinding) {
        super(tuitionViewBinding.getRoot());
        this.tuitionViewBinding = tuitionViewBinding;
    }
}
