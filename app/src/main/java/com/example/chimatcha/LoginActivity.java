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
import android.text.style.ImageSpan;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.text.style.ScaleXSpan;
import android.text.style.ForegroundColorSpan;
import android.text.Spannable;

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

        passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

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

        SpannableString signUpSpan = new SpannableString(" Sign Up");
        signUpSpan.setSpan(new UnderlineSpan(), 0, signUpSpan.length(), 0);
        signUpText.setText(signUpSpan);

        loginButton.setOnTouchListener((v, event) -> {
            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(80f);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bg.setColor(Color.parseColor("#D84962"));
                loginButton.setBackground(bg);
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                bg.setColor(Color.parseColor("#F83758"));
                loginButton.setBackground(bg);
            }
            return false;
        });

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
                AppGlobals.loggedInUsername = username;

                SharedPreferences prefs = getSharedPreferences("chimatcha_prefs", MODE_PRIVATE);
                boolean isFirstTime = prefs.getBoolean("is_first_time", true);

                if (isFirstTime) {
                    prefs.edit().putBoolean("is_first_time", false).apply();
                    Intent intent = new Intent(LoginActivity.this, LaunchingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
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
