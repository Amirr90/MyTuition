package com.mytuition.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mytuition.R;
import com.mytuition.utility.AppUtils;
import com.mytuition.views.activity.ChooseLoginTypeScreen;
import com.mytuition.views.activity.ParentScreen;
import com.mytuition.views.activity.TeacherScreen;

import static com.mytuition.utility.AppUtils.getCurrentUser;
import static com.mytuition.utility.Utils.LOGIN_TYPE;
import static com.mytuition.utility.Utils.LOGIN_TYPE_PARENT;
import static com.mytuition.utility.Utils.LOGIN_TYPE_TEACHER;

public class SplashScreen extends AppCompatActivity {
    String loginType = LOGIN_TYPE_PARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loginType = AppUtils.getString(LOGIN_TYPE, this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (getCurrentUser() != null) {
                    if (loginType.equalsIgnoreCase(LOGIN_TYPE_PARENT)) {
                        intent = new Intent(SplashScreen.this, ParentScreen.class);
                    } else if (loginType.equalsIgnoreCase(LOGIN_TYPE_TEACHER)) {
                        intent = new Intent(SplashScreen.this, TeacherScreen.class);
                    } else intent = new Intent(SplashScreen.this, ChooseLoginTypeScreen.class);
                } else {
                    intent = new Intent(SplashScreen.this, ChooseLoginTypeScreen.class);
                }
                SplashScreen.this.startActivity(intent);
                SplashScreen.this.finish();
            }
        }, 1000);
    }
}