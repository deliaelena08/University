package org.example.restaurant.Repos;

import org.example.restaurant.models.Order;
import org.example.restaurant.models.Status;
import org.example.restaurant.Validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository implements IRepository<Integer, Order> {
    private final Validator<Order> validator;
    private final String url;
    private final String username;
    private final String password;

    public OrderRepository(Validator<Order> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Order createOrder(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int tableId = rs.getInt("table_id");
        LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
        Status status = Status.valueOf(rs.getString("status"));
        Order order = new Order(tableId, date, status);
        order.setId(id);
        return order;
    }

    @Override
    public Optional<Order> find(Integer id) {
        String sql = "SELECT * FROM orders WHERE id=?";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createOrder(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                orders.add(createOrder(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public Optional<Order> save(Order entity) {
        validator.validate(entity);
        String sql = "INSERT INTO orders (table_id, date, status) VALUES (?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entity.getTableId());
            statement.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
            statement.setString(3, entity.getStatus().name());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                var generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
    @Override
    public Optional<Order> update(Order entity) {
        validator.validate(entity);
        String sql = "UPDATE orders SET table_id=?, date=?, status=? WHERE id=?";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entity.getTableId());
            statement.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
            statement.setString(3, entity.getStatus().name());
            statement.setInt(4, entity.getId());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
