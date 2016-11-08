package com.kacyber.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.toolbox.ImageLoader;
import com.kacyber.ActAndFrg.NormalChatActivity;
import com.kacyber.Utils.Constants;
import com.kacyber.Utils.Util;
import com.kacyber.message.Message;
import com.kacyber.message.MessageType;
import com.kacyber.model.Conversation;
import com.kacyber.network.service.ImageSingleton;

import org.json.JSONException;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by guofuming on 3/11/2016.
 */

public class MessageChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String TAG = MessageChatAdapter.class.getName();

    private RealmResults<Message> messageList;

    private final LayoutInflater inflater;
    private final Resources res;
    private final int itemSendedLayout;
    private final int itemReceivedLayout;
    private ImageLoader imageLoader;
    private MessageLongTouchListener messageLongTouchListener;

    public MessageChatAdapter(Context context, int itemSendedLayout, int itemReceivedLayout, RealmResults<Message> messageListInput) {

        inflater = LayoutInflater.from(context);
        res = context.getResources();
        imageLoader = ImageSingleton.getInstance(context).getImageLoader();
        messageList = messageListInput;
        this.itemSendedLayout = itemSendedLayout;
        this.itemReceivedLayout = itemReceivedLayout;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Log.e("ADAPTER", "view type is " + viewType);
        switch (viewType) {
            case Constants.MESSAGE_RECEIVED:
                view = inflater.inflate(itemReceivedLayout, parent, false);
                break;
            case Constants.MESSAGE_SENDED:
                Log.e("adapter", "Message Sended");
                view = inflater.inflate(itemSendedLayout, parent, false);
                return new SendedMessageViewHolder(view, messageLongTouchListener);
            default:
                view = inflater.inflate(itemReceivedLayout, parent, false);
        }

        return new ReceivedMessageViewHolder(view, messageLongTouchListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder.getItemViewType()==Constants.MESSAGE_SENDED) {
            SendedMessageViewHolder sendedMessageViewHolder = (SendedMessageViewHolder) holder;
            sendedMessageViewHolder.messageTime.setText(Util.timeStamp2DateShort(message.sendTime));
            switch (message.msgType) {
                case "TEXT":
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(message.body);
                        sendedMessageViewHolder.messageText.setText(jsonObject.getString("text"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "IMAGE":
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(message.body);
                        sendedMessageViewHolder.messageText.setVisibility(View.GONE);
                        sendedMessageViewHolder.image.setVisibility(View.VISIBLE);
                        sendedMessageViewHolder.image.setImageUrl(jsonObject.getString("fileUrl"), imageLoader);
                        Log.e(TAG, "Message Image fileUrl is" + jsonObject.getString("fileUrl"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
        } else {
            ReceivedMessageViewHolder receivedMessageViewHolder = (ReceivedMessageViewHolder) holder;
            receivedMessageViewHolder.messageTime.setText(Util.timeStamp2DateShort(message.sendTime));
            switch (message.msgType) {
                case "TEXT":
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(message.body);
                        Log.e(TAG, jsonObject.getString("text"));
                        receivedMessageViewHolder.messageText.setText(jsonObject.getString("text"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "IMAGE":
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(message.body);
                        receivedMessageViewHolder.messageText.setVisibility(View.GONE);
                        receivedMessageViewHolder.image.setVisibility(View.VISIBLE);
                        receivedMessageViewHolder.image.setImageUrl(jsonObject.getString("fileUrl"), imageLoader);
                        Log.e(TAG, "Message Image fileUrl is" + jsonObject.getString("fileUrl"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }


    }



    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).senderId == Constants.USER_ID) {
            return Constants.MESSAGE_SENDED;
        } else {
            return Constants.MESSAGE_RECEIVED;
        }

    }

    public void setOnLongTouchListener(MessageLongTouchListener longTouchListener) {
        this.messageLongTouchListener = longTouchListener;
    }

    public void setData(RealmResults<Message> list) {
        this.messageList = list;
    }

}
