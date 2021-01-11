package com.mytuition.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.mytuition.BR;

public class Registration extends BaseObservable {

    String name;
    String password;
    String email;
    String phone;
    String address;
    String highestEducation;
    String fee;
    String experience;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getHighestEducation() {
        return highestEducation;
    }

    public void setHighestEducation(String highestEducation) {
        this.highestEducation = highestEducation;
        notifyPropertyChanged(BR.highestEducation);
    }

    @Bindable
    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
        notifyPropertyChanged(BR.fee);

    }

    @Bindable
    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
        notifyPropertyChanged(BR.experience);
    }
}
