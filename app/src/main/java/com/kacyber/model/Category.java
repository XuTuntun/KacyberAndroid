package com.kacyber.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by guofuming on 24/10/2016.
 */

public class Category implements JSONParceble {
    public int id;
    public String name;
    public int parentId;
    public ArrayList<Category> childrenList;


    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        try {
            id = obj.getInt("id");
            name = obj.getString("name");
            parentId = obj.getInt("parentId");
            if (parentId == 0) {
                JSONArray jsonArray = obj.getJSONArray("childs");
                childrenList = new ArrayList<>();
                for (int i=0; i < jsonArray.length(); i++) {
                    Category category = new Category();
                    category.initWithJSONObject(jsonArray.getJSONObject(i));
                    childrenList.add(category);
                }
            } else {
                childrenList = new ArrayList<>();
                childrenList.clear();
            }
        } catch (Exception e) {

        }
        return true;
    }
}
