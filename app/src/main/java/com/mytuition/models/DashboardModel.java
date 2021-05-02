package com.mytuition.models;

import java.util.List;

public class DashboardModel {
    private BannerData bannerData;
    private List<TeacherModel> teacherList;
    private List<CoachingModel> coachingsData;
    private List<TestimonialsModel> testimonialsList;

    public List<TestimonialsModel> getTestimonialsList() {
        return testimonialsList;
    }

    public BannerData getBannerData() {
        return bannerData;
    }

    public List<TeacherModel> getTeacherList() {
        return teacherList;
    }

    public List<CoachingModel> getCoachingsData() {
        return coachingsData;
    }

    public static class BannerData {
        Banner Banner;

        public Banner getBanner() {
            return Banner;
        }
    }
}
