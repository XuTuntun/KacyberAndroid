package com.kacyber.ActAndFrg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.adapter.ContactAdapter;
import com.kacyber.adapter.RequestAdapter;
import com.kacyber.adapter.RequestItemClickListener;
import com.kacyber.model.AddFriendRequest;
import com.kacyber.model.User;
import com.kacyber.module.MyCodeActivity;
import com.kacyber.zxing.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.IOException;

public class NewFriendsActivity extends Activity implements View.OnClickListener {

    private AQuery aQuery;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends);


        aQuery = new AQuery(this);
        recyclerView = (RecyclerView) findViewById(R.id.friend_request_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        aQuery.id(R.id.add_contact_back).clickable(true).clicked(this);
        aQuery.id(R.id.add_contact_back_text).clickable(true).clicked(this);

        aQuery.id(R.id.add_contact_search).clickable(true).clicked(this);
        aQuery.id(R.id.add_mobile_contact).clickable(true).clicked(this);
        aQuery.id(R.id.add_qr_code_contact).clickable(true).clicked(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<AddFriendRequest> requests = loadRequest();
        RequestAdapter adapter = new RequestAdapter(this, R.layout.user_item_layout, requests);
        recyclerView.setAdapter(adapter);
    }

    public List<AddFriendRequest> loadRequest() throws IOException {
        Realm realm = Realm.getDefaultInstance();

        return realm.where(AddFriendRequest.class).findAll();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_contact_back:
                this.finish();
                break;
            case R.id.add_contact_back_text:
                this.finish();
                break;
            case R.id.add_mobile_contact:
                Intent mobileContactIntent = new Intent();
                mobileContactIntent.setClass(this, MobileContactActivity.class);
                startActivity(mobileContactIntent);
                break;
            case R.id.add_qr_code_contact:
                Intent qrCodeActivity = new Intent();
                qrCodeActivity.setClass(this, CaptureActivity.class);
                startActivity(qrCodeActivity);
                break;

        }
    }
}
