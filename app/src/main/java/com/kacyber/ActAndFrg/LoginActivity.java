package com.kacyber.ActAndFrg;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.kacyber.R;
import com.kacyber.Utils.Constants;
import com.kacyber.View.MainActivity;
import com.kacyber.event.LoginSuccessEvent;
import com.kacyber.model.LoginSuccessModel;
import com.kacyber.network.http.KacyberRestClientUsage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = LoginActivity.class.getName();
    private AQuery aQuery;
    private TextView termText;
    private AccessToken accessToken;
    public static int APP_REQUEST_CODE = 99;
    private static final int FRAMEWORK_REQUEST_CODE = 99;
    private int nextPermissionsRequestCode = 4000;

    private final Map<Integer, OnCompleteListener> permissionsListeners = new HashMap<>();

    private interface OnCompleteListener {
        void onComplete();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);
        aQuery = new AQuery(this);
        aQuery.id(R.id.login_response).clickable(true).clicked(this);
        aQuery.id(R.id.login_facebook_response).clickable(true).clicked(this);
        aQuery.id(R.id.login_google_response).clickable(true).clicked(this);
        aQuery.id(R.id.not_now).clickable(true).clicked(this);
        aQuery.id(R.id.terms_and_conditions).clickable(true).clicked(this);
        termText = (TextView) findViewById(R.id.terms_and_conditions);
        termText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        termText.getPaint().setAntiAlias(true);

        accessToken = AccountKit.getCurrentAccessToken();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_response:
//                Log.e(TAG, "login response clicked");
//                Intent intent = new Intent();
//                intent.setClass(this, SingUpActivity.class);
//                startActivity(intent);

                onLoginPhone(v);

                break;

            case R.id.login_facebook_response:
                onLoginPhone(v);
                break;
            case R.id.login_google_response:
                onLoginPhone(v);
                break;
            case R.id.not_now:
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.terms_and_conditions:
                Intent termsIntent = new Intent();
                termsIntent.setClass(this, WebViewActivity.class);
                termsIntent.putExtra("title", "Terms");
                termsIntent.putExtra("url", Constants.TERMS);

            default:
                break;

        }
    }

    public void onLoginPhone(final View view) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        final AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        final AccountKitConfiguration configuration = configurationBuilder.build();
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configuration);

//        startActivityForResult(intent, APP_REQUEST_CODE);
            OnCompleteListener completeListener = new OnCompleteListener() {
                @Override
                public void onComplete() {
                    Log.e(TAG, "start facebook sms login request");
                    startActivityForResult(intent, FRAMEWORK_REQUEST_CODE);
                }
            };

            if (configuration.isReceiveSMSEnabled()) {
            final OnCompleteListener receiveSMSCompleteListener = completeListener;
            completeListener = new OnCompleteListener() {
                @Override
                public void onComplete() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                                Manifest.permission.RECEIVE_SMS,
                                R.string.permissions_receive_sms_title,
                                R.string.permissions_receive_sms_message,
                                receiveSMSCompleteListener);
                    }
                }
            };
        }
        if (configuration.isReadPhoneStateEnabled()) {
            final OnCompleteListener readPhoneStateCompleteListener = completeListener;
            completeListener = new OnCompleteListener() {
                @Override
                public void onComplete() {
                    requestPermissions(
                            Manifest.permission.READ_PHONE_STATE,
                            R.string.permissions_read_phone_state_title,
                            R.string.permissions_read_phone_state_message,
                            readPhoneStateCompleteListener);
                }
            };
        }

        completeListener.onComplete();

    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != APP_REQUEST_CODE) {
            return;
        }

        final String toastMessage;
        final AccountKitLoginResult loginResult = AccountKit.loginResultWithIntent(data);
        if (loginResult == null || loginResult.wasCancelled()) {
            toastMessage = "Login Cancelled";
        } else if (loginResult.getError() != null) {
            toastMessage = loginResult.getError().getErrorType().getMessage();
//            final Intent intent = new Intent(this, ErrorActivity.class);
//            intent.putExtra(ErrorActivity.HELLO_TOKEN_ACTIVITY_ERROR_EXTRA, loginResult.getError());
//
//            startActivity(intent);
        } else {
            final AccessToken accessToken = loginResult.getAccessToken();
            Log.e(TAG, "accessToken is " + accessToken.getToken());
            Log.e(TAG, "currentAccessToken is " + AccountKit.getCurrentAccessToken());

            Log.e(TAG, "loginResult is " + loginResult);
            final long tokenRefreshIntervalInSeconds =
                    loginResult.getTokenRefreshIntervalInSeconds();
            if (accessToken != null) {
                toastMessage = "Login Facebook Success";
                KacyberRestClientUsage.getInstance().facebookLogin(accessToken.getToken());
//                startActivity(new Intent(this, TokenActivity.class));
            } else {
                toastMessage = "Unknown response type";
            }
        }

        Toast.makeText(
                this,
                toastMessage,
                Toast.LENGTH_LONG)
                .show();
    }

    private void requestPermissions(
            final String permission,
            final int rationaleTitleResourceId,
            final int rationaleMessageResourceId,
            final OnCompleteListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        checkRequestPermissions(
                permission,
                rationaleTitleResourceId,
                rationaleMessageResourceId,
                listener);
    }

    @TargetApi(23)
    private void checkRequestPermissions(
            final String permission,
            final int rationaleTitleResourceId,
            final int rationaleMessageResourceId,
            final OnCompleteListener listener) {
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        final int requestCode = nextPermissionsRequestCode++;
        permissionsListeners.put(requestCode, listener);

        if (shouldShowRequestPermissionRationale(permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(rationaleTitleResourceId)
                    .setMessage(rationaleMessageResourceId)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            // ignore and clean up the listener
                            permissionsListeners.remove(requestCode);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    @TargetApi(23)
    @SuppressWarnings("unused")
    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final @NonNull String permissions[],
                                           final @NonNull int[] grantResults) {
        final OnCompleteListener permissionsListener = permissionsListeners.remove(requestCode);
        if (permissionsListener != null
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsListener.onComplete();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void OnLoginSuccess(LoginSuccessEvent loginSuccessEvent) {
        if (loginSuccessEvent.loginSuccessModel.auth_token!=null) {
            Constants.OWE_ACCESS_TOKEN = "Basic " + loginSuccessEvent.loginSuccessModel.auth_token;
        }
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }

}
