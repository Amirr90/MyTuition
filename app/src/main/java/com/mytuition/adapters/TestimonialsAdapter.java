package com.mytuition.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.databinding.DashBoardViewTestimonialsBinding;
import com.mytuition.models.TestimonialsModel;

import java.util.List;

public class TestimonialsAdapter extends RecyclerView.Adapter<TestimonialsAdapter.TestimonialsVH> {
    List<TestimonialsModel> models;

    public TestimonialsAdapter(List<TestimonialsModel> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public TestimonialsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DashBoardViewTestimonialsBinding binding = DashBoardViewTestimonialsBinding.inflate(inflater, parent, false);
        return new TestimonialsVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TestimonialsVH holder, int position) {
       // holder.binding.setTestimonials(models.get(position));
    }

    @Override
    public int getItemCount() {
        /*  return null == models ? 0 : models.size();*/
        return 10;
    }

    public static class TestimonialsVH extends RecyclerView.ViewHolder {
        DashBoardViewTestimonialsBinding binding;

        public TestimonialsVH(@NonNull DashBoardViewTestimonialsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
