package com.mytuition.adapters;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.R;
import com.mytuition.databinding.TimingViewTeacherPrimaryBinding;
import com.mytuition.databinding.TimingViewTeacherSecondaryBinding;
import com.mytuition.interfaces.TeacherTimingInterface;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.App;

import static com.mytuition.utility.AppUtils.fadeIn;

public class TeacherTimingPrimaryAdapter extends ListAdapter<TeacherModel.TimeSlotModel, TeacherTimingPrimaryAdapter.TeacherVH> {

    public static DiffUtil.ItemCallback<TeacherModel.TimeSlotModel> itemCallback = new DiffUtil.ItemCallback<TeacherModel.TimeSlotModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull TeacherModel.TimeSlotModel oldItem, @NonNull TeacherModel.TimeSlotModel newItem) {
            return oldItem.getType().equalsIgnoreCase(newItem.getType());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull TeacherModel.TimeSlotModel oldItem, @NonNull TeacherModel.TimeSlotModel newItem) {
            return oldItem.equals(newItem);
        }
    };
    TimeSlotsAdapterSecondary adapterSecondary;
    TeacherTimingInterface adapterInterface;


    public TeacherTimingPrimaryAdapter(TeacherTimingInterface adapterInterface) {
        super(itemCallback);
        this.adapterInterface = adapterInterface;
    }

    @NonNull
    @Override
    public TeacherVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TimingViewTeacherPrimaryBinding primaryBinding = TimingViewTeacherPrimaryBinding.inflate(layoutInflater, parent, false);
        return new TeacherVH(primaryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherVH holder, int position) {
        holder.primaryBinding.setTimingModel(getItem(position));

        TeacherModel.TimeSlotModel timeSlotsModel = getItem(position);
        if (null != timeSlotsModel.getType()) {
            adapterSecondary = new TimeSlotsAdapterSecondary(timeSlotsModel, adapterInterface);
            holder.primaryBinding.recSecondary.setAdapter(adapterSecondary);
            holder.primaryBinding.getRoot().setAnimation(fadeIn(App.context));
        }
    }

    private void changeLayoutColor(TimeSlotsAdapterSecondary.SecondaryVH holder, Drawable drawable, int color) {
        holder.viewSecondaryNewBinding.timingText.setBackground(drawable);
        holder.viewSecondaryNewBinding.timingText.setTextColor(color);
    }

    public class TeacherVH extends RecyclerView.ViewHolder {
        TimingViewTeacherPrimaryBinding primaryBinding;

        public TeacherVH(@NonNull TimingViewTeacherPrimaryBinding primaryBinding) {
            super(primaryBinding.getRoot());
            this.primaryBinding = primaryBinding;
        }
    }

    private class TimeSlotsAdapterSecondary extends RecyclerView.Adapter<TimeSlotsAdapterSecondary.SecondaryVH> {
        TeacherModel.TimeSlotModel timeSlotModel;
        TeacherTimingInterface adapterInterface;

        int subSelectedPosition = -1;

        public TimeSlotsAdapterSecondary(TeacherModel.TimeSlotModel timeSlotModel, TeacherTimingInterface adapterInterface) {
            this.timeSlotModel = timeSlotModel;
            this.adapterInterface = adapterInterface;
        }

        @NonNull
        @Override
        public TimeSlotsAdapterSecondary.SecondaryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            TimingViewTeacherSecondaryBinding secondaryBinding = TimingViewTeacherSecondaryBinding.inflate(layoutInflater, parent, false);
            return new SecondaryVH(secondaryBinding);

        }

        @Override
        public void onBindViewHolder(@NonNull TimeSlotsAdapterSecondary.SecondaryVH holder, int position) {

            final String timeDetailsModel = timeSlotModel.getSlots().get(position);
            holder.viewSecondaryNewBinding.setTimeDetailsModel(timeDetailsModel);
            if (subSelectedPosition == position) {
                changeLayoutColor(holder, App.context.getResources().getDrawable(R.drawable.round_green),
                        App.context.getResources().getColor(R.color.white)
                );
            } else {
                changeLayoutColor(holder, App.context.getResources().getDrawable(R.drawable.round_for_search),
                        App.context.getResources().getColor(R.color.colorPrimary)
                );
            }


           /* if (subSelectedPosition == position) {
                changeLayoutColor(holder, ParentScreen.getInstance().getResources().getDrawable(R.drawable.round_green),
                        ParentScreen.getInstance().getResources().getColor(R.color.white)
                );
            } else {
                changeLayoutColor(holder, ParentScreen.getInstance().getResources().getDrawable(R.drawable.round_for_search),
                        ParentScreen.getInstance().getResources().getColor(R.color.colorPrimary)
                );
            }

            holder.viewSecondaryNewBinding.timingText.setOnClickListener(v -> {
                subSelectedPosition = position;
                notifyDataSetChanged();
                adapterInterface.onItemClicked(timeDetailsModel);
            });*/

            holder.viewSecondaryNewBinding.timingText.setOnClickListener(v -> {
                subSelectedPosition = position;
                notifyDataSetChanged();
                adapterInterface.onClick(timeSlotModel.getType(), timeDetailsModel);
            });

        }

        @Override
        public int getItemCount() {
            return timeSlotModel.getSlots().size();
        }

        public class SecondaryVH extends RecyclerView.ViewHolder {
            TimingViewTeacherSecondaryBinding viewSecondaryNewBinding;

            public SecondaryVH(@NonNull TimingViewTeacherSecondaryBinding secondaryBinding) {
                super(secondaryBinding.getRoot());
                this.viewSecondaryNewBinding = secondaryBinding;
            }
        }
    }
}
