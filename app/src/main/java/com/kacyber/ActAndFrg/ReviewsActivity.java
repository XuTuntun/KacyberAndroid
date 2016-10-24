package com.kacyber.ActAndFrg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.kacyber.R;

public class ReviewsActivity extends Activity implements View.OnClickListener {

    private AQuery aQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        aQuery = new AQuery(this);
        aQuery.id(R.id.back_text).clickable(true).clicked(this);
        aQuery.id(R.id.normal_review_back).clickable(true).clicked(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_text:
                this.finish();
                break;
            case R.id.normal_review_back:
                this.finish();
                break;
        }
    }
}
