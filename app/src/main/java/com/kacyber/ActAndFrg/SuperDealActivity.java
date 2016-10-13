package com.kacyber.ActAndFrg;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.adapter.DealMerchantItemClickListener;
import com.kacyber.adapter.DealMerchantListAdapter;
import com.kacyber.event.DealMerchantListEvent;
import com.kacyber.model.DealMerchant;
import com.kacyber.network.http.KacyberRestClientUsage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class SuperDealActivity extends Activity implements DealMerchantItemClickListener, View.OnClickListener{
    private static String TAG = SuperDealActivity.class.getName();

    private RecyclerView recyclerView;
    private ArrayList<DealMerchant> dealMerchantList;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.fragment_super_deal);
        AQuery aQuery = new AQuery(this);
        aQuery.id(R.id.back_text).clickable(true).clicked(this);
        aQuery.id(R.id.normal_chats_back).clickable(true).clicked(this);
        recyclerView = (RecyclerView) findViewById(R.id.super_deal_merchant_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        context = this;
//        aQuery.id()
    }

    @Override
    public void onResume() {
        super.onResume();
        KacyberRestClientUsage.getInstance().getMerchantsBySuperDeal();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDealMerchantListEvent(DealMerchantListEvent event) {
        Log.e(TAG, "onDealMerchantListEvent");
        dealMerchantList = event.merchantList;
        DealMerchantListAdapter dealMerchantListAdapter = new DealMerchantListAdapter(this, R.layout.merchant_item_layout, dealMerchantList);
        dealMerchantListAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(dealMerchantListAdapter);


    }

    @Override
    public void onItemClick(View viewm, int position) {
        if (dealMerchantList!=null) {
            if (dealMerchantList.size()!=0) {
                DealMerchant dealMerchant = dealMerchantList.get(position);
                Intent intent = new Intent();
                intent.setClass(this, MerchantDetailActivity.class);
                intent.putExtra("merchantID", dealMerchant.merchantId);
                this.startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_chats_back:
                this.finish();
                break;

            case R.id.back_text:
                this.finish();
                break;
        }
    }
}
