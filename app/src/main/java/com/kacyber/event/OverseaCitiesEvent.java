package com.kacyber.event;

import com.kacyber.adapter.SortModelCities;

import java.util.ArrayList;

/**
 * Created by guofuming on 21/10/2016.
 */

public class OverseaCitiesEvent {

    public ArrayList<SortModelCities> overseaCitiesList;

    public OverseaCitiesEvent(ArrayList<SortModelCities> overseaCitiesInput) {
        overseaCitiesList = overseaCitiesInput;
    }

}
