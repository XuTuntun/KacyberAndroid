package com.kacyber.model;

import io.realm.RealmObject;

/**
 * Created by guofuming on 1/11/2016.
 */

public class AddFriendRequest extends RealmObject {
    public long requestId;
    public long fromUserId;
    public long toUserId;
    public String message;
}
