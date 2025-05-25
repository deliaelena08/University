package org.example.anar.repos;


import org.example.anar.models.River;
import org.example.anar.validations.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RiverRepository implements IRepository<Integer, River> {
    private final Validator<River> validator;
    private final String url;
    private final String username;
    private final String password;

    public RiverRepository(Validator<River> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private River createRiver(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int length = rs.getInt("length");
        River river = new River(name, length);
        river.setId(id);
        return river;
    }

    @Override
    public Optional<River> find(Integer id) {
        String sql = "SELECT * FROM river WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(createRiver(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<River> findAll() {
        List<River> rivers = new ArrayList<>();
        String sql = "SELECT * FROM river";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                rivers.add(createRiver(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rivers;
    }

    @Override
    public Optional<River> save(River entity) {
        validator.validate(entity);
        String sql = "INSERT INTO river (name, length) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getLength());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(1));
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<River> update(River entity) {
        validator.validate(entity);
        String sql = "UPDATE river SET name = ?, length = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getLength());
            statement.setInt(3, entity.getId());
            if (statement.executeUpdate() > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }


}
