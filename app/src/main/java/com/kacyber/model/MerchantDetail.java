package com.kacyber.model;

import android.util.Log;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by guofuming on 25/10/2016.
 */

public class MerchantDetail implements JSONParceble {

    public int id;
    public String name;
    public String phone;
    public String website;
    public double longitude;
    public double latitude;
    public int gradeMark;
    public String address;
    public String mission;
    public String description;
    public String startTime;
    public String endTime;
    public ArrayList<String> images = new ArrayList<>();

    public String category;
    public ArrayList<WorkHours> workHours = new ArrayList<>();
    public ArrayList<FeatureModel> featureModels = new ArrayList<>();

    @Override
    public boolean initWithJSONObject(JSONObject obj) {
        try {
            id = obj.getInt("id");
            name = obj.getString("name");
            phone = obj.getString("phone");
            website = obj.getString("website");
            if (!website.contains("http")) {
                website = "http://" + website;
            }
            longitude = obj.getDouble("longitude");
            latitude = obj.getDouble("latitude");
            address = obj.getString("address");
            description = obj.getString("description");
            mission = obj.getString("mission");
            startTime = obj.getString("startTime");
            endTime = obj.getString("endTime");
            if (obj.getJSONArray("images").length()>0) {
                for (int i = 0; i < obj.getJSONArray("images").length(); i ++) {
                    images.add(obj.getJSONArray("images").getString(i));
                    Log.e("MerchantDetail", "images is " + images);
                }
            }
            category = obj.getString("category");

            if (obj.getJSONArray("workHours").length()>0) {
                for (int i = 0; i<obj.getJSONArray("workHours").length(); i++) {
                    WorkHours workHoursInstance = new WorkHours();
                    workHoursInstance.initWithJSONObject(obj.getJSONArray("workHours").getJSONObject(i));
                    workHours.add(workHoursInstance);
                }
            }

            if (obj.getJSONArray("featureModels").length()>0) {
                for (int i = 0; i < obj.getJSONArray("featureModels").length(); i++) {
                    FeatureModel featureModel = new FeatureModel();
                    featureModel.initWithJSONObject(obj.getJSONArray("featureModels").getJSONObject(i));
                    featureModels.add(featureModel);
                }
            }

            if (obj.getJSONObject("gradeMark")!=null) {
                gradeMark = obj.getInt("gradeMark");
            } else {
                gradeMark = 5;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
