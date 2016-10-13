package com.kacyber.event;

import com.kacyber.model.DealMerchant;

import java.util.ArrayList;

/**
 * Created by guofuming on 11/10/2016.
 */

public class DealMerchantListEvent {
    public ArrayList<DealMerchant> merchantList;

    public DealMerchantListEvent(ArrayList<DealMerchant> merchantListInput) {
        merchantList = merchantListInput;
    }
}
