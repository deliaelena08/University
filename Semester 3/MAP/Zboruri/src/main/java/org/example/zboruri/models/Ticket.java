package org.example.zboruri.models;

import java.time.LocalDateTime;

public class Ticket extends Entity<Integer>{
    private String username;
    private int flightId;
    private LocalDateTime purchaseTime;

    public Ticket(String username, int flightId, LocalDateTime purchaseTime) {
        this.username = username;
        this.flightId = flightId;
        this.purchaseTime = purchaseTime;
    }

    public String getUsername() {
        return username;
    }

    public int getFlightId() {
        return flightId;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }
}
