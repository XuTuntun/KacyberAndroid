package com.kacyber.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.kacyber.model.Contact;
import com.kacyber.model.User;
import com.kacyber.network.http.KacyberRestClientUsage;
import com.kacyber.network.service.ImageSingleton;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by guofuming on 7/11/2016.
 */

public class RealContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private final LayoutInflater inflater;
    private final Resources res;
    private final int itemLayoutRes;
    private ImageLoader imageLoader;
    private RealmResults<Contact> contactList;
    public UserItemClickListener itemClickListener;

    public RealContactAdapter(Context context, int itemLayoutRes, RealmResults contactList) {
        inflater = LayoutInflater.from(context);
        res = context.getResources();
        imageLoader = ImageSingleton.getInstance(context).getImageLoader();
        this.contactList = contactList;
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
        Log.e("RealContactAdapter", "position is " + position);
        final Contact contact = contactList.get(position);
        holder.circleNetworkImageView.setImageUrl(contact.avatarUrl, imageLoader);
        holder.userName.setText(contact.userName);
        holder.userID.setText("UserID:" + contact.userId);
//        if (user.friend) {
//            holder.friend.setText("Added");
//            holder.friend.setTextColor(Color.BLACK);
//            holder.friend.setBackgroundColor(Color.WHITE);
//            holder.friend.setClickable(false);
//        } else {
//            holder.friend.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    KacyberRestClientUsage.getInstance().addFriend(user.id, "Hello I`m " + user.nickName);
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void setOnItemClickListener(UserItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setData(RealmResults<Contact> contacts) {
        this.contactList = contacts;
    }
}
