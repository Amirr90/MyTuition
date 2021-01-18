package com.mytuition.models;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TeacherModel extends BaseObservable {
    public static String image;
    String name;
    String experience;
    String rating;
    String review;
    String speciality;
    String fee;
    String degree;
    String collegeName;
    String address;
    String description;
    String id;
    Integer priority;

    public Integer getPriority() {
        return null == priority ? 10 : priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

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
