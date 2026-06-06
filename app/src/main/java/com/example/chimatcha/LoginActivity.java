package com.example.chimatcha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);
        TextView forgotPasswordText = findViewById(R.id.forgotPasswordText);
        TextView signUpText = findViewById(R.id.signUpText);
        EditText usernameInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        ImageView passwordToggle = findViewById(R.id.passwordToggle);

        // Set initial password transformation
        passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // Password eye toggle
        passwordToggle.setOnClickListener(v -> {
            if (passwordInput.getTransformationMethod() instanceof PasswordTransformationMethod) {
                passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                passwordToggle.setImageResource(R.drawable.ic_eye_off);
            } else {
                passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                passwordToggle.setImageResource(R.drawable.ic_eye);
            }
            passwordInput.setSelection(passwordInput.getText().length());
        });

        // Underline "Sign Up"
        SpannableString signUpSpan = new SpannableString(" Sign Up");
        signUpSpan.setSpan(new UnderlineSpan(), 0, signUpSpan.length(), 0);
        signUpText.setText(signUpSpan);

        // Button press color change
        loginButton.setOnTouchListener((v, event) -> {
            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(80f);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bg.setColor(Color.parseColor("#CC2A47"));
                loginButton.setBackground(bg);
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                bg.setColor(Color.parseColor("#F83758"));
                loginButton.setBackground(bg);
            }
            return false;
        });

        // Login button — validate then proceed
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            boolean valid = true;

            usernameInput.setError(null);
            passwordInput.setError(null);

            if (username.isEmpty()) {
                usernameInput.setError("Username must be filled");
                valid = false;
            } else if (username.length() <= 6) {
                usernameInput.setError("Username must be longer than 6 characters");
                valid = false;
            }

            if (password.isEmpty()) {
                passwordInput.setError("Password must be filled");
                valid = false;
            }

            if (valid) {
                // Store username globally
                AppGlobals.loggedInUsername = username;

                // Check if first time login using SharedPreferences
                SharedPreferences prefs = getSharedPreferences("chimatcha_prefs", MODE_PRIVATE);
                boolean isFirstTime = prefs.getBoolean("is_first_time", true);

                if (isFirstTime) {
                    // Mark as no longer first time
                    prefs.edit().putBoolean("is_first_time", false).apply();
                    // Go to Launching page
                    Intent intent = new Intent(LoginActivity.this, LaunchingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    // Go directly to Home
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                finish();
            }
        });

        forgotPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
