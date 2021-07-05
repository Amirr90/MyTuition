package com.mytuition.models;

public class BannerAddModel {
    String image;
    String body;
    String headline;
    String callToAction;
    String advertiser;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getCallToAction() {
        return callToAction;
    }

    public void setCallToAction(String callToAction) {
        this.callToAction = callToAction;
    }

    public String getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
    }

    public BannerAddModel(String image, String body, String headline, String callToAction, String advertiser) {
        this.image = image;
        this.body = body;
        this.headline = headline;
        this.callToAction = callToAction;
        this.advertiser = advertiser;
    }
}
