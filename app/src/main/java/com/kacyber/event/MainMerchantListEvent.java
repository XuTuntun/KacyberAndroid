package com.kacyber.event;

import com.kacyber.model.DealMerchant;

import java.util.ArrayList;

/**
 * Created by guofuming on 12/10/2016.
 */

public class MainMerchantListEvent {
    public ArrayList<DealMerchant> merchantList;

    public MainMerchantListEvent(ArrayList<DealMerchant> merchantListInput) {
        merchantList = merchantListInput;
    }

}
