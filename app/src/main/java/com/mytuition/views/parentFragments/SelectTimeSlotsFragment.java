package com.mytuition.views.parentFragments;

import android.os.Bundle;
import android.util.Log;
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
import com.mytuition.interfaces.AdapterInterface;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppConstant;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mytuition.RequestTuitionDetailFragment.TIME_SLOT;
import static com.mytuition.utility.AppUtils.getCurrentDateInWeekMonthDayFormat;
import static com.mytuition.utility.AppUtils.getNextWeekDays;
import static com.mytuition.utility.AppUtils.getSlots;

public class SelectTimeSlotsFragment extends Fragment implements AdapterInterface {


    public static final String TEACHER = "teacher";
    private static final String TAG = "SelectTimeSlotsFragment";
    TeacherModel teacherModel = new TeacherModel();
    FragmentSelectTimeSlotsBinding slotsBinding;
    NavController navController;
    CalendarAdapter calendarAdapter;
    TimeSlotsAdapter slotsAdapter;
    List<TeacherModel.TimeSlotModel> timeSlotsModelList;
    String classId = null;
    String date;

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
            Log.d(TAG, "onViewCreatedJson: " + jsonString);
            Gson gson = new Gson();
            teacherModel = gson.fromJson(jsonString, TeacherModel.class);
        }
        if (null != getArguments().getString("class")) {
            classId = getArguments().getString("class");
        }

        slotsBinding.layout1.setVisibility(null != classId ? View.GONE : View.VISIBLE);
        slotsBinding.setTeacher(teacherModel);

        // AddTimeSlot(teacherModel.getId());
        date = getNextWeekDays().get(0).getDateSend();

        getTimeSlots(0);
        slotsBinding.tvCurrentDate.setText(getCurrentDateInWeekMonthDayFormat());


        calendarAdapter = new CalendarAdapter(getNextWeekDays(), (calendarModel, pos) -> {
            getTimeSlots(pos);
            date = calendarModel.getDateSend();
        });

        slotsBinding.calRec.setAdapter(calendarAdapter);

        slotsBinding.btnConfirm.setOnClickListener(view1 -> {
        });


    }


    private void getTimeSlots(int pos) {
        setSlots();
    }

    private void setSlots() {
        timeSlotsModelList = new ArrayList<>();
        if (null != classId) {
            boolean b = false;
            timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Morning", getSlots(b, 6, 12)));
            timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Noon", getSlots(b, 12, 17)));
            timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Evening", getSlots(b, 17, 21)));
            timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Night", getSlots(b, 21, 24)));
            Log.d(TAG, "setSlots: " + getSlots(b, 21, 24));

        } else {
            try {
                List<TeacherModel.TimeSlotModel> slotModels = new ArrayList<>();
                Map<String, Object> slotsMap = teacherModel.getSlots();
                if (null == slotsMap)
                    return;
                Log.d("TAG", "addData: " + slotsMap.toString());
                if (slotsMap.containsKey("Morning")) {
                    ArrayList<String> slots = (ArrayList<String>) slotsMap.get("Morning");
                    if (!slots.isEmpty())
                        slotModels.add(new TeacherModel.TimeSlotModel("Morning", slots));
                }
                if (slotsMap.containsKey("Noon")) {
                    ArrayList<String> slots = (ArrayList<String>) slotsMap.get("Noon");
                    if (!slots.isEmpty())
                        slotModels.add(new TeacherModel.TimeSlotModel("Noon", slots));
                }
                if (slotsMap.containsKey("Evening")) {
                    ArrayList<String> slots = (ArrayList<String>) slotsMap.get("Evening");
                    if (!slots.isEmpty())
                        slotModels.add(new TeacherModel.TimeSlotModel("Evening", slots));
                }
                if (slotsMap.containsKey("Night")) {
                    ArrayList<String> slots = (ArrayList<String>) slotsMap.get("Night");
                    if (!slots.isEmpty())
                        slotModels.add(new TeacherModel.TimeSlotModel("Night", slots));
                }
                timeSlotsModelList.addAll(slotModels);
            } catch (Exception e) {
                e.printStackTrace();
                boolean b = false;
                timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Morning", getSlots(b, 6, 12)));
                timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Noon", getSlots(b, 12, 17)));
                timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Evening", getSlots(b, 17, 21)));
                timeSlotsModelList.add(new TeacherModel.TimeSlotModel("Night", getSlots(b, 21, 24)));
                Log.d(TAG, "setSlots: " + getSlots(b, 21, 24));
            }

        }
        slotsAdapter = new TimeSlotsAdapter(timeSlotsModelList, this);
        slotsBinding.timingRec.setAdapter(slotsAdapter);


    }


    @Override
    public void onItemClicked(Object o) {
        Bundle bundle = new Bundle();
        String timeSlot = (String) o;
        bundle.putString(TIME_SLOT, timeSlot);
        bundle.putString(AppConstant.DATE, date);

        Gson gson = new Gson();
        String jsonString = gson.toJson(teacherModel);

        if (null == classId) {
            try {
                JSONObject request = new JSONObject(jsonString);
                bundle.putString(AppConstant.TEACHER_MODEL, request.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bundle.putString("class", classId);

        }
        navController.navigate(R.id.action_selectTimeSlotsFragment_to_requestTuitonFragment, bundle);


    }
}