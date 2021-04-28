package com.mytuition.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.mytuition.R;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;
import com.mytuition.views.activity.ChooseLoginTypeScreen;
import com.mytuition.views.activity.ParentScreen;
import com.mytuition.views.activity.TeacherScreen;

import static com.mytuition.utility.AppUtils.getCurrentUser;
import static com.mytuition.utility.Utils.LOGIN_TYPE;
import static com.mytuition.utility.Utils.LOGIN_TYPE_PARENT;
import static com.mytuition.utility.Utils.LOGIN_TYPE_TEACHER;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "SplashScreen";
    String loginType = LOGIN_TYPE_PARENT;
    String notificationId, notificationType;

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
                    getNotificationData();
                    if (loginType.equalsIgnoreCase(LOGIN_TYPE_PARENT)) {
                        intent = new Intent(SplashScreen.this, ParentScreen.class)
                                .putExtra(AppConstant.NOTIFICATION_TYPE, notificationType)
                                .putExtra(AppConstant.NOTIFICATION_ID, notificationId);
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

    public void getNotificationData() {
        if (null != getIntent().getExtras()) {
            for (String key : getIntent().getExtras().keySet()) {
                if (key.equals("id")) {
                    Log.d(TAG, "id " + getIntent().getExtras().getString(key));
                    notificationId = getIntent().getExtras().getString(key);
                }
                if (key.equals("notificationType")) {
                    Log.d(TAG, "notificationType " + getIntent().getExtras().getString(key));
                    notificationType = getIntent().getExtras().getString(key);
                }
            }

        } else Log.d(TAG, "getData: null");
    }
}