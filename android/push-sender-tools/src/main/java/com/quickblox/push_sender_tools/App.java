package com.quickblox.push_sender_tools;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.ServiceZone;
import com.quickblox.messages.services.QBPushManager;
import com.quickblox.push_sender_tools.models.AppConfigs;
import com.quickblox.push_sender_tools.utils.ActivityLifecycle;
import com.quickblox.push_sender_tools.utils.Consts;
import com.quickblox.push_sender_tools.utils.configs.ConfigUtils;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();
    private static App instance;
    private AppConfigs appConfigs;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ActivityLifecycle.init(this);
        initAppConfigs();
        initQBCredentials();
        initPushManager();
    }

    public static synchronized App getInstance() {
        return instance;
    }

    private void initAppConfigs() {
        Log.e(TAG, "QB CONFIG FILE NAME: " + Consts.CONFIG_FILE_NAME);
        appConfigs = ConfigUtils.getAppConfigsOrNull(Consts.CONFIG_FILE_NAME);
    }

    public void initQBCredentials(){
        if (appConfigs != null) {
            QBSettings.getInstance().init(getApplicationContext(), appConfigs.getAppId(), appConfigs.getAuthKey(), appConfigs.getAuthSecret());
            QBSettings.getInstance().setAccountKey(appConfigs.getAccountKey());

            if (!TextUtils.isEmpty(appConfigs.getApiDomain()) && !TextUtils.isEmpty(appConfigs.getChatDomain())) {
                QBSettings.getInstance().setEndpoints(appConfigs.getApiDomain(), appConfigs.getChatDomain(), ServiceZone.PRODUCTION);
                QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
            }
        }
    }

    public AppConfigs getAppConfigs(){
        return appConfigs;
    }

    private void initPushManager() {
        QBPushManager.getInstance().addListener(new QBPushManager.QBSubscribeListener() {
            @Override
            public void onSubscriptionCreated() {
                Toast.makeText(App.this, "Subscription Created", Toast.LENGTH_LONG).show();
                Log.d(TAG, "SubscriptionCreated");
            }

            @Override
            public void onSubscriptionError(Exception e, int resultCode) {
                Log.d(TAG, "SubscriptionError" + e.getLocalizedMessage());
                if (resultCode >= 0) {
                    String error = GoogleApiAvailability.getInstance().getErrorString(resultCode);
                    Log.d(TAG, "SubscriptionError playServicesAbility: " + error);
                }
                Toast.makeText(App.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}