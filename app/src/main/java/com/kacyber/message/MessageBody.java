package com.kacyber.message;

import com.alibaba.fastjson.JSON;
import com.kacyber.model.JSONParceble;

import org.json.JSONObject;

import io.realm.RealmObject;

/**
 * Created by guofuming on 31/10/2016.
 */

public class MessageBody implements JSONParceble {
    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        return true;
    }
}
