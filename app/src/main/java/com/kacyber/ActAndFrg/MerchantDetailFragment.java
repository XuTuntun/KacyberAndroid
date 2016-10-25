package com.kacyber.ActAndFrg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.event.MerchantDetailEvent;
import com.kacyber.model.Merchant;
import com.kacyber.model.MerchantDetail;

import org.greenrobot.eventbus.Subscribe;


/**
 * A simple {@link Fragment} subclass.
 */
public class MerchantDetailFragment extends Fragment {

    private static String TAG = MerchantDetailFragment.class.getName();

    private AQuery aQuery;

    public MerchantDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_merchant_detail, container, false);
        aQuery = new AQuery(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        if (MerchantDetailActivity.currentMerchant!=null) {
            Log.e(TAG, "" + MerchantDetailActivity.currentMerchant.description);
            aQuery.id(R.id.detail_description_text).text(MerchantDetailActivity.currentMerchant.description);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Subscribe
    public void onMerchantDetailEvent(MerchantDetailEvent event) {
        MerchantDetail merchantDetail = event.merchantDetail;

        aQuery.id(R.id.detail_description_text).text(merchantDetail.description);

    }

}
