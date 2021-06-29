package com.mytuition.module;


import com.mytuition.models.TeacherModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
}
