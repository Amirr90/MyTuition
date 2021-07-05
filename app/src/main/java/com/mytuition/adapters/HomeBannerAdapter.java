package com.mytuition.adapters;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mytuition.R;
import com.mytuition.databinding.HomeBannerViewBinding;
import com.mytuition.models.BannerAddModel;
import com.mytuition.utility.App;
import com.mytuition.utility.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.BannerVH> {


    private static final String TAG = "HomeBannerAdapter";
    List<BannerAddModel> bannerImages;

    public HomeBannerAdapter(List<BannerAddModel> bannerImages) {
        this.bannerImages = bannerImages;

    }

    @NonNull
    @Override
    public BannerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HomeBannerViewBinding binding = HomeBannerViewBinding.inflate(inflater, parent, false);
        return new BannerVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerVH holder, int position) {
        BannerAddModel bannerImage = bannerImages.get(position);
        holder.binding.setAdModel(bannerImage);
        Glide.with(App.context).load(bannerImage.getImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.binding.dashboardHomeImage);
        holder.binding.textView51.setAnimation(AppUtils.slideUp(App.context));


        holder.binding.getRoot().setOnClickListener(v -> {
            if (null != bannerImage.getCallToAction() && !bannerImage.getCallToAction().isEmpty()) {
                Log.d(TAG, "onClick: " + bannerImage.getCallToAction());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bannerImage.getCallToAction()));
                App.context.startActivity(browserIntent);
            }
        });
    }


    public void addItem(BannerAddModel addModel) {
        if (null == bannerImages)
            bannerImages = new ArrayList<>();
        bannerImages.add(0, addModel);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return null == bannerImages ? 0 : bannerImages.size();
    }

    public static class BannerVH extends RecyclerView.ViewHolder {
        HomeBannerViewBinding binding;

        public BannerVH(@NonNull HomeBannerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
