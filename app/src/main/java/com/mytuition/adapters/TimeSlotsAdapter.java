package com.mytuition.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.R;
import com.mytuition.databinding.TimingViewPrimaryNewBinding;
import com.mytuition.databinding.TimingViewSecondaryNewBinding;
import com.mytuition.interfaces.AdapterInterface;
import com.mytuition.models.TeacherModel;
import com.mytuition.views.activity.ParentScreen;

import java.util.List;

import static com.mytuition.utility.AppUtils.fadeIn;

public class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.SlotVH> {
    List<TeacherModel.TimeSlotModel> timeSlotsModelList;
    TimeSlotsAdapterSecondary adapterSecondary;
    AdapterInterface adapterInterface;

    public TimeSlotsAdapter(List<TeacherModel.TimeSlotModel> timeSlotsModelList, AdapterInterface adapterInterface) {
        this.timeSlotsModelList = timeSlotsModelList;
        this.adapterInterface = adapterInterface;
    }


    @NonNull
    @Override
    public SlotVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TimingViewPrimaryNewBinding primaryNewBinding = TimingViewPrimaryNewBinding.inflate(inflater, parent, false);
        return new SlotVH(primaryNewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotVH holder, int position) {


        TeacherModel.TimeSlotModel timeSlotsModel = timeSlotsModelList.get(position);

        holder.primaryNewBinding.setTiming(timeSlotsModel);
        if (null != timeSlotsModel.getType()) {
            adapterSecondary = new TimeSlotsAdapterSecondary(timeSlotsModel.getSlots(), new AdapterInterface() {
                @Override
                public void onItemClicked(Object o) {
                    adapterInterface.onItemClicked(o);
                }

            });
            holder.primaryNewBinding.Rec.setAdapter(adapterSecondary);
            holder.primaryNewBinding.getRoot().setAnimation(fadeIn(ParentScreen.getInstance()));
        }


    }

    @Override
    public int getItemCount() {
        return null == timeSlotsModelList ? 0 : timeSlotsModelList.size();
    }

    public static class SlotVH extends RecyclerView.ViewHolder {
        TimingViewPrimaryNewBinding primaryNewBinding;

        public SlotVH(TimingViewPrimaryNewBinding primaryNewBinding) {
            super(primaryNewBinding.getRoot());
            this.primaryNewBinding = primaryNewBinding;
        }
    }


    public static class TimeSlotsAdapterSecondary extends RecyclerView.Adapter<TimeSlotsAdapterSecondary.SlotsSecondaryVH> {

        List<String> timeDetailsModelList;
        int subSelectedPosition = -1;
        AdapterInterface adapterInterface;

        public TimeSlotsAdapterSecondary(List<String> timeDetailsModelList, AdapterInterface adapterInterface) {
            this.timeDetailsModelList = timeDetailsModelList;
            this.adapterInterface = adapterInterface;
        }


        @NonNull
        @Override
        public TimeSlotsAdapterSecondary.SlotsSecondaryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            TimingViewSecondaryNewBinding viewSecondaryNewBinding = TimingViewSecondaryNewBinding.inflate(inflater, parent, false);
            return new SlotsSecondaryVH(viewSecondaryNewBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull TimeSlotsAdapterSecondary.SlotsSecondaryVH holder, final int position) {
            final String timeDetailsModel = timeDetailsModelList.get(position);
            holder.viewSecondaryNewBinding.setTimeDetailsModel(timeDetailsModel);

            if (subSelectedPosition == position) {
                changeLayoutColor(holder, ParentScreen.getInstance().getResources().getDrawable(R.drawable.round_green),
                        ParentScreen.getInstance().getResources().getColor(R.color.white)
                );
            } else {
                changeLayoutColor(holder, ParentScreen.getInstance().getResources().getDrawable(R.drawable.round_for_search),
                        ParentScreen.getInstance().getResources().getColor(R.color.colorPrimary)
                );
            }

            holder.viewSecondaryNewBinding.timingText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subSelectedPosition = position;
                    notifyDataSetChanged();
                    adapterInterface.onItemClicked(timeDetailsModel);
                }
            });

        }

        private void changeLayoutColor(SlotsSecondaryVH holder, Drawable drawable, int color) {
            holder.viewSecondaryNewBinding.timingText.setBackground(drawable);
            holder.viewSecondaryNewBinding.timingText.setTextColor(color);
        }

        @Override
        public int getItemCount() {
            return null == timeDetailsModelList ? 0 : timeDetailsModelList.size();
        }

        public static class SlotsSecondaryVH extends RecyclerView.ViewHolder {
            TimingViewSecondaryNewBinding viewSecondaryNewBinding;

            public SlotsSecondaryVH(TimingViewSecondaryNewBinding viewSecondaryNewBinding) {
                super(viewSecondaryNewBinding.getRoot());
                this.viewSecondaryNewBinding = viewSecondaryNewBinding;
            }
        }
    }

}
