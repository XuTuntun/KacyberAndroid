package com.kacyber.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.androidquery.AQuery;

import com.kacyber.ActAndFrg.AddBusinessActivity;
import com.kacyber.ActAndFrg.BookmarkActivity;
import com.kacyber.ActAndFrg.CategoryActivity;
import com.kacyber.ActAndFrg.DiscoverCitiesActivity;
import com.kacyber.ActAndFrg.MerchantDetailActivity;
import com.kacyber.ActAndFrg.NormalSearchActivity;
import com.kacyber.ActAndFrg.ReviewsActivity;
import com.kacyber.ActAndFrg.SuperDealActivity;
import com.kacyber.ActAndFrg.WebViewActivity;
import com.kacyber.Control.MFGT;
import com.kacyber.PageIndicator.CirclePageIndicator;
import com.kacyber.R;
import com.kacyber.Utils.Constants;
import com.kacyber.adapter.DealMerchantItemClickListener;
import com.kacyber.adapter.DealMerchantListAdapter;
import com.kacyber.dialog.ActionItem;
import com.kacyber.dialog.TitlePopup;
import com.kacyber.event.MainMerchantListEvent;
import com.kacyber.model.DealMerchant;
import com.kacyber.model.EventItem;
import com.kacyber.model.TrendingItem;
import com.kacyber.network.http.KacyberRestClient;
import com.kacyber.network.http.KacyberRestClientUsage;
import com.kacyber.network.service.ImageSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by guofuming on 12/8/16.
 */
public class DiscoverFragment extends Fragment implements View.OnClickListener, DealMerchantItemClickListener {

    private static String TAG = DiscoverFragment.class.getName();

    private ViewPager viewPager = null;
    private CirclePageIndicator circlePageIndicator;
    private Context applicationContext;
    private RecyclerView recyclerView;
    private AQuery aQuery;
    private ArrayList<DealMerchant> merchantArrayList;
    private ArrayList<EventItem> eventItems;
    private ArrayList<TrendingItem> trendingItems;
    private TitlePopup titlePopup;
    private NetworkImageView trendingImage1;
    private NetworkImageView trendingImage2;
    private NetworkImageView eventImage1;
    private NetworkImageView eventImage2;
    private ImageLoader imageLoader;
    private static String trendingOneUrl;
    private static String trendingTwoUrl;
    private static String eventOneUrl;
    private static String eventTwoUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_layout, null);
        aQuery = new AQuery(view);

        EventBus.getDefault().register(this);
        applicationContext = getActivity().getApplicationContext();

        viewPager = (ViewPager) view.findViewById(R.id.discover_view_pager);
        viewPager.setAdapter(new ViewPageAdapter());

        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.discover_pager_indicator);
        circlePageIndicator.setViewPager(viewPager);
//        circlePageIndicator.setRadius(12);
//        circlePageIndicator.setStrokeWidth(3);
//        circlePageIndicator.setFillColor(Color.GRAY);
//        circlePageIndicator.setGap(10);

        recyclerView = (RecyclerView) view.findViewById(R.id.discover_main_page_merchant);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        aQuery.id(R.id.discover_deal_container).clickable(true).clicked(this);
        aQuery.id(R.id.discover_place).clickable(true).clicked(this);
        aQuery.id(R.id.trending_1).clickable(true).clicked(this);
        aQuery.id(R.id.trending_2).clickable(true).clicked(this);
        aQuery.id(R.id.events_1).clickable(true).clicked(this);
        aQuery.id(R.id.events_2).clickable(true).clicked(this);
        aQuery.id(R.id.discover_more).clickable(true).clicked(this);
        aQuery.id(R.id.discover_search).clickable(true).clicked(this);
        eventImage1 = (NetworkImageView) view.findViewById(R.id.events_1);
        eventImage2 = (NetworkImageView) view.findViewById(R.id.events_2);
        trendingImage1 = (NetworkImageView) view.findViewById(R.id.trending_1_image);
        trendingImage2 = (NetworkImageView) view.findViewById(R.id.trending_2_image);
        initPopWindow();
        imageLoader = ImageSingleton.getInstance(this.getActivity()).getImageLoader();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        KacyberRestClientUsage.getInstance().getDiscoverMainPage();
    }

    private void initPopWindow() {
        titlePopup = new TitlePopup(this.getActivity(), ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.setItemOnClickListener(onitemClick);
        // 给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(this.getActivity(), "Reviews",
                R.drawable.discoverpage_reviews_icon));
        titlePopup.addAction(new ActionItem(this.getActivity(), "Bookmarks",
                R.drawable.discoverpage_favorites_icon));
        titlePopup.addAction(new ActionItem(this.getActivity(), "Add Business",
                R.drawable.discoverpage_add_icon));
        titlePopup.addAction(new ActionItem(this.getActivity(), "Help",
                R.drawable.discoverpage_help_icon));

        titlePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                aQuery.id(R.id.discover_more).background(R.drawable.discoverpage_nav_more_icon);
            }
        });
    }

    public void ChannelClickedByViewId(int id, String categoryName) {
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
            case R.id.discover_channel_tour_travel:
                categoryId = 92;
                break;
            case R.id.discover_channel_agricultrue:
                categoryId = 58;
                break;
            case R.id.discover_channel_finance:
                categoryId = 73;
                break;
            case R.id.discover_channel_real_estate:
                categoryId = 66;
                break;
            case R.id.discover_channel_transport:
                categoryId = 81;
                break;
            case R.id.discover_channel_hotels:
                categoryId = 100;
                break;
            case R.id.discover_channel_health:
                categoryId = 106;
                break;
            case R.id.discover_channel_beauty:
                categoryId = 113;
                break;
            case R.id.discover_channel_services:
                categoryId = 120;
                break;
            case R.id.discover_channel_all:
                categoryId = 1;
                break;


        }
        Intent intent = new Intent();
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("categoryName", categoryName);
        intent.setClass(this.getActivity(), CategoryActivity.class);
        startActivity(intent);
//        KacyberRestClientUsage.getInstance().getCategoryById(categoryId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.discover_deal_container:
//                SuperDealActivity superDealFragment = new SuperDealActivity();
//                FragmentManager fragmentManager = this.getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.content_frame, superDealFragment).commit();
                Intent intent = new Intent();
                intent.setClass(this.getActivity(), SuperDealActivity.class);
                startActivity(intent);
                break;

            case R.id.discover_place:

                Intent cityIntent = new Intent();
                cityIntent.setClass(this.getActivity(), DiscoverCitiesActivity.class);
                startActivityForResult(cityIntent, 0);
                break;

            case R.id.trending_1:

                Intent trendingIntent = new Intent();

                trendingIntent.putExtra("title", "Trending");
                if (trendingOneUrl!=null) {
                    trendingIntent.putExtra("url", trendingOneUrl);

                } else {
                    trendingIntent.putExtra("url", Constants.KACYBER_MAIN_PAGE);
                }

                trendingIntent.setClass(this.getActivity(), WebViewActivity.class);

                startActivity(trendingIntent);

                break;

            case R.id.trending_2:

                Intent trendingIntent2 = new Intent();
                trendingIntent2.setClass(this.getActivity(), WebViewActivity.class);
                trendingIntent2.putExtra("title", "Trending");
                if (trendingTwoUrl!=null) {
                    trendingIntent2.putExtra("url", trendingTwoUrl);
                } else {
                    trendingIntent2.putExtra("url", Constants.KACYBER_MAIN_PAGE);
                }
                startActivity(trendingIntent2);

                break;

            case R.id.events_1:

                Intent eventIntent = new Intent();
                eventIntent.setClass(this.getActivity(), WebViewActivity.class);
                eventIntent.putExtra("title", "Event");
                if (eventOneUrl!=null) {
                    eventIntent.putExtra("url", eventOneUrl);
                } else {
                    eventIntent.putExtra("url", Constants.KACYBER_MAIN_PAGE);
                }
                startActivity(eventIntent);

                break;

            case R.id.events_2:

                Intent eventIntent2 = new Intent();
                eventIntent2.setClass(this.getActivity(), WebViewActivity.class);
                eventIntent2.putExtra("title", "Event");
                if (eventTwoUrl!=null) {
                    eventIntent2.putExtra("url", eventTwoUrl);
                } else {
                    eventIntent2.putExtra("url", Constants.KACYBER_MAIN_PAGE);
                }                startActivity(eventIntent2);

                break;

            case R.id.discover_more:
                Log.e(TAG, "right corner clicked titlePopup is " + titlePopup);
                titlePopup.show(this.getActivity().findViewById(R.id.discover_header));
                aQuery.id(R.id.discover_more).background(R.drawable.discoverpage_nav_more_smile_icon);
                break;

            case R.id.discover_search:
                Intent searchIntent = new Intent();
                searchIntent.setClass(this.getActivity(), NormalSearchActivity.class);
                startActivity(searchIntent);
            default:

                break;



        }
    }

    @Subscribe
    public void onMainMerchantListEvent(MainMerchantListEvent event) {
        Log.e(TAG, "merchantArrayList size is " + event.merchantList.size());
        merchantArrayList = event.merchantList;
        eventItems = event.eventItems;
        trendingItems = event.trendingItems;


        DealMerchantListAdapter dealMerchantListAdapter = new DealMerchantListAdapter(this.getContext(), R.layout.merchant_item_layout, merchantArrayList);
        dealMerchantListAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(dealMerchantListAdapter);
        try {
            trendingImage1.setImageUrl(trendingItems.get(0).picUrl, imageLoader);
            trendingImage2.setImageUrl(trendingItems.get(1).picUrl, imageLoader);
            eventImage1.setImageUrl(eventItems.get(0).picUrl, imageLoader);
            if (eventItems.size()>1) {
                eventImage2.setImageUrl(eventItems.get(1).picUrl, imageLoader);
            } else {
                eventImage2.setVisibility(View.GONE);
            }
            aQuery.id(R.id.trending_1_title).text(trendingItems.get(0).title);
            aQuery.id(R.id.trending_1_description).text(trendingItems.get(0).subTitle);
            aQuery.id(R.id.trending_2_title).text(trendingItems.get(1).title);
            aQuery.id(R.id.trending_2_description).text(trendingItems.get(1).subTitle);
            trendingOneUrl = trendingItems.get(0).link;
            trendingTwoUrl = trendingItems.get(1).link;
            eventOneUrl = eventItems.get(0).link;
            eventTwoUrl = eventItems.get(1).link;
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    @Override
    public void onItemClick(View viewm, int position) {
        if (merchantArrayList!=null) {
            if (merchantArrayList.size()!=0) {
                DealMerchant dealMerchant = merchantArrayList.get(position);
                Intent intent = new Intent();
                intent.setClass(this.getActivity(), MerchantDetailActivity.class);
                intent.putExtra("merchantID", dealMerchant.merchantId);
                this.startActivity(intent);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
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
                        final AQuery query2 = new AQuery(mViews[0]);
                        query2.id(R.id.discover_channel_super_deals).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "Super Deals");
                            }
                        });
                        query2.id(R.id.discover_channel_food_drink).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "Food & Drink");
                            }
                        });
                        query2.id(R.id.discover_channel_shopping).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "Shopping");

                            }
                        });
                        query2.id(R.id.discover_channel_entertainment).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "Leisure");
                            }
                        });
                        query2.id(R.id.discover_channel_tour_travel).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "Tour & Travel");
                            }
                        });
                        query2.id(R.id.discover_channel_agricultrue).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "Agriculture");
                            }
                        });
                        query2.id(R.id.discover_channel_finance).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "Finance & Isurance");
                            }
                        });
                        query2.id(R.id.discover_channel_real_estate).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "Real Estate");
                            }
                        });


                        break;
                    }
                    case 1: {
                        mViews[1] = inflater.inflate(R.layout.discover_category_header2, null);
                        Log.e(TAG, "position is" + position);
                        AQuery query2 = new AQuery(mViews[1]);
                        query2.id(R.id.discover_channel_transport).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "Transport");
                            }
                        });
                        query2.id(R.id.discover_channel_hotels).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "Hotels");
                            }
                        });
                        query2.id(R.id.discover_channel_health).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "Health");
                            }
                        });
                        query2.id(R.id.discover_channel_beauty).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "Beauty");
                            }
                        });
                        query2.id(R.id.discover_channel_services).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "OTHERS SERVICES");
                            }
                        });
                        query2.id(R.id.discover_channel_all).clickable(true).clicked(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChannelClickedByViewId(v.getId(), "All Categories");
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

    private TitlePopup.OnItemOnClickListener onitemClick = new TitlePopup.OnItemOnClickListener() {

        @Override
        public void onItemClick(ActionItem item, int position) {
            switch (position) {
                case 0:// Reviews
                    Intent reviewsIntent = new Intent();
                    reviewsIntent.setClass(getActivity(), ReviewsActivity.class);
                    startActivity(reviewsIntent);
                    break;
                case 1:// BookMarks
                    Intent bookmarksIntent = new Intent();
                    bookmarksIntent.setClass(getActivity(), BookmarkActivity.class);
                    startActivity(bookmarksIntent);
                    break;
                case 2:// Add Business

                    Intent addBusinessIntent = new Intent();
                    addBusinessIntent.setClass(getActivity(), AddBusinessActivity.class);
                    startActivity(addBusinessIntent);

                    break;
                case 3://Help

                    Intent helpIntent = new Intent();
                    helpIntent.putExtra("title", "Help");
                    helpIntent.putExtra("url", Constants.HELP_URL);
                    helpIntent.setClass(getActivity(), WebViewActivity.class);
                    startActivity(helpIntent);


                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "requestCode is " + requestCode + " resultCode is " + resultCode + " intent is " +data);
        switch (resultCode) {
            case Constants.CITY_LIST_OK:
                Bundle bundle = data.getExtras();
                String cityName = bundle.getString("cityname");
                double longitude = bundle.getDouble("longitude");
                double latitude = bundle.getDouble("latitude");
                KacyberRestClientUsage.getInstance().getDiscoverMainPageByLatLng(longitude, latitude);
                aQuery.id(R.id.discover_place_text).text(cityName);
                break;

        }
    }

}
