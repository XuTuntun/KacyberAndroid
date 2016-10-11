package com.kacyber.model;

import io.realm.RealmObject;

/**
 * Created by guofuming on 11/10/2016.
 */

public class Deal extends RealmObject {
    public String title;
    public String address;
    public long merchantId;
    public long longitude;
    public long latitude;
}
