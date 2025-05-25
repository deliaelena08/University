package org.example.adoptii.repos;

import org.example.adoptii.models.Animal;
import org.example.adoptii.models.AnimalType;
import org.example.adoptii.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimalRepository implements IRepository<Integer, Animal> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Animal> validator;

    public AnimalRepository(Validator<Animal> validator, String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    private Animal createAnimal(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        Integer adoptionCenterId = rs.getInt("adoption_center_id");
        AnimalType type = AnimalType.valueOf(rs.getString("type"));
        Animal animal = new Animal(name, adoptionCenterId, type);
        animal.setId(id);
        return animal;
    }

    @Override
    public Optional<Animal> find(Integer id) {
        String sql = "SELECT * FROM animals WHERE id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createAnimal(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Animal> findAll() {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animals";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                animals.add(createAnimal(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animals;
    }

    @Override
    public Optional<Animal> save(Animal entity) {
        validator.validate(entity);
        String sql = "INSERT INTO animals (name, adoption_center_id, type) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getAdoptionCenterId());
            statement.setString(3, entity.getType().name());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Animal> update(Animal entity) {
        validator.validate(entity);
        String sql = "UPDATE animals SET name=?, adoption_center_id=?, type=? WHERE id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getAdoptionCenterId());
            statement.setString(3, entity.getType().name());
            statement.setInt(4, entity.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Animal> filterByType(AnimalType type) {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animals WHERE type::text = ?"; // Convertește type la text
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, type.name()); // Setează numele tipului ca string
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    animals.add(createAnimal(resultSet)); // Creează obiectele Animal
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animals;
    }

}
