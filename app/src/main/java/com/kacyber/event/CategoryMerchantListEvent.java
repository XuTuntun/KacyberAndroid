package com.kacyber.event;

import com.kacyber.model.DealMerchant;

import java.util.ArrayList;

/**
 * Created by guofuming on 24/10/2016.
 */

public class CategoryMerchantListEvent {

    public ArrayList<DealMerchant> merchantList;

    public CategoryMerchantListEvent(ArrayList<DealMerchant> merchantListInput) {
        merchantList = merchantListInput;
    }
}
