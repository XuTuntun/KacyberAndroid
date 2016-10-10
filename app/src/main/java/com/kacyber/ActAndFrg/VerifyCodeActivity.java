package com.kacyber.ActAndFrg;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.kacyber.R;
import com.kacyber.Service.MqttService;
import com.kacyber.View.MainActivity;

public class VerifyCodeActivity extends AppCompatActivity implements View.OnClickListener{

    private CountDownTimer countDownTimer;
    private AQuery aQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        aQuery = new AQuery(this);

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                aQuery.id(R.id.count_down_timer).text("Resend Code in 00:" + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                aQuery.id(R.id.count_down_timer).text("Have resend U a code");
            }
        }.start();

        aQuery.id(R.id.verify_cancel).clickable(true).clicked(this);
        aQuery.id(R.id.verify_submit).clickable(true).clicked(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_cancel:
                this.finish();
                break;
            case R.id.verify_submit:
                Intent mainIntent = new Intent();
                mainIntent.setClass(this, MainActivity.class);
                startActivity(mainIntent);
                MqttService.actionStart(this);
                finish();
                break;
        }
    }
}
