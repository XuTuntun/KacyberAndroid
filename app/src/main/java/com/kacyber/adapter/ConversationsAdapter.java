package com.kacyber.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.kacyber.Utils.Util;
import com.kacyber.message.Message;
import com.kacyber.model.Conversation;
import com.kacyber.model.User;
import com.kacyber.network.service.ImageSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by guofuming on 31/10/2016.
 */

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationViewHolder> {

    private final LayoutInflater inflater;
    private final Resources res;
    private final int itemLayoutRes;
    private ImageLoader imageLoader;
    private RealmResults<Conversation> conversationList;
    public ConversationItemClickListener itemClickListener;

    public ConversationsAdapter(Context context, int itemLayoutRes, RealmResults<Conversation> conversationListInput) {

        inflater = LayoutInflater.from(context);
        res = context.getResources();
        imageLoader = ImageSingleton.getInstance(context).getImageLoader();
        conversationList = conversationListInput.sort("lastSendTime", Sort.DESCENDING);
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(itemLayoutRes, parent, false);
        return new ConversationViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        final Conversation conversation = conversationList.get(position);
//        holder.circleNetworkImageView.setImageUrl(conversation.ur);
        if (conversation.unReadCount == 1) {
            holder.chatNotiOne.setVisibility(View.VISIBLE);
            holder.chatNotiNumber.setVisibility(View.INVISIBLE);
        } else if (conversation.unReadCount == 0) {
            holder.chatNotiOne.setVisibility(View.GONE);
            holder.chatNotiNumber.setVisibility(View.GONE);
        } else {
            holder.chatNotiOne.setVisibility(View.INVISIBLE);
            holder.chatNotiNumber.setVisibility(View.VISIBLE);
            holder.chatNotiNumber.setText(""+conversation.unReadCount);
        }
        if (conversation.encrypt!=null) {
            holder.chatSecure.setVisibility(View.VISIBLE);
        } else {
            holder.chatSecure.setVisibility(View.INVISIBLE);
        }
        holder.chatTimeLastSend.setText(Util.timeStamp2Date(conversation.lastSendTime));
        holder.chatTitle.setText(""+conversation.title);
        if (conversation.messagesList.size()!=0) {
            Message message = conversation.messagesList.last();
            if (message.msgType.equals("TEXT")) {
                try {
                    JSONObject jsonObject = new JSONObject(conversation.messagesList.last().body);
                    holder.lastMessage.setText(jsonObject.getString("text"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                holder.lastMessage.setText("[" + conversation.messagesList.last().msgType +"]");
            }
        } else {
            holder.lastMessage.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public void setData(RealmResults<Conversation> conversations) {
        this.conversationList = conversations;
    }

    public void setOnItemClickListener(ConversationItemClickListener itemClickListenerInput) {
        itemClickListener = itemClickListenerInput;
    }
}
