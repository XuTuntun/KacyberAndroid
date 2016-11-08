package com.kacyber.network.http;


import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.kacyber.Utils.Constants;

import org.apache.http.HttpEntity;
import org.json.JSONObject;

import javax.crypto.KeyAgreement;

public class KacyberRestClient {

    private static String TAG = KacyberRestClient.class.getName();

    private static AndroidRestClient client = new AndroidRestClient();

    /**
     * Set Header
     */
    public static void setTokenHeader(String token) {

        client.addHeader("token", token);
        Log.e(TAG, "tooken is " + token);
    }

    /**
     * Set appkey header
     */
    public static void setAppkeyHeader() {

        client.addHeader("appkey", Constants.APP_KEY);
        if (Constants.OWE_ACCESS_TOKEN != null) {
            client.addHeader("Authorization", Constants.OWE_ACCESS_TOKEN);
            Log.e(TAG, "tooken is " + Constants.OWE_ACCESS_TOKEN);
        } else {
            Log.e(TAG, "OWE_ACCESS_TOKEN ERROR");
        }
    }

    /**
     * Get方法用于测试
     * 
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, AndroidRequestParams params, HttpResponseHandler responseHandler) {
        client.sendRequest(false, url, params, responseHandler);
    }

    public static void getWithoutParams(String url, HttpResponseHandler responseHandler) {
        client.sendRequestWithoutParamas(false, url, responseHandler);
    }

    /**
     * 建议使用Post方法
     * 
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void post(String url, AndroidRequestParams params, HttpResponseHandler responseHandler) {
        client.sendRequest(true, url, params, responseHandler);
    }

    public static void postGZip(String url, AndroidRequestParams params, HttpResponseHandler responseHandler) {
        client.sendGZipRequest(true, url, params, responseHandler);
    }

    public static void postBody(Context context, String url, HttpEntity entity, String contentType, HttpResponseHandler responseHandler) {
        client.post(context, url, entity, contentType, responseHandler);
    }

    public static void cancel() {
        client.cancelAllRequests(true);
    }

    private static AlertDialog verifyFaileDialog = null;

    /**
     * 用户信息验证失败
     * 
     * @param context
     */
    public static void verifyFailed(final Context context) {
//        if (verifyFaileDialog != null)
//            return;
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//        builder.setMessage(R.string.yonghuxinxiyanzhengshibai);
//        // Add the buttons
//        builder.setPositiveButton(R.string.qudenglu, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User clicked OK button
//                dialog.dismiss();
//                Intent intent = new Intent(context, LoginFragmentActivity.class);
//                context.startActivity(intent);
//                verifyFaileDialog = null;
//            }
//        });
//        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User cancelled the dialog
//                dialog.cancel();
//                verifyFaileDialog = null;
//            }
//        });
//        verifyFaileDialog = builder.create();
//        verifyFaileDialog.show();
    }
}
