package com.example.chimatcha;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_register);

        Button registerButton = findViewById(R.id.registerButton);
        TextView loginText = findViewById(R.id.loginText);
        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        EditText confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        ImageView passwordToggle = findViewById(R.id.passwordToggle);
        ImageView confirmPasswordToggle = findViewById(R.id.confirmPasswordToggle);

        // Set initial password transformations
        passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // Underline "Login" link
        SpannableString loginSpan = new SpannableString("Login");
        loginSpan.setSpan(new UnderlineSpan(), 0, loginSpan.length(), 0);
        loginText.setText(loginSpan);

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

        // Confirm password eye toggle
        confirmPasswordToggle.setOnClickListener(v -> {
            if (confirmPasswordInput.getTransformationMethod() instanceof PasswordTransformationMethod) {
                confirmPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                confirmPasswordToggle.setImageResource(R.drawable.ic_eye_off);
            } else {
                confirmPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                confirmPasswordToggle.setImageResource(R.drawable.ic_eye);
            }
            confirmPasswordInput.setSelection(confirmPasswordInput.getText().length());
        });

        // Button press color change
        registerButton.setOnTouchListener((v, event) -> {
            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(80f);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bg.setColor(Color.parseColor("#CC2A47"));
                registerButton.setBackground(bg);
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                bg.setColor(Color.parseColor("#FF3B5C"));
                registerButton.setBackground(bg);
            }
            return false;
        });

        // Register button — validate then proceed
        registerButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();
            boolean valid = true;

            usernameInput.setError(null);
            emailInput.setError(null);
            passwordInput.setError(null);
            confirmPasswordInput.setError(null);

            if (username.isEmpty()) {
                usernameInput.setError("Username must be filled");
                valid = false;
            } else if (username.length() <= 6) {
                usernameInput.setError("Username must be longer than 6 characters");
                valid = false;
            }

            if (email.isEmpty()) {
                emailInput.setError("Email must be filled");
                valid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Enter a valid email address");
                valid = false;
            }

            if (password.isEmpty()) {
                passwordInput.setError("Password must be filled");
                valid = false;
            }

            if (confirmPassword.isEmpty()) {
                confirmPasswordInput.setError("Confirm Password must be filled");
                valid = false;
            } else if (!password.isEmpty() && !password.equals(confirmPassword)) {
                confirmPasswordInput.setError("Password and Confirm Password must be the same");
                valid = false;
            }

            if (valid) {
                AppGlobals.loggedInUsername = username;
                Intent intent = new Intent(RegisterActivity.this, LaunchingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
