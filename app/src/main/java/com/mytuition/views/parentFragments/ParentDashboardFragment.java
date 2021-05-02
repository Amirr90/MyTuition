package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

    public static ParentDashboardFragment instance;

    List<DashboardModel> models;

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

        parentDashboardBinding.setParent(getParentModel(requireActivity()));
        adapter1 = new DashboardPatientAdapter1();
        adapter2 = new TeacherAdapter();
        adapter3 = new CoachingAdapter();
        testimonialsModels = new ArrayList<>();
        testimonialsAdapter = new TestimonialsAdapter(testimonialsModels);

        parentDashboardBinding.rec1.setAdapter(adapter1);
        parentDashboardBinding.rec2.setAdapter(adapter2);
        parentDashboardBinding.rec3.setAdapter(adapter3);
        parentDashboardBinding.recTestimonials.setAdapter(testimonialsAdapter);


        imageLoadingService = new PicassoImageLoadingService(ParentScreen.getInstance());
        Slider.init(imageLoadingService);


        viewModel = new ViewModelProvider(requireActivity()).get(ParentViewHolder.class);
        requestModel2.setCity("Lucknow");
        requestModel2.setUserId(getUid());
        viewModel.getDashboardData(requestModel2, requireActivity()).observe(getViewLifecycleOwner(), new Observer<List<DashboardModel>>() {
            @Override
            public void onChanged(List<DashboardModel> dashboardModels) {
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
            }
        });

        updateProfile();
        parentDashboardBinding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_parentDashboardFragment2_to_parentProfileFragment);
            }
        });


        parentDashboardBinding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentScreen.getInstance().openDrawer();
            }
        });
        parentDashboardBinding.tvWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_parentDashboardFragment2_to_writeTestimonialsDialog);
            }
        });

    }


    private void loadBigBannerImage(DashboardModel.BannerData bannerData) {
        List<String> bannerImages = new ArrayList<>();
        bannerImages.add(bannerData.getBanner().getBannerImage());
        if (null != bannerData.getBanner().getBannerImage2())
            bannerImages.add(bannerData.getBanner().getBannerImage2());
        if (null != bannerData.getBanner().getBannerImage3())
            bannerImages.add(bannerData.getBanner().getBannerImage3());
        if (null != bannerData.getBanner().getBannerImage4())
            bannerImages.add(bannerData.getBanner().getBannerImage4());
        if (null != bannerData.getBanner().getBannerImage5())
            bannerImages.add(bannerData.getBanner().getBannerImage5());
        parentDashboardBinding.bannerViewPager.setAdapter(new HomeBannerAdapter(bannerImages));

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