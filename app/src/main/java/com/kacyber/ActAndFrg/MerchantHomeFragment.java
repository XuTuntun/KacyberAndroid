package com.kacyber.ActAndFrg;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.event.MerchantDetailEvent;
import com.kacyber.model.Merchant;
import com.kacyber.model.MerchantDetail;
import com.kacyber.model.WorkHours;
import com.warmtel.expandtab.ExpandPopTabView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class MerchantHomeFragment extends Fragment implements View.OnClickListener {

    private static String TAG = MerchantHomeFragment.class.getName();

    private AQuery aQuery;
    public ImageView star1;
    public ImageView star2;
    public ImageView star3;
    public ImageView star4;
    public ImageView star5;
    public TextView merchantStarLevel;
    public ArrayAdapter<String> arrayAdapter;

    private AppCompatSpinner spinner;

    public MerchantHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_merchant_home, container, false);
        aQuery = new AQuery(view);
        aQuery.id(R.id.merchant_phone).clickable(true).clicked(this);
        aQuery.id(R.id.merchant_website).clickable(true).clicked(this);
        star1 = (ImageView) view.findViewById(R.id.review_start_1);
        star2 = (ImageView) view.findViewById(R.id.review_start_2);
        star3 = (ImageView) view.findViewById(R.id.review_start_3);
        star4 = (ImageView) view.findViewById(R.id.review_start_4);
        star5 = (ImageView) view.findViewById(R.id.review_start_5);
        merchantStarLevel = (TextView) view.findViewById(R.id.merchant_review_star_level);
        spinner = (AppCompatSpinner) view.findViewById(R.id.merchant_home_hours);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MerchantDetailActivity.currentMerchant != null) {
            aQuery.id(R.id.merchant_title).text(MerchantDetailActivity.currentMerchant.name);
            aQuery.id(R.id.merchant_category).text(MerchantDetailActivity.currentMerchant.category);
            aQuery.id(R.id.merchant_best_review).text(MerchantDetailActivity.currentMerchant.mission);
            aQuery.id(R.id.merchant_home_address).text(MerchantDetailActivity.currentMerchant.address);
            ArrayList<String> workHoursList = new ArrayList<>();

            int workHoursCursor = 1;
            for (int i = 0; i < MerchantDetailActivity.currentMerchant.workHours.size(); i++) {
                String temp = MerchantDetailActivity.currentMerchant.workHours.get(i).week + "   " + MerchantDetailActivity.currentMerchant.workHours.get(i).startTime + "-" + MerchantDetailActivity.currentMerchant.workHours.get(i).endTime;
                workHoursList.add(temp);
            }
            arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, workHoursList);
            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);

//            Calendar c = Calendar.getInstance();
//            switch (c.get(Calendar.DAY_OF_WEEK)) {
//                case c.get(Calendar.MONDAY):
//                    break;
//            }


            switch (MerchantDetailActivity.currentMerchant.gradeMark) {
                case 1:
                    merchantStarLevel.setText("1.0");
                    star1.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star2.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                    star3.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                    star4.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                    star5.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                    break;
                case 2:
                    merchantStarLevel.setText("2.0");
                    star1.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star2.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star3.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                    star4.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                    star5.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                    break;
                case 3:
                    merchantStarLevel.setText("3.0");
                    star1.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star2.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star3.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star4.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                    star5.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                    break;
                case 4:
                    merchantStarLevel.setText("4.0");
                    star1.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star2.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star3.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star4.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star5.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                    break;
                case 5:
                    merchantStarLevel.setText("5.0");
                    star1.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star2.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star3.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star4.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    star5.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.merchant_phone:
                try {
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + MerchantDetailActivity.currentMerchant.phone));
                    intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentPhone);
                } catch (Exception e) {
                    Toast.makeText(this.getActivity(), "The merchant`s phone number is empty!", Toast.LENGTH_SHORT);
                }
                break;

            case R.id.merchant_website:
                try {
                    Intent websiteIntent = new Intent();
                    websiteIntent.setClass(this.getActivity(), WebViewActivity.class);
                    websiteIntent.putExtra("url", MerchantDetailActivity.currentMerchant.website);
                    websiteIntent.putExtra("title", MerchantDetailActivity.currentMerchant.name);
                    startActivity(websiteIntent);
                } catch (Exception e) {
                    Toast.makeText(this.getActivity(), "The merchant`s webSite is empty!", Toast.LENGTH_SHORT);
                }
                break;
        }

    }

//    private static class ArrayAdapter<WorkHours> extends android.widget.ArrayAdapter {
//
//        public ArrayAdapter(Context context, int resource, ) {
//            super(context, resource);
//        }
//    }


}
