package com.example.chimatcha;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LaunchingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_launching);

        Button getStartedButton = findViewById(R.id.getStartedButton);

        // Button press color change
        getStartedButton.setOnTouchListener((v, event) -> {
            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(80f);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bg.setColor(Color.parseColor("#CC2A47")); // darker on press
                getStartedButton.setBackground(bg);
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                bg.setColor(Color.parseColor("#FF3B5C")); // original
                getStartedButton.setBackground(bg);
            }
            return false;
        });

        // Navigate to Home
        getStartedButton.setOnClickListener(v -> {
            Intent intent = new Intent(LaunchingActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
