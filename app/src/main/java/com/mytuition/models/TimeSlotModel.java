package com.mytuition.models;

import java.util.List;

public class TimeSlotModel {
    String slotType;
    String type;
    List<TimeDetails> timeDetails;
    List<TimeDetails> slots;

    @Override
    public String toString() {
        return "{" +
                "slotType='" + slotType + '\'' +
                ", type='" + type + '\'' +
                ", timeDetails=" + timeDetails +
                ", slots=" + slots +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TimeDetails> getSlots() {
        return slots;
    }

    public void setSlots(List<TimeDetails> slots) {
        this.slots = slots;
    }

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

        @Override
        public String toString() {
            return "{" +
                    "slotTime='" + slotTime + '\'' +
                    ", booked=" + booked +
                    '}';
        }

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
