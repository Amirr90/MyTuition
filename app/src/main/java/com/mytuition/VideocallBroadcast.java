package com.mytuition;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.mytuition.utility.App;
import com.mytuition.views.activity.IncomingCallScreen;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class VideocallBroadcast extends BroadcastReceiver {
    private static final String TAG = "VideocallBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {

        showDemoCallNotification(context);
    }

    private void showDemoCallNotification(Context context) {
        Log.d(TAG, "showDemoCallNotification: ");



    }
}
