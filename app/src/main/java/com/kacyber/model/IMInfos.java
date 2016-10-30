package com.kacyber.model;

import org.apache.http.ExceptionLogger;
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
            host = obj.getString("host");
            port = obj.getInt("port");
            queueName = obj.getString("queueName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
