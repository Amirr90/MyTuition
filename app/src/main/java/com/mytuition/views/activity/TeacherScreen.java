package com.mytuition.views.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mytuition.R;
import com.mytuition.databinding.ActivityTeacherScreenBinding;

public class TeacherScreen extends AppCompatActivity {

    ActivityTeacherScreenBinding teacherScreenBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teacherScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}