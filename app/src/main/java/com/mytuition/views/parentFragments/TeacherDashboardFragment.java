package com.mytuition.views.parentFragments;

import android.app.Activity;
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

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.firestore.Query;
import com.mytuition.R;
import com.mytuition.adapters.CategoryViewHolder;
import com.mytuition.databinding.FragmentTeacherDashboardBinding;
import com.mytuition.databinding.TuitionViewBinding;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.App;
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
    Query query;
    AdRequest adRequest;
    RewardedAd mRewardedAd;


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
        query = AppUtils.getFirestoreReference().collection(AppConstant.REQUEST_TUITION);
        checkForProfileComplete();

        initAd();
        binding.swipeDashboard.setOnRefreshListener(() -> {
            filterData(binding.radioGroup2.getCheckedRadioButtonId());
        });


        binding.radioGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            filterData(checkedId);

        });
    }

    private void initAd() {
        adRequest = new AdRequest.Builder().build();

        RewardedAd.load(requireActivity(), "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });

    }


    private void filterData(int checkedId) {
        if (checkedId == R.id.radioAll)
            setUpRecData(query);
        else if (checkedId == R.id.radioHired)
            setUpRecData(query.whereEqualTo("reqStatus", AppConstant.REQUEST_STATUS_ACCEPTED));
        else if (checkedId == R.id.radioAvailable)
            setUpRecData(query.whereEqualTo("reqStatus", AppConstant.REQUEST_STATUS_PENDING));
    }

    private void checkForProfileComplete() {
        AppUtils.showRequestDialog(requireActivity());
        TeacherProfile.getProfile(new ApiInterface() {
            @Override
            public void onSuccess(Object obj) {
                hideDialog();
                TeacherModel teacherModel = (TeacherModel) obj;
                AppUtils.setString("name", teacherModel.getName(), requireActivity());
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
                        setUpRecData(query);
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

    private void setUpRecData(Query query) {
        query.orderBy(AppConstant.TIMESTAMP, Query.Direction.DESCENDING);
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
                return new CategoryViewHolder(binding);
            }


            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final RequestTuitionModel model) {
                holder.tuitionViewBinding.setTuitionModel(model);
                try {
                    holder.tuitionViewBinding.button2.setEnabled(model.getActive());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                holder.tuitionViewBinding.button2.setOnClickListener(view -> {
                    showInterstialAd(model);

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

    private void showInterstialAd(RequestTuitionModel model) {

        if (mRewardedAd != null) {
            Activity activityContext = requireActivity();
            mRewardedAd.show(activityContext, rewardItem -> {
                Log.d(TAG, "The user earned the reward.");
                int rewardAmount = rewardItem.getAmount();
                String rewardType = rewardItem.getType();
                Log.d(TAG, "onUserEarnedReward: " + rewardAmount + "    " + rewardType);
                proceedToNextPage(model);
                initAd();
            });
        } else {
            proceedToNextPage(model);
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }

        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.d(TAG, "Ad failed to show.");
                proceedToNextPage(model);
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad was dismissed.");
                mRewardedAd = null;
                Toast.makeText(App.context, "View Full Ad to accept tuition !!", Toast.LENGTH_SHORT).show();
                initAd();
            }


        });

    }

    private void proceedToNextPage(RequestTuitionModel model) {
        Bundle bundle = new Bundle();
        bundle.putString("RequestTuitionModel", getJSONFromModel(model));
        navController.navigate(R.id.action_teacherDashboardFragment_to_acceptTuitionBootomFragment, bundle);
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