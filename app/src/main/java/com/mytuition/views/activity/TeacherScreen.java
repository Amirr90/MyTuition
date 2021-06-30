package com.mytuition.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.mytuition.R;
import com.mytuition.component.AppComponent;
import com.mytuition.databinding.ActivityTeacherScreenBinding;
import com.mytuition.utility.App;
import com.mytuition.utility.AppUtils;
import com.mytuition.views.SplashScreen;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

import static com.mytuition.utility.AppUtils.hideDialog;
import static com.mytuition.utility.AppUtils.hideToolbar;

public class TeacherScreen extends DaggerAppCompatActivity {

    ActivityTeacherScreenBinding teacherScreenBinding;
    NavController navController;
    public static TeacherScreen instance;

    public static TeacherScreen getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        instance = this;
        //initDependency();
        teacherScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_screen);
    }

    private void initDependency() {
        AppComponent appComponent = ((App) getApplication()).getAppComponent();
        appComponent.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        navController = Navigation.findNavController(this, R.id.nav_host_teacher);
        NavigationUI.setupActionBarWithNavController(this, navController);
        hideToolbar(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        try {
            AppUtils.hideSoftKeyboard(TeacherScreen.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return navController.navigateUp();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(TeacherScreen.this)
                .setMessage("Do you really want to logout?")
                .setIcon(R.drawable.ic_launcher_foreground)
                .setPositiveButton("YES",
                        (dialog, id) -> {
                            dialog.cancel();
                            logout();
                        })
                .setNegativeButton("NO", (dialog, id) -> dialog.cancel()).show();

    }

    private void logout() {
        AppUtils.showRequestDialog(TeacherScreen.this);
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    hideDialog();
                    Intent intent = new Intent(TeacherScreen.this, SplashScreen.class);
                    startActivity(intent);
                    Toast.makeText(TeacherScreen.this, "logged out successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}