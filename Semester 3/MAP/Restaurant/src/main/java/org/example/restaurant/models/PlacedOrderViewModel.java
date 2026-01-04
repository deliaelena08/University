package org.example.restaurant.models;

import java.time.LocalDateTime;

public class PlacedOrderViewModel {
    private int tableId;
    private LocalDateTime date;
    private String items;

    public PlacedOrderViewModel(int tableId, LocalDateTime date, String items) {
        this.tableId = tableId;
        this.date = date;
        this.items = items;
    }

    // Getteri și setteri
    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }
}
