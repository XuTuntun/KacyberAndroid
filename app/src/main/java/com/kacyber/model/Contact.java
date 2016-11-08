package com.kacyber.model;

import io.realm.RealmObject;

/**
 * Created by guofuming on 7/11/2016.
 */

public class Contact extends RealmObject {
    public long id;
    public long userId;
    public String userName;
    public long friendUserId;
    public long confirmedTime;
    public String avatarUrl;
}
