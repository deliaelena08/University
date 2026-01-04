package org.example.restaurant.Repos;

import org.example.restaurant.models.MenuItem;
import org.example.restaurant.Validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuItemRepository implements IRepository<Integer, MenuItem> {
    private final Validator<MenuItem> validator;
    private final String url;
    private final String username;
    private final String password;

    public MenuItemRepository(Validator<MenuItem> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private MenuItem createMenuItem(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String category = rs.getString("category");
        String item = rs.getString("item");
        float price = rs.getFloat("price");
        String currency = rs.getString("currency");
        MenuItem menuItem = new MenuItem(category, item, price, currency);
        menuItem.setId(id);
        return menuItem;
    }

    @Override
    public Optional<MenuItem> find(Integer id) {
        String sql = "SELECT * FROM menu_items WHERE id=?";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createMenuItem(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<MenuItem> findAll() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                menuItems.add(createMenuItem(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return menuItems;
    }

    @Override
    public Optional<MenuItem> save(MenuItem entity) {
        validator.validate(entity);
        String sql = "INSERT INTO menu_items (category, item, price, currency) VALUES (?, ?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getCategory());
            statement.setString(2, entity.getItem());
            statement.setFloat(3, entity.getPrice());
            statement.setString(4, entity.getCurrency());
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
    public Optional<MenuItem> update(MenuItem entity) {
        validator.validate(entity);
        String sql = "UPDATE menu_items SET category=?, item=?, price=?, currency=? WHERE id=?";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getCategory());
            statement.setString(2, entity.getItem());
            statement.setFloat(3, entity.getPrice());
            statement.setString(4, entity.getCurrency());
            statement.setInt(5, entity.getId());
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