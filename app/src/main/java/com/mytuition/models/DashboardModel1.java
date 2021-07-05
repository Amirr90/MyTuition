package com.mytuition.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class DashboardModel1 {

    public static DiffUtil.ItemCallback<DashboardModel1> itemCallback = new DiffUtil.ItemCallback<DashboardModel1>() {
        @Override
        public boolean areItemsTheSame(@NonNull DashboardModel1 oldItem, @NonNull DashboardModel1 newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DashboardModel1 oldItem, @NonNull DashboardModel1 newItem) {
            return oldItem.equals(newItem);
        }
    };
    String title;
    String description;


    public DashboardModel1(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashboardModel1 that = (DashboardModel1) o;
        return getTitle().equals(that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle());
    }
}
