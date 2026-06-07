package com.example.chimatcha;

public class Product {
    public String name;
    public String description;
    public String price;
    public int imageRes;
    public int reviews;

    public Product(String name, String description, String price, int imageRes, int reviews) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageRes = imageRes;
        this.reviews = reviews;
    }
}
