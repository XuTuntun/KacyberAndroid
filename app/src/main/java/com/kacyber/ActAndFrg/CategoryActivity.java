package com.kacyber.ActAndFrg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kacyber.R;
import com.kacyber.adapter.DealMerchantItemClickListener;
import com.kacyber.adapter.DealMerchantListAdapter;
import com.kacyber.event.AllCategoryEvent;
import com.kacyber.event.CategoryMerchantListEvent;
import com.kacyber.model.Category;
import com.kacyber.model.DealMerchant;
import com.kacyber.network.http.KacyberRestClient;
import com.kacyber.network.http.KacyberRestClientUsage;
import com.warmtel.expandtab.ExpandPopTabView;
import com.warmtel.expandtab.KeyValueBean;
import com.warmtel.expandtab.PopOneListView;
import com.warmtel.expandtab.PopTwoListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends Activity implements DealMerchantItemClickListener, View.OnClickListener {

    private ExpandPopTabView expandPopTabView;
    private ArrayList<DealMerchant> dealMerchantList;

    private List<KeyValueBean> mParentCategoryList = new ArrayList<>();
    private List<ArrayList<KeyValueBean>> mChildCategoryList = new ArrayList<>();
    private List<KeyValueBean> distanceList;
    private List<KeyValueBean> sortByList;
    private List<KeyValueBean> filtersList;
    private AQuery aQuery;

    private int categoryId;
    private String categoryName;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent = getIntent();
        if (intent != null) {
            categoryId = intent.getIntExtra("categoryId", 1);
            categoryName = intent.getStringExtra("categoryName");
            KacyberRestClientUsage.getInstance().getCategory(categoryId, null, null, null, null);
        }
        EventBus.getDefault().register(this);
        aQuery = new AQuery(this);
        recyclerView = (RecyclerView) findViewById(R.id.super_deal_merchant_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setConfigData();
        aQuery.id(R.id.category_search).clickable(true).clicked(this);
        aQuery.id(R.id.normal_back_text).clickable(true).clicked(this);
        aQuery.id(R.id.back_text).clickable(true).clicked(this);
        expandPopTabView = (ExpandPopTabView) findViewById(R.id.category_expandtab);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (expandPopTabView != null) {
            expandPopTabView.onExpandPopView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        KacyberRestClientUsage.getInstance().getCategoryList();

    }


    private void setConfigData() {

    }

    public void addItem(ExpandPopTabView expandTabView, List<KeyValueBean> lists, String defaultSelect, String defaultShowText) {
        PopOneListView popOneListView = new PopOneListView(this);
        popOneListView.setDefaultSelectByValue(defaultSelect);
        //popViewOne.setDefaultSelectByKey(defaultSelect);
        popOneListView.setCallBackAndData(lists, expandTabView, new PopOneListView.OnSelectListener() {
            @Override
            public void getValue(String key, String value) {
                Log.e("tag", "key :" + key + " ,value :" + value);
            }
        });
        expandTabView.addItemToExpandTab(defaultShowText, popOneListView);
    }

    public void addItem(ExpandPopTabView expandTabView, List<KeyValueBean> parentLists,
                        List<ArrayList<KeyValueBean>> childrenListLists, String defaultParentSelect, String defaultChildSelect, String defaultShowText) {
        PopTwoListView popTwoListView = new PopTwoListView(this);
        popTwoListView.setDefaultSelectByValue(defaultParentSelect, defaultChildSelect);
        //distanceView.setDefaultSelectByKey(defaultParent, defaultChild);
        popTwoListView.setCallBackAndData(expandTabView, parentLists, childrenListLists, new PopTwoListView.OnSelectListener() {
            @Override
            public void getValue(String showText, String parentKey, String childrenKey) {
                Log.e("tag", "showText :" + showText + " ,parentKey :" + parentKey + " ,childrenKey :" + childrenKey);
            }
        });
        expandTabView.addItemToExpandTab(defaultShowText, popTwoListView);
    }

    @Subscribe
    public void onCategoryListEvent(AllCategoryEvent allCategoryEvent) {
        if (expandPopTabView.getChildCount() > 2) {
            return;
        }
        if (allCategoryEvent.allCategoryList.size() != 0) {
            ArrayList<Category> categories = allCategoryEvent.allCategoryList;
            for (int i = 0; i < categories.size(); i++) {
                KeyValueBean keyValueBean = new KeyValueBean();
                keyValueBean.setKey("" + categories.get(i).id);
                keyValueBean.setValue(categories.get(i).name);
                mParentCategoryList.add(keyValueBean);

                if (categories.get(i).parentId == 0) {
                    if (categories.get(i).childrenList.size() != 0) {
                        ArrayList<KeyValueBean> childrenList = new ArrayList<>();
                        for (int j = 0; j < categories.get(i).childrenList.size(); j++) {
                            KeyValueBean keyValueBean1 = new KeyValueBean();
                            keyValueBean1.setKey("" + categories.get(i).childrenList.get(j).id);
                            keyValueBean1.setValue(categories.get(i).childrenList.get(j).name);
                            childrenList.add(keyValueBean1);
                        }
                        mChildCategoryList.add(childrenList);
                    }
                }
            }
        }


        try {
            addItem(expandPopTabView, mParentCategoryList, mChildCategoryList, "SUPER DEALS", "Brand Showcase", "All Categories");
        } catch (Exception e) {
            e.printStackTrace();
        }
        distanceList = new ArrayList<>();
        distanceList.add(new KeyValueBean("1000", "1Km"));
        distanceList.add(new KeyValueBean("3000", "3Km"));
        distanceList.add(new KeyValueBean("5000", "5Km"));
        distanceList.add(new KeyValueBean("10000", "10Km"));
        addItem(expandPopTabView, distanceList, "1000m", "Location");
        sortByList = new ArrayList<>();
        sortByList.add(new KeyValueBean("Relevance", "Relevance"));
        sortByList.add(new KeyValueBean("Popular", "Popular"));
        sortByList.add(new KeyValueBean("Nearby", "Nearby"));
        sortByList.add(new KeyValueBean("Rating", "Rating"));
        addItem(expandPopTabView, sortByList, "Popular", "Sort By");
        filtersList = new ArrayList<>();
        filtersList.add(new KeyValueBean("Discount", "Discount"));
        filtersList.add(new KeyValueBean("Parking", "Parking"));
        filtersList.add(new KeyValueBean("Wi-Fi", "Wi-Fi"));
        filtersList.add(new KeyValueBean("Credit Cards", "Credit Cards"));
        addItem(expandPopTabView, filtersList, "", "Filters");
    }

    @Subscribe
    public void onCategoryMerchantListEvent(CategoryMerchantListEvent categoryMerchantListEvent) {
        dealMerchantList = categoryMerchantListEvent.merchantList;
        DealMerchantListAdapter dealMerchantListAdapter = new DealMerchantListAdapter(this, R.layout.merchant_item_layout, dealMerchantList);
        dealMerchantListAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(dealMerchantListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_category_back:
                this.finish();
                break;
            case R.id.back_text:
                this.finish();
                break;
            case R.id.category_search:
                Intent intent = new Intent();
                intent.setClass(this, NormalSearchActivity.class);
                startActivity(intent);
                break;
        }
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
}
