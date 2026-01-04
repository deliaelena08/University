package org.example.ofertedevacanta.models;

import java.time.LocalDateTime;

public class Reservation extends Entity<Double> {
    private double clientId;
    private double hotelId;
    private LocalDateTime startDate;
    private int noNights;

    public Reservation(double clientId, double hotelId, LocalDateTime startDate, int noNights) {
        this.clientId = clientId;
        this.hotelId = hotelId;
        this.startDate = startDate;
        this.noNights = noNights;
    }

    public double getClientId() {
        return clientId;
    }

    public void setClientId(double clientId) {
        this.clientId = clientId;
    }

    public double getHotelId() {
        return hotelId;
    }

    public void setHotelId(double hotelId) {
        this.hotelId = hotelId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getNoNights() {
        return noNights;
    }

    public void setNoNights(int noNights) {
        this.noNights = noNights;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "clientId=" + clientId +
                ", hotelId=" + hotelId +
                ", startDate=" + startDate +
                ", noNights=" + noNights +
                '}';
    }
}
