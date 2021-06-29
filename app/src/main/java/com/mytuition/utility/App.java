package com.mytuition.utility;

import android.app.Application;

import com.mytuition.component.AppComponent;
import com.mytuition.component.DaggerAppComponent;
import com.mytuition.module.TeacherModule;

public class App extends Application {

    public static App context;
    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .teacherModule(new TeacherModule())
                .build();

        context = this;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
