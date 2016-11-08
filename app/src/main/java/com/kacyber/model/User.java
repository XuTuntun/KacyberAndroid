package com.kacyber.model;

import org.json.JSONObject;

import io.realm.RealmObject;

/**
 * Created by guofuming on 1/11/2016.
 */

public class User extends RealmObject implements JSONParceble {

    public long id;
    public String userName;
    public long mobile;
    public int mobileCode;
    public String avatar;
    public String nickName;
    public boolean friend;
    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        try {
            id = obj.getLong("id");
            mobile = obj.getLong("mobile");
            mobileCode = obj.getInt("mobileCode");
            friend = obj.getBoolean("friend");
            avatar = obj.getString("avatar");
            nickName = obj.getString("nickname");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
