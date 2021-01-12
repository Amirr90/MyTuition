package com.mytuition.models;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class TeacherModel extends BaseObservable {
    String image;
    String name;
    String experience;
    String rating;
    String review;
    String speciality;
    String fee;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeacherModel)) return false;
        TeacherModel that = (TeacherModel) o;
        return Objects.equals(getImage(), that.getImage()) &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImage(), getName());
    }

    public static DiffUtil.ItemCallback<TeacherModel> itemCallback = new DiffUtil.ItemCallback<TeacherModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull TeacherModel oldItem, @NonNull TeacherModel newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull TeacherModel oldItem, @NonNull TeacherModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
