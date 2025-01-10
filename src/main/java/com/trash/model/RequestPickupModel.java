package com.trash.model;

import java.sql.Date;

public class RequestPickupModel {
    private int categoryId;
    private String description;
    private double weight;
    private Date requestDate;  // Ubah menjadi java.sql.Date
    private String address;

    public RequestPickupModel(int categoryId, String description, double weight, Date requestDate, String address) {
        this.categoryId = categoryId;
        this.description = description;
        this.weight = weight;
        this.requestDate = requestDate;  // Ubah menjadi Date
        this.address = address;
    }

    // Getter dan Setter
    public int getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public double getWeight() {
        return weight;
    }

    public Date getRequestDate() {
        return requestDate;  // Kembalikan sebagai java.sql.Date
    }

    public String getAddress() {
        return address;
    }
}
