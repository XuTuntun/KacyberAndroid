package com.kacyber.model;

import org.apache.http.ExceptionLogger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by guofuming on 27/10/2016.
 */

public class IMInfos implements JSONParceble {
    public String host;
    public int port;
    public String queueName;
    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        try {
            JSONArray jsonArray = obj.getJSONArray("servers");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            host = jsonObject.getString("host");
            port = jsonObject.getInt("port");
            queueName = jsonObject.getString("queueName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
