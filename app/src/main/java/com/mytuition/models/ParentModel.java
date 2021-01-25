package com.mytuition.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class ParentModel extends BaseObservable {


    private String user;
    private String lastRequestStatus;
    public String email;
    public String image;
    public String dob;
    public String gender;
    public String address;
    public String name;
    public String mobile;

    @Bindable
    public String getLastRequestStatus() {
        return lastRequestStatus;
    }

    public void setLastRequestStatus(String lastRequestStatus) {
        this.lastRequestStatus = lastRequestStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Bindable
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Bindable
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void notifyAllFields() {
        notifyPropertyChanged(BR._all);

    }

    @Override
    public String toString() {
        return "ParentModel{" +
                "user='" + user + '\'' +
                ", lastRequestStatus='" + lastRequestStatus + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
