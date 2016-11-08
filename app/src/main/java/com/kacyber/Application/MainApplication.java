package com.kacyber.Application;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

//import com.facebook.FacebookSdk;
import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccountKit;
//import com.facebook.appevents.AppEventsLogger;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.Stetho;
import com.melink.bqmmsdk.sdk.BQMM;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Pattern;

import io.realm.Realm;

//import com.melink.bqmmsdk.sdk.BQMM;

/**
 * Created by guofuming on 9/9/16.
 */
public class MainApplication extends Application {

//    public CognitoCachingCredentialsProvider credentialsProvider;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            // Kitkat and lower has a bug that can cause in correct strict mode
//            // warnings about expected activity counts
//            enableStrictMode();
//        }
//        Stetho.initializeWithDefaults(this);
        Realm.init(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        AccountKit.initialize(getApplicationContext());

        try {
            Bundle bundle = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
            BQMM.getInstance().initConfig(this, bundle.getString("bqmm_app_id"), bundle.getString("bqmm_app_secret"));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        RealmInspectorModulesProvider.builder(this)
                .withFolder(getCacheDir())
                .withMetaTables()
                .withDescendingOrder()
                .withLimit(1000)
                .databaseNamePattern(Pattern.compile(".+\\.realm"))
                .build();

//        credentialsProvider = new CognitoCachingCredentialsProvider(
//                getApplicationContext(),
//                "eu-central-1:a77af950-07db-4bb2-86ea-24b9c7b98ed3", // Identity Pool ID
//                Regions.EU_CENTRAL_1 // Region
//        );
//
//        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

//        try {
//            Bundle bundle = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
//            BQMM.getInstance().initConfig(this, "cc764b56e39943e882cc5b3200b07688", "930471a54b6d4c36b4b49155f375540d");
//        } catch (PackageManager.NameNotFoundException e){
//            e.printStackTrace();
//        }
    }

//
//    public void enableStrictMode() {
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());
//    }

}
