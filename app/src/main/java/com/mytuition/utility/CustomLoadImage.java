package com.mytuition.utility;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.mytuition.R;
import com.mytuition.views.activity.ParentScreen;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class CustomLoadImage {

    private static final String TAG = "CustomLoadImage";


    @BindingAdapter("android:loadCustomTeacherImage")
    public static void loadCustomTeacherImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(App.context)
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.teacher_demo_icon)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadImage: " + e.getLocalizedMessage());
                imageView.setImageResource(R.drawable.teacher_demo_icon);
            }
        } else imageView.setImageResource(R.drawable.teacher_demo_icon);

    }

    @BindingAdapter("android:loadCustomClassImage")
    public static void loadCustomClassImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(App.context)
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
                Glide.with(App.context)
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.profile_demo_image)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadImage: " + e.getLocalizedMessage());
                imageView.setImageResource(R.drawable.profile_demo_image);
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

    @BindingAdapter("android:loadCustomAadharImage")
    public static void loadCustomAadharImage(ImageView imageView, String imagePath) {
        if (null != imagePath && !imagePath.isEmpty()) {
            try {
                Glide.with(App.context)
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.capture_image_icon)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "loadImage: " + e.getLocalizedMessage());
                imageView.setImageResource(R.drawable.capture_image_icon);
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
    }
}
