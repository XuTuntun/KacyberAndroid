package com.kacyber.model;

import org.json.JSONObject;

/**
 * Created by guofuming on 27/10/2016.
 */

public class LoginSuccessModel implements JSONParceble {

    public long userId;
    public String auth_token;
    public long loginTime;
    public long expires;
    public IMInfos imInfos;

    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        try {
            userId = obj.getLong("userId");
            auth_token = obj.getString("auth_token");
            expires = obj.getLong("expires");
            loginTime = obj.getLong("loginTime");
            imInfos.initWithJSONObject(obj.getJSONObject("imInfos"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}
