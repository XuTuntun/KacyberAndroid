package com.kacyber.ActAndFrg;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

//import com.kacyber.Emoji.Message;
//import com.kacyber.Emoji.UnicodeToEmoji;
import com.kacyber.R;


import org.json.JSONArray;

import java.util.Date;
import java.util.List;

public class NormalChatActivity extends FragmentActivity {
    private static String TAG = NormalChatActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_chat);

        initView();
    }

    private void initView() {

//        bqmmSDK = BQMM.getInstance();
//        Log.e(TAG, "bqmmSDK is " + bqmmSDK);
//        bqmmSDK.setEditView(editView);
//        bqmmSDK.setKeyboard(keyBoard);
//        bqmmSDK.setSendButton(sendButton);
//
//        UnicodeToEmoji.initPhotos(this);
//
//        bqmmSDK.setUnicodeEmojiProvider(new IBQMMUnicodeEmojiProvider() {
//            @Override
//            public Drawable getDrawableFromCodePoint(int i) {
//                return UnicodeToEmoji.EmojiImageSpan.getEmojiDrawable(i);
//            }
//        });
//        bqmmSDK.load();
//        bqmmSDK.setLanguage(BQMM.LANGUAGE_CONSTANTS.EN);
//        bqmmSDK.setRegion(BQMM.REGION_CONSTANTS.OTHERS);


        /**
         * 默认方式打开软键盘时切换表情符号的状态
		 */
//        editView.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                bqmmKeyboardOpen.setChecked(false);
//                return false;
//            }
//        });

        /**
         * BQMM集成
         * 实现输入联想
         */
//        editView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                BQMM.getInstance().startShortcutPopupWindowByoffset(NormalChatActivity.this, s.toString(), sendButton,0, DensityUtils.dip2px(NormalChatActivity.this,4));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
        /**
         * BQMM集成
         * 设置发送消息的回调
         */
//        bqmmSDK.setBqmmSendMsgListener(new IBqmmSendMessageListener() {
//            /**
//             * 单个大表情消息
//             */
//            @Override
//            public void onSendFace(final Emoji face) {
//                Message message = new Message(Message.MSG_TYPE_FACE,
//                        Message.MSG_STATE_SUCCESS, "Tom", "avatar", "Jerry",
//                        "avatar", BQMMMessageHelper.getFaceMessageData(face), true, true, new Date());
////                datas.add(message);
////                adapter.refresh(datas);
//
//                /**
//                 * 1秒后增加一条和发出的这条相同的消息，模拟对话
//                 */
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        Message getmessage = new Message(Message.MSG_TYPE_FACE,
//                                Message.MSG_STATE_SUCCESS, "Jerry", "avatar", "Tom",
//                                "avatar", BQMMMessageHelper.getFaceMessageData(face), false, true, new Date());
////                        datas.add(getmessage);
////                        adapter.refresh(datas);
//                    }
//                }, 1000);
//            }
//
//            /**
//             * 图文混排消息
//             */
//            @Override
//            public void onSendMixedMessage(List<Object> emojis, boolean isMixedMessage) {
//                final JSONArray msgCodes = BQMMMessageHelper.getMixedMessageData(emojis);
//                Message message = new Message(Message.MSG_TYPE_MIXTURE,
//                        Message.MSG_STATE_SUCCESS, "Tom", "avatar", "Jerry",
//                        "avatar", msgCodes, true, true, new Date());
////                datas.add(message);
////                adapter.refresh(datas);
//
//                /**
//                 * 1秒后增加一条和发出的这条相同的消息，模拟对话
//                 */
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        Message getmessage = new Message(Message.MSG_TYPE_MIXTURE,
//                                Message.MSG_STATE_SUCCESS, "Jerry", "avatar", "Tom",
//                                "avatar", msgCodes, false, true, new Date());
////                        datas.add(getmessage);
////                        adapter.refresh(datas);
//                    }
//                }, 1000);
//
//
//            }
//        });
    }
}
