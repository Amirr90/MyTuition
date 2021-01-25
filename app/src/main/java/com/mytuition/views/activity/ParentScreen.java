package com.mytuition.views.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mytuition.R;
import com.mytuition.adapters.NavAdapter;
import com.mytuition.databinding.ActivityParentScreenBinding;
import com.mytuition.interfaces.NavigationInterface;
import com.mytuition.models.NavModel;
import com.mytuition.models.TuitionModel;
import com.mytuition.utility.AppUtils;
import com.mytuition.views.parentFragments.RequestTuitionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.mytuition.utility.AppUtils.getFirestoreReference;
import static com.mytuition.utility.AppUtils.getUid;
import static com.mytuition.utility.Utils.LOGIN_TYPE;
import static com.mytuition.utility.Utils.getFirebaseReference;
import static com.mytuition.utility.Utils.updateUI;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.PARENT_ID;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_ACCEPTED;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_PENDING;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_STATUS_REJECTED;
import static com.mytuition.views.parentFragments.RequestTuitionFragment.REQUEST_TUITION;

public class ParentScreen extends AppCompatActivity implements NavigationInterface {
    private static final String TAG = "ParentScreen";
    private static final String REQUEST_STATUS_CONFIRMED = "confirmed";

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

        updateRequestView();
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
                                if (null != tuitionModel)
                                    if (tuitionModel.getRequestStatus().equals(REQUEST_STATUS_CONFIRMED)) {
                                        mainBinding.constraintLayout.setVisibility(View.GONE);
                                    } else {
                                        mainBinding.constraintLayout.setVisibility(View.VISIBLE);
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