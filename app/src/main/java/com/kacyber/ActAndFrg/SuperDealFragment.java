package com.kacyber.ActAndFrg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kacyber.R;
import com.kacyber.adapter.DealMerchantListAdapter;
import com.kacyber.event.DealMerchantListEvent;
import com.kacyber.model.DealMerchant;
import com.kacyber.network.http.KacyberRestClientUsage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SuperDealFragment extends Fragment {
    private static String TAG = SuperDealFragment.class.getName();

    private RecyclerView recyclerView;


    public SuperDealFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KacyberRestClientUsage.getInstance().getMerchantsBySuperDeal();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_super_deal, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.super_deal_merchant_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDealMerchantListEvent(DealMerchantListEvent event) {
        Log.e(TAG, "onDealMerchantListEvent");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<DealMerchant> dealMerchantList = realm.where(DealMerchant.class).findAll();

        recyclerView.setAdapter(new DealMerchantListAdapter(this, null, dealMerchantList));


    }
}
