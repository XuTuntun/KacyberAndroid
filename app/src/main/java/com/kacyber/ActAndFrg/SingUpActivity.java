package com.kacyber.ActAndFrg;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.tools.SystemBarTintManager;

public class SingUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView termText;
    private AQuery aQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        aQuery = new AQuery(this);

        termText = (TextView) findViewById(R.id.terms_and_conditions);
        termText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        termText.getPaint().setAntiAlias(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);


        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.white);
//        tintManager.setStatusBarAlpha((float)255);

        aQuery.id(R.id.sign_up_cancel).clickable(true).clicked(this);
        aQuery.id(R.id.sign_up_done).clickable(true).clicked(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_cancel:
                this.finish();
                break;
            case R.id.sign_up_done:
                Intent intent = new Intent();
                intent.setClass(this, VerifyCodeActivity.class);
                startActivity(intent);
                break;

        }
    }
}
