package org.example.restaurant.models;

public class Table extends Entity<Integer> {
    private int seats;

    public Table(int seats) {
        this.seats = seats;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + getId() +
                ", seats=" + seats +
                '}';
    }
}
