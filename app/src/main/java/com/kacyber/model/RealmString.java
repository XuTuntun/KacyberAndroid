package com.kacyber.model;

import io.realm.RealmObject;

/**
 * Created by guofuming on 7/11/2016.
 */

public class RealmString extends RealmObject {
    private String val;

    public String getValue() {
        return val;
    }

    public void setValue(String value) {
        this.val = value;
    }
}
