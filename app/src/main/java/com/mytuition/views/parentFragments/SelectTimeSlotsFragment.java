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

        String jsonString = getArguments().getString("docModel");
        Gson gson = new Gson();
        teacherModel = gson.fromJson(jsonString, TeacherModel.class);

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
        timeSlotsModelList.add(new TimeSlotModel("Morning", getSlots(b, 7, 10)));
        timeSlotsModelList.add(new TimeSlotModel("Noon", getSlots(b, 14, 17)));
        timeSlotsModelList.add(new TimeSlotModel("Evening", getSlots(b, 18, 21)));
        timeSlotsModelList.add(new TimeSlotModel("Night", getSlots(b, 21, 24)));
        slotsAdapter = new TimeSlotsAdapter(timeSlotsModelList, this);
        slotsBinding.timingRec.setAdapter(slotsAdapter);
    }

    private List<TimeSlotModel.TimeDetails> getSlots(boolean b, int i, int i1) {
        List<TimeSlotModel.TimeDetails> s1 = new ArrayList<>();
        ArrayList<String> results = getTimeSet(b, i, i1, 65);
        for (String s : results)
            s1.add(new TimeSlotModel.TimeDetails(s, false));
        return s1;
    }

    private ArrayList<String> getTimeSet(boolean isCurrentDay, int from, int to, int duration) {
        double hrs = (float) duration / 60;
        Log.d(TAG, "getTimeSet: hrs " + (int) ((to - from) / hrs));
        ArrayList results = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);// what should be the default?
        if (!isCurrentDay)
            calendar.set(Calendar.HOUR_OF_DAY, from);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        int count = (int) ((to - from) / hrs);
        for (int i = 0; i < count; i++) {

            String day1 = sdf.format(calendar.getTime());
            calendar.add(Calendar.MINUTE, duration);

            String day2 = sdf.format(calendar.getTime());

            String day = day1 + " - " + day2;

            results.add(i, day);

        }
        return results;
    }


    private List<CalendarModel> getNextWeekDays() {
        List<CalendarModel> calendarModelList = new ArrayList<>();
        ArrayList<HashMap<String, String>> getNextWeekDays = AppUtils.getNextWeekDays();
        for (int a = 1; a < getNextWeekDays.size(); a++) {
            calendarModelList.add(new CalendarModel(
                    getNextWeekDays.get(a).get("date"),
                    getNextWeekDays.get(a).get("day"),
                    getNextWeekDays.get(a).get("dateSend")));
        }

        return calendarModelList;
    }

    @Override
    public void onItemClicked(Object o) {

        String timeSlot = (String) o;
        Bundle bundle = new Bundle();
        bundle.putString(TIME_SLOT, timeSlot);
        bundle.putString(TEACHER, teacherModel.toString());
        navController.navigate(R.id.action_selectTimeSlotsFragment_to_requestTuitonFragment, bundle);
    }
}