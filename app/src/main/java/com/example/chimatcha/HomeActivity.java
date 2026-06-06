package com.example.chimatcha;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // TODO: Replace with your actual home layout
        TextView tv = new TextView(this);
        tv.setText("Welcome, " + AppGlobals.loggedInUsername + "!");
        tv.setTextSize(24f);
        tv.setTextColor(0xFFFFFFFF);
        tv.setGravity(android.view.Gravity.CENTER);
        tv.setBackgroundColor(0xFF1A3328);
        setContentView(tv);
    }
}
