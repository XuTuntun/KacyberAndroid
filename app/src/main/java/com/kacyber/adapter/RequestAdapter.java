package com.kacyber.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.kacyber.model.AddFriendRequest;
import com.kacyber.model.User;
import com.kacyber.network.http.KacyberRestClient;
import com.kacyber.network.http.KacyberRestClientUsage;
import com.kacyber.network.service.ImageSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guofuming on 1/11/2016.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {
    private final LayoutInflater inflater;
    private final Resources res;
    private final int itemLayoutRes;
    private ImageLoader imageLoader;
    private List<AddFriendRequest> userList;
    public RequestItemClickListener itemClickListener;

    public RequestAdapter(Context context, int itemLayoutRes, List<AddFriendRequest> contactList) {

        inflater = LayoutInflater.from(context);
        res = context.getResources();
        imageLoader = ImageSingleton.getInstance(context).getImageLoader();
        userList = contactList;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(itemLayoutRes, parent, false);
        return new RequestViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, int position) {
        final AddFriendRequest request = userList.get(position);
//        holder.circleNetworkImageView.setImageUrl(user.avatar, imageLoader);
        holder.userName.setText(""+ request.fromUserId);
        holder.userID.setText(request.message);
        holder.friend.setText("Accept");
        holder.friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KacyberRestClientUsage.getInstance().acceptFriendRequest(request.requestId);
                holder.friend.setText("Added");
                holder.friend.setTextColor(Color.BLACK);
                holder.friend.setBackgroundColor(Color.WHITE);
                holder.friend.setClickable(false);
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setOnItemClickListener(RequestItemClickListener listener) {
        itemClickListener = listener;
    }
}
