package com.example.chimatcha;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BranchActivity extends AppCompatActivity {

    private BranchAdapter adapter;
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

        // Home — close dropdown
        menuHome.setOnClickListener(v -> {
            closeDropdown();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // Menu — TODO: navigate to menu page
        menuItems.setOnClickListener(v -> {
            closeDropdown();
            Intent intent = new Intent(this, ProductActivity.class);
            startActivity(intent);
        });

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

        setContentView(R.layout.activity_branch);

        setupDropdown();

        noResultsText = findViewById(R.id.noResultsText);

        setupRecycler();
        setupSearch();
    }

    private void setupRecycler() {
        RecyclerView recyclerView = findViewById(R.id.branchRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Branch> branches = new ArrayList<>();
        branches.add(new Branch(
            "Chi Matcha @ Menteng",
            "Jl. Latuharhary No.9, RT.11/RW.7, Menteng, Kota Jakarta Pusat, Daerah Khusus Ibukota Jakarta 10310",
            "9:00 AM - 9:00 PM",
            R.drawable.branch_1,
            -6.1991, 106.8318
        ));
        branches.add(new Branch(
            "Chi Matcha @ Tokyo",
            "4 Chome-3-13 Jingumae, Shibuya, Tokyo 150-0001, Japan",
            "9:00 AM - 9:00 PM",
            R.drawable.branch_2,
            35.6687, 139.7048
        ));
        branches.add(new Branch(
            "Chi Matcha @ BSD",
            "BSD, Jl. BSD Green Office Park Jl. BSD Grand Boulevard, Sampora, Kec. Cisauk, Kabupaten Tangerang, Banten 15345",
            "9:00 AM - 9:00 PM",
            R.drawable.branch_3,
            -6.3014, 106.6499
        ));

        adapter = new BranchAdapter(this, branches);
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
