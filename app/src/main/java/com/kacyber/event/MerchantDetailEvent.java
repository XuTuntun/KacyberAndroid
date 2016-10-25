package com.kacyber.event;

import com.kacyber.model.MerchantDetail;

/**
 * Created by guofuming on 12/10/2016.
 */

public class MerchantDetailEvent {
    public MerchantDetail merchantDetail;

    public MerchantDetailEvent(MerchantDetail merchantDetailInput) {
        merchantDetail = merchantDetailInput;
    }
}
