package com.kacyber.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.kacyber.ActAndFrg.AddContact;
import com.kacyber.ActAndFrg.GroupCreateActivity;
import com.kacyber.ActAndFrg.NormalChatActivity;
import com.kacyber.ActAndFrg.NormalSearchActivity;
import com.kacyber.Control.MFGT;
import com.kacyber.R;
import com.kacyber.adapter.ConversationItemClickListener;
import com.kacyber.adapter.ConversationsAdapter;
import com.kacyber.dialog.ActionItem;
import com.kacyber.dialog.TitlePopup;
import com.kacyber.event.ConversationListEvent;
import com.kacyber.event.ReceiveMessageEvent;
import com.kacyber.model.Conversation;
import com.kacyber.network.http.KacyberRestClientUsage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
//import com.melink.bqmmsdk.ui.store.EmojiPackageDetail;
//import com.melink.bqmmsdk.ui.store.EmojiPackageList;
//import com.melink.bqmmsdk.ui.store.EmojiPackageSetting;
//import com.melink.bqmmsdk.ui.store.EmojiPackageSort;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewChatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewChatsFragment extends Fragment implements View.OnClickListener, ConversationItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String TAG = NewChatsFragment.class.getName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static RealmResults<Conversation> conversations;


    private RecyclerView conversationList;
    private ConversationsAdapter conversationsAdapter;

    private static Activity context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Realm realm;

    private TitlePopup titlePopup;

    public NewChatsFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewChatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewChatsFragment newInstance(String param1, String param2) {
        NewChatsFragment fragment = new NewChatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context= this.getActivity();
        EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        conversations = realm.where(Conversation.class).findAllSorted("lastSendTime", Sort.DESCENDING);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_chats, container, false);
        initPopWindow();
        AQuery aQuery = new AQuery(view);
        aQuery.id(R.id.chats_fragment_add).clickable(true).clicked(this);
        aQuery.id(R.id.chat_search).clickable(true).clicked(this);
        conversationList = (RecyclerView) view.findViewById(R.id.conversation_list);
        conversationList.setLayoutManager(new LinearLayoutManager(getActivity()));
        conversations = realm.where(Conversation.class).findAllSorted("lastSendTime", Sort.DESCENDING);
        conversations.sort("lastSendTime");
        conversationsAdapter = new ConversationsAdapter(this.getActivity(), R.layout.chat_item_layout, conversations);
        conversationsAdapter.setOnItemClickListener(this);
        Log.e(TAG, "onConversationsList size is " + conversations.size());
        conversationList.setAdapter(conversationsAdapter);
        conversations.addChangeListener(new RealmChangeListener<RealmResults<Conversation>>() {
            @Override
            public void onChange(RealmResults<Conversation> element) {
                conversationsAdapter.setData(conversations);
                conversationsAdapter.notifyDataSetChanged();
                conversationList.invalidate();
            }
        });
        KacyberRestClientUsage.getInstance().getConversationList();
        return view;
    }

    private void initPopWindow() {
        titlePopup = new TitlePopup(this.getActivity(), ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.setItemOnClickListener(onitemClick);
        // 给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(this.getActivity(), R.string.menu_groupchat,
                R.drawable.chatspage_group_icon));
        titlePopup.addAction(new ActionItem(this.getActivity(), R.string.menu_addfriend,
                R.drawable.chatspage_add_icon));
        titlePopup.addAction(new ActionItem(this.getActivity(), R.string.menu_qrcode,
                R.drawable.chatspage_scan_icon));
    }

    private TitlePopup.OnItemOnClickListener onitemClick = new TitlePopup.OnItemOnClickListener() {

        @Override
        public void onItemClick(ActionItem item, int position) {
            switch (position) {
                case 0:// 发起群聊
//                    MFGT.gotoCommon(, getString(R.string.menu_groupchat));
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), GroupCreateActivity.class);
                    startActivity(intent);
                    break;
                case 1:// 添加朋友
//                    MFGT.gotoCommon(MainActivity.this, getString(R.string.menu_addfriend));
//                    MFGT.startActivity(MainActivity.this, NearByActivity.class);
                    Intent addContactIntent = new Intent();
                    addContactIntent.setClass(getActivity(), AddContact.class);
                    startActivity(addContactIntent);
                    //TODO
                    break;
                case 2:// 扫一扫
                    //TODO
                    MFGT.gotoZXCode(context);
                    break;
                default:
                    break;
            }
        }
    };


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chats_fragment_add:
                Log.e(TAG, "right corner clicked titlePopup is " + titlePopup);
                titlePopup.show(context.findViewById(R.id.chats_header));
                break;
            case R.id.chat_search:
                Intent intentSearch = new Intent();
                intentSearch.setClass(getActivity(), NormalSearchActivity.class);
                intentSearch.putExtra("hint", "Search");
                intentSearch.putExtra("searchAction", "all");
                startActivity(intentSearch);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.e(TAG, "onItemClick");
        Intent intent = new Intent();
        long conversationId = conversations.get(position).id;
        intent.putExtra("conversationId", conversationId);
        intent.setClass(this.getActivity(), NormalChatActivity.class);
        this.getActivity().startActivity(intent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        realm.close();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConversationsListEvent(ConversationListEvent event) {
        Log.e(TAG, "onConversationsListEvent");
        conversations = realm.where(Conversation.class).findAllSorted("lastSendTime", Sort.DESCENDING);
//        conversationsAdapter.setData(conversations);
//        conversationsAdapter.notifyDataSetChanged();
//        conversationList.invalidate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessageEvent(ReceiveMessageEvent event) {
        conversations = realm.where(Conversation.class).findAllSorted("lastSendTime", Sort.DESCENDING);
//        conversationsAdapter.setData(conversations);
//        conversationsAdapter.notifyDataSetChanged();
//        conversationList.invalidate();

    }
}
