package com.mytuition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mytuition.databinding.FragmentTuitionListBinding;
import com.mytuition.databinding.TuitionListViewBinding;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.responseModel.TuitionDetailResponse;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;
import com.mytuition.viewHolder.ParentViewHolder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import static com.mytuition.utility.AppUtils.fadeIn;


public class TuitionListFragment extends Fragment {

    FragmentTuitionListBinding binding;
    NavController navController;
    ParentViewHolder viewModel;
    private static final String TAG = "TuitionListFragment";


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
        AppUtils.showRequestDialog(requireActivity());
        viewModel = new ViewModelProvider(requireActivity()).get(ParentViewHolder.class);
        viewModel.getTuitionList().observe(getViewLifecycleOwner(), new Observer<List<TuitionDetailResponse.Tuition>>() {
            @Override
            public void onChanged(List<TuitionDetailResponse.Tuition> tuitions) {
                AppUtils.hideDialog();
                if (null != tuitions && !tuitions.isEmpty()) {
                    List<RequestTuitionModel> tuitionListModels = tuitions.get(0).getTuitionModel();
                    binding.prescriptionRec.setAdapter(new AdapterTuitionList(tuitionListModels));
                }
            }
        });

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
            holder.binding.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(listModels.get(position));
                    try {
                        JSONObject request = new JSONObject(jsonString);
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConstant.TUITION_MODEL, request.toString());
                        navController.navigate(R.id.action_tuitionListFragment_to_DetailsFragment2, bundle);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
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

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }
}