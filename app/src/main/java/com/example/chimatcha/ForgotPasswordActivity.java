package com.example.chimatcha;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_forgot_password);

        Button submitButton = findViewById(R.id.submitButton);
        EditText emailInput = findViewById(R.id.emailInput);

        // Button press color change
        submitButton.setOnTouchListener((v, event) -> {
            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(80f);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bg.setColor(Color.parseColor("#CC2A47"));
                submitButton.setBackground(bg);
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                bg.setColor(Color.parseColor("#FF3B5C"));
                submitButton.setBackground(bg);
            }
            return false;
        });

        submitButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            emailInput.setError(null);

            if (email.isEmpty()) {
                emailInput.setError("Email must be filled");
                return;
            }

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
