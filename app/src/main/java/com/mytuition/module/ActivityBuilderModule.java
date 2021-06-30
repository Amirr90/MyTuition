package com.mytuition.module;


import com.mytuition.main.MainFragmentBuilderModule;
import com.mytuition.views.activity.TeacherScreen;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = {MainFragmentBuilderModule.class, TeacherModule.class})
    abstract TeacherScreen teacherScreen();


}
