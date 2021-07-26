package com.mytuition.views.commonFragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.mytuition.R;
import com.mytuition.databinding.FragmentTeacherProfileBinding;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import static com.mytuition.utility.AppUtils.getJSONFromModel;

public class TeacherProfileFragment extends Fragment {
    private static final String TAG = "TeacherProfileFragment";

    FragmentTeacherProfileBinding teacherProfileBinding;
    NavController navController;
    TeacherModel teacherModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        teacherProfileBinding = FragmentTeacherProfileBinding.inflate(getLayoutInflater());
        return teacherProfileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        if (null == getArguments())
            return;

        teacherProfileBinding.animationViewAudioCall.setVisibility(getArguments().containsKey("tuitionPage") ? View.GONE : View.GONE);
        teacherProfileBinding.btnRequestTuition.setVisibility(getArguments().containsKey("tuitionPage") ? View.GONE : View.VISIBLE);

        String jsonString = getArguments().getString("docModel");
        Gson gson = new Gson();

        teacherModel = gson.fromJson(jsonString, TeacherModel.class);
        teacherProfileBinding.setTeacher(teacherModel);

        setAboutMe(teacherModel);


        teacherProfileBinding.btnRequestTuition.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("docModel", getJSONFromModel(teacherModel));
            bundle.putString(AppConstant.SPECIALITY_NAME, AppConstant.ALL);
            navController.navigate(R.id.action_teacherProfileFragment_to_selectTimeSlotsFragment, bundle);
        });
    }


    private void setAboutMe(TeacherModel teacherModel) {

        if (teacherModel != null) {
            String text;
            if (null != teacherModel.getAbout() && !teacherModel.getAbout().isEmpty()) {
                text = teacherModel.getAbout();
            } else {
                text = "Hi there, I'm \'<b>" + teacherModel.getName() + "</b>\' and I teach all subjects and specialization in <b>" + teacherModel.getSpeciality() + "</b>. I have been teaching for over <b>" + teacherModel.getTeachingProfile().getExperience() + "</b> years and have education experience in <b>" + teacherModel.getAcademicInformation().getHighestEducation() + "</b>\'. When you walk in my classroom you will notice.";
            }
            teacherProfileBinding.textView105.setText(Html.fromHtml(text));
        }

        Drawable img = teacherProfileBinding.textView92.getContext().getResources().getDrawable(R.drawable.ic_baseline_verified_user_24);

        if (teacherModel.getProfile().getVerified())
            teacherProfileBinding.textView92.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        else
            teacherProfileBinding.textView92.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtils.showToolbar(requireActivity());
    }
}