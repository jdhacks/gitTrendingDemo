package com.github.display.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.github.display.R;
import com.github.display.Utility.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        goToMainEntryScreen();
    }

    private void goToMainEntryScreen() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            /*Intent intent = new Intent(SplashActivity.this, MainEntryActivity.class);
            if (new PrefManager().getIsLogedin(this)) {
                intent.putExtra(Constants.SCREEN_ID, Constants.SCREEN_HOME);
            } else {
                intent.putExtra(Constants.SCREEN_ID, Constants.SCREEN_REGISTRATION);
            }
            startActivity(intent);
            SplashActivity.this.finish();*/
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("FROM_SPASLH", true);
            startActivity(intent);
            finish();
        }, Constants.SPLASH_SCREEN_TIME_OUT);
    }
}