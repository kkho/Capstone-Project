package com.awesome.kkho.socialme.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.awesome.kkho.socialme.R;
import com.awesome.kkho.socialme.util.SharedPrefUtil;

/**
 * Created by kkho on 31.01.2016.
 */
public class SplashScreenActivity extends Activity {
    TextView mSplashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Get login information stored
        //Then simulate startscreen

        new Thread(new Runnable() {
            @Override
            public void run() {
                //get login information
            }
        }).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                if (SharedPrefUtil.getUserProfile(getApplicationContext()) != null) {
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1200);
    }
}
