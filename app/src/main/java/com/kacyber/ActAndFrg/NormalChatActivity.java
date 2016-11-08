package com.kacyber.ActAndFrg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ListView;

//import com.kacyber.Emoji.Message;
//import com.kacyber.Emoji.UnicodeToEmoji;
import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.Utils.Constants;
import com.kacyber.Utils.UnicodeToEmoji;
import com.kacyber.Utils.Util;
import com.kacyber.adapter.ConversationsAdapter;
import com.kacyber.adapter.MessageChatAdapter;
import com.kacyber.adapter.MessageLongTouchListener;
import com.kacyber.event.ReceiveMessageEvent;
import com.kacyber.message.Message;
import com.kacyber.model.Conversation;
import com.kacyber.network.http.HttpResponseHandler;
import com.kacyber.network.http.KacyberRestClient;
import com.kacyber.network.http.KacyberRestClientUsage;
import com.melink.baseframe.utils.DensityUtils;
import com.melink.bqmmsdk.bean.Emoji;
import com.melink.bqmmsdk.sdk.BQMM;
import com.melink.bqmmsdk.sdk.BQMMMessageHelper;
import com.melink.bqmmsdk.sdk.IBqmmSendMessageListener;
import com.melink.bqmmsdk.ui.keyboard.BQMMKeyboard;
import com.melink.bqmmsdk.ui.keyboard.IBQMMUnicodeEmojiProvider;
import com.melink.bqmmsdk.widget.BQMMEditView;
import com.melink.bqmmsdk.widget.BQMMSendButton;


import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.conn.AbstractClientConnAdapter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.Sort;

public class NormalChatActivity extends FragmentActivity implements View.OnClickListener, MessageLongTouchListener {
    private static String TAG = NormalChatActivity.class.getName();
    private AQuery aQuery;

    private View mMainContainer;
    private View inputbox;
    private static long conversationId;
    private static long senderUserId;


    private Conversation conversation;
    private static long toUserId;
    private Realm realm;
    private RealmResults<Message> messageList;

    private Rect tmp = new Rect();
    private int mScreenHeight;

    private BQMMSendButton bqmmSend;
    private CheckBox bqmmKeyboardOpen;
    private BQMMEditView bqmmEditView;
    private BQMM bqmmsdk;
    private BQMMKeyboard bqmmKeyboard;
    private final String LAST_KEYBOARD_HEIGHT = "last_keyboard_height";

    private static Context context;
    private final int DISTANCE_SLOP = 180;
    private RecyclerView recyclerView;
    private MessageChatAdapter messageChatAdapter;

    private boolean mPendingShowPlaceHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_chat);
        context = this;
        Intent intent = getIntent();
        realm = Realm.getDefaultInstance();
        EventBus.getDefault().register(this);
        if (intent != null) {
            conversationId = intent.getLongExtra("conversationId", 0);

            aQuery = new AQuery(this);
            aQuery.id(R.id.chats_back).clickable(true).clicked(this);
            aQuery.id(R.id.chat_person).clickable(true).clicked(this);
            recyclerView = (RecyclerView) findViewById(R.id.chat_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            conversation = realm.where(Conversation.class).equalTo("id", conversationId).findFirst();
            if (realm.isInTransaction())
                realm.commitTransaction();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    conversation.setUnReadCount(0);
                }
            });
            if (conversation.adminUserId==Constants.USER_ID) {
                toUserId = conversation.targetUserId;
            } else {
                toUserId = conversation.adminUserId;
            }
            KacyberRestClientUsage.getInstance().getUserById(toUserId);
            messageList = realm.where(Message.class).equalTo("conversationId", conversationId).findAll().sort("sendTime", Sort.ASCENDING);
            messageChatAdapter = new MessageChatAdapter(this, R.layout.send_message_layout, R.layout.received_message_layout, messageList);
            messageChatAdapter.setOnLongTouchListener(this);
            recyclerView.setAdapter(messageChatAdapter);
            messageList.addChangeListener(new RealmChangeListener<RealmResults<Message>>() {
                @Override
                public void onChange(RealmResults<Message> element) {
                    Log.e(TAG, "messageList Change");
                    messageChatAdapter.setData(messageList);
                    messageChatAdapter.notifyDataSetChanged();
                    recyclerView.invalidate();
                    if (messageList.size() != 0) {
                        recyclerView.smoothScrollToPosition(messageList.size() - 1);
                    }
                }
            });


            initView();
        }

    }

    private void initView() {
        inputbox = findViewById(R.id.message_tool_box);
        mMainContainer = findViewById(R.id.main_container);
        bqmmKeyboard = (BQMMKeyboard) findViewById(R.id.chat_msg_input_box);
//        mRealListView = (ListView) findViewById(R.id.chat_listview);
//        mRealListView.setSelector(android.R.color.transparent);
        bqmmSend = (BQMMSendButton) findViewById(R.id.chatbox_send);
        bqmmKeyboardOpen = (CheckBox) findViewById(R.id.chatbox_open);
        bqmmEditView = (BQMMEditView) findViewById(R.id.chatbox_message);
        bqmmEditView.requestFocus();

        /**
         * BQMM集成
         * 加载SDK
         */
        bqmmsdk = BQMM.getInstance();
        // 初始化表情MM键盘，需要传入关联的EditView,SendBtn
        bqmmsdk.setEditView(bqmmEditView);
        bqmmsdk.setKeyboard(bqmmKeyboard);
        bqmmsdk.setSendButton(bqmmSend);
        bqmmsdk.setLanguage(BQMM.LANGUAGE_CONSTANTS.EN);
        bqmmsdk.setRegion(BQMM.REGION_CONSTANTS.OTHERS);
        UnicodeToEmoji.initPhotos(this);
        bqmmsdk.setUnicodeEmojiProvider(new IBQMMUnicodeEmojiProvider() {
            @Override
            public Drawable getDrawableFromCodePoint(int i) {
                return UnicodeToEmoji.EmojiImageSpan.getEmojiDrawable(i);
            }
        });
        bqmmsdk.load();
        bqmmsdk.setLanguage(BQMM.LANGUAGE_CONSTANTS.EN);
        bqmmsdk.setRegion(BQMM.REGION_CONSTANTS.OTHERS);
        /*
         * 默认方式打开软键盘时切换表情符号的状态
		 */
        bqmmEditView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bqmmKeyboardOpen.setChecked(false);
                return false;
            }
        });
        /**
         * BQMM集成
         * 实现输入联想
         */
        bqmmEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                BQMM.getInstance().startShortcutPopupWindowByoffset(NormalChatActivity.this, s.toString(), bqmmSend, 0, DensityUtils.dip2px(NormalChatActivity.this, 4));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        /**
         * BQMM集成
         * 设置发送消息的回调
         */
        bqmmsdk.setBqmmSendMsgListener(new IBqmmSendMessageListener() {
            /**
             * 单个大表情消息
             */
            @Override
            public void onSendFace(final Emoji face) {
                Log.e(TAG, "onSendFace");
                face.getPathofImage();
                String imageUrl = face.getMainImage();

                final JSONObject jsonObjectMessage = new JSONObject();
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObjectMessage.put("payloadType", "MESSAGE");
                    jsonObjectMessage.put("conversationId", conversationId);
                    jsonObjectMessage.put("sendTime", System.currentTimeMillis());
                    jsonObjectMessage.put("senderId", "" + Constants.USER_ID);
                    jsonObjectMessage.put("senderUserName", "Hello");
                    jsonObjectMessage.put("toUserId", "" + conversation.adminUserId);
                    jsonObjectMessage.put("toUserName", "Hello");
                    jsonObject.put("fileUrl", face.getMainImage());
                    jsonObjectMessage.put("body", jsonObject);
                    jsonObjectMessage.put("msgType", "IMAGE");
                    jsonObjectMessage.put("encrypt", false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sendTextMessage(jsonObjectMessage);
                Log.e(TAG, "realm is inTransaction is " + realm.isInTransaction());
                if (realm.isInTransaction())
                    realm.commitTransaction();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Message message = realm.createObjectFromJson(Message.class, jsonObjectMessage);
                        messageList = realm.where(Message.class).equalTo("conversationId", conversationId).findAll().sort("sendTime", Sort.ASCENDING);
                        conversation.addMessage(message);
                    }
                });

                if (messageList.size() != 0) {
                    recyclerView.smoothScrollToPosition(messageList.size() - 1);
                }


//                Message message = new Message(Message.MSG_TYPE_FACE,
//                        Message.MSG_STATE_SUCCESS, "Tom", "avatar", "Jerry",
//                        "avatar", BQMMMessageHelper.getFaceMessageData(face), true, true, new Date());
//                datas.add(message);
//                adapter.refresh(datas);

                /**
                 * 1秒后增加一条和发出的这条相同的消息，模拟对话
                 */
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        Message getmessage = new Message(Message.MSG_TYPE_FACE,
//                                Message.MSG_STATE_SUCCESS, "Jerry", "avatar", "Tom",
//                                "avatar", BQMMMessageHelper.getFaceMessageData(face), false, true, new Date());
//                        datas.add(getmessage);
//                        adapter.refresh(datas);
//                    }
//                }, 1000);
            }

            /**
             * 图文混排消息
             */
            @Override
            public void onSendMixedMessage(List<Object> emojis, boolean isMixedMessage) {
                Log.e(TAG, "onSendMixedMessage");
                Log.e(TAG, "text is " + bqmmEditView.getText());
                final JSONObject jsonObjectMessage = new JSONObject();
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObjectMessage.put("payloadType", "MESSAGE");
                    jsonObjectMessage.put("conversationId", conversationId);
                    jsonObjectMessage.put("sendTime", System.currentTimeMillis());
                    jsonObjectMessage.put("senderId", "" + Constants.USER_ID);
                    jsonObjectMessage.put("senderUserName", "Hello");
                    jsonObjectMessage.put("toUserId", "" + conversation.adminUserId);
                    jsonObjectMessage.put("toUserName", "Hello");
                    jsonObject.put("text", bqmmEditView.getText());
                    jsonObjectMessage.put("body", jsonObject);
                    jsonObjectMessage.put("msgType", "TEXT");
                    jsonObjectMessage.put("encrypt", false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendTextMessage(jsonObjectMessage);
                if (realm.isInTransaction())
                    realm.commitTransaction();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Message message = realm.createObjectFromJson(Message.class, jsonObjectMessage);
                        messageList = realm.where(Message.class).equalTo("conversationId", conversationId).findAll().sort("sendTime", Sort.ASCENDING);
                        conversation.addMessage(message);
                    }
                });

                if (messageList.size() != 0) {
                    recyclerView.smoothScrollToPosition(messageList.size() - 1);
                }
//                JSONObject jsonObject = new JSONObject(message)
//                final JSONArray msgCodes = BQMMMessageHelper.getMixedMessageData(emojis);
//                Message message = new Message(Message.MSG_TYPE_MIXTURE,
//                        Message.MSG_STATE_SUCCESS, "Tom", "avatar", "Jerry",
//                        "avatar", msgCodes, true, true, new Date());
//                datas.add(message);
//                adapter.refresh(datas);
//
//                /**
//                 * 1秒后增加一条和发出的这条相同的消息，模拟对话
//                 */
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        Message getmessage = new Message(Message.MSG_TYPE_MIXTURE,
//                                Message.MSG_STATE_SUCCESS, "Jerry", "avatar", "Tom",
//                                "avatar", msgCodes, false, true, new Date());
//                        datas.add(getmessage);
//                        adapter.refresh(datas);
//                    }
//                }, 1000);


            }
        });
//        initListView();

//        /**
//         * 设置键盘的默认高度
//         */
//        int defaultHeight = DensityUtils.dip2px(this, 250);
//        int height = getPreferences(MODE_PRIVATE).getInt(LAST_KEYBOARD_HEIGHT, defaultHeight);
//        ViewGroup.LayoutParams params = bqmmKeyboard.getLayoutParams();
//        if (params != null) {
//            params.height = height;
//            bqmmKeyboard.setLayoutParams(params);
//        }
        /**
         * 表情键盘切换监听
         */
        bqmmEditView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // Keyboard -> BQMM
                if (mPendingShowPlaceHolder) {
                    // 在设置mPendingShowPlaceHolder时已经调用了隐藏Keyboard的方法，直到Keyboard隐藏前都取消重绘
                    if (isKeyboardVisible()) {
                        ViewGroup.LayoutParams params = bqmmKeyboard.getLayoutParams();
                        int distance = getDistanceFromInputToBottom();
                        // 调整PlaceHolder高度
                        if (distance > DISTANCE_SLOP && distance != params.height) {
                            params.height = distance;
                            bqmmKeyboard.setLayoutParams(params);
                            getPreferences(MODE_PRIVATE).edit().putInt(LAST_KEYBOARD_HEIGHT, distance).apply();
                        }
                        return false;
                    } else {
//                        mRealListView.setSelection(mRealListView.getAdapter().getCount() - 1);
                        showBqmmKeyboard();
                        mPendingShowPlaceHolder = false;
                        return false;
                    }
                } else {//BQMM -> Keyboard
                    if (isBqmmKeyboardVisible() && isKeyboardVisible()) {
//                        mRealListView.setSelection(mRealListView.getAdapter().getCount() - 1);
                        hideBqmmKeyboard();
                        return false;
                    }
                }
                return true;
            }
        });
        //切换开关
        bqmmKeyboardOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 除非软键盘和PlaceHolder全隐藏时直接显示PlaceHolder，其他情况此处处理软键盘，onPreDrawListener处理PlaceHolder
                if (isBqmmKeyboardVisible()) { // PlaceHolder -> Keyboard
                    Log.e(TAG, "1");
                    showSoftInput(bqmmEditView);
                } else if (isKeyboardVisible()) { // Keyboard -> PlaceHolder
                    Log.e(TAG, "2");
                    mPendingShowPlaceHolder = true;
                    hideSoftInput(bqmmEditView);
                } else { // Just show PlaceHolder
//                    mRealListView.setSelection(mRealListView.getAdapter().getCount() - 1);
                    Log.e(TAG, "3");
                    showBqmmKeyboard();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chats_back:
                this.finish();
                break;
            case R.id.normal_chats_back:
                this.finish();
                break;
            case R.id.chat_person:
                if (conversation.type==0||conversation.type==2) {
                    Intent intent = new Intent();
                    intent.setClass(this, ProfileActivity.class);
                    intent.putExtra("userId", toUserId);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        realm.close();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        bqmmsdk.destory();
    }

    @Override
    public boolean onLongTouch(View v, int position) {
        Log.e(TAG, "ON Long Touch");
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessageEvent(ReceiveMessageEvent event) {
        Log.e(TAG, "Receive message");
//        conversationsAdapter.setData(conversations);
//        conversationsAdapter.notifyDataSetChanged();
//        conversationList.invalidate();

    }

    /**************************
     * 表情键盘软键盘切换相关 start
     **************************************/
    private void closebroad() {
        if (isBqmmKeyboardVisible()) {
            hideBqmmKeyboard();
        } else if (isKeyboardVisible()) {
            hideSoftInput(bqmmEditView);
        }
    }

    private boolean isKeyboardVisible() {
        return (getDistanceFromInputToBottom() > DISTANCE_SLOP && !isBqmmKeyboardVisible())
                || (getDistanceFromInputToBottom() > (bqmmKeyboard.getHeight() + DISTANCE_SLOP) && isBqmmKeyboardVisible());
    }

    private void showSoftInput(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    private void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Activity在此方法中测量根布局的高度
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mScreenHeight <= 0) {
            mMainContainer.getGlobalVisibleRect(tmp);
            mScreenHeight = tmp.bottom;
        }
    }

    private void showBqmmKeyboard() {
        bqmmKeyboard.showKeyboard();
    }

    private void hideBqmmKeyboard() {
        bqmmKeyboard.hideKeyboard();
    }

    private boolean isBqmmKeyboardVisible() {
        return bqmmKeyboard.isKeyboardVisible();
    }

    /**
     * 输入框的下边距离屏幕的距离
     */
    private int getDistanceFromInputToBottom() {
        return mScreenHeight - getInputBottom();
    }

    /**
     * 输入框下边的位置
     */
    private int getInputBottom() {
        inputbox.getGlobalVisibleRect(tmp);
        return tmp.bottom;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && (isBqmmKeyboardVisible() || isKeyboardVisible())) {
            closebroad();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 若软键盘或表情键盘弹起，点击上端空白处应该隐藏输入法键盘
     */
    private View.OnTouchListener getOnTouchListener() {
        return new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 关闭键盘
                bqmmKeyboardOpen.setChecked(false);
                closebroad();
                return false;
            }
        };
    }

    private boolean sendTextMessage(JSONObject message) {
        Log.e(TAG, "SendTextMessage is " + message);
        try {
            HttpEntity httpEntity = new StringEntity(message.toString());
            return KacyberRestClientUsage.getInstance().sendMessage(this, httpEntity, "application/json");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

}
