package com.mytuition;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.Query;
import com.mytuition.adapters.CategoryViewHolder;
import com.mytuition.databinding.FragmentMyTuitionsBinding;
import com.mytuition.databinding.MyTuitionViewBinding;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;
import com.mytuition.views.activity.TeacherScreen;

import org.jetbrains.annotations.NotNull;

import static com.mytuition.utility.AppUtils.getJSONFromModel;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.AppUtils.hideDialog;


public class MyTuitionsFragment extends Fragment {
    private static final String TAG = "MyTuitionsFragment";


    FragmentMyTuitionsBinding binding;
    NavController navController;
    FirestorePagingAdapter adapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyTuitionsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        setUpRecData();
    }

    private void setUpRecData() {
        AppUtils.showRequestDialog(requireActivity());
        Query query = AppUtils.getFirestoreReference().collection(AppConstant.REQUEST_TUITION)
                .whereEqualTo("teacherId", getUid())
                .orderBy(AppConstant.TIMESTAMP, Query.Direction.DESCENDING);


        query.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<RequestTuitionModel> options1 = new FirestorePagingOptions.Builder<RequestTuitionModel>()
                .setLifecycleOwner(requireActivity())
                .setQuery(query, config, snapshot -> {
                    RequestTuitionModel requestTuitionModel = snapshot.toObject(RequestTuitionModel.class);
                    Log.d(TAG, "setUpRecData: " + snapshot.getData().toString());
                    requestTuitionModel.setId(snapshot.getId());
                    return requestTuitionModel;
                }).build();

        adapter = new FirestorePagingAdapter<RequestTuitionModel, CategoryViewHolder>(options1) {
            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                MyTuitionViewBinding binding = MyTuitionViewBinding.inflate(inflater, parent, false);
                return new CategoryViewHolder(binding);
            }


            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final RequestTuitionModel model) {
                holder.myTuitionViewBinding.setTuitionModel(model);
                holder.myTuitionViewBinding.button2.setEnabled(model.getActive());
                holder.myTuitionViewBinding.button2.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("RequestTuitionModel", getJSONFromModel(model));
                    navController.navigate(R.id.action_teacherDashboardFragment_to_acceptTuitionBootomFragment, bundle);
                });
            }


            @Override
            protected void onError(@NonNull Exception e) {
                super.onError(e);
                hideDialog();
                Log.d(TAG, "onError: " + e.getLocalizedMessage());
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state) {
                    case ERROR: {
                        hideDialog();
                        if (binding.swipeMyTuitions.isRefreshing())
                            binding.swipeMyTuitions.setRefreshing(false);
                        Log.d(TAG, "onLoadingStateChanged: error ");
                        Toast.makeText(requireActivity(), "failed to get Data !!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case FINISHED: {
                        hideDialog();
                        if (binding.swipeMyTuitions.isRefreshing())
                            binding.swipeMyTuitions.setRefreshing(false);
                        Log.d(TAG, "onLoadingStateChanged: FINISHED");
                    }
                    break;
                    case LOADED: {
                        hideDialog();
                        if (binding.swipeMyTuitions.isRefreshing())
                            binding.swipeMyTuitions.setRefreshing(false);
                        Log.d(TAG, "onLoadingStateChanged: LOADED " + getItemCount());
                    }
                    case LOADING_MORE: {
                        Log.d(TAG, "onLoadingStateChanged: LOADING_MORE");
                    }
                    case LOADING_INITIAL: {
                        hideDialog();
                        if (binding.swipeMyTuitions.isRefreshing())
                            binding.swipeMyTuitions.setRefreshing(false);
                        Log.d(TAG, "onLoadingStateChanged: LOADING_INITIAL");

                    }
                    break;
                }
            }
        };
        binding.recMyTuitions.setHasFixedSize(true);
        binding.recMyTuitions.setAdapter(adapter);
        TeacherScreen.getInstance().setBadge(adapter.getItemCount());
    }
}