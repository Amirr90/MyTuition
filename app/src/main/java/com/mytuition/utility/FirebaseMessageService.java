package com.mytuition.utility;

import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mytuition.R;
import com.mytuition.interfaces.onSuccessListener;

public class FirebaseMessageService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessageService";
    MediaPlayer mp;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        AppUtils.updateToken(obj -> Log.d(TAG, "updated  !!"));
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: " + remoteMessage);
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.getData() + "...." + remoteMessage.getFrom());

        mp = MediaPlayer.create(getApplicationContext(), R.raw.notificationsound);
        mp.start();
    }
}
