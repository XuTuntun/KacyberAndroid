package com.kacyber.model;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by guofuming on 7/11/2016.
 */

public class UserProfile extends RealmObject {
    public long id;
    public String userName;
    public String nickname;
    public String avatar;
    public String gender; //MALE FEMALE
    public String signature;
    public String region;
    public double distance; //mi
    public String notes;
    public RealmList<RealmString> album;
    public long longitude;
    public long latitude;


//    "id": 8,
//            "userName": null,
//            "nickname": null,
//            "avatar": null,
//            "gender": null,
//            "region": "China",
//            "signature": null,
//            "distance": null,
//            "notes": null,
//            "album": null,
//            "longitude": null,
//            "latitude": null

}
