package com.example.chimatcha;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    private final Context context;
    private final List<Branch> allBranches;
    private List<Branch> filteredBranches;

    public BranchAdapter(Context context, List<Branch> branches) {
        this.context = context;
        this.allBranches = new ArrayList<>(branches);
        this.filteredBranches = new ArrayList<>(branches);
    }

    public void filter(String query) {
        filteredBranches.clear();
        if (query == null || query.trim().isEmpty()) {
            filteredBranches.addAll(allBranches);
        } else {
            for (Branch branch : allBranches) {
                if (branch.matchesQuery(query)) {
                    filteredBranches.add(branch);
                }
            }
        }
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return filteredBranches.isEmpty();
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_branch, parent, false);
        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
        Branch branch = filteredBranches.get(position);
        holder.branchName.setText(branch.getName());
        holder.branchAddress.setText(branch.getAddress());
        holder.branchHours.setText(branch.getHours());
        holder.branchImage.setImageResource(branch.getImageResId());

        holder.openMapsBtn.setOnClickListener(v -> {
            Uri uri = Uri.parse("geo:" + branch.getLatitude() + "," + branch.getLongitude()
                    + "?q=" + Uri.encode(branch.getName()));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                // Fallback: open in browser
                Uri webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query="
                        + branch.getLatitude() + "," + branch.getLongitude());
                context.startActivity(new Intent(Intent.ACTION_VIEW, webUri));
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredBranches.size();
    }

    static class BranchViewHolder extends RecyclerView.ViewHolder {
        ImageView branchImage;
        TextView branchName, branchAddress, branchHours, openMapsBtn;

        BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            branchImage = itemView.findViewById(R.id.branchImage);
            branchName = itemView.findViewById(R.id.branchName);
            branchAddress = itemView.findViewById(R.id.branchAddress);
            branchHours = itemView.findViewById(R.id.branchHours);
            openMapsBtn = itemView.findViewById(R.id.openMapsBtn);
        }
    }
}
