package com.mytuition.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mytuition.R;
import com.mytuition.databinding.ActivityChooseLoginTypeScreen2Binding;

import static com.mytuition.utility.AppUtils.fadeIn;
import static com.mytuition.utility.Utils.LOGIN_TYPE_PARENT;
import static com.mytuition.utility.Utils.LOGIN_TYPE_TEACHER;

public class ChooseLoginTypeScreen extends AppCompatActivity {

    ActivityChooseLoginTypeScreen2Binding loginTypeScreenBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginTypeScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_choose_login_type_screen2);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loginTypeScreenBinding.getRoot().setAnimation(fadeIn(this));
        loginTypeScreenBinding.btnLoginAsTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setting login Type As Teacher
                loginUi(LOGIN_TYPE_TEACHER);
            }
        });


        loginTypeScreenBinding.btnLoginAsParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setting login Type As Parents
                loginUi(LOGIN_TYPE_PARENT);
            }
        });
    }

    private void loginUi(String loginType) {
        //setString(LOGIN_TYPE, loginType, ChooseLoginTypeScreen.this);
        if (loginType.equalsIgnoreCase(LOGIN_TYPE_TEACHER)) {
            startActivity(new Intent(ChooseLoginTypeScreen.this, RegistrationActivity.class));
            finish();
        }

    }
}