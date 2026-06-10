package com.example.chimatcha;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProductDetailActivity extends AppCompatActivity {

    private int quantity = 1;
    private EditText quantityText;

    private String selectedSweet = "Normal Sweet";
    private String selectedIce   = "Normal Ice";

    public static final String EXTRA_NAME    = "product_name";
    public static final String EXTRA_DESC    = "product_desc";
    public static final String EXTRA_PRICE   = "product_price";
    public static final String EXTRA_IMAGE   = "product_image";
    public static final String EXTRA_REVIEWS = "product_reviews";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_product_detail);

        String name  = getIntent().getStringExtra(EXTRA_NAME);
        String desc  = getIntent().getStringExtra(EXTRA_DESC);
        String price = getIntent().getStringExtra(EXTRA_PRICE);
        int imageRes = getIntent().getIntExtra(EXTRA_IMAGE, R.drawable.ic_chimatcha_logo);
        int reviews  = getIntent().getIntExtra(EXTRA_REVIEWS, 0);

        ImageView   detailImage  = findViewById(R.id.detailImage);
        TextView    detailName   = findViewById(R.id.detailName);
        TextView    detailPrice  = findViewById(R.id.detailPrice);
        TextView    detailDesc   = findViewById(R.id.detailDesc);
        TextView    detailReviews= findViewById(R.id.detailReviews);
        quantityText             = findViewById(R.id.quantityText);
        TextView    btnMinus     = findViewById(R.id.btnMinus);
        TextView    btnPlus      = findViewById(R.id.btnPlus);
        Button      addToCartBtn = findViewById(R.id.addToCartBtn);
        ImageButton backButton   = findViewById(R.id.backButton);

        LinearLayout sweetLayout = findViewById(R.id.sweetLevelLayout);
        TextView     sweetText   = findViewById(R.id.sweetLevelText);
        LinearLayout iceLayout   = findViewById(R.id.iceLevelLayout);
        TextView     iceText     = findViewById(R.id.iceLevelText);

        detailImage.setImageResource(imageRes);
        detailName.setText(name);
        detailPrice.setText(price);
        detailDesc.setText(desc);
        detailReviews.setText("(" + String.format("%,d", reviews) + ")");

        // Sweet level dropdown
        String[] sweetOptions = {"Normal Sweet", "Less Sweet", "Half Sweet", "No Sugar"};
        sweetLayout.setOnClickListener(v ->
            showDropdown(sweetLayout, sweetText, sweetOptions, chosen -> {
                selectedSweet = chosen;
                sweetText.setText(chosen);
                sweetText.setTextColor(0xFF333333);
            })
        );

        // Ice level dropdown
        String[] iceOptions = {"Normal Ice", "Less Ice", "No Ice", "Extra Ice"};
        iceLayout.setOnClickListener(v ->
            showDropdown(iceLayout, iceText, iceOptions, chosen -> {
                selectedIce = chosen;
                iceText.setText(chosen);
                iceText.setTextColor(0xFF333333);
            })
        );

        // Sync quantity int whenever the user edits the field directly
        quantityText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String val = s.toString().trim();
                if (!val.isEmpty()) {
                    try { quantity = Integer.parseInt(val); }
                    catch (NumberFormatException e) { quantity = 0; }
                }
            }
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
                quantityText.setSelection(quantityText.getText().length());
            }
        });

        btnPlus.setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
            quantityText.setSelection(quantityText.getText().length());
        });

        addToCartBtn.setOnClickListener(v -> {
            String raw = quantityText.getText().toString().trim();
            int parsedQty = 0;
            if (!raw.isEmpty()) {
                try { parsedQty = Integer.parseInt(raw); } catch (NumberFormatException ignored) {}
            }
            if (parsedQty <= 0) {
                new MaterialAlertDialogBuilder(this)
                    .setTitle("Invalid Quantity")
                    .setMessage("Please enter a quantity of at least 1 before ordering.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        quantityText.setText("1");
                        quantity = 1;
                        quantityText.requestFocus();
                    })
                    .show();
                return;
            }
            quantity = parsedQty;
            new MaterialAlertDialogBuilder(this)
                .setTitle("Your order of " + quantity + "x " + name + " has been placed!")
                .setMessage("A confirmation email has been sent to your registered address.")
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(this, ProductActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                })
                .show();
        });

        backButton.setOnClickListener(v -> finish());
    }

    interface OnOptionSelected {
        void onSelected(String option);
    }

    private void showDropdown(View anchor, TextView selectedView,
                              String[] options, OnOptionSelected callback) {

        View popupView = LayoutInflater.from(this).inflate(R.layout.dropdown_popup, null);

        TextView headerText = popupView.findViewById(R.id.popupSelectedText);
        ListView listView   = popupView.findViewById(R.id.popupList);

        headerText.setText(selectedView.getText());

        // Measure total height: header (48dp) + divider (1dp) + items (56dp each)
        float density    = getResources().getDisplayMetrics().density;
        int   headerH    = (int)(49 * density);   // 48dp header + 1dp divider
        int   itemH      = (int)(56 * density);
        int   totalH     = headerH + itemH * options.length;
        int   anchorW    = anchor.getWidth();

        PopupWindow popup = new PopupWindow(popupView,
            anchorW, totalH, true);
        popup.setElevation(12 * density);
        popup.setOutsideTouchable(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this, R.layout.spinner_dropdown_item, options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            callback.onSelected(options[position]);
            popup.dismiss();
        });

        // Show aligned to the anchor's top-left
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        popup.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0], location[1]);
    }
}
