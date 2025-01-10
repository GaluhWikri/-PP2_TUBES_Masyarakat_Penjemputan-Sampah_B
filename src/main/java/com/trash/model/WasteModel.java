package com.trash.model;



public class WasteModel {
    private int id;
    private int categoryId;
    private String category;
    private String description;

    // Constructor
    public WasteModel(int id, String category, String description) {
        this.id = id;
        this.category = category;
        this.description = description;
    }

    public WasteModel(int categoryId, String description) {
        this.categoryId = categoryId;  // Inisialisasi categoryId
        this.description = description; // Inisialisasi description
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

