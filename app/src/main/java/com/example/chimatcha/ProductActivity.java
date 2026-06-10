package com.example.chimatcha;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private ProductAdapter adapter;
    private TextView noResultsText;
    private ImageButton hamburgerButton;
    private LinearLayout dropdownMenu;
    private boolean isDropdownOpen = false;

    private void setupDropdown() {
        hamburgerButton = findViewById(R.id.hamburgerButton);
        dropdownMenu = findViewById(R.id.dropdownMenu);
        LinearLayout menuHome = findViewById(R.id.menuHome);
        LinearLayout menuItems = findViewById(R.id.menuItems);
        LinearLayout menuBranch = findViewById(R.id.menuBranch);
        LinearLayout menuLogout = findViewById(R.id.menuLogout);

        // Toggle dropdown on hamburger click
        hamburgerButton.setOnClickListener(v -> {
            if (isDropdownOpen) {
                closeDropdown();
            } else {
                openDropdown();
            }
        });

        // Home
        menuHome.setOnClickListener(v -> {
            closeDropdown();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // Items
        menuItems.setOnClickListener(v -> {
            closeDropdown();
            Intent intent = new Intent(this, ProductActivity.class);
            startActivity(intent);
        });

        // Branch
        menuBranch.setOnClickListener(v -> {
            closeDropdown();
            Intent intent = new Intent(this, BranchActivity.class);
            startActivity(intent);
        });

        // Log Out
        menuLogout.setOnClickListener(v -> {
            closeDropdown();
            AppGlobals.loggedInUsername = "";
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Close dropdown when touching outside of it
        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN && isDropdownOpen) {
                int[] menuLocation = new int[2];
                int[] btnLocation  = new int[2];
                dropdownMenu.getLocationOnScreen(menuLocation);
                hamburgerButton.getLocationOnScreen(btnLocation);

                float x = event.getRawX();
                float y = event.getRawY();

                boolean insideMenu = x >= menuLocation[0]
                        && x <= menuLocation[0] + dropdownMenu.getWidth()
                        && y >= menuLocation[1]
                        && y <= menuLocation[1] + dropdownMenu.getHeight();

                boolean insideBtn = x >= btnLocation[0]
                        && x <= btnLocation[0] + hamburgerButton.getWidth()
                        && y >= btnLocation[1]
                        && y <= btnLocation[1] + hamburgerButton.getHeight();

                if (!insideMenu && !insideBtn) {
                    closeDropdown();
                }
            }
            return false;
        });
    }

    private void openDropdown() {
        hamburgerButton.setImageResource(R.drawable.ic_close);
        dropdownMenu.setVisibility(View.VISIBLE);
        dropdownMenu.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int targetHeight = dropdownMenu.getMeasuredHeight();

        dropdownMenu.getLayoutParams().height = 0;
        dropdownMenu.setAlpha(0f);
        dropdownMenu.requestLayout();

        ValueAnimator animator = ValueAnimator.ofInt(0, targetHeight);
        animator.setDuration(250);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            dropdownMenu.getLayoutParams().height = (int) animation.getAnimatedValue();
            dropdownMenu.requestLayout();
            dropdownMenu.setAlpha(animation.getAnimatedFraction());
        });
        animator.start();
        isDropdownOpen = true;
    }

    private void closeDropdown() {
        hamburgerButton.setImageResource(R.drawable.ic_hamburger);
        int initialHeight = dropdownMenu.getMeasuredHeight();

        ValueAnimator animator = ValueAnimator.ofInt(initialHeight, 0);
        animator.setDuration(200);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            dropdownMenu.getLayoutParams().height = (int) animation.getAnimatedValue();
            dropdownMenu.requestLayout();
            dropdownMenu.setAlpha(1f - animation.getAnimatedFraction());
            if ((int) animation.getAnimatedValue() == 0) {
                dropdownMenu.setVisibility(View.GONE);
            }
        });
        animator.start();
        isDropdownOpen = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_product);

        setupDropdown();

        noResultsText = findViewById(R.id.noResultsText);

        setupRecycler();
        setupSearch();
    }

    private void setupRecycler() {
        RecyclerView recyclerView = findViewById(R.id.productRecycler);
        // 2-column grid
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<Product> products = new ArrayList<>();
        products.add(new Product("StrawMatcha", "A combination between milk matcha and strawberry fruit.", "Rp.25.000", R.drawable.product_strawmatcha, 23456));
        products.add(new Product("Matcha n Boba", "Standard matcha milk with toppings of boba.", "Rp.20.000", R.drawable.product_matchaboba, 45678));
        products.add(new Product("Mango Matcha", "A fresh taste of mango squash with pure matcha.",  "Rp.23.000", R.drawable.product_mangomatcha, 32930));
        products.add(new Product("Base Milk Matcha", "Classical matcha with elegant iced milk.", "Rp.15.000", R.drawable.product_matchalatte, 52344));
        products.add(new Product("Pure Matcha", "A pure fresh brewed matcha", "Rp.22.000", R.drawable.product_purematcha, 18920));
        products.add(new Product("Yuzu Matcha", "Zesty yuzu citrus blended with premium matcha.", "Rp.27.000", R.drawable.product_yuzumatcha, 11203));

        adapter = new ProductAdapter(this, products);
        recyclerView.setAdapter(adapter);
    }

    private void setupSearch() {
        EditText searchInput = findViewById(R.id.searchInput);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
                noResultsText.setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }
}
