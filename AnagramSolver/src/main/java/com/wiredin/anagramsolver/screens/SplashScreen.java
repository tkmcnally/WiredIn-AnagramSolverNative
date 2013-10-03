package com.wiredin.anagramsolver.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wiredin.anagramsolver.screens.MainScreen;
import com.wiredin.anagramsolver.R;

/**
 * Created by Thomas on 9/13/13.
 */
public class SplashScreen extends Activity {

    private static int SPLASH_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main_activity = new Intent(SplashScreen.this, MainScreen.class);
                startActivity(main_activity);

                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
