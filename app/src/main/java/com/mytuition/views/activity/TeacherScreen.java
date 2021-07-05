package com.mytuition.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.badge.BadgeDrawable;
import com.mytuition.R;
import com.mytuition.component.AppComponent;
import com.mytuition.databinding.ActivityTeacherScreenBinding;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.App;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.TeacherProfile;
import com.mytuition.views.SplashScreen;

import dagger.android.support.DaggerAppCompatActivity;

import static com.mytuition.utility.AppUtils.getJSONFromModel;
import static com.mytuition.utility.AppUtils.hideDialog;

public class TeacherScreen extends DaggerAppCompatActivity {
    private static final String TAG = "TeacherScreen";

    ActivityTeacherScreenBinding teacherScreenBinding;
    NavController navController;
    public static TeacherScreen instance;

    BadgeDrawable homeBadge;

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

    public void setBadge(int count) {
        homeBadge.setNumber(count);
    }

    @Override
    protected void onStart() {
        super.onStart();

        homeBadge = teacherScreenBinding.bottomNavigation.getOrCreateBadge(teacherScreenBinding.bottomNavigation.getMenu().findItem(R.id.home).getItemId());
        homeBadge.setVisible(true);
        homeBadge.setNumber(0);

        navController = Navigation.findNavController(this, R.id.nav_host_teacher);
        NavigationUI.setupActionBarWithNavController(this, navController);
        // hideToolbar(this);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (controller.getCurrentDestination().getId() == R.id.teacherDashboardFragment) {
                teacherScreenBinding.bottomNavigation.setVisibility(View.VISIBLE);
            } else teacherScreenBinding.bottomNavigation.setVisibility(View.GONE);
        });


        teacherScreenBinding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    homeBadge.setVisible(false);
                    homeBadge.clearNumber();
                    break;
                case R.id.page_2:
                    homeBadge.setVisible(true);
                    homeBadge.setNumber(20);
                    break;
                case R.id.page_5:
                    AppUtils.showRequestDialog(this);
                    TeacherProfile.getProfile(new ApiInterface() {
                        @Override
                        public void onSuccess(Object obj) {
                            hideDialog();
                            TeacherModel teacherModel = (TeacherModel) obj;
                            if (null != teacherModel) {
                                Log.d(TAG, "onSuccess: " + teacherModel.toString());
                                String model = getJSONFromModel(teacherModel);
                                Bundle bundle = new Bundle();
                                bundle.putString("teacherModel", model);
                                navController.navigate(R.id.demoFragment, bundle);
                            }

                        }

                        @Override
                        public void onFailed(String msg) {
                            hideDialog();
                            Toast.makeText(TeacherScreen.this, msg, Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.action_teacherDashboardFragment_to_demoFragment);
                        }
                    });
                    navController.navigate(R.id.demoFragment);
                    break;

            }
            return false;
        });
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

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        homeBadge = teacherScreenBinding.bottomNavigation.getOrCreateBadge(menu.findItem(R.id.home).getItemId());
        homeBadge.setVisible(true);
        homeBadge.setNumber(99);
        return super.onCreateOptionsMenu(menu);
    }*/

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