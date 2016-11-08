package com.kacyber.ActAndFrg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.adapter.ContactAdapter;
import com.kacyber.adapter.RealContactAdapter;
import com.kacyber.adapter.UserItemClickListener;
import com.kacyber.model.Contact;
import com.kacyber.model.User;
import com.kacyber.network.http.KacyberRestClientUsage;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MobileContactActivity extends Activity implements View.OnClickListener, UserItemClickListener {
    private static String TAG = MobileContactActivity.class.getName();
    private RecyclerView recyclerView;
    private RealContactAdapter adapter;
    private RealmResults<Contact> contacts;
    private AQuery aQuery;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_contact);
        realm = Realm.getDefaultInstance();
        recyclerView = (RecyclerView) findViewById(R.id.mobile_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        aQuery = new AQuery(this);
        aQuery.id(R.id.mobile_contact_back).clickable(true).clicked(this);
        aQuery.id(R.id.mobile_contact_back_text).clickable(true).clicked(this);
        aQuery.id(R.id.mobile_contact_search).clickable(true).clicked(this);
        contacts = realm.where(Contact.class).findAll();
        adapter = new RealContactAdapter(this, R.layout.user_item_layout, contacts);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        contacts.addChangeListener(new RealmChangeListener<RealmResults<Contact>>() {
            @Override
            public void onChange(RealmResults<Contact> element) {
                adapter.setData(contacts);
                adapter.notifyDataSetChanged();
                recyclerView.invalidate();
            }
        });
        KacyberRestClientUsage.getInstance().getContactList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mobile_contact_back:
                this.finish();
                break;
            case R.id.mobile_contact_back_text:
                this.finish();
                break;
            case R.id.mobile_contact_search:
                Intent intent = new Intent();
                intent.setClass(this, NormalSearchActivity.class);
                intent.putExtra("hint", "Search for User ID or Phone Number");
                intent.putExtra("searchAction", "friends");
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.e(TAG, "onItemClick");
        long userId = contacts.get(position).userId;
        Intent intent = new Intent();
        intent.setClass(this, ProfileActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
}
