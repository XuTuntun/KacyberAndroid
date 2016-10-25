package com.kacyber.model;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

/**
 * Created by guofuming on 25/10/2016.
 */

public class WorkHours implements JSONParceble {

    public String startTime;
    public String endTime;
    public String week;
    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        try {
            startTime = obj.getString("startTime");
            endTime = obj.getString("endTime");
            week = obj.getString("week");
        } catch(Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
