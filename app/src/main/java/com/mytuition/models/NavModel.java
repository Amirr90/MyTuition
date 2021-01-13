package com.mytuition.models;

public class NavModel {
    String title;
    int image;


    public NavModel(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "NavModel{" +
                "title='" + title + '\'' +
                ", image=" + image +
                '}';
    }


}
