package com.mytuition.views.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.mytuition.R;
import com.mytuition.databinding.ActivityParentScreenBinding;
import com.mytuition.utility.AppUtils;

import java.util.Objects;

import static com.mytuition.utility.Utils.LOGIN_TYPE;
import static com.mytuition.utility.Utils.updateUI;

public class ParentScreen extends AppCompatActivity {
    private static final String TAG = "ParentScreen";

    ActivityParentScreenBinding mainBinding;
    NavController navController;

    public static ParentScreen instance;

    public static ParentScreen getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_parent_screen);
        instance = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        navController = Navigation.findNavController(this, R.id.nav_host_parent);
        NavigationUI.setupActionBarWithNavController(this, navController);
        Objects.requireNonNull(getSupportActionBar()).hide();
        updateUI(getIntent().getStringExtra(LOGIN_TYPE));
    }


    @Override
    public boolean onSupportNavigateUp() {
        try {
            AppUtils.hideSoftKeyboard(ParentScreen.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return navController.navigateUp();
    }


    public void navigate(int id) {
        navController.navigate(id);
    }

    public void navigate(int id, Bundle bundle) {
        navController.navigate(id, bundle);
    }
}