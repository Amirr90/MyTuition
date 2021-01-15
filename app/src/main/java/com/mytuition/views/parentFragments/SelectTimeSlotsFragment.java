package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.mytuition.models.CalendarModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mytuition.utility.AppUtils.getCurrentDateInWeekMonthDayFormat;
import static com.mytuition.utility.Utils.AddTimeSlot;

public class SelectTimeSlotsFragment extends Fragment {


    TeacherModel teacherModel = new TeacherModel();
    FragmentSelectTimeSlotsBinding slotsBinding;
    NavController navController;

    CalendarAdapter calendarAdapter;
    TimeSlotsAdapter slotsAdapter;
    String date = null;

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

        AddTimeSlot(teacherModel.getId());

        slotsBinding.tvCurrentDate.setText(getCurrentDateInWeekMonthDayFormat());


        calendarAdapter = new CalendarAdapter(getNextWeekDays(), new CalendarAdapter.CalenderInterface() {
            @Override
            public void onItemClicked(CalendarModel calendarModel, int pos) {
                date = SelectTimeSlotsFragment.this.getDateToSend(pos);
                SelectTimeSlotsFragment.this.getDocTimerSlot(date);

            }
        });

        slotsBinding.calRec.setAdapter(calendarAdapter);
        getDocTimerSlot(date);

        slotsBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_selectTimeSlotsFragment_to_requestTuitonFragment);

            }
        });

    }


    private List<CalendarModel> getNextWeekDays() {
        List<CalendarModel> calendarModelList = new ArrayList<>();
        ArrayList<HashMap<String, String>> getNextWeekDays = AppUtils.getNextWeekDays();
        for (int a = 0; a < getNextWeekDays.size(); a++) {
            calendarModelList.add(new CalendarModel(
                    getNextWeekDays.get(a).get("date"),
                    getNextWeekDays.get(a).get("day"),
                    getNextWeekDays.get(a).get("dateSend")));
        }

        return calendarModelList;
    }

    private String getDateToSend(int position) {
        List<CalendarModel> calendarModelList = new ArrayList<>();
        ArrayList<HashMap<String, String>> getNextWeekDays = AppUtils.getNextWeekDays();
        for (int a = 0; a < getNextWeekDays.size(); a++) {
            calendarModelList.add(new CalendarModel(
                    getNextWeekDays.get(a).get("date"),
                    getNextWeekDays.get(a).get("day"),
                    getNextWeekDays.get(a).get("dateSend")));
        }

        return calendarModelList.get(position).getDateSend();
    }

    private void getDocTimerSlot(String date) {
        // navController.navigate(R.id.action_selectTimeSlotsFragment_to_requestTuitonFragment);
    }
}