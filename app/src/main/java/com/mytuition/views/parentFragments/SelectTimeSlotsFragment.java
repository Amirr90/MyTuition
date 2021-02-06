package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.mytuition.R;
import com.mytuition.adapters.CalendarAdapter;
import com.mytuition.adapters.TimeSlotsAdapter;
import com.mytuition.databinding.FragmentSelectTimeSlotsBinding;
import com.mytuition.interfaces.AdapterInterface;
import com.mytuition.models.CalendarModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.models.TimeSlotModel;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import static com.mytuition.utility.AppUtils.getCurrentDateInWeekMonthDayFormat;
import static com.mytuition.utility.AppUtils.getNextWeekDays;
import static com.mytuition.utility.AppUtils.getSlots;
import static com.mytuition.utility.AppUtils.sdfFromTimeStamp;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.TIME_SLOT;

public class SelectTimeSlotsFragment extends Fragment implements AdapterInterface {


    public static final String TEACHER = "teacher";
    TeacherModel teacherModel = new TeacherModel();
    FragmentSelectTimeSlotsBinding slotsBinding;
    NavController navController;

    CalendarAdapter calendarAdapter;
    TimeSlotsAdapter slotsAdapter;
    List<TimeSlotModel> timeSlotsModelList;
    private static final String TAG = "SelectTimeSlotsFragment";
    String classId = null;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        slotsBinding = FragmentSelectTimeSlotsBinding.inflate(getLayoutInflater());
        return slotsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        if (null == getArguments())
            return;

        if (null != getArguments().getString("docModel")) {
            String jsonString = getArguments().getString("docModel");
            Gson gson = new Gson();
            teacherModel = gson.fromJson(jsonString, TeacherModel.class);
        }
        if (null != getArguments().getString("class")) {
            classId = getArguments().getString("class");
        }

        slotsBinding.layout1.setVisibility(null != classId ? View.GONE : View.VISIBLE);
        slotsBinding.setTeacher(teacherModel);

        // AddTimeSlot(teacherModel.getId());
        getTimeSlots(0);
        slotsBinding.tvCurrentDate.setText(getCurrentDateInWeekMonthDayFormat());


        calendarAdapter = new CalendarAdapter(getNextWeekDays(), new CalendarAdapter.CalenderInterface() {
            @Override
            public void onItemClicked(CalendarModel calendarModel, int pos) {
                getTimeSlots(pos);
            }
        });

        slotsBinding.calRec.setAdapter(calendarAdapter);

        slotsBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


    private void getTimeSlots(int pos) {
        setSlots();
    }

    private void setSlots() {
        boolean b = false;
        timeSlotsModelList = new ArrayList<>();
        if (null != classId) {
            timeSlotsModelList.add(new TimeSlotModel("Morning", getSlots(b, 7, 12)));
            timeSlotsModelList.add(new TimeSlotModel("Noon", getSlots(b, 12, 17)));
            timeSlotsModelList.add(new TimeSlotModel("Evening", getSlots(b, 17, 21)));
            timeSlotsModelList.add(new TimeSlotModel("Night", getSlots(b, 21, 24)));
        } else {
            timeSlotsModelList.add(new TimeSlotModel("Morning", getSlots(b, 7, 10)));
            timeSlotsModelList.add(new TimeSlotModel("Noon", getSlots(b, 14, 17)));
            timeSlotsModelList.add(new TimeSlotModel("Evening", getSlots(b, 18, 21)));
            timeSlotsModelList.add(new TimeSlotModel("Night", getSlots(b, 21, 24)));
        }

        slotsAdapter = new TimeSlotsAdapter(timeSlotsModelList, this);
        slotsBinding.timingRec.setAdapter(slotsAdapter);
    }


    @Override
    public void onItemClicked(Object o) {
        Bundle bundle = new Bundle();
        String timeSlot = (String) o;
        bundle.putString(TIME_SLOT, timeSlot);
        if (null == classId) {
            bundle.putString(TEACHER, teacherModel.toString());
        } else {
            bundle.putString("class", classId);

        }
        navController.navigate(R.id.action_selectTimeSlotsFragment_to_requestTuitonFragment, bundle);


    }
}