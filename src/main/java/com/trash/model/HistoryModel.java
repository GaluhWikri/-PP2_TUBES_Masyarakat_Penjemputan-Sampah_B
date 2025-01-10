package com.trash.model;



import java.sql.Date;

public class HistoryModel {
    private int id;
    private String wasteCategory;
    private String description;
    private double weight;
    private Date requestDate;
    private String address;

    public HistoryModel(int id, String wasteCategory, String description, double weight, Date requestDate, String address) {
        this.id = id;
        this.wasteCategory = wasteCategory;
        this.description = description;
        this.weight = weight;
        this.requestDate = requestDate;
        this.address = address;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getWasteCategory() {
        return wasteCategory;
    }

    public String getDescription() {
        return description;
    }

    public double getWeight() {
        return weight;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public String getAddress() {
        return address;
    }
}
