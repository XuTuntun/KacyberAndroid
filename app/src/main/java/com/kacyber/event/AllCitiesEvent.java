package com.kacyber.event;

import com.kacyber.adapter.SortModelCities;
import com.kacyber.model.DealMerchant;

import java.util.ArrayList;

/**
 * Created by guofuming on 21/10/2016.
 */

public class AllCitiesEvent {

    public ArrayList<SortModelCities> allCitiesList;

    public AllCitiesEvent(ArrayList<SortModelCities> allCitiesInput) {
        allCitiesList = allCitiesInput;
    }
}
