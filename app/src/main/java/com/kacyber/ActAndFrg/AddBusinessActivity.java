package com.kacyber.ActAndFrg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.Utils.Constants;

public class AddBusinessActivity extends Activity implements View.OnClickListener {

    private AQuery aQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);
        aQuery = new AQuery(this);
        aQuery.id(R.id.register_online).clickable(true).clicked(this);
        aQuery.id(R.id.call_register).clickable(true).clicked(this);
        aQuery.id(R.id.normal_chats_back).clickable(true).clicked(this);
        aQuery.id(R.id.back_text).clickable(true).clicked(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_online:
                Intent addBusinessIntent = new Intent();
                addBusinessIntent.putExtra("title", "Add Business");
                addBusinessIntent.putExtra("url", Constants.ADD_BUSINESS);
                addBusinessIntent.setClass(this, WebViewActivity.class);
                startActivity(addBusinessIntent);
                break;
            case R.id.call_register:
                Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+256 784 007 939"));
                intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentPhone);
                break;
            case R.id.normal_chats_back:
                this.finish();
                break;
            case R.id.back_text:
                this.finish();
                break;
        }
    }
}
