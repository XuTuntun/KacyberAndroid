package com.kacyber.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.kacyber.R;
import com.kacyber.View.CircleNetworkImageView;

/**
 * Created by guofuming on 4/11/2016.
 */

public class SendedMessageViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    public CircleNetworkImageView headImage;
    public TextView messageTime;
    public TextView messageText;
    public NetworkImageView image;

    public MessageLongTouchListener messageLongTouchListener;
    public SendedMessageViewHolder(View itemView, MessageLongTouchListener messageLongTouchListener) {
        super(itemView);
        headImage = (CircleNetworkImageView) itemView.findViewById(R.id.sender_head);
        messageTime = (TextView) itemView.findViewById(R.id.sender_time);
        messageText = (TextView) itemView.findViewById(R.id.sender_text);
        image = (NetworkImageView) itemView.findViewById(R.id.sender_iamge);
        this.messageLongTouchListener = messageLongTouchListener;
        itemView.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        if (messageLongTouchListener!=null) {
            return messageLongTouchListener.onLongTouch(v, getPosition());
        }
        return false;
    }
}
