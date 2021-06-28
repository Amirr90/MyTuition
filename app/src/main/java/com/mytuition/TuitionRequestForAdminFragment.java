package com.mytuition;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mytuition.adapters.CategoryViewHolder;
import com.mytuition.databinding.FragmentTuitionRequestForAdminBinding;
import com.mytuition.databinding.TuitionListViewBinding;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.mytuition.utility.AppUtils.hideDialog;


public class TuitionRequestForAdminFragment extends Fragment {
    private static final String TAG = "TuitionRequestForAdminF";

    FragmentTuitionRequestForAdminBinding binding;
    NavController navController;
    FirestorePagingAdapter adapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTuitionRequestForAdminBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTuitionData();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }

    private void setTuitionData() {


        AppUtils.showRequestDialog(requireActivity());
        Query query = AppUtils.getFirestoreReference().collection(AppConstant.REQUEST_TUITION)
                .orderBy(AppConstant.TIMESTAMP, Query.Direction.DESCENDING);


        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<RequestTuitionModel> options1 = new FirestorePagingOptions.Builder<RequestTuitionModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, snapshot -> {
                    RequestTuitionModel model = snapshot.toObject(RequestTuitionModel.class);
                    Objects.requireNonNull(model).setId(snapshot.getId());
                    Log.d(TAG, "parseSnapshot: ");
                    return model;
                }).build();

        adapter = new FirestorePagingAdapter<RequestTuitionModel, CategoryViewHolder>(options1) {
            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                TuitionListViewBinding binding = TuitionListViewBinding.inflate(inflater, parent, false);
                return new CategoryViewHolder(binding);
            }


            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final RequestTuitionModel model) {
                holder.binding.setTuition(model);
                String status = model.getReqStatus();
                holder.binding.profileImage.setBorderWidth(2);

                switch (status) {
                    case AppConstant.REQUEST_STATUS_PENDING:
                    case AppConstant.REQUEST_STATUS_PENDING_S:
                        holder.binding.llImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow2)));
                        break;
                    case AppConstant.REQUEST_STATUS_REJECTED:
                    case AppConstant.REQUEST_STATUS_REJECTED_S:
                        holder.binding.llImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
                        break;

                    case AppConstant.REQUEST_STATUS_ACCEPTED:
                    case AppConstant.REQUEST_STATUS_ACCEPTED_S:
                        holder.binding.llImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        break;
                    case AppConstant.REQUEST_STATUS_CANCELLED:
                    case AppConstant.REQUEST_STATUS_CANCELLED_S:
                        holder.binding.llImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red700)));
                        break;
                }
                holder.binding.root.setOnClickListener(v -> {
                    TuitionRequestForAdminFragmentDirections.ActionTuitionRequestForAdminFragmentToSingleTuitionDetailFragment action = TuitionRequestForAdminFragmentDirections.actionTuitionRequestForAdminFragmentToSingleTuitionDetailFragment();
                    action.setTuitionId(model.getId());
                    navController.navigate(action);
                });
            }


            @Override
            protected void onError(@NonNull Exception e) {
                super.onError(e);
                AppUtils.hideDialog();
                Log.d(TAG, "onError: " + e.getLocalizedMessage());
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state) {
                    case ERROR: {
                        hideDialog();
                        Log.d(TAG, "onLoadingStateChanged: error ");
                        Toast.makeText(requireActivity(), "failed to get Data !!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case FINISHED: {
                        hideDialog();
                        Log.d(TAG, "onLoadingStateChanged: FINISHED");
                        Toast.makeText(requireActivity(), "No more data !!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case LOADED: {
                        hideDialog();
                        Log.d(TAG, "onLoadingStateChanged: LOADED " + getItemCount());
                    }
                    case LOADING_MORE: {
                        Log.d(TAG, "onLoadingStateChanged: LOADING_MORE");
                    }
                    case LOADING_INITIAL: {
                        hideDialog();
                        Log.d(TAG, "onLoadingStateChanged: LOADING_INITIAL");

                    }
                    break;
                }
            }
        };

        binding.recTuitionList.setHasFixedSize(true);
        binding.recTuitionList.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != adapter)
            adapter.stopListening();
    }
}