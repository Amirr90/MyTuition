package com.mytuition.module;


import com.mytuition.interfaces.Api;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppUrl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class TeacherModule {

    @Singleton
    @Provides
    TeacherModel.Profile provideProfile() {
        return new TeacherModel.Profile();
    }

    @Singleton
    @Provides
    TeacherModel.AcademicInformation provideAcademicInformation() {
        return new TeacherModel.AcademicInformation();
    }

    @Singleton
    @Provides
    TeacherModel.TeachingProfile provideTeachingProfile() {
        return new TeacherModel.TeachingProfile();
    }

    @Singleton
    @Provides
    TeacherModel provideTeacherModel(TeacherModel.Profile profile, TeacherModel.AcademicInformation academicInformation, TeacherModel.TeachingProfile teachingProfile) {
        TeacherModel teacherModel = new TeacherModel();
        teacherModel.setTeachingProfile(teachingProfile);
        teacherModel.setProfile(profile);
        teacherModel.setAcademicInformation(academicInformation);
        return teacherModel;
    }


    @Singleton
    @Provides
    OkHttpClient.Builder provideHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);

        httpClient.addInterceptor(logging);
        httpClient.dispatcher(dispatcher);
        return httpClient;
    }


    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient.Builder builder) {
        return new Retrofit.Builder()
                .baseUrl(AppUrl.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
    }

    @Singleton
    @Provides
    Api provideApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }


}
