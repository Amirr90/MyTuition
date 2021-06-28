package com.mytuition.utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.mytuition.models.ParentModel;
import com.mytuition.models.TeacherModel;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.MediaStore.Files.FileColumns.PARENT;
import static com.mytuition.utility.AppConstant.USERS;
import static com.mytuition.utility.AppUtils.MY_PREFS_NAME;
import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.views.activity.ChooseLoginTypeScreen.getUserMap;


public class Utils {
    public static Map<LocalTime, Boolean> slots = new HashMap();

    private static final String TAG = "Utils";

    public static final String LOGIN_TYPE = "loginType";
    public static final String LOGIN_TYPE_PARENT = "parent";
    public static final String LOGIN_TYPE_TEACHER = "teacher";
    private static final String TEACHER_TIMING = "TeacherTiming";


    public static void updateUI(final String loginType) {
        if (getUid() == null) {
            return;
        }

        getFirestoreReference().collection(USERS).document(getUid())
                .update(getUserMap(loginType))
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getLocalizedMessage()));

    }

    public static DatabaseReference getFirebaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getFirebaseReference(String child) {
        return FirebaseDatabase.getInstance().getReference().child(child);
    }

    public static TeacherModel getTeacherModel(String id) {
        TeacherModel teacherModel = new TeacherModel();
        teacherModel.setName("Teacher Name");
        /*   teacherModel.setExperience("5");*/
        teacherModel.setSpeciality("Computer");
      /*  teacherModel.setFee("500");
        teacherModel.setRating("5");
        teacherModel.setReview("500");*/
        teacherModel.setId(id);
        teacherModel.setImage("https://img.pngio.com/hd-teach-blogger-round-logo-png-transparent-png-image-download-teach-png-533_533.png");
        return teacherModel;
    }


    public static void AddTimeSlot(String id) {
        List<SlotTiming> timing = new ArrayList<>();
        for (int a = 0; a < 4; a++) {
            SlotTiming timing1 = new SlotTiming();
            timing1.setFrom("10:00 AM");
            timing1.setTo("11:00 AM");
            timing1.setDay("Monday");
            timing.add(timing1);
        }

        List<Slot> slotList = new ArrayList<>();

        Slot slot = new Slot();
        slot.setSlot("Morning");
        slot.setSlotTiming(timing);
        slotList.add(slot);


        Slot slot2 = new Slot();
        slot2.setSlot("Noon");
        slot2.setSlotTiming(timing);
        slotList.add(slot2);


        Slot slot3 = new Slot();
        slot3.setSlot("Evening");
        slot3.setSlotTiming(timing);
        slotList.add(slot3);


        Slot slot4 = new Slot();
        slot4.setSlot("Night");
        slot4.setSlotTiming(timing);
        slotList.add(slot4);

        TimeSlots timeSlots = new TimeSlots();
        timeSlots.setTiming(slotList);

       /* try {
            //getFirestoreReference().collection(TEACHER_TIMING).document(id).set(timeSlots());
        } catch (ParseException e) {
            e.printStackTrace();
        }*/


        initializeSlots();
        allocateSlots("16:00", "18:00");
        Log.d(TAG, "AddTimeSlot: " + slots.toString());
        //  client.allocateSlots("16:00", "18:00");

    }

    public static class TimeSlots {

        public List<Slot> timing;


        public List<Slot> getTiming() {
            return timing;
        }

        public void setTiming(List<Slot> timing) {
            this.timing = timing;
        }
    }

    public static class Slot {
        String slot;
        public List<SlotTiming> slotTiming;

        public List<SlotTiming> getSlotTiming() {
            return slotTiming;
        }

        public void setSlotTiming(List<SlotTiming> slotTiming) {
            this.slotTiming = slotTiming;
        }

        public String getSlot() {
            return slot;
        }

        public void setSlot(String slot) {
            this.slot = slot;
        }
    }

    public static class SlotTiming {
        String from;
        String to;
        String day;


        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }


    public static void initializeSlots() {
        LocalTime time = LocalTime.of(9, 0);
        slots.put(time, true);
        for (int i = 1; i < 24; i++) {
            slots.put(time.plusHours(i), true);
        }
    }

    private static void allocateSlots(String strTime, String edTime) {
        LocalTime startTime = LocalTime.parse(strTime);
        LocalTime endTime = LocalTime.parse(edTime);

        while (startTime.isBefore(endTime)) {
            //check if the time slots between start and end time are available
            if (!slots.get(startTime) || !slots.get(endTime)) {
                System.out.println("slots not available" + " start time: " + strTime + " end time: " + edTime);
                return;
            }
            startTime = startTime.plusHours(1);
            endTime = endTime.minusHours(1);
        }

        System.out.println("slots are available" + " start time: " + strTime + " end time: " + edTime);
        //then here u can mark all slots between to unavailable.
        startTime = LocalTime.parse(strTime);
        endTime = LocalTime.parse(edTime);
        while (startTime.isBefore(endTime)) {
            slots.put(startTime, false);
            slots.put(endTime, false);
            startTime = startTime.plusHours(1);
            endTime = endTime.minusHours(1);
        }
    }

    public static class TimeDemo {
        List<String> timeSlots;

        public TimeDemo(List<String> timeSlots) {
            this.timeSlots = timeSlots;
        }

        public List<String> getTimeSlots() {
            return timeSlots;
        }
    }


    public static ParentModel getParentModel(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString(PARENT, "");
        return gson.fromJson(json, ParentModel.class);
    }


    public static void setParentModel(Activity activity, ParentModel myObject) {
        SharedPreferences pref = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myObject);
        prefsEditor.putString(PARENT, json);
        prefsEditor.commit();
    }

    public static String[] getCityList() {
        return new String[]{"Lucknow", "Kanpur", "Sitapur", "Kakori"};
    }

    public static String[] getStateList() {
        return new String[]{"Uttar Pradesh"};
    }
}
