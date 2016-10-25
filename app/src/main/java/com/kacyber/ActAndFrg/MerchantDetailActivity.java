package com.kacyber.ActAndFrg;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.event.MerchantDetailEvent;
import com.kacyber.model.Merchant;
import com.kacyber.model.MerchantDetail;
import com.kacyber.network.http.KacyberRestClient;
import com.kacyber.network.http.KacyberRestClientUsage;
import com.kacyber.network.service.ImageSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MerchantDetailActivity extends FragmentActivity implements View.OnClickListener {
    private static String TAG = MerchantDetailActivity.class.getName();

    private AQuery aQuery;

    public static MerchantDetail currentMerchant;
    public NetworkImageView backgroundImage;
    private ImageLoader imageLoader;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_detail);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        aQuery = new AQuery(this);
        aQuery.id(R.id.normal_back_text).clickable(true).clicked(this);
        aQuery.id(R.id.normal_chats_back).clickable(true).clicked(this);
        aQuery.id(R.id.detailspage_nav_fav).clickable(true).clicked(this);
        aQuery.id(R.id.detaispage_nav_share).clickable(true).clicked(this);
        backgroundImage = (NetworkImageView) findViewById(R.id.backdrop);
        imageLoader = ImageSingleton.getInstance(this).getImageLoader();
        long merchantID = 1;
        if (intent != null) {
            merchantID = intent.getLongExtra("merchantID", 26);
        }
        KacyberRestClientUsage.getInstance().getMerchantDetailById(merchantID);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        scrollView.setFillViewport(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void setupViewPager(final ViewPager viewPager) {
        adapter = new Adapter(this.getSupportFragmentManager());
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
            case R.id.detailspage_nav_fav:
                Log.e(TAG, "on Clicked " + "fav clicked");
                Toast.makeText(this, "Cannot fav now", Toast.LENGTH_LONG).setGravity(Gravity.TOP, 0, 0);
                break;
            case R.id.detaispage_nav_share:

                Log.e(TAG, "on Clicked" + "share clicked");
                Toast.makeText(this, "Can not share now", Toast.LENGTH_LONG).setGravity(Gravity.TOP, 0, 0);
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

        public void update(){
            for (int i = 0; i< mFragments.size(); i ++) {
                mFragments.get(i).onResume();
            }
        }

        @Override
        public Fragment getItem(int position) {
            Log.e(TAG, "getItem");
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
        currentMerchant = event.merchantDetail;
        Log.e(TAG, "merchant is " + currentMerchant);

        backgroundImage.setImageUrl("http://" + currentMerchant.images.get(0), imageLoader);



        adapter.update();
    }
}
