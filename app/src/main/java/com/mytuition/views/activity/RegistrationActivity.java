package com.mytuition.views.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mytuition.R;
import com.mytuition.databinding.ActivityRegistrationBinding;
import com.mytuition.models.Registration;

import static com.mytuition.utility.Utils.LOGIN_TYPE;
import static com.mytuition.utility.Utils.updateUI;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding registrationBinding;
    Registration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registration = new Registration();
        registrationBinding.setRegistration(registration);

        updateUI(getIntent().getStringExtra(LOGIN_TYPE));

        registrationBinding.btnRegister.setOnClickListener(view -> {
            startActivity(new Intent(RegistrationActivity.this, TeacherScreen.class));
            finish();
        });
    }
}