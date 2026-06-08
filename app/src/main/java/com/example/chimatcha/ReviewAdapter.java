package com.example.chimatcha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    public static class Review {
        public final String name;
        public final String timeAgo;
        public final int rating; // out of 5
        public final String text;

        public Review(String name, String timeAgo, int rating, String text) {
            this.name = name;
            this.timeAgo = timeAgo;
            this.rating = rating;
            this.text = text;
        }
    }

    private final Context context;
    private final List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.reviewerName.setText(review.name);
        holder.reviewTime.setText(review.timeAgo);
        holder.reviewText.setText(review.text);

        // Build star icons dynamically
        holder.starsContainer.removeAllViews();
        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                dpToPx(14), dpToPx(14)
            );
            params.setMargins(0, 0, dpToPx(2), 0);
            star.setLayoutParams(params);
            star.setImageResource(R.drawable.ic_star);
            star.setColorFilter(
                i <= review.rating
                    ? 0xFFFFC107  // gold
                    : 0xFFCCCCCC, // grey
                android.graphics.PorterDuff.Mode.SRC_IN
            );
            holder.starsContainer.addView(star);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewerName, reviewTime, reviewText;
        LinearLayout starsContainer;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewerName = itemView.findViewById(R.id.reviewerName);
            reviewTime = itemView.findViewById(R.id.reviewTime);
            reviewText = itemView.findViewById(R.id.reviewText);
            starsContainer = itemView.findViewById(R.id.starsContainer);
        }
    }
}
