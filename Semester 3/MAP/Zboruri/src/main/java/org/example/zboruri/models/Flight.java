package org.example.zboruri.models;

import java.time.LocalDateTime;

public class Flight extends Entity<Integer>{
    private String from;
    private String to;
    private LocalDateTime departureTime;
    private LocalDateTime landingTime;
    private int seats;

    public Flight(String from, String to, LocalDateTime departureTime, LocalDateTime landingTime, int seats) {
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.landingTime = landingTime;
        this.seats = seats;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getLandingTime() {
        return landingTime;
    }

    public int getSeats() {
        return seats;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setLandingTime(LocalDateTime landingTime) {
        this.landingTime = landingTime;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
