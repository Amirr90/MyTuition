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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mytuition.R;
import com.mytuition.adapters.NavAdapter;
import com.mytuition.databinding.ActivityParentScreenBinding;
import com.mytuition.interfaces.NavigationInterface;
import com.mytuition.models.NavModel;
import com.mytuition.models.TuitionModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.utility.GetAddressIntentService;
import com.mytuition.views.parentFragments.ParentDashboardFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.Utils.LOGIN_TYPE;
import static com.mytuition.utility.Utils.updateUI;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_ACCEPTED;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_PENDING;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_REJECTED;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_TUITION;

public class ParentScreen extends AppCompatActivity implements NavigationInterface {
    private static final String TAG = "ParentScreen";
    private static final String REQUEST_STATUS_CONFIRMED = "confirmed";
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 2020;

    ActivityParentScreenBinding mainBinding;
    NavController navController;

    NavAdapter navAdapter;
    List<NavModel> navModels;

    public String cityName;
    public String areaName;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationAddressResultReceiver addressResultReceiver;
    private Location currentLocation;
    private LocationCallback locationCallback;


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
        setNavAdapter();


        mainBinding.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.DetailsFragment2);
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                mainBinding.constraintLayout.setVisibility(destination.getId() == R.id.DetailsFragment2 ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRequestView();
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                            String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void updateRequestView() {

        if (null != getUid())
            getFirestoreReference().collection(REQUEST_TUITION)
                    .document(getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                TuitionModel tuitionModel = documentSnapshot.toObject(TuitionModel.class);
                                if (null != tuitionModel) {
                                    if (tuitionModel.getRequestStatus().equals(REQUEST_STATUS_CONFIRMED)) {
                                        mainBinding.constraintLayout.setVisibility(View.GONE);
                                    }
                                } else {
                                    if (tuitionModel.getRequestStatus().equals(REQUEST_STATUS_PENDING)) {
                                        mainBinding.textView9.setText("Waiting for Teacher to accept your Demo Class");
                                    } else if (tuitionModel.getRequestStatus().equals(REQUEST_STATUS_ACCEPTED)) {
                                        mainBinding.textView9.setText(tuitionModel.getTeacherModel().getName() + " Accepted your tuition request");
                                        mainBinding.btLoading.setAnimation(R.raw.accepted);
                                        mainBinding.btLoading.playAnimation();
                                    } else if (tuitionModel.getRequestStatus().equals(REQUEST_STATUS_REJECTED)) {
                                        mainBinding.textView9.setText(tuitionModel.getTeacherModel().getName() + " Rejected your tuition request");
                                        mainBinding.btLoading.setAnimation(R.raw.rejected);
                                        mainBinding.btLoading.playAnimation();
                                    }
                                }


                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mainBinding.constraintLayout.setVisibility(View.GONE);
                }
            });

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
        Toast.makeText(instance, "Position " + pos, Toast.LENGTH_SHORT).show();
    }
}