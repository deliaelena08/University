package org.example.adoptii.repos;

import org.example.adoptii.models.AdoptionCenter;
import org.example.adoptii.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdoptionCenterRepository implements IRepository<Integer, AdoptionCenter> {
    private final Validator<AdoptionCenter> validator;
    private final String url;
    private final String username;
    private final String password;

    public AdoptionCenterRepository(Validator<AdoptionCenter> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<AdoptionCenter> find(Integer id) {
        String query = "SELECT * FROM adoption_centers WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToAdoptionCenter(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<AdoptionCenter> findAll() {
        List<AdoptionCenter> adoptionCenters = new ArrayList<>();
        String query = "SELECT * FROM adoption_centers";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                adoptionCenters.add(mapToAdoptionCenter(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adoptionCenters;
    }

    @Override
    public Optional<AdoptionCenter> save(AdoptionCenter entity) {
        validator.validate(entity);
        String query = "INSERT INTO adoption_centers (name, location, capacity) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getLocation());
            statement.setInt(3, entity.getCapacity());
            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<AdoptionCenter> update(AdoptionCenter entity) {
        validator.validate(entity);
        String query = "UPDATE adoption_centers SET name = ?, location = ?, capacity = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getLocation());
            statement.setInt(3, entity.getCapacity());
            statement.setInt(4, entity.getId());
            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private AdoptionCenter mapToAdoptionCenter(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String location = resultSet.getString("location");
        int capacity = resultSet.getInt("capacity");
        AdoptionCenter adoptionCenter = new AdoptionCenter(name, location, capacity);
        adoptionCenter.setId(id);
        return adoptionCenter;
    }
}

