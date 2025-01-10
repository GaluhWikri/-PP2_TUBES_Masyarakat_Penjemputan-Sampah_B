
package com.trash.model;


public class PickupHistory {
    private int id;
    private int wasteId;
    private String pickupDate;
    private String status;
    private String details;

    public PickupHistory(int id, int wasteId, String pickupDate, String status, String details) {
        this.id = id;
        this.wasteId = wasteId;
        this.pickupDate = pickupDate;
        this.status = status;
        this.details = details;
    }

    public PickupHistory(int i, int i0, String string, String pending) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Getter dan setter
    public int getId() {
        return id;
    }

    public int getWasteId() {
        return wasteId;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public String getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }
}



