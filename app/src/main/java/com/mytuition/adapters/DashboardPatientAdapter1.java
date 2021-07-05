package com.mytuition.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mytuition.R;
import com.mytuition.databinding.DashBoardViewBinding;
import com.mytuition.models.DashboardModel1;
import com.mytuition.models.SpecialityModel;
import com.mytuition.views.activity.ParentScreen;

import static com.mytuition.utility.Utils.getTeacherModel;

public class DashboardPatientAdapter1 extends ListAdapter<DashboardModel1, DashboardPatientAdapter1.DashboardModelVH> {

    public static final String SPECIALITY = "Speciality";
    public static final String TEACHERS = "Teachers";
    private static final String TAG = "DashboardPatientAdapter";
    RewardedAd mRewardedAd;
    Integer[] images = new Integer[]{
            R.drawable.teacher,
            R.drawable.classroom,
            R.drawable.id_card,
            R.drawable.placeholder};


    public DashboardPatientAdapter1() {
        super(DashboardModel1.itemCallback);

    }

    @NonNull
    @Override
    public DashboardModelVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DashBoardViewBinding dashBoardViewBinding = DashBoardViewBinding.inflate(inflater, parent, false);
        return new DashboardModelVH(dashBoardViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardModelVH holder, final int position) {

        DashboardModel1 dashboardModel1 = getItem(position);
        holder.dashBoardViewBinding.setDashboard1(dashboardModel1);
        holder.dashBoardViewBinding.imageView21.setImageResource(images[position]);
        holder.dashBoardViewBinding.cv1.setOnClickListener(v -> setRewardAdd(position));

        holder.dashBoardViewBinding.textView55.setText(dashboardModel1.getDescription());


    }


    private void setRewardAdd(final int position) {

    /*    RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("EAE61BEA656088A6BE28C25EDB1A5A2F")).build();
        MobileAds.setRequestConfiguration(configuration);*/

        AdRequest adRequest = new AdRequest.Builder().build();
        Log.d(TAG, "setRewardAdd: Mode " + adRequest.isTestDevice(ParentScreen.getInstance()));
        RewardedAd.load(ParentScreen.getInstance(), "ca-app-pub-5778282166425967/9033553154",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;

                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "onAdFailedToLoad " + rewardedAd.getRewardItem());
                    }
                });

        if (null != mRewardedAd) {
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad was shown.");
                    mRewardedAd = null;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {

                    initNav(position);
                }

                @Override
                public void onAdDismissedFullScreenContent() {

                    Log.d(TAG, "Ad was dismissed.");
                    initNav(position);
                }
            });
        }


        if (mRewardedAd != null) {
            Activity activityContext = ParentScreen.getInstance();
            mRewardedAd.show(activityContext, rewardItem -> {
                // Handle the reward.
                Log.d("TAG", "The user earned the reward.");
                int rewardAmount = rewardItem.getAmount();
                String rewardType = rewardItem.getType();
                Log.d(TAG, "onUserEarnedReward: " + rewardAmount);
                Log.d(TAG, "onUserEarnedRewardType: " + rewardType);
            });
        } else {
            initNav(position);
        }

    }

    private void initNav(int position) {
        if (position == 0)
            ParentScreen.getInstance().navigate(R.id.action_parentDashboardFragment2_to_subjectListFragment);
        else if (position == 1)
            ParentScreen.getInstance().navigate(R.id.action_parentDashboardFragment2_to_specialityFragment);
        else if (position == 2 || position == 3)
            Toast.makeText(ParentScreen.getInstance(), "Coming Soon", Toast.LENGTH_SHORT).show();
    }

    private void createSpecialityData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child(TEACHERS);
        String specialityId = String.valueOf(System.currentTimeMillis());
        myRef.child(specialityId).setValue(getTeacherModel(specialityId)).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getLocalizedMessage()));

        Log.d(TAG, "createSpecialityData: " + specialityId);
    }

    private Object getSpecialityMap(String specialityId) {
        SpecialityModel specialityModel = new SpecialityModel();
        specialityModel.setIcon("");
        specialityModel.setName("English");
        specialityModel.setId(specialityId);
        specialityModel.setActive(true);
        return specialityModel;
    }

    public static class DashboardModelVH extends RecyclerView.ViewHolder {
        DashBoardViewBinding dashBoardViewBinding;

        public DashboardModelVH(DashBoardViewBinding dashBoardViewBinding) {
            super(dashBoardViewBinding.getRoot());
            this.dashBoardViewBinding = dashBoardViewBinding;
        }
    }
}
