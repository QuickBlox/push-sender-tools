package com.quickblox.push_sender_tools.services;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.quickblox.messages.services.gcm.QBGcmPushListenerService;
import com.quickblox.push_sender_tools.utils.Consts;

public class GcmPushListenerService extends QBGcmPushListenerService {

    private static final int NOTIFICATION_ID = 1;
    private final static String TAG = GcmPushListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Intent intent = new Intent(Consts.ACTION_NEW_GCM_PUSH_RECEIVED);
        data.putString("from", from);
        intent.putExtras(data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}