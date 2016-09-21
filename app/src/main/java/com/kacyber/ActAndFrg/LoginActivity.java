package com.kacyber.ActAndFrg;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.kacyber.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = LoginActivity.class.getName();
    private AQuery aQuery;
    private TextView termText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        aQuery = new AQuery(this);
        aQuery.id(R.id.login_response).clickable(true).clicked(this);
        termText = (TextView) findViewById(R.id.terms_and_conditions);
        termText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        termText.getPaint().setAntiAlias(true);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_response:
                Log.e(TAG, "login response clicked");
                Intent intent = new Intent();
                intent.setClass(this, SingUpActivity.class);
                startActivity(intent);

                break;
            default:
                break;

        }
    }
}
