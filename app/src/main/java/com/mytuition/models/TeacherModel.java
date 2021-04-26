package com.mytuition.models;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class TeacherModel extends BaseObservable {


    private AcademicInformation academicInformation;
    private TeachingProfile teachingProfile;
    private String speciality;
    private boolean isActive;
    private String name;
    private String image;
    private String id;
    private List<TimeSlotModel> timeSlots;
    private String about;
    private Profile profile;

    public void setTimeSlots(List<TimeSlotModel> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public List<TimeSlotModel> getTimeSlots() {
        return timeSlots;
    }

    public void setAcademicInformation(AcademicInformation academicInformation) {
        this.academicInformation = academicInformation;
    }

    public void setTeachingProfile(TeachingProfile teachingProfile) {
        this.teachingProfile = teachingProfile;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getImage() {
        return image;
    }

    public AcademicInformation getAcademicInformation() {
        return academicInformation;
    }

    public TeachingProfile getTeachingProfile() {
        return teachingProfile;
    }

    public String getSpeciality() {

        return speciality;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAbout() {
        return about;
    }

    public Profile getProfile() {
        return profile;
    }

    public static class AcademicInformation {
        private String collegeName;
        private String graduation;
        private String scoreClassXII;
        private String postGraduation;
        private String schoolName;
        private String scoreClassX;
        private String highestEducation;

        public void setCollegeName(String collegeName) {
            this.collegeName = collegeName;
        }

        public void setGraduation(String graduation) {
            this.graduation = graduation;
        }

        public void setScoreClassXII(String scoreClassXII) {
            this.scoreClassXII = scoreClassXII;
        }

        public void setPostGraduation(String postGraduation) {
            this.postGraduation = postGraduation;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public void setScoreClassX(String scoreClassX) {
            this.scoreClassX = scoreClassX;
        }

        public void setHighestEducation(String highestEducation) {
            this.highestEducation = highestEducation;
        }

        public String getHighestEducation() {
            return highestEducation;
        }

        public String getCollegeName() {
            return collegeName;
        }

        public String getGraduation() {
            return graduation;
        }

        public String getScoreClassXII() {
            return scoreClassXII;
        }

        public String getPostGraduation() {
            return postGraduation;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public String getScoreClassX() {
            return scoreClassX;
        }

        @Override
        public String toString() {
            return "{" +
                    "collegeName='" + collegeName + '\'' +
                    ", graduation='" + graduation + '\'' +
                    ", scoreClassXII='" + scoreClassXII + '\'' +
                    ", postGraduation='" + postGraduation + '\'' +
                    ", schoolName='" + schoolName + '\'' +
                    ", scoreClassX='" + scoreClassX + '\'' +
                    ", highestEducation='" + highestEducation + '\'' +
                    '}';
        }
    }

    public static class TeachingProfile {
        public List<String> teachingSubject;
        public long perVisitFee;
        public long monthlyFee;
        public String expertIn;
        public String experience;
        public Boolean demoClass;


        public List<String> getTeachingSubject() {
            return teachingSubject;
        }

        public Boolean getDemoClass() {
            return demoClass;
        }

        public long getPerVisitFee() {
            return perVisitFee;
        }

        public long getMonthlyFee() {
            return monthlyFee;
        }

        public String getExpertIn() {
            return expertIn;
        }

        public String getExperience() {
            return experience;
        }

        @Override
        public String toString() {
            return "{" +
                    "teachingSubject=" + teachingSubject +
                    ", perVisitFee=" + perVisitFee +
                    ", monthlyFee=" + monthlyFee +
                    ", expertIn='" + expertIn + '\'' +
                    ", experience='" + experience + '\'' +
                    ", demoClass=" + demoClass +
                    '}';
        }
    }

    public static class Profile {
        public String name;
        public String address;
        public String state;
        public String mobile;
        public String city;
        public String fatherName;
        public String aadharFrontImage;
        public String aadharBackImage;

        @Override
        public String toString() {
            return "{" +
                    "name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    ", state='" + state + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", city='" + city + '\'' +
                    ", fatherName='" + fatherName + '\'' +
                    ", aadharFrontImage='" + aadharFrontImage + '\'' +
                    ", aadharBackImage='" + aadharBackImage + '\'' +
                    '}';
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setFatherName(String fatherName) {
            this.fatherName = fatherName;
        }

        public String getAadharFrontImage() {
            return aadharFrontImage;
        }

        public void setAadharFrontImage(String aadharFrontImage) {
            this.aadharFrontImage = aadharFrontImage;
        }

        public String getAadharBackImage() {
            return aadharBackImage;
        }

        public void setAadharBackImage(String aadharBackImage) {
            this.aadharBackImage = aadharBackImage;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public String getState() {
            return state;
        }

        public String getMobile() {
            return mobile;
        }

        public String getCity() {
            return city;
        }

        public String getFatherName() {
            return fatherName;
        }
    }


    public static DiffUtil.ItemCallback<TeacherModel> itemCallback = new DiffUtil.ItemCallback<TeacherModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull TeacherModel oldItem, @NonNull TeacherModel newItem) {
            return oldItem.name.equalsIgnoreCase(newItem.name);
        }

        @Override
        public boolean areContentsTheSame(@NonNull TeacherModel oldItem, @NonNull TeacherModel newItem) {
            return oldItem.speciality.equalsIgnoreCase(newItem.speciality);
        }
    };

    @Override
    public String toString() {
        return "{" +
                "academicInformation=" + academicInformation +
                ", teachingProfile=" + teachingProfile +
                ", speciality='" + speciality + '\'' +
                ", isActive=" + isActive +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", id='" + id + '\'' +
                ", timeSlots=" + timeSlots +
                ", about='" + about + '\'' +
                ", profile=" + profile +
                '}';
    }

    public static class TimeSlotModel {

        String type;
        List<String> slots;

        public TimeSlotModel(String type, List<String> slots) {
            this.type = type;
            this.slots = slots;
        }

        public TimeSlotModel() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "{" +
                    "type='" + type + '\'' +
                    ", slots=" + slots +
                    '}';
        }

        public List<String> getSlots() {
            return slots;
        }

        public void setSlots(List<String> slots) {
            this.slots = slots;
        }
    }
}
