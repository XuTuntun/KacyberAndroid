package com.kacyber.model;

import org.json.JSONObject;

import io.realm.RealmObject;

/**
 * Created by guofuming on 11/10/2016.
 */

public class TrendingItem implements JSONParceble {

    public long id;
    public String link;
    public String picUrl;
    public long merchantId;
    public double longitude;
    public double latitude;
    public String title;
    public String subTitle;

    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        try {
            id = obj.getInt("id");
            link = "http://" + obj.getString("link");
            picUrl = obj.getString("picUrl");
            longitude = obj.getDouble("longitude");
            latitude = obj.getDouble("latitude");
            title = obj.getString("title");
            subTitle = obj.getString("subTtile");
            merchantId = obj.getLong("merchantId");

        } catch (Exception e) {
            e.printStackTrace();

        }



        return true;
    }
}
