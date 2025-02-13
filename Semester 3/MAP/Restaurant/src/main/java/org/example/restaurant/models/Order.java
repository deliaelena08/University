package org.example.restaurant.models;

import java.time.LocalDateTime;
import java.util.Date;

public class Order extends Entity<Integer>{
    private Integer tableId;
    private LocalDateTime date;
    private Status status;

    public Order(Integer tableId, LocalDateTime date, Status status) {
        this.tableId = tableId;
        this.date = date;
        this.status = status;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + getId() +
                ", tableId=" + tableId +
                ", date=" + date +
                ", status=" + status +
                '}';
    }
}
