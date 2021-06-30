package com.mytuition.component;

import android.app.Application;

import com.mytuition.module.ActivityBuilderModule;
import com.mytuition.utility.App;
import com.mytuition.views.activity.ParentScreen;
import com.mytuition.views.activity.TeacherScreen;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ActivityBuilderModule.class})
public interface AppComponent extends AndroidInjector<App> {

    void inject(TeacherScreen teacherScreen);

    void inject(ParentScreen screen);


    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
