package com.kacyber.event;

import com.kacyber.model.DealMerchant;

import java.util.ArrayList;

/**
 * Created by guofuming on 11/10/2016.
 */

public class DealMerchantListEvent {
    public String superDealImage;
    public ArrayList<DealMerchant> merchantList;

    public DealMerchantListEvent(String superDealImageInput, ArrayList<DealMerchant> merchantListInput) {
        superDealImage = superDealImageInput;
        merchantList = merchantListInput;
    }
}
