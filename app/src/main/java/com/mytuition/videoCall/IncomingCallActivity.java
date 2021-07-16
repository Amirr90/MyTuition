package com.mytuition.videoCall;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mytuition.R;
import com.mytuition.databinding.ActivityIncomingCallBinding;

public class IncomingCallActivity extends AppCompatActivity {
    ActivityIncomingCallBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_incoming_call);

        binding.ivDeclineCall.setOnClickListener(v -> rejectCall());
        binding.ivAcceptCall.setOnClickListener(v -> acceptCall());
    }

    private void acceptCall() {
    }

    private void rejectCall() {
        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }
}