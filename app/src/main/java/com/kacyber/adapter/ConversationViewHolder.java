package com.kacyber.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kacyber.R;
import com.kacyber.View.CircleNetworkImageView;

/**
 * Created by guofuming on 31/10/2016.
 */
public class ConversationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CircleNetworkImageView circleNetworkImageView;
    public ImageView chatNotiOne;
    public TextView chatNotiNumber;
    public ImageView chatSecure;
    public TextView chatTimeLastSend;
    public TextView chatTitle;
    public TextView lastMessage;
    public ConversationItemClickListener itemClickListener;



    public ConversationViewHolder(View itemView, ConversationItemClickListener conversationItemClickListener) {

        super(itemView);
        circleNetworkImageView = (CircleNetworkImageView) itemView.findViewById(R.id.chat_item);
        chatNotiOne = (ImageView) itemView.findViewById(R.id.chat_noti);
        chatNotiNumber = (TextView) itemView.findViewById(R.id.chat_noti_number);
        chatSecure = (ImageView) itemView.findViewById(R.id.chat_secure_bool);
        chatTimeLastSend = (TextView) itemView.findViewById(R.id.chat_time_last_send);
        chatTitle = (TextView) itemView.findViewById(R.id.chat_item_title);
        lastMessage = (TextView) itemView.findViewById(R.id.chat_item_message);
        this.itemClickListener = conversationItemClickListener;
        itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (itemClickListener!=null) {
            itemClickListener.onItemClick(v, getPosition());
        }
    }
}
