package com.kacyber.View;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import com.kacyber.ActAndFrg.SuperDealFragment;
import com.kacyber.PageIndicator.CirclePageIndicator;
import com.kacyber.R;
import com.kacyber.network.http.KacyberRestClientUsage;

/**
 * Created by guofuming on 12/8/16.
 */
public class DiscoverFragment extends Fragment implements View.OnClickListener {

    private static String TAG = DiscoverFragment.class.getName();

    private ViewPager viewPager = null;
    private CirclePageIndicator circlePageIndicator;
    private Context applicationContext;

    private AQuery aQuery;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_layout, null);
        aQuery = new AQuery(view);

        applicationContext = getActivity().getApplicationContext();

        viewPager = (ViewPager) view.findViewById(R.id.discover_view_pager);
        viewPager.setAdapter(new ViewPageAdapter());

        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.discover_pager_indicator);
        circlePageIndicator.setViewPager(viewPager);
//        circlePageIndicator.setRadius(12);
//        circlePageIndicator.setStrokeWidth(3);
//        circlePageIndicator.setFillColor(Color.GRAY);
//        circlePageIndicator.setGap(10);

        aQuery.id(R.id.discover_deal_container).clickable(true).clicked(this);

        return view;
    }

    public void ChannelClickedByViewId(int id) {
        int categoryId = 1;
        switch (id) {
            case R.id.discover_channel_super_deals:
                categoryId = 1;
                break;
            case R.id.discover_channel_food_drink:
                categoryId = 12;
                break;
            case R.id.discover_channel_shopping:
                categoryId = 28;
                break;
            case R.id.discover_channel_entertainment:
                categoryId = 28;
                break;
        }
        KacyberRestClientUsage.getInstance().getCategoryById(categoryId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.discover_deal_container:
                SuperDealFragment superDealFragment = new SuperDealFragment();
                FragmentManager fragmentManager = this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, superDealFragment).commit();
        }
    }

    class ViewPageAdapter extends PagerAdapter {
        private static final int COUNT = 2;
        private View[] mViews = new View[COUNT];

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position < 0 || position >= COUNT) {
                return null;
            }
            if (mViews[position] == null) {
                final LayoutInflater inflater = LayoutInflater.from(applicationContext);
                switch (position) {
                    case 0: {
                        mViews[0] = inflater.inflate(R.layout.discover_category_header1, null);
                        Log.e(TAG, "position is" + position);
                        AQuery query2 = new AQuery(mViews[0]);
                        query2.id(R.id.discover_channel_super_deals).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
                        query2.id(R.id.discover_channel_food_drink).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
                        query2.id(R.id.discover_channel_shopping).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
                        query2.id(R.id.discover_channel_entertainment).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
                        query2.id(R.id.discover_channel_tour_travel).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
                        query2.id(R.id.discover_channel_agricultrue).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
                        query2.id(R.id.discover_channel_finance).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
                        query2.id(R.id.discover_channel_real_estate).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });


//                        collapsingToolbarLayout.setTitleEnabled(true);
//                        query2.id(R.id.rm_welcome_title).text("智能感应").typeface(sanfranciscoTypeface);
//                        query2.id(R.id.rm_welcome_text).text("精准感应实时步频  记录跑步里程时长").typeface(sanfranciscoTypeface);
                        break;
                    }
                    case 1: {
                        mViews[1] = inflater.inflate(R.layout.discover_category_header2, null);
                        Log.e(TAG, "position is" + position);
                        AQuery query2 = new AQuery(mViews[1]);
                        query2.id(R.id.discover_channel_transport).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
                        query2.id(R.id.discover_channel_hotels).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
                        query2.id(R.id.discover_channel_health).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
                        query2.id(R.id.discover_channel_beauty).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
                        query2.id(R.id.discover_channel_services).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
                        query2.id(R.id.discover_channel_all).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId());
                            }
                        });
//                        collapsingToolbarLayout.setTitleEnabled(false);
//                        query2.id(R.id.rm_welcome_title).text("完美对接").typeface(sanfranciscoTypeface);
//                        query2.id(R.id.rm_welcome_text).text("音乐节奏与步频共轨契合  潜力随律动一路攀升").typeface(sanfranciscoTypeface);
                        break;
                    }
                    default:
                        break;

                }
                container.addView(mViews[position]);
            }
            return mViews[position];
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return COUNT;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg1 != null && arg1.equals(arg0);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.e(TAG, "destroy " + position);
        }

    }
}
