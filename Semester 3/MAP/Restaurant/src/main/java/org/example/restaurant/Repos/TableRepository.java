package org.example.restaurant.Repos;


import org.example.restaurant.models.Table;
import org.example.restaurant.Validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableRepository implements IRepository<Integer, Table> {
    private final Validator<Table> validator;
    private final String url;
    private final String username;
    private final String password;

    public TableRepository(Validator<Table> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Table createTable(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int seats = rs.getInt("seats");
        Table table = new Table(seats);
        table.setId(id);
        return table;
    }

    @Override
    public Optional<Table> find(Integer id) {
        String sql = "SELECT * FROM tables WHERE id=?";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createTable(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Table> findAll() {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM tables";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tables.add(createTable(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

    @Override
    public Optional<Table> save(Table entity) {
        validator.validate(entity);
        String sql = "INSERT INTO tables (seats) VALUES (?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entity.getSeats());
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
    public Optional<Table> update(Table entity) {
        validator.validate(entity);
        String sql = "UPDATE tables SET seats=? WHERE id=?";
        try (var connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entity.getSeats());
            statement.setInt(2, entity.getId());
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
