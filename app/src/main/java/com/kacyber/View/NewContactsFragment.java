package com.kacyber.View;

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
import com.kacyber.ActAndFrg.NewFriendsActivity;
import com.kacyber.ActAndFrg.NormalSearchActivity;
import com.kacyber.ActAndFrg.ProfileActivity;
import com.kacyber.R;
import com.kacyber.adapter.RealContactAdapter;
import com.kacyber.adapter.UserItemClickListener;
import com.kacyber.model.AddFriendRequest;
import com.kacyber.model.Contact;
import com.kacyber.network.http.KacyberRestClientUsage;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewContactsFragment extends Fragment implements View.OnClickListener, UserItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String TAG = NewContactsFragment.class.getName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AQuery aQuery;
    private RecyclerView recyclerView;
    private Realm realm;

    private RealContactAdapter adapter;
    private RealmResults<Contact> contacts;

    public NewContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewContactsFragment newInstance(String param1, String param2) {
        NewContactsFragment fragment = new NewContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_contacts, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.contacts_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        aQuery = new AQuery(view);
        aQuery.id(R.id.new_friends).clickable(true).clicked(this);
        aQuery.id(R.id.group_chat).clickable(true).clicked(this);
        aQuery.id(R.id.secret_chat).clickable(true).clicked(this);
        aQuery.id(R.id.people_nearby).clickable(true).clicked(this);
        aQuery.id(R.id.moments_layout).clickable(true).clicked(this);
        aQuery.id(R.id.contact_search).clickable(true).clicked(this);
        aQuery.id(R.id.contact_add).clickable(true).clicked(this);

        contacts = realm.where(Contact.class).findAll().sort("userName", Sort.ASCENDING);
        adapter = new RealContactAdapter(getContext(), R.layout.user_item_layout, contacts);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.setData(contacts);
        adapter.notifyDataSetChanged();
        recyclerView.invalidate();
        contacts.addChangeListener(new RealmChangeListener<RealmResults<Contact>>() {
            @Override
            public void onChange(RealmResults<Contact> element) {
                adapter.setData(contacts);
                adapter.notifyDataSetChanged();
                recyclerView.invalidate();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

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
            case R.id.new_friends:
                Intent intent = new Intent();
                intent.setClass(getActivity(), NewFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.group_chat:
                Intent intentGroup = new Intent();
                intentGroup.setClass(getActivity(), GroupCreateActivity.class);
                startActivity(intentGroup);
                break;
            case R.id.secret_chat:
                Intent intentSecret = new Intent();
                intentSecret.setClass(getActivity(), GroupCreateActivity.class);
                startActivity(intentSecret);
                break;
            case R.id.people_nearby:
                Intent intentNearbyPeople = new Intent();
                intentNearbyPeople.setClass(getActivity(), GroupCreateActivity.class);
                startActivity(intentNearbyPeople);
                break;
            case R.id.moments_layout:
                Intent intentMoment = new Intent();
                intentMoment.setClass(getActivity(), ProfileActivity.class);
                startActivity(intentMoment);
                break;

            case R.id.contact_search:
                Intent intentSearch = new Intent();
                intentSearch.setClass(getActivity(), NormalSearchActivity.class);
                intentSearch.putExtra("hint", "Search for moments, friends or brands");
                intentSearch.putExtra("searchAction", "all");
                startActivity(intentSearch);
                break;

            case R.id.contact_add:
                Intent addIntent = new Intent();
                addIntent.setClass(getActivity(), AddContact.class);
                startActivity(addIntent);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.e(TAG, "onItemClick");
        long userId = contacts.get(position).userId;
        Intent profileIntent = new Intent();
        profileIntent.setClass(getActivity(), ProfileActivity.class);
        profileIntent.putExtra("userId", userId);
        startActivity(profileIntent);
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
    public void onResume() {
        super.onResume();
        KacyberRestClientUsage.getInstance().getContactList();
        Realm realm = Realm.getDefaultInstance();
        if (realm.isInTransaction())
            realm.commitTransaction();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<AddFriendRequest> results = realm.where(AddFriendRequest.class).findAll();
                int size = results.size();
                Log.e(TAG, "AddFriendResult size is " + size);
            }
        });
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();

    }
}
