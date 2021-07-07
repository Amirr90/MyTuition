package com.mytuition;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.mytuition.databinding.FragmentAllotTeacherBinding;
import com.mytuition.databinding.UserListViewBinding;
import com.mytuition.models.TeacherModel;
import com.mytuition.utility.AppConstant;
import com.mytuition.utility.AppUtils;
import com.mytuition.views.activity.ParentScreen;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class AllotTeacherFragment extends Fragment {
    private static final String TAG = "AllotTeacherFragment";

    FragmentAllotTeacherBinding binding;
    NavController navController;
    TeacherAdapter adapter;
    List<TeacherModel> teacherModelList;
    String tuitionId;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllotTeacherBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        if (getArguments() == null)
            return;

        tuitionId = AllotTeacherFragmentArgs.fromBundle(getArguments()).getId();


        binding.textInputLayout2.setEndIconOnClickListener(v -> {
            CharSequence text = Objects.requireNonNull(binding.nameMobile.getText()).toString().trim();
            if (text.length() > 0)
                searchTeachers(text);
            else
                Toast.makeText(requireActivity(), "Enter Name or number to search !!", Toast.LENGTH_SHORT).show();
        });
        teacherModelList = new ArrayList<>();
        adapter = new TeacherAdapter(teacherModelList);
        binding.recTeacherList.setAdapter(adapter);

        getAllTeacherData();
    }

    private void getAllTeacherData() {
        final Query query = AppUtils.getFirestoreReference().collection(AppConstant.TEACHER)
                .orderBy(AppConstant.TIMESTAMP, Query.Direction.DESCENDING)
                .limit(25);
        query.get().addOnCompleteListener(task -> {
            AppUtils.hideDialog();
            addData(task);
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getLocalizedMessage()));
    }

    private void searchTeachers(CharSequence text) {

        AppUtils.hideSoftKeyboard(requireActivity());
        boolean isDigit = TextUtils.isDigitsOnly(text);
        AppUtils.showRequestDialog(requireActivity());
        final Query query = AppUtils.getFirestoreReference().collection(AppConstant.TEACHER)
                .orderBy(isDigit ? AppConstant.PHONE_NUMBER : AppConstant.NAME)
                .startAt(text)
                .endAt(text + "\uf8ff");


        query.get().addOnCompleteListener(task -> {
            AppUtils.hideDialog();
            addData(task);
        });

    }

    private void addData(Task<QuerySnapshot> task) {
        teacherModelList.clear();
        for (DocumentSnapshot snapshot : task.getResult()) {
            TeacherModel model = snapshot.toObject(TeacherModel.class);
            teacherModelList.add(model);
            Log.d(TAG, "Added: " + model.getName());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

    }

    private void AllotTeacher(String teacherId, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put(AppConstant.TEACHER_ID, teacherId);
        map.put(AppConstant.NAME, name);

        AppUtils.showRequestDialog(requireActivity());
        AppUtils.getFirestoreReference().collection(AppConstant.REQUEST_TUITION)
                .document(tuitionId)
                .update(map).addOnSuccessListener(aVoid -> {
            AppUtils.hideDialog();
            Toast.makeText(requireActivity(), "Teacher Allotted Successfully !!", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        }).addOnFailureListener(e -> {
            AppUtils.hideDialog();
            Toast.makeText(requireActivity(), "Something went wrong, try again !!", Toast.LENGTH_SHORT).show();
        });
    }

    private class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.MyViewHolder> {
        List<TeacherModel> list;

        public TeacherAdapter(List<TeacherModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public TeacherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            UserListViewBinding userListViewBinding = UserListViewBinding.inflate(inflater, parent, false);
            return new MyViewHolder(userListViewBinding);

        }

        @Override
        public void onBindViewHolder(@NonNull TeacherAdapter.MyViewHolder holder, final int position) {
            holder.userListViewBinding.setTeacher(list.get(position));

            holder.userListViewBinding.btnAllotTeacher.setOnClickListener(v -> {
                String teacherId = list.get(position).getId();
                String name = list.get(position).getName();
                AllotTeacher(teacherId, name);
            });

            holder.userListViewBinding.mainLayout.setOnClickListener(v -> {
                Gson gson = new Gson();
                String jsonString = gson.toJson(list.get(position));
                try {
                    JSONObject request = new JSONObject(jsonString);
                    Bundle bundle = new Bundle();
                    bundle.putString("docModel", request.toString());
                    ParentScreen.getInstance().navigate(R.id.action_allotTeacherFragment_to_teacherProfileFragment, bundle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public int getItemCount() {
            return null == list ? 0 : list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            UserListViewBinding userListViewBinding;

            public MyViewHolder(@NonNull UserListViewBinding userListViewBinding) {
                super(userListViewBinding.getRoot());
                this.userListViewBinding = userListViewBinding;
            }
        }
    }
}