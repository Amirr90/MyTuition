package com.mytuition.views.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mytuition.R;
import com.mytuition.adapters.NavAdapter;
import com.mytuition.databinding.ActivityParentScreenBinding;
import com.mytuition.interfaces.NavigationInterface;
import com.mytuition.models.NavModel;
import com.mytuition.models.ParentModel;
import com.mytuition.models.RequestTuitionModel;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.GetAddressIntentService;
import com.mytuition.views.SplashScreen;
import com.mytuition.views.parentFragments.ParentDashboardFragment;
import com.mytuition.views.parentFragments.ParentDashboardFragmentDirections;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getMobileNumber;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.AppUtils.hideDialog;
import static com.mytuition.utility.Utils.LOGIN_TYPE;
import static com.mytuition.utility.Utils.setParentModel;
import static com.mytuition.utility.Utils.updateUI;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_ACCEPTED;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_PENDING;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_REJECTED;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_TUITION;

public class ParentScreen extends AppCompatActivity implements NavigationInterface {
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 2020;
    private static final String TAG = "ParentScreen";
    private static final String REQUEST_STATUS_CONFIRMED = "confirmed";
    public static ParentScreen instance;
    public String cityName;
    public String areaName;
    ActivityParentScreenBinding mainBinding;
    NavController navController;
    NavAdapter navAdapter;
    List<NavModel> navModels;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationAddressResultReceiver addressResultReceiver;
    private Location currentLocation;
    private LocationCallback locationCallback;

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

        getNotificationData();

        addressResultReceiver = new LocationAddressResultReceiver(new Handler());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLocation = locationResult.getLocations().get(0);
                Log.d(TAG, "onLocationResult: " + currentLocation);
                getAddress();
            }
        };
        startLocationUpdates();


        updateUI(getIntent().getStringExtra(LOGIN_TYPE));

        if (getUid() != null) {
            updateUserInfo();
        }
        setNavAdapter();


        mainBinding.constraintLayout.setOnClickListener(v -> navController.navigate(R.id.DetailsFragment2));

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // mainBinding.constraintLayout.setVisibility(destination.getId() == R.id.DetailsFragment2 ? View.GONE : View.VISIBLE);
        });


        mainBinding.navView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            mainBinding.drawerLayout.close();
            if (item.getItemId() == R.id.itemTuitionList) {
                navController.navigate(R.id.tuitionListFragment);
            } else if (item.getItemId() == R.id.itemAboutUs) {
                navController.navigate(R.id.aboutUsFragment);
            } else if (item.getItemId() == R.id.itemTuitionList) {
                navController.navigate(R.id.tuitionRequestForAdminFragment);
            } else if (item.getItemId() == R.id.itemAllTuition) {
                AppUtils.shareApp(instance);
            } else if (item.getItemId() == R.id.itemLogout)
                showLogoutDialog();
            else Toast.makeText(instance, "coming soon !!", Toast.LENGTH_SHORT).show();
            return true;
        });


        //mainBinding.

    }

    private void updateUserInfo() {
        if (null != getUid())
            getFirestoreReference().collection(AppConstant.USERS).document(getUid()).addSnapshotListener((value, error) -> {
                if (error == null && null != value) {
                    ParentModel user = value.toObject(ParentModel.class);
                    if (null != user)
                        user.setMobile(getMobileNumber());
                    mainBinding.setUser(user);
                    setParentModel(ParentScreen.this, user);

                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateToken();
        // updateRequestView();
    }

    public void openDrawer() {
        mainBinding.drawerLayout.open();
    }

    public void closeDrawer() {
        mainBinding.drawerLayout.close();
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                            String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(0);
            locationRequest.setFastestInterval(0);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void updateRequestView() {

        if (null != getUid())
            getFirestoreReference().collection(REQUEST_TUITION)
                    .document(getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            RequestTuitionModel tuitionModel = documentSnapshot.toObject(RequestTuitionModel.class);
                            if (null != tuitionModel) {
                                if (tuitionModel.getReqStatus().equals(REQUEST_STATUS_CONFIRMED)) {
                                    mainBinding.constraintLayout.setVisibility(View.GONE);
                                }
                            } else {
                                switch (tuitionModel.getReqStatus()) {
                                    case REQUEST_STATUS_PENDING:
                                        mainBinding.textView9.setText("Waiting for Teacher to accept your Demo Class");
                                        break;
                                    case REQUEST_STATUS_ACCEPTED:
                                        //mainBinding.textView9.setText(tuitionModel.getTeacherModel().getName() + " Accepted your tuition request");
                                        mainBinding.btLoading.setAnimation(R.raw.accepted);
                                        mainBinding.btLoading.playAnimation();
                                        break;
                                    case REQUEST_STATUS_REJECTED:
                                        //mainBinding.textView9.setText(tuitionModel.getTeacherModel().getName() + " Rejected your tuition request");
                                        mainBinding.btLoading.setAnimation(R.raw.rejected);
                                        mainBinding.btLoading.playAnimation();
                                        break;
                                }
                            }


                        }
                    }).addOnFailureListener(e -> mainBinding.constraintLayout.setVisibility(View.GONE));

    }

    private void setNavAdapter() {
        navModels = new ArrayList<>();
        navAdapter = new NavAdapter(navModels, ParentScreen.this);
        mainBinding.navRec.setAdapter(navAdapter);
        loadNavData();
    }

    private void loadNavData() {
        navModels.add(new NavModel(getString(R.string.tuition_reqiest), R.drawable.ic_baseline_control_camera_24));
        navModels.add(new NavModel(getString(R.string.about_us), R.drawable.ic_baseline_textsms_24));
        navModels.add(new NavModel(getString(R.string.share_app), R.drawable.ic_baseline_share_24));
        navModels.add(new NavModel(getString(R.string.logout), R.drawable.ic_baseline_logout_24));
        if (null != getUid()) {
            switch (getUid()) {
                case AppConstant.ADMIN_UID:
                case AppConstant.ADMIN_UID2:
                    navModels.add(new NavModel(getString(R.string.requests), R.drawable.ic_baseline_verified_user_24));
            }
        }


        navAdapter.notifyDataSetChanged();
    }

    private void getAddress() {
        if (!Geocoder.isPresent()) {
            Toast.makeText(ParentScreen.this, "Can't find current address, ", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, GetAddressIntentService.class);
        intent.putExtra("add_receiver", addressResultReceiver);
        intent.putExtra("add_location", currentLocation);
        startService(intent);

    }

    public void getNotificationData() {

        if (!getIntent().hasExtra(AppConstant.NOTIFICATION_ID))
            return;

        String notificationId = getIntent().getStringExtra(AppConstant.NOTIFICATION_ID);
        String notificationType = getIntent().getStringExtra(AppConstant.NOTIFICATION_TYPE);

        Log.d(TAG, "getNotificationData: notificationId " + notificationId);
        Log.d(TAG, "getNotificationData: notificationType " + notificationType);

        if (null != notificationId && null != notificationType) {
            if (notificationType.equalsIgnoreCase(AppConstant.NOTIFICATION_TUITION_DETAIL)) {
                ParentDashboardFragmentDirections.ActionParentDashboardFragment2ToDetailsFragment2 action = ParentDashboardFragmentDirections.actionParentDashboardFragment2ToDetailsFragment2();
                action.setTuitionId(notificationId);
                navController.navigate(action);
            }
        }
    }

    public void showResults(String currentAdd, String lat, String lng) {

        final String[] address = currentAdd.split(",");

        try {
            setAreaName(address[1]);
            setCityName(address[0]);
            ParentDashboardFragment.getInstance().updateLocation(address[0], address[1]);
            Log.d(TAG, "showResults: " + address[0]);


        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "showResults: Exception " + e.getLocalizedMessage());
        }
    }

    public String getCityName() {
        if (cityName == null)
            return "";
        else return cityName;

    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        if (areaName == null)
            return "";
        else return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

        if (navModels.get(pos).getTitle().equalsIgnoreCase(getString(R.string.tuition_reqiest))) {
            navController.navigate(R.id.tuitionListFragment);
        } else if (navModels.get(pos).getTitle().equalsIgnoreCase(getString(R.string.about_us))) {
            navController.navigate(R.id.aboutUsFragment);
        } else if (navModels.get(pos).getTitle().equalsIgnoreCase(getString(R.string.requests))) {
            navController.navigate(R.id.tuitionRequestForAdminFragment);
        } else if (navModels.get(pos).getTitle().equalsIgnoreCase(getString(R.string.share_app))) {
            AppUtils.shareApp(instance);
        } else if (navModels.get(pos).getTitle().equalsIgnoreCase(getString(R.string.logout)))
            showLogoutDialog();
        else Toast.makeText(instance, "coming soon !!", Toast.LENGTH_SHORT).show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(ParentScreen.this)
                .setMessage("Do you really want to logout?")
                .setIcon(R.drawable.app_icon)
                .setPositiveButton("Yes",
                        (dialog, id) -> {
                            dialog.cancel();
                            logout();
                        })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel()).show();

    }

    private void logout() {
        AppUtils.showRequestDialog(ParentScreen.this);
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    hideDialog();
                    Intent intent = new Intent(ParentScreen.this, SplashScreen.class);
                    startActivity(intent);
                    Toast.makeText(ParentScreen.this, "logged out successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void updateToken() {
        AppUtils.updateToken(obj -> Log.d(TAG, "updateToken: " + (String) obj));
    }

    private class LocationAddressResultReceiver extends ResultReceiver {
        LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == 0) {
                Log.d("Address", "Location null retrying");
                getAddress();
            }

            if (resultCode == 1) {
                Toast.makeText(ParentScreen.this, "Address not found, ", Toast.LENGTH_SHORT).show();
            }

            String currentAdd = resultData.getString("address_result");
            String lat = resultData.getString("lat");
            String lng = resultData.getString("lng");
            Log.d(TAG, "onReceiveResult: " + currentAdd);
            showResults(currentAdd, lat, lng);

        }
    }
}