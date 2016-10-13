package com.kacyber.ActAndFrg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.androidquery.AQuery;
import com.kacyber.R;

public class DiscoverCitiesActivity extends AppCompatActivity implements View.OnClickListener {
    private AQuery aQuery;
    private static boolean allOversea = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_discover_cities);
        aQuery = new AQuery(this);
        aQuery.id(R.id.all_switcher).clickable(true).clicked(this);
        aQuery.id(R.id.oversea_switcher).clickable(true).clicked(this);
        aQuery.id(R.id.back_text).clickable(true).clicked(this);
        aQuery.id(R.id.normal_cities_back).clickable(true).clicked(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_switcher:

                if (allOversea != true) {
                    allOversea = true;
                    aQuery.id(R.id.all_switcher).background(R.drawable.blue_left_2_corners).textColor(R.color.white);
                    aQuery.id(R.id.oversea_switcher).background(R.drawable.white_right_2_corners).textColor(R.color.colorPrimary);
                }
                break;
            case R.id.oversea_switcher:
                if (allOversea != false) {
                    allOversea = false;
                    aQuery.id(R.id.oversea_switcher).background(R.drawable.blue_right_2_corners).textColor(R.color.white);
                    aQuery.id(R.id.all_switcher).background(R.drawable.white_left_2_corners).textColor(R.color.colorPrimary);
                }

                break;
            case R.id.back_text:
                this.finish();
                break;

            case R.id.normal_cities_back:
                this.finish();
                break;
            default:
                break;

        }
    }
}
