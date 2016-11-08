package com.kacyber.ActAndFrg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.module.MyCodeActivity;
import com.kacyber.zxing.CaptureActivity;

public class AddContact extends Activity implements View.OnClickListener {

    private AQuery aQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        aQuery = new AQuery(this);
        aQuery.id(R.id.add_contact_back).clickable(true).clicked(this);
        aQuery.id(R.id.add_contact_back_text).clickable(true).clicked(this);

        aQuery.id(R.id.add_contact_search).clickable(true).clicked(this);
        aQuery.id(R.id.add_mobile_contact).clickable(true).clicked(this);
        aQuery.id(R.id.add_qr_code_contact).clickable(true).clicked(this);


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
            case R.id.add_contact_search:
                Intent intent = new Intent();
                intent.setClass(this, NormalSearchActivity.class);
                intent.putExtra("hint", "Search for User ID or Phone Number");
                intent.putExtra("searchAction", "friends");
                startActivity(intent);
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
