package com.kacyber.Application;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;

import io.realm.Realm;

//import com.melink.bqmmsdk.sdk.BQMM;

/**
 * Created by guofuming on 9/9/16.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

//        try {
//            Bundle bundle = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
//            BQMM.getInstance().initConfig(this, "cc764b56e39943e882cc5b3200b07688", "930471a54b6d4c36b4b49155f375540d");
//        } catch (PackageManager.NameNotFoundException e){
//            e.printStackTrace();
//        }
    }
}
