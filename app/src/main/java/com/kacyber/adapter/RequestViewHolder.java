package com.kacyber.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kacyber.R;
import com.kacyber.View.CircleNetworkImageView;


/**
 * Created by guofuming on 1/11/2016.
 */

public class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public CircleNetworkImageView circleNetworkImageView;
    public TextView userName;
    public TextView userID;
    public TextView friend;
    public UserItemClickListener itemClickListener;


    public RequestViewHolder(View itemView, RequestItemClickListener userItemClickListener) {
        super(itemView);
        circleNetworkImageView = (CircleNetworkImageView) itemView.findViewById(R.id.user_item_avatar);
        userName = (TextView) itemView.findViewById(R.id.user_item_title);
        userID = (TextView) itemView.findViewById(R.id.user_item_userid);
        friend = (TextView) itemView.findViewById(R.id.user_item_friend);
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener!=null) {
            itemClickListener.onItemClick(v, getPosition());
        }

    }
}
