package com.kacyber.model;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by guofuming on 27/10/2016.
 */

public class LoginSuccessModel implements JSONParceble {

    public long userId;
    public String auth_token;
    public long loginTime;
    public long expires;
    public int mobileAreaCode;
    public String mobile;
    public IMInfos imInfos = new IMInfos();

    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        try {
            Log.e("LoginSucessModel", ""+ obj);
            userId = obj.getLong("userId");
            auth_token = obj.getString("auth_token");
            expires = obj.getLong("expires");
            loginTime = obj.getLong("loginTime");
            mobileAreaCode = obj.getInt("mobileAreaCode");
            mobile = obj.getString("mobile");
            JSONObject jsonObject = obj.getJSONObject("imInfos");
            Log.e("IMInfos", ""+jsonObject);
            imInfos.initWithJSONObject(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


}
