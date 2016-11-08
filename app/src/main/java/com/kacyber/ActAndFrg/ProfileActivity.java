package com.kacyber.ActAndFrg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.Utils.Constants;
import com.kacyber.View.CircleNetworkImageView;
import com.kacyber.model.UserProfile;
import com.kacyber.network.http.KacyberRestClient;
import com.kacyber.network.http.KacyberRestClientUsage;
import com.kacyber.network.service.ImageSingleton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Realm realm;
    private static long userId;
    private UserProfile userProfile = new UserProfile();
    private AQuery aQuery;
    private CircleNetworkImageView headImage;
    private ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        realm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        userId = intent.getLongExtra("userId", -1);
        aQuery = new AQuery(this);

        aQuery.id(R.id.send_greeting).clickable(true).clicked(this);
        aQuery.id(R.id.normal_chats_back).clickable(true).clicked(this);
        aQuery.id(R.id.profile_back_text).clickable(true).clicked(this);


        if (userId==-1) {
            return;
        }

        KacyberRestClientUsage.getInstance().getUserById(userId);
        userProfile = realm.where(UserProfile.class).equalTo("id", userId).findFirst();
        headImage = (CircleNetworkImageView) findViewById(R.id.profile_portrait);
        imageLoader = ImageSingleton.getInstance(this).getImageLoader();
        userProfile.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                headImage.setImageUrl(userProfile.avatar, imageLoader);
                aQuery.id(R.id.profile_nick).text(userProfile.nickname);
                if (userProfile.gender!=null) {
                    if (userProfile.gender.equals("FEMALE")) {
                        aQuery.id(R.id.profile_gender).background(R.drawable.contactspage_females_icon);
                    } else {
                        aQuery.id(R.id.profile_gender).background(R.drawable.contactspage_males_icon);
                    }
                }
                aQuery.id(R.id.profile_region).text(userProfile.region);
                aQuery.id(R.id.profile_from).text(userProfile.region);



            }
        });



    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_greeting:
                aQuery.id(R.id.send_greeting).text("Sended");
                aQuery.id(R.id.sender_iamge).clickable(false);
                break;
            case R.id.normal_back_text:
                this.finish();
                break;
            case R.id.profile_back_text:
                this.finish();
                break;
        }
    }
}
