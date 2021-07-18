package com.mytuition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.mytuition.databinding.FragmentSearchTeacherBinding;
import com.mytuition.utility.AppUtils;

public class SearchTeacherFragment extends Fragment {


    FragmentSearchTeacherBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_teacher, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtils.showToolbar(requireActivity());
    }
}