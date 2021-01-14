package com.mytuition.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class SpecialityModel {

    String icon;
    String name;
    String id;
    boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecialityModel)) return false;
        SpecialityModel that = (SpecialityModel) o;
        return Objects.equals(getIcon(), that.getIcon()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(icon, name);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static DiffUtil.ItemCallback<SpecialityModel>itemCallback=new DiffUtil.ItemCallback<SpecialityModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull SpecialityModel oldItem, @NonNull SpecialityModel newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull SpecialityModel oldItem, @NonNull SpecialityModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
