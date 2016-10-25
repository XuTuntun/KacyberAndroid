package com.kacyber.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.kacyber.R;
import com.kacyber.View.DiscoverFragment;
import com.kacyber.model.DealMerchant;
import com.kacyber.network.service.ImageSingleton;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by guofuming on 11/10/2016.
 */

public class DealMerchantListAdapter extends RecyclerView.Adapter<DealMerchantViewHolder> {

    private final LayoutInflater inflater;
    private final Resources res;
    private final int itemLayoutRes;
    private ImageLoader imageLoader;
    private ArrayList<DealMerchant> merchantList;
    public DealMerchantItemClickListener itemClickListener;

    public DealMerchantListAdapter(Context context, int itemLayoutRes, ArrayList<DealMerchant> dealMerchantList) {

        inflater = LayoutInflater.from(context);
        res = context.getResources();
        imageLoader = ImageSingleton.getInstance(context).getImageLoader();
        merchantList = dealMerchantList;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public DealMerchantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(itemLayoutRes, parent, false);
        return new DealMerchantViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(DealMerchantViewHolder holder, int position) {
        DealMerchant dealMerchant = merchantList.get(position);
        holder.merchantImage.setImageUrl(dealMerchant.headPicUrl, imageLoader);
        holder.merchantTitle.setText(dealMerchant.title);
        holder.merchantAddress.setText(dealMerchant.address);
//        holder.merchantCategory.setText(dealMerchant.);
        holder.viewTimes.setText(""+dealMerchant.viewCount);
        if (dealMerchant.distance<1000) {
            holder.distance.setText("~"+dealMerchant.distance+"m");
        } else {
            holder.distance.setText("~"+(int)(dealMerchant.distance/1000)+"Km");
        }
        switch (dealMerchant.score) {
            case 1:
                holder.merchantStarLevel.setText("1.0");
                holder.star1.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star2.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                holder.star3.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                holder.star4.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                holder.star5.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                break;
            case 2:
                holder.merchantStarLevel.setText("2.0");
                holder.star1.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star2.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star3.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                holder.star4.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                holder.star5.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                break;
            case 3:
                holder.merchantStarLevel.setText("3.0");
                holder.star1.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star2.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star3.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star4.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                holder.star5.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                break;
            case 4:
                holder.merchantStarLevel.setText("4.0");
                holder.star1.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star2.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star3.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star4.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star5.setBackgroundResource(R.drawable.discoverpage_star_icon_nomal);
                break;
            case 5:
                holder.merchantStarLevel.setText("5.0");
                holder.star1.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star2.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star3.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star4.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                holder.star5.setBackgroundResource(R.drawable.discoverpage_star_icon_highlighted);
                break;
            default:
                break;
        }


    }

    @Override
    public int getItemCount() {
        return merchantList.size();
    }


    public void setOnItemClickListener(DealMerchantItemClickListener listener) {
        itemClickListener = listener;
    }

}
