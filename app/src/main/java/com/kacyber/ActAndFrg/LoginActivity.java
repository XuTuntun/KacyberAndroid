package com.kacyber.ActAndFrg;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.usage.ConfigurationStats;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kacyber.R;
import com.kacyber.Service.MqttService;
import com.kacyber.Utils.Constants;
import com.kacyber.View.MainActivity;
import com.kacyber.event.LoginSuccessEvent;
import com.kacyber.model.LoginSuccessModel;
import com.kacyber.network.http.KacyberRestClientUsage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static String TAG = LoginActivity.class.getName();
    private AQuery aQuery;
    private TextView termText;
    private AccessToken accessToken;
    public static int APP_REQUEST_CODE = 99;
    public static int RC_SIGN_IN = 55;
    private static final int FRAMEWORK_REQUEST_CODE = 115;
    private int nextPermissionsRequestCode = 4000;
    private LinearLayout facebookLogin;
    private CallbackManager callbackManager;

    private final Map<Integer, OnCompleteListener> permissionsListeners = new HashMap<>();

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

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


        SharedPreferences sharedPreferences = this.getSharedPreferences("Kacyber", Activity.MODE_PRIVATE);
        String oweAccessToken = sharedPreferences.getString("OWE_ACCESS_TOKEN", "");
        String queueName = sharedPreferences.getString("queueName", "");
        String mobile = sharedPreferences.getString("mobile", "");
        int mobileAreaCode = sharedPreferences.getInt("mobileAreaCode", 0);
        long userId = sharedPreferences.getLong("userId", 0);
        if (!oweAccessToken.equals("") && !queueName.equals("")) {
            Log.e(TAG, "accessToken is ------------" + oweAccessToken + "\n" +
                    "queueName is -------------" + queueName + "\n" +
                    "userId is -------------" + userId);
            Constants.OWE_ACCESS_TOKEN = oweAccessToken;
            Constants.USER_ID = userId;
            Constants.QUEUE_NAME = queueName;
            Constants.MOBILE = mobile;
            Constants.MOBILE_AREA_CODE = mobileAreaCode;
            MqttService.actionStart(this.getApplication(), queueName);
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }

        /**
         * Facebook Login without SMS
         */
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "login Result is " + loginResult);
                com.facebook.AccessToken accessToken = loginResult.getAccessToken();
                String token = accessToken.getToken();
                Log.e("FBNotSMS", "token is " + token);
                KacyberRestClientUsage.getInstance().facebookLogin(accessToken.getToken());
            }

            @Override

            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        accessToken = AccountKit.getCurrentAccessToken();

        /**
         * Login Google without SMS
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_response:
                Log.e(TAG, "login response clicked");
//                Intent intent = new Intent();
//                intent.setClass(this, SingUpActivity.class);
//                startActivity(intent);
                onLoginPhone(LoginType.PHONE);


                break;


            case R.id.login_facebook_response:
//                try {
//                    onLoginPhone(LoginType.EMAIL);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
                break;
            case R.id.login_google_response:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

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

    public void onLoginPhone(final LoginType loginType) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        final AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        final AccountKitConfiguration configuration = configurationBuilder.build();
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configuration);

        OnCompleteListener completeListener = new OnCompleteListener() {
            @Override
            public void onComplete() {
                Log.e(TAG, "start facebook sms login request");
                startActivityForResult(intent, FRAMEWORK_REQUEST_CODE);
            }
        };

        switch (loginType) {
            case EMAIL:
                final OnCompleteListener getAccountsCompleteListener = completeListener;
                completeListener = new OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        requestPermissions(
                                Manifest.permission.GET_ACCOUNTS,
                                R.string.permissions_get_accounts_title,
                                R.string.permissions_get_accounts_message,
                                getAccountsCompleteListener);
                    }
                };
                break;
            case PHONE:
                if (configuration.isReceiveSMSEnabled()) {
                    final OnCompleteListener receiveSMSCompleteListener = completeListener;
                    completeListener = new OnCompleteListener() {
                        @Override
                        public void onComplete() {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(
                                    Manifest.permission.RECEIVE_SMS,
                                    R.string.permissions_receive_sms_title,
                                    R.string.permissions_receive_sms_message,
                                    receiveSMSCompleteListener);
//                    }
                        }
                    };
                }
                if (configuration.isReadPhoneStateEnabled()) {
                    final OnCompleteListener readPhoneStateCompleteListener = completeListener;
                    completeListener = new OnCompleteListener() {
                        @Override
                        public void onComplete() {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(
                                    Manifest.permission.READ_PHONE_STATE,
                                    R.string.permissions_read_phone_state_title,
                                    R.string.permissions_read_phone_state_message,
                                    readPhoneStateCompleteListener);
//                    }

                        }
                    };
                }


        }
        completeListener.onComplete();


    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "requestCode is " + resultCode + " \nresultCode is " + resultCode + " \ndata is " + data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            return;
        }

        if (requestCode == FRAMEWORK_REQUEST_CODE) {
            final String toastMessage;
            final AccountKitLoginResult loginResult = AccountKit.loginResultWithIntent(data);
            if (loginResult == null || loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else if (loginResult.getError() != null) {
                toastMessage = "Facebook Login Error";
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
            return;
        } else {
            Log.e(TAG, "callbackManager on activity result");
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }




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

        if (loginSuccessEvent.loginSuccessModel.auth_token != null) {
            Constants.OWE_ACCESS_TOKEN = "Basic " + loginSuccessEvent.loginSuccessModel.auth_token;
        }
        Constants.QUEUE_NAME = loginSuccessEvent.loginSuccessModel.imInfos.queueName;
        Constants.USER_ID = loginSuccessEvent.loginSuccessModel.userId;
        Constants.MOBILE_AREA_CODE = loginSuccessEvent.loginSuccessModel.mobileAreaCode;
        Constants.MOBILE = loginSuccessEvent.loginSuccessModel.mobile;

        SharedPreferences sharedPreferences = this.getSharedPreferences("Kacyber", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("OWE_ACCESS_TOKEN", Constants.OWE_ACCESS_TOKEN);
        editor.putString("queueName", Constants.QUEUE_NAME);
        editor.putLong("userId", loginSuccessEvent.loginSuccessModel.userId);
        editor.putInt("mobileAreaCode", loginSuccessEvent.loginSuccessModel.mobileAreaCode);
        editor.putString("mobile", loginSuccessEvent.loginSuccessModel.mobile);
        editor.commit();
        MqttService.actionStart(this.getApplication(), Constants.QUEUE_NAME);
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        this.finish();


    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            acct.getIdToken();
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        } else {

        }
    }


}
