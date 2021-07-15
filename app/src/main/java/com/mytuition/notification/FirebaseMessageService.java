package com.mytuition.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mytuition.R;
import com.mytuition.VideocallBroadcast;
import com.mytuition.utility.App;
import com.mytuition.utility.AppConstant;
import com.mytuition.views.activity.ParentScreen;
import com.mytuition.views.activity.TeacherScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FirebaseMessageService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    final String CHANNEL_ID = "com.mytuition";
    Bitmap bitmap;
    Context context;
    int destinationId;
    private NotificationManager mManager;
    VideocallBroadcast videocallBroadcast;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceivedData: " + remoteMessage.getData());

        /*try {
            if (remoteMessage.getData().get("notificationType").equalsIgnoreCase("tuitionDetail"))
                showDemoCallNotification();
            else
                showNotification(remoteMessage.getData());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "onMessageReceived: error " + e.getLocalizedMessage());
        }*/

        try {
            showNotification(remoteMessage.getData());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onMessageReceivedError: " + e.getLocalizedMessage());
        }

    }

    private void showDemoCallNotification() {
        Intent intent = new Intent();
        intent.setClassName("com.mytuition", "com.mytuition.views.activity.IncomingCallScreen");
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        App.context.startActivity(intent);
    }

    private void showNotification(Map<String, String> data) {

        JSONObject json = new JSONObject(data);
        try {
            String image;
            String title = json.getString(AppConstant.NOTIFICATION_TITLE);
            String msg = json.getString(AppConstant.NOTIFICATION_BODY);
            String id = json.getString(AppConstant.NOTIFICATION_ID_);
            String notificationType = json.getString(AppConstant.NOTIFICATION_TYPE);
            if (null != json.getString(AppConstant.NOTIFICATION_IMAGE) && !json.getString(AppConstant.NOTIFICATION_IMAGE).isEmpty()) {
                image = json.getString(AppConstant.NOTIFICATION_IMAGE);
                URL url = new URL(image);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }

            Bundle bundle = new Bundle();

            if (notificationType.equalsIgnoreCase(AppConstant.NOTIFICATION_TUITION_DETAIL)) {
                destinationId = R.id.DetailsFragment2;
                bundle.putString(AppConstant.TUITION_ID, id);
            }

            PendingIntent pendingIntent = new NavDeepLinkBuilder(null == ParentScreen.getInstance() ? TeacherScreen.getInstance() : ParentScreen.getInstance())
                    .setGraph(R.navigation.parent_nav)
                    .setDestination(destinationId)
                    .setArguments(bundle)
                    .createPendingIntent();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel androidChannel = new NotificationChannel(CHANNEL_ID,
                        title, NotificationManager.IMPORTANCE_HIGH);
                androidChannel.enableLights(true);
                androidChannel.enableVibration(true);
                androidChannel.setLightColor(Color.GREEN);

                androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                getManager().createNotificationChannel(androidChannel);

                NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                if (bitmap != null) {
                    notification.setLargeIcon(bitmap);
                }

                int timestamp = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

                getManager().notify(timestamp, notification.build());

                playNotificationSound();

            } else {
                try {
                    playNotificationSound();
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(msg)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setLights(0xFF760193, 300, 1000)
                            .setVibrate(new long[]{200, 400});

                    if (bitmap != null) {
                        notificationBuilder.setLargeIcon(bitmap);
                    }
                    int timestamp = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(timestamp, notificationBuilder.build());
                } catch (SecurityException se) {
                    se.printStackTrace();
                    Log.d(TAG, "createNotification: " + se.getLocalizedMessage());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "showNotification: " + e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: " + s);
    }

    public void playNotificationSound() {
        try {
            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(this, notificationSoundUri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
}
