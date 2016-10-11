package com.kacyber.model;

import io.realm.RealmObject;

/**
 * Created by guofuming on 11/10/2016.
 */

public class TrendingItem extends RealmObject {

    public String link;
    public String picUrl;
    public long merchantId;
    public long longititude;
    public long latitude;

}
