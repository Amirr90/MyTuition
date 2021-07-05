package com.mytuition.models;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class CoachingModel extends BaseObservable {
    public static DiffUtil.ItemCallback<CoachingModel> itemCallback = new DiffUtil.ItemCallback<CoachingModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull CoachingModel oldItem, @NonNull CoachingModel newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CoachingModel oldItem, @NonNull CoachingModel newItem) {
            return oldItem.equals(newItem);
        }
    };
    String image;
    String address;
    String name;
    String city;
    String state;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoachingModel)) return false;
        CoachingModel that = (CoachingModel) o;
        return Objects.equals(getImage(), that.getImage()) &&
                Objects.equals(getAddress(), that.getAddress()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCity(), that.getCity()) &&
                Objects.equals(getState(), that.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImage(), getAddress(), getName(), getCity(), getState());
    }
}
