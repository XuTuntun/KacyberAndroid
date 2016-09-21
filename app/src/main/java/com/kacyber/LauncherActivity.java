package com.kacyber;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.kacyber.ActAndFrg.LoginActivity;
import com.kacyber.View.MainActivity;
import com.kacyber.tools.SystemBarTintManager;

import java.util.Random;

public class LauncherActivity extends AppCompatActivity {

    private Animation mFadeIn;
    private Animation mFadeInScale;
    private Animation mFadeOut;

    private ImageView mImageView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        Window window = this.getWindow();
        //系统通知栏透明
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(Color.TRANSPARENT);
//        tintManager.setStatusBarAlpha((float)0);
        context = this;
        mImageView = (ImageView) findViewById(R.id.image);

//        startService(new Intent(this, CoreService.class));
//        startService(new Intent(this, CleanerService.class));


//        if (!SharedPreferencesUtils.isShortCut(mContext)) {
//            createShortCut();
//        }

        initAnim();
        setListener();
    }


    private void initAnim() {
        mFadeIn = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in);
        mFadeIn.setDuration(500);
        mFadeInScale = AnimationUtils.loadAnimation(this,
                R.anim.welcome_fade_in_scale);
        mFadeInScale.setDuration(2000);
        mFadeOut = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_out);
        mFadeOut.setDuration(500);
        mImageView.startAnimation(mFadeIn);
    }


    /**
     * 监听事件
     */
    public void setListener() {
        /**
         * 动画切换原理:开始时是用第一个渐现动画,当第一个动画结束时开始第二个放大动画,当第二个动画结束时调用第三个渐隐动画,
         * 第三个动画结束时修改显示的内容并且重新调用第一个动画,从而达到循环效果
         */
        mFadeIn.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                mImageView.startAnimation(mFadeInScale);
            }
        });
        mFadeInScale.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {

                /**
                 * 跳转登录界面
                 */
                boolean temple = true;
                if (temple) {
                    Intent loginIntent = new Intent();
                    loginIntent.setClass(context, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
                /**
                 * 跳转MainActivity
                 */
                else {
                    Intent mainIntent = new Intent();
                    mainIntent.setClass(context, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        });
        mFadeOut.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                // startActivity(MainActivity.class);
            }
        });
    }
}