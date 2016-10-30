package com.kacyber.event;

import com.kacyber.model.DealMerchant;
import com.kacyber.model.EventItem;
import com.kacyber.model.TrendingItem;

import java.util.ArrayList;

/**
 * Created by guofuming on 12/10/2016.
 */

public class MainMerchantListEvent {
    public ArrayList<DealMerchant> merchantList;
    public ArrayList<TrendingItem> trendingItems;
    public ArrayList<EventItem> eventItems;

    public MainMerchantListEvent(ArrayList<DealMerchant> merchantListInput, ArrayList<TrendingItem> trendingItemsInput, ArrayList<EventItem> eventItemsInput) {

        merchantList = merchantListInput;
        trendingItems = trendingItemsInput;
        eventItems = eventItemsInput;

    }

}
