package com.kacyber.message;

import com.alibaba.fastjson.JSON;
import com.kacyber.model.JSONParceble;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by guofuming on 31/10/2016.
 */

public class Message extends RealmObject {

    public String payloadType;
    public long msgId;
    public long conversationId;
    public long sendTime;

    public boolean encrypt;
    public String encryptText;

    public long senderId;
    public String senderUserName;
    public long toUserId;
    public String toUserName;
    public String body;
    public String msgType;
}
