package com.example.chimatcha;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BranchActivity extends AppCompatActivity {

    private BranchAdapter adapter;
    private TextView noResultsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_branch);

        // Back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

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
