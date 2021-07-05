package com.mytuition.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mytuition.R;
import com.mytuition.databinding.CalenderViewBinding;
import com.mytuition.models.CalendarModel;
import com.mytuition.views.activity.ParentScreen;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalenderVH> {

    List<CalendarModel> calendarModelList;
    CalenderInterface calenderInterface;

    int selectedPosition = 0;

    public CalendarAdapter(List<CalendarModel> calendarModelList, CalenderInterface calenderInterface) {
        this.calendarModelList = calendarModelList;
        this.calenderInterface = calenderInterface;

    }

    @NonNull
    @Override
    public CalenderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CalenderViewBinding calenderViewBinding = CalenderViewBinding.inflate(inflater, parent, false);
        calenderViewBinding.setCalenderInterface(calenderInterface);
        return new CalenderVH(calenderViewBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull final CalenderVH holder, final int position) {

        final CalendarModel calendarModel = calendarModelList.get(position);
        holder.calenderViewBinding.setCalender(calendarModel);
        holder.calenderViewBinding.rlCalenderRoot.setOnClickListener(v -> {
            calenderInterface.onItemClicked(calendarModel, position);
            holder.calenderViewBinding.getRoot().setBackground(ParentScreen.getInstance().getResources().getDrawable(R.drawable.rectangle_outline_new_ui_color_yellow));
            selectedPosition = position;
            setTextColor(holder, ParentScreen.getInstance().getResources().getColor(R.color.white),
                    ParentScreen.getInstance().getResources().getColor(R.color.white));
            notifyDataSetChanged();
        });

        if (selectedPosition == position) {
            setTextColor(holder, ParentScreen.getInstance().getResources().getColor(R.color.white),
                    ParentScreen.getInstance().getResources().getColor(R.color.white));
            holder.calenderViewBinding.getRoot().setBackground(ParentScreen.getInstance().getResources().getDrawable(R.drawable.rectangle_outline_new_ui_color_yellow));

        } else {
            setTextColor(holder, ParentScreen.getInstance().getResources().getColor(R.color.colorPrimary),
                    ParentScreen.getInstance().getResources().getColor(R.color.GreyColo));
            holder.calenderViewBinding.getRoot().setBackground(ParentScreen.getInstance().getResources().getDrawable(R.drawable.rectangle_outline_new_ui_color));
        }


    }

    private void setTextColor(CalenderVH holder, int color, int color2) {
        holder.calenderViewBinding.textView81.setTextColor(color2);
        holder.calenderViewBinding.textView85.setTextColor(color);
        holder.calenderViewBinding.textView86.setTextColor(color2);
    }

    @Override
    public int getItemCount() {
        return calendarModelList.size();
    }

    public interface CalenderInterface {
        void onItemClicked(CalendarModel calendarModel, int position);
    }

    public static class CalenderVH extends RecyclerView.ViewHolder {
        CalenderViewBinding calenderViewBinding;

        public CalenderVH(CalenderViewBinding calenderViewBinding) {
            super(calenderViewBinding.getRoot());
            this.calenderViewBinding = calenderViewBinding;
        }
    }
}

