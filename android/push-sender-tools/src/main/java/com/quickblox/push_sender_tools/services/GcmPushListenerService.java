package com.quickblox.push_sender_tools.services;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.quickblox.messages.services.gcm.QBGcmPushListenerService;
import com.quickblox.push_sender_tools.R;
import com.quickblox.push_sender_tools.ui.activities.SplashActivity;
import com.quickblox.push_sender_tools.utils.ActivityLifecycle;
import com.quickblox.push_sender_tools.utils.Consts;
import com.quickblox.push_sender_tools.utils.NotificationUtils;
import com.quickblox.push_sender_tools.utils.ResourceUtils;

public class GcmPushListenerService extends QBGcmPushListenerService {

    private static final int NOTIFICATION_ID = 1;
    private final static String TAG = GcmPushListenerService.class.getSimpleName();

    @Override
    public void sendPushMessage(Bundle data, String from, String message) {
        super.sendPushMessage(data, from, message);
        Log.v(TAG, "From: " + from);
        Log.v(TAG, "Message: " + message);

        if (ActivityLifecycle.getInstance().isBackground()) {
            showNotification(message);
        } else {
            Intent intent = new Intent(Consts.ACTION_NEW_GCM_PUSH_RECEIVED);
            data.putString("message", message);
            data.putString("from", from);
            intent.putExtras(data);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    protected void showNotification(String message) {
        NotificationUtils.showNotification(this, SplashActivity.class,
                ResourceUtils.getString(R.string.notification_title), message,
                R.mipmap.ic_launcher, NOTIFICATION_ID);
    }
}