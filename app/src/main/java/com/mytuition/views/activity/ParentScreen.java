package com.mytuition.views.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.mytuition.R;
import com.mytuition.adapters.NavAdapter;
import com.mytuition.databinding.ActivityParentScreenBinding;
import com.mytuition.interfaces.NavigationInterface;
import com.mytuition.models.NavModel;
import com.mytuition.utility.AppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mytuition.utility.Utils.LOGIN_TYPE;
import static com.mytuition.utility.Utils.updateUI;

public class ParentScreen extends AppCompatActivity implements NavigationInterface {
    private static final String TAG = "ParentScreen";

    ActivityParentScreenBinding mainBinding;
    NavController navController;

    NavAdapter navAdapter;
    List<NavModel> navModels;

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

        setNavAdapter();
    }

    private void setNavAdapter() {
        navModels = new ArrayList<>();
        navAdapter = new NavAdapter(navModels, ParentScreen.this);
        mainBinding.navRec.setAdapter(navAdapter);
        loadNavData();
    }

    private void loadNavData() {
        navModels.add(new NavModel(getString(R.string.app_name), R.drawable.ic_launcher_foreground));
        navModels.add(new NavModel(getString(R.string.app_name), R.drawable.ic_launcher_foreground));
        navModels.add(new NavModel(getString(R.string.app_name), R.drawable.ic_launcher_foreground));
        navModels.add(new NavModel(getString(R.string.app_name), R.drawable.ic_launcher_foreground));
        navModels.add(new NavModel(getString(R.string.app_name), R.drawable.ic_launcher_foreground));
        navModels.add(new NavModel(getString(R.string.app_name), R.drawable.ic_launcher_foreground));

        navAdapter.notifyDataSetChanged();
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

    @Override
    public void onNavigationItemClicked(int pos) {
        mainBinding.drawerLayout.close();
        Toast.makeText(instance, "Position " + pos, Toast.LENGTH_SHORT).show();
    }
}