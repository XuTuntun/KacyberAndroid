package com.kacyber.model;

import io.realm.RealmObject;

/**
 * Created by guofuming on 11/10/2016.
 */

public class DealMerchant extends RealmObject {
    public long merchantId;
    public String headPicUrl;
    public String title;
    public int score;
    public String address;
    public int viewCount;
    public long distance;
    public long longitude;
    public long latitude;
}
