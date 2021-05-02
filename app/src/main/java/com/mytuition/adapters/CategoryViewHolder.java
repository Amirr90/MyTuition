package com.mytuition.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.databinding.TuitionListViewBinding;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public TuitionListViewBinding binding;

    public CategoryViewHolder(@NonNull TuitionListViewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
