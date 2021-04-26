package com.mytuition.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mytuition.R;
import com.mytuition.databinding.HomeBannerViewBinding;
import com.mytuition.utility.App;

import java.util.List;

public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.BannerVH> {


    List<String> bannerImages;

    public HomeBannerAdapter(List<String> bannerImages) {
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
        String bannerImage = bannerImages.get(position);
        Glide.with(App.context).load(bannerImage)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.binding.dashboardHomeImage);
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
