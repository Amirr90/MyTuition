package com.mytuition;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.databinding.FragmentTuitionListBinding;
import com.mytuition.databinding.TuitionListViewBinding;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.utility.AppConstant;
import com.mytuition.viewHolder.ParentViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static com.mytuition.utility.AppUtils.fadeIn;


public class TuitionListFragment extends Fragment {

    FragmentTuitionListBinding binding;
    NavController navController;
    ParentViewHolder viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTuitionListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        binding.getRoot().setAnimation(fadeIn(requireActivity()));
        viewModel = new ViewModelProvider(requireActivity()).get(ParentViewHolder.class);
        getData();

        binding.btnRetry.setOnClickListener(v -> {
            binding.noDataLayout.setVisibility(View.GONE);
            getData();
        });

    }

    private void getData() {
        viewModel.getTuitionList(requireActivity()).observe(getViewLifecycleOwner(), tuitions -> {
            if (null != tuitions && !tuitions.isEmpty()) {
                binding.noDataLayout.setVisibility(View.GONE);
                binding.prescriptionRec.setVisibility(View.VISIBLE);
                List<RequestTuitionModel> tuitionListModels = tuitions.get(0).getTuitionModel();
                binding.prescriptionRec.setAdapter(new AdapterTuitionList(tuitionListModels));
            } else {
                binding.noDataLayout.setVisibility(View.VISIBLE);
                binding.prescriptionRec.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }

    private class AdapterTuitionList extends RecyclerView.Adapter<AdapterTuitionList.TuitionVH> {
        List<RequestTuitionModel> listModels;

        public AdapterTuitionList(List<RequestTuitionModel> listModels) {
            this.listModels = listModels;
        }

        @NonNull
        @Override
        public AdapterTuitionList.TuitionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            TuitionListViewBinding binding = TuitionListViewBinding.inflate(inflater, parent, false);
            return new TuitionVH(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterTuitionList.TuitionVH holder, final int position) {
            holder.binding.setTuition(listModels.get(position));

            String status = listModels.get(position).getReqStatus();
            switch (status) {
                case AppConstant.REQUEST_STATUS_PENDING:
                    holder.binding.llImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow2)));
                    break;
                case AppConstant.REQUEST_STATUS_REJECTED:
                    holder.binding.llImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
                    break;

                case AppConstant.REQUEST_STATUS_ACCEPTED:
                    holder.binding.llImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    break;
                case AppConstant.REQUEST_STATUS_CANCELLED:
                    holder.binding.llImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red700)));
                    break;
            }

            holder.binding.root.setOnClickListener(v -> {

                TuitionListFragmentDirections.ActionTuitionListFragmentToDetailsFragment2 action = TuitionListFragmentDirections.actionTuitionListFragmentToDetailsFragment2();
                action.setTuitionId(listModels.get(position).getId());
                navController.navigate(action);


            });
        }

        @Override
        public int getItemCount() {
            return null == listModels ? 0 : listModels.size();
        }

        public class TuitionVH extends RecyclerView.ViewHolder {
            TuitionListViewBinding binding;

            public TuitionVH(@NonNull TuitionListViewBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}