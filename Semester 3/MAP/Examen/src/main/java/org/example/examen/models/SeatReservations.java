package org.example.examen.models;

public class SeatReservations extends Entity<Integer> {
    private String userEmail;
    private Integer showId;

    public SeatReservations(String userEmail, Integer showId) {
        this.userEmail = userEmail;
        this.showId = showId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getShowId() {
        return showId;
    }

    public void setShowId(Integer showId) {
        this.showId = showId;
    }
}
