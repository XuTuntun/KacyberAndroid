package com.kacyber.model;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;

/**
 * Created by guofuming on 11/10/2016.
 */

public class DealMerchant implements JSONParceble {
    public long merchantId;
    public String headPicUrl;
    public String title;
    public int score;
    public String address;
    public int viewCount;
    public long distance;
    public long longitude;
    public long latitude;


    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        try {
            merchantId = obj.getLong("merchantId");
            headPicUrl = obj.getString("headPicUrl");
            title = obj.getString("title");
            score = obj.getInt("score");
            address = obj.getString("address");
            viewCount = obj.getInt("view_count");
            distance = obj.getLong("distance");
            longitude = obj.getLong("longitude");
            latitude = obj.getLong("latitude");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }
}
