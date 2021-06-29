package com.mytuition.component;

import com.mytuition.module.TeacherModule;
import com.mytuition.views.activity.ParentScreen;
import com.mytuition.views.activity.TeacherScreen;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = TeacherModule.class)
public interface AppComponent {

    void inject(TeacherScreen teacherScreen);

    void inject(ParentScreen screen);

}
