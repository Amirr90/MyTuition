package com.mytuition.utility;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mytuition.adapters.SpecialityAdapter;
import com.mytuition.interfaces.DatabaseCallbackInterface;
import com.mytuition.models.SpecialityModel;
import com.mytuition.models.TeacherModel;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static com.mytuition.adapters.DashboardPatientAdapter1.SPECIALITY;
import static com.mytuition.utility.AppConstant.USERS;
import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.views.activity.ChooseLoginTypeScreen.getUserMap;

public class Utils {
    private static final String TAG = "Utils";

    public static final String LOGIN_TYPE = "login_type";
    public static final String LOGIN_TYPE_PARENT = "parent";
    public static final String LOGIN_TYPE_TEACHER = "teacher";
    private static final String TEACHER_TIMING = "TeacherTiming";


    public static void updateUI(final String loginType) {
        if (getUid() == null) {
            return;
        }

        getFirestoreReference().collection(USERS).document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    getFirestoreReference().collection(USERS).document(getUid()).update(getUserMap(loginType))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                }
                            });
                } else
                    getFirestoreReference().collection(USERS).document(getUid()).set(getUserMap(loginType))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                }
                            });
            }
        });

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
        teacherModel.setExperience("5");
        teacherModel.setSpeciality("Computer");
        teacherModel.setFee("500");
        teacherModel.setRating("5");
        teacherModel.setReview("500");
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
        slot.setSlot("Noon");
        slot.setSlotTiming(timing);
        slotList.add(slot2);

        Slot slo3 = new Slot();
        slot.setSlot("Evening");
        slot.setSlotTiming(timing);
        slotList.add(slo3);


        TimeSlots timeSlots = new TimeSlots();
        timeSlots.setTiming(slotList);

        getFirestoreReference().collection(TEACHER_TIMING).document(id).set(timeSlots);

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
}
