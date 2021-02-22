package com.mytuition.views.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mytuition.R;
import com.mytuition.databinding.ActivityTeacherScreenBinding;
import com.mytuition.utility.AppUtils;
import com.mytuition.views.SplashScreen;

import static com.mytuition.utility.AppUtils.hideDialog;
import static com.mytuition.utility.AppUtils.hideToolbar;

public class TeacherScreen extends AppCompatActivity {

    ActivityTeacherScreenBinding teacherScreenBinding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teacherScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_screen);
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
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                logout();
                            }
                        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();

    }

    private void logout() {
        AppUtils.showRequestDialog(TeacherScreen.this);
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        hideDialog();
                        Intent intent = new Intent(TeacherScreen.this, SplashScreen.class);
                        startActivity(intent);
                        Toast.makeText(TeacherScreen.this, "logged out successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}