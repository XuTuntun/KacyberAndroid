package com.kacyber.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by guofuming on 11/10/2016.
 */

public class PackageInfoUtil {
    public static int getVersionCode(Context context) {
        PackageInfo pinfo = null;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {

        }
        if(pinfo == null){
            return 1;
        }

        int versionNumber = pinfo.versionCode;
        return versionNumber;
    }
}
