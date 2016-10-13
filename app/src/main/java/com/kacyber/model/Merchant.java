package com.kacyber.model;

import org.json.JSONObject;

/**
 * Created by guofuming on 12/10/2016.
 */

public class Merchant implements JSONParceble {
    public long merchantId;
    public String name;
    public String phone;
    public String website;
    public long longitude;
    public long latitude;


    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        return false;
    }
}
