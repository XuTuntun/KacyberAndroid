package com.kacyber.View;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.tools.SystemBarTintManager;

public class MainActivity extends AppCompatActivity implements NewChatsFragment.OnFragmentInteractionListener, NewContactsFragment.OnFragmentInteractionListener, NewMoreFragment.OnFragmentInteractionListener {
    private static String TAG = MainActivity.class.getName();

    public Fragment contentFragment;
    public FragmentTabHost mTabHost;

    public static String currentTab_ = "Chats";

    private LayoutInflater layoutInflater;
    private AQuery aQuery;

    @SuppressWarnings("rawtypes")
    private final Class fragmentArray[] = {
            NewChatsFragment.class, NewContactsFragment.class, DiscoverFragment.class, NewMoreFragment.class
    };

    private int mImageViewArray[] = {R.drawable.tab_weixin, R.drawable.tab_contact_list, R.drawable.tab_find, R.drawable.tab_profile};

    private final String mTextviewArray[] = {"Chats", "Contacts", "Discover", "More"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        ActionBar actionBar = this.getActionBar();
//        actionBar.hide();

        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarAlpha((float)255);
//        tintManager.setTintColor(Color.parseColor("#3ea1f5"));


        initView();


    }

    private void initView() {
        layoutInflater = LayoutInflater.from(this);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.content_frame);

        int countFragment = fragmentArray.length;

        for (int i = 0; i < countFragment; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));

            mTabHost.addTab(tabSpec, fragmentArray[i], null);

            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.tab_bar_background);

            mTabHost.getTabWidget().setStripEnabled(false);
        }

        TabWidget mTabWidget = (TabWidget) findViewById(android.R.id.tabs);

        mTabWidget.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                selectTabItem(tabId);
            }
        });
    }

    @SuppressLint("InflateParams")
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_layout, null);
        // tab item的图
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        // tab item 的字
         TextView textView = (TextView) view.findViewById(R.id.tab_textview);
         textView.setText(mTextviewArray[index]);
        return view;
    }

    /**
     * Tab Bar的选择按键逻辑
     * @param tabId
     */
    public void selectTabItem(String tabId) {

        currentTab_ = tabId;
        contentFragment = getSupportFragmentManager().findFragmentByTag(currentTab_);
        Log.e(TAG, "contentFragment is " + contentFragment);
        Log.e(TAG, "currentTab is " + currentTab_);
        if (contentFragment!=null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, contentFragment).commit();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
