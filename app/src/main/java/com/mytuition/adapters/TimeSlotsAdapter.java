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
import com.mytuition.models.TimeSlotModel;
import com.mytuition.views.activity.ParentScreen;

import java.util.List;

import static com.mytuition.utility.AppUtils.fadeIn;

public class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.SlotVH> {
    List<TimeSlotModel> timeSlotsModelList;
    TimeSlotsAdapterSecondary adapterSecondary;
    AdapterInterface adapterInterface;

    public TimeSlotsAdapter(List<TimeSlotModel> timeSlotsModelList, AdapterInterface adapterInterface) {
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


        TimeSlotModel timeSlotsModel = timeSlotsModelList.get(position);

        holder.primaryNewBinding.setTiming(timeSlotsModel);
        if (null != timeSlotsModel.getTimeDetails()) {
            adapterSecondary = new TimeSlotsAdapterSecondary(timeSlotsModel.getTimeDetails(), new AdapterInterface() {
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
        return timeSlotsModelList.size();
    }

    public class SlotVH extends RecyclerView.ViewHolder {
        TimingViewPrimaryNewBinding primaryNewBinding;

        public SlotVH(TimingViewPrimaryNewBinding primaryNewBinding) {
            super(primaryNewBinding.getRoot());
            this.primaryNewBinding = primaryNewBinding;
        }
    }


    public class TimeSlotsAdapterSecondary extends RecyclerView.Adapter<TimeSlotsAdapterSecondary.SlotsSecondaryVH> {

        List<TimeSlotModel.TimeDetails> timeDetailsModelList;
        int subSelectedPosition = -1;
        AdapterInterface adapterInterface;

        public TimeSlotsAdapterSecondary(List<TimeSlotModel.TimeDetails> timeDetailsModelList, AdapterInterface adapterInterface) {
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
            final TimeSlotModel.TimeDetails timeDetailsModel = timeDetailsModelList.get(position);
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
                    adapterInterface.onItemClicked(timeDetailsModel.getSlotTime());
                }
            });

        }

        private void changeLayoutColor(SlotsSecondaryVH holder, Drawable drawable, int color) {
            holder.viewSecondaryNewBinding.timingText.setBackground(drawable);
            holder.viewSecondaryNewBinding.timingText.setTextColor(color);
        }

        @Override
        public int getItemCount() {
            return timeDetailsModelList.size();
        }

        public class SlotsSecondaryVH extends RecyclerView.ViewHolder {
            TimingViewSecondaryNewBinding viewSecondaryNewBinding;

            public SlotsSecondaryVH(TimingViewSecondaryNewBinding viewSecondaryNewBinding) {
                super(viewSecondaryNewBinding.getRoot());
                this.viewSecondaryNewBinding = viewSecondaryNewBinding;
            }
        }
    }

}
