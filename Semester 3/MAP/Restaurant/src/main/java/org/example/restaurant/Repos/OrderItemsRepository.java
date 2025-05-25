package org.example.restaurant.Repos;

import org.example.restaurant.models.MenuItem;
import org.example.restaurant.models.Order;
import org.example.restaurant.models.OrderItems;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderItemsRepository {
    private final String url;
    private final String username;
    private final String password;
    public OrderItemsRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private OrderItems createOrderItems(ResultSet rs) throws SQLException {
        int orderId = rs.getInt("order_id");
        int menuItemId = rs.getInt("menu_item_id");
        return new OrderItems(orderId, menuItemId);
    }

    public Optional<OrderItems> find(int orderId, int menuItemId) {
        String sql = "SELECT * FROM order_items WHERE order_id=? AND menu_item_id=?";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            statement.setInt(2, menuItemId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createOrderItems(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<OrderItems> findAll() {
        List<OrderItems> orderItemsList = new ArrayList<>();
        String sql = "SELECT * FROM order_items";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                orderItemsList.add(createOrderItems(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderItemsList;
    }

    public boolean save(OrderItems entity) {
        String sql = "INSERT INTO order_items (order_id, menu_item_id) VALUES (?, ?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entity.getOrderId());
            statement.setInt(2, entity.getMenuItemId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
