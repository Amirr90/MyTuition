package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mytuition.R;
import com.mytuition.adapters.CoachingAdapter;
import com.mytuition.adapters.DashboardPatientAdapter1;
import com.mytuition.adapters.HomeBannerAdapter;
import com.mytuition.adapters.MainSliderAdapter;
import com.mytuition.adapters.TestimonialsAdapter;
import com.mytuition.databinding.FragmentParentDashboardBinding;
import com.mytuition.models.Banner;
import com.mytuition.models.BannerAddModel;
import com.mytuition.models.CoachingModel;
import com.mytuition.models.DashboardModel;
import com.mytuition.models.DashboardModel1;
import com.mytuition.models.ParentModel;
import com.mytuition.models.RequestModel2;
import com.mytuition.models.TestimonialsModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.PicassoImageLoadingService;
import com.mytuition.utility.Utils;
import com.mytuition.viewHolder.ParentViewHolder;
import com.mytuition.views.activity.ParentScreen;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.ImageLoadingService;
import ss.com.bannerslider.Slider;

import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.Utils.getParentModel;

public class ParentDashboardFragment extends Fragment {

    public static final String DASHBOARD = "Dashboard";
    public static final String BANNER = "Banner";
    public static final String BANNER_SLIDER = "SliderBanner";
    private static final String TAG = "ParentDashboardFragment";
    public static ParentDashboardFragment instance;
    //aamirr.3212@gmail.com
    FragmentParentDashboardBinding parentDashboardBinding;
    DashboardPatientAdapter1 adapter1;
    TeacherAdapter adapter2;
    CoachingAdapter adapter3;
    TestimonialsAdapter testimonialsAdapter;
    NavController navController;
    ParentViewHolder viewModel;
    ImageLoadingService imageLoadingService;
    RequestModel2 requestModel2;
    List<TestimonialsModel> testimonialsModels;
    List<DashboardModel> models;

    AdView adView;
    AdRequest adRequest;
    AdLoader adLoader;
    List<BannerAddModel> bannerImages = new ArrayList<>();
    HomeBannerAdapter homeBannerAdapter;

    public static ParentDashboardFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentDashboardBinding = FragmentParentDashboardBinding.inflate(getLayoutInflater());
        instance = this;
        return parentDashboardBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        requestModel2 = new RequestModel2();

        initAds();
        initNativeAds();
        parentDashboardBinding.setParent(getParentModel(requireActivity()));
        adapter1 = new DashboardPatientAdapter1();
        adapter2 = new TeacherAdapter();
        adapter3 = new CoachingAdapter();
        testimonialsModels = new ArrayList<>();
        testimonialsAdapter = new TestimonialsAdapter(testimonialsModels);

        homeBannerAdapter = new HomeBannerAdapter(bannerImages);
        parentDashboardBinding.bannerViewPager.setAdapter(homeBannerAdapter);

        parentDashboardBinding.rec1.setAdapter(adapter1);
        parentDashboardBinding.rec2.setAdapter(adapter2);
        parentDashboardBinding.rec3.setAdapter(adapter3);

        parentDashboardBinding.recTestimonials.setAdapter(testimonialsAdapter);


        imageLoadingService = new PicassoImageLoadingService(ParentScreen.getInstance());
        Slider.init(imageLoadingService);


        viewModel = new ViewModelProvider(requireActivity()).get(ParentViewHolder.class);
        requestModel2.setCity("Lucknow");
        requestModel2.setUserId(getUid());
        viewModel.getDashboardData(requestModel2, navController).observe(getViewLifecycleOwner(), dashboardModels -> {
            AppUtils.hideDialog();
            models = dashboardModels;
            if (!dashboardModels.isEmpty()) {

                //Binding First Adapter
                adapter1.submitList(getFirstAdapterData());

                //Binding Top Teacher Data Adapter
                adapter2.submitList(dashboardModels.get(0).getTeacherList());


                //Binding Top Coaching Adapter
                adapter3.submitList(getTopCoachingData());

                //Binding Testimonials  Adapter
                testimonialsModels.clear();
                testimonialsModels.addAll(dashboardModels.get(0).getTestimonialsList());
                testimonialsAdapter.notifyDataSetChanged();


                //Binding Slider Adapter
                setSlider();
                loadBigBannerImage(dashboardModels.get(0).getBannerData());
            } else Log.d(TAG, "onChanged: no data");
        });

        updateProfile();
        parentDashboardBinding.profileImage.setOnClickListener(view1 -> navController.navigate(R.id.action_parentDashboardFragment2_to_parentProfileFragment));


        parentDashboardBinding.imageView7.setOnClickListener(
                v -> ParentScreen.getInstance().openDrawer());
        parentDashboardBinding.tvWriteReview.setOnClickListener(v -> navController.navigate(R.id.action_parentDashboardFragment2_to_writeTestimonialsDialog));

        parentDashboardBinding.bottomAppBar.setNavigationOnClickListener(v -> ParentScreen.getInstance().openDrawer());

        parentDashboardBinding.bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.profile_image) {
                navController.navigate(R.id.action_parentDashboardFragment2_to_parentProfileFragment);
            }
            return true;
        });

        parentDashboardBinding.btnVideoCall.setOnClickListener(v -> navController.navigate(R.id.action_parentDashboardFragment2_to_videoCallExplainationFragment));


        parentDashboardBinding.laySearch.setOnClickListener(v -> navController.navigate(R.id.action_parentDashboardFragment2_to_searchTeacherFragment));
        getCredential();
    }

    public void getCredential() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        try {
            Log.d(TAG, "getAccessToken: " + firebaseAuth.getAccessToken(true).addOnSuccessListener(getTokenResult -> {
                Log.d(TAG, "onSuccess: " + getTokenResult.getToken());
                Log.d(TAG, "onSuccess: " + getTokenResult.getExpirationTimestamp());
                Log.d(TAG, "onSuccess: " + getTokenResult.getSignInProvider());
                Log.d(TAG, "onSuccess: " + getTokenResult.getAuthTimestamp());
                Log.d(TAG, "onSuccess: " + getTokenResult.getClaims());

                /*FirebaseAuth.getInstance().revokeRefreshTokens(uid);
                UserRecord user = FirebaseAuth.getInstance().getUser(uid);
                long revocationSecond = user.getTokensValidAfterTimestamp() / 1000;
                System.out.println("Tokens revoked at: " + revocationSecond);*/


            }));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getCredential: " + e.getLocalizedMessage());
        }
    }

    private void initNativeAds() {
        adLoader = new AdLoader.Builder(requireContext(), "ca-app-pub-3940256099942544/2247696110")
                /*   adLoader = new AdLoader.Builder(requireContext(), "ca-app-pub-4669458320732543/1192567403")*/
                .forNativeAd(NativeAd -> {
                    Log.d(TAG, "onNativeAdLoaded: " + NativeAd.getStore());
                    for (int a = 0; a < NativeAd.getImages().size(); a++) {
                        String image = (NativeAd.getImages().get(a).getUri().toString());
                        homeBannerAdapter.addItem(new BannerAddModel(image, NativeAd.getBody(), NativeAd.getHeadline(), NativeAd.getCallToAction(), NativeAd.getAdvertiser()));
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        Log.d(TAG, "onAdFailedToLoad: " + adError.getMessage());
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAds(new AdRequest.Builder().build(), 3);


    }


    private void initAds() {

        adView = new AdView(requireActivity());
        adView.setAdUnitId(getString(R.string.adaptive_banner_ad_unit_id_test));
        parentDashboardBinding.adContainer.addView(adView);

        MobileAds.initialize(requireActivity(), initializationStatus -> {
            setUpAds();
        });
    }

    private void setUpAds() {

        loadBanner();
    }


    private void loadBanner() {
        adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(requireActivity(), adWidth);
    }

    private void loadBigBannerImage(DashboardModel.BannerData bannerData) {
        bannerImages.clear();
        bannerImages.add(new BannerAddModel(bannerData.getBanner().getBannerImage(), "", "", "", ""));
        if (null != bannerData.getBanner().getBannerImage2())
            bannerImages.add(new BannerAddModel(bannerData.getBanner().getBannerImage2(), "", "", "", ""));
        if (null != bannerData.getBanner().getBannerImage3())
            bannerImages.add(new BannerAddModel(bannerData.getBanner().getBannerImage3(), "", "", "", ""));
        if (null != bannerData.getBanner().getBannerImage4())
            bannerImages.add(new BannerAddModel(bannerData.getBanner().getBannerImage4(), "", "", "", ""));
        if (null != bannerData.getBanner().getBannerImage5())
            bannerImages.add(new BannerAddModel(bannerData.getBanner().getBannerImage5(), "", "", "", ""));
        homeBannerAdapter.notifyDataSetChanged();
        parentDashboardBinding.dotsIndicator.setViewPager2(parentDashboardBinding.bannerViewPager);
    }


    public void updateProfile() {
        ParentModel parentModel = getParentModel(requireActivity());
        parentDashboardBinding.setParent(parentModel);
    }


    private List<CoachingModel> getTopCoachingData() {
        List<CoachingModel> coachingModels = new ArrayList<>();
        for (int a = 0; a <= 10; a++) {
            CoachingModel coachingModel = new CoachingModel();
            coachingModel.setName("Coaching Name");
            coachingModel.setAddress("Address");
            coachingModel.setCity("Lucknow");
            coachingModel.setState("UP");
            coachingModel.setImage("https://d19d5sz0wkl0lu.cloudfront.net/dims4/default/485d738/2147483647/resize/x300%3E/quality/90/?url=https%3A%2F%2Fatd-brightspot.s3.amazonaws.com%2Fe0%2F6a%2F2cab111993406927d729a836dec0%2Fcoaching-word-bubble.jpg");
            coachingModels.add(coachingModel);
        }
        return coachingModels;
    }


    public void updateLocation(String city, String area) {
        try {
            parentDashboardBinding.tvLocation.setText(area);
            parentDashboardBinding.tvCity.setText(city);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<DashboardModel1> getFirstAdapterData() {
        List<DashboardModel1> dashboardModel1s = new ArrayList<>();
        dashboardModel1s.add(new DashboardModel1(getString(R.string.speciality), getString(R.string.find_tutor_by)));
        dashboardModel1s.add(new DashboardModel1(getString(R.string.classs), getString(R.string.find_tutor_by)));
        dashboardModel1s.add(new DashboardModel1(getString(R.string.name), getString(R.string.find_tutor_by)));
        dashboardModel1s.add(new DashboardModel1(getString(R.string.near_you), getString(R.string.find_coachings)));
        return dashboardModel1s;
    }

    private List<Banner> getSliderData() {
        List<Banner> bannerDetails = new ArrayList<>();
        Banner banner = new Banner();
        banner.setBannerImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQIMtTXtQqOhJIBPSSS2oZvX_IMG-d-PN-7Qw&usqp=CAU");
        bannerDetails.add(banner);

        banner.setBannerImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSzksE4aiykxPHYitCwoE6rl_1N1gH43Fd2MA&usqp=CAU");
        bannerDetails.add(banner);
        return bannerDetails;
    }

    private void setSlider() {
        Utils.getFirebaseReference().child(DASHBOARD).child(BANNER_SLIDER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> images = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        // TODO: handle the post
                        Banner banner = postSnapshot.getValue(Banner.class);
                        if (banner != null)
                            images.add(banner.getBannerImage());
                    }
                } else {
                    for (Banner banner : getSliderData())
                        images.add(banner.getBannerImage());
                }

                parentDashboardBinding.bannerSlider1.setAdapter(new MainSliderAdapter(images));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ArrayList<String> images = new ArrayList<>();
                for (Banner banner : getSliderData())
                    images.add(banner.getBannerImage());
                parentDashboardBinding.bannerSlider1.setAdapter(new MainSliderAdapter(images));

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtils.hideToolbar(requireActivity());
    }


}