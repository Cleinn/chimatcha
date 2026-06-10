package com.example.chimatcha;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 carouselViewPager;
    private ImageButton hamburgerButton;
    private LinearLayout dotsIndicator;
    private List<Integer> carouselImages;
    private Handler autoSlideHandler;
    private Runnable autoSlideRunnable;
    private LinearLayout dropdownMenu;
    private boolean isDropdownOpen = false;
    private static final int AUTO_SLIDE_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_home);

        // Welcome message
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome, " + AppGlobals.loggedInUsername + "!");

        setupDropdown();
        setupCarousel();
        setupBestSelling();
        setupReputation();  // <-- NEW
    }

    private void setupDropdown() {
        hamburgerButton = findViewById(R.id.hamburgerButton);
        dropdownMenu = findViewById(R.id.dropdownMenu);
        LinearLayout menuHome = findViewById(R.id.menuHome);
        LinearLayout menuItems = findViewById(R.id.menuItems);
        LinearLayout menuBranch = findViewById(R.id.menuBranch);
        LinearLayout menuLogout = findViewById(R.id.menuLogout);

        hamburgerButton.setOnClickListener(v -> {
            if (isDropdownOpen) {
                closeDropdown();
            } else {
                openDropdown();
            }
        });

        menuHome.setOnClickListener(v -> closeDropdown());

        menuItems.setOnClickListener(v -> {
            closeDropdown();
            Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
            startActivity(intent);
        });

        menuBranch.setOnClickListener(v -> {
            closeDropdown();
            Intent intent = new Intent(HomeActivity.this, BranchActivity.class);
            startActivity(intent);
        });

        menuLogout.setOnClickListener(v -> {
            closeDropdown();
            AppGlobals.loggedInUsername = "";
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Close dropdown when touching outside of it
        // Use mainScroll (NestedScrollView) since it covers the full screen
        NestedScrollView mainScroll = findViewById(R.id.mainScroll);
        mainScroll.setOnTouchListener((v, event) -> {
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

    private void setupCarousel() {
        carouselViewPager = findViewById(R.id.carouselViewPager);
        dotsIndicator = findViewById(R.id.dotsIndicator);

        carouselImages = new ArrayList<>();
        carouselImages.add(R.drawable.carousel_1);
        carouselImages.add(R.drawable.carousel_2);
        carouselImages.add(R.drawable.carousel_3);

        CarouselAdapter adapter = new CarouselAdapter(this, carouselImages);
        carouselViewPager.setAdapter(adapter);

        carouselViewPager.setPageTransformer((page, position) -> {
            float absPos = Math.abs(position);
            page.setAlpha(1 - absPos * 0.3f);
            page.setScaleY(1 - absPos * 0.05f);
        });

        setupDots(0);

        carouselViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setupDots(position);
            }
        });

        autoSlideHandler = new Handler(Looper.getMainLooper());
        autoSlideRunnable = new Runnable() {
            @Override
            public void run() {
                int next = (carouselViewPager.getCurrentItem() + 1) % carouselImages.size();
                carouselViewPager.setCurrentItem(next, true);
                autoSlideHandler.postDelayed(this, AUTO_SLIDE_DELAY);
            }
        };
        autoSlideHandler.postDelayed(autoSlideRunnable, AUTO_SLIDE_DELAY);
    }

    private void setupDots(int activeIndex) {
        dotsIndicator.removeAllViews();
        for (int i = 0; i < carouselImages.size(); i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(8), dpToPx(8));
            params.setMargins(dpToPx(4), 0, dpToPx(4), 0);
            dot.setLayoutParams(params);
            dot.setBackgroundResource(i == activeIndex ? R.drawable.dot_active : R.drawable.dot_inactive);
            dotsIndicator.addView(dot);
        }
    }

    private void setupBestSelling() {
        RecyclerView recyclerView = findViewById(R.id.bestSellingRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Product> products = new ArrayList<>();
        products.add(new Product("StrawMatcha", "A combination between milk matcha and strawberry fruit.", "Rp.25.000", R.drawable.product_strawmatcha, 23456));
        products.add(new Product("Mango Matcha", "A fresh taste of mango squash with pure matcha.", "Rp.23.000", R.drawable.product_mangomatcha, 32930));
        products.add(new Product("Matcha n Boba", "Standard matcha milk with toppings of boba.", "Rp.20.000", R.drawable.product_purematcha, 18200));
        products.add(new Product("Base Milk Matcha", "Smooth matcha with creamy milk.", "Rp.22.000", R.drawable.product_matchalatte, 27500));

        BestSellingAdapter adapter = new BestSellingAdapter(this, products);
        recyclerView.setAdapter(adapter);
    }

    // ===== NEW: Our Reputation Section =====
    private void setupReputation() {
        RecyclerView recyclerView = findViewById(R.id.reputationRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<ReviewAdapter.Review> reviews = new ArrayList<>();
        reviews.add(new ReviewAdapter.Review("Akila", "2w", 5, "So goooood"));
        reviews.add(new ReviewAdapter.Review("Mia", "3w", 5, "Very refreshing! The Mango Matcha is my absolute favorite."));
        reviews.add(new ReviewAdapter.Review("Rafi", "1mo", 4, "Great taste and fast delivery. Will order again!"));
        reviews.add(new ReviewAdapter.Review("Sari", "1mo", 5, "Best matcha drink I've ever had. Highly recommended!"));
        reviews.add(new ReviewAdapter.Review("Dion", "2mo", 4, "Love the StrawMatcha combo. Really unique flavor."));

        ReviewAdapter adapter = new ReviewAdapter(this, reviews);
        recyclerView.setAdapter(adapter);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (autoSlideHandler != null) autoSlideHandler.removeCallbacks(autoSlideRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autoSlideHandler != null) autoSlideHandler.postDelayed(autoSlideRunnable, AUTO_SLIDE_DELAY);
    }
}
