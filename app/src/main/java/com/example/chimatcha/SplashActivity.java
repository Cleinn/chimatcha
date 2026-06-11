package com.example.chimatcha;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private View dot1, dot2, dot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full screen - hide status bar
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_splash);

        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);

        startDotAnimation();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

    private void startDotAnimation() {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(dot1, "alpha", 1f, 0.2f);
        anim1.setDuration(600);
        anim1.setRepeatMode(ObjectAnimator.REVERSE);
        anim1.setRepeatCount(ObjectAnimator.INFINITE);
        anim1.setStartDelay(0);

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(dot2, "alpha", 1f, 0.2f);
        anim2.setDuration(600);
        anim2.setRepeatMode(ObjectAnimator.REVERSE);
        anim2.setRepeatCount(ObjectAnimator.INFINITE);
        anim2.setStartDelay(200);

        ObjectAnimator anim3 = ObjectAnimator.ofFloat(dot3, "alpha", 1f, 0.2f);
        anim3.setDuration(600);
        anim3.setRepeatMode(ObjectAnimator.REVERSE);
        anim3.setRepeatCount(ObjectAnimator.INFINITE);
        anim3.setStartDelay(400);

        anim1.start();
        anim2.start();
        anim3.start();
    }
}
