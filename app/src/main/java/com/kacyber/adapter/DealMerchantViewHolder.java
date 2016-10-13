package com.kacyber.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.kacyber.R;

/**
 * Created by guofuming on 11/10/2016.
 */
public class DealMerchantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public NetworkImageView merchantImage;
    public TextView merchantTitle;
    public TextView merchantStarLevel;
    public TextView merchantReviewCount;
    public TextView merchantAddress;
    public TextView merchantCategory;
    public TextView viewTimes;
    public TextView distance;
    public ImageView star1;
    public ImageView star2;
    public ImageView star3;
    public ImageView star4;
    public ImageView star5;
    public DealMerchantItemClickListener itemClickListener;


    public DealMerchantViewHolder(View view, DealMerchantItemClickListener itemClickListener) {
        super(view);
        merchantImage = (NetworkImageView) view.findViewById(R.id.merchant_item_image);
        merchantTitle = (TextView) view.findViewById(R.id.merchant_title);
        merchantStarLevel = (TextView) view.findViewById(R.id.merchant_review_star_level);
        merchantReviewCount = (TextView) view.findViewById(R.id.merchant_review_star_count);
        merchantAddress = (TextView) view.findViewById(R.id.merchant_address);
        merchantCategory = (TextView) view.findViewById(R.id.merchant_category);
        viewTimes = (TextView) view.findViewById(R.id.merchant_browse_number);
        distance = (TextView) view.findViewById(R.id.merchant_distance);
        star1 = (ImageView) view.findViewById(R.id.review_start_1);
        star2 = (ImageView) view.findViewById(R.id.review_start_2);
        star3 = (ImageView) view.findViewById(R.id.review_start_3);
        star4 = (ImageView) view.findViewById(R.id.review_start_4);
        star5 = (ImageView) view.findViewById(R.id.review_start_5);
        this.itemClickListener = itemClickListener;
        view.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (itemClickListener!=null) {
            itemClickListener.onItemClick(v, getPosition());
        }
    }
}
