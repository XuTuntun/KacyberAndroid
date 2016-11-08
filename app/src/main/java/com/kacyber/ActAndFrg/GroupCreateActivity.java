package com.kacyber.ActAndFrg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.kacyber.R;

public class GroupCreateActivity extends Activity implements View.OnClickListener {

    private AQuery aQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        aQuery = new AQuery(this);
        aQuery.id(R.id.create_group_cancel).clickable(true).clicked(this);
        aQuery.id(R.id.group_create_done).clickable(true).clicked(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_group_cancel:
                this.finish();
                break;

            case R.id.group_create_done:
                this.finish();
                break;
        }
    }
}
