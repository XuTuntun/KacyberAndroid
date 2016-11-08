package com.kacyber.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.kacyber.model.DealMerchant;
import com.kacyber.model.User;
import com.kacyber.network.http.KacyberRestClientUsage;
import com.kacyber.network.service.ImageSingleton;

import java.util.ArrayList;

/**
 * Created by guofuming on 1/11/2016.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private final LayoutInflater inflater;
    private final Resources res;
    private final int itemLayoutRes;
    private ImageLoader imageLoader;
    private ArrayList<User> userList;
    public UserItemClickListener itemClickListener;

    public ContactAdapter(Context context, int itemLayoutRes, ArrayList<User> contactList) {

        inflater = LayoutInflater.from(context);
        res = context.getResources();
        imageLoader = ImageSingleton.getInstance(context).getImageLoader();
        userList = contactList;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(itemLayoutRes, parent, false);
        return new ContactViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        final User user = userList.get(position);
        holder.circleNetworkImageView.setImageUrl(user.avatar, imageLoader);
        holder.userName.setText(user.nickName);
        holder.userID.setText("UserID:" + user.id);
        if (user.friend) {
            holder.friend.setText("Added");
            holder.friend.setTextColor(Color.BLACK);
            holder.friend.setBackgroundColor(Color.WHITE);
            holder.friend.setClickable(false);
        } else {
            holder.friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KacyberRestClientUsage.getInstance().addFriend(user.id, "Hello I`m " + user.nickName);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setOnItemClickListener(UserItemClickListener listener) {
        itemClickListener = listener;
    }
}
