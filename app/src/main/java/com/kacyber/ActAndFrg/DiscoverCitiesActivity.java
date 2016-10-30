package com.kacyber.ActAndFrg;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.Utils.Constants;
import com.kacyber.Utils.PinyinComparator;
import com.kacyber.View.SideBar;
import com.kacyber.adapter.SortAdapter;
import com.kacyber.adapter.SortModelCities;
import com.kacyber.event.AllCitiesEvent;
import com.kacyber.event.OverseaCitiesEvent;
import com.kacyber.network.http.KacyberRestClientUsage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiscoverCitiesActivity extends AppCompatActivity implements View.OnClickListener {
    private AQuery aQuery;
    private static boolean allOversea = true;
    private ListView sortListView;

    private ArrayList<SortModelCities> SourceDateList;

    private PinyinComparator pinyinComparator;


    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_discover_cities);
        EventBus.getDefault().register(this);
        aQuery = new AQuery(this);

        pinyinComparator = new PinyinComparator();
        aQuery.id(R.id.all_switcher).clickable(true).clicked(this);
        aQuery.id(R.id.oversea_switcher).clickable(true).clicked(this);
        aQuery.id(R.id.back_text).clickable(true).clicked(this);
        aQuery.id(R.id.normal_cities_back).clickable(true).clicked(this);

        sideBar = (SideBar) findViewById(R.id.city_side_bar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        sortListView = (ListView) findViewById(R.id.city_list);


    }

    @Override
    protected void onResume() {
        super.onResume();
        allOversea = true;
        KacyberRestClientUsage.getInstance().getAllCities();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_switcher:
                sortListView.setAdapter(null);
                if (allOversea != true) {
                    allOversea = true;
                    aQuery.id(R.id.all_switcher).background(R.drawable.blue_left_2_corners).textColor(R.color.white);
                    aQuery.id(R.id.oversea_switcher).background(R.drawable.white_right_2_corners).textColor(R.color.colorPrimary);
                }
                KacyberRestClientUsage.getInstance().getAllCities();
                break;
            case R.id.oversea_switcher:
                sortListView.setAdapter(null);
                if (allOversea != false) {
                    allOversea = false;
                    aQuery.id(R.id.oversea_switcher).background(R.drawable.blue_right_2_corners).textColor(R.color.white);
                    aQuery.id(R.id.all_switcher).background(R.drawable.white_left_2_corners).textColor(R.color.colorPrimary);
                }
                KacyberRestClientUsage.getInstance().getOverseaCities();

                break;
            case R.id.back_text:
                this.finish();
                break;

            case R.id.normal_cities_back:
                this.finish();
                break;
            default:
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onAllCitiesEvent(AllCitiesEvent allCitiesEvent) {
        SourceDateList = filledData(allCitiesEvent.allCitiesList);
        Log.e("FM=============", "" + SourceDateList);
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                double latitude = ((SortModelCities) adapter.getItem(position)).lat;
                double longitude = ((SortModelCities) adapter.getItem(position)).lng;
                String cityName = ((SortModelCities) adapter.getItem(position)).name;
                Intent intent = new Intent();
                intent.putExtra("cityname", cityName);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                setResult(Constants.CITY_LIST_OK, intent);
                finish();
            }
        });
    }

    @Subscribe
    public void overseaCitiesEvent(OverseaCitiesEvent overseaCitiesEvent) {
        SourceDateList = filledData(overseaCitiesEvent.overseaCitiesList);
        Log.e("FM=============", "" + SourceDateList);
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplication(), ((SortModelCities) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private ArrayList<SortModelCities> filledData(ArrayList<SortModelCities> sortModelCities) {

        ArrayList<SortModelCities> mSortList = new ArrayList<SortModelCities>();

        for (int i = 0; i < sortModelCities.size(); i++) {
            String sortString = sortModelCities.get(i).name.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModelCities.get(i).setSortLetters(sortString.toLowerCase());
            } else {
                sortModelCities.get(i).setSortLetters("#");
            }

            mSortList.add(sortModelCities.get(i));
        }

        return mSortList;
    }
}
