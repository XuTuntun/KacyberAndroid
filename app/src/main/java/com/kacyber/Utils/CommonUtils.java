package com.kacyber.Utils;

import android.widget.Toast;

import com.kacyber.AppContext;


/**
 * Description :
 * Author : AllenJuns
 * Date   : 2016-3-04
 */
public class CommonUtils {
    public static void showLongToast( String pMsg) {
        Toast.makeText(AppContext.getInstance(), pMsg, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(String pMsg) {
        Toast.makeText(AppContext.getInstance(), pMsg, Toast.LENGTH_SHORT).show();
    }
}
