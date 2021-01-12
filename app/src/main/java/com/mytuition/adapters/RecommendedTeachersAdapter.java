package com.mytuition.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.databinding.DocViewBinding;
import com.mytuition.interfaces.AdapterInterface;
import com.mytuition.models.TeacherModel;

public class RecommendedTeachersAdapter extends ListAdapter<TeacherModel, RecommendedTeachersAdapter.RecommendedVH> {
    AdapterInterface adapterInterface;

    public RecommendedTeachersAdapter(AdapterInterface adapterInterface) {
        super(TeacherModel.itemCallback);
        this.adapterInterface = adapterInterface;
    }


    @NonNull
    @Override
    public RecommendedVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DocViewBinding docViewBinding = DocViewBinding.inflate(inflater, parent, false);
        docViewBinding.setRecommendedInterface(adapterInterface);
        return new RecommendedVH(docViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedVH holder, int position) {

        TeacherModel doctorModel = getItem(position);

        holder.docViewBinding.setDoc(doctorModel);
    }

    public class RecommendedVH extends RecyclerView.ViewHolder {
        DocViewBinding docViewBinding;

        public RecommendedVH(DocViewBinding docViewBinding) {
            super(docViewBinding.getRoot());
            this.docViewBinding = docViewBinding;
        }
    }
}

