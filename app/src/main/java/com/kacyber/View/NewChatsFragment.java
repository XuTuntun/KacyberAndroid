package com.kacyber.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.kacyber.ActAndFrg.NormalChatActivity;
import com.kacyber.Control.MFGT;
import com.kacyber.R;
import com.kacyber.dialog.ActionItem;
import com.kacyber.dialog.TitlePopup;
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
public class NewChatsFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String TAG = NewChatsFragment.class.getName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static Activity context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private TitlePopup titlePopup;

    public NewChatsFragment() {
        // Required empty public constructor
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_chats, container, false);

        initPopWindow();
        AQuery aQuery = new AQuery(view);
        aQuery.id(R.id.chat_item_test).clickable(true).clicked(this);
        aQuery.id(R.id.chats_fragment_add).clickable(true).clicked(this);
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
                    break;
                case 1:// 添加朋友
//                    MFGT.gotoCommon(MainActivity.this, getString(R.string.menu_addfriend));
//                    MFGT.startActivity(MainActivity.this, NearByActivity.class);
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
            case R.id.chat_item_test:
                Intent intent = new Intent();
                intent.setClass(this.getActivity(), NormalChatActivity.class);
                this.getActivity().startActivity(intent);
                break;
            case R.id.chats_fragment_add:
                Log.e(TAG, "right corner clicked titlePopup is " + titlePopup);
                titlePopup.show(context.findViewById(R.id.chats_header));
                break;
        }
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
}
