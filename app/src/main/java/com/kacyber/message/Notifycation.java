package com.kacyber.message;

import com.kacyber.model.FastJSONParceble;
import com.kacyber.model.JSONParceble;

import org.json.JSONObject;

/**
 * Created by guofuming on 31/10/2016.
 */

public class Notifycation implements FastJSONParceble {

    public String action;


    @Override
    public boolean initWithJSONObject(com.alibaba.fastjson.JSONObject obj) {

        return true;
    }
}
