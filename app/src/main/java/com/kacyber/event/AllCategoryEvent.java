package com.kacyber.event;

import com.kacyber.model.Category;

import java.util.ArrayList;

/**
 * Created by guofuming on 24/10/2016.
 */

public class AllCategoryEvent {
    public ArrayList<Category> allCategoryList;

    public AllCategoryEvent(ArrayList<Category> allCategoryListInput) {
        allCategoryList = allCategoryListInput;
    }

}
