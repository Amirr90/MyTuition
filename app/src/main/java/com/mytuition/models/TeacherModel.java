package com.mytuition.models;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.mytuition.utility.AppUtils.getCurrencyFormat;

public class TeacherModel extends BaseObservable {
    public static String image;
    public static String name;
    public static String fatherName;
    public static String email;
    public static String experience;
    public static String rating;
    public static String review;
    public static String speciality;
    public static String fee;
    public static String perVisit;
    public static String feeInInstance;
    public static String perVisitFeeInInstance;
    public static String degree;
    public static String collegeName;
    public static String address;
    public static String description;
    public static String mobile;
    public static String id;
    public static Integer priority;
    public static Integer tuition;
    public static boolean isDemoClassFree;
    public static String landMark;
    public static String city;
    public static String state;
    public static String schoolName;
    public static String aadharFrontImage;
    public static String aadharBackImage;

    @Bindable
    public String getAadharFrontImage() {
        return aadharFrontImage;
    }

    public void setAadharFrontImage(String aadharFrontImage) {
        this.aadharFrontImage = aadharFrontImage;
    }

    @Bindable
    public String getAadharBackImage() {
        return aadharBackImage;
    }

    public void setAadharBackImage(String aadharBackImage) {
        this.aadharBackImage = aadharBackImage;
    }

    @Bindable
    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Bindable
    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Bindable
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Bindable
    public boolean isDemoClassFree() {
        return isDemoClassFree;
    }

    public void setDemoClassFree(boolean demoClassFree) {
        isDemoClassFree = demoClassFree;
    }

    @Bindable
    public String getPerVisit() {
        return perVisit;
    }

    public void setPerVisit(String perVisit) {
        this.perVisit = perVisit;
    }

    @Bindable
    public String getPerVisitFeeInInstance() {
        if (null == perVisit)
            return getCurrencyFormat(0);
        else return getCurrencyFormat(perVisit);

    }

    @Bindable
    public Integer getTuition() {
        return tuition;
    }

    public void setTuition(Integer tuition) {
        this.tuition = tuition;
    }

    public String getFeeInInstance() {
        if (null == fee)
            return getCurrencyFormat(0);
        else return getCurrencyFormat(fee);
    }

    @Bindable
    public Integer getPriority() {
        return null == priority ? 10 : priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Bindable
    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    @Bindable
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @Bindable
    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    @Bindable
    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Bindable
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Bindable
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Bindable
    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Bindable
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

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", experience='" + experience + '\'' +
                ", rating='" + rating + '\'' +
                ", review='" + review + '\'' +
                ", speciality='" + speciality + '\'' +
                ", fee='" + fee + '\'' +
                ", perVisit='" + perVisit + '\'' +
                ", feeInInstance='" + feeInInstance + '\'' +
                ", perVisitFeeInInstance='" + perVisitFeeInInstance + '\'' +
                ", degree='" + degree + '\'' +
                ", collegeName='" + collegeName + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", priority=" + priority +
                ", tuition=" + tuition +
                ", isDemoClassFree=" + isDemoClassFree +
                '}';
    }
}
