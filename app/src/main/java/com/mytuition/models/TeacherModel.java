package com.mytuition.models;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class TeacherModel extends BaseObservable {


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
    private AcademicInformation academicInformation;
    private TeachingProfile teachingProfile;
    private String speciality;
    private boolean isActive;
    private boolean isProfileFilled;
    private boolean availableForDemoClass;
    private boolean availableForSunday;
    private boolean verified;
    private boolean online;
    private String name;
    private String image;
    private String id;
    private List<TimeSlotModel> timeSlots;
    private Map<String, Object> slots;
    private String about;
    private String phoneNumber;
    private Profile profile;
    private long timestamp;


    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public TeacherModel() {
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Map<String, Object> getSlots() {
        return slots;
    }

    public void setSlots(Map<String, Object> slots) {
        this.slots = slots;
    }

    @Override
    public String toString() {
        return "{" +
                "academicInformation=" + academicInformation +
                ", teachingProfile=" + teachingProfile +
                ", speciality='" + speciality + '\'' +
                ", isActive=" + isActive +
                ", isProfileFilled=" + isProfileFilled +
                ", availableForDemoClass=" + availableForDemoClass +
                ", availableForSunday=" + availableForSunday +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", id='" + id + '\'' +
                ", timeSlots=" + timeSlots +
                ", about='" + about + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profile=" + profile +
                ", timestamp=" + timestamp +
                '}';
    }

    public boolean isAvailableForDemoClass() {
        return availableForDemoClass;
    }

    public void setAvailableForDemoClass(boolean availableForDemoClass) {
        this.availableForDemoClass = availableForDemoClass;
    }

    public boolean isAvailableForSunday() {
        return availableForSunday;
    }

    public void setAvailableForSunday(boolean availableForSunday) {
        this.availableForSunday = availableForSunday;
    }

    public boolean isProfileFilled() {
        return isProfileFilled;
    }

    public void setProfileFilled(boolean profileFilled) {
        isProfileFilled = profileFilled;
    }

    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<TimeSlotModel> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlotModel> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public String getImage() {
        return null == image ? "" : image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public AcademicInformation getAcademicInformation() {
        return academicInformation;
    }

    public void setAcademicInformation(AcademicInformation academicInformation) {
        this.academicInformation = academicInformation;
    }

    public TeachingProfile getTeachingProfile() {
        return teachingProfile;
    }

    public void setTeachingProfile(TeachingProfile teachingProfile) {
        this.teachingProfile = teachingProfile;
    }

    public String getSpeciality() {

        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public static class AcademicInformation {
        private String collegeName;
        private String graduation;
        private String scoreClassXII;
        private String postGraduation;
        private String schoolName;
        private String scoreClassX;
        private String highestEducation;
        private String otherCertificate;

        @Inject
        public AcademicInformation() {
        }

        public String getOtherCertificate() {
            return otherCertificate;
        }

        public void setOtherCertificate(String otherCertificate) {
            this.otherCertificate = otherCertificate;
        }

        public String getHighestEducation() {
            return highestEducation;
        }

        public void setHighestEducation(String highestEducation) {
            this.highestEducation = highestEducation;
        }

        public String getCollegeName() {
            return collegeName;
        }

        public void setCollegeName(String collegeName) {
            this.collegeName = collegeName;
        }

        public String getGraduation() {
            return graduation;
        }

        public void setGraduation(String graduation) {
            this.graduation = graduation;
        }

        public String getScoreClassXII() {
            return scoreClassXII;
        }

        public void setScoreClassXII(String scoreClassXII) {
            this.scoreClassXII = scoreClassXII;
        }

        public String getPostGraduation() {
            return postGraduation;
        }

        public void setPostGraduation(String postGraduation) {
            this.postGraduation = postGraduation;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getScoreClassX() {
            return scoreClassX;
        }

        public void setScoreClassX(String scoreClassX) {
            this.scoreClassX = scoreClassX;
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
                    ", otherCertificate='" + otherCertificate + '\'' +
                    '}';
        }
    }

    public static class TeachingProfile {
        public List<String> teachingSubject;
        public String perVisitFee;
        public String monthlyFee;
        public long tuitions;
        public String expertIn;
        public String experience;
        public String tuitionHours;
        public Boolean demoClass;


        @Inject
        public TeachingProfile() {
        }

        public long getTuitions() {
            return tuitions;
        }

        public void setTuitions(long tuitions) {
            this.tuitions = tuitions;
        }

        public String getTuitionHours() {
            return tuitionHours;
        }

        public void setTuitionHours(String tuitionHours) {
            this.tuitionHours = tuitionHours;
        }

        public List<String> getTeachingSubject() {
            return teachingSubject;
        }

        public void setTeachingSubject(List<String> teachingSubject) {
            this.teachingSubject = teachingSubject;
        }

        public Boolean getDemoClass() {
            return demoClass;
        }

        public void setDemoClass(Boolean demoClass) {
            this.demoClass = demoClass;
        }

        public String getPerVisitFee() {
            return perVisitFee;
        }

        public void setPerVisitFee(String perVisitFee) {
            this.perVisitFee = perVisitFee;
        }

        public String getMonthlyFee() {
            return monthlyFee;
        }

        public void setMonthlyFee(String monthlyFee) {
            this.monthlyFee = monthlyFee;
        }

        public String getExpertIn() {
            return expertIn;
        }

        public void setExpertIn(String expertIn) {
            this.expertIn = expertIn;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        @Override
        public String toString() {
            return "{" +
                    "teachingSubject=" + teachingSubject +
                    ", perVisitFee='" + perVisitFee + '\'' +
                    ", monthlyFee='" + monthlyFee + '\'' +
                    ", tuitions=" + tuitions +
                    ", expertIn='" + expertIn + '\'' +
                    ", experience='" + experience + '\'' +
                    ", tuitionHours='" + tuitionHours + '\'' +
                    ", demoClass=" + demoClass +
                    '}';
        }
    }

    public static class Profile {
        public String name;
        public String email;
        public String address;
        public String landMark;
        public String state;
        public String mobile;
        public String city;
        public String fatherName;
        public String aadharFrontImage;
        public String aadharBackImage;
        public Boolean verified;

        @Inject
        public Profile() {
        }

        public String getLandMark() {
            return landMark;
        }

        public void setLandMark(String landMark) {
            this.landMark = landMark;

        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;

        }

        public Boolean getVerified() {
            return verified;
        }

        public void setVerified(Boolean verified) {
            this.verified = verified;
        }

        @Override
        public String toString() {
            return "{" +
                    "name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", address='" + address + '\'' +
                    ", landMark='" + landMark + '\'' +
                    ", state='" + state + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", city='" + city + '\'' +
                    ", fatherName='" + fatherName + '\'' +
                    ", aadharFrontImage='" + aadharFrontImage + '\'' +
                    ", aadharBackImage='" + aadharBackImage + '\'' +
                    ", verified=" + verified +
                    '}';
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

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;

        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
            // notifyPropertyChanged(BR.sta);
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getFatherName() {
            return fatherName;
        }

        public void setFatherName(String fatherName) {
            this.fatherName = fatherName;
        }
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
