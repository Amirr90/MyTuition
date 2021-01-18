package com.mytuition.models;

import java.util.List;

public class TimeSlotModel {
    String slotType;
    List<TimeDetails> timeDetails;

    public TimeSlotModel(String slotType, List<TimeDetails> timeDetails) {
        this.slotType = slotType;
        this.timeDetails = timeDetails;
    }

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }

    public List<TimeDetails> getTimeDetails() {
        return timeDetails;
    }

    public void setTimeDetails(List<TimeDetails> timeDetails) {
        this.timeDetails = timeDetails;
    }

    public static class TimeDetails {
        String slotTime;
        Boolean booked;

        public TimeDetails(String slotTime, Boolean booked) {
            this.slotTime = slotTime;
            this.booked = booked;
        }

        public Boolean getBooked() {
            return booked;
        }

        public String getSlotTime() {
            return slotTime;
        }
    }

}
