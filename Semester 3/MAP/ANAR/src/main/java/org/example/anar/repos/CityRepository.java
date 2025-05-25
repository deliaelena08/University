package org.example.anar.repos;


import org.example.anar.models.City;
import org.example.anar.validations.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CityRepository implements IRepository<Integer, City> {
    private final Validator<City> validator;
    private final String url;
    private final String username;
    private final String password;

    public CityRepository(Validator<City> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private City createCity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int river = rs.getInt("river");
        int cmdr = rs.getInt("cmdr");
        int cma = rs.getInt("cma");
        City city = new City(name, river, cmdr, cma);
        city.setId(id);
        return city;
    }

    @Override
    public Optional<City> find(Integer id) {
        String sql = "SELECT * FROM city WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(createCity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<City> findAll() {
        List<City> cities = new ArrayList<>();
        String sql = "SELECT * FROM city";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                cities.add(createCity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cities;
    }

    @Override
    public Optional<City> save(City entity) {
        validator.validate(entity);
        String sql = "INSERT INTO city (name, river, cmdr, cma) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getRiver());
            statement.setInt(3, entity.getCmdr());
            statement.setInt(4, entity.getCma());
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
    public Optional<City> update(City entity) {
        validator.validate(entity);
        String sql = "UPDATE city SET name = ?, river = ?, cmdr = ?, cma = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getRiver());
            statement.setInt(3, entity.getCmdr());
            statement.setInt(4, entity.getCma());
            statement.setInt(5, entity.getId());
            if (statement.executeUpdate() > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

}
