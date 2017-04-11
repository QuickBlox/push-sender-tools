package com.quickblox.push_sender_tools.ui.activities;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.push_sender_tools.App;
import com.quickblox.push_sender_tools.R;
import com.quickblox.push_sender_tools.utils.Consts;
import com.quickblox.push_sender_tools.utils.ErrorUtils;
import com.quickblox.push_sender_tools.utils.VersionUtils;
import com.quickblox.push_sender_tools.utils.configs.ConfigUtils;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class SplashActivity extends BaseActivity {
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_splash);

        TextView appNameTextView = _findViewById(R.id.text_splash_app_title);
        TextView versionTextView = _findViewById(R.id.text_splash_app_version);

        appNameTextView.setText(R.string.app_title);
        versionTextView.setText(getString(R.string.splash_app_version, VersionUtils.getAppVersionName()));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = getIntent().getExtras().getString(Consts.EXTRA_GCM_MESSAGE);
        }

        if (checkConfigsWithSnackebarError()){
            signInQB();
        }
    }

    private void signInQB() {
        if (!checkSignIn()) {
            QBUser qbUser = ConfigUtils.getUserFromConfig(Consts.CONFIG_FILE_NAME);

            QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    proceedToTheNextActivity();
                }

                @Override
                public void onError(QBResponseException e) {
                    showSnackbarError(null, R.string.splash_create_session_error, e, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            signInQB();
                        }
                    });
                }
            });
        } else {
            proceedToTheNextActivity();
        }
    }

    protected void proceedToTheNextActivity() {
        MessagesActivity.start(this, message);
        finish();
    }

    protected boolean appConfigIsCorrect() {
        boolean result = App.getInstance().getAppConfigs() != null;
        result = result && ConfigUtils.getUserFromConfig(Consts.CONFIG_FILE_NAME) != null;
        return result;
    }

    protected boolean checkSignIn() {
        return QBSessionManager.getInstance().getSessionParameters() != null;
    }

    protected boolean checkConfigsWithSnackebarError(){
        if (!appConfigIsCorrect()){
            showSnackbarErrorParsingConfigs();
            return false;
        }

        return true;
    }

    @Override
    protected void showSnackbarError(View rootLayout, @StringRes int resId, QBResponseException e, View.OnClickListener clickListener) {
        rootLayout = findViewById(R.id.layout_root);
        ErrorUtils.showSnackbar(rootLayout, resId, e, R.string.dlg_retry, clickListener);
    }

    protected void showSnackbarErrorParsingConfigs(){
        ErrorUtils.showSnackbar(findViewById(R.id.layout_root), R.string.error_parsing_configs, R.string.dlg_ok, null);
    }

}