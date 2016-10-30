package com.kacyber.model;

import org.json.JSONObject;

import io.realm.RealmObject;

/**
 * Created by guofuming on 11/10/2016.
 */

public class EventItem implements JSONParceble {
    public long id;
    public String link;
    public String picUrl;
    public long merchantId;
    public double longtitude;
    public double latitude;

    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        try {
            id = obj.getInt("id");
            link = "http://" + obj.getString("link");
            picUrl = obj.getString("picUrl");
            longtitude = obj.getDouble("longitude");
            latitude = obj.getDouble("latitude");
            merchantId = obj.getLong("merchantId");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
