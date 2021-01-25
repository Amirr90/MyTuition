package com.mytuition.utility;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.mytuition.R;
import com.mytuition.views.activity.ParentScreen;

public class CustomLoadImage {

    private static final String TAG = "CustomLoadImage";
/*
    @BindingAdapter("android:loadCustomDoctorImage")
    public static void loadImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(ParentScreen.getInstance())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.defualt_clinics_image)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadImage: " + e.getLocalizedMessage());
            }
        }

    }

    @BindingAdapter("android:loadCustomProductImage")
    public static void loadCustomProductImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(PatientDashboard.getInstance())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.defualt_clinics_image)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadCustomProductImage: " + e.getLocalizedMessage());
            }
        }

    }


    @BindingAdapter("android:loadCustomDoctorWithDummyImage")
    public static void loadDummyImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(PatientDashboard.getInstance())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.doctor_dummy_image)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadDummyImage: " + e.getLocalizedMessage());
            }
        }

    }


    @BindingAdapter("android:loadCustomUserImage")
    public static void loadUserImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(PatientDashboard.getInstance())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.profile_demo_image)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                imageView.setImageResource(R.drawable.profile_demo_image);
                Log.d(TAG, "loadUserImage: " + e.getLocalizedMessage());
            }
        } else {
            imageView.setImageResource(R.drawable.profile_demo_image);
        }

    }

    @BindingAdapter("android:loadCustomPrescriptionImage")
    public static void loadPrescriptionImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(PatientDashboard.getInstance())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.diagnosis_demo_image)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadPrescriptionImage: " + e.getLocalizedMessage());
            }
        }

    }

    @BindingAdapter("android:LoadInvestigationImage")
    public static void LoadInvestigationImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(PatientDashboard.getInstance())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.diagnosis_demo_image)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadPrescriptionImage: " + e.getLocalizedMessage());
            }
        }

    }

    @BindingAdapter("android:loadNavImage")
    public static void loadNavImage(ImageView imageView, int imagePath) {
        imageView.setImageResource(imagePath);
        Log.d(TAG, "loadNavImage: " + imagePath);
    }

    @BindingAdapter("android:setCustomVisibility")
    public static void setCustomVisibility(View view, String text) {
        if (null == text || text.isEmpty())
            view.setVisibility(GONE);
        else view.setVisibility(VISIBLE);
    }

    @BindingAdapter("android:setCustomVisibility")
    public static void setCustomVisibility(View view, Boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }*/

    @BindingAdapter("android:loadCustomTeacherImage")
    public static void loadCustomTeacherImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(ParentScreen.getInstance())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadImage: " + e.getLocalizedMessage());
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }
        else imageView.setImageResource(R.drawable.ic_launcher_foreground);

    }

    @BindingAdapter("android:loadCustomClassImage")
    public static void loadCustomClassImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(ParentScreen.getInstance())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.class_demo_image)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadImage: " + e.getLocalizedMessage());
                imageView.setImageResource(R.drawable.class_demo_image);
            }
        } else imageView.setImageResource(R.drawable.class_demo_image);

    }

    @BindingAdapter("android:loadCustomParentImage")
    public static void loadCustomParentImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(ParentScreen.getInstance())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadImage: " + e.getLocalizedMessage());
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }

    }

    @BindingAdapter("android:loadCustomCoachingImage")
    public static void loadCustomCoachingImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(ParentScreen.getInstance())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadImage: " + e.getLocalizedMessage());
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }

    }

    @BindingAdapter("android:loadNavImage")
    public static void loadNavImage(ImageView imageView, int imagePath) {
        imageView.setImageResource(imagePath);
        Log.d(TAG, "loadNavImage: " + imagePath);
    }
}
