package org.example.examen.models;

import java.time.LocalDateTime;

public class Show extends Entity<Integer> {
    private String name;
    private Integer durationMinutes;
    private LocalDateTime startDate;
    private Integer numberOfSeats;
    private LocalDateTime creationDate;

    public Show(String name, Integer durationMinutes, LocalDateTime startDate, Integer numberOfSeats, LocalDateTime creationDate) {
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.startDate = startDate;
        this.numberOfSeats = numberOfSeats;
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
