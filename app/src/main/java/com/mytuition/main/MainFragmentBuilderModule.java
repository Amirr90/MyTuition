package com.mytuition.main;

import com.mytuition.TeacherTimeSlotsFragment;
import com.mytuition.views.parentFragments.TeacherDashboardFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract TeacherTimeSlotsFragment getTeacherTimeSlotFragment();

    @ContributesAndroidInjector
    abstract TeacherDashboardFragment teacherDashboardFragment();
}
