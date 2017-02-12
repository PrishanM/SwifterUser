package com.evensel.swyftr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.evensel.swyftr.authentication.LoginActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startHandler();
    }

    private void startHandler() {
        int SPLASH_TIMEOUT_TIME = 3000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //Start next activity
                Intent mainActivity = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mainActivity);
                finish();
            }
        }, SPLASH_TIMEOUT_TIME);
    }
}
