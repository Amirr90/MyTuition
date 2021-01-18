package com.mytuition.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.firebase.ui.auth.AuthUI;
import com.mytuition.R;
import com.mytuition.databinding.ActivityChooseLoginTypeScreen2Binding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mytuition.utility.AppConstant.TIMESTAMP;
import static com.mytuition.utility.AppConstant.UID;
import static com.mytuition.utility.AppUtils.fadeIn;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.AppUtils.setString;
import static com.mytuition.utility.Utils.LOGIN_TYPE;
import static com.mytuition.utility.Utils.LOGIN_TYPE_PARENT;
import static com.mytuition.utility.Utils.LOGIN_TYPE_TEACHER;

public class ChooseLoginTypeScreen extends AppCompatActivity {
    private static final String TAG = "ChooseLoginTypeScreen";

    ActivityChooseLoginTypeScreen2Binding loginTypeScreenBinding;

    String loginType = null;

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
                loginUi(LOGIN_TYPE_TEACHER);
            }
        });


        loginTypeScreenBinding.btnLoginAsParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUi(LOGIN_TYPE_PARENT);
            }
        });
    }

    private void loginUi(String loginType) {
        loginTypeScreenBinding.progressBar.setVisibility(View.VISIBLE);
        showLoginScreen();
        this.loginType = loginType;


    }

    private void showLoginScreen() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.ic_launcher_foreground)
                        .setTheme(R.style.AppTheme_NoActionBar)
                        .build(),
                10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 10) {
            loginTypeScreenBinding.progressBar.setVisibility(View.GONE);
            if (resultCode == RESULT_OK) {
                Toast.makeText(ChooseLoginTypeScreen.this, "sign in successfully", Toast.LENGTH_SHORT).show();
                setString(LOGIN_TYPE, loginType, ChooseLoginTypeScreen.this);
                if (loginType.equalsIgnoreCase(LOGIN_TYPE_TEACHER)) {
                    startActivity(new Intent(ChooseLoginTypeScreen.this, RegistrationActivity.class)
                            .putExtra(LOGIN_TYPE, loginType));
                } else
                    startActivity(new Intent(ChooseLoginTypeScreen.this, ParentScreen.class)
                            .putExtra(LOGIN_TYPE, loginType));
                finish();
            } else {
                Toast.makeText(ChooseLoginTypeScreen.this, "sign in failed", Toast.LENGTH_SHORT).show();

            }
        }
    }


    public static Map<String, Object> getUserMap(String loginType) {
        Map<String, Object> map = new HashMap<>();
        map.put(LOGIN_TYPE, loginType);
        return map;
    }
}