package com.mytuition.views.parentFragments;

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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mytuition.R;
import com.mytuition.adapters.CoachingAdapter;
import com.mytuition.adapters.DashboardPatientAdapter1;
import com.mytuition.adapters.MainSliderAdapter;
import com.mytuition.databinding.FragmentParentDashboardBinding;
import com.mytuition.models.Banner;
import com.mytuition.models.CoachingModel;
import com.mytuition.models.DashboardModel1;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.PicassoImageLoadingService;
import com.mytuition.utility.Utils;
import com.mytuition.viewHolder.ParentViewHolder;
import com.mytuition.views.activity.ParentScreen;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ss.com.bannerslider.ImageLoadingService;
import ss.com.bannerslider.Slider;

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
    NavController navController;
    ParentViewHolder viewModel;
    ImageLoadingService imageLoadingService;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentDashboardBinding = FragmentParentDashboardBinding.inflate(getLayoutInflater());
        return parentDashboardBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        adapter1 = new DashboardPatientAdapter1();
        adapter2 = new TeacherAdapter();
        adapter3 = new CoachingAdapter();

        parentDashboardBinding.rec1.setAdapter(adapter1);
        parentDashboardBinding.rec2.setAdapter(adapter2);
        parentDashboardBinding.rec3.setAdapter(adapter3);


        imageLoadingService = new PicassoImageLoadingService(ParentScreen.getInstance());
        Slider.init(imageLoadingService);


        viewModel = new ViewModelProvider(requireActivity()).get(ParentViewHolder.class);


        //Binding First Adapter
        adapter1.submitList(getFirstAdapterData());


        //Binding Second Adapter
        adapter2.submitList(getTopTeacherData());


        //Binding Third Adapter
        adapter3.submitList(getTopCoachingData());


        //Binding Slider Adapter
        setSlider();


        loadBigBannerImage();


        parentDashboardBinding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_parentDashboardFragment2_to_parentProfileFragment);
            }
        });


        setStepper();


        setLocation();

    }

    private void setLocation() {
        try {
            parentDashboardBinding.tvLocation.setText(ParentScreen.getInstance().getAreaName());
            parentDashboardBinding.tvCity.setText(ParentScreen.getInstance().getCityName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStepper() {

        /*String[] mySteps = {"Name", "Email", "Phone Number"};
        int colorPrimary = ContextCompat.getColor(requireContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark);


        // Setting up and initializing the form
        VerticalStepperFormLayout.Builder.newInstance(parentDashboardBinding.verticalStepperForm, mySteps, this, requireActivity())
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(true) // It is true by default, so in this case this line is not necessary
                .init();*/
    }

    private void loadBigBannerImage() {
        Utils.getFirebaseReference().child(DASHBOARD).child(BANNER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Banner imageUrl = dataSnapshot.getValue(Banner.class);
                    if (null != imageUrl)
                        Glide.with(requireActivity()).load(imageUrl.getBannerImage())
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(parentDashboardBinding.dashboardHomeImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    private List<DashboardModel1> getFirstAdapterData() {
        List<DashboardModel1> dashboardModel1s = new ArrayList<>();
        dashboardModel1s.add(new DashboardModel1(getString(R.string.speciality), getString(R.string.find_tutor_by)));
        dashboardModel1s.add(new DashboardModel1(getString(R.string.classs), getString(R.string.find_tutor_by)));
        dashboardModel1s.add(new DashboardModel1(getString(R.string.name), getString(R.string.find_tutor_by)));
        dashboardModel1s.add(new DashboardModel1(getString(R.string.near_you), getString(R.string.find_coachings)));
        return dashboardModel1s;
    }

    private List<TeacherModel> getTopTeacherData() {
        List<TeacherModel> teacherModels = new ArrayList<>();
        for (int a = 0; a < 10; a++) {
            TeacherModel teacherModel = new TeacherModel();
            teacherModel.setName("Teacher Name");
            teacherModel.setImage("https://cdn2.iconfinder.com/data/icons/school-flat-circle/512/Guy_hipster_jumper_man_teacher_sir-512.png");
            teacherModels.add(teacherModel);
        }
        return teacherModels;
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
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
    }
}