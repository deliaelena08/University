package org.example.restaurant.models;


import org.example.restaurant.models.Tuple;

public class OrderItems extends Entity<Tuple<Integer, Integer>> {
    private Integer orderId;
    private Integer menuItemId;

    public OrderItems(Integer orderId, Integer menuItemId) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.setId(new Tuple<>(orderId, menuItemId)); // Setează ID-ul compus
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
        this.setId(new Tuple<>(orderId, menuItemId)); // Actualizează ID-ul compus
    }

    public Integer getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Integer menuItemId) {
        this.menuItemId = menuItemId;
        this.setId(new Tuple<>(orderId, menuItemId)); // Actualizează ID-ul compus
    }

    @Override
    public String toString() {
        return "OrderItems{" +
                "orderId=" + orderId +
                ", menuItemId=" + menuItemId +
                '}';
    }
}

