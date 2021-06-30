package com.mytuition.utility;

import com.mytuition.component.AppComponent;
import com.mytuition.component.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class App extends DaggerApplication {

    public static App context;
    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
