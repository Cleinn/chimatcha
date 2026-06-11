package com.example.chimatcha;

public class Branch {
    private String name;
    private String address;
    private String hours;
    private int imageResId;
    private double latitude;
    private double longitude;

    public Branch(String name, String address, String hours, int imageResId, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.hours = hours;
        this.imageResId = imageResId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getHours() { return hours; }
    public int getImageResId() { return imageResId; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public boolean matchesQuery(String query) {
        String q = query.toLowerCase().trim();
        return name.toLowerCase().contains(q)
            || address.toLowerCase().contains(q)
            || hours.toLowerCase().contains(q);
    }
}
