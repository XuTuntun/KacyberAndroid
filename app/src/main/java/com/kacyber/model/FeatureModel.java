package com.kacyber.model;

import org.json.JSONObject;

/**
 * Created by guofuming on 25/10/2016.
 */

public class FeatureModel implements JSONParceble {
    public String name;
    public String image;
    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        try {
            name = obj.getString("name");
            image = obj.getString("image");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
