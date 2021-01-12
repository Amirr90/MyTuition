package com.mytuition.models;

public class BannerModel {
    String sliderImages;


    public BannerModel(String sliderImages) {
        this.sliderImages = sliderImages;
    }

    public BannerModel() {
    }

    public void setSliderImages(String sliderImages) {
        this.sliderImages = sliderImages;
    }

    public String getSliderImages() {
        return sliderImages;
    }
}
