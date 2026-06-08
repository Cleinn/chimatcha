package com.example.chimatcha;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private final List<Product> allProducts;
    private List<Product> filteredProducts;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.allProducts = new ArrayList<>(products);
        this.filteredProducts = new ArrayList<>(products);
    }

    public void filter(String query) {
        filteredProducts.clear();
        if (query == null || query.trim().isEmpty()) {
            filteredProducts.addAll(allProducts);
        } else {
            String q = query.toLowerCase().trim();
            for (Product p : allProducts) {
                if (p.name.toLowerCase().contains(q)
                        || p.description.toLowerCase().contains(q)
                        || p.price.toLowerCase().contains(q)) {
                    filteredProducts.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return filteredProducts.isEmpty();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = filteredProducts.get(position);
        holder.productImage.setImageResource(product.imageRes);
        holder.productName.setText(product.name);
        holder.productDesc.setText(product.description);
        holder.productPrice.setText(product.price);
        holder.reviewCount.setText(String.valueOf(product.reviews));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_NAME, product.name);
            intent.putExtra(ProductDetailActivity.EXTRA_DESC, product.description);
            intent.putExtra(ProductDetailActivity.EXTRA_PRICE, product.price);
            intent.putExtra(ProductDetailActivity.EXTRA_IMAGE, product.imageRes);
            intent.putExtra(ProductDetailActivity.EXTRA_REVIEWS, product.reviews);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productDesc, productPrice, reviewCount;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productDesc = itemView.findViewById(R.id.productDesc);
            productPrice = itemView.findViewById(R.id.productPrice);
            reviewCount = itemView.findViewById(R.id.reviewCount);
        }
    }
}
