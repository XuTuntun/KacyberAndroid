package com.kacyber.model;

import com.kacyber.message.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by guofuming on 31/10/2016.
 */

public class Conversation extends RealmObject {
    public long id;
    public String appId;
    public int type;
    public int adminUserId;
    public int targetUserId;
    public String qos;
    public String encrypt;
    public String qrcodeUrl;
    public String title;
    public String notice;

    public int unReadCount = 0;
    public long lastSendTime;

    public RealmList<Message> messagesList = new RealmList<>();

//    public List<Message> messageList;

//    public ArrayList<Message>


    public void addMessage(Message message) {
        messagesList.add(message);
        unReadCount++;
        lastSendTime = message.sendTime;
    }

    public void setUnReadCount(int count) {
        unReadCount = count;
    }
}
