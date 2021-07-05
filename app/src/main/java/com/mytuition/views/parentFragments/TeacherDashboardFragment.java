package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.Query;
import com.mytuition.R;
import com.mytuition.adapters.CategoryViewHolder;
import com.mytuition.databinding.FragmentTeacherDashboardBinding;
import com.mytuition.databinding.TuitionViewBinding;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.TeacherProfile;
import com.mytuition.views.activity.TeacherScreen;

import org.jetbrains.annotations.NotNull;

import dagger.android.support.DaggerFragment;

import static com.mytuition.utility.AppUtils.getJSONFromModel;
import static com.mytuition.utility.AppUtils.hideDialog;


public class TeacherDashboardFragment extends DaggerFragment {
    private static final String TAG = "TeacherDashboardFragmen";


    FragmentTeacherDashboardBinding binding;
    NavController navController;
    FirestorePagingAdapter adapter;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeacherDashboardBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        checkForProfileComplete();

        binding.swipeDashboard.setOnRefreshListener(() -> setUpRecData());
    }

    private void checkForProfileComplete() {
        AppUtils.showRequestDialog(requireActivity());
        TeacherProfile.getProfile(new ApiInterface() {
            @Override
            public void onSuccess(Object obj) {
                hideDialog();
                TeacherModel teacherModel = (TeacherModel) obj;
                if (null != teacherModel) {

                    Log.d(TAG, "onSuccess: " + teacherModel.toString());
                    if (!teacherModel.isProfileFilled()) {
                        Toast.makeText(requireActivity(), "incomplete profile !!", Toast.LENGTH_SHORT).show();
                        String model = getJSONFromModel(teacherModel);
                        Bundle bundle = new Bundle();
                        bundle.putString("teacherModel", model);
                        navController.navigate(R.id.action_teacherDashboardFragment_to_demoFragment, bundle);
                    } else if (!teacherModel.isVerified())
                        navController.navigate(R.id.action_teacherDashboardFragment_to_userNotVerifiedFragment);
                    else {
                        setUpRecData();
                    }
                }

            }

            @Override
            public void onFailed(String msg) {
                hideDialog();
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.action_teacherDashboardFragment_to_demoFragment);
            }
        });
    }

    private void setUpRecData() {


        Log.d(TAG, "setUpRecData: ");
        /* AppUtils.showRequestDialog(requireActivity());*/
        Query query = AppUtils.getFirestoreReference().collection(AppConstant.REQUEST_TUITION)
                .orderBy(AppConstant.TIMESTAMP, Query.Direction.DESCENDING);


        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<RequestTuitionModel> options1 = new FirestorePagingOptions.Builder<RequestTuitionModel>()
                .setLifecycleOwner(requireActivity())
                .setQuery(query, config, snapshot -> {
                    RequestTuitionModel requestTuitionModel = snapshot.toObject(RequestTuitionModel.class);
                    requestTuitionModel.setId(snapshot.getId());
                    return requestTuitionModel;
                }).build();

        adapter = new FirestorePagingAdapter<RequestTuitionModel, CategoryViewHolder>(options1) {
            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                TuitionViewBinding binding = TuitionViewBinding.inflate(inflater, parent, false);
                Log.d(TAG, "onCreateViewHolder: ");
                return new CategoryViewHolder(binding);
            }


            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final RequestTuitionModel model) {
                holder.tuitionViewBinding.setTuitionModel(model);
                holder.tuitionViewBinding.button2.setEnabled(model.isActive());

                holder.tuitionViewBinding.button2.setOnClickListener(view -> {

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
                        if (binding.swipeDashboard.isRefreshing())
                            binding.swipeDashboard.setRefreshing(false);
                        Log.d(TAG, "onLoadingStateChanged: error ");
                        Toast.makeText(requireActivity(), "failed to get Data !!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case FINISHED: {
                        hideDialog();
                        if (binding.swipeDashboard.isRefreshing())
                            binding.swipeDashboard.setRefreshing(false);
                        Log.d(TAG, "onLoadingStateChanged: FINISHED");
                    }
                    break;
                    case LOADED: {
                        hideDialog();
                        if (binding.swipeDashboard.isRefreshing())
                            binding.swipeDashboard.setRefreshing(false);
                        Log.d(TAG, "onLoadingStateChanged: LOADED " + getItemCount());
                    }
                    case LOADING_MORE: {
                        Log.d(TAG, "onLoadingStateChanged: LOADING_MORE");
                    }
                    case LOADING_INITIAL: {
                        hideDialog();
                        if (binding.swipeDashboard.isRefreshing())
                            binding.swipeDashboard.setRefreshing(false);
                        Log.d(TAG, "onLoadingStateChanged: LOADING_INITIAL");

                    }
                    break;
                }
            }
        };
        binding.dashboardRec.setHasFixedSize(true);
        binding.dashboardRec.setAdapter(adapter);
        TeacherScreen.getInstance().setBadge(adapter.getItemCount());
    }

    @Override
    public void onStart() {
        super.onStart();
        AppUtils.showToolbar(requireActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != adapter) {
            adapter.retry();
            Log.d(TAG, "onResume: refreshed !!");
        }
    }
}