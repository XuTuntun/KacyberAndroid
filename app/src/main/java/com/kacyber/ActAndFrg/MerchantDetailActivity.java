package com.kacyber.ActAndFrg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.event.MerchantDetailEvent;
import com.kacyber.network.http.KacyberRestClient;
import com.kacyber.network.http.KacyberRestClientUsage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MerchantDetailActivity extends FragmentActivity implements View.OnClickListener {

    private AQuery aQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_detail);
        Intent intent = getIntent();
        aQuery = new AQuery(this);
        aQuery.id(R.id.normal_back_text).clickable(true).clicked(this);
        aQuery.id(R.id.normal_chats_back).clickable(true).clicked(this);
        long merchantID = 1;
        if (intent != null) {
            merchantID = intent.getLongExtra("merchantID", 1);
        }
        KacyberRestClientUsage.getInstance().getMerchantDetailById(merchantID);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.nested_scroll_view);
        scrollView.setFillViewport (true);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(this.getSupportFragmentManager());
        adapter.addFragment(new MerchantHomeFragment(), "Home");
        adapter.addFragment(new MerchantDetailFragment(), "Details");
        adapter.addFragment(new MerchantReviewsFragment(), "Reviews");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_back_text:
                this.finish();
                break;
            case R.id.normal_chats_back:
                this.finish();
                break;
        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Subscribe
    public void onMerchantDetailEvent(MerchantDetailEvent event) {

    }
}
