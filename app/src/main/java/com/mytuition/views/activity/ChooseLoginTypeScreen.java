package com.mytuition.views.activity;

import static com.mytuition.utility.AppConstant.USERS;
import static com.mytuition.utility.AppUtils.fadeIn;
import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.AppUtils.setString;
import static com.mytuition.utility.Utils.LOGIN_TYPE;
import static com.mytuition.utility.Utils.LOGIN_TYPE_PARENT;
import static com.mytuition.utility.Utils.LOGIN_TYPE_TEACHER;
import static com.mytuition.utility.Utils.setParentModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.firebase.ui.auth.AuthUI;
import com.mytuition.R;
import com.mytuition.databinding.ActivityChooseLoginTypeScreen2Binding;
import com.mytuition.models.ParentModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseLoginTypeScreen extends AppCompatActivity {
    private static final String TAG = "ChooseLoginTypeScreen";

    ActivityChooseLoginTypeScreen2Binding loginTypeScreenBinding;

    String loginType = null;

    public static Map<String, Object> getUserMap(String loginType) {
        Map<String, Object> map = new HashMap<>();
        map.put(LOGIN_TYPE, loginType);
        return map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginTypeScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_choose_login_type_screen2);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loginTypeScreenBinding.getRoot().setAnimation(fadeIn(this));
        loginTypeScreenBinding.btnLoginAsTeacher.setOnClickListener(view -> {

            //showMsg();
            loginUi(LOGIN_TYPE_TEACHER);

        });


        loginTypeScreenBinding.btnLoginAsParent.setOnClickListener(view -> loginUi(LOGIN_TYPE_PARENT));
    }

    public void showMsg() {

        new AlertDialog.Builder(ChooseLoginTypeScreen.this)
                .setTitle("Registration closed")
                .setMessage("Registration for Teachers is temporarily closed.\nDrop your mail on \n'aamirr.1232@gmail.com'\n for any enquiry.")
                .setPositiveButton("OK", (dialog, id) -> dialog.cancel()).show();
    }

    private void loginUi(String loginType) {
        loginTypeScreenBinding.progressBar.setVisibility(View.VISIBLE);
        this.loginType = loginType;
        showLoginScreen();


    }

    private void showLoginScreen() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.ic_launcher_foreground)
                        .setTheme(R.style.AppTheme_NoActionBar)
                        .setIsSmartLockEnabled(false)
                        .build(), 10);
    }

    /*    startActivity(new Intent(this, TeacherSignUpActivity.class));*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            loginTypeScreenBinding.progressBar.setVisibility(View.GONE);
            if (resultCode == RESULT_OK) {
                Toast.makeText(ChooseLoginTypeScreen.this, "sign in successfully", Toast.LENGTH_SHORT).show();
                setString(LOGIN_TYPE, loginType, ChooseLoginTypeScreen.this);
                if (loginType.equalsIgnoreCase(LOGIN_TYPE_TEACHER)) {
                    startActivity(new Intent(ChooseLoginTypeScreen.this, TeacherScreen.class)
                            .putExtra(LOGIN_TYPE, loginType));
                    finish();
                } else {
                    updateParentModel();
                }

            } else {
                Toast.makeText(ChooseLoginTypeScreen.this, "sign in failed", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void updateParentModel() {
        Log.d(TAG, "updateParentModel: " + getUid());
        getFirestoreReference().collection(USERS)
                .document(getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    ParentModel parentModel = documentSnapshot.toObject(ParentModel.class);
                    setParentModel(ChooseLoginTypeScreen.this, parentModel);
                    startActivity(new Intent(ChooseLoginTypeScreen.this, ParentScreen.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(ChooseLoginTypeScreen.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }
}